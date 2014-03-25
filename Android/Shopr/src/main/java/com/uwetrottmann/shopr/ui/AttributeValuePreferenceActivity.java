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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

public abstract class AttributeValuePreferenceActivity extends Activity
		implements OnItemClickListener {

	protected TextView mTextViewExplanation;
	protected GridView mGridView;
	protected Button mButtonUpdatePreferences;
	protected AttributeValueAdapter mAdapter;

	protected abstract Attribute attribute();

	protected abstract void onUpdatePreferencesFinish();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attribute_value_preference);
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

		mAdapter = new AttributeValueAdapter(this);
		mAdapter.clear();
		mAdapter.addAll(attribute().getAttributeValues());

		mGridView = (GridView) findViewById(R.id.gridViewAttributeValueList);
		mGridView.setOnItemClickListener(this);
		mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
		mGridView.setAdapter(mAdapter);
	}

	protected String getPreferenceExplanation() {
		String baseExplanation = getString(R.string.attribute_value_preference_update_explanation);
		return String.format(baseExplanation, attribute().id());
	}

	protected void onUpdateAttributeValuePreferences() {
		SparseBooleanArray checkedPositions = mAdapter.getCheckedPositions();
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
		SparseBooleanArray checkedPositions = mGridView
				.getCheckedItemPositions();
		for (int i = 0; i < checkedPositions.size(); i++) {
			if (checkedPositions.valueAt(i)) {
				itemChecked = true;
				break;
			}
		}

		return itemChecked;
	}

	public class AttributeValueAdapter extends ArrayAdapter<AttributeValue> {

		private static final int LAYOUT = R.layout.attribute_value_layout;
		private LayoutInflater mInflater;
		private SparseBooleanArray mCheckedPositions = new SparseBooleanArray();

		public AttributeValueAdapter(Context context) {
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
				holder.pictureContainer = convertView
						.findViewById(R.id.containerAttributeValuePicture);
				holder.picture = (ImageView) convertView
						.findViewById(R.id.imageViewAttributeValuePicture);
				holder.name = (TextView) convertView
						.findViewById(R.id.textViewAttributeValueName);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final AttributeValue value = getItem(position);
			holder.name.setText(value.descriptor());
			holder.picture.setBackgroundResource(R.drawable.ic_action_tshirt);

			return convertView;
		}

		public SparseBooleanArray getCheckedPositions() {
			return mCheckedPositions;
		}

		class ViewHolder {
			View pictureContainer;
			ImageView picture;
			TextView name;
		}
	}

}
