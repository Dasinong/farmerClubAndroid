package com.dasinong.farmerclub.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.database.encyclopedias.PetdisspecbrowseDao;
import com.dasinong.farmerclub.database.encyclopedias.VarietybrowseDao;
import com.dasinong.farmerclub.database.encyclopedias.domain.Petdisspecbrowse;
import com.dasinong.farmerclub.database.encyclopedias.domain.Varietybrowse;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.PesticideListEntity;
import com.dasinong.farmerclub.entity.VarietyNamedListEntity;
import com.dasinong.farmerclub.entity.PesticideNamedListEntity.Pesticide;
import com.dasinong.farmerclub.entity.VarietyNamedListEntity.Variety;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.adapter.DiseaseListAdapter;
import com.dasinong.farmerclub.ui.adapter.PesticideListAdapter;
import com.dasinong.farmerclub.ui.adapter.VarietyListAdapter;
import com.dasinong.farmerclub.ui.adapter.VarietyNamedListAdapter;
import com.dasinong.farmerclub.ui.view.LetterView;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.ui.view.LetterView.OnTouchingLetterChangedListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchVarietyNamedResultActivity extends BaseActivity {

	private TopbarView mTopbarView;
	
	private String type;

	private ListView mListView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disease_list);
		
		type = getIntent().getStringExtra("type");
		initView();
		setUpView();
		requestData();
		
	}


	protected void setAdapter(List<Variety> query) {
		
		if(query!=null && query.size()==1){
			Variety item = query.get(0);
			Intent intent = new Intent(SearchVarietyNamedResultActivity.this, VarietyDetailActivity.class);
			intent.putExtra("id", item.getId());
			intent.putExtra("title", item.getVarietyName());
			startActivity(intent);
			finish();
			return;
		}
		
		VarietyNamedListAdapter adapter = new VarietyNamedListAdapter(this, query, false);
		mListView.setAdapter(adapter);
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mListView = (ListView) this.findViewById(R.id.list_sms_list);
		
		findViewById(R.id.letterview).setVisibility(View.GONE);
	}

	private void setUpView() {
		mTopbarView.setCenterText(type);
		mTopbarView.setLeftView(true, true);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Variety item = (Variety) parent.getItemAtPosition(position);
				Intent intent = new Intent(SearchVarietyNamedResultActivity.this, VarietyDetailActivity.class);
				intent.putExtra("id", item.getId());
				intent.putExtra("title", item.getVarietyName());
				startActivity(intent);
			}
		});
	}

	private void requestData() {
		startLoadingDialog();
		RequestService.getInstance().getVarietysByName(this, type, VarietyNamedListEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if(resultData.isOk()){
					VarietyNamedListEntity entity = (VarietyNamedListEntity) resultData;
					setAdapter(entity.getData());
				}else{
					showToast(resultData.getMessage());
				}
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				showToast(R.string.please_check_netword);
				dismissLoadingDialog();
				
			}
		});
	}
	
	
}
