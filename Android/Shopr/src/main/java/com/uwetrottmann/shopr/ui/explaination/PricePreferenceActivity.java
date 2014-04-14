package com.uwetrottmann.shopr.ui.explaination;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;
import com.uwetrottmann.shopr.algorithm.model.Price;

public class PricePreferenceActivity extends PreferenceActivity {

	protected final Attribute attribute() {
		return new Price();
	}
	
	protected final int layout() {
		return R.layout.price_preference;
	}
	
	protected final TextView explanationView() {
		return (TextView) findViewById(R.id.textViewAttributeValuePreference);
	}

	protected final Button updatePreferenceButton() {
		return (Button) findViewById(R.id.buttonUpdatePreferences);
	}

	protected final ArrayAdapter<AttributeValue> attributeValueAdapter() {
		return new PriceRangeAdapter(this);
	}

	protected final AbsListView attributeValueList() {
		return (ListView) findViewById(R.id.listViewAttributeValueList);
	}

	public class PriceRangeAdapter extends ArrayAdapter<AttributeValue> {

		private static final int LAYOUT = R.layout.checkable_row;
		private LayoutInflater mInflater;

		public PriceRangeAdapter(Context context) {
			super(context, LAYOUT);
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(LAYOUT, null);

				holder = new ViewHolder();
				holder.name = (TextView) convertView
						.findViewById(R.id.textViewTitle);
			

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final AttributeValue value = getItem(position);
			holder.name.setText(value.descriptor());

			return convertView;
		}

		class ViewHolder {
			TextView name;
		}
	}

}
