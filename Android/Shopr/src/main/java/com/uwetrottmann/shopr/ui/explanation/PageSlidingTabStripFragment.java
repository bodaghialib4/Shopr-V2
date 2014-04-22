package com.uwetrottmann.shopr.ui.explanation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.model.SectionItem;

public class PageSlidingTabStripFragment extends Fragment {

	public static final String TAG = PageSlidingTabStripFragment.class
			.getSimpleName();
	private PagerAdapter adapter;

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
		return inflater.inflate(R.layout.basic_pager, container, false);
	}

	/*
	 * Necessary to propagate activity result to children tabs as sliding tab fragment only hosts the tab children fragments.
	 * */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		for(SectionItem section : adapter.getFragmentSections()) {
			section.getFragment().onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.tabs);

		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		adapter = new PagerAdapter(getChildFragmentManager());
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
	}

	/*
	 * As a workaround to Fragment Manager crash,
	 * 'java.lang.IllegalStateException: No activity' because of a bug in the
	 * support lib. Bug:
	 * https://code.google.com/p/android/issues/detail?id=42601
	 * http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
	 */
	@Override
	public void onDetach() {
		super.onDetach();

		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private class PagerAdapter extends FragmentPagerAdapter {
		private List<SectionItem> fragmentSections;

		public PagerAdapter(FragmentManager fm) {
			super(fm);
			this.fragmentSections = createFragmentSections();
		}
		
		public List<SectionItem> getFragmentSections() {
			return fragmentSections;
		}

		private List<SectionItem> createFragmentSections() {
			List<SectionItem> sections = new ArrayList<SectionItem>();

			sections.add(new SectionItem()
					.title(getString(R.string.title_list)).fragment(
							ItemListFragmentExplanation.newInstance()));

			sections.add(new SectionItem().title(getString(R.string.title_map))
					.fragment(ShopMapFragmentExplanation.newInstance()));

			return sections;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return fragmentSections.get(position).getTitle();
		}

		@Override
		public int getCount() {
			return fragmentSections.size();
		}

		@Override
		public Fragment getItem(int position) {
			return fragmentSections.get(position).getFragment();
		}
	}
}
