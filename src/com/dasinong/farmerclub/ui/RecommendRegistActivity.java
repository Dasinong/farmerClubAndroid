package com.dasinong.farmerclub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.LoginRegEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.manager.AccountManager;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.AppInfoUtils;
import com.umeng.analytics.MobclickAgent;

public class RecommendRegistActivity extends BaseActivity implements OnClickListener {

	private TopbarView topbar;
	private Button btnSure;
	private EditText inputCode;
	private TextView tvSkip;
	private Button btn_scan;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind);

		int appInstitutionId = AppInfoUtils.getInstitutionId(this);

		if (appInstitutionId != 0) {
			Intent addCropIntrent = new Intent(this, SelectUserTypeActivity.class);
			startActivity(addCropIntrent);
			finish();
			return;
		}

		initView();
		initTopBar();
	}

	private void initView() {
		topbar = (TopbarView) findViewById(R.id.topbar);
		btn_scan = (Button) findViewById(R.id.btn_scan);
		inputCode = (EditText) findViewById(R.id.et_input_recommend_code);
		btnSure = (Button) findViewById(R.id.btn_sure);
		tvSkip = (TextView) findViewById(R.id.tv_skip);

		btnSure.setOnClickListener(this);
		tvSkip.setOnClickListener(this);
		btn_scan.setOnClickListener(this);
	}

	private void initTopBar() {
		topbar.setCenterText("请填写邀请人");
		topbar.setLeftView(true, true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_scan:
			Intent intent = new Intent(RecommendRegistActivity.this, CaptureActivity.class);
			intent.putExtra("isForResult", true);
			startActivityForResult(intent, 0);
			break;
		case R.id.btn_sure:

			MobclickAgent.onEvent(this, "InputRecommend");

			String invCode = inputCode.getText().toString().trim();
			invCode = invCode.toLowerCase();
			String invRegex = "^[0-9a-z]{6}$";
			if (!TextUtils.isEmpty(invCode) && invCode.matches(invRegex)) {
				setRefCode(invCode);
			} else {
				RecommendRegistActivity.this.showToast("请核对邀请码是否确");
			}
			break;
		case R.id.tv_skip:

			MobclickAgent.onEvent(this, "SkipRecommend");

			Intent addCropIntrent = new Intent(this, SelectUserTypeActivity.class);
			startActivity(addCropIntrent);
			break;
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String refCode = data.getStringExtra("refcode");
		setRefCode(refCode);
	}

	private void setRefCode(String invCode) {
		RecommendRegistActivity.this.startLoadingDialog();
		RequestService.getInstance().setRef(RecommendRegistActivity.this, invCode, LoginRegEntity.class, new RequestListener() {
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					LoginRegEntity entity = (LoginRegEntity) resultData;
					RecommendRegistActivity.this.showToast("验证成功");

					// SharedPreferencesHelper.setInt(RecommendRegistActivity.this,
					// Field.REFUID, entity.getData().getRefuid());
					// SharedPreferencesHelper.setInt(RecommendRegistActivity.this,
					// Field.INSTITUTIONID,
					// entity.getData().getInstitutionId());

					AccountManager.saveAccount(RecommendRegistActivity.this, entity);

					Intent selectIntent = new Intent(RecommendRegistActivity.this, SelectUserTypeActivity.class);
					RecommendRegistActivity.this.startActivity(selectIntent);

				} else {
					RecommendRegistActivity.this.showToast(resultData.getMessage());
				}
				RecommendRegistActivity.this.dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				RecommendRegistActivity.this.dismissLoadingDialog();
			}
		});
	}
	


}
