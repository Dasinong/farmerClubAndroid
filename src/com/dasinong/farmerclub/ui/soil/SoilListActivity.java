package com.dasinong.farmerclub.ui.soil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.ui.WebBrowserActivity;

public class SoilListActivity extends SoilBaseActivity implements AdapterView.OnItemClickListener {

	private ListView mListView;

	@Override
	protected int getMainResourceId() {
		return R.layout.activity_soil_list;
	}

	@Override
	protected void initView() {
		mListView = (ListView) findViewById(R.id.list_view);
		mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.soil_list_item, R.id.soil_item_text, getResources().getStringArray(
				R.array.soil_list)));

	}

	@Override
	protected void initEvent() {
		mListView.setOnItemClickListener(this);
	}

	@Override
	public int getTitleText() {
		return R.string.soil_check;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		switch (position) {
		case 0:
			intent.setClass(this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.URL, "file:///android_asset/SamplingImportance.html");
			intent.putExtra(WebBrowserActivity.TITLE, "为什么要测土");
			startActivity(intent);
			break;
		case 1:
			intent.setClass(this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.URL, "file:///android_asset/SamplingNotice.html");
			intent.putExtra(WebBrowserActivity.TITLE, "采样须知");
			startActivity(intent);
			break;
		case 2:

			intent.setClass(this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.URL, "file:///android_asset/soiltest-sample.html");
			intent.putExtra(WebBrowserActivity.TITLE, "测土报告解读");
			startActivity(intent);
			break;
		}
	}

	

	@Override
	public void onTaskSuccess(int requestCode, Object response) {

	}
}
