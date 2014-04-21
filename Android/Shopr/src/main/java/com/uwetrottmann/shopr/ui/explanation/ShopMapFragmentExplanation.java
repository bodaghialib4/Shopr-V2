
package com.uwetrottmann.shopr.ui.explanation;

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
import com.uwetrottmann.shopr.loaders.ShopLoader;
import com.uwetrottmann.shopr.model.Constraints;
import com.uwetrottmann.shopr.model.ShoprShop;
import com.uwetrottmann.shopr.settings.AppSettings;
import com.uwetrottmann.shopr.ui.explanation.ItemListFragmentExplanation.ShopUpdateEvent;
import com.uwetrottmann.shopr.ui.explanation.MainActivityExplanation.LocationUpdateEvent;

import de.greenrobot.event.EventBus;

import java.util.List;
import java.util.Map;

public class ShopMapFragmentExplanation extends SupportMapFragment implements LoaderCallbacks<List<ShoprShop>> {

    private static final int ZOOM_LEVEL_INITIAL = 14;
    public static final String TAG = "Shops Map";
    private static final int LAODER_ID = 22;

    public static ShopMapFragmentExplanation newInstance() {
        return new ShopMapFragmentExplanation();
    }

    private List<Marker> mShopMarkers;
    private boolean mIsInitialized;
    private Map<Integer, Integer> mShopsWithItems;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mIsInitialized = false;

        // enable my location feature
        getMap().setMyLocationEnabled(!AppSettings.isUsingFakeLocation(getActivity()));

        getLoaderManager().initLoader(LAODER_ID, null, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault()
                .registerSticky(this, LocationUpdateEvent.class, ShopUpdateEvent.class);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void onInitializeMap() {
        if (!mIsInitialized) {
            Log.d(TAG, "Initializing map.");

            LatLng userPosition = ((MainActivityExplanation) getActivity()).getLastLocation();
            if (userPosition == null) {
                return;
            }

            // move camera to current position
            getMap().moveCamera(
                    CameraUpdateFactory.newLatLngZoom(userPosition, ZOOM_LEVEL_INITIAL));
            // draw a circle around it
            getMap().addCircle(new CircleOptions()
                    .center(userPosition)
                    .radius(Constraints.RADIUS_METERS)
                    .strokeColor(getResources().getColor(R.color.lilac))
                    .strokeWidth(4)
                    .fillColor(getResources().getColor(R.color.lilac_transparent)));

            mIsInitialized = true;
        }
    }

    private void onUpdateShops(List<ShoprShop> shops) {
        // remove existing markers
        if (mShopMarkers != null) {
            for (Marker marker : mShopMarkers) {
                marker.remove();
            }
        }

        List<Marker> shopMarkersNew = Lists.newArrayList();

        for (ShoprShop shop : shops) {
            // determine color and recom. items in this shop
            float color;
            int itemCount;
            if (mShopsWithItems != null && mShopsWithItems.containsKey(shop.id())) {
                itemCount = mShopsWithItems.get(shop.id());
                color = BitmapDescriptorFactory.HUE_VIOLET;
            } else {
                itemCount = 0;
                color = BitmapDescriptorFactory.HUE_AZURE;
            }

            // place marker
            Marker marker = getMap().addMarker(
                    new MarkerOptions()
                            .position(shop.location())
                            .title(shop.name())
                            .snippet(getString(R.string.has_x_recommendations, itemCount))
                            .icon(BitmapDescriptorFactory.defaultMarker(color)));
            shopMarkersNew.add(marker);
        }

        mShopMarkers = shopMarkersNew;
    }

    public void onEvent(LocationUpdateEvent event) {
        onInitializeMap();
    }

    public void onEvent(ShopUpdateEvent event) {
        mShopsWithItems = event.shopMap;
        getLoaderManager().restartLoader(LAODER_ID, null, this);
    }

    @Override
    public Loader<List<ShoprShop>> onCreateLoader(int loaderId, Bundle args) {
        return new ShopLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<ShoprShop>> loader, List<ShoprShop> data) {
        onUpdateShops(data);
    }

    @Override
    public void onLoaderReset(Loader<List<ShoprShop>> laoder) {
    }

}
