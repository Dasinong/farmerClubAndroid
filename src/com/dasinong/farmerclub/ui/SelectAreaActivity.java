package com.dasinong.farmerclub.ui;

import java.util.Map;

import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.VillageInfo;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.SelectAreaView;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.ui.view.SelectAreaView.OnGetVillagesListener;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelectAreaActivity extends BaseActivity {

	private TopbarView topBar;
	private SelectAreaView sav_area;
	private Button btn_commit;
	public static Activity instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_area);
		
		instance = this;

		initView();
		initTopBar();
		initEvent();
	}

	private void initTopBar() {
		topBar.setCenterText("选择地址");
		topBar.setLeftView(true, true);
	}

	private void initView() {
		topBar = (TopbarView) findViewById(R.id.topbar);
		sav_area = (SelectAreaView) findViewById(R.id.sav_area);
		btn_commit = (Button) findViewById(R.id.btn_commit);
	}

	private void initEvent() {
		sav_area.setOnGetVillageListener(new OnGetVillagesListener() {

			@Override
			public void onGetVillages(String provices2, String city, String area, String town) {
				startLoadingDialog();
				RequestService.getInstance().getLocation(DsnApplication.getContext(), provices2, city, area, town, VillageInfo.class, new RequestListener() {

					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if (resultData.isOk()) {
							Map<String, String> villageMap = ((VillageInfo) resultData).data;
							sav_area.setVillages(villageMap);
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
			}
		});
		
		btn_commit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String villageId = sav_area.getVillageId();
				SharedPreferencesHelper.setString(SelectAreaActivity.this, Field.VILLAGE_ID, villageId);
				if(!TextUtils.isEmpty(villageId)){
					MobclickAgent.onEvent(SelectAreaActivity.this, "SelectAddress");
					Intent intent = new Intent(SelectAreaActivity.this, InPutFieldSizeActivity.class);
					startActivity(intent);
				} else {
					showToast("请完善地理信息");
				}
			}
		});
	}
}
