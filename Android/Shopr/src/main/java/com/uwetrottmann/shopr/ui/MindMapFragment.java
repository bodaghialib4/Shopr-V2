package com.uwetrottmann.shopr.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.ui.MainActivity.LocationUpdateEvent;

import de.greenrobot.event.EventBus;

/**
 * Shows a list of clothing items the user can critique by tapping an up or down
 * vote button.
 */
public class MindMapFragment extends Fragment {

	public static final String TAG = "Mind Map";
	
	private TextView colorPreferenceView, clothingTypePreferenceView,
	sexPreferenceView, brandPreferenceView, priceRangePreferenceView;

	public static MindMapFragment newInstance() {
		return new MindMapFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mind_map,
				container, false);

		colorPreferenceView = (TextView) v.findViewById(R.id.textViewColorPreference);
		clothingTypePreferenceView = (TextView) v.findViewById(R.id.textViewClothingTypePreference);
		sexPreferenceView = (TextView) v.findViewById(R.id.textViewSexPreference);
		brandPreferenceView = (TextView) v.findViewById(R.id.textViewBrandPreference);
		priceRangePreferenceView = (TextView) v.findViewById(R.id.textViewPriceRangePreference);
		
		initViews();

		return v;
	}
	
	private void initViews() {
		initColorPreferenceSection();
		initClothingTypePreferenceSection();
		initSexPreferenceSection();
		initBrandPreferenceSection();
		initPriceRangePreferenceSection();
	}

	private void initColorPreferenceSection() {
		colorPreferenceView.setText("You are indifferent to colors");
	}
	
	private void initClothingTypePreferenceSection() {
		clothingTypePreferenceView.setText("TO DO");	
	}

	private void initSexPreferenceSection() {
		sexPreferenceView.setText("TO DO");
	}
	
	private void initBrandPreferenceSection() {
		brandPreferenceView.setText("TO DO");
	}
	
	private void initPriceRangePreferenceSection() {
		priceRangePreferenceView.setText("TO DO");
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public void onStart() {
		super.onStart();
		//EventBus.getDefault().registerSticky(this, LocationUpdateEvent.class);
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
