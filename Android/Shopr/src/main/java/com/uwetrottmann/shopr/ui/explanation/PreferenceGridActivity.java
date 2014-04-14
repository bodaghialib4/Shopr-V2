package com.uwetrottmann.shopr.ui.explanation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

public abstract class PreferenceGridActivity extends
		PreferenceActivity {

	protected abstract Attribute attribute();
	
	@Override
	protected int layout() {
		return R.layout.attribute_value_preference;
	}	
	
	protected final TextView explanationView() {
		return (TextView) findViewById(R.id.textViewAttributeValuePreference);
	}

	protected final Button updatePreferenceButton() {
		return (Button) findViewById(R.id.buttonUpdatePreferences);
	}

	protected final ArrayAdapter<AttributeValue> attributeValueAdapter() {
		return new AttributeValueAdapter(this);
	}

	protected final AbsListView attributeValueList() {
		GridView gridView = (GridView) findViewById(R.id.gridViewAttributeValueList);
		gridView.setNumColumns(numColums());
		return gridView;
	}

	protected int numColums() {
		return 4;
	}

	public class AttributeValueAdapter extends ArrayAdapter<AttributeValue> {

		private static final int LAYOUT = R.layout.attribute_value_layout;
		private LayoutInflater mInflater;

		public AttributeValueAdapter(Context context) {
			super(context, LAYOUT);
			mInflater = LayoutInflater.from(context);
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(LAYOUT, null);

				holder = new ViewHolder();
				holder.pictureContainer = convertView
						.findViewById(R.id.containerAttributeValuePicture);
				holder.picture = (ImageView) convertView
						.findViewById(R.id.imageViewAttributeValuePicture);
				holder.name = (TextView) convertView
						.findViewById(R.id.textViewAttributeValueName);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final AttributeValue value = getItem(position);
			holder.name.setText(value.descriptor());
			holder.picture.setBackgroundDrawable(getDrawableOrDefault(value
					.simpleName()));

			return convertView;
		}

		private Drawable getDrawableOrDefault(String name) {
			Resources resources = getContext().getResources();
			final int resourceId = getContext().getResources().getIdentifier(
					name, "drawable", getContext().getPackageName());

			try {
				Drawable drawable = resources.getDrawable(resourceId);
				return drawable;
			} catch (Resources.NotFoundException e) {
				return resources.getDrawable(R.drawable.ic_action_tshirt);
			}
		}

		class ViewHolder {
			View pictureContainer;
			ImageView picture;
			TextView name;
		}
	}

}
