package com.uwetrottmann.shopr.mindmap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;

import de.greenrobot.event.EventBus;

public abstract class MindMapFragment extends Fragment {
	
	protected LinearLayout preferenceChart; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mind_map,
				container, false);
		
		preferenceChart = (LinearLayout) v.findViewById(R.id.preferenceChart);	
		preferenceChart.addView(getChartView());
		preferenceChart.setOnClickListener(getOnClickListener());
		return v;
	}
	
	protected OnClickListener getOnClickListener() {
		return new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				onAttributePreferenceChangeRequested();
			}
		};
	}
	
	protected abstract Attribute attribute();
	protected abstract void onAttributePreferenceChangeRequested();
	protected abstract View getChartView();
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		EventBus.getDefault().unregister(this);
		super.onStop();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.item_list, menu);
	}
	
}
