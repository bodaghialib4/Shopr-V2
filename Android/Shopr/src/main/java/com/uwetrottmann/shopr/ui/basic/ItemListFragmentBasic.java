
package com.uwetrottmann.shopr.ui.basic;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.adapters.BasicItemAdapter;
import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.Query;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.ui.ItemListFragment;

/**
 * Shows a list of clothing items the user can critique by tapping an up or down
 * vote button.
 */
public class ItemListFragmentBasic extends ItemListFragment<Item> {

    private TextView mTextViewReason;
    private GridView mGridView;
    
    private static ItemListFragmentBasic instance;

    public static ItemListFragmentBasic newInstance() {
    	if(instance == null)
    		instance = new ItemListFragmentBasic();
    	
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.basic_fragment_item_list, container, false);

        mTextViewReason = (TextView) v.findViewById(R.id.textViewItemListReason);
        mGridView = (GridView) v.findViewById(R.id.gridViewItemList);
        View emtpyView = v.findViewById(R.id.textViewItemListEmpty);
        mGridView.setEmptyView(emtpyView);
        return v;
    }


    private void updateReason() {
        Query currentQuery = AdaptiveSelection.get().getCurrentQuery();
        // Display current reason as explanatory text
        String reasonString = currentQuery.attributes().getReasonString();
        if (TextUtils.isEmpty(reasonString)) {
            mTextViewReason.setText(R.string.reason_empty);
        } else {
            mTextViewReason.setText(reasonString);
        }
    }

	@Override
	public ArrayAdapter<Item> createAdapter() {
		return new BasicItemAdapter(getActivity(), this, this, this);
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
		updateReason();		
	}
}
