package com.adiguzel.shopr.explanation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.adiguzel.shopr.explanation.model.Argument;
import com.adiguzel.shopr.explanation.model.Argument.Type;
import com.adiguzel.shopr.explanation.model.Dimension;
import com.adiguzel.shopr.explanation.model.Explanation;
import com.adiguzel.shopr.explanation.model.ScoreComputer;
import com.uwetrottmann.shopr.algorithm.Query;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Item;

public class ContentSelector {
	// α - compares explanation score
	public static double ALPHA = 0.6;
	// µ - second criteria for explanation score (µ < α)
	public static double MU = 0.51;
	// β - compares global score
	public static double BETA = 0.6;
	// γ - compares information score
	public static double GAMMA = 0.6;

	public Explanation select(Item item, Query query,
			List<Item> recommendedItems) {

		Explanation explanation = new Explanation(item);
		List<Argument> sortedInitialArguments = generateSortedInitialArguments(
				item, query);

		List<Argument> primaryArguments = filterBy(sortedInitialArguments,
				new PrimaryArgumentFilter());

		if (primaryArguments.size() > 0) {
			explanation.addPrimaryArguments(primaryArguments);

			for (Argument arg : primaryArguments) {
				double informationScore = ScoreComputer.informationScore(item,
						query, arg.dimension(), recommendedItems);
				arg.dimension().informationScore(informationScore);
			}

			Argument mainArgument = primaryArguments.get(0);

			// Dimension provides low information, attempt to add a supporting
			// arguments
			if (mainArgument.dimension().informationScore() < GAMMA) {
				explanation.addSupportingArguments(filterBy(
						sortedInitialArguments, new SecondaryArgumentFilter()));
			}

		}
		// No dimension is larger than alpha(α), no argument can be selected
		else {
			// Item is only a good average
			if (ScoreComputer.globalScore(item, query) > BETA) {
				explanation.addSupportingArgument(new Argument(
						Type.GOOD_AVERAGE));

				explanation.addSupportingArguments(filterBy(
						sortedInitialArguments, new SecondaryArgumentFilter()));
			}
			// Recommender couldn't find better alternatives
			else {
				explanation.addSupportingArgument(new Argument(
						Type.NO_BETTER_ALTERNATIVES));
			}
		}

		return explanation;
	}

	public Explanation selectLegacy(Item item, Query query,
			List<Item> recommendedItems) {

		Explanation explanation = new Explanation(item);
		List<Argument> sortedInitialArguments = generateSortedInitialArguments(
				item, query);
		Argument bestInitialArgument = sortedInitialArguments.get(0);
		Argument secondBestInitialArgument = sortedInitialArguments.get(1);

		// Dimension is good enough for a first argument
		if (bestInitialArgument.dimension().explanationScore() > ALPHA) {
			explanation.addPrimaryArgument(bestInitialArgument);

			double informationScore = ScoreComputer.informationScore(item,
					query, bestInitialArgument.dimension(), recommendedItems);
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
			if (ScoreComputer.globalScore(item, query) > BETA) {
				explanation.addSupportingArgument(new Argument(
						Type.GOOD_AVERAGE));
				if (secondBestInitialArgument.dimension().explanationScore() > MU) {
					explanation
							.addSupportingArgument(secondBestInitialArgument);
				}
			}
			// Recommender couldn't find better alternatives
			else {
				explanation.addSupportingArgument(new Argument(
						Type.NO_BETTER_ALTERNATIVES));
			}
		}

		return explanation;
	}

	private List<Argument> generateSortedInitialArguments(Item item, Query query) {
		List<Argument> arguments = new ArrayList<Argument>();

		for (Attribute attribute : item.attributes().values()) {
			Dimension dimension = new Dimension(attribute);
			dimension.explanationScore(ScoreComputer.explanationScore(item,
					query, dimension));
			arguments.add(new Argument(dimension, true));
		}
		sortDesc(arguments);
		/*System.out.println(item.name());
		for (Argument arg : arguments) {
			System.out.println(arg.dimension().explanationScore());
		}*/
		return arguments;
	}

	private void sortDesc(List<Argument> arguments) {
		Comparator<Argument> descComparator = new Comparator<Argument>() {

			@Override
			public int compare(Argument arg1, Argument arg2) {
				if (arg1.dimension().explanationScore() == arg2.dimension()
						.explanationScore()) {
					return 0;
				} else if (arg1.dimension().explanationScore() > arg2
						.dimension().explanationScore()) {
					return -1;
				}
				return 1;
			}

		};

		Collections.sort(arguments, descComparator);
	}

	private <T> List<T> filterBy(List<T> elems,
			Filter<T> filter) {
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

	public class SecondaryArgumentFilter implements Filter<Argument> {

		public final boolean applies(Argument arg) {
			return arg.dimension().explanationScore() > MU
					&& arg.dimension().explanationScore() <= ALPHA;
		}
	}

	public class PrimaryArgumentFilter implements Filter<Argument> {
		
		public final boolean applies(Argument arg) {
			return arg.dimension().explanationScore() > ALPHA;
		}
		
	}

}
