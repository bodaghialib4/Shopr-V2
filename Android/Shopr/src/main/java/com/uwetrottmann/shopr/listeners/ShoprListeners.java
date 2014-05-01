package com.uwetrottmann.shopr.listeners;

import com.adiguzel.shopr.explanation.Recommendation;
import com.uwetrottmann.shopr.algorithm.model.Item;

public class ShoprListeners {
	public interface OnItemCritiqueListener {
		public void onItemCritique(Item item, boolean isLike);
	}

	public interface OnItemDisplayListener {
		public void onItemDisplay(Item item);
	}

	public interface OnItemFavouriteListener {
		public void onItemFavourite(Item item);
	}
	
	public interface OnRecommendationDisplayListener {
		public void onRecommendationDisplay(Recommendation recommendation);
	}
}
