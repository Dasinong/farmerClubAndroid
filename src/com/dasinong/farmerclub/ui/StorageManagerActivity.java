package com.dasinong.farmerclub.ui;

import java.util.ArrayList;
import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.ProductListEntity;
import com.dasinong.farmerclub.entity.ProductListEntity.Product;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.adapter.ProductAdapter;
import com.dasinong.farmerclub.ui.view.TopbarView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class StorageManagerActivity extends BaseActivity {
	private TopbarView topBar;
	private ListView lv_product;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storage_manager);
		
		initView();
		
		qureyData();
		
		initTopBar();
	}

	private void qureyData() {
		
//		ProductListEntity entity = new ProductListEntity();
//		entity.data = new Data();
//		entity.data.productList = new ArrayList<>();
//		for (int i = 0; i < 3; i++) {
//			Product p = entity.new Product();
//			p.id = i;
//			p.name = "百润" + i;
//			p.volume = "123";
//			p.count = 5;
//			entity.data.productList.add(p);
//		}
//		setData(entity.data.productList);
		
		RequestService.getInstance().checkBSFStock(this, ProductListEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if(resultData.isOk()){
					ProductListEntity entity = (ProductListEntity) resultData;
					if(entity.data != null ){
						setData(entity.data);
					}
				}
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				error.printStackTrace();
			}
		});
	}

	protected void setData(final List<Product> list) {
		lv_product.setAdapter(new ProductAdapter(this, list, false));
		
		lv_product.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(StorageManagerActivity.this, StorageDetailActivity.class);
				list.get(position);
				intent.putExtra("id", list.get(position).id);
				intent.putExtra("name", list.get(position).name);
				intent.putExtra("volume", list.get(position).volume);
				intent.putExtra("count", list.get(position).count);
				startActivity(intent);
			}
		});
	}

	private void initTopBar() {
		topBar.setCenterText("入库管理");
		topBar.setLeftView(true, true);
	}

	private void initView() {
		topBar = (TopbarView) findViewById(R.id.topbar);
		lv_product = (ListView) findViewById(R.id.lv_product);
	}
}
