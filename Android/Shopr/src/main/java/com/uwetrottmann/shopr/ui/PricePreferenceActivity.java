package com.uwetrottmann.shopr.ui;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;
import com.uwetrottmann.shopr.algorithm.model.Price;

public class PricePreferenceActivity extends Activity implements
		OnItemClickListener {

	protected TextView mTextViewExplanation;
	protected ListView mListView;
	protected Button mButtonUpdatePreferences;
	protected ArrayAdapter<AttributeValue> mAdapter;

	protected Attribute attribute() {
		return new Price();
	}

	protected void onUpdatePreferencesFinish() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.price_preference);
		setupViews();
	}

	protected void setupViews() {
		mTextViewExplanation = (TextView) findViewById(R.id.textViewAttributeValuePreference);
		mTextViewExplanation.setText(getPreferenceExplanation());

		mButtonUpdatePreferences = (Button) findViewById(R.id.buttonUpdatePreferences);
		mButtonUpdatePreferences.setEnabled(false);
		mButtonUpdatePreferences.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onUpdateAttributeValuePreferences();
			}
		});

		mAdapter = new PriceRangeAdapter(this);
		mAdapter.clear();
		mAdapter.addAll(attribute().getAttributeValues());

		mListView = (ListView) findViewById(R.id.listViewAttributeValueList);
		mListView.setOnItemClickListener(this);
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		mListView.setAdapter(mAdapter);
	}

	protected int numColums() {
		return 4;
	}

	protected String getPreferenceExplanation() {
		String baseExplanation = getString(R.string.attribute_value_preference_update_explanation);
		return String.format(baseExplanation, attribute().id());
	}

	protected void onUpdateAttributeValuePreferences() {
		SparseBooleanArray checkedPositions = mListView
				.getCheckedItemPositions();
		Set<AttributeValue> attributeValues = new HashSet<AttributeValue>();
		for (int i = 0; i < mAdapter.getCount(); i++) {
			if (checkedPositions.get(i)) {
				attributeValues.add(mAdapter.getItem(i));
			}
		}
		onUpdatePreferencesFinish();
	}

	@Override
	public void onItemClick(AdapterView<?> a, View v, int arg2, long arg3) {
		mButtonUpdatePreferences.setEnabled(isButtonEnabled());
	}

	private boolean isButtonEnabled() {
		return hasAtLeastOneItemChecked();
	}

	private boolean hasAtLeastOneItemChecked() {
		boolean itemChecked = false;
		SparseBooleanArray checkedPositions = mListView
				.getCheckedItemPositions();
		for (int i = 0; i < checkedPositions.size(); i++) {
			if (checkedPositions.valueAt(i)) {
				itemChecked = true;
				break;
			}
		}

		return itemChecked;
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
