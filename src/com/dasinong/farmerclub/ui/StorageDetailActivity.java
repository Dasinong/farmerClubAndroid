package com.dasinong.farmerclub.ui;

import java.io.File;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;

public class StorageDetailActivity extends BaseActivity {
	private int id;
	private String name;
	private String volume;
	private int count;
	private TopbarView topBar;
	private TextView tv_name;
	private TextView tv_volume;
	private TextView tv_count;
	private String userId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storage_detail);

		userId = SharedPreferencesHelper.getString(this, Field.USER_ID, "");

		id = getIntent().getIntExtra("id", 0);
		name = getIntent().getStringExtra("name");
		volume = getIntent().getStringExtra("volume");
		count = getIntent().getIntExtra("count", 0);

		initView();
		
		intiTopBar();

		checkLocalFile();

		setData();
	}


	private void setData() {
		tv_name.setText(name);
		tv_volume.setText(volume);
		tv_count.setText(count + "");
	}

	private void initView() {
		topBar = (TopbarView) findViewById(R.id.topbar);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_volume = (TextView) findViewById(R.id.tv_volume);
		tv_count = (TextView) findViewById(R.id.tv_count);
	}
	
	private void intiTopBar() {
		topBar.setCenterText("入库管理");
		topBar.setLeftView(true, true);
	}
	

	private void checkLocalFile() {
		String appFileDir = getFilesDir().getAbsolutePath();
		File dir = new File(appFileDir + File.separator + userId);
		if (dir.exists() && dir.isDirectory() && dir.listFiles().length > 0) {
			showNotifyDialog();
		}
	}

	private void showNotifyDialog() {
		final Dialog dialog = new Dialog(StorageDetailActivity.this, R.style.CommonDialog);
		dialog.setContentView(R.layout.smssdk_back_verify_dialog);
		TextView tv = (TextView) dialog.findViewById(R.id.tv_dialog_hint);
		tv.setText("有未上传的本地数据，当前计数仅供参考");
		tv.setTextSize(18);
		Button waitBtn = (Button) dialog.findViewById(R.id.btn_dialog_ok);
		waitBtn.setText("知道了");
		waitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		View lineView = dialog.findViewById(R.id.view_line);
		lineView.setVisibility(View.GONE);
		Button backBtn = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
		backBtn.setVisibility(View.GONE);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
}
