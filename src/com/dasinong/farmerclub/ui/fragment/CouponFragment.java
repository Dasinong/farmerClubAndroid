package com.dasinong.farmerclub.ui.fragment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.AllCouponEntity;
import com.dasinong.farmerclub.entity.AllCouponEntity.CouponCampaign;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.MyCouponsEntity.Coupon;
import com.dasinong.farmerclub.entity.MyCouponsEntity.UseStatus;
import com.dasinong.farmerclub.entity.RetailerCampaignEntity;
import com.dasinong.farmerclub.entity.RetailerInfoEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.BaseActivity;
import com.dasinong.farmerclub.ui.CaptureActivity;
import com.dasinong.farmerclub.ui.CouponDetailActivity;
import com.dasinong.farmerclub.ui.CouponQRCodeActivity;
import com.dasinong.farmerclub.ui.MyCouponActivity;
import com.dasinong.farmerclub.ui.MyInfoActivity;
import com.dasinong.farmerclub.ui.RetailerCouponActivity;
import com.dasinong.farmerclub.ui.RetailerInfoActivity;
import com.dasinong.farmerclub.ui.SelectUserTypeActivity;
import com.dasinong.farmerclub.ui.StorageManagerActivity;
import com.dasinong.farmerclub.ui.adapter.CouponAdapter;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.umeng.analytics.MobclickAgent;

public class CouponFragment extends Fragment implements OnClickListener {
	private View mContentView;
	private ListView lv_coupon;
	private BaseActivity mBaseActivity;
	private TopbarView topBar;
	private boolean isFarmer = true;
	private RetailerCampaignEntity scannedCouponEntity;
	private LinearLayout ll_retail;
	private RelativeLayout rl_scan_qrcode;
	private RelativeLayout rl_coupon_management;
	private RelativeLayout rl_retailer_info;
	private RelativeLayout rl_storage_manager;
	private Map<Integer, Coupon> userCouponStatus;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof BaseActivity) {
			mBaseActivity = (BaseActivity) activity;
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String userType = SharedPreferencesHelper.getString(getActivity(), Field.USER_TYPE, SelectUserTypeActivity.FARMER);
		if (SelectUserTypeActivity.RETAILER.equals(userType)) {
			isFarmer = false;
		}
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = View.inflate(getActivity(), R.layout.fragment_coupon, null);
			initView();
			if (isFarmer) {
				lv_coupon.setVisibility(View.VISIBLE);
				ll_retail.setVisibility(View.GONE);
				queryFarmerData();
			} else {
				lv_coupon.setVisibility(View.GONE);
				ll_retail.setVisibility(View.VISIBLE);
			}
			initTopBar();
		} else {
			ViewGroup parent = (ViewGroup) mContentView.getParent();
			if (parent != null) {
				parent.removeView(mContentView);
			}
		}
		return mContentView;
	}

	private void initView() {
		topBar = (TopbarView) mContentView.findViewById(R.id.topbar);
		ll_retail = (LinearLayout) mContentView.findViewById(R.id.ll_retail);
		rl_scan_qrcode = (RelativeLayout) mContentView.findViewById(R.id.rl_scan_qrcode);
		rl_coupon_management = (RelativeLayout) mContentView.findViewById(R.id.rl_coupon_management);
		rl_retailer_info = (RelativeLayout) mContentView.findViewById(R.id.rl_retailer_info);
		rl_storage_manager = (RelativeLayout) mContentView.findViewById(R.id.rl_storage_manager);
		lv_coupon = (ListView) mContentView.findViewById(R.id.lv_coupon);

		rl_scan_qrcode.setOnClickListener(this);
		rl_coupon_management.setOnClickListener(this);
		rl_retailer_info.setOnClickListener(this);
		rl_storage_manager.setOnClickListener(this);

	}

	private void queryFarmerData() {
		mBaseActivity.startLoadingDialog();
		RequestService.getInstance().couponCampaigns(getActivity(), AllCouponEntity.class, new RequestListener() {
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					AllCouponEntity entity = (AllCouponEntity) resultData;
					if (entity.data != null && entity.data.campaigns != null && entity.data.coupons != null) {
						setData(entity.data.campaigns);
						getUserCouponStatus(entity.data.coupons);
					}
				}
				mBaseActivity.dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				mBaseActivity.dismissLoadingDialog();
			}
		});
	}

	private void getUserCouponStatus(List<Coupon> coupons) {
		userCouponStatus = new HashMap<>();
		if (!coupons.isEmpty()) {
			for (Coupon coupon : coupons) {
				userCouponStatus.put(coupon.campaignId, coupon);
			}
		}
	}

	private void initTopBar() {
		if (isFarmer) {
			int institutionId = SharedPreferencesHelper.getInt(getActivity(), Field.INSTITUTIONID, 0);
			String title = "";
			switch (institutionId) {
			case 1:
				title = "陶氏活动";
				break;
			case 2:
				title = "燕化活动";
				break;
			case 3:
				title = "巴斯夫活动";
				break;
			default:
				title = "大户俱乐部活动";
				break;
			}
			topBar.setCenterText(title);
			topBar.setRightText("我的");
			topBar.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent myCouponIntent = new Intent(getActivity(), MyCouponActivity.class);
					startActivity(myCouponIntent);
				}
			});
		} else {
			topBar.setCenterText("店铺管理");
		}
	}

	protected void setData(final List<CouponCampaign> campaigns) {
		if (isFarmer) {
			
			lv_coupon.setAdapter(new CouponAdapter(getActivity(), campaigns, false));

			lv_coupon.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					CouponCampaign item = (CouponCampaign) parent.getItemAtPosition(position);
					if (userCouponStatus.containsKey(item.id)) {
						Coupon coupon = userCouponStatus.get(item.id);
						if (UseStatus.NOT_USED.equals(coupon.displayStatus)) {
							Intent intent = new Intent(getActivity(), CouponQRCodeActivity.class);
							intent.putExtra("picUrl", item.pictureUrls.get(0));
							intent.putExtra("name", item.name);
							intent.putExtra("time", coupon.claimedAt);
							intent.putExtra("stores", (Serializable) item.stores);
							intent.putExtra("id", coupon.id);
							startActivity(intent);
						} else {
							Intent intent = new Intent(getActivity(), CouponDetailActivity.class);
							intent.putExtra("campaignId", item.id);
							intent.putExtra("isApply", false);
							startActivity(intent);
						}
					} else {
						Intent intent = new Intent(getActivity(), CouponDetailActivity.class);
						intent.putExtra("campaignId", item.id);
						intent.putExtra("isApply", true);
						startActivity(intent);
					}
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_scan_qrcode:
			Intent scanIntent = new Intent(getActivity(), CaptureActivity.class);
			startActivity(scanIntent);
			break;
		case R.id.rl_coupon_management:

			Intent retailerCouponIntent = new Intent(getActivity(), RetailerCouponActivity.class);
			startActivity(retailerCouponIntent);
			break;
		case R.id.rl_retailer_info:
			Intent infoIntent = new Intent(getActivity(), RetailerInfoActivity.class);
			startActivity(infoIntent);
			break;
		case R.id.rl_storage_manager:
			Intent storageManagerIntent = new Intent(getActivity(), StorageManagerActivity.class);
			startActivity(storageManagerIntent);
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onEvent(getActivity(), "CouponFragment");
	}
}
