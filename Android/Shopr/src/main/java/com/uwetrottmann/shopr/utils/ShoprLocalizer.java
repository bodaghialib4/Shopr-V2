package com.uwetrottmann.shopr.utils;

import android.content.Context;

import com.adiguzel.shopr.explanation.ExplanationLocalizer;
import com.uwetrottmann.shopr.R;

public class ShoprLocalizer implements ExplanationLocalizer {

	private Context mContext;

	public ShoprLocalizer(Context context) {
		mContext = context;
	}

	@Override
	public String getLocalizedValueDescriptor(String value) {
		return ValueConverter.getLocalizedStringForValue(mContext, value);
	}

	@Override
	public String getOnlyString() {
		return mContext.getString(R.string.only);
	}

	@Override
	public String getAvoidString() {
		return mContext.getString(R.string.avoid);
	}

	@Override
	public String getPreferablyString() {
		return mContext.getString(R.string.preferably);
	}

	@Override
	public String getIndifferentToAnyTemplate() {
		return mContext
				.getString(R.string.explanation_on_preference_indifferent);
	}

	@Override
	public String getOnlySomeTemplate() {
		return mContext
				.getString(R.string.explanation_on_preference_only_interested_in_some);
	}

	@Override
	public String getAvoidsSomeTemplate() {
		return mContext
				.getString(R.string.explanation_on_preference_avoiding_some);
	}

	@Override
	public String getPrefersSomeTemplate() {
		return mContext
				.getString(R.string.explanation_on_preference_prefer_some);
	}

	@Override
	public String getCommaString() {
		return mContext.getString(R.string.conjunction_comma);
	}

	@Override
	public String getAndString() {
		return mContext.getString(R.string.conjunction_and);
	}

	@Override
	public String[] getStrongArgumentTemplates() {
		return new String[] {
				mContext.getString(R.string.explanation_template_on_dimension_strong_1),
				mContext.getString(R.string.explanation_template_on_dimension_strong_2) };
	}

	@Override
	public String[] getWeakArgumentTemplates() {
		return new String[] {
				mContext.getString(R.string.explanation_template_on_dimension_weak_1),
				mContext.getString(R.string.explanation_template_on_dimension_weak_2) };
	}

	@Override
	public String[] getSerendipitousityTemplates() {
		return new String[] {
				mContext.getString(R.string.explanation_template_serendipidity_1),
				mContext.getString(R.string.explanation_template_serendipidity_2),
				mContext.getString(R.string.explanation_template_serendipidity_3) };
	}

	@Override
	public String[] getContextArgumentTemplates() {
		return new String[] {
				mContext.getString(R.string.explanation_template_context_location),
				mContext.getString(R.string.explanation_template_context_location_average) };
	}

	@Override
	public String getSupportingArgumentTemplate() {
		return mContext
				.getString(R.string.explanation_template_on_dimension_weak_supporting_1);
	}

	@Override
	public String getGoodAverageTemplate() {
		return mContext.getString(R.string.explanation_template_average_item);
	}

}
