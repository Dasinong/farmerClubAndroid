package com.dasinong.farmerclub.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.view.TopbarView;

import android.os.Bundle;
import android.widget.TextView;

public class StorageDetailActivity extends BaseActivity {
	private int id;
	private String name;
	private String volume;
	private int count;
	private TopbarView topBar;
	private TextView tv_name;
	private TextView tv_volume;
	private TextView tv_count;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storage_detail);

		id = getIntent().getIntExtra("id", 0);
		name = getIntent().getStringExtra("name");
		volume = getIntent().getStringExtra("volume");
		count = getIntent().getIntExtra("count", 0);

		initView();

		setData();
	}

	private void setData() {
		tv_name.setText(name);
		tv_volume.setText(volume);
		tv_count.setText(getCount());
	}

	private String getCount() {
		String spName = new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis())) + id;
		int currentCount = SharedPreferencesHelper.getInt(this, spName, 0);
		
		System.out.println("currentCount          " + currentCount);
		
		System.out.println("count    " + count);
		
		
		return (currentCount + count) + "";
	}

	private void initView() {
		topBar = (TopbarView) findViewById(R.id.topbar);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_volume = (TextView) findViewById(R.id.tv_volume);
		tv_count = (TextView) findViewById(R.id.tv_count);
	}
}
