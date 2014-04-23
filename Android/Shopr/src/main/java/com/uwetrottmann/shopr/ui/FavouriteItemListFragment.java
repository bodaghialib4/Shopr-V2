package com.uwetrottmann.shopr.ui;

import java.util.List;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.adapters.FavouriteItemAdapter;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.loaders.FavouriteItemLoader;

/**
 * Shows a list of clothing items the user can critique by tapping an up or down
 * vote button.
 */
public class FavouriteItemListFragment extends ItemListFragment<Item> {

	private GridView mGridView;
	private TextView emtpyView;

	public static FavouriteItemListFragment newInstance() {
		return new FavouriteItemListFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.common_fragment_favourite_items_list,
				container, false);

		mGridView = (GridView) v.findViewById(R.id.gridViewItemList);
		emtpyView = (TextView) v.findViewById(R.id.textViewItemListEmpty);
		mGridView.setEmptyView(emtpyView);

		return v;
	}	

	
	@Override
	public Loader<List<Item>> onCreateLoader(int loaderId, Bundle args) {
		return new FavouriteItemLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
		super.onLoadFinished(loader, data);

		if (data.size() == 0) {
			emtpyView.setText(R.string.favourite_list_empty);
		}
	}

	@Override
	public ArrayAdapter<Item> createAdapter() {
		return new FavouriteItemAdapter(getActivity(), this);
	}

	@Override
	public void setAdapter(ArrayAdapter<Item> adapter) {
		mGridView.setAdapter(adapter);
	}

	@Override
	protected List<Item> explainOrLeaveSame(List<Item> items) {
		return items;
	}

	@Override
	protected void afterUpdateItems() {
	}

}
