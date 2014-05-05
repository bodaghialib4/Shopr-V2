package com.uwetrottmann.shopr.ui;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uwetrottmann.androidutils.Lists;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.model.Shop;
import com.uwetrottmann.shopr.event.ShopUpdateEvent;
import com.uwetrottmann.shopr.model.Constraints;
import com.uwetrottmann.shopr.model.ShoprShop;
import com.uwetrottmann.shopr.settings.AppSettings;
import com.uwetrottmann.shopr.ui.LocationHandler.LocationUpdateEvent;

import de.greenrobot.event.EventBus;

public abstract class ShopMapFragment extends SupportMapFragment implements
		LoaderCallbacks<List<ShoprShop>> {
	public static final String TAG = "Shops Map";

	private boolean mIsInitialized = false;
	protected List<Marker> mShopMarkers;
	protected Map<Integer, Integer> mShopsWithItems;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mIsInitialized = false;

		// enable my location feature
		getMap().setMyLocationEnabled(
				!AppSettings.isUsingFakeLocation(getActivity()));

		initShopLoader();
	}

	protected abstract int loaderId();

	private void initShopLoader() {
		getLoaderManager().initLoader(loaderId(), null, this);
	}

	private void restartShopLoader() {
		getLoaderManager().restartLoader(loaderId(), null, this);
	}

	@Override
	public void onStart() {
		super.onStart();
		registerLocationEvents();
	}

	@Override
	public void onStop() {
		unregisterLocationEvents();
		super.onStop();
	}

	protected void registerLocationEvents() {
		  EventBus.getDefault()
          .registerSticky(this, LocationUpdateEvent.class, ShopUpdateEvent.class);
	}

	protected void unregisterLocationEvents() {
		EventBus.getDefault().unregister(this);
	}

	public void onEvent(LocationUpdateEvent event) {
		onInitializeMap();
	}

	public void onEvent(ShopUpdateEvent event) {
		mShopsWithItems = event.shopMap;
		restartShopLoader();
	}

	private void onInitializeMap() {
		if (!mIsInitialized) {
			Log.d(TAG, "Initializing map.");

			drawUserPosition();

			mIsInitialized = true;
		}
	}

	private void drawUserPosition() {
		LatLng userPosition = LocationHandler.getInstance(getActivity())
				.getLastLocation();
		if (userPosition == null) {
			return;
		}
		drawUserIndicator(userPosition);
		moveCameraTo(userPosition);
		drawCircleAround(userPosition);
	}
	
	private void drawUserIndicator(LatLng position) {
		getMap().addMarker(
				new MarkerOptions().position(position)
						.title(getString(R.string.user_position_you))
						.snippet(getString(R.string.user_position_definition))
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	}

	private void moveCameraTo(LatLng position) {
		getMap().moveCamera(
				CameraUpdateFactory.newLatLngZoom(position, zoomLevelInitial()));
	}

	protected int zoomLevelInitial() {
		return 14;
	}

	private void drawCircleAround(LatLng position) {
		// draw a circle around it
		getMap().addCircle(
				new CircleOptions()
						.center(position)
						.radius(Constraints.RADIUS_METERS)
						.strokeColor(getResources().getColor(R.color.lilac))
						.strokeWidth(4)
						.fillColor(
								getResources().getColor(
										R.color.lilac_transparent)));
	}
	
	private void onUpdateShops(List<ShoprShop> shops) {
		cleanupMarkers();
		drawShopMarkers(shops);
	}

	private void cleanupMarkers() {
		removeExistingMarkes();
		mShopMarkers = Lists.newArrayList();

	}

	private void removeExistingMarkes() {
		if (mShopMarkers != null) {
			for (Marker marker : mShopMarkers) {
				marker.remove();
			}
		}

	}
		
	private void drawShopMarkers(List<ShoprShop> shops) {
		for (ShoprShop shop : shops) {
			if (hasRelativeItems(shop)) {
				int numItemsAtShop = mShopsWithItems.get(shop.id());
				mShopMarkers.add(createMarker(shop,
						BitmapDescriptorFactory.HUE_VIOLET, numItemsAtShop));
			} else if (shouldDrawEmptyShops()) {
				mShopMarkers.add(createMarker(shop,
						BitmapDescriptorFactory.HUE_AZURE, 0));
			}
		}
	}

	private boolean hasRelativeItems(Shop shop) {
		return mShopsWithItems != null
				&& mShopsWithItems.containsKey(shop.id());
	}

	private Marker createMarker(ShoprShop shop, float color, int numItemsAtShop) {
		return getMap().addMarker(
				new MarkerOptions().position(shop.location())
						.title(shop.name())
						.snippet(shopInfoOnMap(numItemsAtShop))
						.icon(BitmapDescriptorFactory.defaultMarker(color)));
	}

	public abstract boolean shouldDrawEmptyShops();

	protected abstract String shopInfoOnMap(int numItmesAtShop);

	@Override
	public void onLoadFinished(Loader<List<ShoprShop>> loader,
			List<ShoprShop> data) {
		onUpdateShops(data);
	}

	@Override
	public void onLoaderReset(Loader<List<ShoprShop>> laoder) {
	}
}
