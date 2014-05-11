package com.uwetrottmann.shopr.ui.explanation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Price;
import com.uwetrottmann.shopr.algorithm.model.Sex;

public class MindMapOverviewFragment extends Fragment{
	
	protected GridView preferenceGrid;
	protected TextView preferenceOverviewText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.explanation_fragment_mind_map_overview,
				container, false);

		preferenceOverviewText = (TextView) v
				.findViewById(R.id.preferenceExplanation);
		preferenceGrid = (GridView) v.findViewById(R.id.preferenceChart);
		
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		redrawPreferenceGrid();
	}
	
	public void redrawPreferenceGrid() {
		
	}
	
	
	public class CustomGridAdapter extends BaseAdapter {

	    private Context context;
	    private Attribute[] attributes = new Attribute[]{new ClothingType(), new Color(), new Sex(), new Price()};
	    private LayoutInflater inflater;

	    public CustomGridAdapter(Context context, String[] items) {
	        this.context = context;
	        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {

	        if (convertView == null) {
	           // convertView = inflater.inflate(R.layout.cell, null);
	        }
	       // Button button = (Button) convertView.findViewById(R.id.grid_item);
	       //.setText(items[position]);

	        return convertView;
	    }

	    @Override
	    public int getCount() {
	        return attributes.length;
	    }

	    @Override
	    public Attribute getItem(int position) {
	        return attributes[position];
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }
	}
	

}
