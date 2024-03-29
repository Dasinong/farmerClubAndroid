/*
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2014年 mob.com. All rights reserved.
 */
package com.dasinong.farmerclub.ui;

import static com.mob.tools.utils.R.getBitmapRes;
import static com.mob.tools.utils.R.getIdRes;
import static com.mob.tools.utils.R.getLayoutRes;
import static com.mob.tools.utils.R.getStringRes;
import static com.mob.tools.utils.R.getStyleRes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.UserInterruptException;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.IsPassSetEntity;
import com.dasinong.farmerclub.entity.LoginRegEntity;
import com.dasinong.farmerclub.entity.QQUserInfoEntity;
import com.dasinong.farmerclub.entity.TokenEntity;
import com.dasinong.farmerclub.entity.WXAccessTokenEntity;
import com.dasinong.farmerclub.entity.WXUserInfoEntity;
import com.dasinong.farmerclub.listener.BaseUIListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetConfig.ResponseCode;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.manager.AccountManager;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.AppInfoUtils;
import com.dasinong.farmerclub.utils.DeviceHelper;
import com.dasinong.farmerclub.utils.StringHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.common.StringUtils;
import com.mob.tools.FakeActivity;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;

/** 短信注册页面 */
public class RegisterPhoneActivity extends BaseActivity implements OnClickListener, TextWatcher {

	// 默认使用中国区号
	private static final String DEFAULT_COUNTRY_ID = "42";

	// private EventHandler callback;

	private TopbarView mTopbarView;
	private EditText mPhoneEdit;
	private Button mNextButton;
	private TextView mAgreementText;

	// // 国家
	// private TextView tvCountry;
	// // 手机号码
	// private EditText etPhoneNum;
	// // 国家编号
	// private TextView tvCountryNum;
	// // clear 号码
	// private ImageView ivClear;

	// private String currentId;
	private String currentCode;
	private EventHandler handler;
	// 国家号码规则
	private HashMap<String, String> countryRules;
	private OnSendMessageHandler osmHandler;

	// private static String APPKEY = "7ddca5c23518";
	// private static String APPSECRET = "674b27af7f64b70e317f147098bc782b";
	private static String APPKEY = "eb96311cfbc4";
	private static String APPSECRET = "93d69dd2aa27c2fe211bc5907334e332";

	private String phone;
	private boolean isAuthPhone = false;

	private Button btn_qq_login;
	private Button btn_wx_login;
	public static QQAuth mQQAuth;
	private Tencent mTencent;
	private static final int GET_OPEN_ID = 0;
	private static final int GET_USER_INFO = 1;
	private static final int GET_WX_TOKEN = 2;
	private static final int GET_WX_INFO = 3;
	private TokenEntity tokenEntity;
	private QQUserInfoEntity userInfo;
	// 自己微信应用的 appId
	public static String APP_ID = "wx4d695a8b93857af1";
	// 自己微信应用的 appSecret
	public static String WX_SECRET = "e4d2e28f0dc7123145aeacfb93e70ca7";

	public static String WX_CODE = "";

	public static IWXAPI wxApi;
	public static boolean isWXLogin = false;
	
	private String channel = AppInfoUtils.getChannelCode(this);
	private int appInstitutionId = AppInfoUtils.getInstitutionId(this);

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_OPEN_ID:
				String token = ((JSONObject) msg.obj).toString();
				tokenEntity = new Gson().fromJson(token, TokenEntity.class);
				getInfo();
				break;
			case GET_USER_INFO:
				String info = ((JSONObject) msg.obj).toString();
				userInfo = new Gson().fromJson(info, QQUserInfoEntity.class);
				
				startLoadingDialog();
				String QQHeadImageUrl = userInfo.figureurl_qq_2.replace("http://q.qlogo.cn/", "");
				
