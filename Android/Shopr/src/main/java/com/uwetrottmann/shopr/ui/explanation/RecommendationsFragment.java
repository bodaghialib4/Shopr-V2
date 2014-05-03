package com.uwetrottmann.shopr.ui.explanation;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adiguzel.shopr.explanation.Expositor;
import com.adiguzel.shopr.explanation.Recommendation;
import com.adiguzel.shopr.explanation.model.LocationContext;
import com.etsy.android.grid.StaggeredGridView;
import com.google.android.gms.maps.model.LatLng;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.adapters.ExplainedItemAdapter;
import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.Query;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.listeners.ShoprListeners.OnRecommendationDisplayListener;
import com.uwetrottmann.shopr.ui.ItemListFragment;
import com.uwetrottmann.shopr.ui.LocationHandler;
import com.uwetrottmann.shopr.utils.ShoprLocalizer;
import com.uwetrottmann.shopr.utils.ShoprTextFormatter;

/**
 * Shows a list of clothing items with explanations the user can critique by
 * tapping an up or down vote button.
 */
public class RecommendationsFragment extends
		ItemListFragment<Recommendation> implements OnRecommendationDisplayListener {

	private TextView mTextViewReason;
	private StaggeredGridView mGridView;

	private static RecommendationsFragment instance;

	public static RecommendationsFragment newInstance() {
		if (instance == null)
			instance = new RecommendationsFragment();

		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.explanation_fragment_item_list,
				container, false);

		mTextViewReason = (TextView) v
				.findViewById(R.id.textViewItemListReason);
		mGridView = (StaggeredGridView) v.findViewById(R.id.gridViewItemList);
		View emtpyView = v.findViewById(R.id.textViewItemListEmpty);
		mGridView.setEmptyView(emtpyView);

		return v;
	}

	private void updateReason() {
		Query currentQuery = AdaptiveSelection.get().getCurrentQuery();
		// Display current reason as explanatory text
		String reasonString = currentQuery.attributes().getReasonString();
		if (TextUtils.isEmpty(reasonString)) {
			mTextViewReason.setText(R.string.reason_empty);
		} else {
			mTextViewReason.setText(reasonString);
		}
	}

	@Override
	protected List<Recommendation> explainOrLeaveSame(List<Item> items) {
		List<com.adiguzel.shopr.explanation.model.Context> contexts = new ArrayList<com.adiguzel.shopr.explanation.model.Context>();
		LatLng location = LocationHandler.getInstance(getActivity())
				.getLastLocation();
		if (location != null) {
			contexts.add(new LocationContext(location.latitude,
					location.longitude));
		}
		return new Expositor(new ShoprLocalizer(getActivity()), new ShoprTextFormatter(this)).explain(items, AdaptiveSelection.get()
				.getCurrentQuery(), contexts);
	}

	@Override
	protected void afterUpdateItems() {
		updateReason();
	}

	@Override
	public ArrayAdapter<Recommendation> createAdapter() {
		return new ExplainedItemAdapter(getActivity(), this, this, this);
	}

	@Override
	public void setAdapter(ArrayAdapter<Recommendation> adapter) {
		mGridView.setAdapter(adapter);
	}
	
	@Override
	public void onRecommendationDisplay(Recommendation recommendation) {
		  // display details
        Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
        ItemDetailsActivity.RecommendationDisplayHelper.recommendation = recommendation;
        startActivity(intent);
	}
}
