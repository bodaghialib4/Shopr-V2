package com.adiguzel.shopr.explanation;

import com.adiguzel.shopr.explanation.model.Explanation;
import com.uwetrottmann.shopr.algorithm.model.Item;

public class Recommendation {
	private Item item;
	private Explanation explanation;
	
	public Recommendation(Item item, Explanation explanation) {
		this.item = item;
		this.explanation = explanation;
	}
	
	public Item item() {
		return item;
	}
	
	public Explanation explanation() {
		return explanation;
	}

}
