package com.dasinong.farmerclub.ui;

import java.util.Map;

import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.VillageInfo;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.view.SelectAreaView;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.ui.view.SelectAreaView.OnGetVillagesListener;
import com.dasinong.farmerclub.utils.StringHelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;

public class RetailerInfoSetActivity extends BaseActivity {

	public static final int EDIT_RETAILER_PHONE = 100;
	public static final int EDIT_RETAILER_NAME = EDIT_RETAILER_PHONE + 1;
	public static final int EDIT_RETAILER_ADDRESS = EDIT_RETAILER_NAME + 1;

	private TopbarView mTopbarView;
	private EditText mEditText;
	private View mSelectAreaLayout;
	private int editType;
	private SelectAreaView sav_area;
	private String info;
	private EditText et_address;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retailer_info_set);

		editType = getIntent().getIntExtra("editType", EDIT_RETAILER_PHONE);

		initView();

		setupView();
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mEditText = (EditText) this.findViewById(R.id.et_retailer_info);

		mSelectAreaLayout = this.findViewById(R.id.layout_select_area);
		sav_area = (SelectAreaView) findViewById(R.id.sav_area);
		et_address = (EditText) findViewById(R.id.et_address);
	}

	private void setupView() {
		switch (editType) {
		case EDIT_RETAILER_PHONE:
			mSelectAreaLayout.setVisibility(View.GONE);
			mEditText.setVisibility(View.VISIBLE);

			mTopbarView.setCenterText("联系方式");
			mEditText.setHint("11位手机号码");
			mEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
			break;
		case EDIT_RETAILER_NAME:
			mSelectAreaLayout.setVisibility(View.GONE);
			mEditText.setVisibility(View.VISIBLE);

			mTopbarView.setCenterText("店铺名称");
			mEditText.setHint("店铺名称");
			break;
		case EDIT_RETAILER_ADDRESS:
			mSelectAreaLayout.setVisibility(View.VISIBLE);
			mEditText.setVisibility(View.GONE);

			mTopbarView.setCenterText("店铺地址");
			break;
		}

		setVillageListener();

		mTopbarView.setLeftView(true, true);
		mTopbarView.setRightText("完成");
		mTopbarView.setRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				upLoadInfo();
			}
		});
	}

	private void setVillageListener() {
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
	}

	private void upLoadInfo() {

		String telephone = "";
		String name = "";
		String locationId = "";
		String streetAndNumber = "";

		switch (editType) {
		case EDIT_RETAILER_PHONE:
			info = mEditText.getText().toString().trim();
			if (TextUtils.isEmpty(info)) {
				showToast("请输入手机号");
				return;
			}
			if (!StringHelper.isPhoneNumber(info)) {
				showToast("请输入正确的手机号");
				return;
			}
			break;
		case EDIT_RETAILER_NAME:
			info = mEditText.getText().toString().trim();
			if (TextUtils.isEmpty(info)) {
				showToast("请店铺名称");
				return;
			}
			name = info;
			break;
		case EDIT_RETAILER_ADDRESS:
			locationId = sav_area.getVillageId();
			streetAndNumber = et_address.getText().toString().trim();
			info = sav_area.getAddress() + streetAndNumber;
			if (TextUtils.isEmpty(locationId)) {
				return;
			}
			if (TextUtils.isEmpty(streetAndNumber)) {
				showToast("请填写路名和门牌号");
				return;
			}
			break;
		}
		requestUpload(name, locationId, streetAndNumber, telephone);
	}

	private void requestUpload(String name, String loacationId, String streetAndNumber, String phone) {
		startLoadingDialog();
		RequestService.getInstance().stores(this, name, "", loacationId, streetAndNumber, "", "", "", phone, "", "", BaseEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					showToast("更新成功");
					Intent intent = new Intent();
					intent.putExtra("info", info);
					setResult(RESULT_OK, intent);
					finish();
				}
				dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}
}
