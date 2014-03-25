package com.uwetrottmann.shopr.ui;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Sex;

public class GenderPreferenceActivity extends AttributeValuePreferenceActivity {

	public static final String TAG = GenderPreferenceActivity.class
			.getSimpleName();
	protected int NUM_COLUMNS = 3;

	@Override
	protected Attribute attribute() {
		return new Sex();
	}

	@Override
	protected void onUpdatePreferencesFinish() {
		
	}
	
	@Override
	protected int numColums() {
		return 3;
	}

}
