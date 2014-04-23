package com.uwetrottmann.shopr.adapters.explanation;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.uwetrottmann.shopr.model.SectionItem;

public abstract class SlidingTabStripPagerAdapter extends FragmentPagerAdapter {
	private List<SectionItem> fragmentSections;
	private Fragment fragment;
	

	public SlidingTabStripPagerAdapter(Fragment fragment) {
		super(fragment.getChildFragmentManager());
		this.fragment = fragment;
		this.fragmentSections = createFragmentSections();
	}
	
	protected abstract List<SectionItem> createFragmentSections();

	public List<SectionItem> getFragmentSections() {
		return fragmentSections;
	}
	
	protected Fragment getFragment() {
		return fragment;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return fragmentSections.get(position).getTitle();
	}

	@Override
	public int getCount() {
		return fragmentSections.size();
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentSections.get(position).getFragment();
	}
}
