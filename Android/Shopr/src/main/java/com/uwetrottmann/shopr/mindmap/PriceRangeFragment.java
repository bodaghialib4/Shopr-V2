package com.uwetrottmann.shopr.mindmap;

import android.view.View;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Price;

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
		// TODO Auto-generated method stub
		
	}
	
}
