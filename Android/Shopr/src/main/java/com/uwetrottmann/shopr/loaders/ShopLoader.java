
package com.uwetrottmann.shopr.loaders;

import java.util.List;

import android.content.Context;

import com.uwetrottmann.shopr.model.Shop;
import com.uwetrottmann.shopr.utils.ShopUtils;

public class ShopLoader extends Loader<List<Shop>> {

    public ShopLoader(Context context) {
        super(context);
    }

    @Override
    public List<Shop> loadInBackground() {
    	return ShopUtils.getShops(getContext());  
    }

}
