package com.dasinong.farmerclub.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.ClaimCouponEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.view.TopbarView;

import android.content.Intent;
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
	private TopbarView topBar;

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
		initTopBar();
	}

	private void initTopBar() {
		topBar.setCenterText("申领表");
		topBar.setLeftView(true, true);
	}

	private void initView() {
		topBar = (TopbarView) findViewById(R.id.topbar);
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
		RequestService.getInstance().claimCoupon(this, campaignId, ClaimCouponEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if(resultData.isOk()){
					showToast("领取成功");
					ClaimCouponEntity entity = (ClaimCouponEntity) resultData;
					if(entity.data != null && entity.data.coupon != null && entity.data.coupon.campaign != null){
						Intent intent = new Intent(ApplyCouponActivity.this, CouponQRCodeActivity.class);
						intent.putExtra("picUrl",entity.data.coupon.campaign.pictureUrls.get(0));
						intent.putExtra("name", entity.data.coupon.campaign.name);
						intent.putExtra("amount", entity.data.coupon.campaign.amount);
						String time = time2String(entity.data.coupon.campaign.redeemTimeStart, entity.data.coupon.campaign.redeemTimeEnd);
						intent.putExtra("time", time);
						intent.putExtra("id", entity.data.coupon.id);
						startActivity(intent);
						finish();
					}
					
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
	
	private String time2String(long start, long end) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		Date date = new Date();

		date.setTime(start);
		String strStart = sdf.format(date).toString();

		date.setTime(end);
		String strEnd = sdf.format(date).toString();

		return strStart + "-" + strEnd;
	}
}
