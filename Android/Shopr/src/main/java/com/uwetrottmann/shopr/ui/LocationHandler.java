package com.uwetrottmann.shopr.ui;

import android.app.Dialog;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

import de.greenrobot.event.EventBus;


public class LocationHandler {
	private static String TAG = "LocationHandler";
	private FragmentActivity activity;
	private LatLng mLastLocation;
	private LocationClient mLocationClient;
	private static LocationHandler instance;
	
	
	/*
	 * Define a request code to send to Google Play services. This code is
	 * returned in Activity.onActivityResult.
	 */
	public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
	private LocationHandler(FragmentActivity activity) {
		this.activity = activity;
	}
	
	public static LocationHandler getInstance(FragmentActivity activity) {
		if(instance == null)
			instance = new LocationHandler(activity);
		return instance;
	}
	
	public void setLastLocation(LatLng lastLocation) {
		mLastLocation = lastLocation;
	}
	
	public void connectLocationClient() {
		mLocationClient.connect();
	}
	
	public void disconnectLocationClient() {
		mLocationClient.disconnect();
	}
	
	public void initLocationClientOrExit() {
		// Check if Google Play services is installed
		if (!servicesConnected()) {
			return;
		}
		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
		mLocationClient = new LocationClient(activity,
				new GoogleServicesConnectionCallbacks(),
				GoogleServicesConnectionFailedListener.newInstance(activity));
	}
	
	/**
	 * Verify that Google Play services is available before making a request.
	 * 
	 * @return true if Google Play services is available, otherwise false
	 */
	private boolean servicesConnected() {

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(activity);

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
					activity, 0);
			if (dialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				errorFragment.show(activity.getSupportFragmentManager(), TAG);
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
	
	public LocationUpdateEvent newLocationUpdateEvent() {
		return new LocationUpdateEvent();
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
