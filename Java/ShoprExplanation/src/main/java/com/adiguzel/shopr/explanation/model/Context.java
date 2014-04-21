package com.adiguzel.shopr.explanation.model;

import java.util.List;

import com.uwetrottmann.shopr.algorithm.model.Item;

public abstract class Context {
	// threshold for explanation score 
	public static double ALPHA = 0.8;
	// threshold for information score 
	public static double GAMMA = 0.7;
	
	public abstract double explanationScore(Item item);
	public abstract double informationScore(Item item, List<Item> recommendations);
	
	public boolean isValidArgument(Item item, List<Item> recommendations) {
		return explanationScore(item) > ALPHA && informationScore(item, recommendations) > GAMMA;
	}
}
