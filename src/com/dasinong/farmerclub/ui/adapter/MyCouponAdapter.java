package com.dasinong.farmerclub.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.MyCouponsEntity.Coupon;
import com.dasinong.farmerclub.net.NetConfig;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class MyCouponAdapter extends MyBaseAdapter<Coupon> {

	public MyCouponAdapter(Context ctx, List<Coupon> list, boolean flag) {
		super(ctx, list, flag);
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder viewHolder;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = View.inflate(context, R.layout.item_my_coupon, null);
			viewHolder.iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
			viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.display(viewHolder.iv_pic, NetConfig.COUPON_IMAGE + list.get(pos).campaign.pictureUrls.get(0));

		viewHolder.tv_title.setText(list.get(pos).campaign.name);

		String redeemTime = time2String(list.get(pos).campaign.redeemTimeStart, list.get(pos).campaign.redeemTimeEnd);

		viewHolder.tv_time.setText("使用时间" + redeemTime);
		return view;
	}

	public static class ViewHolder {
		ImageView iv_pic;
		TextView tv_title;
		TextView tv_time;
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
