
package com.uwetrottmann.shopr.loaders;

import java.util.List;

import android.content.Context;

import com.uwetrottmann.shopr.model.ShoprShop;
import com.uwetrottmann.shopr.utils.ShopUtils;

public class ShopLoader extends Loader<List<ShoprShop>> {

    public ShopLoader(Context context) {
        super(context);
    }

    @Override
    public List<ShoprShop> loadInBackground() {
    	return ShopUtils.getShops(getContext());  
    }

}
