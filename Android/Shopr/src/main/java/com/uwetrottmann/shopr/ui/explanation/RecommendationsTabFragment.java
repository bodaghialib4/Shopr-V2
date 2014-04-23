package com.uwetrottmann.shopr.ui.explanation;

import com.uwetrottmann.shopr.adapters.explanation.RecommendationsPager;
import com.uwetrottmann.shopr.adapters.explanation.SlidingTabStripPagerAdapter;

public class RecommendationsTabFragment extends PageSlidingTabStripFragment {

	public static RecommendationsTabFragment newInstance() {
		return new RecommendationsTabFragment();
	}

	@Override
	public SlidingTabStripPagerAdapter pagerAdapter() {
		return new RecommendationsPager(this);
	}
}
