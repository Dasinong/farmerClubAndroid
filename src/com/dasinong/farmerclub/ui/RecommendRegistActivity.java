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

public class RecommendRegistActivity extends BaseActivity implements OnClickListener {

	private TopbarView topbar;
	private Button btnSure;
	private EditText inputCode;
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

		btnSure.setOnClickListener(this);
		tvSkip.setOnClickListener(this);
	}

	private void initTopBar() {
		topbar.setCenterText("请填写邀请人");
		topbar.setLeftView(true, true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sure:
			String invCode = inputCode.getText().toString().trim();
			invCode = invCode.toLowerCase();
			String invRegex = "^[0-9a-z]{6}$";
			if (!TextUtils.isEmpty(invCode) && invCode.matches(invRegex)) {
				RecommendRegistActivity.this.startLoadingDialog();
				RequestService.getInstance().setRef(RecommendRegistActivity.this, invCode, LoginRegEntity.class, new RequestListener() {
					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if (resultData.isOk()) {
							LoginRegEntity entity = (LoginRegEntity) resultData;
							RecommendRegistActivity.this.showToast("验证成功");
							
							SharedPreferencesHelper.setInt(RecommendRegistActivity.this, Field.REFUID, entity.getData().getRefuid());
							SharedPreferencesHelper.setInt(RecommendRegistActivity.this, Field.INSTITUTIONID, entity.getData().getInstitutionId());
							Class clazz = null;
							if(entity.getData().getInstitutionId() == 0){
								clazz = AddCropActivity.class;
							} else {
								clazz = SelectCropActivity.class;
							}
							
							Intent selectIntent = new Intent(RecommendRegistActivity.this, clazz);
							RecommendRegistActivity.this.startActivity(selectIntent);

						} else {
							RecommendRegistActivity.this.showToast(resultData.getMessage());
						}
						RecommendRegistActivity.this.dismissLoadingDialog();
					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {
						showToast(R.string.please_check_netword);
						RecommendRegistActivity.this.dismissLoadingDialog();
					}
				});
			} else {
				RecommendRegistActivity.this.showToast("请核对邀请码是否确");
			}
			break;
		case R.id.tv_skip:
			Intent addCropIntrent = new Intent(this, AddCropActivity.class);
			startActivity(addCropIntrent);
			break;
		}

	}

}
