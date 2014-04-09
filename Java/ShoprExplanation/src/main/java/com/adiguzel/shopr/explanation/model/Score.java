package com.adiguzel.shopr.explanation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uwetrottmann.shopr.algorithm.Query;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Item;

public class Score {
	
	public double informationScore(Item item, Query query, Dimension dimension, List<Item> recommendations) {
		double R = calculateRange(recommendations, query, dimension);
		double I = calculateInformation(recommendations, query, dimension);
		return (R + I) / 2;
	}
	
	private double calculateInformation(List<Item> recommendations, Query query, Dimension dimension) {
		int n = recommendations.size();
		int h = findNumMostFrequentX(recommendations, query, dimension);
		return (n - h) / (n - 1);
	}

	private int findNumMostFrequentX(List<Item> recommendations, Query query, Dimension dimension) {
		List<Double> explanationScores = mapToExplanationScore(recommendations, query, dimension);
		Map<Double,Integer> frequencyOfScores = new HashMap<Double,Integer>();
		int max = 1;
		for(Double score: explanationScores) {
			if(frequencyOfScores.containsKey(score)) {
				frequencyOfScores.put(score, 1);
			} else {
				int newFreq = frequencyOfScores.get(score) + 1;
				if(newFreq > max) max = newFreq;
				frequencyOfScores.put(score, newFreq);
			}
		}
		return max;
	}
	
	private List<Double> mapToExplanationScore(List<Item> recommendations, Query query, Dimension dimension) {
		List<Double> explanationScores = new ArrayList<Double>();
		for(Item item: recommendations) {
			double expScore = explanationScore(item, query, dimension);
			explanationScores.add(expScore);
		}
		return explanationScores;
	}

	public double calculateRange(List<Item> recommendations, Query query, Dimension dimension) {
		double max = 0.0;
		double min = 1.0;
		
		for(Item item: recommendations) {
			double ES = explanationScore(item, query, dimension);
			max = Math.max(max, ES);
			min = Math.min(min, ES);
		}
		
		return max - min;
	}
	
	public double globalScore(Item item, Query query) {
		double scoreSum = 0.0;
		double numDimensions = item.attributes().values().size();
		for (Attribute attribute : item.attributes().values()) {
			Dimension dimension = new Dimension(attribute);
			scoreSum += explanationScore(item, query, dimension);
		}
		return scoreSum / numDimensions;
	}

	public double explanationScore(Item item, Query query, Dimension dimension) {
		Attribute itemAttr = item.attributes().getAttributeById(dimension.attribute().id());
		Attribute queryAttr = query.attributes().getAttributeById(
				dimension.attribute().id());
		double[] itemValueWeights = itemAttr.getValueWeights();
		double[] queryValueWeights;

		if (itemAttr != null && queryAttr != null) {
			queryValueWeights = queryAttr.getValueWeights();
		} else {
			Attribute missingQueryAttr = query.attributes().initialize(
					dimension.attribute());
			queryValueWeights = missingQueryAttr.getValueWeights();
		}
		System.out.println(calculateLocalScore(itemValueWeights, queryValueWeights));
		return calculateLocalScore(itemValueWeights, queryValueWeights);
	}

	private double calculateLocalScore(double[] itemWeights, double[] queryWeights) {
		double score = 0.0;
		for (int i = 0; i < itemWeights.length; i++) {
			score += itemWeights[i] * queryWeights[i];
		}

		return score;
	}

}
