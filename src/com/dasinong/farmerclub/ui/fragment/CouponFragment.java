package com.dasinong.farmerclub.ui.fragment;

import java.util.List;

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

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.AllCouponEntity;
import com.dasinong.farmerclub.entity.AllCouponEntity.CouponCampaign;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.RetailerCampaignEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.BaseActivity;
import com.dasinong.farmerclub.ui.CaptureActivity;
import com.dasinong.farmerclub.ui.MyCouponActivity;
import com.dasinong.farmerclub.ui.RedeemRecordsActivity;
import com.dasinong.farmerclub.ui.SelectUserTypeActivity;
import com.dasinong.farmerclub.ui.adapter.CouponAdapter;
import com.dasinong.farmerclub.ui.adapter.RetailerCouponAdapter;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.umeng.analytics.MobclickAgent;

public class CouponFragment extends Fragment {
	private View mContentView;
	private ListView lv_coupon;
	private BaseActivity mBaseActivity;
	private TopbarView topBar;
	private boolean isFarmer = true;
	private RetailerCampaignEntity scannedCouponEntity;
	private LinearLayout ll_prompt;

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
		// userType = SelectUserTypeActivity.RETAILER;
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
				queryFarmerData();
			} else {
				queryRetailerData();
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
		lv_coupon = (ListView) mContentView.findViewById(R.id.lv_coupon);
		ll_prompt = (LinearLayout) mContentView.findViewById(R.id.ll_prompt);
	}

	private void queryFarmerData() {
		mBaseActivity.startLoadingDialog();
		RequestService.getInstance().couponCampaigns(getActivity(), AllCouponEntity.class, new RequestListener() {
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					AllCouponEntity entity = (AllCouponEntity) resultData;
					if (entity.data != null && entity.data.campaigns != null) {
						setData(entity.data.campaigns);
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

	private void queryRetailerData() {
		mBaseActivity.startLoadingDialog();
		RequestService.getInstance().getScannableCampaigns(getActivity(), RetailerCampaignEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					scannedCouponEntity = (RetailerCampaignEntity) resultData;
					if (scannedCouponEntity.data != null && scannedCouponEntity.data.campaigns != null) {
						setData(scannedCouponEntity.data.campaigns);
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
			topBar.setCenterText("店铺券管理");
			topBar.setRightText("扫一扫");
			topBar.setRightClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 友盟统计自定义统计事件
					MobclickAgent.onEvent(getActivity(), "ScanQRcode");

					Intent scanIntent = new Intent(getActivity(), CaptureActivity.class);
					startActivity(scanIntent);
				}
			});
		}
	}

	protected void setData(final List<CouponCampaign> campaigns) {
		if (isFarmer) {
			lv_coupon.setAdapter(new CouponAdapter(getActivity(), campaigns, false));
		} else {
			if (campaigns.isEmpty()) {
				ll_prompt.setVisibility(View.VISIBLE);
				lv_coupon.setVisibility(View.GONE);
			} else {
				ll_prompt.setVisibility(View.GONE);
				lv_coupon.setVisibility(View.VISIBLE);
				lv_coupon.setAdapter(new RetailerCouponAdapter(getActivity(), campaigns, false));
				lv_coupon.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent intent = new Intent(getActivity(), RedeemRecordsActivity.class);
						intent.putExtra("campaignId", campaigns.get(position).id);
						startActivity(intent);
					}
				});
			}

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onEvent(getActivity(), "CouponFragment");
	}
}
