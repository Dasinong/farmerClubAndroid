package com.dasinong.farmerclub.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.database.disaster.domain.NatDisspec;
import com.dasinong.farmerclub.database.disaster.domain.PetDisspec;
import com.dasinong.farmerclub.net.NetConfig;
import com.lidroid.xutils.BitmapUtils;

public class HarmAdapter<T> extends MyBaseAdapter<T> {

	private BitmapUtils bitmapUtils;

	public HarmAdapter(Context ctx, List<T> list, boolean flag) {
		super(ctx, list, flag);
		bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.harm_list_item, null);
			holder.iv_harm_pic = (ImageView) view.findViewById(R.id.iv_harm_pic);
			holder.tv_harm_name = (TextView) view.findViewById(R.id.tv_harm_name);
			holder.tv_harm_des = (TextView) view.findViewById(R.id.tv_harm_des);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		PetDisspec pet = (PetDisspec) list.get(pos);
		String path = null;
		if(pet.pictureIds.contains("\n")){
			path = pet.pictureIds.substring(0, pet.pictureIds.indexOf("\n"));
		} else {
			path = pet.pictureIds;
		}
		// TODO Ming 设置合适的默认图片
//		LoadUtils.getInstance().loadImage(holder.iv_harm_pic, NetConfig.PET_IMAGE+path,R.drawable.test_pic);
		
		bitmapUtils.display(holder.iv_harm_pic, NetConfig.PET_IMAGE+path);
		
		holder.tv_harm_name.setText(pet.petDisSpecName);
		holder.tv_harm_des.setText(pet.sympthon.replace("[为害症状]", ""));

		return view;
	}

	public static class ViewHolder {
		ImageView iv_harm_pic;
		TextView tv_harm_name;
		TextView tv_harm_des;
	}
}
