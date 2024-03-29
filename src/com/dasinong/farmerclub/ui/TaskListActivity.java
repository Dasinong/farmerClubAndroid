package com.dasinong.farmerclub.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.components.domain.TaskStatus;
import com.dasinong.farmerclub.database.task.dao.impl.SubStageDaoImpl;
import com.dasinong.farmerclub.database.task.dao.impl.TaskSpecDaoImpl;
import com.dasinong.farmerclub.database.task.domain.SubStage;
import com.dasinong.farmerclub.database.task.domain.TaskSpec;
import com.dasinong.farmerclub.entity.AllTaskEntity;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.AllTaskEntity.TaskDetailEntity;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.AnimatedExpandableListView;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.ui.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.dasinong.farmerclub.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mob.tools.utils.SharePrefrenceHelper;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class TaskListActivity extends BaseActivity {
	private TopbarView mTopbarView;
	private AnimatedExpandableListView listView;
	private ExampleAdapter adapter;
	List<GroupItem> taskDataList = new ArrayList<GroupItem>();

	private List<TaskSpec> mSelectTask = new ArrayList<TaskSpec>();

	private long filedId;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			initListView();
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);

		initView();
		setUpView();

		requestData();

	}

	private void initListView() {
		adapter = new ExampleAdapter(this);
		adapter.setData(taskDataList);

		listView = (AnimatedExpandableListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);

		// if(listView.getChildCount()>0)
		for (int i = 0; i < taskDataList.size(); i++) {
			listView.expandGroup(i);
		}

		// In order to show animations, we need to use a custom click handler
		// for our ExpandableListView.
		listView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// We call collapseGroupWithAnimation(int) and
				// expandGroupWithAnimation(int) to animate group
				// expansion/collapse.
				if (listView.isGroupExpanded(groupPosition)) {
					// listView.collapseGroupWithAnimation(groupPosition);
					listView.collapseGroup(groupPosition);
				} else {
					// listView.expandGroupWithAnimation(groupPosition);
					listView.expandGroup(groupPosition);
				}
				return true;
			}

		});

		listView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

				TaskStatus child = adapter.getChild(groupPosition, childPosition);

				Intent intent = new Intent();
				intent.putExtra(TaskDetailsActivity.TASK_ID, child.taskSpecId);
				intent.putExtra(TaskDetailsActivity.TASK_TITLE, child.taskSpecName);
				setResult(RESULT_OK, intent);

				finish();

				return false;
			}
		});
	}

	private void requestData() {
		long fieldId = SharedPreferencesHelper.getLong(this, Field.FIELDID, 0);
		RequestService.getInstance().getAllTask(this, fieldId + "", AllTaskEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					AllTaskEntity entity = (AllTaskEntity) resultData;
					
					for (Integer i : entity.data.keySet()) {

						List<TaskDetailEntity> list = entity.data.get(i);

						GroupItem groupItem = new GroupItem();

						for (int j = 0; j < list.size(); j++) {
							TaskStatus taskStatus = new TaskStatus();
							
							if(j == 0){
								groupItem.subStage = new SubStage();
								
								groupItem.subStage.stageName = list.get(j).stageName;
								groupItem.subStage.subStageName = list.get(j).subStageName;
								groupItem.subStage.subStageId = list.get(j).subStageId;
							}
							
							taskStatus.subStageId = list.get(j).subStageId;
							taskStatus.taskSpecId = list.get(j).taskSpecId;
							taskStatus.taskSpecName = list.get(j).taskSpecName;
							
							groupItem.items.add(taskStatus);
							
						}
						taskDataList.add(groupItem);
					}

					handler.sendEmptyMessage(0);
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				initData();
				initListView();
				
			}
		});
	}

	private void initData() {
		filedId = SharedPreferencesHelper.getLong(this, Field.FIELDID, 0);
		
		SubStageDaoImpl dao = new SubStageDaoImpl(this);
		List<String> queryStageCategory = null;
		queryStageCategory = dao.queryStageCategory();
		// TODO MING 获取究竟是什么作物
		String cropName = SharedPreferencesHelper.getString(this, Field.CROP_NAME, "水稻");
		if ("水稻".equals(cropName)) {
			queryStageCategory = dao.queryStageCategory(35, 59);
		} else if ("小麦".equals(cropName)) {
			queryStageCategory = dao.queryStageCategory(60, 73);
		}
		for (String queryStage : queryStageCategory) {
			List<SubStage> queryStageSubCategory = dao.queryStageSubCategory(queryStage);

			TaskSpecDaoImpl dao1 = new TaskSpecDaoImpl(this);
			for (SubStage subStage : queryStageSubCategory) {
				if ("水稻".equals(cropName) && subStage.subStageId > 59) {
					continue;
				} else if ("小麦".equals(cropName) && subStage.subStageId < 60) {
					continue;
				}
				GroupItem item = new GroupItem();
				item.subStage = subStage;
				List<TaskStatus> readTaskStatus = readTaskStatus(subStage);
				if (!readTaskStatus.isEmpty()) {
					item.items = readTaskStatus;
					taskDataList.add(item);
				} else {
					List<TaskSpec> queryTaskSpecWithSubStage = dao1.queryTaskSpecWithSubStage(subStage.subStageId);
					for (TaskSpec task : queryTaskSpecWithSubStage) {
						TaskStatus status = new TaskStatus();
						status.subStageId = subStage.subStageId;
						status.taskSpecName = task.taskSpecName;
						status.taskSpecId = task.taskSpecId;
						status.isCheck = false;
						readTaskStatus.add(status);
					}
					item.items = readTaskStatus;
					taskDataList.add(item);
				}
			}
		}
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
	}

	private void setUpView() {
		mTopbarView.setCenterText("全部农事");
		mTopbarView.setLeftView(true, true);
		// mTopbarView.setRightText("提交");
		// mTopbarView.setRightClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// updateTaskStates();
		// }
		// });
	}

	protected void updateTaskStates() {

		if (mSelectTask.size() <= 0) {
			return;
		}

		showToast("选择任务：" + mSelectTask.size());

		StringBuilder taskIds = new StringBuilder();
		StringBuilder taskStatuss = new StringBuilder();

		for (int i = 0; i < mSelectTask.size(); i++) {
			TaskSpec task = mSelectTask.get(i);
			if (i == mSelectTask.size() - 1) {
				taskIds.append(task.taskSpecId);
				taskStatuss.append(true + "");
			} else {
				taskIds.append(task.taskSpecId + ",");
				taskStatuss.append(true + ",");
			}
		}
		// for (TaskSpec task : mSelectTask) {
		// taskIds.append(task.taskSpecId + ",");
		// taskStatuss.append(true + ",");
		// }

		startLoadingDialog();
		RequestService.getInstance().updateTask(this, "10", /* * taskIds.toString()*/"4,5,6", taskStatuss.toString(), BaseEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();

			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();

			}
		});
	}

	private static class GroupItem {
		SubStage subStage;
		List<TaskStatus> items = new ArrayList<TaskStatus>();
	}

	private static class ChildHolder {
		TextView title;
		CheckBox state;
		// TextView hint;
	}

	private static class GroupHolder {
		TextView title;
	}

	/**
	 * Adapter for our list of {@link GroupItem}s.
	 */
	private class ExampleAdapter extends AnimatedExpandableListAdapter {
		private LayoutInflater inflater;

		private List<GroupItem> items;

		public ExampleAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void setData(List<GroupItem> items) {
			this.items = items;
		}

		@Override
		public TaskStatus getChild(int groupPosition, int childPosition) {
			return items.get(groupPosition).items.get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getRealChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			ChildHolder holder;
			final TaskStatus item = getChild(groupPosition, childPosition);
			// if (convertView == null) {
			holder = new ChildHolder();
			convertView = inflater.inflate(R.layout.task_list_item, parent, false);
			holder.title = (TextView) convertView.findViewById(R.id.textTitle);
			holder.state = (CheckBox) convertView.findViewById(R.id.cb_state);
			convertView.setTag(holder);
			// } else {
			// holder = (ChildHolder) convertView.getTag();
			// }

			holder.title.setText(item.taskSpecName);
			// holder.hint.setText(item.type);
			Logger.d("YSL", item.isCheck + "--" + item.taskSpecName);
			holder.state.setChecked(item.isCheck);

			holder.state.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					GroupItem group = getGroup(groupPosition);
					// group.get(groupPosition).setChecked(childrenAllIsChecked);
					group.items.get(childPosition).isCheck = isChecked;
					item.isCheck = isChecked;
					saveTaskStatus(group);
					Logger.d("YSL", "onCheckedChanged--" + isChecked + "--" + item.taskSpecName);
				}
			});

			return convertView;
		}

		@Override
		public int getRealChildrenCount(int groupPosition) {
			return items.get(groupPosition).items.size();
		}

		@Override
		public GroupItem getGroup(int groupPosition) {
			return items.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return items.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			GroupHolder holder;
			GroupItem item = getGroup(groupPosition);
			if (convertView == null) {
				holder = new GroupHolder();
				convertView = inflater.inflate(R.layout.task_group_item, parent, false);
				holder.title = (TextView) convertView.findViewById(R.id.textTitle);
				convertView.setTag(holder);
			} else {
				holder = (GroupHolder) convertView.getTag();
			}

			holder.title.setText(item.subStage.subStageName);
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			return true;
		}
	}

	private static final String PREFIX = "field";

	private String getSaveKey(SubStage mCurrentSubStage) {
		return PREFIX + filedId + mCurrentSubStage.subStageId;
	}

	private List<TaskStatus> readTaskStatus(SubStage mCurrentSubStage) {

		Gson gson = new Gson();
		String key = getSaveKey(mCurrentSubStage);
		String result = SharedPreferencesHelper.getString(this, key, null);
		List<TaskStatus> lists = new ArrayList<TaskStatus>();
		if (result != null) {
			lists.clear();
			lists = gson.fromJson(result, new TypeToken<List<TaskStatus>>() {
			}.getType());
		}

		return lists;
	}

	private void saveTaskStatus(GroupItem group) {
		List<TaskStatus> lists = group.items;
		if (lists.isEmpty()) {
			return;
		}
		String key = getSaveKey(group.subStage);
		Gson gson = new Gson();
		SharedPreferencesHelper.setString(this, key, gson.toJson(lists));
	}

}
