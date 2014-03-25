package com.uwetrottmann.shopr.mindmap;

import android.view.View;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Price;
import com.uwetrottmann.shopr.ui.PricePreferenceActivity;

public class PriceRangeFragment extends MindMapFragment {

	@Override
	protected View getChartView() {
		return new DoughnutChart(getActivity(), attribute()).getView();
	}
	
	protected Attribute attribute() {
		return new Price();
	}

	@Override
	protected void onAttributePreferenceChangeRequested() {
		startActivity(PricePreferenceActivity.class);
	}
	
}
