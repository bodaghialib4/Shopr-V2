package com.uwetrottmann.shopr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.model.NavDrawerItem;
import com.uwetrottmann.shopr.model.NavMenuItem;
import com.uwetrottmann.shopr.model.NavMenuSection;

public class NavDrawerAdapter extends ArrayAdapter<NavDrawerItem> {

    private LayoutInflater inflater;
    
    public NavDrawerAdapter(Context context, int textViewResourceId, NavDrawerItem[] objects ) {
        super(context, textViewResourceId, objects);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null ;
        NavDrawerItem menuItem = this.getItem(position);
        if ( menuItem.getType() == NavMenuItem.ITEM_TYPE ) {
            view = getItemView(convertView, parent, menuItem );
        }
        else {
            view = getSectionView(convertView, parent, menuItem);
        }
        return view ;
    }
    
    public View getItemView( View convertView, ViewGroup parentView, NavDrawerItem navDrawerItem ) {
        
        NavMenuItem menuItem = (NavMenuItem) navDrawerItem ;
        NavMenuItemHolder navMenuItemHolder = null;
        
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.drawer_list_item, parentView, false);
            TextView labelView = (TextView) convertView
                    .findViewById( R.id.drawerItemTitle );
            ImageView iconView = (ImageView) convertView
                    .findViewById( R.id.drawerItemIcon );

            navMenuItemHolder = new NavMenuItemHolder();
            navMenuItemHolder.labelView = labelView ;
            navMenuItemHolder.iconView = iconView ;

            convertView.setTag(navMenuItemHolder);
        }

        if ( navMenuItemHolder == null ) {
            navMenuItemHolder = (NavMenuItemHolder) convertView.getTag();
        }
                    
        navMenuItemHolder.labelView.setText(menuItem.getLabel());
        navMenuItemHolder.iconView.setImageResource(menuItem.getIcon());
        
        return convertView ;
    }

    public View getSectionView(View convertView, ViewGroup parentView,
            NavDrawerItem navDrawerItem) {
        
        NavMenuSection menuSection = (NavMenuSection) navDrawerItem ;
        NavMenuSectionHolder navMenuItemHolder = null;
        
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.drawer_section, parentView, false);
            TextView labelView = (TextView) convertView
                    .findViewById( R.id.navmenusection_label );

            navMenuItemHolder = new NavMenuSectionHolder();
            navMenuItemHolder.labelView = labelView ;
            convertView.setTag(navMenuItemHolder);
        }

        if ( navMenuItemHolder == null ) {
            navMenuItemHolder = (NavMenuSectionHolder) convertView.getTag();
        }
                    
        navMenuItemHolder.labelView.setText(menuSection.getLabel());
        
        return convertView ;
    }
    
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    
    @Override
    public int getItemViewType(int position) {
        return this.getItem(position).getType();
    }
    
    @Override
    public boolean isEnabled(int position) {
        return getItem(position).isEnabled();
    }
    
    
    private static class NavMenuItemHolder {
        private TextView labelView;
        private ImageView iconView;
    }
    
    private class NavMenuSectionHolder {
        private TextView labelView;
    }
}

/*public class NavDrawerListAdapter extends BaseAdapter {
    
    private Context context;
    private List<NavItem> navDrawerItems;
     
    public NavDrawerListAdapter(Context context, List<NavItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }
 
    @Override
    public int getCount() {
        return navDrawerItems.size();
    }
 
    @Override
    public Object getItem(int position) {       
        return navDrawerItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
          
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.drawerItemIcon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.drawerItemTitle);
          
        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());        
        txtTitle.setText(navDrawerItems.get(position).getTitle());
              
        return convertView;
    }
 
}*/
