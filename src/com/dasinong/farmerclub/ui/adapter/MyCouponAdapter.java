package com.dasinong.farmerclub.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.MyCouponsEntity.Coupon;
import com.dasinong.farmerclub.entity.MyCouponsEntity.UseStatus;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class MyCouponAdapter extends MyBaseAdapter<Coupon> {

	private static final int DAY_MS = 24 * 60 * 60 * 1000;

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

		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		Date date = new Date();
		if (UseStatus.USED.equals(list.get(pos).displayStatus)) {
			date.setTime(list.get(pos).redeemedAt);
			String usedDate = sdf.format(date).toString();
			viewHolder.tv_time.setText("扫描日期：" + usedDate);
		} else if (UseStatus.EXPIRED.equals(list.get(pos).displayStatus)) {
			date.setTime(list.get(pos).claimedAt + 31 * 24 * 3600 * 1000);
			String overdueData = sdf.format(date).toString();
			viewHolder.tv_time.setText("过期日期：" + overdueData);
		} else {
			boolean isDaren = SharedPreferencesHelper.getBoolean(context, Field.ISDAREN, false);
			String time = "";
			if (isDaren) {
				time = time2String(list.get(pos).claimedAt + 10 * DAY_MS , list.get(pos).claimedAt + 30 * DAY_MS);
			} else {
				time = time2String(list.get(pos).claimedAt, list.get(pos).claimedAt + 30 * DAY_MS);
			}
			if(list.get(pos).campaign.id == 15){
				viewHolder.tv_time.setVisibility(View.GONE);
			} else {
				viewHolder.tv_time.setVisibility(View.VISIBLE);
				viewHolder.tv_time.setText("兑换时间：" + time);
			}
		}
		return view;
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

	public static class ViewHolder {
		ImageView iv_pic;
		TextView tv_title;
		TextView tv_time;
	}
}
