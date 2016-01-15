package com.dasinong.farmerclub.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.AllCouponEntity.CouponCampaign;
import com.dasinong.farmerclub.ui.ApplyCouponActivity;
import com.dasinong.farmerclub.ui.CouponDetailActivity;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CouponAdapter extends MyBaseAdapter<CouponCampaign> {

	public CouponAdapter(Context ctx, List<CouponCampaign> list, boolean flag) {
		super(ctx, list, flag);
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder viewHolder;
		if (view == null) {
			view = View.inflate(context, R.layout.item_coupon, null);
			viewHolder = new ViewHolder();
			viewHolder.ll_summary = (LinearLayout) view.findViewById(R.id.ll_summary);
			viewHolder.iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
			viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			viewHolder.tv_money = (TextView) view.findViewById(R.id.tv_money);
			viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			viewHolder.btn_apply = (Button) view.findViewById(R.id.btn_apply);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.display(viewHolder.iv_pic, "");

		viewHolder.tv_title.setText(list.get(pos).name);
		viewHolder.tv_money.setText("¥" + list.get(pos).amount + ".00");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		Date date = new Date();
		
		date.setTime(list.get(pos).claimTimeStart);
		String startTime = sdf.format(date).toString();
		
		date.setTime(list.get(pos).claimTimeEnd);
		String endTime = sdf.format(date).toString();
		


		viewHolder.tv_time.setText("申领时间：" + startTime + "-" + endTime);

		viewHolder.ll_summary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,CouponDetailActivity.class);
				context.startActivity(intent);
			}
		});

		viewHolder.btn_apply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,ApplyCouponActivity.class);
				context.startActivity(intent);
			}
		});

		return view;
	}

	public static class ViewHolder {
		public LinearLayout ll_summary;
		public ImageView iv_pic;
		public TextView tv_title;
		public TextView tv_money;
		public TextView tv_time;
		public Button btn_apply;
	}

}
