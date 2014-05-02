package com.adiguzel.shopr.explanation;

import java.util.List;

public interface TextFormatter {
	public CharSequence fromHtml(String html);

	public CharSequence renderSpannable(String text, List<String> toSpan);
}
