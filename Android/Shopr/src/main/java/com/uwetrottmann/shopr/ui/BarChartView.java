package com.uwetrottmann.shopr.ui;

import java.util.ArrayList;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.AbstractChart;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

public class BarChartView extends GraphicalView {

	public static final int COLOR_GREEN = Color.parseColor("#62c51a");
	public static final int COLOR_ORANGE = Color.parseColor("#ff6c0a");
	public static final int COLOR_BLUE = Color.parseColor("#23bae9");

	/**
	 * Constructor that only calls the super method. It is not used to
	 * instantiate the object from outside of this class.
	 * 
	 * @param context
	 * @param arg1
	 */
	private BarChartView(Context context, AbstractChart chart) {
		super(context, chart);
	}

	public static GraphicalView getNewInstance(Context context, int income,
			int costs) {
		return ChartFactory
				.getBarChartView(context, getDataSet(),
						getRenderer(), Type.DEFAULT);
	}

	private static XYMultipleSeriesDataset getDataSet() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        final int nr = 4;
        Random r = new Random();
        ArrayList<String> legendTitles = new ArrayList<String>();
        legendTitles.add("Yellow");
        legendTitles.add("Red");
        for (int i = 0; i < 2; i++) {
            CategorySeries series = new CategorySeries(legendTitles.get(i));
            for (int k = 0; k < nr; k++) {
                series.add(100 + r.nextInt() % 100);
            }
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
	}

	private static XYMultipleSeriesRenderer getRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setMargins(new int[] { 30, 40, 15, 0 });
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(Color.YELLOW);
		renderer.addSeriesRenderer(r);
		r = new SimpleSeriesRenderer();
		r.setColor(Color.RED);
		
		renderer.addSeriesRenderer(r);
		renderer.setBackgroundColor(Color.WHITE);
		
		renderer.setZoomEnabled(false);
		renderer.setPanEnabled(false);
		return renderer;
	}
}