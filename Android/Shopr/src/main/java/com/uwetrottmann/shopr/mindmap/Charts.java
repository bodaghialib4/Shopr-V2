package com.uwetrottmann.shopr.mindmap;

import java.util.List;

import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.graphics.Color;

public class Charts {
	
	public static DefaultRenderer getDefaultRenderer(List<ChartElem> elems) {
		DefaultRenderer defaultRenderer = new DefaultRenderer();

		for (ChartElem elem : elems) {
			SimpleSeriesRenderer simpleRenderer = new SimpleSeriesRenderer();
			simpleRenderer.setColor(elem.color());
			defaultRenderer.addSeriesRenderer(simpleRenderer);
		}

		defaultRenderer.setLabelsColor(Color.BLACK);
		defaultRenderer.setShowLabels(true);
		defaultRenderer.setShowLegend(true);
		defaultRenderer.setZoomEnabled(false);
		defaultRenderer.setPanEnabled(false);
		return defaultRenderer;
	}
}
