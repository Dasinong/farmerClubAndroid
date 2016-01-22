package com.dasinong.farmerclub.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.AllCouponEntity.CouponCampaign;
import com.dasinong.farmerclub.entity.AllCouponEntity.Store;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.CouponDetailEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.GraphicUtils;

public class CouponDetailActivity extends BaseActivity {
	private LinearLayout ll_pics;
	private LinearLayout ll_exchange_place;
	private TopbarView topBar;
	private Map<String, List<Store>> map;
	private TextView tv_title;
	private TextView tv_amount;
	private TextView tv_claim;
	private TextView tv_description;
	private TextView tv_redeem;
	private Button btn_apply;
	private int campaignId;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon_detail);

		initView();

		int id = getIntent().getIntExtra("campaignId", -1);
		if (id != -1) {
			queryData(String.valueOf(id));
		}

		initTopBar();
		initEvent();
	}

	private void initView() {
		ll_pics = (LinearLayout) findViewById(R.id.ll_pics);
		ll_exchange_place = (LinearLayout) findViewById(R.id.ll_exchange_place);
		topBar = (TopbarView) findViewById(R.id.topbar);
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_amount = (TextView) findViewById(R.id.tv_amount);
		tv_claim = (TextView) findViewById(R.id.tv_claim);
		tv_description = (TextView) findViewById(R.id.tv_description);
		tv_redeem = (TextView) findViewById(R.id.tv_redeem);
		btn_apply = (Button) findViewById(R.id.btn_apply);

	}

	private void initEvent() {
		btn_apply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CouponDetailActivity.this,ApplyCouponActivity.class);
				intent.putExtra("campaignId", campaignId);
				startActivity(intent);
			}
		});
	}
	
	private void queryData(String id) {
		startLoadingDialog();
		RequestService.getInstance().couponCampaigns(this, id, CouponDetailEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					CouponDetailEntity entity = (CouponDetailEntity) resultData;
					if (entity.data != null && entity.data.campaign != null) {
						campaignId = entity.data.campaign.id;
						formatData(entity.data.campaign);
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

	private void formatData(CouponCampaign campaign) {
		map = new HashMap<String, List<Store>>();
		if (campaign.stores != null && !campaign.stores.isEmpty()) {
			for (int i = 0; i < campaign.stores.size(); i++) {
				if(!map.containsKey(campaign.stores.get(i).province)){
					List<Store> list = new ArrayList<Store>();
					list.add(campaign.stores.get(i));
					map.put(campaign.stores.get(i).province, list);
				} else {
					map.get(campaign.stores.get(i).province).add(campaign.stores.get(i));
				}
			}
		}
		setData(campaign);
	}

	private void initTopBar() {
		topBar.setCenterText("大户俱乐部福利");
		topBar.setLeftView(true, true);
	}

	private void setData(CouponCampaign campaign) {
		
		tv_title.setText(campaign.name);
		
		String claimTime = time2String(campaign.claimTimeStart, campaign.claimTimeEnd);
		
		tv_claim.setText(claimTime);
		
		tv_description.setText(campaign.description);
		
		String redeemTime = time2String(campaign.redeemTimeStart, campaign.redeemTimeEnd);
		
		tv_redeem.setText(redeemTime);
		
		tv_amount.setText("¥" + campaign.amount + ".00");
		
		for (int i = 0; i < campaign.pictureUrls.size(); i++) {
			ImageView imageView = new ImageView(this);

			LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, GraphicUtils.dip2px(this, 200));
			params.setMargins(0, 0, 0, GraphicUtils.dip2px(this, 5));
			imageView.setImageResource(R.drawable.small);
			imageView.setLayoutParams(params);
			ll_pics.addView(imageView);
		}
		
		Set<String> keySet = map.keySet();
		for (String province : keySet) {
			TextView tv = new TextView(this);
			LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, GraphicUtils.dip2px(this, 32.5f));
			tv.setText(province);
			tv.setPadding(GraphicUtils.dip2px(this, 15), 0, 0, 0);
			tv.setTextColor(getResources().getColor(R.color.color_666666));
			tv.setBackgroundResource(R.color.color_F5F5F5);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

			tv.setLayoutParams(params);
			ll_exchange_place.addView(tv);
			
			for (Store store : map.get(province)) {
				View view = View.inflate(this, R.layout.item_exchange_place, null);
				TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
				TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
				TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
				
				tv_name.setText(store.name);
				tv_address.setText(store.location);
				tv_phone.setText(store.phone);
				
				LinearLayout.LayoutParams dividerParams = new LayoutParams(LayoutParams.MATCH_PARENT, GraphicUtils.dip2px(this, 0.5f));
				View divider = new View(this);
				divider.setBackgroundResource(R.color.color_DBE3E5);
				divider.setLayoutParams(dividerParams);
				view.setPadding(0, GraphicUtils.dip2px(this, 15), 0, GraphicUtils.dip2px(this, 15));
				ll_exchange_place.addView(view);
				ll_exchange_place.addView(divider);
			}
		}
	}
	
	private String time2String(long start, long end){
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		Date date = new Date();
		
		date.setTime(start);
		String strStart = sdf.format(date).toString();
		
		date.setTime(end);
		String strEnd = sdf.format(date).toString();
		
		return strStart + "-" + strEnd;
	}

}
