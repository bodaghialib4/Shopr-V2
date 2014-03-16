package com.uwetrottmann.shopr.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.adapters.NavDrawerListAdapter;
import com.uwetrottmann.shopr.eval.TestSetupActivity;
import com.uwetrottmann.shopr.importer.ImporterActivity;
import com.uwetrottmann.shopr.model.NavDrawerItem;
import com.uwetrottmann.shopr.settings.AppSettings;

import de.greenrobot.event.EventBus;

public class MainActivity extends FragmentActivity {

	private static final String TAG = "Shopr";

	/*
	 * Define a request code to send to Google Play services. This code is
	 * returned in Activity.onActivityResult.
	 */
	public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private LocationClient mLocationClient;
	private LatLng mLastLocation;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private List<NavDrawerItem> navDrawerItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_drawer);

		setupDrawer(savedInstanceState);
		setupLocationClientOrExitIfNoServiceExist();
	}

	private void setupDrawer(Bundle savedInstanceState) {
		mDrawerTitle = getTitle();
		// mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
				mDrawerLayout, /* DrawerLayout object */
				R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open, /* "open drawer" description for accessibility */
				R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				//getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		setupDrawerSections();

		if (savedInstanceState == null) {
			displayNavDrawerItem(0);
		}
	}

	private void setupDrawerSections() {
		navDrawerItems = new ArrayList<NavDrawerItem>();
		navDrawerItems.add(
				new NavDrawerItem()
				.fragment(PageSlidingTabStripFragment.newInstance())
				.title(getString(R.string.drawer_section_title_home))
				.icon(R.drawable.ic_menu_home));
		
		navDrawerItems.add(
				new NavDrawerItem()
				.fragment(FavouriteItemListFragment.newInstance())
				.title(getString(R.string.drawer_section_title_favourites))
				.icon(R.drawable.ic_menu_star));
		
		navDrawerItems.add(
				new NavDrawerItem()
				.fragment(MindMapFragment.newInstance())
				.title(getString(R.string.drawer_section_title_mind_map))
				.icon(R.drawable.ic_menu_preferences));	
		setupDrawerList();
	}
	
	private void setupDrawerList(){
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setAdapter(new NavDrawerListAdapter(this, navDrawerItems));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}
	
	private void setupLocationClientOrExitIfNoServiceExist() {
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
	
	private void displayNavDrawerItem(int position) {
		NavDrawerItem drawerItem = navDrawerItems.get(position);
		
	    if (drawerItem.getFragment() != null) {
	    	getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.content, drawerItem.getFragment()).commit();
 
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            getActionBar().setTitle(drawerItem.getTitle());
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e(TAG, "Error in creating navigation drawer fragment");
        }
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
		case android.R.id.home: {
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
			break;
		}
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


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	// The click listener for ListView in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			displayNavDrawerItem(position);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
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
