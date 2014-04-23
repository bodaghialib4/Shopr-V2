
package com.uwetrottmann.shopr.ui.explanation;

import java.util.List;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.uwetrottmann.shopr.loaders.ShopLoader;
import com.uwetrottmann.shopr.model.ShoprShop;
import com.uwetrottmann.shopr.ui.ShopMapFragment;

public class FavouritesShopMap extends ShopMapFragment {

    public static FavouritesShopMap newInstance() {
        return new FavouritesShopMap();
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
		return false;
	}

	@Override
	protected String shopInfoOnMap(int numItemsAtShop) {
		return "Sells " + numItemsAtShop + " items you want." ;//getString(R.string.has_x_recommendations, numItemsAtShop);
	}

}
