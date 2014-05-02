package com.uwetrottmann.shopr.utils;

import java.util.List;

import android.text.Html;

import com.adiguzel.shopr.explanation.TextFormatter;

public class ShoprTextFormatter implements TextFormatter{

	@Override
	public CharSequence fromHtml(String html) {
		return Html.fromHtml(html);
	}

	@Override
	public CharSequence renderSpannable(String text, List<String> toSpan) {
		// TODO Auto-generated method stub
		return null;
	}

}
