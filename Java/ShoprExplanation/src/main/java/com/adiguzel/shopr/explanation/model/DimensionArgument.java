package com.adiguzel.shopr.explanation.model;

public class DimensionArgument extends Argument {
	private Dimension dimension;
	
	public DimensionArgument(Type type) {
		super(type);
	}

	public DimensionArgument(Dimension dimension, boolean isPositive) {
		super(isPositive);
		this.dimension = dimension;
		this.type = Type.ON_DIMENSION;
	}

	public Dimension dimension() {
		return dimension;
	}

}
