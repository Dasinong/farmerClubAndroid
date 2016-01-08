package com.dasinong.farmerclub.ui.view;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.FieldDetailEntity.TaskSpecwEntity;
import com.dasinong.farmerclub.ui.TaskDetailsActivity;
import com.dasinong.farmerclub.utils.GraphicUtils;
import com.lidroid.xutils.BitmapUtils;

public class MyFieldTaskView extends LinearLayout {

	private View rootView;
	private LinearLayout ll_task;
	private Context context;

	public MyFieldTaskView(Context context) {
		this(context, null);
	}

	public MyFieldTaskView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyFieldTaskView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init(context);
	}

	private void init(Context context) {
		initView(context);
	}

	private void initView(Context context) {
		rootView = View.inflate(context, R.layout.view_my_field_task, this);
		ll_task = (LinearLayout) rootView.findViewById(R.id.ll_task);
	}

	public void setData(List<TaskSpecwEntity> list) {

		ll_task.removeAllViews();
		
		Collections.sort(list);

		for (final TaskSpecwEntity taskSpecwEntity : list) {

			View item = View.inflate(context, R.layout.item_task_view, null);

			ImageView iv_pic = (ImageView) item.findViewById(R.id.iv_pic);
			TextView tv_title = (TextView) item.findViewById(R.id.tv_title);
			TextView tv_content = (TextView) item.findViewById(R.id.tv_content);
			String picUrl = taskSpecwEntity.steps.get(0).thumbnailPicture.split(",")[0];
			if (!TextUtils.isEmpty(picUrl)) {
				BitmapUtils bitmapUtils = new BitmapUtils(context);
				// TODO TASK 加载图片
				bitmapUtils.display(iv_pic, "http://120.26.208.198:8080/nongshi/" + picUrl+".jpg");
			}
			tv_title.setText(taskSpecwEntity.taskSpecName);
			tv_content.setText(taskSpecwEntity.steps.get(0).description);

			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.setMargins(0, GraphicUtils.dip2px(context, 20), 0, 0);
			item.setLayoutParams(params);
			ll_task.addView(item);

			item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getContext(), TaskDetailsActivity.class);
					intent.putExtra(TaskDetailsActivity.TASK_ID, taskSpecwEntity.taskSpecId);
					intent.putExtra(TaskDetailsActivity.TASK_TITLE, taskSpecwEntity.taskSpecName);
					getContext().startActivity(intent);
				}
			});
		}
	}
}
