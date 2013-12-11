package com.uwetrottmann.shopr.utils;

import android.content.ContentValues;
import android.content.Context;

import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.provider.ShoprContract.Favourites;

public class FavouriteItemUtils {

	public static void addToFavourites(Context context, Item item) {
		ContentValues favItemValues = new ContentValues();
		favItemValues.put(Favourites.ITEM_ID, item.id());
		context.getContentResolver().insert(Favourites.CONTENT_URI,
				favItemValues);
	}
}
