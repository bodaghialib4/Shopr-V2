package com.adiguzel.shopr.explanation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.adiguzel.shopr.explanation.model.Argument.Type;
import com.adiguzel.shopr.explanation.model.Context;
import com.adiguzel.shopr.explanation.model.ContextArgument;
import com.adiguzel.shopr.explanation.model.Dimension;
import com.adiguzel.shopr.explanation.model.DimensionArgument;
import com.adiguzel.shopr.explanation.model.Explanation;
import com.adiguzel.shopr.explanation.model.Valuator;
import com.uwetrottmann.shopr.algorithm.Query;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Item;

public class ContentSelector {
	// α - compares explanation score
	public static double ALPHA = 0.11;// 0.6;
	// µ - second criteria for explanation score (µ < α)
	public static double MU = 0.06;// 0.51;
	// β - compares global score
	public static double BETA = 0.6;
	// γ - compares information score
	public static double GAMMA = 0.5;

	public Explanation select(Item item, Query query,
			List<Item> recommendedItems, List<Context> contexts) {

		Explanation explanation = new Explanation(item);
		List<DimensionArgument> sortedInitialArguments = generateSortedInitialArguments(
				item, query, recommendedItems);

		List<DimensionArgument> strongPrimaryArguments = filterBy(
				sortedInitialArguments, new StrongPrimaryArgumentFilter());

		List<DimensionArgument> weakPrimaryArguments = filterBy(sortedInitialArguments,
				new WeakPrimaryArgumentFilter());
		
		// Select context arguments
		for(Context context : contexts) {
			if(context.isValidArgument(item, recommendedItems))
				explanation.addContextArgument(new ContextArgument(context, true));
		}
		
		if (strongPrimaryArguments.size() > 0) {
			explanation.addPrimaryArguments(strongPrimaryArguments);
			explanation.branch("Strong.");
		}
		// Dimension provides low information, attempt to add supporting
		// arguments
		else if (weakPrimaryArguments.size() > 0) {
			explanation.addPrimaryArguments(weakPrimaryArguments);
			explanation.addSupportingArguments(filterBy(sortedInitialArguments,
					new SecondaryArgumentFilter()));
			explanation.branch("Weak.");

		}
		// No dimension is larger than alpha(α), no argument can be selected
		else {
			// Item is only a good average
			if (Valuator.globalScore(item, query) > BETA) {
				explanation.addSupportingArgument(new DimensionArgument(
						Type.GOOD_AVERAGE));
				explanation.branch("Average.");

				explanation.addSupportingArguments(filterBy(
						sortedInitialArguments, new SecondaryArgumentFilter()));
			}
			// Recommender couldn't find better alternatives
			else {
				explanation.addSupportingArgument(new DimensionArgument(
						Type.SERENDIPITOUS));
				explanation.branch("No alternatives.");
			}
		}

		return explanation;
	}

	public Explanation selectLegacy(Item item, Query query,
			List<Item> recommendedItems) {

		Explanation explanation = new Explanation(item);
		List<DimensionArgument> sortedInitialArguments = generateSortedInitialArguments(
				item, query, recommendedItems);
		DimensionArgument bestInitialArgument = sortedInitialArguments.get(0);
		DimensionArgument secondBestInitialArgument = sortedInitialArguments.get(1);

		// Dimension is good enough for a first argument
		if (bestInitialArgument.dimension().explanationScore() > ALPHA) {
			explanation.addPrimaryArgument(bestInitialArgument);

			double informationScore = Valuator.informationScore(item, query,
					bestInitialArgument.dimension(), recommendedItems);
			bestInitialArgument.dimension().informationScore(informationScore);

			// Dimension provides low information, attempt to add a supporting
			// argument
			if (informationScore < GAMMA
					&& secondBestInitialArgument.dimension().explanationScore() > MU) {
				explanation.addSupportingArgument(secondBestInitialArgument);
			}
		}
		// No dimension is larger than alpha(α), no argument can be selected
		else {
			// Item is only a good average
			if (Valuator.globalScore(item, query) > BETA) {
				explanation.addSupportingArgument(new DimensionArgument(
						Type.GOOD_AVERAGE));
				if (secondBestInitialArgument.dimension().explanationScore() > MU) {
					explanation
							.addSupportingArgument(secondBestInitialArgument);
				}
			}
			// Recommender couldn't find better alternatives
			else {
				explanation.addSupportingArgument(new DimensionArgument(
						Type.SERENDIPITOUS));
			}
		}

		return explanation;
	}

	private List<DimensionArgument> generateSortedInitialArguments(Item item,
			Query query, List<Item> recommendedItems) {
		List<DimensionArgument> arguments = new ArrayList<DimensionArgument>();

		for (Attribute attribute : item.attributes().values()) {
			Dimension dimension = new Dimension(attribute);
			dimension.explanationScore(Valuator.explanationScore(item,
					query, dimension));
			dimension.informationScore(Valuator.informationScore(item, query,
					dimension, recommendedItems));
			arguments.add(new DimensionArgument(dimension, true));
		}
		sortDesc(arguments);
		return arguments;
	}

	private void sortDesc(List<DimensionArgument> arguments) {
		Comparator<DimensionArgument> descComparator = new Comparator<DimensionArgument>() {

			@Override
			public int compare(DimensionArgument arg1, DimensionArgument arg2) {

				if (arg1.dimension().hybridScore() == arg2.dimension()
						.hybridScore()) {
					return 0;
				} else if (arg1.dimension().hybridScore() > arg2.dimension()
						.hybridScore()) {
					return -1;
				}
				return 1;
			}

		};

		Collections.sort(arguments, descComparator);
	}

	private <T> List<T> filterBy(List<T> elems, Filter<T> filter) {
		List<T> filtered = new ArrayList<T>();
		for (T elem : elems) {
			if (filter.applies(elem))
				filtered.add(elem);
		}
		return filtered;
	}

	public interface Filter<T> {
		public boolean applies(T elem);
	}

	public class SecondaryArgumentFilter implements Filter<DimensionArgument> {

		public final boolean applies(DimensionArgument arg) {
			return arg.dimension().explanationScore() > MU
					&& arg.dimension().explanationScore() <= ALPHA;
		}
	}

	public class WeakPrimaryArgumentFilter implements Filter<DimensionArgument> {

		public final boolean applies(DimensionArgument arg) {
			return arg.dimension().explanationScore() > ALPHA;
		}

	}

	public class StrongPrimaryArgumentFilter implements Filter<DimensionArgument> {

		public final boolean applies(DimensionArgument arg) {
			return arg.dimension().explanationScore() > ALPHA
					&& arg.dimension().informationScore() > GAMMA;
		}

	}

}
