package com.dasinong.farmerclub.ui;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
	private String experience;
	private String campaignId;
	private String [] experiences = {"第一年的新手","2-3年有些经验","3-5年的老手","5-10年的专家","10年以上的资深专家"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_coupon);
		
		int id = getIntent().getIntExtra("campaignId", -1);
		if (id != -1) {
			campaignId = String.valueOf(id);
		}

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
				startLoadingDialog();
				if(checkNull()){
					RequestService.getInstance().requestCoupon(ApplyCouponActivity.this, name, unit, crop, size, last_yield, experience, "", phone, BaseEntity.class, new RequestListener(){
						@Override
						public void onSuccess(int requestCode, BaseEntity resultData) {
							if(resultData.isOk()){
								dismissLoadingDialog();
								claimCoupon();
							} 
						}


						@Override
						public void onFailed(int requestCode, Exception error, String msg) {
							dismissLoadingDialog();
						}
						
					});
				} else {
					dismissLoadingDialog();
				}
			}
		});
	}
	
	private void claimCoupon() {
		startLoadingDialog();
		RequestService.getInstance().claimCoupon(this, campaignId, BaseEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if(resultData.isOk()){
					showToast("领取成功，请到我的福利查看");
					finish();
				} else if("2001".equals(resultData.getRespCode())){
					showToast("您已领取过了，请勿重复领取");
				}else if("2002".equals(resultData.getRespCode())){
					showToast("对不起，活动已过期");
				}else if("2003".equals(resultData.getRespCode())){
					showToast("对不起，该优惠券已经被抢光了");
				}
				dismissLoadingDialog();
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
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
		experience = experiences[(checkedId-1) % 5];
		return true;
	}
}
