package com.uwetrottmann.shopr.model;

import android.support.v4.app.Fragment;

public class NavDrawerItem extends SectionItem {
	private int icon;
	
	public NavDrawerItem() {
		super();
	}
	
	public String getTitle() {
		return this.title;
	}

	public NavDrawerItem title(String title) {
		this.title = title;
		return this;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public NavDrawerItem fragment(Fragment fragment) {
		this.fragment = fragment;
		return this;
	}

	public int getIcon() {
		return this.icon;
	}

	public NavDrawerItem icon(int icon) {
		this.icon = icon;
		return this;
	}
}