package com.dasinong.farmerclub.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.database.encyclopedias.domain.Cpproductbrowse;
import com.dasinong.farmerclub.database.encyclopedias.domain.Petdisspecbrowse;
import com.dasinong.farmerclub.entity.CPProductEntity;
import com.dasinong.farmerclub.entity.CPProductEntity.CPProduct;
import com.dasinong.farmerclub.entity.DiseaseEntity;
import com.dasinong.farmerclub.ui.adapter.DiseaseListAdapter.ViewHolder;

public class BasfPesticideListAdapter extends MyBaseAdapter<CPProduct> {
	
	public BasfPesticideListAdapter(Context ctx, List<CPProduct> list, boolean flag) {
		super(ctx, list, flag);
	}
	
	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if(view == null){
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.view_disease_item_small, null);
			holder.nameText = (TextView) view.findViewById(R.id.textview_title);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final CPProduct item = list.get(pos);
		holder.nameText.setText(item.name);
		return view;
	}

	public static class ViewHolder {
		TextView  nameText;
	}
}
