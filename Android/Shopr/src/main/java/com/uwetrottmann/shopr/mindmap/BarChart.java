package com.uwetrottmann.shopr.mindmap;

import java.math.BigDecimal;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;

public class BarChart extends AttributeChart {
	private String yTitle = "weight (%)";
	private double startPos = 0.5;
	private double gap = 0.5;

	public BarChart(Context context, Attribute attribute) {
		super(context, attribute);
	}

	@Override
	public GraphicalView getView() {
		return ChartFactory.getBarChartView(context, getDataSet(),
				getRenderer(), Type.DEFAULT);
	}

	private XYMultipleSeriesDataset getDataSet() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		double i = startPos;
		for (ChartElem elem : getElems()) {
			XYSeries series = new XYSeries(elem.name());
			Double weightPercent = new BigDecimal(elem.value() * 100.0)
					.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			series.add(i, weightPercent);
			dataset.addSeries(series);
			i += gap;
		}

		return dataset;

	}

	private XYMultipleSeriesRenderer getRenderer() {
		XYMultipleSeriesRenderer mRenderer = initRenderer();

		for (ChartElem elem : getElems()) {
			XYSeriesRenderer renderer = new XYSeriesRenderer();
			renderer.setColor(elem.color());
			renderer.setDisplayChartValues(true);
			mRenderer.addSeriesRenderer(renderer);
			// mRenderer.addXTextLabel(i, elem.name());
		}

		return mRenderer;
	}

	private XYMultipleSeriesRenderer initRenderer() {
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer.setChartTitle(attribute.id() + " preference");
		mRenderer.setXTitle(attribute.id());
		mRenderer.setYTitle(yTitle);
		mRenderer.setAxesColor(Color.BLACK);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.WHITE);
		mRenderer.setMarginsColor(Color.WHITE);
		mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
		
		mRenderer.setBarSpacing(0.5);
		mRenderer.setAxisTitleTextSize(16);
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setLabelsTextSize(15);
		mRenderer.setLegendTextSize(15);

		mRenderer.setBarWidth(50);
		mRenderer.setXAxisMin(-2);
		mRenderer.setXAxisMax(11);
		mRenderer.setYAxisMin(0);
		mRenderer.setYLabelsAlign(Align.RIGHT);
		mRenderer.setXLabelsColor(Color.BLACK);
		mRenderer.setYLabelsColor(0, Color.BLACK);
		mRenderer.setXLabels(0);
		
		//mRenderer.setClickEnabled(false);
		mRenderer.setZoomEnabled(false);
		mRenderer.setZoomButtonsVisible(false);
		mRenderer.setPanEnabled(false);
		
		return mRenderer;
	}

}