package com.uwetrottmann.shopr.mindmap;

import android.view.View;

import com.uwetrottmann.shopr.algorithm.model.Price;

public class PriceRangeFragment extends MindMapFragment {

	@Override
	protected View getChartView() {
		return new DoughnutChart(getActivity(), new Price()).getView();
	}
	
}
