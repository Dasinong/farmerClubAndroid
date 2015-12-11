package com.dasinong.farmerclub.ui;

import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.view.TopbarView;

import android.R;
import android.os.Bundle;
import android.widget.EditText;

public class LoginActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startLoadingDialog();
		
		initView();
		setView();  
	}
	
	private void test() {
	}

	private void setView() {
//		mTopbarView = this.findViewById(R.id.topbar)
	}

	private void initView() {
		
	}

	
	
}
