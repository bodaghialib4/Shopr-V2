package com.adiguzel.shopr.explanation.model;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import com.uwetrottmann.shopr.algorithm.model.Item;

public class Explanation {
	private Item item;
	private Set<DimensionArgument> primaryArguments;
	private Set<DimensionArgument> supportingArguments;
	private Set<ContextArgument> contextArguments;
	private String branch;
	
	public Explanation(Item item) {
		this.item = item;
		primaryArguments = new LinkedHashSet<DimensionArgument>();
		supportingArguments = new LinkedHashSet<DimensionArgument>();
		contextArguments = new LinkedHashSet<ContextArgument>();
	}
	
	public Explanation branch(String branch) {
		this.branch = branch;
		return this;
	}
	
	public String branch() {
		return branch;
	}

	public Set<DimensionArgument> primaryArguments() {
		return primaryArguments;
	}
	
	public Set<DimensionArgument> supportingArguments() {
		return supportingArguments;
	}
	
	public Set<DimensionArgument> contexArguments() {
		return supportingArguments;
	}


	public DimensionArgument mainArgument() {
		if (primaryArguments.iterator().hasNext())
			return primaryArguments.iterator().next();
		else
			return null;
	}

	public void addPrimaryArgument(DimensionArgument argument) {
		primaryArguments.add(argument);
	}
	
	public void addPrimaryArguments(Collection<DimensionArgument> arguments) {
		primaryArguments.addAll(arguments);
	}
	
	public void addSupportingArgument(DimensionArgument argument) {
		supportingArguments.add(argument);
	}
	
	public void addSupportingArguments(Collection<DimensionArgument> arguments) {
		supportingArguments.addAll(arguments);
	}
	
	public void addContextArgument(ContextArgument argument) {
		contextArguments.add(argument);
	}
	
	public void addContextArguments(Collection<ContextArgument> arguments) {
		contextArguments.addAll(arguments);
	}

	public Item item() {
		return item;
	}
}
