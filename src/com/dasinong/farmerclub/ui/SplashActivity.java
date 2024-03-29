package com.dasinong.farmerclub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.LoginRegEntity;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.manager.AccountManager;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.utils.AppInfoUtils;
import com.umeng.message.PushAgent;

public class SplashActivity extends BaseActivity {
	private TextView tv_version;
	private ImageView splash_iv;
	private int serverInstitutionId;
	private int appInstitutionId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		tv_version = (TextView) findViewById(R.id.tv_version);
		splash_iv = (ImageView) findViewById(R.id.splash_iv);

		autoLogin();

		String versionName = AppInfoUtils.getVersionName(this);
		serverInstitutionId = SharedPreferencesHelper.getInt(this, Field.INSTITUTIONID, 0);
		appInstitutionId = AppInfoUtils.getInstitutionId(this);

		if (appInstitutionId == 0 && serverInstitutionId == 0) {
			setUserTag("普通");
		} else if (appInstitutionId == 1 || serverInstitutionId == 1) {
			splash_iv.setImageResource(R.drawable.splash_image_taoshi_club);
			setUserTag("陶氏");
		} else if (appInstitutionId == 3 || serverInstitutionId == 3) {
			splash_iv.setImageResource(R.drawable.splash_image_basf_club);
			setUserTag("巴斯夫");
		} else if (appInstitutionId == 4 || serverInstitutionId == 4){
			splash_iv.setImageResource(R.drawable.splash_image_zn_club);
			setUserTag("中农");
		}

		if (!TextUtils.isEmpty(versionName)) {
			tv_version.setText("当前版本：" + versionName);
		} else {
			tv_version.setVisibility(View.GONE);
		}

		// 发送一个延时的消息
		new Handler() {
			public void handleMessage(android.os.Message msg) {
				// 接受到消息后的处理
				boolean isFirst = SharedPreferencesHelper.getBoolean(SplashActivity.this, Field.IS_FIRST, true);

				if (isFirst) {
					// 第一次进入应用，进入导航界面
					SharedPreferencesHelper.setBoolean(SplashActivity.this, Field.IS_FIRST, false);
					Intent intent = new Intent(SplashActivity.this, RegisterPhoneActivity.class);
					intent.putExtra("isFirst", isFirst);
					startActivity(intent);
					createShortCut();
					finish();
				} else {
					// 不是第一次进入应用,直接跳转到主界面
					if (AccountManager.isLogin(SplashActivity.this)) {
						Intent intent = new Intent(SplashActivity.this, MainTabActivity.class);
						startActivity(intent);
						finish();
					} else {
						Intent intent = new Intent(SplashActivity.this, RegisterPhoneActivity.class);
						startActivity(intent);
						finish();
					}
				}

			};
		}.sendEmptyMessageDelayed(0, 2000);
	}

	private void setUserTag(final String channelTag) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					PushAgent.getInstance(SplashActivity.this).getTagManager().add(channelTag);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	private void createShortCut() {
		// 创建添加快捷方式的Intent
		Intent addShortCut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		// 加载app名
		String title = getResources().getString(R.string.app_name);
		// 加载app的logo
		Parcelable icon = Intent.ShortcutIconResource.fromContext(SplashActivity.this, R.drawable.ic_launcher);
		// 点击快捷方式后操作Intent,快捷方式建立后，再次启动该程序
		Intent intent = new Intent(SplashActivity.this, SplashActivity.class);
		// 设置快捷方式的标题
		addShortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		// 设置快捷方式的图标
		addShortCut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		// 设置快捷方式对应的Intent
		addShortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// 发送广播添加快捷方式
		sendBroadcast(addShortCut);
	}

	private void autoLogin() {
		String phone = SharedPreferencesHelper.getString(this, Field.USER_PHONE, "");
		String channel = AppInfoUtils.getChannelCode(this);

		if (!TextUtils.isEmpty(phone)) {
			RequestService.getInstance().authcodeLoginReg(this, phone, channel, appInstitutionId + "", LoginRegEntity.class, new RequestListener() {

				@Override
				public void onSuccess(int requestCode, BaseEntity resultData) {
					if (resultData.isOk()) {
						LoginRegEntity entity = (LoginRegEntity) resultData;
						if (entity != null) {
							AccountManager.saveAccount(SplashActivity.this, entity);
						}
					}
				}

				@Override
				public void onFailed(int requestCode, Exception error, String msg) {
				}
			});
		}

	}
}
