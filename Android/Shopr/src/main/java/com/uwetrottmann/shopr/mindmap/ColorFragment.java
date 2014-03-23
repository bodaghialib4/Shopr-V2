package com.uwetrottmann.shopr.mindmap;

import com.uwetrottmann.shopr.algorithm.model.Color;

import android.view.View;

public class ColorFragment extends MindMapFragment {

	@Override
	protected View getChartView() {
		return new PieChart(getActivity(), new Color()).getView();
	}

}
