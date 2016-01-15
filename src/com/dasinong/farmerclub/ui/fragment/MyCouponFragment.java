package com.dasinong.farmerclub.ui.fragment;

import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyCouponFragment extends Fragment {

	private int fragmentPosition;

	public static Fragment newInstance(int position) {
		MyCouponFragment fragment = new MyCouponFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentPosition = getArguments() != null ? getArguments().getInt("position") : 0;
		queryData(fragmentPosition);
	}

	private void queryData(int position) {
		RequestService.getInstance().getCoupons(getActivity(), BaseEntity.class , new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				
			}
		});
	}
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
