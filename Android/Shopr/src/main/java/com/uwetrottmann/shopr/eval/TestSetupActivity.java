package com.uwetrottmann.shopr.eval;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.model.RecommendationAlgorithm;
import com.uwetrottmann.shopr.settings.AppSettings;
import com.uwetrottmann.shopr.ui.SettingsActivity;

public class TestSetupActivity extends Activity implements
		OnItemSelectedListener {

	private static final String TAG = "Test Setup";

	private EditText mNameEditText;
	private Spinner taskSpinner;
	private VariantTask selectedTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_activity_test_setup);

		setupViews();
		setupActionBar();
		selectedTask = VariantTask.all()[0];
	}

	private void setupViews() {
		View startButton = findViewById(R.id.buttonTestSetupStart);
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onStartTest();
			}
		});

		mNameEditText = (EditText) findViewById(R.id.editTextTestSetupName);
		String prevUserName = Statistics.get().getUserName();
		mNameEditText.setText(TextUtils.isEmpty(prevUserName) ? "thisis"
				+ new Random().nextInt(999999) : prevUserName);

		taskSpinner = (Spinner) findViewById(R.id.taskSpinner);
		ArrayAdapter<String> taskAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				VariantTask.allTaskNames());
		taskAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		taskSpinner.setAdapter(taskAdapter);
		taskSpinner.setOnItemSelectedListener(this);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	protected void onStartTest() {
		if (TextUtils.isEmpty(mNameEditText.getText())) {
			Toast.makeText(this, "Please supply a name or pseudonym.",
					Toast.LENGTH_LONG).show();
			return;
		}

		// set diversity on
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putBoolean(AppSettings.KEY_USING_DIVERSITY, true).commit();
		Log.d(TAG, "Setting diversity to checked.");

		// record name, time and type, start task
		Statistics.get().startTask(mNameEditText.getText().toString(),
				selectedTask);
		
		RecommendationAlgorithm.restart(this);
		selectedTask.getTaskListener().onStart(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_setup, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		taskSpinner.setSelection(position);
		selectedTask = VariantTask.all()[position];
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// we ensure the first is always selected by default at least
	}

}
