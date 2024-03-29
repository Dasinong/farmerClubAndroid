package com.dasinong.farmerclub.ui.adapter;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.database.encyclopedias.domain.Petdisspecbrowse;
import com.dasinong.farmerclub.entity.DiseaseEntity;

public class VarietiesFirstListAdapter extends MyBaseAdapter<String> {
	
	private static final String[] strs = {"粮食作物","经济作物","蔬菜","水果","菌类","坚果类","花卉","草料","药材"};
	private static final int[] resIds = {R.drawable.icon_liangshizuowu,R.drawable.icon_jingjizuowu,R.drawable.icon_shucai,
			R.drawable.icon_shuiguo,R.drawable.icon_junlei,R.drawable.icon_jianguo,
			R.drawable.icon_huahui,R.drawable.icon_caoliao,R.drawable.icon_yaocai};
	
	private int selectPosition = 0;
	
	public VarietiesFirstListAdapter(Context ctx, List<String> list, boolean flag) {
		super(ctx, Arrays.asList(strs), flag);
	}
	
	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if(view == null){
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.view_varieties_first_list_item, null);
			holder.deleteImage = (ImageView) view.findViewById(R.id.imageview_icon);
			holder.nameText = (TextView) view.findViewById(R.id.textview_title);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final String item = list.get(pos);
		holder.nameText.setText(item);
		holder.deleteImage.setImageResource(resIds[pos]);
		
		if(selectPosition == pos){
			view.setActivated(true);
		}else{
			view.setActivated(false);
		}
		
		return view;
	}
	
	public void setSelectPosition(int position){
		this.selectPosition  = position;
		notifyDataSetChanged();
	}

	public static class ViewHolder {
		ImageView  deleteImage;
		TextView  nameText;
	}
	
}
