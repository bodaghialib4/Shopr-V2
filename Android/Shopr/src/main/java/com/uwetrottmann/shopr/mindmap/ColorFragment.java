package com.uwetrottmann.shopr.mindmap;

import android.view.View;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.ui.explanation.ColorPreferenceActivity;

public class ColorFragment extends MindMapFragment {

	@Override
	protected View getChartView() {
		return new PieChart(getActivity(), attribute()).getView();
	}

	protected Attribute attribute() {
		return new Color();
	}

	@Override
	protected void onAttributePreferenceChangeRequested() {
		startActivity(ColorPreferenceActivity.class);
	}

}
