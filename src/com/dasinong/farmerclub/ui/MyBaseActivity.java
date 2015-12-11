package com.dasinong.farmerclub.ui;

import android.content.Intent;

public class MyBaseActivity extends BaseActivity {
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(0, 0);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(0, 0);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, 0);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, 0);
	}
}
