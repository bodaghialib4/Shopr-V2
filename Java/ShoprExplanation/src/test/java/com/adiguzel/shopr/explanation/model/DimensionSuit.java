package com.adiguzel.shopr.explanation.model;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;

public class DimensionSuit {
	
	@Test
	public void compareDimensionsWithEqualExpScore() {
		compareDimensions(0.1, 0.1, 0);
	}
	
	@Test
	public void compareDimensionsWithOtherHavingHigherExpScore() {
		compareDimensions(0.1, 0.2, -1);
	}
	
	@Test
	public void compareDimensionsWithOtherHavingLowerExpScore() {
		compareDimensions(0.2, 0.1, 1);
	}
	
	private void compareDimensions(double infScore, double otherInfScore, int expected) {
		Dimension dimen = new Dimension(new Color()).informationScore(infScore);
		Dimension otherDimen = new Dimension(new ClothingType()).informationScore(otherInfScore);
		
		assertThat(dimen.compareTo(otherDimen)).isEqualTo(expected);
	}


}