				try {
					QQHeadImageUrl = URLEncoder.encode(QQHeadImageUrl, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				qqRegLog(tokenEntity.openid, QQHeadImageUrl, userInfo.nickname);
				break;
			case GET_WX_TOKEN:
				 WXAccessTokenEntity WXTokenentity = (WXAccessTokenEntity) msg.obj;
				 String accessToken = WXTokenentity.access_token;
				 String openId = WXTokenentity.openid;
				
				 loadWXUserInfo(accessToken, openId);
				break;
			case GET_WX_INFO:
				WXUserInfoEntity wxInfoEntity = (WXUserInfoEntity) msg.obj;
				startLoadingDialog();
				String WXHeaderimageUrl = null;
				if(wxInfoEntity != null && wxInfoEntity.headimgurl != null){
					WXHeaderimageUrl = wxInfoEntity.headimgurl.replace("http://wx.qlogo.cn/", "");
				}
				
				try {
					WXHeaderimageUrl = URLEncoder.encode(WXHeaderimageUrl, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				wxRegLog(wxInfoEntity.openid, WXHeaderimageUrl, wxInfoEntity.nickname);
				break;
			}
		};
	};


	private void initSDK() {
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		final Handler handler = new Handler();
		EventHandler eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
	}

	public void setOnSendMessageHandler(OnSendMessageHandler h) {
		osmHandler = h;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);

		mTencent = Tencent.createInstance("1104698311", this.getApplicationContext());
		mQQAuth = QQAuth.createInstance("1104698311", this);

		wxApi = WXAPIFactory.createWXAPI(this, APP_ID, true);
		wxApi.registerApp(APP_ID);

		initData();
		initSDK();
		initView();

		String[] country = getCurrentCountry();
		if (country != null) {
			currentCode = country[1];
		}

		handler = new EventHandler() {
			@SuppressWarnings("unchecked")
			public void afterEvent(final int event, final int result, final Object data) {

				runOnUiThread(new Runnable() {
					public void run() {

						if (result == SMSSDK.RESULT_COMPLETE) {
							if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
								// 请求支持国家列表
								onCountryListGot((ArrayList<HashMap<String, Object>>) data);
							} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
								// 请求验证码后，跳转到验证码填写页面
								afterVerificationCodeRequested();
							}
						} else {

							dismissLoadingDialog();
							
							
							if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE && data != null && (data instanceof UserInterruptException)) {
								// 由于此处是开发者自己决定要中断发送的，因此什么都不用做
								return;
							}
							
							afterVerificationCodeRequested();

							// 根据服务器返回的网络错误，给toast提示
//							try {
//								((Throwable) data).printStackTrace();
//								Throwable throwable = (Throwable) data;
//
//								JSONObject object = new JSONObject(throwable.getMessage());
//								String des = object.optString("detail");
//								if (!TextUtils.isEmpty(des)) {
//									showToast(des);
//									return;
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							showToast(R.string.smssdk_network_error);
						}
					}
				});
			}
		};

		if (!isAuthPhone) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					DeviceHelper.showIME(mPhoneEdit);
				}
			}, 500);
		}

	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mPhoneEdit = (EditText) this.findViewById(R.id.edittext_phone_number);
		mAgreementText = (TextView) this.findViewById(R.id.textview_agreement);

		mNextButton = (Button) this.findViewById(R.id.button_next);

		btn_qq_login = (Button) findViewById(R.id.btn_qq_login);
		btn_wx_login = (Button) findViewById(R.id.btn_wx_login);

		mTopbarView.setCenterText("登录");
		mTopbarView.setLeftView(true, true);

		mPhoneEdit.setText("");
		mPhoneEdit.addTextChangedListener(this);
		mPhoneEdit.requestFocus();
		if (mPhoneEdit.getText().length() > 0) {
			mPhoneEdit.setEnabled(true);
		}

		mNextButton.setOnClickListener(this);
		btn_qq_login.setOnClickListener(this);
		btn_wx_login.setOnClickListener(this);

		mPhoneEdit.setText(phone);
		if (isAuthPhone) {
			onClick(mNextButton);
		}

		mAgreementText.setText(getClickableSpan());
		mAgreementText.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	/**
	 * QQ 注册
	 * @param qqtoken
	 * @param avater
	 * @param username
	 */
	protected void qqRegLog(String qqtoken, String avater, String username) {
		RequestService.getInstance().qqAuthRegLog(this, qqtoken, avater, username, channel, appInstitutionId+"", LoginRegEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					LoginRegEntity entity = (LoginRegEntity) resultData;
					AccountManager.saveAccount(RegisterPhoneActivity.this, entity);

					Class clazz = null;
					
					boolean isExist = SharedPreferencesHelper.getBoolean(RegisterPhoneActivity.this, Field.IS_USER_EXIST, true);
					if(isExist){
						clazz = MainTabActivity.class;
					} else {
						clazz = RecommendRegistActivity.class;
					}
					
					Intent intent = new Intent(RegisterPhoneActivity.this,clazz);
					startActivity(intent);
				} else {
					showToast(resultData.getMessage());
				}

			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}
	protected void wxRegLog(String WXToken, String avater, String username) {
		RequestService.getInstance().weixinAuthRegLog(this, WXToken, avater, username, channel, appInstitutionId+"",LoginRegEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					LoginRegEntity entity = (LoginRegEntity) resultData;
					AccountManager.saveAccount(RegisterPhoneActivity.this, entity);

					Class clazz = null;
					
					boolean isExist = SharedPreferencesHelper.getBoolean(RegisterPhoneActivity.this, Field.IS_USER_EXIST, true);
					if(isExist){
						clazz = MainTabActivity.class;
					} else  {
						clazz = RecommendRegistActivity.class;
					}
					
					Intent intent = new Intent(RegisterPhoneActivity.this,clazz);
					startActivity(intent);
				} else {
					showToast(RegisterPhoneActivity.this.getResources().getString(R.string.please_check_netword));
				}

			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	private SpannableString getClickableSpan() {

		SpannableString spanableInfo = new SpannableString(mAgreementText.getText().toString());
		int start = 14;
		int end = spanableInfo.length();
		spanableInfo.setSpan(new ClickableSpan() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(RegisterPhoneActivity.this, RegisterServiceActivity.class);
				intent.putExtra("url", "agreement.html");
				intent.putExtra("title", "服务协议条款");
				startActivity(intent);
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(false);
			}

		}, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanableInfo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_33AB33)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanableInfo.setSpan(new BackgroundColorSpan(Color.WHITE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		mAgreementText.setHighlightColor(Color.TRANSPARENT);

		return spanableInfo;
	}

	private void initData() {
		phone = getIntent().getStringExtra("phone");
		isAuthPhone = getIntent().getBooleanExtra("isAuthPhone", false);
	}

	private String[] getCurrentCountry() {
		String mcc = getMCC();
		String[] country = null;
		if (country == null) {
			Log.w("SMSSDK", "no country found by MCC: " + mcc);
			country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
		}
		return country;
	}

	private String getMCC() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
		String networkOperator = tm.getNetworkOperator();
		if (!TextUtils.isEmpty(networkOperator)) {
			return networkOperator;
		}
		// 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
		return tm.getSimOperator();
	}

	public void onResume() {
		super.onResume();
		SMSSDK.registerEventHandler(handler);
		if (isWXLogin) {
			getAccessToken();
		}
	}

	public void onPause() {
		super.onPause();
		SMSSDK.unregisterEventHandler(handler);
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() > 0) {
			mNextButton.setEnabled(true);
		} else {
			mNextButton.setEnabled(false);
		}
	}

	public void afterTextChanged(Editable s) {

	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button_next:
			phone = mPhoneEdit.getText().toString().trim().replaceAll("\\s*", "");

			if (TextUtils.isEmpty(phone)) {
				showToast(R.string.smssdk_write_mobile_phone);
				return;
			}

			if (!StringHelper.isPhoneNumber(phone)) {
				showToast(R.string.smssdk_write_right_mobile_phone);
				return;
			}
			MobclickAgent.onEvent(this, "InputPhone");
			if (isAuthPhone) {
				startLoadingDialog();
				if (countryRules == null || countryRules.size() <= 0) {
					SMSSDK.getSupportedCountries();
				} else {
					// String code = tvCountryNum.getText().toString().trim();
					checkPhoneNum(phone);
				}
			} else {
				checkUser(phone);
			}
			break;
		case R.id.btn_qq_login:
			if (!mTencent.isSessionValid()) {
				mTencent.login(this, "all", new BaseUIListener(this) {
					@Override
					public void onComplete(Object response) {
						super.onComplete(response);
						Message msg = mHandler.obtainMessage();
						msg.what = GET_OPEN_ID;
						msg.obj = response;
						mHandler.sendMessage(msg);
					}
				});
			} else {
				getInfo();
			}

			break;
		case R.id.btn_wx_login:

			isWXLogin = true;
			SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "wechat_sdk_demo";
			wxApi.sendReq(req);
			break;
		}
	}

	/**
	 * QQ登陆获取用户信息
	 */
	protected void getInfo() {
		if (mTencent.isSessionValid()) {
			UserInfo info = new UserInfo(this, mTencent.getQQToken());

			info.getUserInfo(new BaseUIListener(this, "get_simple_userinfo") {
				@Override
				public void onComplete(Object response) {
					super.onComplete(response);
					Message msg = mHandler.obtainMessage();
					msg.what = GET_USER_INFO;
					msg.obj = response;
					mHandler.sendMessage(msg);
				}
			});

		}
	}

	/**
	 * 微信登陆获取微信端 token
	 */
	private void getAccessToken() {
		
		startLoadingDialog();

		RequestService.getInstance().getWXAccessToken(this, APP_ID, WX_SECRET, WX_CODE, WXAccessTokenEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData != null) {
					WXAccessTokenEntity entity = (WXAccessTokenEntity) resultData;

					Message msg = mHandler.obtainMessage();
					msg.obj = entity;
					msg.what = GET_WX_TOKEN;
					mHandler.sendMessage(msg);
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				showToast("获取TOKEN信息失败");
				dismissLoadingDialog();
			}
		});
	}
	
	/**
	 * 加载微信用户信息
	 * @param accessToken
	 * @param openId
	 */
	private void loadWXUserInfo(String accessToken, String openId) {
		RequestService.getInstance().getWXUserInfo(this, accessToken, openId, WXUserInfoEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				Message msg = mHandler.obtainMessage();
				msg.what = GET_WX_INFO;
				msg.obj = resultData;
				mHandler.sendMessage(msg);
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				showToast("获取用户信息失败");
			}
		});
		isWXLogin = false;
	}
	
	private void checkUser(final String phone) {
		((BaseActivity) RegisterPhoneActivity.this).startLoadingDialog();
		RequestService.getInstance().checkUser(RegisterPhoneActivity.this, phone, IsPassSetEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				((BaseActivity) RegisterPhoneActivity.this).dismissLoadingDialog();
				if (resultData.isOk()) {

					IsPassSetEntity entity = (IsPassSetEntity) resultData;
					
					SharedPreferencesHelper.setBoolean(RegisterPhoneActivity.this, Field.IS_USER_EXIST, entity.isData());

					if (entity.isData()) {
						checkPwd(phone);
					} else {
						startLoadingDialog();
						if (countryRules == null || countryRules.size() <= 0) {
							SMSSDK.getSupportedCountries();
						} else {
							checkPhoneNum(phone);
						}
					}
				} else {
					((BaseActivity) RegisterPhoneActivity.this).showToast(resultData.getMessage());
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				((BaseActivity) RegisterPhoneActivity.this).dismissLoadingDialog();

			}
		});
	}

	private void checkPwd(final String phone) {
		startLoadingDialog();
		RequestService.getInstance().isPassSet(this, phone , IsPassSetEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					IsPassSetEntity entity = (IsPassSetEntity) resultData;

					if (!entity.isData()) {
						startLoadingDialog();
						if (countryRules == null || countryRules.size() <= 0) {
							SMSSDK.getSupportedCountries();
						} else {
							checkPhoneNum(phone);
						}
					} else {
						Intent intent = new Intent(RegisterPhoneActivity.this, RegisterPasswordActivity.class);
						intent.putExtra("phone", phone);
						intent.putExtra("isLogin", true);
						startActivity(intent);
						finish();
					}

				} else {
					showToast(resultData.getMessage());
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});

	}


	private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
		// 解析国家列表
		for (HashMap<String, Object> country : countries) {
			String code = (String) country.get("zone");
			String rule = (String) country.get("rule");
			if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
				continue;
			}

			if (countryRules == null) {
				countryRules = new HashMap<String, String>();
			}
			countryRules.put(code, rule);
		}
		// 检查手机号码
		String phone = mPhoneEdit.getText().toString().trim().replaceAll("\\s*", "");
		checkPhoneNum(phone);
	}

	/** 分割电话号码 */
	private String splitPhoneNum(String phone) {
		StringBuilder builder = new StringBuilder(phone);
		builder.reverse();
		for (int i = 4, len = builder.length(); i < len; i += 5) {
			builder.insert(i, ' ');
		}
		builder.reverse();
		return builder.toString();
	}

	/** 检查电话号码 */
	private void checkPhoneNum(final String phone) {
		if (currentCode.startsWith("+")) {
			currentCode = currentCode.substring(1);
		}

		if (TextUtils.isEmpty(phone)) {
			showToast(R.string.smssdk_write_mobile_phone);
			dismissLoadingDialog();
			return;
		}

		String rule = countryRules.get(currentCode);
		Pattern p = Pattern.compile(rule);
		Matcher m = p.matcher(phone);
		if (!m.matches()) {
			showToast(R.string.smssdk_write_right_mobile_phone);
			dismissLoadingDialog();
			return;
		}
		dismissLoadingDialog();
		startLoadingDialog();
		new Thread() {
			public void run() {
				SMSSDK.getVerificationCode(currentCode, phone.trim(), osmHandler);
			};
		}.start();
	}

	/** 请求验证码后，跳转到验证码填写页面 */
	private void afterVerificationCodeRequested() {

		dismissLoadingDialog();

		String phone = mPhoneEdit.getText().toString().trim().replaceAll("\\s*", "");
		String code = currentCode;
		if (code.startsWith("+")) {
			code = code.substring(1);
		}
		String formatedPhone = "+" + code + " " + splitPhoneNum(phone);
		// 验证码页面
		Intent intent = new Intent(this, AuthCodeActivity.class);
		intent.putExtra("phone", phone);
		intent.putExtra("code", code);
		intent.putExtra("formatedPhone", formatedPhone);
		intent.putExtra("isAuthPhone", isAuthPhone);
		if (isAuthPhone) {
			intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
			startActivity(intent);
			finish();
		} else {
			startActivityForResult(intent, 0);
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent intent) {
		if (RESULT_OK == arg1) {
			setResult(RESULT_OK, intent);
			finish();
		}
		super.onActivityResult(arg0, arg1, intent);
	}

}
