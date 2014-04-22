
package com.uwetrottmann.shopr.adapters;

import java.text.NumberFormat;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.utils.ValueConverter;

public class FavouriteItemAdapter extends ArrayAdapter<Item> {

    private static final int LAYOUT = R.layout.common_favourite_items_layout;

    private LayoutInflater mInflater;

    private OnFavouriteItemDisplayListener mItemDisplayListener;

    public interface OnItemCritiqueListener {
        public void onItemCritique(Item item, boolean isLike);
    }

    public interface OnFavouriteItemDisplayListener {
        public void onItemDisplay(Item item);
    }

    public FavouriteItemAdapter(Context context,
            OnFavouriteItemDisplayListener itemDisplayListener) {
        super(context, LAYOUT);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemDisplayListener = itemDisplayListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(LAYOUT, null);

            holder = new ViewHolder();
            holder.pictureContainer = convertView.findViewById(R.id.containerItemPicture);
            holder.picture = (ImageView) convertView.findViewById(R.id.imageViewItemPicture);
            holder.name = (TextView) convertView.findViewById(R.id.textViewItemName);
            holder.label = (TextView) convertView.findViewById(R.id.textViewItemLabel);
            holder.price = (TextView) convertView.findViewById(R.id.textViewItemPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Item item = getItem(position);
        holder.name.setText(item.name());
        holder.label.setText(ValueConverter.getLocalizedStringForValue(getContext(), item
                .attributes().getAttributeById(Color.ID).currentValue()
                .descriptor()));
        holder.price.setText(NumberFormat.getCurrencyInstance(Locale.GERMANY).format(
                item.price().doubleValue()));
        holder.pictureContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemDisplayListener != null) {
                    mItemDisplayListener.onItemDisplay(item);
                }
            }
        });

        // load picture
        Picasso.with(getContext())
                .load(item.image())
                .placeholder(null)
                .error(R.drawable.ic_action_tshirt)
                .resizeDimen(R.dimen.default_image_width, R.dimen.default_image_height)
                .centerCrop()
                .into(holder.picture);

        return convertView;
    }

    static class ViewHolder {
        View pictureContainer;
        ImageView picture;
        TextView name;
        TextView label;
        TextView price;
    }

}
