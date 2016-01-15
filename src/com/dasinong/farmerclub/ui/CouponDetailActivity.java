package com.dasinong.farmerclub.ui;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.GraphicUtils;

public class CouponDetailActivity extends BaseActivity {
	private LinearLayout ll_pics;
	private LinearLayout ll_exchange_place;
	private TopbarView topBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon_detail);

		ll_pics = (LinearLayout) findViewById(R.id.ll_pics);
		ll_exchange_place = (LinearLayout) findViewById(R.id.ll_exchange_place);
		topBar = (TopbarView) findViewById(R.id.topbar);

		initView();
		initTopBar();
	}

	private void initTopBar() {
		topBar.setCenterText("大户俱乐部福利");
		topBar.setLeftView(true, true);
		
	}

	private void initView() {
		for (int i = 0; i < 3; i++) {
			ImageView imageView = new ImageView(this);

			LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, GraphicUtils.dip2px(this, 200));
			params.setMargins(0, 0, 0, GraphicUtils.dip2px(this, 5));
			imageView.setImageResource(R.drawable.small);
			imageView.setLayoutParams(params);
			ll_pics.addView(imageView);
		}
		
		for (int i = 0; i < 4; i++) {
			TextView tv = new TextView(this);
			LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, GraphicUtils.dip2px(this, 32.5f));
			tv.setText("哈尔滨");
			tv.setPadding(GraphicUtils.dip2px(this, 15), 0, 0, 0);
			tv.setTextColor(getResources().getColor(R.color.color_666666));
			tv.setBackgroundResource(R.color.color_F5F5F5);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			
			tv.setLayoutParams(params);
			ll_exchange_place.addView(tv);
			for (int j = 0; j < 3; j++) {
				View view = View.inflate(this, R.layout.item_exchange_place, null);
				LinearLayout.LayoutParams dividerParams = new LayoutParams(LayoutParams.MATCH_PARENT,GraphicUtils.dip2px(this, 0.5f));
				View divider = new View(this);
				divider.setBackgroundResource(R.color.color_DBE3E5);
				divider.setLayoutParams(dividerParams);
				view.setPadding(0, GraphicUtils.dip2px(this, 15), 0, GraphicUtils.dip2px(this, 15));
				ll_exchange_place.addView(view);
				ll_exchange_place.addView(divider);
			}
		}
	}

}
