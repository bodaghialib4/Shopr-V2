package com.adiguzel.shopr.explanation.model;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import com.uwetrottmann.shopr.algorithm.model.Item;

public class Explanation {
	private Item item;
	private Set<Argument> primaryArguments;
	private Set<Argument> supportingArguments;

	public Explanation(Item item) {
		this.item = item;
		primaryArguments = new LinkedHashSet<Argument>();
		supportingArguments = new LinkedHashSet<Argument>();
	}

	public Set<Argument> primaryArguments() {
		return primaryArguments;
	}
	
	public Set<Argument> supportingArguments() {
		return supportingArguments;
	}

	public Argument mainArgument() {
		if (primaryArguments.iterator().hasNext())
			return primaryArguments.iterator().next();
		else
			return null;
	}

	public void addPrimaryArgument(Argument argument) {
		primaryArguments.add(argument);
	}
	
	public void addPrimaryArguments(Collection<Argument> arguments) {
		primaryArguments.addAll(arguments);
	}
	
	public void addSupportingArgument(Argument argument) {
		supportingArguments.add(argument);
	}
	
	public void addSupportingArguments(Collection<Argument> arguments) {
		supportingArguments.addAll(arguments);
	}

	public Item item() {
		return item;
	}
}
