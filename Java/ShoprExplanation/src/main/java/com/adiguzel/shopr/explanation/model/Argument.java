package com.adiguzel.shopr.explanation.model;

public class Argument {
	private Dimension dimension;
	private boolean isPositive;

	public Argument(Dimension dimension, boolean isPositive) {
		this.dimension = dimension;
		this.isPositive = isPositive;
	}

	public boolean isPositive() {
		return isPositive;
	}
	
	public boolean isNegative() {
		return !isPositive;
	}
	
	public Dimension dimension() {
		return dimension;
	}
	
}
