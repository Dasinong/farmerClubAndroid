package com.dasinong.farmerclub.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.MyCouponsEntity.Coupon;
import com.dasinong.farmerclub.entity.MyCouponsEntity.UseStatus;
import com.dasinong.farmerclub.net.NetConfig;
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
			viewHolder.tv_time.setText("使用日期：" + usedDate);
		} else if (UseStatus.EXPIRED.equals(list.get(pos).displayStatus)) {
			date.setTime(list.get(pos).claimedAt + 31 * 24 * 3600 * 1000);
			String overdueData = sdf.format(date).toString();
			viewHolder.tv_time.setText("过期日期：" + overdueData);
		} else {
			int day = (int) (31 - (System.currentTimeMillis() - list.get(pos).claimedAt) / (1000 * 24 * 3600));
			viewHolder.tv_time.setText("剩余兑换时间：" + day + " 天");
		}
		return view;
	}

	public static class ViewHolder {
		ImageView iv_pic;
		TextView tv_title;
		TextView tv_time;
	}
}
