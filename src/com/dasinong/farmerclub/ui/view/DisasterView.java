package com.dasinong.farmerclub.ui.view;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.applyDimension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.FieldDetailEntity.PetdisspecwEntity;
import com.dasinong.farmerclub.ui.EncyclopediasDiseaseActivity;
import com.dasinong.farmerclub.ui.HarmDetailsActivity;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.umeng.analytics.MobclickAgent;

/**
 * disaster item view Created by lxn on 15/6/5.
 */
public class DisasterView extends LinearLayout {

	private int mDefaultBottomPadding = 2;

	private LayoutInflater mLayoutInflater;

	private TextView mBottomView;

	private LinearLayout.LayoutParams mBottomLayoutParam;

	private BottomClickListener mBottomClickListener;

	private View mTopView;

	private LinearLayout.LayoutParams mTopLayoutParam;

	private int mPetDisasterRes;

	private String mCropName;

	private static final int MAX_LIMIT_COUNT = 4;

	public DisasterView(Context context) {
		super(context);
		initView();
	}

	public DisasterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		setOrientation(VERTICAL);
		this.setBackgroundColor(Color.WHITE);
		/* get displayMetrics */
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		mDefaultBottomPadding = (int) applyDimension(COMPLEX_UNIT_DIP, mDefaultBottomPadding, displayMetrics);

		mLayoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mPetDisasterRes = R.drawable.disaster_bg;

		initTopView();

