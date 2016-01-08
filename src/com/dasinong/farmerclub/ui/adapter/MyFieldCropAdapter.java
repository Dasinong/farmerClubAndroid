package com.dasinong.farmerclub.ui.adapter;

import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.CropSubscriptionsEntity.Subscriptions;
import com.dasinong.farmerclub.net.NetConfig;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MyFieldCropAdapter extends MyBaseAdapter<Subscriptions> {

	private boolean visible = false;
	private OnDelClickListener mOnDelClickListener;

	public MyFieldCropAdapter(Context ctx, List<Subscriptions> list, boolean flag) {
		super(ctx, list, flag);
	}

	public void setDelVisible(Boolean visible) {
		this.visible = visible;
	}

	public boolean getBtnVisible() {
		return this.visible;
	}

	public void setData(List<Subscriptions> list) {
		this.list = list;
	}

	public void setOnDelClickListener(OnDelClickListener onDelClickListener) {
		this.mOnDelClickListener = onDelClickListener;
	}

	@Override
	public View getView(final int pos, View view, ViewGroup group) {
		ViewHolder viewHolder;
		if (view == null) {
			view = View.inflate(context, R.layout.item_my_crop, null);
			viewHolder = new ViewHolder();
			viewHolder.cropPic = (ImageView) view.findViewById(R.id.iv_crop_pic);
			viewHolder.cropName = (TextView) view.findViewById(R.id.tv_crop_name);
			viewHolder.fieldCount = (TextView) view.findViewById(R.id.tv_field_count);
			viewHolder.del = (ImageButton) view.findViewById(R.id.btn_del);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		if (visible) {
			viewHolder.del.setVisibility(View.VISIBLE);
		} else {
			viewHolder.del.setVisibility(View.GONE);
		}

		viewHolder.del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnDelClickListener.onClick(pos);
			}
		});
		viewHolder.cropName.setText(list.get(pos).crop.cropName);
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.display(viewHolder.cropPic, NetConfig.PET_IMAGE + list.get(pos).crop.iconUrl.replace("/pic/", ""));
		if (list.get(pos).fields != null && list.get(pos).fields.size() > 0) {
			viewHolder.fieldCount.setText(list.get(pos).fields.size() + "块田");
		} else {
			viewHolder.fieldCount.setText("未种植");
		}

		return view;
	}

	public interface OnDelClickListener {
		public void onClick(int position);
	}

	public static class ViewHolder {
		ImageView cropPic;
		TextView cropName;
		TextView fieldCount;
		ImageButton del;
	}
}
