package com.uwetrottmann.shopr.eval;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.google.analytics.tracking.android.EasyTracker;
import com.uwetrottmann.shopr.provider.ShoprContract.Stats;

/**
 * Stores data about the current task.
 */
public class Statistics {

	private static Statistics _instance;

	private long mStartTime;
	private int mCritiqueCount;
	private int mExplicitPreferenceChangeCount;
	private int mCritiquePositiveCount;
	private String mUserName;
	private VariantTask mTask;

	private boolean mIsStarted;

	private Statistics() {

	}
	
	public int cycleCount() {
		return mCritiqueCount + mExplicitPreferenceChangeCount;
	}

	public synchronized static Statistics get() {
		if (_instance == null) {
			_instance = new Statistics();
		}
		return _instance;
	}

	/**
	 * Saves the current time, user name and task type until
	 * {@link #finishTask(Context)} is called.
	 */
	public synchronized void startTask(String username, VariantTask task) {
		mIsStarted = true;
		mUserName = username;
		mTask = task;
		mStartTime = System.currentTimeMillis();
		mCritiqueCount = 0;
		mCritiquePositiveCount = 0;
		mExplicitPreferenceChangeCount = 0;
	}
	
	/**
	 * Increases the recommendation cycle count by 1. Also the positive cycle
	 * count if isPositive is true.
	 */
	public synchronized void incrementExplicityPreferenceChangeCount() {
		mExplicitPreferenceChangeCount++;
	}

	/**
	 * Increases the recommendation cycle count by 1. Also the positive cycle
	 * count if isPositive is true.
	 */
	public synchronized void incrementCritiqueCount(boolean isPositive) {
		mCritiqueCount++;
		if (isPositive) {
			mCritiquePositiveCount++;
		}
	}

	/**
	 * Stops the task and writes all data to the database.
	 * 
	 * @return The {@link Uri} pointing to the new data set or {@code null} if
	 *         {@link #startTask(String, boolean)} was not called before.
	 */
	public synchronized Uri finishTask(Context context) {
		if (!mIsStarted) {
			return null;
		}

		mIsStarted = false;
		long duration = System.currentTimeMillis() - mStartTime;

		// Write to database
		ContentValues statValues = new ContentValues();
		statValues.put(Stats.USERNAME, mUserName);
		statValues.put(Stats.TASK_TYPE, mTask.toString());
		statValues.put(Stats.CRITIQUE_COUNT, mCritiqueCount);
		statValues.put(Stats.PREFERENCE_CHANGE_COUNT, mExplicitPreferenceChangeCount);
		statValues.put(Stats.CYCLE_COUNT, cycleCount());
		statValues.put(Stats.DURATION, duration);
		final Uri inserted = context.getContentResolver().insert(
				Stats.CONTENT_URI, statValues);

		EasyTracker.getTracker().sendEvent("Results", "Type", mTask.toString(),
				(long) 0);
		EasyTracker.getTracker().sendEvent("Results", "Value", "Cycles",
				(long) mCritiqueCount);
		EasyTracker.getTracker().sendEvent("Results", "Value",
				"Cycles (positive)", (long) mCritiquePositiveCount);
		EasyTracker.getTracker().sendEvent("Results", "Value", "Duration",
				duration);

		return inserted;
	}

	public synchronized String getUserName() {
		return mUserName;
	}

}
