package com.dasinong.farmerclub.ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.hardware.Camera.Area;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.RetailerInfoEntity;
import com.dasinong.farmerclub.entity.VillageInfo;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.view.SelectAreaView;
import com.dasinong.farmerclub.ui.view.SelectAreaView.OnGetVillagesListener;
import com.dasinong.farmerclub.ui.view.TopbarView;

public class SubmitBusinessmanInfo extends BaseActivity implements OnCheckedChangeListener {
	private EditText et_store_name;
	private SelectAreaView sav_area;
	private CheckBox cb_seed;
	private CheckBox cb_fertilizer;
	private CheckBox cb_pesticides;
	private CheckBox cb_wholesale;
	private EditText et_phone;
	private EditText et_contacts;
	private EditText et_description;
	private TopbarView topBar;

	private final int[] ids = { R.id.cb_seed, R.id.cb_fertilizer, R.id.cb_pesticides, R.id.cb_wholesale };
	private final int[] types = { 1, 2, 4, 8 };

	private Set<Integer> typeSet = new HashSet<Integer>();
	private EditText et_address;
	private boolean isUpdata;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_businessman_info);
		isUpdata = getIntent().getBooleanExtra("isUpdata", false);
		initView();
		if (isUpdata) {
			queryData();
		}
		initTopBar();
		initEvent();
	}

	private void queryData() {
		startLoadingDialog();
		RequestService.getInstance().stores(this, RetailerInfoEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					RetailerInfoEntity entity = (RetailerInfoEntity) resultData;
					setData(entity);
				}

				dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	private void initView() {
		topBar = (TopbarView) findViewById(R.id.topbar);
		et_store_name = (EditText) findViewById(R.id.et_store_name);
		sav_area = (SelectAreaView) findViewById(R.id.sav_area);
		et_address = (EditText) findViewById(R.id.et_address);
		cb_seed = (CheckBox) findViewById(R.id.cb_seed);
		cb_fertilizer = (CheckBox) findViewById(R.id.cb_fertilizer);
		cb_pesticides = (CheckBox) findViewById(R.id.cb_pesticides);
		cb_wholesale = (CheckBox) findViewById(R.id.cb_wholesale);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_contacts = (EditText) findViewById(R.id.et_contacts);
		et_description = (EditText) findViewById(R.id.et_description);
	}

	protected void setData(RetailerInfoEntity entity) {
		et_store_name.setText(entity.data.store.name);
		et_phone.setText(entity.data.store.phone);
		et_contacts.setText(entity.data.store.contactName);
		et_description.setText(entity.data.store.desc);
		
		if((entity.data.store.type & types[0]) > 0){
			cb_seed.setChecked(true);
		}
		
		if((entity.data.store.type & types[1]) > 0){
			cb_fertilizer.setChecked(true);
		}
		
		if((entity.data.store.type & types[2]) > 0){
			cb_pesticides.setChecked(true);
		}
		
		if((entity.data.store.type & types[3]) > 0){
			cb_wholesale.setChecked(true);
		}
		
		cb_seed.setClickable(false);
		cb_fertilizer.setClickable(false);
		cb_pesticides.setClickable(false);
		cb_wholesale.setClickable(false);
		

		et_store_name.setEnabled(false);
		et_phone.setEnabled(false);
		et_contacts.setEnabled(false);
		et_description.setEnabled(false);
		
		sav_area.setVisibility(View.GONE);
		et_address.setText(entity.data.store.location + entity.data.store.streetAndNumber);
		et_address.setEnabled(false);
		
	}

	private void initTopBar() {
		topBar.setCenterText("商户认证");
		topBar.setLeftView(true, true);
		if (isUpdata) {
			topBar.setRightText("编辑");
		} else {
			topBar.setRightText("完成");

		}

		topBar.setRightClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if ("完成".equals(topBar.getRightText())) {
					String name = et_store_name.getText().toString().trim();
					String villageId = sav_area.getVillageId();
					String address = et_address.getText().toString().trim();
					String phone = et_phone.getText().toString().trim();
					String contacts = et_contacts.getText().toString().trim();
					String descripthon = et_description.getText().toString().trim();
					if (TextUtils.isEmpty(name) || TextUtils.isEmpty(villageId) || TextUtils.isEmpty(address) || TextUtils.isEmpty(phone)
							|| TextUtils.isEmpty(contacts) || TextUtils.isEmpty(descripthon) || typeSet.isEmpty()) {
						showToast("请完善以上所有信息");
						return;
					}
					int intType = 0;
					for (int item : typeSet) {
						intType += item;
					}
					String type = String.valueOf(intType);
					sendQuery(name, descripthon, villageId, address, contacts, phone, type);
				} else if("编辑".equals(topBar.getRightText())){
					et_store_name.setEnabled(true);
					et_phone.setEnabled(true);
					et_contacts.setEnabled(true);
					et_description.setEnabled(true);
					et_address.setEnabled(true);
					
					cb_seed.setClickable(true);
					cb_fertilizer.setClickable(true);
					cb_pesticides.setClickable(true);
					cb_wholesale.setClickable(true);
					
					sav_area.setVisibility(View.VISIBLE);
					et_address.setText("");
					
					topBar.setRightText("完成");
				}
			}

		});
	}

	protected void sendQuery(String name, String desc, String locationId, String streetAndNumber, String contactName, String phone, String type) {
		startLoadingDialog();
		RequestService.getInstance().stores(this, name, desc, locationId, streetAndNumber, "", "", contactName, phone, type, "0", BaseEntity.class,
				new RequestListener() {
					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if (resultData.isOk()) {
							showToast("恭喜您，认证成功");
							if (isUpdata) {
								finish();
							} else {
								goToNext();
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
	}

	protected void goToNext() {
		Intent intent = new Intent(this, SelectCropActivity.class);
		startActivity(intent);
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
		});

		cb_seed.setOnCheckedChangeListener(this);
		cb_fertilizer.setOnCheckedChangeListener(this);
		cb_pesticides.setOnCheckedChangeListener(this);
		cb_wholesale.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int id = buttonView.getId();
		int index = Arrays.binarySearch(ids, id);
		if (isChecked) {
			typeSet.add(types[index]);
		} else {
			typeSet.remove(types[index]);
		}
	}
}
