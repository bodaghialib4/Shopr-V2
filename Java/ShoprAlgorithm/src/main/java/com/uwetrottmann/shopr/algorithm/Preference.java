package com.uwetrottmann.shopr.algorithm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

public class Preference {

	private Attribute attribute;
	private Set<AttributeValue> attributeValues = new HashSet<AttributeValue>();
	
    public Preference(Attribute attribute) {
        this.attribute = attribute;
    }

	public Attribute attribute() {
		return attribute;
	}

	public Preference attribute(Attribute attribute) {
		this.attribute = attribute;
		return this;
	}

	public Set<AttributeValue> attributeValues() {
		return attributeValues;
	}

	public Preference add(AttributeValue value) {
		attributeValues.add(value);
		return this;
	}

	public Preference add(AttributeValue... values) {
		attributeValues.addAll(Arrays.asList(values));
		return this;
	}
}
