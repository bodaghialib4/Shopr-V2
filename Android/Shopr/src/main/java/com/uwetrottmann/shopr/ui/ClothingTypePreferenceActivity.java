package com.uwetrottmann.shopr.ui;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;

public class ClothingTypePreferenceActivity extends AttributeValuePreferenceActivity {

	public static final String TAG = ClothingTypePreferenceActivity.class
			.getSimpleName();

	@Override
	protected Attribute getAttribute() {
		return new ClothingType();
	}

	@Override
	protected void onUpdatePreferencesFinish() {
		
	}

}
