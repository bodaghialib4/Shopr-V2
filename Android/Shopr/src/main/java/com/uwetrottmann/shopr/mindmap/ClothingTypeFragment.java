package com.uwetrottmann.shopr.mindmap;

import android.view.View;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.ui.explanation.ClothingTypePreferenceActivity;

public class ClothingTypeFragment extends MindMapFragment {

	@Override
	protected View getChartView() {
		return new PieChart(getActivity(), attribute()).getView();
	}

	@Override
	protected Attribute attribute() {
		return new ClothingType();
	}

	protected void onAttributePreferenceChangeRequested() {
		startActivity(ClothingTypePreferenceActivity.class);
	}
	
}
