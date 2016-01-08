package com.dasinong.farmerclub.ui;

import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.database.task.dao.impl.StepsDaoImpl;
import com.dasinong.farmerclub.database.task.dao.impl.SubStageDaoImpl;
import com.dasinong.farmerclub.database.task.dao.impl.TaskSpecDaoImpl;
import com.dasinong.farmerclub.database.task.domain.Steps;
import com.dasinong.farmerclub.database.task.domain.SubStage;
import com.dasinong.farmerclub.database.task.domain.TaskSpec;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.StepsListEntity;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.adapter.TaskDetailsAdapter;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class TaskDetailsActivity extends BaseActivity {

	public static final String TASK_ID = "task_id";
	public static final String TASK_TITLE = "task_title";
	
	private TopbarView mTopbarView;
	
	private ListView mListView;

	private List<Steps> taskSpecs;
	
	private int taskSpecId;
	private String mTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tast_details);
		
		initData();
		initView();
		setUpView();
		requestData();
	}

	private void requestData() {
		startLoadingDialog();
		RequestService.getInstance().getSteps(this, "0", taskSpecId+"", StepsListEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if(resultData.isOk()){
					StepsListEntity entity = (StepsListEntity) resultData;
					setAdapter(entity);
				}else{
					showToast(resultData.getMessage());
				}
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	protected void setAdapter(StepsListEntity entity) {
		TaskDetailsAdapter mAdapter = new TaskDetailsAdapter(this, entity.getData(), false);
		mListView.setAdapter(mAdapter);
	}

	private void initData() {
		taskSpecId = getIntent().getIntExtra(TASK_ID, 0);
		mTitle = getIntent().getStringExtra(TASK_TITLE);
		
		StepsDaoImpl dao1 = new StepsDaoImpl(this);
		taskSpecs = dao1.queryStepsWithTaskSpecId(taskSpecId);
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mListView = (ListView) this.findViewById(R.id.list_task_detail_list);
	}

	private void setUpView() {
		mTopbarView.setCenterText(mTitle);
		mTopbarView.setLeftView(true, true);
//		mTopbarView.setRightText("查看全部");
//		mTopbarView.setRightClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(TaskDetailsActivity.this,TaskListActivity.class);
//				startActivityForResult(intent, 0);
//			}
//		});
		
//		TaskDetailsAdapter mAdapter = new TaskDetailsAdapter(this, taskSpecs, false);
//		mListView.setAdapter(mAdapter);
//		mListView
		
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(arg1 == RESULT_OK){
			taskSpecId = arg2.getIntExtra(TASK_ID, 0);
			mTitle = arg2.getStringExtra(TASK_TITLE);
			mTopbarView.setCenterText(mTitle);
			requestData();
		}
	}
	
}
