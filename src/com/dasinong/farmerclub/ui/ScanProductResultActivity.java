package com.dasinong.farmerclub.ui;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ScanProductResultActivity extends BaseActivity implements OnClickListener {
	private TextView tv_box_code;
	private TextView tv_name;
	private TextView tv_number;
	private TextView tv_volume;
	private TextView tv_count;
	private Button btn_finish;
	private Button btn_continue;

	private String boxcode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_product_result);

		boxcode = getIntent().getStringExtra("boxcode");

		initView();

		queryData();
	}

	private void initView() {
		tv_box_code = (TextView) findViewById(R.id.tv_box_code);

		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_number = (TextView) findViewById(R.id.tv_number);
		tv_volume = (TextView) findViewById(R.id.tv_volume);
		tv_count = (TextView) findViewById(R.id.tv_count);

		btn_finish = (Button) findViewById(R.id.btn_finish);
		btn_continue = (Button) findViewById(R.id.btn_continue);
		
		tv_box_code.setText(boxcode);

		btn_finish.setOnClickListener(this);
		btn_continue.setOnClickListener(this);
	}

	private void queryData() {
		RequestService.getInstance().getWinsafeProductInfo(this, boxcode, BaseEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if(resultData.isOk()){
					setData();
				} else {
					setData();
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				setData();
			}
		});
	}

	protected void setData() {
		setDataToView("",tv_name);
		setDataToView("",tv_number);
		setDataToView("",tv_volume);
		setDataToView("",tv_count);
	}

	private void setDataToView(String text, TextView view) {
		if(TextUtils.isEmpty(text)){
			((View)view.getParent()).setVisibility(View.INVISIBLE);
		} else {
			view.setText(text);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_finish:
			this.finish();
			break;
		case R.id.btn_continue:
			Intent intent = new Intent(this,CaptureActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}
}
