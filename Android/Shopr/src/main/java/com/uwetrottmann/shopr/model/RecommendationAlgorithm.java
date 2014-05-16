package com.uwetrottmann.shopr.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
import com.uwetrottmann.shopr.algorithm.model.Shop;
import com.uwetrottmann.shopr.provider.ShoprContract.Items;
import com.uwetrottmann.shopr.provider.ShoprContract.Shops;
import com.uwetrottmann.shopr.settings.AppSettings;
import com.uwetrottmann.shopr.utils.ShopUtils;
import com.uwetrottmann.shopr.utils.ShoprLocalizer;
import com.uwetrottmann.shopr.utils.Utils;
import com.uwetrottmann.shopr.utils.ValueConverter;

public class RecommendationAlgorithm {

	public static void restart(Context context) {

		AdaptiveSelection manager = AdaptiveSelection.get();

		manager.setLocalizationModule(new ShoprLocalizer(context));

		List<Item> caseBase = getItemsAtNearbyShops(context);
		manager.setInitialCaseBase(caseBase,
				AppSettings.isUsingDiversity(context));

		int maxRecommendations = AppSettings.getMaxRecommendations(context);
		AdaptiveSelection.get().setMaxRecommendations(maxRecommendations);
	}

	private static List<Item> getItemsAtNearbyShops(Context context) {
		List<Item> items = Lists.newArrayList();
		Map<Integer, ShoprShop> nearbyShops = ShopUtils.getNearbyShops(
				context);

		Cursor query = context.getContentResolver().query(
				Items.CONTENT_URI,
				new String[] { Items._ID, Items.CLOTHING_TYPE, Items.BRAND,
						Items.PRICE, Items.COLOR, Items.SEX, Shops.REF_SHOP_ID,
						Items.IMAGE_URLS, }, null, null, null);

		if (query != null) {
			while (query.moveToNext()) {
				int shopId = query.getInt(6);
				if (nearbyShops.get(shopId) != null) {
					Shop shop = nearbyShops.get(shopId);
					Item item = new Item();

					item.id(query.getInt(0));
					item.shop(shop);
					// name
					ClothingType type = new ClothingType(query.getString(1));
					String brand = query.getString(2);
					item.name(ValueConverter.getLocalizedStringForValue(
							context, type.currentValue().descriptor())
							+ " "
							+ brand);
					// price
					BigDecimal price = new BigDecimal(query.getDouble(3));
					item.price(price);
					item.brand(brand);
					// critiquable attributes
					item.attributes(new Attributes().putAttribute(type)
							.putAttribute(new Color(query.getString(4)))
							.putAttribute(new Price(price))
							.putAttribute(new Sex(query.getString(5))));

					item.imageUrls(Utils.extractUrls(query.getString(7)));

					items.add(item);

				}
			}

			query.close();
		}

		return items;
	}

}
