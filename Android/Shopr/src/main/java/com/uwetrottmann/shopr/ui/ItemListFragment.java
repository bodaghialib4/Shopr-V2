package com.uwetrottmann.shopr.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.uwetrottmann.androidutils.Maps;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.eval.ResultsActivity;
import com.uwetrottmann.shopr.eval.Statistics;
import com.uwetrottmann.shopr.event.ShopUpdateEvent;
import com.uwetrottmann.shopr.listeners.ShoprListeners.OnItemCritiqueListener;
import com.uwetrottmann.shopr.listeners.ShoprListeners.OnItemDisplayListener;
import com.uwetrottmann.shopr.listeners.ShoprListeners.OnItemFavouriteListener;
import com.uwetrottmann.shopr.loaders.ItemLoader;
import com.uwetrottmann.shopr.provider.ShoprContract.Stats;
import com.uwetrottmann.shopr.ui.LocationHandler.LocationUpdateEvent;
import com.uwetrottmann.shopr.utils.FavouriteItemUtils;

import de.greenrobot.event.EventBus;

public abstract class ItemListFragment<T> extends Fragment implements LoaderCallbacks<List<Item>>,
OnItemCritiqueListener, OnItemDisplayListener, OnItemFavouriteListener{
	public static final String TAG = "Item List";
			 
    // I = 9, T = 20
    private static final int LOADER_ID = 920;
    public static final int REQUEST_CODE = 12;
  
    private ArrayAdapter<T> mAdapter;

    private boolean mIsInitialized;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = createAdapter();
        setAdapter(mAdapter);
        
        Bundle args = new Bundle();
        args.putBoolean("isinit", false);
        getLoaderManager().initLoader(LOADER_ID, args, this);
        
        setHasOptionsMenu(true);
    }
    
    public abstract ArrayAdapter<T> createAdapter();
    
    public abstract void setAdapter(ArrayAdapter<T> adapter);
    
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this, LocationUpdateEvent.class);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_restart:
                onInitializeItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public Loader<List<Item>> onCreateLoader(int loaderId, Bundle args) {
        boolean isInit = false;
        if (args != null) {
            isInit = args.getBoolean("isinit");
        }
        LatLng location = LocationHandler.getInstance(getActivity()).getLastLocation();
        
        return new ItemLoader(getActivity(), this, location, isInit);
    }

    @Override
    public void onLoadFinished(Loader<List<Item>> loader, List<Item> items) {
        onUpdateItems(items);
        onUpdateShops(items);
    }
    
    private final void onUpdateItems(List<Item> items) {
        mAdapter.clear();
    	mAdapter.addAll(explainOrLeaveSame(items));
    	afterUpdateItems();
    }
    
    protected abstract List<T> explainOrLeaveSame(List<Item> items);
    
    protected abstract void afterUpdateItems();
    
    /**
     * Post {@link ShopUpdateEvent} based on current list of recommendations.
     * 
     * @param data
     */
    private void onUpdateShops(List<Item> items) {
        ShopUpdateEvent event = new ShopUpdateEvent();

        // get shops and number of items per shop
        event.shopMap = Maps.newHashMap();
        for (Item item : items) {
            int shopId = item.shopId();
            int count = 1;

            if (event.shopMap.containsKey(shopId)) {
                count = event.shopMap.get(shopId);
                count++;
            }

            event.shopMap.put(shopId, count);
        }

        EventBus.getDefault().postSticky(event);
    }

    @Override
    public void onLoaderReset(Loader<List<Item>> loader) {
        mAdapter.clear();
    }

    @Override
    public void onItemDisplay(Item item) {
        // display details
        Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
        intent.putExtra(ItemDetailsActivity.InitBundle.ITEM_ID, item.id());
        startActivity(intent);
    }

    @Override
    public void onItemCritique(Item item, boolean isLike) {
    	Intent intent = new Intent(getActivity(), CritiqueActivity.class);
    	intent.putExtra(CritiqueActivity.InitBundle.IS_POSITIVE_CRITIQUE, isLike);
        intent.putExtra(CritiqueActivity.InitBundle.ITEM_ID, item.id());
    	startActivityForResult(intent, REQUEST_CODE);
    }
    
	@Override
	public void onItemFavourite(Item item) {
		Context context = this.getActivity();
		// finish task, store stats to database
        Uri statUri = Statistics.get().finishTask(context);
        if (statUri == null) {
            Toast.makeText(context, "Task was not started.", Toast.LENGTH_LONG).show();
            return;
        }
        
        FavouriteItemUtils.addToFavourites(context, item);

        // display results
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(ResultsActivity.InitBundle.STATS_ID,
                Integer.valueOf(Stats.getStatId(statUri)));
        intent.putExtra(ResultsActivity.InitBundle.ITEM_ID, item.id());
        startActivity(intent);
	}

    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            Log.d(TAG, "Received recommendation update, requerying");
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    public void onEvent(LocationUpdateEvent event) {
        if (!mIsInitialized) {
            Log.d(TAG, "Received location update, requerying");
            mIsInitialized = true;
            onInitializeItems();
        }
    }

    private void onInitializeItems() {
        Bundle args = new Bundle();
        args.putBoolean("isinit", true);
        getLoaderManager().restartLoader(LOADER_ID, args, this);
    }
}
