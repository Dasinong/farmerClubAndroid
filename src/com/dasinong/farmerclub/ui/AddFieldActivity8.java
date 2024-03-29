package com.dasinong.farmerclub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.umeng.analytics.MobclickAgent;

public class AddFieldActivity8 extends MyBaseActivity implements OnClickListener{
	private Button btn_direct_seeding;
	private Button btn_transplanting;
	private TopbarView topbar;
	
	public static final String DIRECT = "direct";
	public static final String TRANSPLANTING = "transplanting";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(0, 0);
		setContentView(R.layout.activity_add_field_8);
		
		btn_direct_seeding = (Button) findViewById(R.id.btn_direct_seeding);
		btn_transplanting = (Button) findViewById(R.id.btn_transplanting);
		topbar = (TopbarView) findViewById(R.id.topbar);
		
		initTopBar();
		
		btn_direct_seeding.setOnClickListener(this);
		btn_transplanting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_direct_seeding:
			MobclickAgent.onEvent(this, "AddFieldFifth");
			SharedPreferencesHelper.setString(this, Field.SEEDING_METHOD, "true");
			break;
		case R.id.btn_transplanting:
			MobclickAgent.onEvent(this, "AddFieldFifth");
			SharedPreferencesHelper.setString(this, Field.SEEDING_METHOD, "false");
			break;
		}
		Intent intent = new Intent(this, SelectSubStageActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}
	
	private void initTopBar() {
		topbar.setCenterText("种植方式");
		topbar.setLeftView(true, true);
	}
}
