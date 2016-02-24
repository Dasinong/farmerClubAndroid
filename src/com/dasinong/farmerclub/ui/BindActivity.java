package com.dasinong.farmerclub.ui;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.LoginRegEntity;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist_recommend);

		initView();

		initTopBar();
	}

	private void initView() {
		topbar = (TopbarView) findViewById(R.id.topbar);
		inputCode = (EditText) findViewById(R.id.et_input_recommend_code);
		btnSure = (Button) findViewById(R.id.btn_sure);
		tvSkip = (TextView) findViewById(R.id.tv_skip);

		tvSkip.setVisibility(View.GONE);

		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String invCode = inputCode.getText().toString().trim();
				invCode = invCode.toLowerCase();
				String invRegex = "^[0-9a-z]{6}$";
				if (!TextUtils.isEmpty(invCode) && invCode.matches(invRegex)) {
					startLoadingDialog();
					RequestService.getInstance().setRef(BindActivity.this, invCode, LoginRegEntity.class, new RequestListener() {
						@Override
						public void onSuccess(int requestCode, BaseEntity resultData) {
							if (resultData.isOk()) {
								LoginRegEntity entity = (LoginRegEntity) resultData;
								showToast("验证成功");

								SharedPreferencesHelper.setInt(BindActivity.this, Field.REFUID, entity.getData().getRefuid());
								SharedPreferencesHelper.setInt(BindActivity.this, Field.INSTITUTIONID, entity.getData().getInstitutionId());
								
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
					showToast("请核对邀请码是否确");
				}
			}
		});
	}

	private void initTopBar() {
		topbar.setCenterText("请填写邀请人");
		topbar.setLeftView(true, true);
	}
}