package com.uwetrottmann.shopr.model;

import android.support.v4.app.Fragment;

public class SectionItem {

	protected Fragment fragment;
	protected String title;
	
	public String getTitle() {
		return this.title;
	}

	public SectionItem title(String title) {
		this.title = title;
		return this;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public SectionItem fragment(Fragment fragment) {
		this.fragment = fragment;
		return this;
	}
}