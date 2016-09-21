package com.dasinong.farmerclub.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.RetailerCampaignEntity;
import com.dasinong.farmerclub.entity.ScannedCouponsEntity;
import com.dasinong.farmerclub.entity.MyCouponsEntity.Coupon;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.lidroid.xutils.BitmapUtils;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	private TopbarView topBar;
	private LinearLayout ll_money;
	private String type;
	private int campaignId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_redeem_records);

		campaignId = getIntent().getIntExtra("campaignId", -1);
		type = getIntent().getStringExtra("type");
		if(campaignId != -1){
			requestData(campaignId);
		}
		
		initView();
		initTopBar();
	}

	private void requestData(int campaignId) {
		startLoadingDialog();
		RequestService.getInstance().getScannedCouponsByCampaignId(this, campaignId + "", ScannedCouponsEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					ScannedCouponsEntity entity = (ScannedCouponsEntity) resultData;
					if (entity.data != null && entity.data.coupons != null && entity.data.campaign != null) {
						couponList = entity.data.coupons;
						title = entity.data.campaign.name;
						time = time2String(entity.data.campaign.redeemTimeStart, entity.data.campaign.redeemTimeEnd);
						url = entity.data.campaign.pictureUrls.get(0);
						setData();
					}
				}
				dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	private void initTopBar() {
		topBar.setCenterText("兑换记录");
		topBar.setLeftView(true, true);
	}

	private void setData() {
		BitmapUtils bitmapUtils = new BitmapUtils(this);
		bitmapUtils.display(iv_pic, NetConfig.COUPON_IMAGE + url);
		tv_title.setText(title);
		tv_time.setText("使用时间：" + time);
		tv_count.setText(couponList.size() + "");
		if(couponList.isEmpty()){
			tv_all_amount.setText("0");
			return;
		} else {
			tv_all_amount.setText((couponList.size() * couponList.get(0).amount) + "");
		}
		
		for (Coupon coupon : couponList) {
			TableRow item = (TableRow) View.inflate(this, R.layout.item_redeem_record, null);
			TextView tv_used_time = (TextView) item.findViewById(R.id.tv_used_time);
			TextView tv_phone_four = (TextView) item.findViewById(R.id.tv_phone_four);
			TextView tv_coupon_id = (TextView) item.findViewById(R.id.tv_coupon_id);
			tv_used_time.setText(formatTime(coupon.redeemedAt));
			tv_phone_four.setText(coupon.claimerCell);
			tv_coupon_id.setText(coupon.id + "");
			list.addView(item);
		}
	}

	private void initView() {
		topBar = (TopbarView) findViewById(R.id.topbar);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_count = (TextView) findViewById(R.id.tv_count);
		ll_money = (LinearLayout) findViewById(R.id.ll_money);
		tv_all_amount = (TextView) findViewById(R.id.tv_all_amount);
		list = (TableLayout) findViewById(R.id.tl_table);

		if("INSURANCE".equals(type)){
			ll_money.setVisibility(View.GONE);
		}

		if(campaignId == 15){
			tv_time.setVisibility(View.GONE);
		}
	}

	private String formatTime(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		Date date = new Date();

		date.setTime(time);
		return sdf.format(date).toString();
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
