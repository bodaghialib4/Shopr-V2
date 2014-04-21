package com.adiguzel.shopr.explanation.model;


public class ContextArgument extends Argument {
	private Context context;
	
	public ContextArgument(Type type) {
		super(type);
	}

	public ContextArgument(Context context, boolean isPositive) {
		super(isPositive);
		this.context = context;
		this.type = Type.CONTEXT;
	}
	
	public Context context() {
		return context;
	}

}
