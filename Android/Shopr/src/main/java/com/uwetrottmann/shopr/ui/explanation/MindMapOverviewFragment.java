package com.uwetrottmann.shopr.ui.explanation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.adiguzel.shopr.explanation.PreferenceExpositor;
import com.adiguzel.shopr.explanation.TextFormatter;
import com.etsy.android.grid.StaggeredGridView;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.model.Attributes;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Price;
import com.uwetrottmann.shopr.algorithm.model.Sex;
import com.uwetrottmann.shopr.mindmap.MindMapFragment;
import com.uwetrottmann.shopr.utils.ShoprLocalizer;
import com.uwetrottmann.shopr.utils.ShoprTextFormatter;

public class MindMapOverviewFragment extends Fragment {

	protected StaggeredGridView preferenceGrid;
	protected TextView preferenceOverviewText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.explanation_fragment_mind_map_overview, container,
				false);

		preferenceOverviewText = (TextView) v
				.findViewById(R.id.preferenceExplanation);
		preferenceGrid = (StaggeredGridView) v.findViewById(R.id.preferenceGrid);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		redrawPreferenceGrid();
	}

	public void redrawPreferenceGrid() {
		preferenceGrid.setAdapter(new CustomGridAdapter(getActivity()));
	}

	public TextFormatter textFormatter() {
		return new ShoprTextFormatter(this);
	}

	public class CustomGridAdapter extends BaseAdapter {

		private Context context;
		private ColorfulAttribute[] attributes = new ColorfulAttribute[] {
				new ColorfulAttribute(new ClothingType(), "#FF6600"),
				new ColorfulAttribute(new Color(), "#FFCC00"),
				new ColorfulAttribute(new Sex(), "#92CD00"),
				new ColorfulAttribute(new Price(), "#CA278C") };
		private LayoutInflater inflater;

		public CustomGridAdapter(Context context) {
			this.context = context;
			inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.explanation_mind_map_overview_item, null);
			}
			TextView titleText = (TextView) convertView
					.findViewById(R.id.title);
			titleText.setText(attributes[position].attribute.id());
			
			TextView explanationText = (TextView) convertView
					.findViewById(R.id.preferenceExplanation);
			CharSequence explanation = new PreferenceExpositor(
					new ShoprLocalizer(context), textFormatter())
					.explain(getQueryAttribute(attributes[position].attribute));
			explanationText.setText(explanation);
			explanationText.setMovementMethod(LinkMovementMethod.getInstance());
			explanationText.setOnClickListener(new View.OnClickListener() {	
				@Override
				public void onClick(View v) {
					onPreferenceChangeRequestedFor(attributes[position].attribute);
				}
			});
			
			explanationText.setBackgroundColor(attributes[position].color);

			return convertView;
		}
		
		private void onPreferenceChangeRequestedFor(Attribute attribute) {
				startActivity(MindMapFragment.getPreferenceChangerFor(attribute));
		}
		
		private void startActivity(Class<?> activity) {
			Intent intent = new Intent(getActivity(), activity);
			getActivity().startActivity(intent);
		}
		
		private Attribute getQueryAttribute(Attribute attribute) {
			Attribute queryAttribute = AdaptiveSelection.get().getCurrentQuery()
					.attributes().getAttributeById(attribute.id());

			if (queryAttribute == null)
				return new Attributes().initialize(attribute);
			return queryAttribute;
		}

		@Override
		public int getCount() {
			return attributes.length;
		}

		@Override
		public ColorfulAttribute getItem(int position) {
			return attributes[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public class ColorfulAttribute {
			public int color;
			public Attribute attribute;

			public ColorfulAttribute(Attribute attribute, String color) {
				this.attribute = attribute;
				this.color = android.graphics.Color.parseColor(color);
			}
		}
	}

}
