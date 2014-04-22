package com.uwetrottmann.shopr.ui.explanation;

import com.uwetrottmann.shopr.adapters.explanation.RecommendationsPager;
import com.uwetrottmann.shopr.adapters.explanation.SlidingTabStripPagerAdapter;

public class RecommendationsFragment extends PageSlidingTabStripFragment {

	public static RecommendationsFragment newInstance() {
		return new RecommendationsFragment();
	}

	@Override
	public SlidingTabStripPagerAdapter pagerAdapter() {
		return new RecommendationsPager(this);
	}
}
