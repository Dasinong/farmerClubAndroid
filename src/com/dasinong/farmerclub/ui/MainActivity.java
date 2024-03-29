package com.dasinong.farmerclub.ui;

import java.util.ArrayList;
import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.database.task.dao.impl.SubStageDaoImpl;
import com.dasinong.farmerclub.database.task.dao.impl.TaskSpecDaoImpl;
import com.dasinong.farmerclub.database.task.domain.SubStage;
import com.dasinong.farmerclub.database.task.domain.TaskSpec;
import com.dasinong.farmerclub.ui.view.AnimatedExpandableListView;
import com.dasinong.farmerclub.ui.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

/**
 * This is an example usage of the AnimatedExpandableListView class.
 * 
 * It is an activity that holds a listview which is populated with 100 groups
 * where each group has from 1 to 100 children (so the first group will have one
 * child, the second will have two children and so on...).
 */
public class MainActivity extends Activity {
    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;

    List<GroupItem> taskDataList = new ArrayList<GroupItem>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        
//        List<GroupItem> items = new ArrayList<GroupItem>();
//        
//        // Populate our list with groups and it's children
//        for(int i = 1; i < 100; i++) {
//            GroupItem item = new GroupItem();
//            
//            item.title = "Group " + i;
//            
//            for(int j = 0; j < i; j++) {
//                ChildItem child = new ChildItem();
//                child.title = "Awesome item " + j;
//                child.hint = "Too awesome";
//                
//                item.items.add(child);
//            }
//            
//            items.add(item);
//        }
        
        initData();
        
        adapter = new ExampleAdapter(this);
        adapter.setData(taskDataList);
        
        listView = (AnimatedExpandableListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        
        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group 
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
            
        });
        
        listView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				
				TaskSpec child = adapter.getChild(groupPosition, childPosition);
				Toast.makeText(MainActivity.this, child.taskSpecName, 0).show();
				return false;
			}
		});
        
    }
    
    private void initData() {
		SubStageDaoImpl dao = new SubStageDaoImpl(this);
		List<String> queryStageCategory = dao.queryStageCategory();
		List<SubStage> queryStageSubCategory = dao.queryStageSubCategory(queryStageCategory.get(0));
		
		TaskSpecDaoImpl dao1 = new TaskSpecDaoImpl(this);
		for(SubStage subStage:queryStageSubCategory){
			GroupItem item = new GroupItem();
			item.subStage = subStage;
			List<TaskSpec> queryTaskSpecWithSubStage = dao1.queryTaskSpecWithSubStage(subStage.subStageId);
			item.items = queryTaskSpecWithSubStage;
			taskDataList.add(item);
		}
		
	}
    
    private static class GroupItem {
		SubStage subStage;
		List<TaskSpec> items = new ArrayList<TaskSpec>();
	}
    
//    private static class ChildItem {
//        String title;
//        String hint;
//    }
    
    private static class ChildHolder {
        TextView title;
//        TextView hint;
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
        public TaskSpec getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            TaskSpec item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.task_list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
//                holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            
            holder.title.setText(item.taskSpecName);
//            holder.hint.setText(item.hint);
            
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
            
            holder.title.setText(item.subStage.stageName);
            
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
    
}
