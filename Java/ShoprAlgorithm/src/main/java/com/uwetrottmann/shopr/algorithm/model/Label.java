
package com.uwetrottmann.shopr.algorithm.model;

import java.util.Arrays;

import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

public class Label extends GenericAttribute {

    public static final String ID = "label";

    public enum Value implements AttributeValue {
        ARMANI("Armani", Colors.BLUE),
        HUGO_BOSS("Hugo Boss", Colors.BLACK),
        CHANEL("Chanel", Colors.RED),
        DOLCE_AND_GABBANA("Dolce & Gabbana", Colors.GREEN),
        KARL_LAGERFELD("Karl Lagerfeld", Colors.GOLD);

        private String mDescriptor;
        private String mColor;

        Value(String name, String color) {
            mDescriptor = name;
            mColor = color;
        }

        @Override
        public String descriptor() {
            return mDescriptor;
        }

        @Override
        public String simpleName() {
            return mDescriptor;
        }

        @Override
        public int index() {
            return ordinal();
        }

		@Override
		public String color() {
			return mColor;
		}
    }
    
    public Label() {
        int numValues = Value.values().length;
        mValueWeights = new double[numValues];
        Arrays.fill(mValueWeights, 1.0 / numValues);
    }

    public Label(Value value) {
        mValueWeights = new double[Value.values().length];
        Arrays.fill(mValueWeights, 0.0);
        mValueWeights[value.ordinal()] = 1.0;
        currentValue(value);
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public Value[] getValueSymbols() {
        return Value.values();
    }
    
	@Override
	public AttributeValue[] getAttributeValues() {
		return Value.values();
	}
}
