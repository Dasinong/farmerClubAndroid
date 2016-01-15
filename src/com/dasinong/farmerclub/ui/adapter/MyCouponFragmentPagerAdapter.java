package com.dasinong.farmerclub.ui.adapter;

import com.dasinong.farmerclub.ui.fragment.MyCouponFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyCouponFragmentPagerAdapter extends FragmentPagerAdapter {

	String[] titles = { "未使用", "已失效" };

	public MyCouponFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return titles.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public Fragment getItem(int position) {
		return MyCouponFragment.newInstance(position);
	}

}
