package com.dasinong.farmerclub.ui;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.database.city.dao.impl.CityDaoImpl;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.VillageInfo;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.MyInfoSetActivity.MyComparator;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddPlaceActivity extends BaseActivity implements OnClickListener {

	private Button btnSure;
	private TopbarView topBar;
	private View mSelectAreaLayout;
	private Spinner mProvinceSp;
	private Spinner mCitySp;
	private Spinner mAreaSp;
	private Spinner mTownsSp;
	private Spinner mVillageSp;
	private CityDaoImpl dao;
	private List<String> province;
	private Map<String, String> villageMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_place);

		initView();
	}

	private void initView() {
		topBar = (TopbarView) findViewById(R.id.topbar);
		mProvinceSp = (Spinner) this.findViewById(R.id.spinner_province);
		mCitySp = (Spinner) this.findViewById(R.id.spinner_city);
		mAreaSp = (Spinner) this.findViewById(R.id.spinner_area);
		mTownsSp = (Spinner) this.findViewById(R.id.spinner_towns);
		mVillageSp = (Spinner) this.findViewById(R.id.spinner_village);
		btnSure = (Button) findViewById(R.id.btn_sure);
		
		btnSure.setOnClickListener(this);

		initTopBar();

		initProvince();
	}

	private void initTopBar() {
		topBar.setCenterText("选择地址");
		topBar.setLeftView(true, true);
	}

	private void initProvince() {
		new Thread() {
			public void run() {
				dao = new CityDaoImpl(AddPlaceActivity.this);
				province = dao.getProvince();
				province.add(0, "请选择省");
				runOnUiThread(new Runnable() {
					public void run() {
						setProvince();
					}
				});
			};
		}.start();
	}

	private void setProvince() {
		mProvinceSp.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, province));
		mProvinceSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String province = (String) mProvinceSp.getSelectedItem();
				setCity(province);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	protected void setCity(final String province2) {
		List<String> city = dao.getCity(province2);
		city.add(0, "请选择市");
		mCitySp.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, city));
		mCitySp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String city = (String) mCitySp.getSelectedItem();
				setArea(province2, city);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	protected void setArea(final String province2, final String city) {
		List<String> county = dao.getCounty(province2, city);
		county.add(0, "请选择区");
		mAreaSp.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, county));
		mAreaSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String area = (String) mAreaSp.getSelectedItem();
				setTowns(province2, city, area);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		// mTownsSp.setAdapter(null);
	}

	protected void setTowns(final String province2, final String city, final String area) {
		List<String> county = dao.getDistrict(province2, city, area);
		county.add(0, "请选择镇");
		mTownsSp.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, county));
		mTownsSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String town = (String) mTownsSp.getSelectedItem();
				initVillage(province2, city, area, town);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	protected void initVillage(String provices2, String city, String area, String town) {
		startLoadingDialog();
		RequestService.getInstance().getLocation(DsnApplication.getContext(), provices2, city, area, town, VillageInfo.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					villageMap = ((VillageInfo) resultData).data;
					MyComparator mComparator = new MyComparator();
					List<String> villageList = new ArrayList<String>(villageMap.keySet());
					Collections.sort(villageList, mComparator);
					setVillages(villageList);

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
	}

	protected void setVillages(List<String> village) {
		village.add(0, "请选择村");
		if (village != null && village.size() > 0) {
			mVillageSp.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, village));
		} else {
			mVillageSp.setVisibility(View.GONE);
		}
	}

	class MyComparator implements Comparator<String> {
		Collator cmp = Collator.getInstance(java.util.Locale.CHINA);

		@Override
		public int compare(String str1, String str2) {
			if (cmp.compare(str1, str2) > 0) {
				return 1;
			} else if (cmp.compare(str1, str2) < 0) {
				return -1;
			}
			return 0;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sure:
			if (mProvinceSp.getSelectedItemPosition() == 0) {
				showToast("请选择省");
				return;
			}
			if (mCitySp.getSelectedItemPosition() == 0) {
				showToast("请选择市");
				return;
			}
			if (mAreaSp.getSelectedItemPosition() == 0) {
				showToast("请选择区");
				return;
			}
			if (mTownsSp.getSelectedItemPosition() == 0) {
				showToast("请选择镇");
				return;
			}
			if (mVillageSp.getVisibility() == View.VISIBLE) {
				if (mVillageSp.getSelectedItemPosition() == 0) {
					showToast("请选择村");
					return;
				}
			}
			
			String village = (String) mVillageSp.getSelectedItem();
			String locationId = villageMap.get(village);
			
			MobclickAgent.onEvent(this, "FinishAddPlace");
			
			startLoadingDialog();
			RequestService.getInstance().weatherSubscriptions(this, locationId, WeatherSubscriptionsEntity.class, new RequestListener() {
				
				@Override
				public void onSuccess(int requestCode, BaseEntity resultData) {
					if(resultData.isOk()){
						WeatherSubscriptionsEntity entity = (WeatherSubscriptionsEntity) resultData;
						if(entity != null && entity.data != null){
//							SharedPreferencesHelper.setLong(AddPlaceActivity.this, Field.CURRRENT_MONITOR_LOCATION_ID, entity.data.monitorLocationId);
							showToast("关注成功");
							finish();
						}
					} else {
						showToast(R.string.please_check_netword);
					}
					dismissLoadingDialog();
				}
				
				@Override
				public void onFailed(int requestCode, Exception error, String msg) {
					dismissLoadingDialog();
				}
			});
			
			break;
		}
	}
}
