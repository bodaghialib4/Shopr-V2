package com.uwetrottmann.shopr.loaders;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.model.RecommendationAlgorithm;

/**
 * Returns a list of items based on the current user model.
 */
public class ItemLoader extends Loader<List<Item>> {

	private static final String TAG = "ItemLoader";
	private LatLng mLocation;
	private boolean mIsInit;
	private Context mContext;
	
	public ItemLoader(Context context, LatLng location, boolean isInit) {
		super(context);
		mLocation = location;
		mIsInit = isInit;
		mContext = context;
	}

	@Override
	public List<Item> loadInBackground() {
		if (mLocation == null) {
			return new ArrayList<Item>();
		}

		AdaptiveSelection manager = AdaptiveSelection.get();
		
		// get initial case base
		if (mIsInit) {
			RecommendationAlgorithm.restart(mContext);
		}

		Log.d(TAG, "Fetching recommendations.");
		List<Item> recommendations = manager.getRecommendations();

		return recommendations;
	}

}
