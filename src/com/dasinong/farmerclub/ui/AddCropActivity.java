package com.dasinong.farmerclub.ui;

import java.util.ArrayList;
import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.SubscriptionCropEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.adapter.AddCropAdapter;
import com.dasinong.farmerclub.ui.adapter.SelectCropAdapter;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.GraphicUtils;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AddCropActivity extends BaseActivity implements OnClickListener {

	private ListView lv_crop;
	private Button btn_finish;
	private List<String> cropList = new ArrayList<String>();
	private List<String> cropIdList = new ArrayList<String>();

	public static final String CROP_NAME = "cropName";
	public static final String CROP_ID = "cropId";
	private AddCropAdapter adapter;
	private TopbarView topbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_crop);

		initView();

		setData();

	}

	private void setData() {
		adapter = new AddCropAdapter(cropList, this);
		setListViewHeightBasedOnChildren(lv_crop);
		lv_crop.setAdapter(adapter);

		lv_crop.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if (position == cropList.size()) {
					Intent intent = new Intent(AddCropActivity.this, AllVarietyActivity.class);
					startActivityForResult(intent, 0);
				}
			}
		});
	}

	private void initView() {
		lv_crop = (ListView) findViewById(R.id.lv_crop);
		btn_finish = (Button) findViewById(R.id.btn_finish);
		topbar = (TopbarView) findViewById(R.id.topbar);

		initTopbar();

		btn_finish.setOnClickListener(this);
	}

	private void initTopbar() {
		topbar.setCenterText("请选择作物");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_finish:
			List<String> ids = new ArrayList<String>();
			for (int i = 0; i < lv_crop.getChildCount()-1; i++) {

				LinearLayout root = (LinearLayout) lv_crop.getChildAt(i);
				CheckBox checkBox = (CheckBox) root.getChildAt(0);
				if (checkBox.isChecked()) {
					ids.add(cropIdList.get(i));
				}
			}
			setSubscribeCrop(ids);
			break;
		}
	}

	private void setSubscribeCrop(List<String> list) {
		if (list == null || list.isEmpty()) {
			showToast("请至少选择一个关注作物");
			return;
		}
		MobclickAgent.onEvent(this, "FinishSelectCrop");
		startLoadingDialog();
		RequestService.getInstance().cropSubscriptions(this, list, SubscriptionCropEntity.class, new RequestListener() {
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					SubscriptionCropEntity entity = (SubscriptionCropEntity) resultData;
					showToast("关注成功");

					Intent intent = new Intent(AddCropActivity.this, MainTabActivity.class);
					startActivity(intent);
					
					finish();

				} else {
					showToast(R.string.please_check_netword);
				}
				dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				showToast(R.string.please_check_netword);
				dismissLoadingDialog();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(intent != null){
			String cropName = intent.getStringExtra(CROP_NAME);
			String cropId = intent.getStringExtra(CROP_ID);
			if (!cropList.contains(cropName)) {
				cropList.add(0, cropName);
				cropIdList.add(cropId);
				adapter.notifyDataSetChanged();
			} else {
				showToast("您已添加过该作物");
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 根据内容决定gridView的高度
	 * 
	 * @param gridView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
