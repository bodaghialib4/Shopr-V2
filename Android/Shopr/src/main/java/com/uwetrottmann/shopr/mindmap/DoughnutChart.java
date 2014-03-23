package com.uwetrottmann.shopr.mindmap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;

public class DoughnutChart extends AttributeChart {

	public DoughnutChart(Context context, Attribute attribute) {
		super(context, attribute);
	}

	public GraphicalView getView() {
		MultipleCategorySeries series = getDataSet();
		DefaultRenderer renderer = Charts.getDefaultRenderer(getElems());
		return ChartFactory.getDoughnutChartView(context, series, renderer);
	}

	private MultipleCategorySeries getDataSet() {
		MultipleCategorySeries series = new MultipleCategorySeries(attribute.id());
		series.add(getTitles(), getValues());
		return series;
	}

}
