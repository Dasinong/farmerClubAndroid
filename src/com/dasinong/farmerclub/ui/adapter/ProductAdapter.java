package com.dasinong.farmerclub.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.ProductListEntity.Product;

public class ProductAdapter extends MyBaseAdapter<Product> {

	public ProductAdapter(Context ctx, List<Product> list, boolean flag) {
		super(ctx, list, flag);
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if(view == null){
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.item_product, null);
			holder.tv = (TextView) view.findViewById(R.id.tv_name);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.tv.setText(list.get(pos).name);
		return view;
	}
	
	public static class ViewHolder {
		TextView tv ;
	}

}
