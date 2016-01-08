package com.dasinong.farmerclub.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.components.home.view.popupwidow.CommSelectPopWindow;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.ChangeStageEntity;
import com.dasinong.farmerclub.entity.CropDetailEntity;
import com.dasinong.farmerclub.entity.FieldDetailEntity;
import com.dasinong.farmerclub.entity.FieldDetailEntity.StageEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.DisasterView;
import com.dasinong.farmerclub.ui.view.MyFieldTaskView;
import com.dasinong.farmerclub.ui.view.MyFieldTopView;
import com.dasinong.farmerclub.ui.view.MyFieldTopView.OnStageItemClickListener;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.SerializableMap;

public class MyFieldDetailActivity extends BaseActivity {

	private Map<Long, String> fields = new HashMap<Long, String>();
	private MyFieldTopView view_top;
	private FieldDetailEntity entity;
	private MyFieldTaskView view_task;
	private DisasterView view_disaster;
	private ChangeStageEntity changeEntity;
	private TopbarView topBar;
	private long currentFieldId;
	private List<Long> fieldIdList = new ArrayList<Long>();
	private List<String> fieldNameList = new ArrayList<String>();
	private CommSelectPopWindow popWindow;
	private CropDetailEntity cropEntity;
	private boolean hasField = true;
	private String cropId;
	private String cropName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_field_detail);

		initView();
		initEvent();

	}

	@Override
	protected void onStart() {
		super.onStart();

		Bundle bundle = getIntent().getExtras();
		SerializableMap map = (SerializableMap) bundle.getSerializable("fields");
		if (map != null) {
			this.fields = map.getMap();
		}

		String newFieldName = bundle.getString("fieldName");
		int newFieldId = bundle.getInt("fieldId");

		if (newFieldId != -1 && !TextUtils.isEmpty(newFieldName)) {
			fields.put((long) newFieldId, newFieldName);
		}

		Set<Entry<Long, String>> entrySet = fields.entrySet();
		for (Entry<Long, String> entry : entrySet) {
			fieldIdList.add(entry.getKey());
			fieldNameList.add(entry.getValue());
		}

		String strFieldId = null;
		currentFieldId = SharedPreferencesHelper.getLong(this, Field.FIELDID, -1);

		if (currentFieldId != -1 && fields.keySet().contains(currentFieldId)) {
			hasField = true;
			strFieldId = String.valueOf(currentFieldId);
			initTopBar();
			queryData(strFieldId);
		} else if (fields.size() > 0) {
			hasField = true;
			currentFieldId = fields.keySet().iterator().next();
			SharedPreferencesHelper.setLong(this, Field.FIELDID, currentFieldId);
			strFieldId = String.valueOf(currentFieldId);
			initTopBar();
			queryData(strFieldId);
		} else {
			hasField = false;
			cropId = getIntent().getStringExtra("cropId");
			cropName = getIntent().getStringExtra("cropName");
			initTopBar(cropName);
			queryData(cropId, "-1");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void initView() {
		topBar = (TopbarView) findViewById(R.id.topbar);
		view_top = (MyFieldTopView) findViewById(R.id.view_top);
		view_task = (MyFieldTaskView) findViewById(R.id.view_task);
		view_disaster = (DisasterView) findViewById(R.id.view_disaster);
	}

	private void initTopBar() {
		topBar.setCenterText(fields.get(currentFieldId));
		topBar.setRightText("加田");
		topBar.setLeftView(true, true);

		topBar.setCenterClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initPoPuWindow();
				popWindow.showAsDropDown(topBar);
			}
		});

		topBar.setRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferencesHelper.setString(MyFieldDetailActivity.this, Field.CROP_ID, String.valueOf(entity.data.field.crop.cropId));
				Intent intent = new Intent(MyFieldDetailActivity.this, IsInFieldActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initTopBar(String cropName) {
		topBar.setCenterText(cropName);
		topBar.setRightText("加田");
		topBar.setRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferencesHelper.setString(MyFieldDetailActivity.this, Field.CROP_ID, String.valueOf(cropEntity.data.crop.crop.cropId));
				Intent intent = new Intent(MyFieldDetailActivity.this, IsInFieldActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initPoPuWindow() {
		if (popWindow != null) {
			return;
		}
		popWindow = new CommSelectPopWindow(this);
		popWindow.setDatas(fieldNameList);
		popWindow.setPopWidth(topBar.getMeasuredWidth());
		popWindow.setmPopItemSelectListener(new CommSelectPopWindow.PopItemSelectListener() {

			@Override
			public void itemSelected(CommSelectPopWindow window, int position, CharSequence fieldName) {

				if (fieldIdList.get(position) == currentFieldId) {
					return;
				}

				currentFieldId = fieldIdList.get(position);

				topBar.setCenterText(fieldNameList.get(position));

				SharedPreferencesHelper.setLong(MyFieldDetailActivity.this, Field.FIELDID, currentFieldId);

				queryData(String.valueOf(currentFieldId));
			}
		});
	}

	private void initEvent() {
		view_top.setOnStageItemClickListener(new OnStageItemClickListener() {

			@Override
			public void onClick(int position) {
				if (hasField) {
					startLoadingDialog();
					String fieldId = String.valueOf(entity.data.field.fieldId);
					String subStageId = String.valueOf(entity.data.field.stagelist.get(position).subStageId);
					RequestService.getInstance().changeStage(MyFieldDetailActivity.this, "" + fieldId, "" + subStageId, ChangeStageEntity.class,
							new RequestListener() {
								@Override
								public void onSuccess(int requestCode, BaseEntity resultData) {
									if (resultData.isOk()) {
										changeEntity = (ChangeStageEntity) resultData;
										setData(true);
									}

									dismissLoadingDialog();
								}

								@Override
								public void onFailed(int requestCode, Exception error, String msg) {
									dismissLoadingDialog();
								}
							});
				} else {
					queryData(cropId, cropEntity.data.crop.substagews.get(position).subStageId + "");
				}
			}
		});
	}

	private void queryData(String fieldId) {
		startLoadingDialog();
		RequestService.getInstance().getField(this, fieldId, FieldDetailEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					entity = (FieldDetailEntity) resultData;
					setData(false);
				} else {

				}
				dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				error.printStackTrace();
				dismissLoadingDialog();
			}
		});
	}

	private void queryData(String cropId, final String subStageId) {
		startLoadingDialog();
		String validId = "";
		if ("-1".endsWith(subStageId)) {
			validId = "";
		} else {
			validId = subStageId;
		}
		RequestService.getInstance().getCropDetails(this, cropId, validId, CropDetailEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					cropEntity = (CropDetailEntity) resultData;
					setNoFieldData(subStageId);
				}
				dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				error.printStackTrace();
				dismissLoadingDialog();
			}
		});
	}

	protected void setNoFieldData(String subStageId) {
		int intSubstageId = Integer.valueOf(subStageId);
		view_top.setData(intSubstageId, cropEntity.data.crop.crop, 0, cropEntity.data.crop.substagews);
		view_disaster.updateView(cropEntity.data.crop.petdisspecws,cropEntity.data.crop.crop.cropName);
		view_task.setData(cropEntity.data.crop.taskspecws);
	}

	private void setData(boolean isChange) {
		if (isChange) {
			view_top.setData(changeEntity.data.currentStageID, changeEntity.data.crop, changeEntity.data.area, changeEntity.data.stagelist);
			view_disaster.updateView(changeEntity.data.petdisspecws,changeEntity.data.crop.cropName);
			view_task.setData(changeEntity.data.taskspecws);
		} else {
			view_top.setData(entity.data.field.currentStageID, entity.data.field.crop, entity.data.field.area, entity.data.field.stagelist);
			view_disaster.updateView(entity.data.field.petdisspecws,entity.data.field.crop.cropName);
			view_task.setData(entity.data.field.taskspecws);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}
}
