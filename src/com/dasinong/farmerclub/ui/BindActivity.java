package com.dasinong.farmerclub.ui;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.LoginRegEntity;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.manager.AccountManager;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BindActivity extends BaseActivity {

	private TopbarView topbar;
	private EditText inputCode;
	private Button btnSure;
	private TextView tvSkip;
	private Button btn_scan;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind);

		initView();
		initTopBar();
	}

	private void initView() {
		topbar = (TopbarView) findViewById(R.id.topbar);
		inputCode = (EditText) findViewById(R.id.et_input_recommend_code);
		btnSure = (Button) findViewById(R.id.btn_sure);
		btn_scan = (Button) findViewById(R.id.btn_scan);
		
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String invCode = inputCode.getText().toString().trim();
				invCode = invCode.toLowerCase();
				if (invCode.length() == 4 || invCode.length() == 6) {
					setRefCode(invCode);
				} else {
					showToast("请核对邀请码是否确");
				}
			}
		});
		
		btn_scan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(BindActivity.this, CaptureActivity.class);
				startActivityForResult(intent, 0);
			}
		});
	}
	

	private void setRefCode(String invCode) {
		startLoadingDialog();
		RequestService.getInstance().setRef(BindActivity.this, invCode, LoginRegEntity.class, new RequestListener() {
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					LoginRegEntity entity = (LoginRegEntity) resultData;
					showToast("验证成功");
					
					AccountManager.saveAccount(BindActivity.this, entity);
					
					finish();
					
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

	private void initTopBar() {
		topbar.setCenterText("请填写邀请人");
		topbar.setLeftView(true, true);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String refCode = data.getStringExtra("refcode");
		setRefCode(refCode);
	}
}
