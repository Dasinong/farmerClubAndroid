package com.dasinong.farmerclub.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dasinong.farmerclub.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class SelectCropAdapter extends MyBaseAdapter<String> {
	
	private static HashMap<Integer, Boolean> isSelectedMap;

	public SelectCropAdapter(Context ctx, List<String> list, boolean flag) {
		super(ctx, list, flag);
		isSelectedMap = new HashMap<>();
		
		initDate();
	}
	
	private void initDate() {
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	@Override
	public View getView(final int pos, View view, ViewGroup group) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.checkbox_crop, null);
			holder.checkBox = (CheckBox) view.findViewById(R.id.cb_crop);
			holder.linearLayout = (LinearLayout) view.findViewById(R.id.ll_add_crop);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.checkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isSelectedMap.get(pos)){
					isSelectedMap.put(pos, false);
					setIsSelected(isSelectedMap);
				} else {
					isSelectedMap.put(pos, true);
					setIsSelected(isSelectedMap);
				}
			}
		});

		holder.checkBox.setText(list.get(pos));
		holder.checkBox.setChecked(getIsSelected().get(pos));
		holder.linearLayout.setVisibility(View.GONE);
		return view;
	}

	public static class ViewHolder {
		CheckBox checkBox;
		LinearLayout linearLayout;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelectedMap;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		SelectCropAdapter.isSelectedMap = isSelected;
	}
}
