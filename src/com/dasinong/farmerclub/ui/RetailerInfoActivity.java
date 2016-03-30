package com.dasinong.farmerclub.ui;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.RetailerInfoEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.lidroid.xutils.BitmapUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RetailerInfoActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout layout_headview;
	private RelativeLayout layout_phoneNumber;
	private RelativeLayout layout_name;
	private RelativeLayout layout_address;
	private ImageView iv_headerview;
	private TextView tv_phone_number;
	private TextView tv_name;
	private TextView tv_address;
	private TopbarView topbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retailer_info);

		initView();
		
		initTopBar();

		queryData();

	}

	private void initTopBar() {
		topbar.setCenterText("店铺信息");
		topbar.setLeftView(true, true);
	}

	private void initView() {
		
		topbar = (TopbarView) findViewById(R.id.topbar);
		layout_headview = (RelativeLayout) findViewById(R.id.layout_headview);
		layout_phoneNumber = (RelativeLayout) findViewById(R.id.layout_phoneNumber);
		layout_name = (RelativeLayout) findViewById(R.id.layout_name);
		layout_address = (RelativeLayout) findViewById(R.id.layout_address);

		iv_headerview = (ImageView) findViewById(R.id.iv_headview);
		tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_address = (TextView) findViewById(R.id.tv_address);

		layout_headview.setOnClickListener(this);
		layout_phoneNumber.setOnClickListener(this);
		layout_name.setOnClickListener(this);
		layout_address.setOnClickListener(this);
	}

	private void queryData() {
		startLoadingDialog();
		RequestService.getInstance().stores(this, RetailerInfoEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					RetailerInfoEntity entity = (RetailerInfoEntity) resultData;
					setData(entity);
				}

				dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	protected void setData(RetailerInfoEntity entity) {
		if (entity.data == null || entity.data.store == null) {
			return;
		}
		String headerUrl = SharedPreferencesHelper.getString(this, Field.PICTURE_ID, "");
		BitmapUtils bitmapUtils = new BitmapUtils(this);
		bitmapUtils.display(iv_headerview,  NetConfig.IMAGE_URL + headerUrl);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.header_default);
		tv_phone_number.setText(entity.data.store.phone);
		tv_name.setText(entity.data.store.name);
		tv_address.setText(entity.data.store.location);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_headview:
			
			break;
		case R.id.layout_phoneNumber:
			Intent phoneIntent = new Intent(this, RetailerInfoSetActivity.class);
			phoneIntent.putExtra("editType", RetailerInfoSetActivity.EDIT_RETAILER_PHONE);
			startActivity(phoneIntent);
			break;
		case R.id.layout_name:
			Intent nameIntent = new Intent(this, RetailerInfoSetActivity.class);
			nameIntent.putExtra("editType", RetailerInfoSetActivity.EDIT_RETAILER_NAME);
			startActivity(nameIntent);
			break;
		case R.id.layout_address:
			Intent addressIntent = new Intent(this, RetailerInfoSetActivity.class);
			addressIntent.putExtra("editType", RetailerInfoSetActivity.EDIT_RETAILER_ADDRESS);
			startActivity(addressIntent);
			break;
		}
	}
}
