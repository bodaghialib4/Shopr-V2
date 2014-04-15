package com.adiguzel.shopr.explanation.model;

public class Argument {
	private Dimension dimension;
	private boolean isPositive;
	private Type type;
	
	public Argument(Type type) {
		this.type = type;
	}

	public Argument(Dimension dimension, boolean isPositive) {
		this.dimension = dimension;
		this.isPositive = isPositive;
		this.type = Type.ON_DIMENSION;
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

	public Type getType() {
		return type;
	}
	
	public enum Type {
		ON_DIMENSION,
		GOOD_AVERAGE,
		NO_BETTER_ALTERNATIVES
	}
}
