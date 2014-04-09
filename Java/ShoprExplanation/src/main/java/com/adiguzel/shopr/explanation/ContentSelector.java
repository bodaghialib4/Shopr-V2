package com.adiguzel.shopr.explanation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.adiguzel.shopr.explanation.model.Argument;
import com.adiguzel.shopr.explanation.model.Dimension;
import com.adiguzel.shopr.explanation.model.Explanation;
import com.adiguzel.shopr.explanation.model.ScoreComputer;
import com.uwetrottmann.shopr.algorithm.Query;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Item;

public class ContentSelector {
	public static double ALPHA = 0.01;
	public static double BETA = 0.0;
	public static double GAMMA = 0.0;
	public static double MU = 0.0;

	public Explanation select(List<Item> recommendedItems, Item item,
			Query query) {
		assert (MU < ALPHA);
		Explanation explanation = new Explanation(item);
		List<Argument> sortedInitialArguments = generateSortedInitialArguments(
				item, query);
		Argument bestArgument = sortedInitialArguments.get(0);
		Argument secondBestArgument = sortedInitialArguments.get(1);
		explanation.addArgument(bestArgument);

		if (bestArgument.dimension().explanationScore() > ALPHA) {
			double informationScore = ScoreComputer.informationScore(item, query,
					bestArgument.dimension(), recommendedItems);
			// Dimension provides low information, attempt to add a supporting
			// argument
			if (informationScore < GAMMA
					&& secondBestArgument.dimension().explanationScore() > MU) {
				explanation.addArgument(secondBestArgument);
			}

		} else {
			// Item is good average
			if (ScoreComputer.globalScore(item, query) > BETA) {
			}
			// Recommender couldn't find better alternatives
			else {
			}
		}
		return explanation;

	}

	private List<Argument> generateSortedInitialArguments(Item item, Query query) {
		List<Argument> arguments = new ArrayList<Argument>();

		ScoreComputer score = new ScoreComputer();
		for (Attribute attribute : item.attributes().values()) {
			Dimension dimension = new Dimension(attribute);
			dimension.explanationScore(score.explanationScore(item, query,
					dimension));
			arguments.add(new Argument(dimension, true));
		}
		Collections.sort(arguments);
		return arguments;
	}

}
