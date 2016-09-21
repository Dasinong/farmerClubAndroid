package com.dasinong.farmerclub.ui;

import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.MyCouponsEntity;
import com.dasinong.farmerclub.entity.MyCouponsEntity.Coupon;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.adapter.MyCouponFragmentPagerAdapter;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.PagerSlidingTabStrip;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.AppInfoUtils;

public class MyCouponActivity extends BaseActivity {
	
	private TopbarView topBar;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private String lat;
	private String lon;
	private boolean isBASF = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_coupon);
		
		lat = SharedPreferencesHelper.getString(this, Field.CURRENT_LAT, "");
		lon = SharedPreferencesHelper.getString(this, Field.CURRENT_LON, "");
		
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
		RequestService.getInstance().getCoupons(this, lat, lon, MyCouponsEntity.class , new RequestListener() {

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

		int institutionId = SharedPreferencesHelper.getInt(this,Field.INSTITUTIONID,0);
		int localInstitutionId = AppInfoUtils.getInstitutionId(this);

		if(institutionId == 3 || localInstitutionId == 3){
			isBASF = true;
		}

        pager.setAdapter(new MyCouponFragmentPagerAdapter(getSupportFragmentManager(), coupons, isBASF));
        
        tabs.setViewPager(pager);
	}
}
