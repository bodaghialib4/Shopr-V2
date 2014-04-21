
package com.uwetrottmann.shopr.utils;

import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.uwetrottmann.androidutils.Lists;
import com.uwetrottmann.shopr.model.ShoprShop;
import com.uwetrottmann.shopr.provider.ShoprContract.Shops;

public class ShopUtils {

    public static List<ShoprShop> getShopsSamples() {
        List<ShoprShop> shops = Lists.newArrayList();

        shops.add(new ShoprShop().id(0).name("Armani Shop").location(new LatLng(48.249246, 11.64988)));
        shops.add(new ShoprShop().id(1).name("Hugo Shop").location(new LatLng(48.250878, 11.651548)));
        shops.add(new ShoprShop().id(2).name("Chanel Shop").location(new LatLng(48.249346, 11.651854)));
        shops.add(new ShoprShop().id(3).name("Dolce Shop").location(new LatLng(48.249403, 11.648378)));
        shops.add(new ShoprShop().id(4).name("Karl Shop").location(new LatLng(48.250353, 11.645679)));
        shops.add(new ShoprShop().id(5).name("Empty Shop").location(new LatLng(48.250403, 11.655056)));

        return shops;
    }
    
    public static List<ShoprShop> getShops(Context context) {
        final Cursor query = context.getContentResolver().query(Shops.CONTENT_URI,
                new String[] {
                        Shops._ID, Shops.NAME, Shops.LAT, Shops.LONG
                }, null, null,
                null);

        List<ShoprShop> shops = Lists.newArrayList();

        if (query != null) {
            while (query.moveToNext()) {
                ShoprShop shop = new ShoprShop();
                shop.id(query.getInt(0));
                shop.name(query.getString(1));
                shop.location(new LatLng(query.getDouble(2), query.getDouble(3)));
                shops.add(shop);
            }

            query.close();
        }

        return shops;
    }

}