		initBottomView();

	}

	private void initTopView() {
		mTopView = new View(this.getContext());
		mTopLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.home_dimen_20));
		mTopView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ededed")));

	}

	private void initBottomView() {
		int padding = (int) getResources().getDimension(R.dimen.home_dimen_30);
		mBottomView = new TextView(this.getContext());
		mBottomView.setPadding(padding, padding, padding, padding);
		mBottomView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		mBottomView.setText(R.string.disaster_show);
		mBottomView.setGravity(Gravity.CENTER);
		mBottomView.setBackgroundResource(R.drawable.button_white_bg_selector);
		mBottomView.setTextColor(Color.parseColor("#1768bc"));
		mBottomLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		// set event
		mBottomClickListener = new BottomClickListener();
		mBottomView.setOnClickListener(mBottomClickListener);

	}

	// false,true

	public synchronized void updateView(List<PetdisspecwEntity> list, String cropName) {

		this.removeAllViews();
		
		this.mCropName = cropName;

		if (list == null || list.isEmpty()) {
			return;
		}

		Collections.sort(list);

		// addTopView();

		updatePetdisspecws(list);

		// updatePetView(petdiswsEntities);

		addBottomView();

	}

	private void addTopView() {

		ViewParent parent = mTopView.getParent();
		if (parent != null) {
			ViewGroup container = (ViewGroup) parent;
			container.removeView(mTopView);
		}

		this.addView(mTopView, 0, mTopLayoutParam);

	}

	private void updatePetdisspecws(List<PetdisspecwEntity> items) {
		View child;
		if (items != null && !items.isEmpty()) {
			int count = 0;
			for (PetdisspecwEntity item : items) {
				if (item == null) {
					continue;
				}
				if (count == MAX_LIMIT_COUNT) {
					return;
				}
				child = createPetdisspecws(item);
				this.addView(child);
				count++;
			}
		}

	}

	private View createPetdisspecws(final PetdisspecwEntity item) {

		View child = mLayoutInflater.inflate(R.layout.view_home_disaster, this, false);

		TextView desc = (TextView) child.findViewById(R.id.disaster_desc);
		ImageView icon = (ImageView) child.findViewById(R.id.disaster_icon);
		TextView name = (TextView) child.findViewById(R.id.disaster_name);
		TextView type = (TextView) child.findViewById(R.id.disaster_type);

		name.setText(item.petDisSpecName);

		type.setBackgroundResource(R.drawable.natdisaster_bg);

		type.setText("近期易发" + getDisasterString(item.type));
		icon.setImageResource(getDisasterIcon(item.type));

		desc.setText(item.sympton.replace("[为害症状]", ""));

		child.findViewById(R.id.disaster_prevent).setOnClickListener(new PreVentClickListener(item.id, item.petDisSpecName));
		child.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// 友盟统计自定义统计事件
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("name", item.petDisSpecName);
				MobclickAgent.onEvent(DisasterView.this.getContext(), "HarmDetailItem", map);

				Intent intent = HarmDetailsActivity.createIntent(item.id, HarmDetailsActivity.FLAG_ITEM, getContext());
				getContext().startActivity(intent);

			}

		});
		return child;

	}

	private void updatePetView(List<PetdisspecwEntity> petdiswsEntities) {
		View child;
		if (petdiswsEntities != null && !petdiswsEntities.isEmpty()) {
			for (PetdisspecwEntity item : petdiswsEntities) {
				child = createPetView(item);
				this.addView(child);
			}
		}

	}

	private View createPetView(final PetdisspecwEntity item) {

		View child = mLayoutInflater.inflate(R.layout.view_home_disaster, this, false);

		TextView desc = (TextView) child.findViewById(R.id.disaster_desc);
		ImageView icon = (ImageView) child.findViewById(R.id.disaster_icon);
		TextView name = (TextView) child.findViewById(R.id.disaster_name);
		TextView type = (TextView) child.findViewById(R.id.disaster_type);

		name.setText(item.petDisSpecName);

		type.setBackgroundResource(mPetDisasterRes);

		type.setText(getDisasterString(item.type) + "预警");
		icon.setImageResource(getDisasterIcon(item.type));

		desc.setText(item.sympton);
		child.findViewById(R.id.disaster_prevent).setOnClickListener(new PreVentClickListener(item.id, item.petDisSpecName));
		child.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// DONE
				// 友盟统计自定义统计事件
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("name", item.petDisSpecName);
				MobclickAgent.onEvent(DisasterView.this.getContext(), "HarmDetailItem", map);
				Intent intent = HarmDetailsActivity.createIntent(item.id, HarmDetailsActivity.FLAG_ITEM, getContext());
				getContext().startActivity(intent);

			}

		});
		return child;

	}

	private void addBottomView() {
		ViewParent parent = mBottomView.getParent();
		if (parent != null) {
			ViewGroup container = (ViewGroup) parent;
			container.removeView(mBottomView);
		}

		this.addView(mBottomView, -1, mBottomLayoutParam);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();

	}

	class BottomClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {

			// 友盟统计自定义统计事件
			MobclickAgent.onEvent(DisasterView.this.getContext(), "MoreHarm");

			Intent intent = new Intent(v.getContext(), EncyclopediasDiseaseActivity.class);
			intent.putExtra("cropName", mCropName);

			v.getContext().startActivity(intent);
		}
	}

	class PreVentClickListener implements View.OnClickListener {

		private int id;
		private String name;

		public PreVentClickListener(int id, String petDisSpecName) {
			this.id = id;
			this.name = petDisSpecName;
		}

		@Override
		public void onClick(View v) {
			// 需要标明你是点击防治，预防，还是该条item跳进来的

			// 友盟统计自定义统计事件
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", name);
			MobclickAgent.onEvent(DisasterView.this.getContext(), "HarmDetailPrevent", map);

			Intent intent = HarmDetailsActivity.createIntent(id, HarmDetailsActivity.FLAG_PREVENT, getContext());
			v.getContext().startActivity(intent);

		}
	}

	public String getDisasterString(String type) {
		if (TextUtils.isEmpty(type)) {
			return "病害";
		}
		if (type.contains("病害")) {
			return "病害";

		} else if (type.contains("虫害")) {
			return "虫害";

		} else if (type.contains("草害")) {

			return "草害";
		} else {
			return "病害";
		}
	}

	public int getDisasterIcon(String type) {
		if (TextUtils.isEmpty(type)) {
			return R.drawable.weed;
		}
		if (type.contains("病害")) {
			return R.drawable.binghai;

		} else if (type.contains("虫害")) {
			return R.drawable.pest;

		} else if (type.contains("草害")) {

			return R.drawable.weed;
		} else {
			return R.drawable.naturaldis;
		}
	}
}
