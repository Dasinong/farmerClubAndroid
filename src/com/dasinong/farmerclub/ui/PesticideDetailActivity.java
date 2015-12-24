package com.dasinong.farmerclub.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.CPProductEntity;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.view.MyTabView;
import com.dasinong.farmerclub.ui.view.MyTabView.OnItemClickListener;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.GraphicUtils;
import com.lidroid.xutils.BitmapUtils;

public class PesticideDetailActivity extends BaseActivity {
	private TextView tv_active_ingredient;
	private TextView tv_type;
	private TextView tv_manufacturer;
	private TextView tv_registration_id;
	private TopbarView topBar;
	private MyTabView mtv_pesticide;
	private String id;
	private List<String> list = new ArrayList<String>();
	public static final String USE_DIRECTION = "农药方法";
	public static final String FEATURE = "产品特点";
	public static final String GUIDE = "用药指导";
	public static final String TIP = "注意事项";
	private TextView tv_tip;
	private LinearLayout ll_table;
	private TextView tv_telephone;
	private ImageView iv_pic;
	private TextView tv_slogan;
	private LinearLayout ll_feature;
	private TextView tv_guide;
	private TextView tv_feature;
	private TextView tv_description;
	private LinearLayout ll_guide;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pesticide_detail);

		id = getIntent().getStringExtra("id");

		topBar = (TopbarView) findViewById(R.id.topbar);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		tv_slogan = (TextView) findViewById(R.id.tv_slogan);
		tv_active_ingredient = (TextView) findViewById(R.id.tv_active_ingredient);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_manufacturer = (TextView) findViewById(R.id.tv_manufacturer);
		tv_registration_id = (TextView) findViewById(R.id.tv_registration_id);
		tv_telephone = (TextView) findViewById(R.id.tv_telephone);
		mtv_pesticide = (MyTabView) findViewById(R.id.mtv_description);
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		ll_table = (LinearLayout) findViewById(R.id.ll_table);
		ll_feature = (LinearLayout) findViewById(R.id.ll_feature);
		tv_feature = (TextView) findViewById(R.id.tv_feature);
		tv_description = (TextView) findViewById(R.id.tv_description);
		
		ll_guide = (LinearLayout) findViewById(R.id.ll_guide);
		tv_guide = (TextView) findViewById(R.id.tv_guide);

		queryData();
	}

	private void initTopBar(CPProductEntity entity) {
		topBar.setCenterText(entity.data.name);
		topBar.setLeftView(true, true);
	}

	private void queryData() {
		startLoadingDialog();
		RequestService.getInstance().getFormattedCPProductById(this, id, CPProductEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					CPProductEntity entity = (CPProductEntity) resultData;
					if (entity.data != null) {
						initTopBar(entity);
						initData(entity.data);
					}
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}

		});
	}

	private void initData(final CPProductEntity.CPProduct data) {
		if (TextUtils.isEmpty(data.pictureUrl)) {
			iv_pic.setVisibility(View.GONE);
		} else {
			BitmapUtils bitmapUtils = new BitmapUtils(this);
			bitmapUtils.display(iv_pic, NetConfig.PET_IMAGE + data.pictureUrl.replace("/pic/", ""));
		}
		setText2View(data.slogan, tv_slogan);
		
		if (data.activeIngredient != null && !data.activeIngredient.isEmpty()) {
			setActiveIngredient(data.activeIngredient, data.activeIngredientUsage);
		} else {
			((View)tv_active_ingredient.getParent()).setVisibility(View.GONE);
		}

		setText2View(data.type, tv_type);
		setText2View(data.manufacturer, tv_manufacturer);
		setText2View(data.registrationId, tv_registration_id);
		setText2View(data.telephone, tv_telephone);

		list.add(USE_DIRECTION);
		list.add(GUIDE);
		list.add(FEATURE);
		list.add(TIP);
		
		if(TextUtils.isEmpty(data.guideline)){
			list.remove(GUIDE);
		}
		if (TextUtils.isEmpty(data.description) && TextUtils.isEmpty(data.feature)) {
			list.remove(FEATURE);
		}
		if (TextUtils.isEmpty(data.tip)) {
			list.remove(TIP);
		}

		mtv_pesticide.setData(list);

		mtv_pesticide.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(int position) {

				switch (list.get(position)) {
				case USE_DIRECTION:
					tv_tip.setVisibility(View.GONE);
					ll_guide.setVisibility(View.GONE);
					ll_feature.setVisibility(View.GONE);
					ll_table.setVisibility(View.VISIBLE);
					break;
				case GUIDE:
					tv_tip.setVisibility(View.GONE);
					ll_feature.setVisibility(View.GONE);
					ll_table.setVisibility(View.GONE);
					ll_guide.setVisibility(View.VISIBLE);
					setText2View(data.guideline, tv_guide);
					break;
				case FEATURE:
					ll_table.setVisibility(View.GONE);
					ll_guide.setVisibility(View.GONE);
					tv_tip.setVisibility(View.GONE);
					ll_feature.setVisibility(View.VISIBLE);
					setText2View(data.feature, tv_feature);
					setText2View(data.description, tv_description);
					break;
				case TIP:
					ll_table.setVisibility(View.GONE);
					ll_guide.setVisibility(View.GONE);
					ll_feature.setVisibility(View.GONE);
					tv_tip.setVisibility(View.VISIBLE);
					tv_tip.setText(data.tip);
					break;
				}
			}
		});

		onFirstInit(data);
	}

	private void setActiveIngredient(List<String> nameList, List<String> contentList) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < nameList.size(); i++) {
			if (i == nameList.size() - 1) {
				builder.append(nameList.get(i)).append("   ").append(contentList.get(i));
			} else {
				builder.append(nameList.get(i)).append("   ").append(contentList.get(i)).append("\n");
			}
		}
		setText2View(builder.toString(), tv_active_ingredient);
	}

	private void onFirstInit(CPProductEntity.CPProduct data) {
		tv_tip.setVisibility(View.GONE);
		ll_guide.setVisibility(View.GONE);
		ll_feature.setVisibility(View.GONE);
		ll_table.setVisibility(View.VISIBLE);
		for (int i = 0; i < data.instructions.size(); i++) {
			TableLayout table = (TableLayout) View.inflate(PesticideDetailActivity.this, R.layout.item_table, null);
			TextView tv_crop = (TextView) table.findViewById(R.id.tv_crop);
			TextView tv_disease = (TextView) table.findViewById(R.id.tv_disease);
			TextView tv_volumn = (TextView) table.findViewById(R.id.tv_volumn);
			TextView tv_method = (TextView) table.findViewById(R.id.tv_method);
			TextView tv_guideline = (TextView) table.findViewById(R.id.tv_guideline);

			tv_crop.setText(data.instructions.get(i).crop);
			tv_disease.setText(data.instructions.get(i).disease);
			tv_volumn.setText(data.instructions.get(i).volume);
			tv_method.setText(data.instructions.get(i).method);
			if(!TextUtils.isEmpty(data.instructions.get(i).guideline)){
				 tv_guideline.setText(data.instructions.get(i).guideline);
				 ((View)tv_method.getParent()).setBackgroundResource(R.drawable.table_normal_bg);
			} else {
				((View)tv_guideline.getParent()).setVisibility(View.GONE);
				((View)tv_method.getParent()).setBackgroundResource(R.drawable.table_bottom_bg);
			}
//			setText2View(data.instructions.get(i).guideline, tv_guideline);

			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.setMargins(0, 0, 0, GraphicUtils.dip2px(this, 20));
			table.setLayoutParams(params);
			ll_table.addView(table);
		}
	}

	private void setText2View(String text, TextView tv) {
		if (TextUtils.isEmpty(text)) {
			ViewParent parent = tv.getParent();
			if (parent instanceof LinearLayout) {
				LinearLayout ll = (LinearLayout) parent;
				ll.setVisibility(View.GONE);
			} else {
				tv.setVisibility(View.GONE);
			}
			return;
		}
		tv.setText(text);
	}

}
