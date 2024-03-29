package com.dasinong.farmerclub.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.umeng.analytics.MobclickAgent;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class AddFieldActivity6 extends MyBaseActivity implements OnClickListener {
	private Button btn_no_date;
	private Button btn_sure_date;
	private TopbarView topbar;
	private TextView tv_select_date;
	private int myear;
	private int mmonth;
	private int mday;
	private Calendar calendar;
	private TextView tv_prompt;
	private String seedingMethod;
	private String strTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_6);

		tv_prompt = (TextView) findViewById(R.id.tv_prompt);
		btn_no_date = (Button) findViewById(R.id.btn_no_date);
		btn_sure_date = (Button) findViewById(R.id.btn_sure_date);
		topbar = (TopbarView) findViewById(R.id.topbar);
		tv_select_date = (TextView) findViewById(R.id.tv_select_date);
		
		seedingMethod = SharedPreferencesHelper.getString(this, Field.SEEDING_METHOD, "false");
		
		if("false".equals(seedingMethod)){
			tv_prompt.setText("哪天移栽的？");
		} else {
			tv_prompt.setText("哪天播种的？");
		}

		initTopBar();

		btn_no_date.setOnClickListener(this);
		btn_sure_date.setOnClickListener(this);
		tv_select_date.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		switch (id) {
		case R.id.tv_select_date:
			showDatePickerDialog();
			break;
		case R.id.btn_no_date:
			MobclickAgent.onEvent(this, "AddFieldSeventh");
			strTime = String.valueOf(System.currentTimeMillis());
			goToNext();
			break;
		case R.id.btn_sure_date:
			MobclickAgent.onEvent(this, "AddFieldSeventh");
			String date = myear + "-" + mmonth + "-" + mday;
			DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			Date userDate = null;
			Date currentDate;
			try {
				userDate = dateformat.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			currentDate = new Date();
			if (myear == 0 && mmonth == 0 && mday == 0) {
				showToast("请选择时间");
				return;
			} else {
				if (currentDate.after(userDate)) {
					long time = userDate.getTime();
					strTime = String.valueOf(time);
				} else {
					showToast("请选择正确的时间");
					return;
				}
			}
			goToNext();
			break;
		}
	}

	private void initTopBar() {
		topbar.setCenterText("种植周期");
		topbar.setLeftView(true, true);
	}

	private void showDatePickerDialog() {
		int year, month, day;
		calendar = Calendar.getInstance();
		if (myear != 0 && mday != 0) {
			year = myear;
			month = mmonth;
			day = mday;
		} else {
			year = calendar.get(Calendar.YEAR);
			month = 0;
			day = 1;
		}

		DatePickerDialog dateDialog = new DatePickerDialog(this, new MyOnDateSetListener(), year, month, day);
		dateDialog.show();
	}

	class MyOnDateSetListener implements OnDateSetListener {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			myear = year;
			mmonth = monthOfYear;
			mday = dayOfMonth;
			monthOfYear = monthOfYear + 1;
			tv_select_date.setText(year + "年" + monthOfYear + "月" + dayOfMonth + "日");
		}
	}

	private void goToNext() {
		
		SharedPreferencesHelper.setString(this, Field.PLANTING_DATE, strTime);
		Intent intent = new Intent(this, AddFieldActivity7.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}
}
