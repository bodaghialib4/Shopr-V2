package com.uwetrottmann.shopr.mindmap;

import android.view.View;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Sex;
import com.uwetrottmann.shopr.ui.ColorPreferenceActivity;

public class SexFragment extends MindMapFragment {

	@Override
	protected View getChartView() {
		return new BarChart(getActivity(), attribute()).getView();
	}
	
	protected Attribute attribute() {
		return new Sex();
	}

	@Override
	protected void onAttributePreferenceChangeRequested() {
		startActivity(ColorPreferenceActivity.class);
	}
	
}
