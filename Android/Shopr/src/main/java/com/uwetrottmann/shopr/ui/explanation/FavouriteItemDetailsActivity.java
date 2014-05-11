package com.uwetrottmann.shopr.ui.explanation;

import java.text.NumberFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.algorithm.model.Sex;
import com.uwetrottmann.shopr.utils.FavouriteItemUtils;
import com.uwetrottmann.shopr.utils.ValueConverter;
import com.viewpagerindicator.CirclePageIndicator;

public class FavouriteItemDetailsActivity extends Activity {
	private Item item;

	public static class ItemDisplayHelper {
		public static Item item;
	}

	private class ImagePagerAdapter extends PagerAdapter {
		private Context context;

		public ImagePagerAdapter(Context context) {
			this.context = context;
		}

		public ImageView getImage(String url) {
			final ImageView image = new ImageView(context);
			image.setScaleType(ImageView.ScaleType.CENTER);
			// load picture
			Picasso.with(context)
					.load(url)
					.placeholder(null)
					.error(R.drawable.ic_action_tshirt)
					.resizeDimen(R.dimen.default_image_width,
							R.dimen.default_image_height).centerCrop()
					.into(image);
			return image;
		}

		@Override
		public void destroyItem(final ViewGroup container, final int position,
				final Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}

		@Override
		public int getCount() {
			return item.imageUrls().length;
		}

		@Override
		public Object instantiateItem(final ViewGroup container,
				final int position) {
			String imageUrl = item.imageUrls()[position];
			final ImageView imageView = getImage(imageUrl);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public boolean isViewFromObject(final View view, final Object object) {
			return view == ((ImageView) object);
		}
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.explanation_activity_fav_item_details);

		item = ItemDisplayHelper.item;
		if (item == null) {
			finish();
			return;
		}
		setTitle(item.name());
		setupViews();
	}

	private void setupViews() {
		setupImagePager();
		setupItemDetails();

		findViewById(R.id.deleteFavouriteButton).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						onFavouriteDelete();
					}
				});

	}

	private void setupImagePager() {
		final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		final ImagePagerAdapter adapter = new ImagePagerAdapter(this);
		viewPager.setAdapter(adapter);

		final CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		circleIndicator.setViewPager(viewPager);
	}
	
	private void setupItemDetails() {
		String clothingType = ValueConverter.getLocalizedStringForValue(this,
				item.attributes().getAttributeById(ClothingType.ID)
						.currentValue().descriptor());

		String gender = ValueConverter.getLocalizedStringForValue(this, item
				.attributes().getAttributeById(Sex.ID).currentValue()
				.descriptor());

		String color = ValueConverter.getLocalizedStringForValue(this, item
				.attributes().getAttributeById(Color.ID).currentValue()
				.descriptor());

		String price = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(
				item.price().doubleValue());

		TextView clothingTypeText = (TextView) findViewById(R.id.clothingType);
		TextView brandText = (TextView) findViewById(R.id.brand);
		TextView colorText = (TextView) findViewById(R.id.color);
		TextView priceText = (TextView) findViewById(R.id.price);
		TextView genderText = (TextView) findViewById(R.id.gender);

		clothingTypeText.setText(clothingType);
		brandText.setText(item.brand());
		colorText.setText(color);
		priceText.setText(price);
		genderText.setText(gender);
	}

	protected void onFavouriteDelete() {
		FavouriteItemUtils.remove(this, item);
		
		setResult(RESULT_OK);
		finish();
	}
}