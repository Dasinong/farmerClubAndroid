package com.dasinong.farmerclub.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.BasfPesticideListEntity;
import com.dasinong.farmerclub.entity.CPProductEntity.CPProduct;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.adapter.BasfPesticideListAdapter;
import com.dasinong.farmerclub.ui.view.TopbarView;

public class SearchBasfPesticideResultActivity extends BaseActivity {

	private TopbarView mTopbarView;
	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basf_disease_list);

		String type = getIntent().getStringExtra("type");
		String manufacturer = getIntent().getStringExtra("manufacturer");

		initView();
		setUpView(type);
		requestData(type, manufacturer);
	}

	protected void setAdapter(List<CPProduct> query) {
		BasfPesticideListAdapter adapter = new BasfPesticideListAdapter(this, query, false);
		mListView.setAdapter(adapter);
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mListView = (ListView) this.findViewById(R.id.list_sms_list);
	}

	private void setUpView(String type) {
		if ("专业解决方案 有害生物控制".equals(type)) {
			type = "公共卫生";
		}
		mTopbarView.setCenterText(type);
		mTopbarView.setLeftView(true, true);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CPProduct item = (CPProduct) parent.getItemAtPosition(position);
				Intent intent = new Intent(SearchBasfPesticideResultActivity.this, PesticideDetailActivity.class);
				intent.putExtra("id", String.valueOf(item.id));
				startActivity(intent);
			}
		});
	}

	private void requestData(String type, String manufacturer) {
		startLoadingDialog();
		RequestService.getInstance().browseCustomizedCPProduct(this, type, manufacturer, BasfPesticideListEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					BasfPesticideListEntity entity = (BasfPesticideListEntity) resultData;
					setAdapter(entity.data);
				} else {
					showToast(resultData.getMessage());
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();

			}
		});
	}

}
