
package com.uwetrottmann.shopr.ui.basic;

import java.util.List;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.loaders.ShopLoader;
import com.uwetrottmann.shopr.model.ShoprShop;
import com.uwetrottmann.shopr.ui.ShopMapFragment;

public class RecommendationsShopMapBasic extends ShopMapFragment {

	  public static RecommendationsShopMapBasic newInstance() {
	        return new RecommendationsShopMapBasic();
	    }

	    protected final int loaderId(){
	    	return 22;
	    }

	    @Override
	    public Loader<List<ShoprShop>> onCreateLoader(int loaderId, Bundle args) {
	        return new ShopLoader(getActivity());
	    }

		@Override
		public boolean shouldDrawEmptyShops() {
			return true;
		}

		@Override
		protected String shopInfoOnMap(int numItemsAtShop) {
			return getString(R.string.has_x_recommendations, numItemsAtShop);
		}


}
