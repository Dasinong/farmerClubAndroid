package com.dasinong.farmerclub.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.AllCouponEntity.CouponCampaign;
import com.dasinong.farmerclub.net.NetConfig;
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
	public View getView(final int pos, View view, ViewGroup group) {
		ViewHolder viewHolder;
		if (view == null) {
			view = View.inflate(context, R.layout.item_coupon, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
			viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.display(viewHolder.iv_pic, NetConfig.COUPON_IMAGE + list.get(pos).pictureUrls.get(0));
		
		String claimTime = time2String(list.get(pos).claimTimeStart, list.get(pos).claimTimeEnd);
		viewHolder.tv_title.setText(list.get(pos).name);
		viewHolder.tv_time.setText("申领时间：" + claimTime);

		return view;
	}

	public static class ViewHolder {
		public ImageView iv_pic;
		public TextView tv_title;
		public TextView tv_time;
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
