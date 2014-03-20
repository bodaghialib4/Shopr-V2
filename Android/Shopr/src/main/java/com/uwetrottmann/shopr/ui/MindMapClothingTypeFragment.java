package com.uwetrottmann.shopr.ui;

import org.achartengine.GraphicalView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.uwetrottmann.shopr.R;

import de.greenrobot.event.EventBus;

/**
 * Shows a list of clothing items the user can critique by tapping an up or down
 * vote button.
 */
public class MindMapClothingTypeFragment extends Fragment {

	public static final String TAG = MindMapClothingTypeFragment.class.getSimpleName();
		
	private LinearLayout preferenceChart; 
	
	public enum Type {
	    CLOTHINGTYPE, COLOR, GENDER, PRICERANGE
	}

	public static MindMapClothingTypeFragment newInstance() {
		return new MindMapClothingTypeFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mind_map,
				container, false);
		
		preferenceChart = (LinearLayout) v.findViewById(R.id.preferenceChart);	
		preferenceChart.addView(BarChartView.getNewInstance(getActivity(), 200, 100));
		return v;
	}
	
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
