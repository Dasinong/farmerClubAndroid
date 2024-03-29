package com.dasinong.farmerclub.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.database.city.dao.impl.CityDaoImpl;
import com.dasinong.farmerclub.entity.AccountEntity;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.LocationInfo;
import com.dasinong.farmerclub.entity.LocationResult;
import com.dasinong.farmerclub.net.NetRequest;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.manager.AccountManager;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.DeviceHelper;
import com.dasinong.farmerclub.utils.LocationUtils;
import com.dasinong.farmerclub.utils.Logger;
import com.dasinong.farmerclub.utils.LocationUtils.LocationListener;
import com.umeng.analytics.MobclickAgent;

public class IsInFieldActivity extends MyBaseActivity implements OnClickListener {

	// 纬度
	private double latitude;
	// 经度
	private double longitude;
	// 省
	private String mprovince;
	// 市
	private String mcity;
	// 区
	private String mdistrict;
	// 街道
	private String mstreet;
	private LocationInfo locationInfo;
	private Button btn_in_field;
	private Button btn_no_in_field;
	private TopbarView topbar;
	private RunnableTask task = new RunnableTask();
	private static final int MAX_DELAY_COUNT = 1;
	private int count = 0;
	public static Activity instance;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			count++;
			initLocation();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_is_in_field);
		
		instance = this;

		btn_no_in_field = (Button) findViewById(R.id.btn_no_in_field);
		btn_in_field = (Button) findViewById(R.id.btn_in_field);
		topbar = (TopbarView) findViewById(R.id.topbar);

		initLocation();

		initTopBar();

		btn_in_field.setOnClickListener(this);
		btn_no_in_field.setOnClickListener(this);
		
	}

	@Override
	public void onResume() {
		super.onResume();
		initLocation();
	}

	@Override
	public void onClick(View v) {
		if (!DeviceHelper.checkNetWork(this)) {
			showToast("请检测您的网络连接");
			return;
		}
		int id = v.getId();

		if ((latitude == 0 || longitude == 0) && count < MAX_DELAY_COUNT) {
			showToast("定位失败，请重新点击按钮");
			handler.postDelayed(new RunnableTask(), 500);
			return;
		} else if ((latitude == 0 || longitude == 0) && count >= MAX_DELAY_COUNT) {
			handler.removeCallbacks(task);
			Intent intent = new Intent(this, SelectAreaActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			overridePendingTransition(0, 0);
			return;
		}

		String latitudeText = String.valueOf(latitude);
		String longitudeText = String.valueOf(longitude);
		SharedPreferencesHelper.setString(this, Field.LATITUDE, latitudeText);
		SharedPreferencesHelper.setString(this, Field.LONGITUDE, longitudeText);

		switch (id) {
		case R.id.btn_no_in_field:
			MobclickAgent.onEvent(this, "InField");
			if(!TextUtils.isEmpty(mprovince)){
				hasCurrentLocation();
			}
			goToTwo();
			break;
		case R.id.btn_in_field:
			MobclickAgent.onEvent(this, "NoInField");
			startLoadingDialog();
			boolean flag = false;
			if (!TextUtils.isEmpty(mprovince)) {
				flag = hasCurrentLocation();
			}
			
			if (flag) {
				RequestService.getInstance().searchLocation(this, latitudeText, longitudeText, mprovince, mcity, mdistrict, LocationInfo.class,
						new NetRequest.RequestListener() {

							@Override
							public void onSuccess(int requestCode, BaseEntity resultData) {

								if (resultData.isOk()) {
									locationInfo = (LocationInfo) resultData;
									goToThree();
								} else {
									showToast(resultData.getMessage());
								}
								dismissLoadingDialog();
							}

							@Override
							public void onFailed(int requestCode, Exception error, String msg) {
								dismissLoadingDialog();
							}
						});
			} else {
				dismissLoadingDialog();
				goToTwo();
			}

			break;
		}
	}

	// 判断百度地图地理编码出来的地名在数据库中是否存在
	private boolean hasCurrentLocation() {
		CityDaoImpl dao = new CityDaoImpl(this);
		if (mprovince.contains("西藏")) {
			mprovince = "西藏";
		} else if (mprovince.contains("新疆")) {
			mprovince = "新疆";
		} else if (mprovince.contains("内蒙古")) {
			mprovince = "内蒙古";
		} else if (mprovince.contains("宁夏")) {
			mprovince = "宁夏";
		} else if (mprovince.contains("广西")) {
			mprovince = "广西";
		} else {
			if (mprovince.contains("省") || mprovince.contains("市")) {
				mprovince = mprovince.substring(0, mprovince.length() - 1);
			}
		}
		if (mcity.contains("市") || mcity.contains("县") || mcity.contains("盟")) {
			mcity = mcity.substring(0, mcity.length() - 1);
		} else if (mcity.contains("地区")) {
			mcity = mcity.replace("地区", "");
		} else if (mcity.contains("自治州")) {
			mcity = mcity.replace("自治州", "");
		}

		if (dao.hasCity(mcity) && dao.hasCounty(mdistrict)) {
			return true;
		}
		return false;
	}

	private void initLocation() {

		Logger.d("MING", "进入定位方法");

		LocationUtils.getInstance().registerLocationListener(new LocationListener() {

			@Override
			public void locationNotify(LocationResult result) {
				Logger.d("MING", "定位开始执行");
				latitude = result.getLatitude();
				longitude = result.getLongitude();

				mprovince = result.getProvince();
				mcity = result.getCity();
				mdistrict = result.getDistrict();
				mstreet = result.getStreet();

				Logger.d("addFieldLocation", "latitude  =====  " + latitude);
				Logger.d("addFieldLocation", "longitude  =====  " + longitude);

				Logger.d("addFieldLocation", "province  =====  " + mprovince);
				Logger.d("addFieldLocation", "city  =====  " + mcity);
				Logger.d("addFieldLocation", "district  =====  " + mdistrict);
				Logger.d("addFieldLocation", "street  =====  " + mstreet);

				Logger.d("MING", "定位结束");
			}
		});
	}

	private void initTopBar() {
		topbar.setCenterText("田是否在附近");
		topbar.setLeftView(true, true);
		topbar.setLeftClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IsInFieldActivity.this, MainTabActivity.class);
				intent.putExtra(MainTabActivity.TARGET_TAB, 1);
				startActivity(intent);
			}
		});
	}

	class RunnableTask implements Runnable {

		@Override
		public void run() {

			handler.sendEmptyMessage(0);

		}

	}

	private void goToTwo() {

		Intent intent = new Intent(DsnApplication.getContext(), SelectAreaActivity.class);
		intent.putExtra("mprovince", mprovince);
		intent.putExtra("mcity", mcity);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}

	private void goToThree() {
		SharedPreferencesHelper.setString(this, Field.PROVINCE, locationInfo.data.get("province"));
		SharedPreferencesHelper.setString(this, Field.CITY, locationInfo.data.get("city"));
		SharedPreferencesHelper.setString(this, Field.COUNTY, locationInfo.data.get("country"));
		SharedPreferencesHelper.setString(this, Field.VILLAGE_ID, locationInfo.data.get("locationId"));
		Intent intent = new Intent(DsnApplication.getContext(), InPutFieldSizeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}

	@Override
	protected void onStop() {
		LocationUtils.getInstance().unRegisterLocationListener();
		super.onStop();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			Intent intent = new Intent(this, MainTabActivity.class);
			intent.putExtra(MainTabActivity.TARGET_TAB, 1);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
