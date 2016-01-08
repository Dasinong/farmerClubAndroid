package com.dasinong.farmerclub.ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_businessman_info);
		initView();
		initTopBar();
		initEvent();
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

	private void initTopBar() {
		topBar.setCenterText("商户认证");
		topBar.setLeftView(true, true);
		topBar.setRightText("完成");
		topBar.setRightClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
							goToNext();
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
