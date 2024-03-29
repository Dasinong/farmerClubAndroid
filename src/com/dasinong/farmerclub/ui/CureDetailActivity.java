package com.dasinong.farmerclub.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.database.disaster.domain.CPProduct;
import com.dasinong.farmerclub.database.disaster.service.DisasterManager;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.DrugListEntity;
import com.dasinong.farmerclub.entity.DrugListEntity.Data;
import com.dasinong.farmerclub.entity.DrugListEntity.Drug;
import com.dasinong.farmerclub.entity.HarmDetailEntity.Solutions;
import com.dasinong.farmerclub.entity.PetDisSpecEntity.PetDisSpec.Solution;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.adapter.CureAdapter;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.DeviceHelper;
import com.umeng.analytics.MobclickAgent;

public class CureDetailActivity extends BaseActivity {

	private ListView lv_medicine;
	private TextView tv_cure_name;
	private TextView tv_cure_stage;
	private TextView tv_cure_provider;
	private TextView tv_cure_content;
	private TopbarView topbar;
	private View header;
	private Solution solu;
	private LinearLayout ll_medicine_title;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			DrugListEntity drugList = (DrugListEntity) msg.obj;
			initHeader(drugList.data.petSolutions);
			initListView(drugList.data.cPProducts);
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cure_details);
		lv_medicine = (ListView) findViewById(R.id.lv_medicine);
		topbar = (TopbarView) findViewById(R.id.topbar);

		header = View.inflate(this, R.layout.cure_details_header, null);

		tv_cure_name = (TextView) header.findViewById(R.id.tv_cure_name);
		tv_cure_stage = (TextView) header.findViewById(R.id.tv_cure_stage);
		tv_cure_provider = (TextView) header.findViewById(R.id.tv_cure_provider);
		tv_cure_content = (TextView) header.findViewById(R.id.tv_cure_content);
		ll_medicine_title = (LinearLayout) header.findViewById(R.id.ll_medicine_title);

		initTopBar();

		solu = (Solution) getIntent().getExtras().getSerializable("solu");
		int position = getIntent().getExtras().getInt("position");
		int size = getIntent().getExtras().getInt("size");

		if (position > size) {
			// 预防
			char c = (char) (65 + position - size - 1);
			tv_cure_name.setText("治疗方案" + c);
		} else {
			// 治疗
			char c = (char) (65 + position - 1);
			tv_cure_name.setText("预防方案" + c);
		}
		if (DeviceHelper.checkNetWork(this)) {
			queryDrug(solu.petSoluId);
		} else {
			initHeader(solu);
			initData(solu.petSoluId);
		}
	}

	// 此方法在此处重载 ，以适应版本变化 ，兼容两个bean中的solution字段
	private void initHeader(Solution solu) {
		if (TextUtils.isEmpty(solu.subStageId) || "0".equals(solu.subStageId)) {
			tv_cure_stage.setVisibility(View.GONE);
		} else {
			tv_cure_stage.setText("适宜时期：" + solu.subStageId);
		}

		if (TextUtils.isEmpty(solu.providedBy)) {
			tv_cure_provider.setVisibility(View.GONE);
		} else {
			tv_cure_provider.setVisibility(View.VISIBLE);
			tv_cure_provider.setText(solu.providedBy);
		}

		// tv_cure_provider.setText(solu.providedBy);
		tv_cure_content.setText(solu.petSoluDes);
	}

	private void initHeader(Solutions solu) {
		if (TextUtils.isEmpty(solu.subStageId) || "0".equals(solu.subStageId)) {
			tv_cure_stage.setVisibility(View.GONE);
		} else {
			tv_cure_stage.setText("适宜时期：" + solu.subStageId);
		}

		if (TextUtils.isEmpty(solu.providedBy)) {
			tv_cure_provider.setVisibility(View.GONE);
		} else {
			tv_cure_provider.setVisibility(View.VISIBLE);
			tv_cure_provider.setText(solu.providedBy);
		}

		// tv_cure_provider.setText(solu.providedBy);
		tv_cure_content.setText(solu.petSoluDes);
	}

	private void queryDrug(int petSoluId) {
		startLoadingDialog();
		RequestService.getInstance().getPetSolu(this, petSoluId, DrugListEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					DrugListEntity drugList = (DrugListEntity) resultData;

					Message msg = handler.obtainMessage();
					msg.obj = drugList;
					handler.sendMessage(msg);

					dismissLoadingDialog();
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				initHeader(solu);
				initData(solu.petDisSpecId);
				dismissLoadingDialog();
			}
		});
	}

	private void initTopBar() {
		topbar.setCenterText("防治方案详情");
		topbar.setLeftView(true, true);
	}

	private void initData(int petSoluId) {
		List<CPProduct> drugList = DisasterManager.getInstance(this).getAllDrug(petSoluId);
		// TODO MING 增加本地查询
		DrugListEntity drugListEntity = new DrugListEntity();
		drugListEntity.data = new Data();
		drugListEntity.data.cPProducts = new ArrayList<Drug>();
		for (int i = 0; i < drugList.size(); i++) {
			Drug drug = new Drug();

			drug.activeIngredient = drugList.get(i).activeIngredient;
			drug.disease = drugList.get(i).disease;
			drug.guideline = drugList.get(i).guideline;
			drug.id = drugList.get(i).cPProductId;
			drug.manufacturer = drugList.get(i).manufacturer;
			drug.name = drugList.get(i).cPProductName;
			drug.registrationId = drugList.get(i).registerationId;
			drug.tip = drugList.get(i).tip;
			drug.type = drugList.get(i).type;
			drug.volumn = drugList.get(i).volume;

			drugListEntity.data.cPProducts.add(drug);
		}
		initListView(drugListEntity.data.cPProducts);
	}

	private void initListView(final List<Drug> drugList) {
		if (drugList == null || drugList.size() < 1) {
			ll_medicine_title.setVisibility(View.GONE);
		} else {
			ll_medicine_title.setVisibility(View.VISIBLE);
		}
		lv_medicine.addHeaderView(header, null, false);
		lv_medicine.setAdapter(new CureAdapter(this, drugList, false));

		lv_medicine.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MobclickAgent.onEvent(CureDetailActivity.this, "PesticideDetail");
				Intent intent = new Intent(CureDetailActivity.this, PesticideDetailActivity.class);
				intent.putExtra("id", drugList.get(position - 1).id + "");
				startActivity(intent);
			}
		});
	}
}
