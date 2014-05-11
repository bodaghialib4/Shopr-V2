package com.uwetrottmann.shopr.mindmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adiguzel.shopr.explanation.PreferenceExpositor;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.utils.ShoprLocalizer;
import com.uwetrottmann.shopr.utils.ShoprTextFormatter;

import de.greenrobot.event.EventBus;

public abstract class MindMapFragment extends Fragment {

	protected LinearLayout preferenceChart;
	protected TextView preferenceExplanation;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.explanation_fragment_mind_map,
				container, false);

		preferenceExplanation = (TextView) v
				.findViewById(R.id.preferenceExplanation);
		preferenceChart = (LinearLayout) v.findViewById(R.id.preferenceChart);
		preferenceChart.setOnClickListener(getOnClickListener());
		return v;
	}

	protected OnClickListener getOnClickListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onAttributePreferenceChangeRequested();
			}
		};
	}

	protected void startActivity(Class<?> activity) {
		Intent intent = new Intent(getActivity(), activity);
		startActivity(intent);
	}

	protected abstract Attribute attribute();

	private Attribute queryAttribute() {
		Attribute queryAttribute = AdaptiveSelection.get().getCurrentQuery()
				.attributes().getAttributeById(attribute().id());

		if (queryAttribute == null)
			return attribute();
		return queryAttribute;
	}

	protected abstract void onAttributePreferenceChangeRequested();

	protected abstract View getChartView();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		resetExplanation();
		redrawChart();
	}

	private void redrawChart() {
		preferenceChart.removeAllViews();
		preferenceChart.addView(getChartView());
	}

	private void resetExplanation() {
		CharSequence explanation = new PreferenceExpositor(new ShoprLocalizer(
				getActivity()), new ShoprTextFormatter(this)).explain(queryAttribute());
		preferenceExplanation.setText(explanation);
		preferenceExplanation.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public void onStop() {
		EventBus.getDefault().unregister(this);
		super.onStop();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.item_list, menu);
	}

}
