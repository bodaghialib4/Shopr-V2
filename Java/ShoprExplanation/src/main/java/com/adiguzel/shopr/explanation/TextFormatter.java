package com.adiguzel.shopr.explanation;

import java.util.List;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;


public interface TextFormatter {
	public CharSequence fromHtml(String html);
	
	public CharSequence renderClickable(AttributeText attributeText);
		
	public CharSequence renderClickable(CharSequence originalText, Attribute attribute, String toRender);
	
	public CharSequence concat(CharSequence... text);
	
	public class AttributeText {
		private CharSequence originalText;
		private Attribute attribute;
		private List<String> replacableTexts;
		
		public AttributeText(CharSequence originalText, Attribute attribute, List<String> replacableTexts) {
			this.originalText = originalText;
			this.attribute = attribute;
			this.replacableTexts = replacableTexts;
		}
		
		public CharSequence originalText() {
			return originalText;
		}
		
		public Attribute attribute() {
			return attribute;
		}
		
		public List<String> replacableTexts() {
			return replacableTexts;
		}
	}
}
