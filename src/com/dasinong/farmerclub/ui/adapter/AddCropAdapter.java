package com.dasinong.farmerclub.ui.adapter;

import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.database.encyclopedias.domain.Varietybrowse;
import com.dasinong.farmerclub.ui.adapter.VarietyListAdapter.ViewHolder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class AddCropAdapter extends BaseAdapter {

	private List<String> list;
	private Context context;

	public AddCropAdapter(List<String> list, Context context) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		if (position < list.size()) {
			return list.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		if (position < list.size()) {
			return position;
		} else {
			return position + 1;
		}
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.checkbox_crop, null);
			holder.linearlayout = view.findViewById(R.id.ll_add_crop);
			holder.checkBox = (CheckBox) view.findViewById(R.id.cb_crop);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (pos < list.size()) {
			final String item = list.get(pos);
			holder.linearlayout.setVisibility(View.GONE);
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.checkBox.setText(item);
			holder.checkBox.setChecked(true);
		} else {
			holder.linearlayout.setVisibility(View.VISIBLE);
			holder.checkBox.setVisibility(View.GONE);
		}
		return view;
	}

	public static class ViewHolder {
		View linearlayout;
		CheckBox checkBox;
	}
}
