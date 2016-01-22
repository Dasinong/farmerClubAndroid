package com.dasinong.farmerclub.ui.adapter;

import java.util.List;

import com.dasinong.farmerclub.entity.MyCouponsEntity.Coupon;
import com.dasinong.farmerclub.ui.fragment.MyCouponFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyCouponFragmentPagerAdapter extends FragmentPagerAdapter {

	String[] titles = { "未使用", "已失效" };
	private List<Coupon> list;

	public MyCouponFragmentPagerAdapter(FragmentManager fm, List<Coupon> list) {
		super(fm);
		this.list = list;
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
		return new MyCouponFragment(position, list);
	}

}
