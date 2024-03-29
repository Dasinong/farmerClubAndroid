package com.dasinong.farmerclub.ui.adapter;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dasinong.farmerclub.ui.fragment.HarmFragment;

/**
 * 
 * @author Ming
 * 此类为病虫草害列表页ViewPager的适配器
 */

public class HarmFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private final String [] TITLES = {"病害","虫害","草害"};
	
	public HarmFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		
	}
	
	@Override
	public int getCount() {
		return TITLES.length;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return TITLES[position];
	}
	
	@Override
	public Fragment getItem(int position) {
		return HarmFragment.newInstance(position);
	}
	
}
