package com.dasinong.farmerclub.ui;

import java.util.ArrayList;
import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.MyCouponsEntity;
import com.dasinong.farmerclub.entity.MyCouponsEntity.Coupon;
import com.dasinong.farmerclub.entity.MyCouponsEntity.UseStatus;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.adapter.MyCouponFragmentPagerAdapter;
import com.dasinong.farmerclub.ui.view.PagerSlidingTabStrip;
import com.dasinong.farmerclub.ui.view.TopbarView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
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
		setContentView(R.layout.activity_my_coupon);
		
		initView();
		
		initTopBar();
		
		queryData();
		
	}

	private void initView() {
		topBar = (TopbarView) findViewById(R.id.topbar);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
	}
	
	private void initTopBar() {
		topBar.setCenterText("我的活动");
		topBar.setLeftView(true, true);
	}
	
	private void queryData() {
		startLoadingDialog();
		RequestService.getInstance().getCoupons(this, MyCouponsEntity.class , new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if(resultData.isOk()){
					MyCouponsEntity entity = (MyCouponsEntity) resultData;
					if(entity.data != null && entity.data.coupons != null){
						initTabs(entity.data.coupons);
					}
				}
				dismissLoadingDialog();
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}
	
	private void initTabs(List<Coupon> coupons) {
        tabs.setIndicatorColorResource(R.color.color_2BAD2A);
        tabs.setBackgroundResource(R.color.color_F0F0F0);
        tabs.setDividerColorResource(R.color.color_F0F0F0);
        tabs.setIndicatorHeight(5);
        tabs.setShouldExpand(true);
        tabs.setTextSize(32);
        tabs.setSelectedTextColorResource(R.color.color_2BAD2A);
        tabs.setSelectedTextSize(32);
        
        pager.setAdapter(new MyCouponFragmentPagerAdapter(getSupportFragmentManager(),coupons));
        
        tabs.setViewPager(pager);
	}
}
