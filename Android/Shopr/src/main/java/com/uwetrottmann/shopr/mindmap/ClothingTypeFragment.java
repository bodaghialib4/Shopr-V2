package com.uwetrottmann.shopr.mindmap;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.ui.ClothingTypePreferenceActivity;

public class ClothingTypeFragment extends MindMapFragment {

	@Override
	protected View getChartView() {
		return new PieChart(getActivity(), new ClothingType()).getView();
	}	
	
	protected OnClickListener getOnClickListener() {
		return new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				onAttributeValueFavorRequested();
			}
		};
	}
	
	private void onAttributeValueFavorRequested() {
        Intent intent = new Intent(getActivity(), ClothingTypePreferenceActivity.class);
        startActivity(intent);
	}
}
