package com.dasinong.farmerclub.ui.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.PetDisSpecsListEntity.PetDisSpecs;
import com.dasinong.farmerclub.net.NetConfig;
import com.lidroid.xutils.BitmapUtils;

public class DiseaseListAdapter extends MyBaseAdapter<PetDisSpecs> {
	
	private BitmapUtils bitmapUtils;

	public DiseaseListAdapter(Context ctx, List<PetDisSpecs> list, boolean flag) {
		super(ctx, list, flag);
		bitmapUtils = new BitmapUtils(ctx);
	}
	
	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if(view == null){
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.view_disease_item, null);
			holder.nameText = (TextView) view.findViewById(R.id.textview_title);
			holder.desText = (TextView) view.findViewById(R.id.textview_description);
			holder.pic = (ImageView) view.findViewById(R.id.iv_pic);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final PetDisSpecs item = list.get(pos);
		holder.nameText.setText(item.petDisSpecName);
		if(TextUtils.isEmpty(item.sympthon)){
			holder.desText.setVisibility(View.GONE);
		}
		holder.desText.setText(item.sympthon);
		holder.pic.setVisibility(View.VISIBLE);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.big_harm_default);
		bitmapUtils.display(holder.pic, NetConfig.PET_IMAGE+item.thumbnailId);
		return view;
	}

	public static class ViewHolder {
		TextView  nameText;
		TextView  desText;
		ImageView pic; 
	}
	
}
