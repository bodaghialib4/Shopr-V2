package com.uwetrottmann.shopr.utils;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;

import com.adiguzel.shopr.explanation.TextFormatter;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Price;
import com.uwetrottmann.shopr.algorithm.model.Sex;
import com.uwetrottmann.shopr.ui.explanation.ClothingTypePreferenceActivity;
import com.uwetrottmann.shopr.ui.explanation.ColorPreferenceActivity;
import com.uwetrottmann.shopr.ui.explanation.GenderPreferenceActivity;
import com.uwetrottmann.shopr.ui.explanation.PricePreferenceActivity;
import com.uwetrottmann.shopr.ui.explanation.RecommendationsFragment;

public class ShoprTextFormatter implements TextFormatter {
	private Fragment fragment;

	public ShoprTextFormatter(Fragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public CharSequence fromHtml(String html) {
		return Html.fromHtml(html);
	}

	@Override
	public CharSequence renderClickable(AttributeText attributeText) {
		SpannableString ss = new SpannableString(attributeText.originalText());

		for (String toRender : attributeText.replacableTexts()) {
			setSpanOnLink(ss, toRender, new PreferenceClickableSpan(
					attributeText.attribute()));
		}
		return ss;
	}

	private void setSpanOnLink(SpannableString ss, String link, ClickableSpan cs) {
		String text = ss.toString();
		int start = text.indexOf(link);
		int end = start + link.length();
		if(start >= 0)
			ss.setSpan(cs, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	class PreferenceClickableSpan extends ShoprClickableSpan {
		private Attribute attribute;

		public PreferenceClickableSpan(Attribute attribute) {
			this.attribute = attribute;
		}

		@Override
		public void onClick(View widget) {
			Class<?> cls = null;
			if (attribute instanceof Color) {
				cls = ColorPreferenceActivity.class;
			} else if (attribute instanceof ClothingType) {
				cls = ClothingTypePreferenceActivity.class;
			} else if (attribute instanceof Price) {
				cls = PricePreferenceActivity.class;
			} else if (attribute instanceof Sex) {
				cls = GenderPreferenceActivity.class;
			}
			Intent intent = new Intent(fragment.getActivity(), cls);
			fragment.startActivityForResult(intent,
					RecommendationsFragment.REQUEST_CODE);
		}

	}

	abstract class ShoprClickableSpan extends ClickableSpan {

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(android.graphics.Color.BLUE);
			ds.setTypeface(Typeface.DEFAULT_BOLD);
			ds.setUnderlineText(false);
		}
	}

	@Override
	public CharSequence renderClickable(CharSequence originalText,
			Attribute attribute, String toRender) {
		SpannableString ss = new SpannableString(originalText);
		setSpanOnLink(ss, toRender, new PreferenceClickableSpan(attribute));
		return ss;
	}

	@Override
	public CharSequence concat(CharSequence... text) {
		return TextUtils.concat(text);
	}

}
