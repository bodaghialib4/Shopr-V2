package com.uwetrottmann.shopr.ui.explanation;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.Preference;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;
import com.uwetrottmann.shopr.eval.Statistics;

public abstract class PreferenceActivity extends Activity implements
		OnItemClickListener {
	
	public static String EXTRAS_ATTRIBUTE_VALUE = "toEdit";

	private Button mButtonUpdatePreferences;
	private ArrayAdapter<AttributeValue> mAdapter;
	private AbsListView mAttributeValueListView;
	private String attributeValue = "";

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent().hasExtra(EXTRAS_ATTRIBUTE_VALUE))
			attributeValue = getIntent().getExtras().getString(EXTRAS_ATTRIBUTE_VALUE);
		setContentView(layout());
		setupViews();
	}

	protected abstract int layout();

	protected final void setupViews() {
		initExplanationView();
		mButtonUpdatePreferences = initUpdatePreferenceButton();
		mAdapter = initAdapter();
		mAttributeValueListView = initAttributeValueList(mAdapter);
	}

	protected final TextView initExplanationView() {
		TextView explanation = explanationView();
		String template = getString(R.string.attribute_value_preference_update_explanation_attribute_value);
		String startTemplate = getString(R.string.attribute_value_preference_update_explanation_start);
		CharSequence changingAttributeValue = Html.fromHtml(String.format(template, attributeValue));
		CharSequence exp = attributeValue.isEmpty() ? TextUtils.concat(startTemplate, getPreferenceExplanation()) : TextUtils.concat(changingAttributeValue, getPreferenceExplanation());
		explanation.setText(exp);
		return explanation;
	}

	protected final Button initUpdatePreferenceButton() {
		Button updateButton = updatePreferenceButton();
		updateButton.setEnabled(false);
		updateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onUpdateAttributeValuePreferences();
			}
		});
		return updateButton;
	}

	protected final ArrayAdapter<AttributeValue> initAdapter() {
		ArrayAdapter<AttributeValue> adapter = attributeValueAdapter();
		adapter.clear();
		adapter.addAll(attribute().getAttributeValues());
		return adapter;
	}

	protected abstract Attribute attribute();

	protected final AbsListView initAttributeValueList(
			ArrayAdapter<AttributeValue> adapter) {
		AbsListView absListView = attributeValueList();
		absListView.setOnItemClickListener(this);
		absListView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
		absListView.setAdapter(adapter);
		return absListView;
	}

	protected abstract TextView explanationView();

	protected abstract Button updatePreferenceButton();

	protected abstract ArrayAdapter<AttributeValue> attributeValueAdapter();

	protected abstract AbsListView attributeValueList();

	protected final String getPreferenceExplanation() {
		String baseExplanation = getString(R.string.attribute_value_preference_update_explanation);
		return String.format(baseExplanation, attribute().id());
	}

	protected final void onUpdateAttributeValuePreferences() {
		SparseBooleanArray checkedPositions = mAttributeValueListView
				.getCheckedItemPositions();
		Preference preference = new Preference(attribute());
		for (int i = 0; i < mAdapter.getCount(); i++) {
			if (checkedPositions.get(i)) {
				preference.add(mAdapter.getItem(i));
			}
		}
		AdaptiveSelection.get().submitPreference(preference);
		
		// Record cycles
        Statistics.get().incrementExplicityPreferenceChangeCount();
		onPreferencesUpdateFinish();
	}

	protected void onPreferencesUpdateFinish() {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public final void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		mButtonUpdatePreferences.setEnabled(isButtonEnabled());
	}

	private boolean isButtonEnabled() {
		return hasAtLeastOneItemChecked();
	}

	private boolean hasAtLeastOneItemChecked() {
		boolean itemChecked = false;
		SparseBooleanArray checkedPositions = mAttributeValueListView
				.getCheckedItemPositions();
		for (int i = 0; i < checkedPositions.size(); i++) {
			if (checkedPositions.valueAt(i)) {
				itemChecked = true;
				break;
			}
		}

		return itemChecked;
	}

}
