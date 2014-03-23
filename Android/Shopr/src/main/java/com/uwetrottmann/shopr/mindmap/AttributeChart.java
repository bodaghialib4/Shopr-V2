package com.uwetrottmann.shopr.mindmap;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.GraphicalView;

import android.content.Context;
import android.graphics.Color;

import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

public abstract class AttributeChart {

	protected Context context;
	protected Attribute attribute;

	public AttributeChart(Context context, Attribute attribute) {
		this.context = context;
		this.attribute = attribute;
	}

	public abstract GraphicalView getView();

	protected List<ChartElem> getElems() {
		List<ChartElem> elems = new ArrayList<ChartElem>();
		AdaptiveSelection manager = AdaptiveSelection.get();
		Attribute attr = manager.getCurrentQuery().attributes()
				.getAttributeById(attribute.id());

		if (attr != null) {
			AttributeValue[] values = attr.getAttributeValues();
			int i = 0;
			for (double weight : attr.getValueWeights()) {
				AttributeValue value = values[i];
				ChartElem elem = new ChartElem().name(value.descriptor())
						.color(Color.parseColor(value.color())).value(weight);
				elems.add(elem);
				i++;
			}
		}

		return elems;
	}

	protected String[] getTitles() {
		List<ChartElem> elems = getElems();
		String[] titles = new String[elems.size()];

		int i = 0;
		for (ChartElem elem : getElems()) {
			titles[i] = (elem.name());
			i++;
		}

		return titles;
	}

	protected double[] getValues() {
		List<ChartElem> elems = getElems();
		double[] values = new double[elems.size()];

		int i = 0;
		for (ChartElem elem : getElems()) {
			values[i] = (elem.value());
			i++;
		}

		return values;
	}
}
