package com.dasinong.farmerclub.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;

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
	private String appFileDir;
	private String userId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_product_result);

		boxcode = getIntent().getStringExtra("boxcode");
		
		appFileDir = getFilesDir().getAbsolutePath();
		userId = SharedPreferencesHelper.getString(this, Field.USER_ID, ""); 

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
		RequestService.getInstance().getWinsafeProductInfo(this, boxcode, true, ScanProductEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					ScanProductEntity entity = (ScanProductEntity) resultData;
					if (entity.data != null) {
						entity.data.count = "1";
						setData(entity);
					}
				} else {
					setData();
					writerFile(boxcode);
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				setData();
				writerFile(boxcode);
			}
		});
	}
	
	private void writerFile(String resultString) {
		File dir = new File(appFileDir + File.separator + userId);
		if (!dir.exists()) {
			dir.mkdir();
		}

		String fileName = getFileName();
		writFileData(fileName, resultString);
	}
	
	private String getFileName() {
		String fileName = null;
		// 判断当前日期是否存在文件
		File dir = new File(appFileDir + File.separator + userId);
		String currentDay = new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis()));
		if (dir.isDirectory() && dir.listFiles().length > 0) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (currentDay.equals(file.getName().substring(0, 8))) {
					fileName = file.getName();
				}
			}
		}
		if (TextUtils.isEmpty(fileName)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			fileName = sdf.format(new Date(System.currentTimeMillis())) + ".txt";
		}
		return fileName;
	}
	
	private void writFileData(String fileName, String resultString) {
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()));
		String text = "*****" + currentTime + resultString + "7" + System.getProperty("line.separator");
		File currentFile = new File(appFileDir + File.separator + userId + File.separator + fileName);
		boolean isSuccess = false;
		try {
			
			if(!currentFile.exists()){
				currentFile.createNewFile();
			}
			
			FileReader reader = new FileReader(currentFile);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			while (line != null) {
				if(line.contains(resultString)){
					showToast("已扫过的二维码");
					System.out.println("此处执行");
					return;
				}
				line = br.readLine();
			}
			br.close();
			reader.close();
			
			FileOutputStream fos = new FileOutputStream(currentFile, true);
			byte[] bytes = text.getBytes();
			fos.write(bytes);
			fos.flush();
			fos.close();
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		}
		 if(isSuccess){
			 showToast("网络异常，二维码信息已存储至本地");
		 } else {
			 showToast("扫描失败，请重新扫描");
		 }
	}

//	private void writeFailedFile() {
//		File dir = new File(getFilesDir().getAbsolutePath() + File.separator + "failed");
//		if (!dir.exists()) {
//			dir.mkdir();
//		}
//		String fileName = new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis())) + ".txt";
//		File file = new File(dir, fileName);
//		try {
//			FileWriter fw = new FileWriter(file, true);
//			BufferedWriter bw = new BufferedWriter(fw);
//			bw.write(boxcode);
//			bw.newLine();
//			bw.close();
//			fw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

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
