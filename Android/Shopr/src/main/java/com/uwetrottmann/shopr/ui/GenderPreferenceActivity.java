package com.uwetrottmann.shopr.ui;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Sex;

public class GenderPreferenceActivity extends PreferenceGridActivity {

	public static final String TAG = GenderPreferenceActivity.class
			.getSimpleName();

	@Override
	protected Attribute attribute() {
		return new Sex();
	}
	
	@Override
	protected int numColums() {
		return 3;
	}

}
