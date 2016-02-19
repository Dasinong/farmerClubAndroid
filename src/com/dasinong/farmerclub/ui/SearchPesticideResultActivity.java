package com.dasinong.farmerclub.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.database.encyclopedias.CpproductbrowseDao;
import com.dasinong.farmerclub.database.encyclopedias.domain.Cpproductbrowse;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.PesticideListEntity;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.adapter.DiseaseListAdapter;
import com.dasinong.farmerclub.ui.adapter.PesticideListAdapter;
import com.dasinong.farmerclub.ui.view.LetterView;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.ui.view.LetterView.OnTouchingLetterChangedListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchPesticideResultActivity extends BaseActivity {

	private TopbarView mTopbarView;
	
//	private String type;

	private Handler mHandler = new Handler();
	
	private ListView mListView;
	
	private LetterView letterView;
	private HashMap<String, Integer> alphaIndexer;
	private OverlayThread mOverlayThread;
	private WindowManager mWindowManager;
	private TextView mOverlay;
//	private String manufacturer;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disease_list);
		
		String type = getIntent().getStringExtra("type");
		String manufacturer = getIntent().getStringExtra("manufacturer");
		
		initView();
		setUpView(type);
		initOverlay();
		if(TextUtils.isEmpty(manufacturer)){
			initData(type);
			requestData(type);
		} else {
			requestData(type, manufacturer);
		}
	}

	private void initData(final String type) {
		
		
		new Thread(){
			public void run() {
				CpproductbrowseDao dao = new CpproductbrowseDao(SearchPesticideResultActivity.this);
				final List<Cpproductbrowse> query = dao.query(type);
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						setAdapter(query);
					}
				});
			};
			
		}.start();
	}

	protected void setAdapter(List<Cpproductbrowse> query) {
		query = sortContact(query);

		alphaIndexer.clear();
		for (int i = 0; i < query.size(); i++) {
			Cpproductbrowse entity = query.get(i);
			if (entity.isTitle) {
				alphaIndexer.put(entity.title, i);
			}
		}
		
		PesticideListAdapter adapter = new PesticideListAdapter(this, query, false);
		mListView.setAdapter(adapter);
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mListView = (ListView) this.findViewById(R.id.list_sms_list);
		
		letterView = (LetterView) findViewById(R.id.letterview);
		letterView.setOnTouchingLetterChangedListener(new letterViewListener());
		letterView.setVisibility(View.VISIBLE);
		alphaIndexer = new HashMap<String, Integer>();
		mOverlayThread = new OverlayThread();
	}

	private void setUpView(String type) {
		if("专业解决方案 有害生物控制".equals(type)){
			type = "公共卫生";
		}
		mTopbarView.setCenterText(type);
		mTopbarView.setLeftView(true, true);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Cpproductbrowse item = (Cpproductbrowse) parent.getItemAtPosition(position);
				Intent intent = new Intent(SearchPesticideResultActivity.this, SearchPesticideNamedResultActivity.class);
				intent.putExtra("type", item.activeIngredient);
				startActivity(intent);
			}
		});
	}

	private void requestData(String type) {
		startLoadingDialog();
		RequestService.getInstance().browseCPProductByModel(this, type, PesticideListEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if(resultData.isOk()){
					PesticideListEntity entity = (PesticideListEntity) resultData;
					setAdapter(entity.getData());
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
	
	private void requestData(String type, String manufacturer) {
		startLoadingDialog();
		RequestService.getInstance().browseCPProductByModelAndManufacturer(this, type, manufacturer, PesticideListEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if(resultData.isOk()){
					PesticideListEntity entity = (PesticideListEntity) resultData;
					setAdapter(entity.getData());
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
	
	private void initOverlay() {
		mHandler = new Handler();
		LayoutInflater inflater = LayoutInflater.from(this);
		mOverlay = (TextView) inflater.inflate(R.layout.contacts_overlay, null);
		mOverlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT);
		mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.addView(mOverlay, lp);
	}

	@Override
	protected void onDestroy() {
		if (mWindowManager != null) {
			mWindowManager.removeView(mOverlay);
			mWindowManager = null;
		}
		super.onDestroy();
	}

	@Override
	public void finish() {
		try {
			if (mWindowManager != null) {
				mWindowManager.removeView(mOverlay);
				mWindowManager = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.finish();
	}

	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			mOverlay.setVisibility(View.GONE);
		}
	}

	private class letterViewListener implements OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				mListView.setSelection(position);
				mOverlay.setText(s);
				mOverlay.setVisibility(View.VISIBLE);
				mHandler.removeCallbacks(mOverlayThread);
				mHandler.postDelayed(mOverlayThread, 1500);
			}
		}

	}
	
	public static List<Cpproductbrowse> sortContact(List<Cpproductbrowse> data) {
		if (data == null) {
			return null;
		}

		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		Collections.sort(data, new Comparator<Cpproductbrowse>() {
			@Override
			public int compare(Cpproductbrowse lhs, Cpproductbrowse rhs) {

				int lhsascll = lhs.activeIngredientPY.charAt(0);
				int rhsascll = rhs.activeIngredientPY.charAt(0);

				if (lhsascll < 97 || lhsascll > 122) {
					lhs.activeIngredientPY = "~";
				}

				if (rhsascll < 97 || rhsascll > 122) {
					rhs.activeIngredientPY = "~";
				}

				return lhs.activeIngredientPY.compareTo(rhs.activeIngredientPY);
			}
		});

		char mc = '*';
//		List<Cpproductbrowse> newData = new ArrayList<Cpproductbrowse>();
		for (int i = 0; i < data.size(); i++) {
			Cpproductbrowse friend = data.get(i);
			friend.isTitle = false;
			char c = getFirstPy(friend.activeIngredientPY);

			if (c < 65 || c > 90) {
				c = '#';
			}

			if (mc != c) {
				mc = c;
//				Cpproductbrowse f = new Cpproductbrowse();
//				f.isTitle = true;
//				newData.add(f);
				
				friend.isTitle = true;
				friend.title = String.valueOf(c);
			}
//			newData.add(friend);
		}
		
		return data;
	}
	private static char getFirstPy(String pinyin) {
		if(null == pinyin || pinyin.length() == 0){
			return '#';
		}else{
			return Character.toUpperCase(pinyin.charAt(0));
		}
	}
	
}
