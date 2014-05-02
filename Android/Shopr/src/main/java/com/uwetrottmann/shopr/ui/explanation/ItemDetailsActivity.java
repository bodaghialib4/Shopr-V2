package com.uwetrottmann.shopr.ui.explanation;

import java.text.NumberFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adiguzel.shopr.explanation.Recommendation;
import com.adiguzel.shopr.explanation.model.Explanation;
import com.squareup.picasso.Picasso;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.algorithm.model.Sex;
import com.uwetrottmann.shopr.eval.ResultsActivity;
import com.uwetrottmann.shopr.eval.Statistics;
import com.uwetrottmann.shopr.provider.ShoprContract.Stats;
import com.uwetrottmann.shopr.utils.FavouriteItemUtils;
import com.uwetrottmann.shopr.utils.ValueConverter;
import com.viewpagerindicator.CirclePageIndicator;

public class ItemDetailsActivity extends Activity {
	private Recommendation recommendation;

	public static class RecommendationDisplayHelper {
		public static Recommendation recommendation;
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
			return 2;
		}

		@Override
		public Object instantiateItem(final ViewGroup container,
				final int position) {
			final ImageView imageView = getImage(recommendation.item().image());
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
		setContentView(R.layout.explanation_activity_item_details);

		recommendation = RecommendationDisplayHelper.recommendation;
		if (recommendation == null) {
			finish();
			return;
		}

		setupViews();
	}

	private void setupViews() {
		setupImagePager();
		setupExplanation();
		setupItemDetails();

		findViewById(R.id.saveItemButton).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						onFinishTask();
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

	private void setupExplanation() {
		Explanation explanation = recommendation.explanation();
		LinearLayout explanations = (LinearLayout) findViewById(R.id.explanations);
		
		for(CharSequence reason: explanation.positiveReasons()) {
			explanations.addView(generateTextView("+ " + reason));
		}
		
		for(CharSequence reason: explanation.negativeReasons()) {
			explanations.addView(generateTextView("-" + reason));
		}			
	}
	
	private TextView generateTextView(CharSequence text) {
		TextView view = new TextView(this);
		view.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		view.setText(text);
		return view;
	}

	private void setupItemDetails() {
		Item item = recommendation.item();

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

	protected void onFinishTask() {
		// finish task, store stats to database
		Uri statUri = Statistics.get().finishTask(this);
		if (statUri == null) {
			Toast.makeText(this, "Task was not started.", Toast.LENGTH_LONG)
					.show();
			return;
		}

		FavouriteItemUtils.add(this, recommendation.item());

		// display results
		Intent intent = new Intent(this, ResultsActivity.class);
		intent.putExtra(ResultsActivity.InitBundle.STATS_ID,
				Integer.valueOf(Stats.getStatId(statUri)));
		intent.putExtra(ResultsActivity.InitBundle.ITEM_ID, recommendation
				.item().id());
		startActivity(intent);
	}
}