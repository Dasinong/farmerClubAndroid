package com.dasinong.farmerclub.ui.view;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.database.city.dao.impl.CityDaoImpl;
import com.dasinong.farmerclub.entity.VillageInfo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class SelectAreaView extends LinearLayout {

	private Context context;
	protected CityDaoImpl dao;
	protected List<String> province;
	private Spinner mProvinceSp;
	private Spinner mCitySp;
	private Spinner mAreaSp;
	private Spinner mTownsSp;
	private Spinner mVillageSp;
	private Map<String, String> villageMap;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			setProvince();
		}
	};
	private OnGetVillagesListener mOnGetVillagesListener;
	private View view;

	public SelectAreaView(Context context) {
		this(context, null);
	}

	public SelectAreaView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SelectAreaView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		initView(context);
		initProvince();
	}

	public void setOnGetVillageListener(OnGetVillagesListener onGetVillagesListener) {
		this.mOnGetVillagesListener = onGetVillagesListener;
	}

	private void initView(Context context) {
		view = View.inflate(context, R.layout.view_select_area, this);
		mProvinceSp = (Spinner) view.findViewById(R.id.spinner_province);
		mCitySp = (Spinner) view.findViewById(R.id.spinner_city);
		mAreaSp = (Spinner) view.findViewById(R.id.spinner_area);
		mTownsSp = (Spinner) view.findViewById(R.id.spinner_towns);
		mVillageSp = (Spinner) view.findViewById(R.id.spinner_village);
	}

	private void initProvince() {
		new Thread() {
			public void run() {
				dao = new CityDaoImpl(context);
				province = dao.getProvince();
				province.add(0, "请选择省");
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	private void setProvince() {
		mProvinceSp.setAdapter(new ArrayAdapter<String>(context, R.layout.textview, province));
		mProvinceSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String province = (String) mProvinceSp.getSelectedItem();
				setCity(province);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	protected void setCity(final String province2) {
		List<String> city = dao.getCity(province2);
		city.add(0, "请选择市");
		mCitySp.setAdapter(new ArrayAdapter<String>(context, R.layout.textview, city));
		mCitySp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String city = (String) mCitySp.getSelectedItem();
				setArea(province2, city);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	protected void setArea(final String province2, final String city) {
		List<String> county = dao.getCounty(province2, city);
		county.add(0, "请选择区");
		mAreaSp.setAdapter(new ArrayAdapter<String>(context, R.layout.textview, county));
		mAreaSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String area = (String) mAreaSp.getSelectedItem();
				setTowns(province2, city, area);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	protected void setTowns(final String province2, final String city, final String area) {
		List<String> county = dao.getDistrict(province2, city, area);
		county.add(0, "请选择镇");
		mTownsSp.setAdapter(new ArrayAdapter<String>(context, R.layout.textview, county));
		mTownsSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String town = (String) mTownsSp.getSelectedItem();
				initVillage(province2, city, area, town);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	protected void initVillage(String provices2, String city, String area, String town) {
		if (mOnGetVillagesListener != null) {
			mOnGetVillagesListener.onGetVillages(provices2, city, area, town);
		} else {
			setVillages(new ArrayList<String>());
		}
	}

	public void setVillages(Map<String, String> villageMap) {
		this.villageMap = villageMap;
		MyComparator mComparator = new MyComparator();
		List<String> villageList = new ArrayList<String>(villageMap.keySet());
		Collections.sort(villageList, mComparator);
		setVillages(villageList);
	}

	public void setVillages(List<String> village) {
		village.add(0, "请选择村");
		if (village != null && village.size() > 0) {
			mVillageSp.setAdapter(new ArrayAdapter<String>(context, R.layout.textview, village));
		} else {
			mVillageSp.setVisibility(View.GONE);
		}
	}

	public String getVillageId() {
		if (mVillageSp.getSelectedItemPosition() == 0) {
			return "";
		} else {
			return villageMap.get((String) mVillageSp.getSelectedItem());
		}
	}
	
	public String getAddress(){
		if (mProvinceSp.getSelectedItemPosition() == 0) {
			Toast.makeText(context, "请选择省", Toast.LENGTH_SHORT).show();
			return null;
		}
		if (mCitySp.getSelectedItemPosition() == 0) {
			Toast.makeText(context, "请选择市", Toast.LENGTH_SHORT).show();
			return null;
		}
		if (mAreaSp.getSelectedItemPosition() == 0) {
			Toast.makeText(context, "请选择区", Toast.LENGTH_SHORT).show();
			return null;
		}
		if (mTownsSp.getSelectedItemPosition() == 0) {
			Toast.makeText(context, "请选择镇", Toast.LENGTH_SHORT).show();
			return null;
		}
		if(mVillageSp.getVisibility() == View.VISIBLE){
			if (mVillageSp.getSelectedItemPosition() == 0){
				Toast.makeText(context, "请选择村", Toast.LENGTH_SHORT).show();
				return null;
			}
		}
		String province = (String) mProvinceSp.getSelectedItem();
		String city = (String) mCitySp.getSelectedItem();
		String area = (String) mAreaSp.getSelectedItem();
		String towns = (String) mTownsSp.getSelectedItem();
		String village = (String) mVillageSp.getSelectedItem();

		return province + " " + city + " " + area + " " + towns + " " + village;
	}

	public interface OnGetVillagesListener {
		public void onGetVillages(String provices2, String city, String area, String town);
	}

	class MyComparator implements Comparator<String> {
		Collator cmp = Collator.getInstance(java.util.Locale.CHINA);

		@Override
		public int compare(String str1, String str2) {
			if (cmp.compare(str1, str2) > 0) {
				return 1;
			} else if (cmp.compare(str1, str2) < 0) {
				return -1;
			}
			return 0;
		}
	}

}
