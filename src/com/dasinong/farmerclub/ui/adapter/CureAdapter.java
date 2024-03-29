package com.dasinong.farmerclub.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.database.disaster.domain.CPProduct;
import com.dasinong.farmerclub.entity.DrugListEntity.Drug;

public class CureAdapter extends MyBaseAdapter<Drug> {

	public CureAdapter(Context ctx, List<Drug> list, boolean flag) {
		super(ctx, list, flag);
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.medicine_item, null);
			holder.tv_medicine_name = (TextView) view.findViewById(R.id.tv_medicine_name);
			holder.tv_medicine_descript = (TextView) view.findViewById(R.id.tv_medicine_descript);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_medicine_name.setText(list.get(pos).name);
		holder.tv_medicine_descript.setText(list.get(pos).activeIngredient);
		return view;
	}

	public static class ViewHolder {
		TextView tv_medicine_name;
		TextView tv_medicine_descript;
	}
}
