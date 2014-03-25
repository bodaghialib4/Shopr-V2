package com.uwetrottmann.shopr.mindmap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;

public class PieChart extends AttributeChart {

	public PieChart(Context context, Attribute attribute) {
		super(context, attribute);
	}

	public GraphicalView getView() {
		CategorySeries series = getDataSet();
		DefaultRenderer renderer = Charts.getDefaultRenderer(getElems());
		return ChartFactory.getPieChartView(context, series, renderer);
	}

	protected CategorySeries getDataSet() {
		CategorySeries series = new CategorySeries(attribute.id());

		for (ChartElem elem : getElems()) {
			series.add(elem.name(), elem.value());
		}
		
		return series;
	}

}