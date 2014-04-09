package com.adiguzel.shopr.explanation;

import java.util.ArrayList;
import java.util.List;

import com.adiguzel.shopr.explanation.model.Explanation;
import com.uwetrottmann.shopr.algorithm.Query;
import com.uwetrottmann.shopr.algorithm.model.Item;

public class Discloser {
	
	public List<Recommendation> explain(List<Item> recommendedItems, Query query) {
		List<Recommendation> explainedRecommendations = new ArrayList<Recommendation>();
		for(Item item: recommendedItems) {
			explainedRecommendations.add(new Recommendation(item, explain(item, query)));
		}
		return explainedRecommendations;
	}
	
	private Explanation explain(Item item, Query query) {
		Explanation abstractExplanation = selectContent(item, query);
		return generateSurface(abstractExplanation, query);
	}
	
	private Explanation selectContent(Item item, Query query) {
		return null;
	}
	
	private Explanation generateSurface(Explanation explanation, Query query) {
		return null;
	}
}
