package com.uwetrottmann.shopr.eval;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.provider.ShoprContract.Stats;
import com.uwetrottmann.shopr.ui.basic.MainActivityBasic;
import com.uwetrottmann.shopr.ui.explanation.MainActivityExplanation;

/**
 * Displays the given stats and allows to start a new task.
 */
public class ResultsActivity extends Activity {

	public interface InitBundle {
		String STATS_ID = "stats_id";
		String ITEM_ID = "item_id";
	}

	private int mStatId;
	private Item mItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_activity_results);

		// extract stat id
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			finish();
			return;
		}
		// get stats for this eval task
		mStatId = extras.getInt(InitBundle.STATS_ID);
		// get the item the user selected
		int itemId = extras.getInt(InitBundle.ITEM_ID);
		List<Item> currentCaseBase = AdaptiveSelection.get()
				.getCurrentRecommendations();
		for (Item item : currentCaseBase) {
			if (item.id() == itemId) {
				mItem = item;
				break;
			}
		}

		if (mItem == null) {
			finish();
			return;
		}

		setupViews();
	}

	private void setupViews() {
		TextView textViewUserName = (TextView) findViewById(R.id.textViewResultsUsername);
		TextView textViewTaskType = (TextView) findViewById(R.id.textViewResultsTaskType);
		TextView textViewDuration = (TextView) findViewById(R.id.textViewResultsDuration);
		TextView textViewCycles = (TextView) findViewById(R.id.textViewResultsCycles);
		Button exit = (Button) findViewById(R.id.exitStatsButton);

		final Cursor query = getContentResolver().query(
				Stats.buildStatUri(mStatId),
				new String[] { Stats._ID, Stats.USERNAME, Stats.TASK_TYPE,
						Stats.DURATION, Stats.CYCLE_COUNT }, null, null, null);
		if (query != null) {
			if (query.moveToFirst()) {
				final String username = query.getString(1);
				final String task = query.getString(2);
				final long durationInMillis = query.getLong(3);
				final String numCycles = query.getString(4);

				textViewUserName.setText(username);
				textViewTaskType.setText(task);
				long duration = durationInMillis / DateUtils.SECOND_IN_MILLIS;
				textViewDuration.setText(String.format("%dh:%02dm:%02ds",
						duration / 3600, (duration % 3600) / 60,
						(duration % 60)) + " ("+ duration + " seconds)");
				textViewCycles.setText(numCycles);
				exit.setOnLongClickListener(new View.OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						exitTask(task);
						return false;
					}
				});
				exit.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				});
			}
			query.close();
		}

		// display selected item info
		TextView textViewItem = (TextView) findViewById(R.id.textViewResultsItem);
		textViewItem.setText(mItem.id() + " " + mItem.name() + " "
				+ mItem.attributes().getReasonString());
		ImageView imageViewItem = (ImageView) findViewById(R.id.imageViewResultsItemPicture);
		Picasso.with(this)
				.load(mItem.mainImage())
				.resizeDimen(R.dimen.default_image_width,
						R.dimen.default_image_height).centerCrop()
				.into(imageViewItem);
	}

	public void exitTask(String task) {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// do nothing, prevents accidental exits.
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_restart:
			// go back to setup activity
			startActivity(new Intent(this, TestSetupActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
