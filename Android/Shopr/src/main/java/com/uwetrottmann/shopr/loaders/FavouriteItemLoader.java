package com.uwetrottmann.shopr.loaders;

import java.math.BigDecimal;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.uwetrottmann.androidutils.Lists;
import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.model.Attributes;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.algorithm.model.Price;
import com.uwetrottmann.shopr.algorithm.model.Sex;
import com.uwetrottmann.shopr.provider.ShoprContract.Favourites;
import com.uwetrottmann.shopr.provider.ShoprContract.Items;
import com.uwetrottmann.shopr.provider.ShoprContract.Shops;
import com.uwetrottmann.shopr.utils.ValueConverter;

/**
 * Returns a list of items based on the current user model.
 */
public class FavouriteItemLoader extends Loader<List<Item>> {

	// private static final String TAG = "FavouriteItemLoader";

	public FavouriteItemLoader(Context context) {
		super(context);
	}

	@Override
	public List<Item> loadInBackground() {
		return getFavourites();
	}

	private List<Item> getFavourites() {
		// List<Integer> itemIds = Lists.newArrayList();
		List<Item> favourites = Lists.newArrayList();
		Cursor query = getContext().getContentResolver().query(
				Favourites.CONTENT_URI, new String[] { Favourites.ITEM_ID },
				null, null, null);

		if (query != null) {
			while (query.moveToNext()) {
				Item item = getItemWithId(query.getInt(0));
				if (item != null)
					favourites.add(item);
			}
			query.close();
		}
		return favourites;
	}

	private Item getItemWithId(Integer id) {
		AdaptiveSelection adaptiveSelection = AdaptiveSelection.get();
		Item caseBaseItem = adaptiveSelection.getItem(id);
		if (caseBaseItem != null) {
			return caseBaseItem;
		} else {
			Item item = null;
			Cursor query = getContext().getContentResolver().query(
					Items.CONTENT_URI,
					new String[] { Items._ID, Items.CLOTHING_TYPE, Items.BRAND,
							Items.PRICE, Items.IMAGE_URL, Items.COLOR,
							Items.SEX, Shops.REF_SHOP_ID },
					Items._ID + "='" + id + "'", null, null);

			if (query != null) {
				if (query.moveToNext()) {
					item = new Item();

					item.id(query.getInt(0));
					item.image(query.getString(4));
					item.shopId(query.getInt(7));
					// name
					ClothingType type = new ClothingType(query.getString(1));
					String brand = query.getString(2);
					item.name(ValueConverter.getLocalizedStringForValue(
							getContext(), type.currentValue().descriptor())
							+ " " + brand);
					// price
					BigDecimal price = new BigDecimal(query.getDouble(3));
					item.price(price);
					// critiquable attributes
					item.attributes(new Attributes().putAttribute(type)
							.putAttribute(new Color(query.getString(5)))
							.putAttribute(new Price(price))
							.putAttribute(new Sex(query.getString(6))));

				}
				query.close();
			}

			return item;
		}
	}

}
