package com.dasinong.farmerclub.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.components.home.view.HomeTopView;
import com.dasinong.farmerclub.ui.view.TopbarView;

public class NotFarmWorkActivity extends BaseActivity {
	private LinearLayout ll_farm_work;
	private ScrollView sl_pesticide;
	private TopbarView topbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_not_farm_work);
		
		String type = getIntent().getStringExtra("type");
		ll_farm_work = (LinearLayout) findViewById(R.id.ll_farm_work);
		sl_pesticide = (ScrollView) findViewById(R.id.sl_pesticide);
		topbar = (TopbarView) findViewById(R.id.topbar);
		
		if(HomeTopView.FARMWORK.equals(type)){
			ll_farm_work.setVisibility(View.VISIBLE);
			sl_pesticide.setVisibility(View.GONE);
			initTopBar("不适合下地");
		} else {
			ll_farm_work.setVisibility(View.GONE);
			sl_pesticide.setVisibility(View.VISIBLE);
			initTopBar("不适合打药");
		}
	}

	private void initTopBar(String title) {
		topbar.setCenterText(title);
		topbar.setLeftView(true, true);
	}
}
