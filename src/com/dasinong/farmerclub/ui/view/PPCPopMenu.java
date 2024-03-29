package com.dasinong.farmerclub.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.utils.GraphicUtils;

public class PPCPopMenu {
	private ArrayList<String> itemList;
	private Context context;
	private PopupWindow popupWindow;
	private ListView listView;
	private PopAdapter adapter;
	private View parent;

	public PPCPopMenu(Context context, View parent ,int width) {
		this.context = context;
		this.parent = parent;
		View view = LayoutInflater.from(context).inflate(R.layout.ppc_popmenu, null);
		itemList = new ArrayList<String>();
		// 设置 listview
		listView = (ListView) view.findViewById(R.id.listView);

		adapter = new PopAdapter();

		listView.setAdapter(adapter);

		popupWindow = new PopupWindow(view,  width, LayoutParams.WRAP_CONTENT);
		// popupWindow.setBackgroundDrawable(new BitmapDrawable());

		popupWindow.setBackgroundDrawable(DsnApplication.getContext().getResources().getDrawable(R.drawable.popup_window_bg));
	}

	// 设置菜单项点击监听器
	public void setOnItemClickListener(android.widget.AdapterView.OnItemClickListener listener) {
		listView.setOnItemClickListener(listener);
	}

	public void addItems(List<String> list) {
		if (itemList != null) {
			itemList.clear();
		}
		itemList.addAll(list);
		adapter.notifyDataSetChanged();
		if(list.size() > 5){
			popupWindow.setHeight(GraphicUtils.dip2px(context, 215));
		}
	}

	// 单个添加菜单项
	public void addItem(String item) {
		itemList.add(item);
	}

	public Object getItem(int position) {
		return itemList.get(position);
	}

	// 下拉式 弹出 pop菜单 parent
	public void showAsDropDown() {

		// 保证尺寸是根据屏幕像素密度来的
		popupWindow.showAsDropDown(parent, 0, 0);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}

	// 隐藏菜单
	public void dismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	// 适配器
	private final class PopAdapter extends BaseAdapter {

		public int getCount() {
			return itemList.size();
		}

		public Object getItem(int position) {
			return itemList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			convertView = LayoutInflater.from(context).inflate(R.layout.textview, parent, false);
			TextView groupItem = (TextView) convertView.findViewById(R.id.textview);
			groupItem.setText(itemList.get(position));
			return convertView;
		}
	}
}
