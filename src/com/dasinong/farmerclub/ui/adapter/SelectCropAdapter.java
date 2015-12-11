package com.dasinong.farmerclub.ui.adapter;

import java.util.List;

import com.dasinong.farmerclub.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SelectCropAdapter extends MyBaseAdapter<String> {

	public SelectCropAdapter(Context ctx, List<String> list, boolean flag) {
		super(ctx, list, flag);
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.checkbox_crop, null);
			holder.checkBox = (CheckBox) view.findViewById(R.id.cb_crop);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.checkBox.setText(list.get(pos));
		return view;
	}

	public static class ViewHolder {
		CheckBox checkBox;
	}

}
