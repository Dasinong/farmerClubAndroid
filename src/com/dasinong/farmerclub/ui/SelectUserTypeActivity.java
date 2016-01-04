package com.dasinong.farmerclub.ui;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.R.string;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.view.TopbarView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelectUserTypeActivity extends BaseActivity implements OnClickListener {
	private TopbarView topBar;
	private Button btn_farmer;
	private Button btn_businessman;
	private Button btn_other;
	private Intent intent;

	private static final String FARMER = "farmer";
	private static final String RETAILER = "retailer";
	private static final String OTHERS = "others";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_user_type);

		initView();
		setEvent();
		initTopBar();
	}

	private void initView() {
		topBar = (TopbarView) findViewById(R.id.topbar);
		btn_farmer = (Button) findViewById(R.id.btn_farmer);
		btn_businessman = (Button) findViewById(R.id.btn_businessman);
		btn_other = (Button) findViewById(R.id.btn_other);
	}

	private void setEvent() {
		btn_farmer.setOnClickListener(this);
		btn_businessman.setOnClickListener(this);
		btn_other.setOnClickListener(this);
	}

	private void initTopBar() {
		topBar.setCenterText("请选择身份");
	}

	@Override
	public void onClick(View v) {
		intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_farmer:
			intent.setClass(this, SelectCropActivity.class);
			sendQurey(FARMER);
			break;
		case R.id.btn_businessman:
			intent.setClass(this, SubmitBusinessmanInfo.class);
			sendQurey(RETAILER);
			break;
		case R.id.btn_other:
			intent.setClass(this, SelectCropActivity.class);
			sendQurey(OTHERS);
			break;
		}
	}

	public void sendQurey(String type) {
		startLoadingDialog();
		RequestService.getInstance().setUserType(this, type, BaseEntity.class, new RequestListener() {
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					showToast("设置成功");
					startActivity(intent);
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
}
