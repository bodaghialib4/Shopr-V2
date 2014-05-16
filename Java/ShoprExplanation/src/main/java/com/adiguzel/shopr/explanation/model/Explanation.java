package com.adiguzel.shopr.explanation.model;

import java.util.ArrayList;
import java.util.Collection;

public class Explanation {
	private CharSequence simpleReason;
	private Collection<CharSequence> positiveReasons = new ArrayList<CharSequence>();
	private Collection<CharSequence> negativeReasons = new ArrayList<CharSequence>();

	public Explanation addPositiveReason(CharSequence positive) {
		positiveReasons.add(positive);
		return this;
	}

	public Collection<CharSequence> positiveReasons() {
		return positiveReasons;
	}

	public Explanation addNegativeReason(CharSequence negative) {
		negativeReasons.add(negative);
		return this;
	}

	public Collection<CharSequence> negativeReasons() {
		return negativeReasons;
	}

	public Explanation simple(CharSequence simpleReason) {
		this.simpleReason = simpleReason;
		return this;
	}

	public CharSequence simple() {
		return simpleReason;
	}
}
