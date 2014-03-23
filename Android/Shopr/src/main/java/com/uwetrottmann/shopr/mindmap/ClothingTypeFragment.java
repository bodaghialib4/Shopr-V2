package com.uwetrottmann.shopr.mindmap;

import android.view.View;

import com.uwetrottmann.shopr.algorithm.model.ClothingType;

public class ClothingTypeFragment extends MindMapFragment {

	@Override
	protected View getChartView() {
		return new PieChart(getActivity(), new ClothingType()).getView();
	}	
}
