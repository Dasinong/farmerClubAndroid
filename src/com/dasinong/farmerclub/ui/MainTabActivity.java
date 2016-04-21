package com.dasinong.farmerclub.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.LocationResult;
import com.dasinong.farmerclub.entity.LoginRegEntity;
import com.dasinong.farmerclub.net.NetRequest;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.fragment.CouponFragment;
import com.dasinong.farmerclub.ui.fragment.EncyclopediaFragment;
import com.dasinong.farmerclub.ui.fragment.HomeFragment;
import com.dasinong.farmerclub.ui.fragment.MeFragment;
import com.dasinong.farmerclub.ui.fragment.MyFieldFragment;
import com.dasinong.farmerclub.ui.manager.AccountManager;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.utils.AppInfoUtils;
import com.dasinong.farmerclub.utils.LocationUtils;
import com.dasinong.farmerclub.utils.Logger;
import com.dasinong.farmerclub.utils.LocationUtils.LocationListener;
import com.lidroid.xutils.db.annotation.Table;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UpdateStatus;
/**
 * @ClassName MainTabActivity
 * @author ysl
 * @Description
 */
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

/**
 * @ClassName MainTabActivity
 * @author ysl
 * @Description
 */
public class MainTabActivity extends BaseActivity implements OnClickListener {

	public static final String TARGET_TAB = "tagTab";

	private FragmentTabHost mTabHost;

	private LayoutInflater layoutInflater;

	public static Activity activity;

	// private Class fragmentArray[] = { HomeFragment.class,
	// MyFieldFragment.class, CouponFragment.class, EncyclopediaFragment.class,
	// MeFragment.class };
	//
	// private int mImageViewArray[] = {
	// R.drawable.main_tab1_selector,R.drawable.main_tab2_selector,R.drawable.main_tab3_selector,
	// R.drawable.main_tab4_selector, R.drawable.main_tab5_selector };
	//
	// private String mTextviewArray[] = { "天气","我的田","福利社", "农事百科", "我" };

	private List<Class> fragmentList = new ArrayList<>();
	private List<Integer> mImageViewList = new ArrayList<>();
	private List<String> mTextViewList = new ArrayList<>();

	private int index;

	public static boolean isMustUpdate = false;

	private boolean isFirstInTo = true;

	private LinearLayout ll_front;

	private ImageView iv_weather;

	private ImageView iv_product;

	private ImageView iv_daren;

	private ImageView iv_scan;

	private boolean isRetailer;

