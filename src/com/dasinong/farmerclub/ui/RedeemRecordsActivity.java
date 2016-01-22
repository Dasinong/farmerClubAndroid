package com.dasinong.farmerclub.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.MyCouponsEntity.Coupon;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RedeemRecordsActivity extends BaseActivity {
	private ImageView iv_pic;
	private TextView tv_title;
	private TextView tv_time;
	private TextView tv_count;
	private TextView tv_all_amount;
	private TableLayout list;
	private List<Coupon> couponList;
	private String title;
	private String time;
	private String url;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_redeem_records);

		couponList = (List<Coupon>) getIntent().getSerializableExtra("list");
		title = getIntent().getStringExtra("title");
		time = getIntent().getStringExtra("time");
		url = getIntent().getStringExtra("url");

		initView();
		setData();

	}

	private void setData() {
		tv_title.setText(title);
		tv_time.setText("使用时间：" + time);
		tv_count.setText(couponList.size() + "");
		tv_all_amount.setText((couponList.size() * couponList.get(0).amount) + "");

		for (Coupon coupon : couponList) {
			TableRow item = (TableRow) View.inflate(this, R.layout.item_redeem_record, null);
			TextView tv_used_time = (TextView) item.findViewById(R.id.tv_used_time);
			TextView tv_phone_four = (TextView) item.findViewById(R.id.tv_phone_four);
			TextView tv_coupon_id = (TextView) item.findViewById(R.id.tv_coupon_id);
			tv_used_time.setText(formatTime(coupon.redeemedAt));
			tv_phone_four.setText("1234");
			tv_coupon_id.setText(coupon.id + "");
			list.addView(item);
		}
	}

	private void initView() {
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_count = (TextView) findViewById(R.id.tv_count);
		tv_all_amount = (TextView) findViewById(R.id.tv_all_amount);
		list = (TableLayout) findViewById(R.id.tl_table);
	}

	private String formatTime(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		Date date = new Date();

		date.setTime(time);
		return sdf.format(date).toString();
	}
}
