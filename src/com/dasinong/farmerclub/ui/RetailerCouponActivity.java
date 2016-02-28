package com.dasinong.farmerclub.ui;

import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.RetailerCampaignEntity;
import com.dasinong.farmerclub.entity.AllCouponEntity.CouponCampaign;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.adapter.CouponAdapter;
import com.dasinong.farmerclub.ui.adapter.RetailerCouponAdapter;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class RetailerCouponActivity extends BaseActivity {
	private LinearLayout ll_prompt;
	private ListView lv_retail_coupon;
	private RetailerCampaignEntity scannedCouponEntity;
	private TopbarView topBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retailer_coupon);

		queryData();
		initView();
		initTopBar();
	}

	private void queryData() {
		startLoadingDialog();
		RequestService.getInstance().getScannableCampaigns(this, RetailerCampaignEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					scannedCouponEntity = (RetailerCampaignEntity) resultData;
					if (scannedCouponEntity.data != null && scannedCouponEntity.data.campaigns != null) {
						setData(scannedCouponEntity.data.campaigns);
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

	private void initView() {
		ll_prompt = (LinearLayout) findViewById(R.id.ll_prompt);
		lv_retail_coupon = (ListView) findViewById(R.id.lv_retail_coupon);
		topBar = (TopbarView) findViewById(R.id.topbar);
	}

	private void initTopBar() {
		topBar.setCenterText("店铺券管理");
		topBar.setRightText("扫一扫");
		topBar.setRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 友盟统计自定义统计事件
				MobclickAgent.onEvent(RetailerCouponActivity.this, "ScanQRcode");

				Intent scanIntent = new Intent(RetailerCouponActivity.this, CaptureActivity.class);
				startActivity(scanIntent);
			}
		});
	}

	protected void setData(final List<CouponCampaign> campaigns) {
		if (campaigns.isEmpty()) {
			ll_prompt.setVisibility(View.VISIBLE);
			lv_retail_coupon.setVisibility(View.GONE);
		} else {
			ll_prompt.setVisibility(View.GONE);
			lv_retail_coupon.setVisibility(View.VISIBLE);
			lv_retail_coupon.setAdapter(new RetailerCouponAdapter(this, campaigns, false));
			lv_retail_coupon.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(RetailerCouponActivity.this, RedeemRecordsActivity.class);
					intent.putExtra("campaignId", campaigns.get(position).id);
					startActivity(intent);
				}
			});
		}
	}
}
