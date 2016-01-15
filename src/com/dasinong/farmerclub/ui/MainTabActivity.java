package com.dasinong.farmerclub.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.LocationResult;
import com.dasinong.farmerclub.entity.LoginRegEntity;
import com.dasinong.farmerclub.net.NetRequest;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.fragment.CouponFragment;
import com.dasinong.farmerclub.ui.fragment.EncyclopediaFragment;
import com.dasinong.farmerclub.ui.fragment.HomeFragment;
import com.dasinong.farmerclub.ui.fragment.MeFragment;
import com.dasinong.farmerclub.ui.fragment.MyFieldFragment;
import com.dasinong.farmerclub.ui.manager.AccountManager;
import com.dasinong.farmerclub.utils.AppInfoUtils;
import com.dasinong.farmerclub.utils.LocationUtils;
import com.dasinong.farmerclub.utils.Logger;
import com.dasinong.farmerclub.utils.LocationUtils.LocationListener;
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
public class MainTabActivity extends BaseActivity {

	public static final String TARGET_TAB = "tagTab";

	private FragmentTabHost mTabHost;

	private LayoutInflater layoutInflater;

	private Class fragmentArray[] = { HomeFragment.class, MyFieldFragment.class, CouponFragment.class, EncyclopediaFragment.class, MeFragment.class };

	private int mImageViewArray[] = { R.drawable.main_tab1_selector,R.drawable.main_tab2_selector,R.drawable.main_tab3_selector, R.drawable.main_tab4_selector, R.drawable.main_tab5_selector };

	private String mTextviewArray[] = { "天气","我的田","福利社", "农事百科", "我" };
	
//	private Class fragmentArray[] = { HomeFragment.class, MyFieldFragment.class, EncyclopediaFragment.class, MeFragment.class };
//
//	private int mImageViewArray[] = { R.drawable.main_tab1_selector, R.drawable.main_tab2_selector, R.drawable.main_tab4_selector, R.drawable.main_tab5_selector };
//
//	private String mTextviewArray[] = { "天气","我的田", "农事百科", "我" };

	private int index;

	public static boolean isMustUpdate = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tab_layout);
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

		// startLoadingDialog();

		// if(getIntent() != null){
		// index = getIntent().getIntExtra("index", 0);
		// if(index != 0){
		// mTabHost.setCurrentTab(index);
		// }
		// }
	}

	private void login() {
		if (AccountManager.isLogin(MainTabActivity.this)) {
			return;
		}
		int appInstitutionId = AppInfoUtils.getInstitutionId(this);
		RequestService.getInstance().authcodeLoginReg(MainTabActivity.this, "13112345678", "", appInstitutionId+"", LoginRegEntity.class,
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

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		mTabHost.getTabWidget().setDividerDrawable(null);

		int count = fragmentArray.length;

		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
		}

		if (index != 0) {
			mTabHost.setCurrentTab(index);
		}
	}

	private View getTabItemView(int index) {
		View view = layoutInflater.inflate(R.layout.view_main_tab_item, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);

		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextviewArray[index]);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		// int targetTab = getIntent().getIntExtra(TARGET_TAB, -1);
		//
		// System.out.println("targetTab  " + targetTab);
		//
		// if(targetTab > 0){
		// mTabHost.setCurrentTab(targetTab);
		// }
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

				// Toast.makeText(MainTabActivity.this,
				// result.getCity()+" -- "+result.getStreet(), 0).show();
			}
		});
	}

}
