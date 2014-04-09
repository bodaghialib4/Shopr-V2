package com.adiguzel.shopr.explanation.model;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;

public class ArgumentSuit {
	
	@Test
	public void compareArgumentsWithEqualExpScore() {	
		compareArguments(0.1, 0.1, 0);
	}
	
	@Test
	public void compareArgumentsWithOtherHavingHigherExpScore() {
		compareArguments(0.1, 0.2, -1);
	}
	
	@Test
	public void compareArgumentsWithOtherHavingLowerExpScore() {
		compareArguments(0.2, 0.1, 1);
	}
	
	private void compareArguments(double infScore, double otherInfScore, int expected) {
		Dimension dimen = new Dimension(new Color()).informationScore(infScore);
		Dimension otherDimen = new Dimension(new ClothingType()).informationScore(otherInfScore);
		
		Argument argument = new Argument(dimen, true);
		Argument otherArgument = new Argument(otherDimen, true);
		
		assertThat(argument.compareTo(otherArgument)).isEqualTo(expected);
	}

}
