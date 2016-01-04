package com.dasinong.farmerclub.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.InstitutionCropEntity;
import com.dasinong.farmerclub.entity.InstitutionCropEntity.Crop;
import com.dasinong.farmerclub.entity.SubscriptionCropEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.adapter.SelectCropAdapter;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.umeng.analytics.MobclickAgent;

public class SelectCropActivity extends BaseActivity {

	private ListView lv_crops;
	private Button btn_sure;
	private TopbarView topbar;
	private InstitutionCropEntity entity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_crop);

		initView();
		queryCrop();
	}

	private void queryCrop() {
		
		startLoadingDialog();
		RequestService.getInstance().getSubscriableCrops(this,InstitutionCropEntity.class, new RequestListener() {
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					entity = (InstitutionCropEntity) resultData;
					if (entity != null && entity.data != null && entity.data.crops != null && !entity.data.crops.isEmpty()) {
						List<String> list = new ArrayList<String>();
						for (Crop crop : entity.data.crops) {
							list.add(crop.cropName);
						}
						setListViewHeightBasedOnChildren(lv_crops);
						lv_crops.setAdapter(new SelectCropAdapter(SelectCropActivity.this, list, true));
					} else {
						showToast(R.string.please_check_netword);
					}
				} else {
					showToast(R.string.please_check_netword);
				}
				dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				showToast(R.string.please_check_netword);
				dismissLoadingDialog();
			}
		});
	}

	private void initView() {
		lv_crops = (ListView) findViewById(R.id.lv_crops);
		btn_sure = (Button) findViewById(R.id.btn_sure);
		topbar = (TopbarView) findViewById(R.id.topbar);

		initTopbar();

		btn_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<String> cropIdList = new ArrayList<String>();
				for (int i = 0; i < lv_crops.getChildCount(); i++) {
					
					LinearLayout root = (LinearLayout) lv_crops.getChildAt(i);
					CheckBox checkBox = (CheckBox) root.getChildAt(0);
					if (checkBox.isChecked()) {
						cropIdList.add(entity.data.crops.get(i).cropId + "");
					}
				}

				setSubscribeCrop(cropIdList);
			}
		});

	}

	private void initTopbar() {
		topbar.setCenterText("请选择作物");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setSubscribeCrop(List<String> list) {
		if (list == null || list.isEmpty()) {
			showToast("请至少选择一个关注作物");
			return;
		}
		MobclickAgent.onEvent(this, "FinishSelectCrop");
		startLoadingDialog();
		RequestService.getInstance().cropSubscriptions(this, list, SubscriptionCropEntity.class, new RequestListener() {
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					SubscriptionCropEntity entity = (SubscriptionCropEntity) resultData;
					showToast("关注成功");

					Intent intent = new Intent(SelectCropActivity.this, MainTabActivity.class);
					startActivity(intent);
					finish();
				} else {
					showToast(R.string.please_check_netword);
				}
				dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				showToast(R.string.please_check_netword);
				dismissLoadingDialog();
			}
		});
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
