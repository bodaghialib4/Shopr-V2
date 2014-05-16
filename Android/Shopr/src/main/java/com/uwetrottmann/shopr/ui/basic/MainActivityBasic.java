package com.uwetrottmann.shopr.ui.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.maps.model.LatLng;
import com.uwetrottmann.androidutils.Maps;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.eval.TestSetupActivity;
import com.uwetrottmann.shopr.importer.ImporterActivity;
import com.uwetrottmann.shopr.settings.AppSettings;
import com.uwetrottmann.shopr.ui.LocationHandler;
import com.uwetrottmann.shopr.ui.SettingsActivity;
import com.uwetrottmann.shopr.utils.Tuple;

import de.greenrobot.event.EventBus;

public class MainActivityBasic extends FragmentActivity implements
		ActionBar.TabListener {

	private LocationHandler locationHandler;
	/*
	 * Define a request code to send to Google Play services. This code is
	 * returned in Activity.onActivityResult.
	 */
	public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Use fake location unless otherwise stated
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putBoolean(AppSettings.KEY_USING_DIVERSITY, true).commit();

		locationHandler = LocationHandler.getInstance(this);
		locationHandler.initLocationClientOrExit();

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		// Use fake location unless otherwise stated
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putBoolean(AppSettings.KEY_USING_DIVERSITY, true).commit();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (AppSettings.isUsingFakeLocation(this)) {
			// use fake location (Marienplatz, Munich)
			locationHandler.setLastLocation(new LatLng(48.137314, 11.575253));
			// send out location update event immediately
			EventBus.getDefault().postSticky(
					locationHandler.newLocationUpdateEvent());
		} else {
			locationHandler.connectLocationClient();
		}

		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	public void onStop() {
		if (!AppSettings.isUsingFakeLocation(this)) {
			locationHandler.disconnectLocationClient();
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
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		private Map<Integer, Tuple<Fragment, String>> fragmentSectionMap;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			this.fragmentSectionMap = toMap(createFragmentSections());
		}

		private Map<Integer, Tuple<Fragment, String>> toMap(
				List<Tuple<Fragment, String>> fragmentSections) {
			Map<Integer, Tuple<Fragment, String>> fragmentSectionMap = Maps
					.newHashMap();

			int index = 0;
			for (Tuple<Fragment, String> fragmentSection : fragmentSections) {
				fragmentSectionMap.put(index, fragmentSection);
				index++;
			}
			return fragmentSectionMap;
		}

		private List<Tuple<Fragment, String>> createFragmentSections() {
			Locale l = Locale.getDefault();
			Tuple<Fragment, String> itemFragment = new Tuple<Fragment, String>(
					ItemListFragmentBasic.newInstance(), getString(
							R.string.title_list).toUpperCase(l));
			Tuple<Fragment, String> shopMapFragment = new Tuple<Fragment, String>(
					RecommendationsShopMapBasic.newInstance(), getString(
							R.string.title_map).toUpperCase(l));
			/*
			 * Tuple<Fragment, String> favouritesFragment = new Tuple<Fragment,
			 * String>(FavouriteItemListFragment.newInstance(),
			 * getString(R.string.title_favourites).toUpperCase(l));
			 */
			List<Tuple<Fragment, String>> fragmentSections = new ArrayList<Tuple<Fragment, String>>();
			// fragmentSections.add(favouritesFragment);
			fragmentSections.add(itemFragment);
			fragmentSections.add(shopMapFragment);

			return fragmentSections;
		}

		@Override
		public Fragment getItem(int position) {
			Tuple<Fragment, String> fragmentSection = fragmentSectionMap
					.get(position);
			Fragment fragment = fragmentSection._1;
			return fragment;
		}

		@Override
		public int getCount() {
			return fragmentSectionMap.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Tuple<Fragment, String> fragmentSection = fragmentSectionMap
					.get(position);
			String title = fragmentSection._2;
			return title;
		}
	}

}
