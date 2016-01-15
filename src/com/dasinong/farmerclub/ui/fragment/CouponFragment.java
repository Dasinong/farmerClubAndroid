package com.dasinong.farmerclub.ui.fragment;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.AllCouponEntity;
import com.dasinong.farmerclub.entity.AllCouponEntity.Data;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.BaseActivity;
import com.dasinong.farmerclub.ui.adapter.CouponAdapter;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CouponFragment extends Fragment {
	private View mContentView;
	private ListView lv_coupon;
	private BaseActivity mBaseActivity;
	private TopbarView topBar;

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
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = View.inflate(getActivity(), R.layout.fragment_coupon, null);
			initView();
			queryData();
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
	}

	private void queryData() {
		mBaseActivity.startLoadingDialog();
		RequestService.getInstance().couponCampaigns(getActivity(), AllCouponEntity.class, new RequestListener() {
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					AllCouponEntity entity = (AllCouponEntity) resultData;
					if (entity.data != null) {
						setData(entity.data);
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
		topBar.setCenterText("大户俱乐部福利");
	}

	protected void setData(Data data) {
		lv_coupon.setAdapter(new CouponAdapter(getActivity(), data.campaigns, false));
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onEvent(getActivity(), "CouponFragment");
	}
}
