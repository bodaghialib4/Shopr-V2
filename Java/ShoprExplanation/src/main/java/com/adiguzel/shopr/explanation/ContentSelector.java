package com.adiguzel.shopr.explanation;

import com.adiguzel.shopr.explanation.model.Argument;
import com.adiguzel.shopr.explanation.model.Dimension;
import com.adiguzel.shopr.explanation.model.Explanation;
import com.adiguzel.shopr.explanation.model.Score;
import com.uwetrottmann.shopr.algorithm.Query;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Item;

public class ContentSelector {
	public static double ALPHA = 0.0;
	public static double BETA = 0.0;

	public Explanation selectContent(Item item, Query query) {

		Argument positiveArgument = positiveArgument(item, query);
		if (positiveArgument.dimension().explanationScore() > ALPHA) {

		} else {
			// Item is good average
			if(new Score().globalScore(item, query) > BETA) {
				
			}
			// Recommender couldn't find better alternatives
			else {
				
			}
		}
		return null;

	}

	private Argument positiveArgument(Item item, Query query) {
		Dimension bestPerformingDimension = null;
		Score score = new Score();
		for (Attribute attribute : item.attributes().values()) {
			Dimension dimension = new Dimension(attribute);
			dimension.explanationScore(score.explanationScore(item, query,
					dimension));
			if (dimension == null
					|| dimension.explanationScore() > bestPerformingDimension
							.explanationScore()) {
				bestPerformingDimension = dimension;
			}
		}
		return new Argument(bestPerformingDimension, true);
	}

}
