package com.uwetrottmann.shopr.ui.explanation;

import com.uwetrottmann.shopr.adapters.explanation.FavouritesPager;
import com.uwetrottmann.shopr.adapters.explanation.SlidingTabStripPagerAdapter;

public class FavouritesFragment extends PageSlidingTabStripFragment {

	public static FavouritesFragment newInstance() {
		return new FavouritesFragment();
	}

	@Override
	public SlidingTabStripPagerAdapter pagerAdapter() {
		return new FavouritesPager(this);
	}
}
