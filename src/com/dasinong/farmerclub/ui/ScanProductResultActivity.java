package com.dasinong.farmerclub.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.ScanProductEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;

import android.R.fraction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ScanProductResultActivity extends BaseActivity implements OnClickListener {
	private TextView tv_box_code;
	private TextView tv_name;
	private TextView tv_number;
	private TextView tv_volume;
	private TextView tv_count;
	private Button btn_finish;
	private Button btn_continue;

	private String boxcode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_product_result);

		boxcode = getIntent().getStringExtra("boxcode");

		initView();

		queryData();
	}

	private void initView() {
		tv_box_code = (TextView) findViewById(R.id.tv_box_code);

		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_number = (TextView) findViewById(R.id.tv_number);
		tv_volume = (TextView) findViewById(R.id.tv_volume);
		tv_count = (TextView) findViewById(R.id.tv_count);

		btn_finish = (Button) findViewById(R.id.btn_finish);
		btn_continue = (Button) findViewById(R.id.btn_continue);

		tv_box_code.setText(boxcode);

		btn_finish.setOnClickListener(this);
		btn_continue.setOnClickListener(this);
	}

	private void queryData() {
		RequestService.getInstance().getWinsafeProductInfo(this, boxcode, ScanProductEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					ScanProductEntity entity = (ScanProductEntity) resultData;
					if (entity.data != null) {

						String spName = new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis())) + entity.data.proid;

						int count = SharedPreferencesHelper.getInt(ScanProductResultActivity.this, spName, 0);

						SharedPreferencesHelper.setInt(ScanProductResultActivity.this, spName, count + 1);

						entity.data.count = String.valueOf(count + 1);
						setData(entity);
					}
				} else {
					setData();
					writeFailedFile();
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				setData();
				writeFailedFile();
			}
		});
	}

	private void writeFailedFile() {
		File dir = new File(getFilesDir().getAbsolutePath() + File.separator + "failed");
		if (!dir.exists()) {
			dir.mkdir();
		}
		String fileName = new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis())) + ".txt";
		File file = new File(dir, fileName);
		try {
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(boxcode);
			bw.newLine();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setData() {
		setDataToView("", tv_name);
		setDataToView("", tv_number);
		setDataToView("", tv_volume);
		setDataToView("", tv_count);
	}

	private void setData(ScanProductEntity entity) {
		setDataToView(entity.data.proname, tv_name);
		setDataToView(entity.data.proid, tv_number);
		setDataToView(entity.data.prospecial, tv_volume);
		setDataToView(entity.data.count, tv_count);
	}

	private void setDataToView(String text, TextView view) {
		if (TextUtils.isEmpty(text)) {
			((View) view.getParent()).setVisibility(View.INVISIBLE);
		} else {
			view.setText(text);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_finish:
			this.finish();
			break;
		case R.id.btn_continue:
			Intent intent = new Intent(this, CaptureActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}
}
