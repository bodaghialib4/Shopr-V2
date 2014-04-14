package com.uwetrottmann.shopr.ui.explaination;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;

public class ClothingTypePreferenceActivity extends PreferenceGridActivity {

	public static final String TAG = ClothingTypePreferenceActivity.class
			.getSimpleName();

	@Override
	protected Attribute attribute() {
		return new ClothingType();
	}

}
