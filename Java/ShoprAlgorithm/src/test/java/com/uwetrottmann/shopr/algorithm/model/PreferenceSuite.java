package com.uwetrottmann.shopr.algorithm.model;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

import com.uwetrottmann.shopr.algorithm.Preference;
import com.uwetrottmann.shopr.algorithm.Query;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

public class PreferenceSuite {
	
	 @Test
	 public void testUpdateQueryWithPreference() {
		 AttributeValue favoredValue = Color.Value.BLACK;
		 Query query  = new Query();
		 Color color = new Color();
		 query.attributes().initializeAttribute(color);
		 Attribute queryAttr = query.attributes().getAttributeById(color.id());
		 
		 double initialExpected = 1.0 / color.getValueWeights().length;
		 assertThat(getFavoredValueWeight(favoredValue, queryAttr)).isEqualTo(initialExpected);
		 
		 Preference preference = new Preference(color).add(favoredValue);
		 query.revise(preference);
		 
		 double finalExpected = 1.0;
		 assertThat(getFavoredValueWeight(favoredValue, queryAttr)).isEqualTo(finalExpected);
	 }
	 
	 @Test
	 public void testUpdateQueryWithPreferenceWithMultipleElements() {
		 AttributeValue favoredBlack = Color.Value.BLACK;
		 AttributeValue favoredBlue = Color.Value.BLUE;
		 Color color = new Color();
		 
		 Query query  = new Query();
		 query.attributes().initializeAttribute(color);
		 Attribute queryAttr = query.attributes().getAttributeById(color.id());
		
		 double initialExpected = 1.0 / color.getValueWeights().length;
		 assertThat(getFavoredValueWeight(favoredBlue, queryAttr)).isEqualTo(initialExpected);
		 assertThat(getFavoredValueWeight(favoredBlack, queryAttr)).isEqualTo(initialExpected);
		 
		
		 Preference preference = new Preference(color);
		 preference.add(favoredBlack);
		 preference.add(favoredBlue);
		 query.revise(preference);
		 
		 double finalExpected = 1.0 / preference.attributeValues().size();
		 assertThat(getFavoredValueWeight(favoredBlack, queryAttr)).isEqualTo(finalExpected);
		 assertThat(getFavoredValueWeight(favoredBlue, queryAttr)).isEqualTo(finalExpected);
	 }
	 
	 private double getFavoredValueWeight(AttributeValue value, Attribute attr) {
		 return attr.getValueWeights()[value.index()]; 
	 }

}
