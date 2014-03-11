package com.uwetrottmann.shopr.algorithm.model;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

public class FavorValueSuite {
	
	 @Test
	 public void testFavorValueWithSingleElement() {
		 AttributeValue favoredValue = Color.Value.BLACK;
		 Color color = new Color();
		 double initialExpected = 1.0 / color.getValueWeights().length;
		 assertThat(getFavoredValueWeight(favoredValue, color)).isEqualTo(initialExpected);
		 
		 Set<AttributeValue> favoredValues = new HashSet<AttributeValue>();
		 favoredValues.add(favoredValue);
		 color.favorAttributeValues(favoredValues, color.getValueWeights());
		 double finalExpected = 1.0;
		 assertThat(getFavoredValueWeight(favoredValue, color)).isEqualTo(finalExpected);
	 }
	 
	 @Test
	 public void testFavorValueWithMultipleElements() {
		 AttributeValue favoredBlack = Color.Value.BLACK;
		 AttributeValue favoredBlue = Color.Value.BLUE;
		 Color color = new Color();
		 double initialExpected = 1.0 / color.getValueWeights().length;
		 assertThat(getFavoredValueWeight(favoredBlue, color)).isEqualTo(initialExpected);
		 assertThat(getFavoredValueWeight(favoredBlack, color)).isEqualTo(initialExpected);
		 
		 Set<AttributeValue> favoredValues = new HashSet<AttributeValue>();
		 favoredValues.add(favoredBlack);
		 favoredValues.add(favoredBlue);
		 color.favorAttributeValues(favoredValues, color.getValueWeights());
		 double finalExpected = 1.0 / favoredValues.size();
		 assertThat(getFavoredValueWeight(favoredBlue, color)).isEqualTo(finalExpected);
		 assertThat(getFavoredValueWeight(favoredBlack, color)).isEqualTo(finalExpected);
	 }
	 
	 private double getFavoredValueWeight(AttributeValue value, Attribute attr) {
		 return attr.getValueWeights()[value.index()]; 
	 }

}