	private String userId;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tab_layout);

		activity = this;

		initList();

		userId = SharedPreferencesHelper.getString(this, Field.USER_ID, "");
		checkAndUploadLog();
		
		String userType = SharedPreferencesHelper.getString(this, Field.USER_TYPE, SelectUserTypeActivity.FARMER);
		int institutionId = SharedPreferencesHelper.getInt(this, Field.INSTITUTIONID, -1);
		boolean isDaren = SharedPreferencesHelper.getBoolean(this, Field.ISDAREN, false);
		boolean enableWelfare = SharedPreferencesHelper.getBoolean(this, Field.ENABLEWELFARE, false);

		// TODO  临时解决方案
		if (enableWelfare || institutionId == 100) {
			fragmentList.add(2, CouponFragment.class);
			mImageViewList.add(2, R.drawable.main_tab3_selector);
			mTextViewList.add(2, "活动");
			if (SelectUserTypeActivity.RETAILER.equals(userType)) {
				isRetailer = true;
				mImageViewList.set(2, R.drawable.main_tab6_selector);
				mTextViewList.set(2, "店铺");
			}
		}
		initData();
		initView();
		initLocation();

		// 友盟更新
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false);

		MobclickAgent.updateOnlineConfig(this);

		// 强制更新代码
		String minVersion = MobclickAgent.getConfigParams(this, "minVersion");

		int versionCode = AppInfoUtils.getVersionCode(this);
		if (!TextUtils.isEmpty(minVersion)) {

			if (versionCode < Integer.valueOf(minVersion)) {
				isMustUpdate = true;
			}

		}

		if (isMustUpdate) {
			UmengUpdateAgent.forceUpdate(this);
		}

		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {

			@Override
			public void onClick(int status) {
				if (isMustUpdate) {
					MainTabActivity.this.finish();
				}
			}
		});

		// Umeng 消息推送
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		// 获取用户唯一标示
		String device_token = UmengRegistrar.getRegistrationId(this);

	}

	private void checkAndUploadLog() {
		List<File> fileList = new ArrayList<>();
		String appFileDir = getFilesDir().getAbsolutePath();
		File dir = new File(appFileDir + File.separator + userId);
		String currentDay = new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis()));
		int intCurrentDay = Integer.valueOf(currentDay);
		if (dir.exists() && dir.isDirectory() && dir.listFiles().length > 0) {
			File[] files = dir.listFiles();
			for (File file : files) {
				int fileDay = Integer.valueOf(file.getName().substring(0, 8));
				if (fileDay < intCurrentDay) {
					fileList.add(file);
				}
			}
		}
		for (File file : fileList) {
			System.out.println(file.getAbsolutePath());
		}
		
		if (!fileList.isEmpty()) {
			for (final File file : fileList) {
				RequestService.getInstance().uploadLog(this, file, BaseEntity.class, new RequestListener() {
					
					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if(resultData.isOk()){
							file.delete();
							System.out.println("上传成功 +++++  " + file.getName());
						} else {
							System.out.println("上传失败  ++++++ " + file.getName());
						}
					}
					
					@Override
					public void onFailed(int requestCode, Exception error, String msg) {
						System.out.println("上传失败  ++++++ " + file.getName());
					}
				});
			}
		}
	}

	private void initList() {
		fragmentList.add(HomeFragment.class);
		fragmentList.add(MyFieldFragment.class);
		fragmentList.add(EncyclopediaFragment.class);
		fragmentList.add(MeFragment.class);

		mImageViewList.add(R.drawable.main_tab1_selector);
		mImageViewList.add(R.drawable.main_tab2_selector);
		mImageViewList.add(R.drawable.main_tab4_selector);
		mImageViewList.add(R.drawable.main_tab5_selector);

		mTextViewList.add("天气");
		mTextViewList.add("我的田");
		mTextViewList.add("农事百科");
		mTextViewList.add("我");
	}

	private void login() {
		if (AccountManager.isLogin(MainTabActivity.this)) {
			return;
		}
		int appInstitutionId = AppInfoUtils.getInstitutionId(this);
		RequestService.getInstance().authcodeLoginReg(MainTabActivity.this, "13112345678", "", appInstitutionId + "", LoginRegEntity.class,
				new NetRequest.RequestListener() {

					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {

						if (resultData.isOk()) {
							LoginRegEntity entity = (LoginRegEntity) resultData;
							AccountManager.saveAccount(MainTabActivity.this, entity);
							showToast("登录成功");
						} else {
							Logger.d("TAG", resultData.getMessage());
						}
					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {
						Logger.d("TAG", "msg" + msg);
					}
				});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			index = intent.getIntExtra("index", 0);
			if (index != 0) {
				mTabHost.setCurrentTab(index);
			}
		}
	}

	protected void initData() {
		index = getIntent().getIntExtra("index", 0);
	}

	protected void initView() {
		layoutInflater = LayoutInflater.from(this);

		ll_front = (LinearLayout) findViewById(R.id.ll_front);

		iv_weather = (ImageView) findViewById(R.id.iv_weather);
		iv_product = (ImageView) findViewById(R.id.iv_product);
		iv_daren = (ImageView) findViewById(R.id.iv_daren);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);

		if (isRetailer) {
			iv_scan.setBackgroundResource(R.drawable.button4_2);
		}

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		mTabHost.getTabWidget().setDividerDrawable(null);

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				int institutionId = SharedPreferencesHelper.getInt(MainTabActivity.this, Field.INSTITUTIONID, 0);
				if (isFirstInTo && "天气".equals(tabId) && institutionId == 3) {
					ll_front.setVisibility(View.VISIBLE);
					isFirstInTo = false;
				} else {
					if (ll_front.getVisibility() == View.VISIBLE || mTabHost.getVisibility() == View.GONE) {
						ll_front.setVisibility(View.GONE);
						mTabHost.setVisibility(View.VISIBLE);
					}
				}
			}
		});

		int count = fragmentList.size();

		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = mTabHost.newTabSpec(mTextViewList.get(i)).setIndicator(getTabItemView(i));
			mTabHost.addTab(tabSpec, fragmentList.get(i), null);
		}

		if (index != 0) {
			mTabHost.setCurrentTab(index);
		}

		iv_weather.setOnClickListener(this);
		iv_product.setOnClickListener(this);
		iv_daren.setOnClickListener(this);
		iv_scan.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		ll_front.setVisibility(View.GONE);
		mTabHost.setVisibility(View.VISIBLE);
		isFirstInTo = false;

		switch (v.getId()) {
		case R.id.iv_product:
			mTabHost.setCurrentTab(1);
			break;
		case R.id.iv_daren:
			mTabHost.setCurrentTab(2);
			break;
		case R.id.iv_scan:
			Intent scanIntent = new Intent(this, CaptureActivity.class);
			startActivity(scanIntent);
			break;
		}
	}

	private View getTabItemView(final int index) {
		View view = layoutInflater.inflate(R.layout.view_main_tab_item, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewList.get(index));

		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextViewList.get(index));

		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		LocationUtils.getInstance().unRegisterLocationListener();
		super.onStop();
	}

	private void initLocation() {
		LocationUtils.getInstance().registerLocationListener(new LocationListener() {

			@Override
			public void locationNotify(LocationResult result) {
				SharedPreferencesHelper.setString(MainTabActivity.this, Field.CURRENT_LAT, result.getLatitude() + "");
				
				SharedPreferencesHelper.setString(MainTabActivity.this, Field.CURRENT_LON, result.getLongitude() + "");
				// Toast.makeText(MainTabActivity.this,
				// result.getCity()+" -- "+result.getStreet(), 0).show();
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (ll_front.getVisibility() == View.VISIBLE) {
			ll_front.setVisibility(View.GONE);
			mTabHost.setVisibility(View.VISIBLE);
			return;
		}
		super.onBackPressed();
	}
}
