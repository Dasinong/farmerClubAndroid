package com.dasinong.farmerclub.ui.adapter;

import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.SearchItem;
import com.dasinong.farmerclub.entity.SmsSubscribeItem;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.BaseActivity;
import com.dasinong.farmerclub.ui.MyInfoActivity;
import com.dasinong.farmerclub.ui.manager.AccountManager;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchResultAdapter extends MyBaseAdapter<SearchItem> {

	private boolean isDelete = false;
	
	public SearchResultAdapter(Context ctx, List<SearchItem> list, boolean flag) {
		super(ctx, list, flag);
	}
	
	public void setDeleteState(boolean state){
		this.isDelete = state;
	}
	
	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if(view == null){
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.view_search_result_item, null);
			holder.nameText = (TextView) view.findViewById(R.id.textview_title);
			holder.desText = (TextView) view.findViewById(R.id.textview_description);
			holder.typeText = (TextView) view.findViewById(R.id.textview_title_type);
			holder.line1View =  view.findViewById(R.id.view_line1);
			holder.contentLayout =  view.findViewById(R.id.layout_content);
			holder.typeLayout =  view.findViewById(R.id.layout_type);
			holder.typeImage = (ImageView) view.findViewById(R.id.imageview_type_image);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final SearchItem item = list.get(pos);
		
		if(item.isType()){
			holder.contentLayout.setVisibility(View.GONE);
			holder.line1View.setVisibility(View.VISIBLE);
			holder.typeLayout.setVisibility(View.VISIBLE);
			holder.typeText.setText(item.getName());
			holder.typeImage.setImageResource(item.getResId());
		}else{
			if(!TextUtils.isEmpty(item.getName())){
				holder.nameText.setText(Html.fromHtml(item.getName()));
				holder.nameText.setVisibility(View.VISIBLE);
			}else{
				holder.nameText.setVisibility(View.GONE);
			}
			if(!TextUtils.isEmpty(item.getSource())){
				holder.desText.setText(Html.fromHtml(item.getSource()));
				holder.desText.setVisibility(View.VISIBLE);
			}else{
				holder.desText.setVisibility(View.GONE);
			}
			
			holder.contentLayout.setVisibility(View.VISIBLE);
			holder.line1View.setVisibility(View.GONE);
			holder.typeLayout.setVisibility(View.GONE);
		}
		
		return view;
	}
	

	public static class ViewHolder {
		TextView  nameText;
		TextView  desText;
		View contentLayout;
		
		View line1View;
		View typeLayout;
		TextView typeText;
		ImageView typeImage;
	}
	
}
