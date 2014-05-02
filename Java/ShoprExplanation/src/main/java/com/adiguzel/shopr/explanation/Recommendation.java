package com.adiguzel.shopr.explanation;

import com.uwetrottmann.shopr.algorithm.model.Item;

public class Recommendation {
	private Item item;
	private CharSequence explanation;
	
	public Recommendation(Item item, CharSequence explanation) {
		this.item = item;
		this.explanation = explanation;
	}
	
	public Item item() {
		return item;
	}
	
	public CharSequence explanation() {
		return explanation;
	}

}
