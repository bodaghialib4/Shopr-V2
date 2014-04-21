package com.adiguzel.shopr.explanation.model;

public class Argument {
	protected boolean isPositive;
	protected Type type;
	
	public Argument(Type type) {
		this.type = type;
	}

	public Argument(boolean isPositive) {
		this.isPositive = isPositive;
		this.type = Type.ON_DIMENSION;
	}

	public boolean isPositive() {
		return isPositive;
	}
	
	public boolean isNegative() {
		return !isPositive;
	}
	
	public Type getType() {
		return type;
	}
	
	public enum Type {
		ON_DIMENSION,
		GOOD_AVERAGE,
		NO_BETTER_ALTERNATIVES,
		CONTEXT
	}
}
