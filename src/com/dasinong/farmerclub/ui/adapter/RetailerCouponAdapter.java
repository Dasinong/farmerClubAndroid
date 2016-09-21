package com.dasinong.farmerclub.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.AllCouponEntity.CouponCampaign;
import com.dasinong.farmerclub.net.NetConfig;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RetailerCouponAdapter extends MyBaseAdapter<CouponCampaign> {

	public RetailerCouponAdapter(Context ctx, List<CouponCampaign> list, boolean flag) {
		super(ctx, list, flag);
	}
	
	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder viewHolder;
		if(view == null){
			viewHolder = new ViewHolder();
			view = View.inflate(context, R.layout.item_my_coupon, null);
			viewHolder.iv = (ImageView) view.findViewById(R.id.iv_pic);
			viewHolder.title = (TextView) view.findViewById(R.id.tv_title);
			viewHolder.time = (TextView) view.findViewById(R.id.tv_time);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.display(viewHolder.iv, NetConfig.COUPON_IMAGE + list.get(pos).pictureUrls.get(0));
		
		viewHolder.title.setText(list.get(pos).name);
		String time = time2String(list.get(pos).redeemTimeStart,list.get(pos).redeemTimeEnd);
		if(list.get(pos).id == 15){
			viewHolder.time.setVisibility(View.GONE);
		} else {
			viewHolder.time.setVisibility(View.VISIBLE);
			viewHolder.time.setText("使用时间：" + time);
		}
		return view;
	}
	
	public final static class ViewHolder{
		ImageView iv;
		TextView title;
		TextView time;
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
