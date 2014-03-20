package com.uwetrottmann.shopr.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.adapters.NavDrawerAdapter;
import com.uwetrottmann.shopr.eval.TestSetupActivity;
import com.uwetrottmann.shopr.importer.ImporterActivity;
import com.uwetrottmann.shopr.model.NavDrawerItem;
import com.uwetrottmann.shopr.model.NavMenuItem;
import com.uwetrottmann.shopr.model.NavMenuSection;
import com.uwetrottmann.shopr.model.ui.NavDrawerActivityConfiguration;
import com.uwetrottmann.shopr.settings.AppSettings;

import de.greenrobot.event.EventBus;

public class MainActivity extends AbstractNavDrawerActivity {

	private static final String TAG = "Shopr";

	/*
	 * Define a request code to send to Google Play services. This code is
	 * returned in Activity.onActivityResult.
	 */
	public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private LocationClient mLocationClient;
	private LatLng mLastLocation;

	private NavDrawerActivityConfiguration navDrawerActivityConfiguration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initLocationClientOrExit();
		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.content,
							PageSlidingTabStripFragment.newInstance()).commit();
		}
	}

	@Override
	protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

		NavDrawerItem[] menu = new NavDrawerItem[] {
				NavMenuItem.create(101,
						getString(R.string.drawer_section_title_home),
						"ic_menu_home", false, this),
				NavMenuItem.create(102,
						getString(R.string.drawer_section_title_favourites),
						"ic_menu_star", true, this),
				NavMenuSection.create(200,
						getString(R.string.drawer_section_title_mind_map)),
				NavMenuItem.create(202, "Clothing Type", "navdrawer_rating",
						false, this),
				NavMenuItem.create(203, "Color", "navdrawer_eula", false, this),
				NavMenuItem
						.create(204, "Gender", "navdrawer_quit", false, this),
				NavMenuItem.create(205, "Price Range", "navdrawer_quit", false,
						this) };

		navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
		navDrawerActivityConfiguration
				.setMainLayout(R.layout.activity_main_drawer);
		navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
		navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
		navDrawerActivityConfiguration.setNavItems(menu);
		navDrawerActivityConfiguration
				.setDrawerShadow(R.drawable.drawer_shadow);
		navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
		navDrawerActivityConfiguration
				.setDrawerCloseDesc(R.string.drawer_close);
		navDrawerActivityConfiguration.setBaseAdapter(new NavDrawerAdapter(
				this, R.layout.drawer_list_item, menu));
		return navDrawerActivityConfiguration;
	}

	@Override
	protected void onNavItemSelected(int id) {
		switch ((int) id) {
		case 101:
			replaceContent(PageSlidingTabStripFragment.newInstance());
			break;
		case 102:
			replaceContent(FavouriteItemListFragment.newInstance());
			break;
		case 202:
			replaceContent(MindMapFragment.newInstance());
			break;
		case 203:
			replaceContent(MindMapClothingTypeFragment.newInstance());
			break;
		}
	}

	private void replaceContent(Fragment fragment) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content, fragment).commit();
	}

	private void initLocationClientOrExit() {
		// Check if Google Play services is installed
		if (!servicesConnected()) {
			return;
		}
		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
		mLocationClient = new LocationClient(this,
				new GoogleServicesConnectionCallbacks(),
				GoogleServicesConnectionFailedListener.newInstance(this));
	}

	@Override
	public void onStart() {
		super.onStart();
		if (AppSettings.isUsingFakeLocation(this)) {
			// use fake location (Marienplatz, Munich)
			mLastLocation = new LatLng(48.137314, 11.575253);
			// send out location update event immediately
			EventBus.getDefault().postSticky(new LocationUpdateEvent());
		} else {
			mLocationClient.connect();
		}

		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	public void onStop() {
		if (!AppSettings.isUsingFakeLocation(this)) {
			mLocationClient.disconnect();
		}
		super.onStop();

		EasyTracker.getInstance().activityStop(this);
	}

	@Override
	public void onBackPressed() {
		// do nothing, prevents accidental back presses
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_restart_test:
			startActivity(new Intent(this, TestSetupActivity.class));
			// clean this activity up
			finish();
			return true;
		case R.id.action_import:
			startActivity(new Intent(this, ImporterActivity.class));
			return true;
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Verify that Google Play services is available before making a request.
	 * 
	 * @return true if Google Play services is available, otherwise false
	 */
	private boolean servicesConnected() {

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d(TAG, "Google Play services available");

			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Display an error dialog
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
					this, 0);
			if (dialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				errorFragment.show(getSupportFragmentManager(), TAG);
			}
			return false;
		}
	}

	/**
	 * Define a DialogFragment to display the error dialog generated in
	 * showErrorDialog.
	 */

	public void getLocation() {
		// If Google Play Services is available
		if (servicesConnected()) {
			// Get the current location
			Location lastLocation = mLocationClient.getLastLocation();
			mLastLocation = new LatLng(lastLocation.getLatitude(),
					lastLocation.getLongitude());
			EventBus.getDefault().postSticky(new LocationUpdateEvent());
		}
	}

	public LatLng getLastLocation() {
		return mLastLocation;
	}

	public class LocationUpdateEvent {
	}

	private class GoogleServicesConnectionCallbacks implements
			GooglePlayServicesClient.ConnectionCallbacks {

		@Override
		public void onConnected(Bundle dataBundle) {
			Log.d(TAG, "Connected to location service");
			getLocation();
		}

		@Override
		public void onDisconnected() {
			Log.d(TAG, "Disconnected from location service.");
		}

	}

	private static class GoogleServicesConnectionFailedListener implements
			GooglePlayServicesClient.OnConnectionFailedListener {
		private FragmentActivity activity;

		public static GoogleServicesConnectionFailedListener newInstance(
				FragmentActivity activity) {
			return new GoogleServicesConnectionFailedListener()
					.setActivity(activity);
		}

		public GoogleServicesConnectionFailedListener setActivity(
				FragmentActivity activity) {
			this.activity = activity;
			return this;
		}

		@Override
		public void onConnectionFailed(ConnectionResult connectionResult) {
			/*
			 * Google Play services can resolve some errors it detects. If the
			 * error has a resolution, try sending an Intent to start a Google
			 * Play services activity that can resolve error.
			 */
			if (connectionResult.hasResolution()) {
				try {
					// Start an Activity that tries to resolve the error
					connectionResult.startResolutionForResult(activity,
							CONNECTION_FAILURE_RESOLUTION_REQUEST);
					/*
					 * Thrown if Google Play services canceled the original
					 * PendingIntent
					 */
				} catch (IntentSender.SendIntentException e) {
					// Log the error
					e.printStackTrace();
				}
			} else {
				/*
				 * If no resolution is available, display a dialog to the user
				 * with the error.
				 */
				showErrorDialog(connectionResult.getErrorCode());
			}
		}

		/**
		 * Show a dialog returned by Google Play services for the connection
		 * error code
		 * 
		 * @param errorCode
		 *            An error code returned from onConnectionFailed
		 */
		private void showErrorDialog(int errorCode) {
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					errorCode, activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment in which to show the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				errorFragment.show(activity.getSupportFragmentManager(), TAG);
			}
		}
	}

}
