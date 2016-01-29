package com.dasinong.farmerclub.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dasinong.farmerclub.entity.MyCouponsEntity.Coupon;
import com.dasinong.farmerclub.entity.MyCouponsEntity.UseStatus;
import com.dasinong.farmerclub.ui.CouponQRCodeActivity;
import com.dasinong.farmerclub.ui.adapter.MyCouponAdapter;
import com.dasinong.farmerclub.utils.GraphicUtils;

public class MyCouponFragment extends Fragment {

	private int fragmentPosition;
	private List<Coupon> notUsedCoupons = new ArrayList<Coupon>();
	private List<Coupon> usedOrExpiredCoupons = new ArrayList<Coupon>();

	// public static Fragment newInstance(int position, List<Coupon> list) {
	// MyCouponFragment fragment = new MyCouponFragment();
	// Bundle bundle = new Bundle();
	// bundle.putInt("position", position);
	// bundle.putParcelableArrayList("list", list);
	// fragment.setArguments(bundle);
	// return fragment;
	// }

	public MyCouponFragment(int position, List<Coupon> list) {
		super();
		this.fragmentPosition = position;
		for (Coupon coupon : list) {
			if (UseStatus.NOT_USED.text.equals(coupon.displayStatus)) {
				notUsedCoupons.add(coupon);
			} else {
				usedOrExpiredCoupons.add(coupon);
			}
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		ListView listView = new ListView(getActivity());
		if (fragmentPosition == 0) {
			listView.setAdapter(new MyCouponAdapter(getActivity(), notUsedCoupons, false));
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(getActivity(),CouponQRCodeActivity.class);
					intent.putExtra("picUrl",notUsedCoupons.get(position).campaign.pictureUrls.get(0));
					intent.putExtra("name", notUsedCoupons.get(position).campaign.name);
					intent.putExtra("amount", notUsedCoupons.get(position).campaign.amount);
					String time = time2String(notUsedCoupons.get(position).campaign.redeemTimeStart, notUsedCoupons.get(position).campaign.redeemTimeEnd);
					intent.putExtra("time", time);
					intent.putExtra("id", notUsedCoupons.get(position).id);
					startActivity(intent);
				}
			});
		} else if (fragmentPosition == 1) {
			listView.setAdapter(new MyCouponAdapter(getActivity(), usedOrExpiredCoupons, false));
		}
		
		listView.setPadding(GraphicUtils.dip2px(getActivity(), 15), 0, GraphicUtils.dip2px(getActivity(), 15), 0);

		return listView;
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