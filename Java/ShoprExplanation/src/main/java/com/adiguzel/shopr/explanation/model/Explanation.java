package com.adiguzel.shopr.explanation.model;

import java.util.LinkedHashSet;
import java.util.Set;

import com.uwetrottmann.shopr.algorithm.model.Item;

public class Explanation {
	private Item item;
	private Set<Argument> arguments;

	public Explanation(Item item) {
		this.item = item;
		arguments = new LinkedHashSet<Argument>();
	}

	public Set<Argument> arguments() {
		return arguments;
	}

	public Argument mainArgument() {
		if (arguments.iterator().hasNext())
			return arguments.iterator().next();
		else
			return null;
	}

	public void addArgument(Argument argument) {
		arguments.add(argument);
	}

	public Item item() {
		return item;
	}
}
