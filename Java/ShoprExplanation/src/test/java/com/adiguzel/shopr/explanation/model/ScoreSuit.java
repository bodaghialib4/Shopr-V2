package com.adiguzel.shopr.explanation.model;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

import com.uwetrottmann.shopr.algorithm.Preference;
import com.uwetrottmann.shopr.algorithm.Query;
import com.uwetrottmann.shopr.algorithm.model.Attributes;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Color.Value;
import com.uwetrottmann.shopr.algorithm.model.Item;

public class ScoreSuit {
	
	@Test
	public void localScoreBetweenZeroAndOne() {
		Item item = new Item();
		Attribute attr = new Color();
		
		Attributes attributes = new Attributes().putAttribute(attr);
		item.attributes(attributes);
		
		Query query = new Query();
		query.attributes().putAttribute(attr);
		
		double explanationScore = ScoreComputer.explanationScore(item, query, new Dimension(attr));
		
		assertThat(explanationScore).isLessThanOrEqualTo(1.0);
		assertThat(explanationScore).isGreaterThanOrEqualTo(0.0);
	}
	
	@Test
	public void localScoreWithNoQueryAttributeBetweenZeroAndOne() {
		Item item = new Item();
		Attribute attr = new Color();
		
		Attributes attributes = new Attributes().putAttribute(attr);
		item.attributes(attributes);
		
		Query query = new Query();
		
		double explanationScore = ScoreComputer.explanationScore(item, query, new Dimension(attr));
		
		assertThat(explanationScore).isLessThanOrEqualTo(1.0);
		assertThat(explanationScore).isGreaterThanOrEqualTo(0.0);
	}
	
	@Test
	public void localFullScoreWithExactMatchToPreference() {
		Item item = new Item();
		Attribute attr = new Color(Color.Value.BLACK);
		
		Attributes attributes = new Attributes().putAttribute(attr);
		item.attributes(attributes);
		
		Query query = new Query();
		query.attributes().putAttribute(attr);
		
		assertThat(ScoreComputer.explanationScore(item, query, new Dimension(attr))).isEqualTo(1.0);
	}
	
	@Test
	public void localZeroScoreWithNoMatchToPreference() {
		Item item = new Item();
		Attribute itemAttr = new Color(Color.Value.BLACK);
		
		Attributes attributes = new Attributes().putAttribute(itemAttr);
		item.attributes(attributes);
		
		Query query = new Query();
		// Set preferences to some other color
		Preference preference = new Preference(new Color());
		preference.add(Color.Value.BLUE);
		query.revise(preference);
		
		assertThat(ScoreComputer.explanationScore(item, query, new Dimension(new Color()))).isEqualTo(0.0);
	}
	
	@Test
	public void localHalfScoreWithHalfMatchToPreference() {
		Item item = new Item();
		Value sameColor = Color.Value.BLACK;
		Attribute itemAttr = new Color(sameColor);
		
		Attributes attributes = new Attributes().putAttribute(itemAttr);
		item.attributes(attributes);
		
		Query query = new Query();
		// Set preferences half same and half some other color
		Preference preference = new Preference(new Color());
		preference.add(sameColor);
		preference.add(Color.Value.BLUE);
		query.revise(preference);
		
		assertThat(ScoreComputer.explanationScore(item, query, new Dimension(new Color()))).isEqualTo(0.5);
	}
	
	@Test
	public void globalFullScoreWithExactMatchToPreferenceWithMultipleDimens() {
		Item item = new Item();
		Attribute colorAttr = new Color(Color.Value.BLACK);
		
		Attribute clothingAttr = new ClothingType(ClothingType.Value.BIKINI);
		
		Attributes attributes = new Attributes().putAttribute(colorAttr).putAttribute(clothingAttr);
		item.attributes(attributes);
		
		Query query = new Query();
		query.attributes().putAttribute(colorAttr).putAttribute(clothingAttr);
		
		assertThat(ScoreComputer.globalScore(item, query)).isEqualTo(1.0);
	}
	
	@Test
	public void globalMultipleDimensWithDifferentScores() {
		Item item = new Item();
		Value sameColor = Color.Value.BLACK;
		Value anotherColor = Color.Value.BLUE;
		Attribute colorAttr = new Color(sameColor);
		
		Attribute clothingAttr = new ClothingType(ClothingType.Value.BIKINI);
		
		Attributes attributes = new Attributes().putAttribute(colorAttr).putAttribute(clothingAttr);
		item.attributes(attributes);
		
		Query query = new Query();
		query.attributes().putAttribute(colorAttr).putAttribute(clothingAttr);
		
		Preference preference = new Preference(new Color());
		preference.add(sameColor);
		preference.add(anotherColor);
		query.revise(preference);
		
		assertThat(ScoreComputer.globalScore(item, query)).isEqualTo(0.75);
	}

}
