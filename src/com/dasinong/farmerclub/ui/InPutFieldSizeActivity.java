package com.dasinong.farmerclub.ui;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.NearbyUser;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.StringHelper;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InPutFieldSizeActivity extends MyBaseActivity {

	private TextView tv_user_count;
	private EditText et_field_size;
	private Button btn_sure_size;
	private TopbarView topbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_field_size);
		tv_user_count = (TextView) findViewById(R.id.tv_user_count);
		et_field_size = (EditText) findViewById(R.id.et_field_size);
		btn_sure_size = (Button) findViewById(R.id.btn_sure_size);
		topbar = (TopbarView) findViewById(R.id.topbar);
		initUserCount();

		initTopBar();

		btn_sure_size.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String fieldSize = et_field_size.getText().toString().trim();
				if (StringHelper.invalidString(fieldSize)) {
					showToast("请输入有效值");
					return;
				}
				
				MobclickAgent.onEvent(InPutFieldSizeActivity.this, "AddFieldThird");
				
				SharedPreferencesHelper.setString(InPutFieldSizeActivity.this, Field.FIELD_SIZE, fieldSize);

				Intent intent = new Intent(InPutFieldSizeActivity.this, SelectSubStageActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});
	}

	private void initTopBar() {
		topbar.setCenterText("农田信息");
		topbar.setLeftView(true, true);
	}

	private void initUserCount() {
		String latitude = SharedPreferencesHelper.getString(this, Field.LATITUDE, "");
		String longitude = SharedPreferencesHelper.getString(this, Field.LONGITUDE, "");
		RequestService.getInstance().searchNearUser(this, latitude, longitude, NearbyUser.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					NearbyUser nearbyUser = (NearbyUser) resultData;
					tv_user_count.setText("啊呀,你附近有" + nearbyUser.data + "个用户");
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				
			}
		});
	}
}
