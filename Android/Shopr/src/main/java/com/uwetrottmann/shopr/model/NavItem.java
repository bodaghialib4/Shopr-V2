package com.uwetrottmann.shopr.model;

import android.support.v4.app.Fragment;

public class NavItem extends SectionItem {
	private int icon;
	
	public NavItem() {
		super();
	}
	
	public String getTitle() {
		return this.title;
	}

	public NavItem title(String title) {
		this.title = title;
		return this;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public NavItem fragment(Fragment fragment) {
		this.fragment = fragment;
		return this;
	}

	public int getIcon() {
		return this.icon;
	}

	public NavItem icon(int icon) {
		this.icon = icon;
		return this;
	}
}