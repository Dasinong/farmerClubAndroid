package com.dasinong.farmerclub.ui;

import com.dasinong.farmerclub.R;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class ApplyCouponActivity extends BaseActivity {
	private EditText et_name;
	private EditText et_unit;
	private EditText et_crop;
	private EditText et_size;
	private EditText et_last_yield;
	private EditText et_phone;
	private RadioGroup rg_experience;
	private Button btn_submit;
	private String name;
	private String unit;
	private String crop;
	private String size;
	private String last_yield;
	private String phone;
	private int checkedId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_coupon);

		initView();
		initEvent();
	}

	private void initView() {
		et_name = (EditText) findViewById(R.id.et_name);
		et_unit = (EditText) findViewById(R.id.et_unit);
		et_crop = (EditText) findViewById(R.id.et_crop);
		et_size = (EditText) findViewById(R.id.et_size);
		et_last_yield = (EditText) findViewById(R.id.et_last_yield);
		et_phone = (EditText) findViewById(R.id.et_phone);
		rg_experience = (RadioGroup) findViewById(R.id.rg_experience);
		btn_submit = (Button) findViewById(R.id.btn_submit);
	}
	
	private void initEvent() {
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkNull()){
					showToast("开始领券");
				}
			}
		});
	}

	protected boolean checkNull() {
		name = et_name.getText().toString().trim();
		unit = et_unit.getText().toString().trim();
		crop = et_crop.getText().toString().trim();
		size = et_size.getText().toString().trim();
		last_yield = et_last_yield.getText().toString().trim();
		phone = et_phone.getText().toString().trim();
		checkedId = rg_experience.getCheckedRadioButtonId();
		
		if(TextUtils.isEmpty(name)){
			showToast("请填写姓名");
			return false;
		}
		if(TextUtils.isEmpty(crop)){
			showToast("请填写作物");
			return false;
		}
		if(TextUtils.isEmpty(size)){
			showToast("请填写面积");
			return false;
		}
		if(TextUtils.isEmpty(last_yield)){
			showToast("请填写去年产量");
			return false;
		}
		if(TextUtils.isEmpty(phone)){
			showToast("请填写联系电话");
			return false;
		}
		if(TextUtils.isEmpty(name)){
			showToast("请填写姓名");
			return false;
		}
		if(TextUtils.isEmpty(name)){
			showToast("请填写姓名");
			return false;
		}
		if(checkedId == -1){
			showToast("请选择种植经验");
			return false;
		}
		return true;
	}
}
