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
		Argument bestInitialArgument = sortedInitialArguments.get(0);
		Argument secondBestInitialArgument = sortedInitialArguments.get(1);

		// Dimension is good enough for a first argument
		if (bestInitialArgument.dimension().explanationScore() > ALPHA) {
			explanation.addArgument(bestInitialArgument);

			double informationScore = ScoreComputer.informationScore(item,
					query, bestInitialArgument.dimension(), recommendedItems);
			bestInitialArgument.dimension().informationScore(informationScore);

			// Dimension provides low information, attempt to add a supporting
			// argument
			if (informationScore < GAMMA
					&& secondBestInitialArgument.dimension().explanationScore() > MU) {
				explanation.addArgument(secondBestInitialArgument);
			}
		}
		// No dimension is larger than alpha(α), no argument can be selected
		else {
			// Item is only a good average
			if (ScoreComputer.globalScore(item, query) > BETA) {
				explanation.addArgument(new Argument(Type.GOOD_AVERAGE));
				if (secondBestInitialArgument.dimension().explanationScore() > MU) {
					explanation.addArgument(secondBestInitialArgument);
				}
			}
			// Recommender couldn't find better alternatives
			else {
				explanation.addArgument(new Argument(
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
		System.out.println(item.name());
		for (Argument arg : arguments) {
			System.out.println(arg.dimension().explanationScore());
		}
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

}
