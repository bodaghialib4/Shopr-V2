package com.uwetrottmann.shopr.ui;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Color;

public class ColorPreferenceActivity extends AttributeValuePreferenceActivity {

	public static final String TAG = ColorPreferenceActivity.class
			.getSimpleName();

	@Override
	protected Attribute attribute() {
		return new Color();
	}

	@Override
	protected void onUpdatePreferencesFinish() {
		
	}

}
