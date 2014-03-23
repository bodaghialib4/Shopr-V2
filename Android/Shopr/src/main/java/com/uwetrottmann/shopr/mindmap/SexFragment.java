package com.uwetrottmann.shopr.mindmap;

import android.view.View;

import com.uwetrottmann.shopr.algorithm.model.Sex;

public class SexFragment extends MindMapFragment {

	@Override
	protected View getChartView() {
		return new BarChart(getActivity(), new Sex()).getView();
	}
	
}
