package com.dasinong.farmerclub.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.ChangeStageEntity;
import com.dasinong.farmerclub.entity.StagesEntity;
import com.dasinong.farmerclub.entity.StagesEntity.StageEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.ui.view.ViewMiddle;
import com.umeng.analytics.MobclickAgent;

public class SelectSubStageActivity extends MyBaseActivity implements OnClickListener {
	private Button btn_no_sure_substage;
	private Button btn_sure_substage;
	protected String lastStage;
	private String subStageId;
	private TopbarView topbar;
	private ViewMiddle viewMiddle;
	private List<View> viewList = new ArrayList<View>();
	private List<String> textList = new ArrayList<String>();
	private String cropId;
	private Map<String, Map<String, Integer>> stagesMap = new LinkedHashMap<String, Map<String, Integer>>();
	private Spinner spinner_substage;
	private List<Integer> stageIdList;
	private List<String> stageNameList;
	private ChangeStageEntity entity;
	public static Activity instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(0, 0);
		setContentView(R.layout.activity_select_substage);
		
		instance = this;
		
		spinner_substage = (Spinner) findViewById(R.id.spinner_substage);
		btn_no_sure_substage = (Button) findViewById(R.id.btn_no_sure_substage);
		btn_sure_substage = (Button) findViewById(R.id.btn_sure_substage);
		topbar = (TopbarView) findViewById(R.id.topbar);

		cropId = SharedPreferencesHelper.getString(this, Field.CROP_ID, "");

		viewMiddle = new ViewMiddle(this);
		viewList.add(viewMiddle);
		textList.add("请选择生长阶段");

		if (!TextUtils.isEmpty(cropId)) {
			queryStages(cropId);
		}

		initTopBar();

		btn_no_sure_substage.setOnClickListener(this);
		btn_sure_substage.setOnClickListener(this);
	}

	private void queryStages(String cropId) {
		startLoadingDialog();
		RequestService.getInstance().getStages(this, cropId, StagesEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					StagesEntity entity = (StagesEntity) resultData;
					convertData(entity.data);
				}
				dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	protected void setData() {
		spinner_substage.setAdapter(new ArrayAdapter<>(this, R.layout.textview, stageNameList));

		spinner_substage.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				subStageId = String.valueOf(stageIdList.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	protected void convertData(List<StageEntity> list) {
		stageIdList = new ArrayList<Integer>();
		stageNameList = new ArrayList<String>();
		for (StageEntity stageEntity : list) {
			stageIdList.add(stageEntity.subStageId);
			stageNameList.add(stageEntity.subStageName);
		}
		stageNameList.add(0, "请选择");
		stageIdList.add(0, -1);
		setData();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_no_sure_substage:
			subStageId = "";
			MobclickAgent.onEvent(this, "NoSubStage");
			goToNext();
			
			break;
		case R.id.btn_sure_substage:
			if (TextUtils.isEmpty(subStageId) || "-1".equals(subStageId)) {
				showToast("请完善作物生长阶段，或者选择我不知道");
				return;
			}

			SharedPreferencesHelper.setString(this, Field.SUBSTAGE_ID, subStageId);
			MobclickAgent.onEvent(this, "SureSubStage");
			goToNext();
			break;
		}
	}

	private void initTopBar() {
		topbar.setCenterText("生长期周期");
		topbar.setLeftView(true, true);
	}

	private void goToNext() {
		startLoadingDialog();
		String area = SharedPreferencesHelper.getString(this, Field.FIELD_SIZE, "");
		String locationId = SharedPreferencesHelper.getString(this, Field.VILLAGE_ID, "");

		RequestService.getInstance().createField(this, "", area, locationId, cropId, subStageId, ChangeStageEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					entity = (ChangeStageEntity) resultData;
					SharedPreferencesHelper.setLong(SelectSubStageActivity.this, Field.FIELDID, entity.data.fieldId);
					Intent intent = new Intent(SelectSubStageActivity.this, MyFieldDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("fieldId", entity.data.fieldId);
					bundle.putString("fieldName", entity.data.fieldName);
					intent.putExtras(bundle);
					
					startActivity(intent);
					SelectSubStageActivity.this.showToast(resultData.getMessage());
				}
				dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
	}
}
