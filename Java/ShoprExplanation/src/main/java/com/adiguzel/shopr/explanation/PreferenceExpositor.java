package com.adiguzel.shopr.explanation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adiguzel.shopr.explanation.util.TextUtils;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;

public class PreferenceExpositor {
	private ExplanationLocalizer localizer;
	private TextFormatter formatter;

	public PreferenceExpositor(ExplanationLocalizer localizer, TextFormatter formatter) {
		this.localizer = localizer;
		this.formatter = formatter;
	}

	public CharSequence explain(Attribute attribute) {
		double[] attributeValueWeights = attribute.getValueWeights();
		// Indifferent to any value
		if (isIndifferent(attributeValueWeights)) {
			return toText(localizer.getIndifferentToAnyTemplate(), attribute);
		}
		// Prefers only some and avoids others
		else if (hasPreferenceOnlyOverSome(attributeValueWeights)) {
			List<String> preferredOnly = filterByNonzero(attribute);
			return toText(localizer.getOnlySomeTemplate(), preferredOnly);
		}
		// Avoids some, indifferent to the others
		else if (avoidsSome(attributeValueWeights)) {
			List<String> avoided = filterAvoided(attribute);
			return toText(localizer.getAvoidsSomeTemplate(), avoided);
		}
		// prefers some more than others
		else {
			Map<Double, List<String>> attributeValueGroups = groupByWeight(attribute);
			Double[] values = attributeValueGroups.keySet().toArray(
					new Double[attributeValueGroups.size()]);
			double biggest = max(values);
			List<String> preferred = attributeValueGroups.get(biggest);
			return toText(localizer.getPrefersSomeTemplate(), preferred);
		}
	}
	
	private CharSequence toText(String template, Attribute attribute) {
		String text =  String.format(template, TextUtils.textOf(localizer, attribute.id()));
		return formatter.fromHtml(text);
	}
	
	private CharSequence toText(String template, List<String> reasons) {
		String text = String.format(template, TextUtils.textify(localizer, reasons));
		return formatter.fromHtml(text);
	}
	
	public Double max(Double[] values) {
		Double max = 0.0;
		for (int i = 0; i < values.length; i++) {
			if (values[i] > max)
				max = values[i];
		}
		return max;
	}

	private Map<Double, List<String>> groupByWeight(Attribute attribute) {
		Map<Double, List<String>> attributeGroups = new HashMap<Double, List<String>>();

		double[] valueWeights = attribute.getValueWeights();

		for (int i = 0; i < valueWeights.length; i++) {
			List<String> currentValues = attributeGroups
					.get(new Double(valueWeights[i]));
			if (currentValues == null) {
				currentValues = new ArrayList<String>();
			}
			currentValues.add(TextUtils.textOf(localizer,
					attribute.getAttributeValues()[i]));
			attributeGroups.put(new Double(valueWeights[i]), currentValues);
		}

		return attributeGroups;
	}

	private List<String> filterAvoided(Attribute attribute) {
		List<String> avoided = new ArrayList<String>();
		double[] valueWeights = attribute.getValueWeights();
		for (int i = 0; i < valueWeights.length; i++) {
			if (valueWeights[i] == 0) {
				avoided.add(TextUtils.textOf(localizer,
						attribute.getAttributeValues()[i]));
			}
		}
		return avoided;
	}

	private List<String> filterByNonzero(Attribute attribute) {
		List<String> nonzero = new ArrayList<String>();
		double[] valueWeights = attribute.getValueWeights();
		for (int i = 0; i < valueWeights.length; i++) {
			if (valueWeights[i] > 0) {
				nonzero.add(TextUtils.textOf(localizer,
						attribute.getAttributeValues()[i]));
			}

		}
		return nonzero;
	}

	private boolean avoidsSome(double[] weights) {
		int numZeros = numOccurences(weights, 0);
		int numNonzeros = weights.length - numZeros;

		return numZeros > 0 && numZeros < numNonzeros
				&& allNonZeroValuesEqual(weights);
	}

	private boolean hasPreferenceOnlyOverSome(double[] weights) {
		int numZeros = numOccurences(weights, 0);
		int numNonzeros = weights.length - numZeros;

		return numZeros > 0 && numNonzeros <= numZeros
				&& allNonZeroValuesEqual(weights);
	}

	private boolean isIndifferent(double[] values) {
		return allNonzeroAndEqual(values);
	}

	private boolean allNonZeroValuesEqual(double[] values) {
		double firstNonZero = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i] == 0)
				break;

			if (firstNonZero == 0)
				firstNonZero = values[i];
			else if (firstNonZero > 0 && values[i] != firstNonZero)
				return false;
		}
		return true;
	}

	private int numOccurences(double[] values, double expected) {
		int n = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i] == expected)
				n++;
		}

		return n;
	}

	private boolean allNonzeroAndEqual(double[] values) {
		if (values.length == 0)
			return false;
		else if (values.length == 1)
			return values[0] != 0;
		else {
			double first = values[0];
			if (first == 0)
				return false;

			for (int i = 1; i < values.length; i++) {
				if (values[i] != first || values[i] == 0)
					return false;
			}
			return true;
		}
	}

}
