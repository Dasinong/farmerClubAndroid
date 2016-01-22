package com.dasinong.farmerclub.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.lidroid.xutils.BitmapUtils;

public class CouponQRCodeActivity extends BaseActivity {

	private ImageView iv_pic;
	private TextView tv_title;
	private TextView tv_amount;
	private TextView tv_time;
	private ImageView iv_qrcode;
	private TextView tv_coupon_id;
	private String picUrl;
	private String name;
	private int amount;
	private String time;
	private long id;
	private TopbarView topBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon_qrcode);

		picUrl = getIntent().getStringExtra("picUrl");
		name = getIntent().getStringExtra("name");
		amount = getIntent().getIntExtra("amount", -1);
		time = getIntent().getStringExtra("time");
		id = getIntent().getLongExtra("id", -1);

		initView();

		setData();

	}

	private void initView() {
		topBar = (TopbarView) findViewById(R.id.topbar);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_amount = (TextView) findViewById(R.id.tv_amount);
		tv_time = (TextView) findViewById(R.id.tv_time);
		iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
		tv_coupon_id = (TextView) findViewById(R.id.tv_coupon_id);
	}

	private void setData() {
		topBar.setCenterText("大户俱乐部福利");
		topBar.setLeftView(true, true);
		BitmapUtils bitmapUtils = new BitmapUtils(this);
		bitmapUtils.display(iv_pic, NetConfig.PET_IMAGE + "couponCampaign/" + picUrl);

		tv_title.setText(name);
		tv_amount.setText("¥" + amount + ".00");
		tv_time.setText("兑换时间：" + time);
		bitmapUtils.display(iv_qrcode, NetConfig.PET_IMAGE + "couponCampaign/QRCode/" + id + ".png");
		tv_coupon_id.setText("券号 " + id);
	}
}
