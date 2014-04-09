package com.adiguzel.shopr.explanation.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;

public class Dimension implements Comparable<Dimension> {
	// A dimension relates to an attribute
	private Attribute attribute;
	private double explanationScore;
	private double informationScore;
	
	public Dimension(Attribute attribute) {
		this.attribute = attribute;
	}
	
	public Attribute attribute() {
		return attribute;
	}
	
	public Dimension explanationScore(double explanationScore) {
		this.explanationScore = explanationScore;
		return this;
	}
	
	public double explanationScore() {
		return explanationScore;
	}
	
	public Dimension informationScore(double informationScore) {
		this.informationScore = informationScore;
		return this;
	}
	
	public double informationScore() {
		return informationScore;
	}

	@Override
	public int compareTo(Dimension other) {
		if(informationScore() == other.informationScore()) {
			return 0;
		}
		else if (informationScore() > other.informationScore()) {
			return 1;
		}
		return -1;
	}
}
