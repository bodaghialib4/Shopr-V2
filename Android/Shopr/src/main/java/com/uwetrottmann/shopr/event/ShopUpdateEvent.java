package com.uwetrottmann.shopr.event;

import java.util.Map;

public class ShopUpdateEvent {
	/**
	 * Holds a list of shop ids and how many recommendations are shown for
	 * each shop.
	 */
	public Map<Integer, Integer> shopMap;
}