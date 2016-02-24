package com.dasinong.farmerclub.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.AppInfoUtils;

public class RecommendActivity extends BaseActivity {

	private TopbarView topbar;
	private boolean isShow = true;
	private TextView tv_recommend_code;
	private EditText et_phone;
	private Button btn_send;
	private String refCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		int refuId = SharedPreferencesHelper.getInt(this, Field.REFUID, -1);
		int serverInstitutionId = SharedPreferencesHelper.getInt(this, Field.INSTITUTIONID, 0);
		int appInstitutionId = AppInfoUtils.getInstitutionId(this);

		refCode = SharedPreferencesHelper.getString(this, Field.REFCODE, "");

		if (refuId > 0 || serverInstitutionId > 0 || appInstitutionId > 0) {
			isShow = false;
		}

		initView();
		initTopBar();

	}

	private void initView() {
		topbar = (TopbarView) findViewById(R.id.topbar);
		tv_recommend_code = (TextView) findViewById(R.id.tv_recommend_code);
		et_phone = (EditText) findViewById(R.id.et_phone);
		btn_send = (Button) findViewById(R.id.btn_send);
		
		tv_recommend_code.setText(refCode);
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String regExp = "^1[3|4|5|8][0-9]\\d{8}$";
				String phone = et_phone.getText().toString().trim();
				if (!TextUtils.isEmpty(phone) && phone.matches(regExp)) {
					startLoadingDialog();
					RequestService.getInstance().refApp(RecommendActivity.this, phone, BaseEntity.class, new RequestListener() {

						@Override
						public void onSuccess(int requestCode, BaseEntity resultData) {
							if (resultData.isOk()) {
								showToast("邀请短信已发出");
								dismissLoadingDialog();
							} else {
								showToast(resultData.getMessage());
								dismissLoadingDialog();
							}
						}

						@Override
						public void onFailed(int requestCode, Exception error, String msg) {
							dismissLoadingDialog();
						}
					});
				} else {
					showToast("请输入正确的手机号");
				}
			}

		});
	}

	private void initTopBar() {
		topbar.setCenterText("有奖推荐");
		topbar.setLeftView(true, true);
	}
}
