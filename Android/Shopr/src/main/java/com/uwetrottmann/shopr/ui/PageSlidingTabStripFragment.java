package com.uwetrottmann.shopr.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.utils.Tuple;

public class PageSlidingTabStripFragment extends Fragment {

	public static final String TAG = PageSlidingTabStripFragment.class
			.getSimpleName();

	public static PageSlidingTabStripFragment newInstance() {
		return new PageSlidingTabStripFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.pager, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.tabs);
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);

	}

	public class MyPagerAdapter extends FragmentPagerAdapter {
		private List<Tuple<Fragment, String>> fragmentSections;
		
		public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
			this.fragmentSections = getFragmentSections();
		}
		
		private List<Tuple<Fragment, String>> getFragmentSections() {
			List<Tuple<Fragment, String>> sections = new ArrayList<Tuple<Fragment, String>>();
			
			Tuple<Fragment, String> itemsFragment = new Tuple<Fragment, String>(
					ItemListFragment.newInstance(), getString(
							R.string.title_list));
			
			Tuple<Fragment, String> shopsFragment = new Tuple<Fragment, String>(
					ShopMapFragment.newInstance(), getString(
							R.string.title_map));
			
			sections.add(itemsFragment);
			sections.add(shopsFragment);
			return sections;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Tuple<Fragment, String> fragmentSection = fragmentSections
					.get(position);
			String title = fragmentSection._2;
			return title;
		}

		@Override
		public int getCount() {
			return fragmentSections.size();
		}

		@Override
		public Fragment getItem(int position) {
			Tuple<Fragment, String> fragmentSection = fragmentSections
					.get(position);
			Fragment fragment = fragmentSection._1;
			return fragment;
		}

	}
}
