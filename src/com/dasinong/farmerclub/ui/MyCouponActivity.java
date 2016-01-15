package com.dasinong.farmerclub.ui;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.ui.adapter.MyCouponFragmentPagerAdapter;
import com.dasinong.farmerclub.ui.view.PagerSlidingTabStrip;
import com.dasinong.farmerclub.ui.view.TopbarView;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MyCouponActivity extends BaseActivity {
	
	private TopbarView topBar;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend);
		
		initView();
		
		initTopBar();
		
		initPager();
		
		initTabs();
		
	}

	private void initView() {
		topBar = (TopbarView) findViewById(R.id.topbar);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
	}
	
	private void initTopBar() {
		topBar.setCenterText("我的福利");
		topBar.setLeftView(true, true);
	}
	
	private void initPager() {
		pager.setAdapter(new MyCouponFragmentPagerAdapter(getSupportFragmentManager()));
	}
	
	private void initTabs() {
        tabs.setIndicatorColorResource(R.color.color_2BAD2A);
        tabs.setBackgroundResource(R.color.color_F0F0F0);
        tabs.setDividerColorResource(R.color.color_F0F0F0);
        tabs.setIndicatorHeight(5);
        tabs.setShouldExpand(true);
        tabs.setTextSize(32);
        tabs.setSelectedTextColorResource(R.color.color_2BAD2A);
        tabs.setSelectedTextSize(32);
	}
}
