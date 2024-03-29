/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2014年 mob.com. All rights reserved.
 */
package com.dasinong.farmerclub.ui;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.SMSReceiver;

import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.IsPassSetEntity;
import com.dasinong.farmerclub.entity.LoginRegEntity;
import com.dasinong.farmerclub.entity.SecurityCodeEntity;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.ui.manager.AccountManager;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.AppInfoUtils;
import com.umeng.analytics.MobclickAgent;

public class AuthCodeActivity extends BaseActivity implements OnClickListener, TextWatcher {

	private static final int RETRY_INTERVAL = 30;
	private String phone;
	private String code;
	private String formatedPhone;
	private int time = RETRY_INTERVAL;
	private EventHandler handler;
	private Handler mHandler = new Handler();

	private TopbarView mTopbarView;

	private EditText etIdentifyNum;
	// private TextView tvTitle;
	private TextView tvPhone;
	private TextView tvIdentifyNotify;
	private TextView tvUnreceiveIdentify;
	// private ImageView ivClear;
	private Button btnSubmit;
	// private Button btnSounds;
	private BroadcastReceiver smsReceiver;
	// private int SHOWDIALOGTYPE = 1;

	private int mFailedCount = 0;

	private boolean isAuthPhone = false;
	private boolean isAuthPempPwd = false;

	private TextView mCallPhoneText;

	private int securityCode;
	private int appInstitutionId;

	private static String APPKEY = "eb96311cfbc4";
	private static String APPSECRET = "93d69dd2aa27c2fe211bc5907334e332";

	public void setPhone(String phone, String code, String formatedPhone) {
		this.phone = phone;
		this.code = code;
		this.formatedPhone = formatedPhone;
	}

	private void initData() {
		phone = getIntent().getStringExtra("phone");
		securityCode = getIntent().getIntExtra("securityCode", -1);
		code = getIntent().getStringExtra("code");
		formatedPhone = getIntent().getStringExtra("formatedPhone");
		isAuthPhone = getIntent().getBooleanExtra("isAuthPhone", false);
		isAuthPempPwd = getIntent().getBooleanExtra("authPempPwd", false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register_login_authcode);

		initData();

		SMSSDK.initSDK(this, APPKEY, APPSECRET);

		appInstitutionId = AppInfoUtils.getInstitutionId(this);

		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mTopbarView.setCenterText("填写验证码");
		mTopbarView.setLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				runOnUiThread(new Runnable() {
					public void run() {
						showNotifyDialog();
					}
				});
			}
		});

		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		btnSubmit.setEnabled(false);

		etIdentifyNum = (EditText) findViewById(R.id.edittext_authcode);
		etIdentifyNum.addTextChangedListener(this);
		tvIdentifyNotify = (TextView) findViewById(R.id.tv_identify_notify);
		String text = getString(R.string.smssdk_send_mobile_detail);
		tvIdentifyNotify.setText(Html.fromHtml(text));
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		tvPhone.setText(phone);
		tvUnreceiveIdentify = (TextView) findViewById(R.id.tv_unreceive_identify);
		String unReceive = getString(R.string.smssdk_receive_msg, time);
		tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
		tvUnreceiveIdentify.setOnClickListener(this);
		tvUnreceiveIdentify.setEnabled(false);

		handler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					afterSubmit(result, data);
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					afterGet(result, data);
				} else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
					afterGetVoice(result, data);
				}
			}
		};
		SMSSDK.registerEventHandler(handler);
		countDown();

		smsReceiver = new SMSReceiver(new SMSSDK.VerifyCodeReadListener() {
			@Override
			public void onReadVerifyCode(final String verifyCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						etIdentifyNum.setText(verifyCode);
						onClick(btnSubmit);
					}
				});
			}
		});
		registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

		mCallPhoneText = (TextView) this.findViewById(R.id.btn_phone_kefu);

		if (isAuthPempPwd) {
			tvUnreceiveIdentify.setVisibility(View.GONE);
			mCallPhoneText.setVisibility(View.VISIBLE);
		} else {
			tvUnreceiveIdentify.setVisibility(View.VISIBLE);
			mCallPhoneText.setVisibility(View.GONE);
		}

		mCallPhoneText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCallDialog();
			}
		});

	}

	private void showCallDialog() {
		final Dialog dialog = new Dialog(this, R.style.CommonDialog);
		dialog.setContentView(R.layout.smssdk_back_verify_dialog);
		TextView tv = (TextView) dialog.findViewById(R.id.tv_dialog_hint);
		tv.setText("确定致电: 4000556050 ?");
		tv.setTextSize(18);
		Button waitBtn = (Button) dialog.findViewById(R.id.btn_dialog_ok);
		waitBtn.setText("取消");
		waitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button backBtn = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
		backBtn.setText("确定");
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4000556050"));
				startActivity(intent);
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void finish() {
		SMSSDK.unregisterEventHandler(handler);
		unregisterReceiver(smsReceiver);
		super.finish();
	}

	/** 倒数计时 */
	private void countDown() {

		mHandler.postDelayed(new Runnable() {
			public void run() {
				time--;
				if (time == 0) {
					String unReceive = getString(R.string.smssdk_unreceive_identify_code, time);
					tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
					tvUnreceiveIdentify.setEnabled(true);
					// btnSounds.setVisibility(View.VISIBLE);
					time = RETRY_INTERVAL;
				} else {
					String unReceive = getString(R.string.smssdk_receive_msg, time);
					tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
					tvUnreceiveIdentify.setEnabled(false);
					mHandler.postDelayed(this, 1000);
				}
			}
		}, 1000);
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// 如果输入框木有，就隐藏delbtn
		if (s.length() > 0) {
			btnSubmit.setEnabled(true);
		} else {
			btnSubmit.setEnabled(false);
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	public void afterTextChanged(Editable s) {
		// btnSounds.setVisibility(View.GONE);
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_submit:
			// 提交验证码
			String verificationCode = etIdentifyNum.getText().toString().trim();

			MobclickAgent.onEvent(this, "InputAuthCode");

			if (isAuthPempPwd) {
				authPempPwd(verificationCode);
			} else {
				if (!TextUtils.isEmpty(code)) {
					startLoadingDialog();
					SMSSDK.submitVerificationCode(code, phone, verificationCode);
				} else {
					showToast(R.string.smssdk_write_identify_code);
				}
			}

			break;
		case R.id.tv_unreceive_identify:
			// SHOWDIALOGTYPE = 1;
			// 没有接收到短信
			showDialog1();
			break;
		}
	}

	private void authPempPwd(String verificationCode) {
		startLoadingDialog();
		String strSecurityCode;
		if (securityCode == -1) {
			strSecurityCode = "";
		} else {
			strSecurityCode = securityCode + "";
		}
		RequestService.getInstance().loginWithSecCode(this, strSecurityCode, phone, verificationCode, LoginRegEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					LoginRegEntity entity = (LoginRegEntity) resultData;

					AccountManager.saveAccount(AuthCodeActivity.this, entity);

					Class clazz = null;
					boolean isExist = SharedPreferencesHelper.getBoolean(AuthCodeActivity.this, Field.IS_USER_EXIST, true);
					if (isExist) {
						int institutionId = SharedPreferencesHelper.getInt(AuthCodeActivity.this, Field.INSTITUTIONID, 0);
						if (institutionId == 3) {
							Intent splashIntent = new Intent(AuthCodeActivity.this, SplashActivity.class);
							startActivity(splashIntent);
							AuthCodeActivity.this.finish();
							return;
						}
						clazz = MainTabActivity.class;
					} else {
						clazz = RecommendRegistActivity.class;
					}

					Intent intent = new Intent(AuthCodeActivity.this, clazz);

					startActivity(intent);

					Intent setIntent = new Intent(AuthCodeActivity.this, MyInfoSetActivity.class);
					setIntent.putExtra("editType", MyInfoSetActivity.EDIT_PASSWORD);
					setIntent.putExtra("isNewPwd", true);
					startActivity(setIntent);

					finish();
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

	/** 弹出重新发送短信对话框,或发送语音窗口 */
	private void showDialog1() {
		final Dialog dialog = new Dialog(this, R.style.CommonDialog);

		View view = View.inflate(this, R.layout.dialog_authcode_no, null);
		Button againBt = (Button) view.findViewById(R.id.button_get_code_again);
		Button skipBt = (Button) view.findViewById(R.id.button_skip_auth);

		dialog.setContentView(view);
		againBt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				tvUnreceiveIdentify.setEnabled(false);

				startLoadingDialog();

				// 重新获取验证码短信
				SMSSDK.getVerificationCode(code, phone.trim(), null);
			}
		});

		skipBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				checkUser(phone);
			}
		});

		dialog.setCanceledOnTouchOutside(true);
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				tvUnreceiveIdentify.setEnabled(true);
			}
		});
		dialog.show();

	}

	private void checkUser(final String phone) {
		((BaseActivity) AuthCodeActivity.this).startLoadingDialog();
		RequestService.getInstance().checkUser(AuthCodeActivity.this, phone, IsPassSetEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				((BaseActivity) AuthCodeActivity.this).dismissLoadingDialog();
				if (resultData.isOk()) {
					IsPassSetEntity entity = (IsPassSetEntity) resultData;

					SharedPreferencesHelper.setBoolean(AuthCodeActivity.this, Field.IS_USER_EXIST, entity.isData());

					if (entity.isData()) {
						requestCode();
					} else {
						Intent intent = new Intent(AuthCodeActivity.this, RegisterPasswordActivity.class);
						intent.putExtra("phone", phone);
						intent.putExtra("isLogin", false);
						AuthCodeActivity.this.startActivity(intent);
						finish();
					}
				} else {
					((BaseActivity) AuthCodeActivity.this).showToast(resultData.getMessage());
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				((BaseActivity) AuthCodeActivity.this).dismissLoadingDialog();

			}
		});
	}

	// private void checkPwd() {
	// startLoadingDialog();
	// RequestService.getInstance().isPassSet(this, IsPassSetEntity.class, new
	// RequestListener() {
	//
	// @Override
	// public void onSuccess(int requestCode, BaseEntity resultData) {
	// dismissLoadingDialog();
	// if(resultData.isOk()){
	// IsPassSetEntity entity = (IsPassSetEntity) resultData;
	//
	// if(!entity.isData()){
	// requestCode();
	// }else{
	// Intent intent = new
	// Intent(AuthCodeActivity.this,RegisterPasswordActivity.class);
	// intent.putExtra("phone", phone);
	// intent.putExtra("isLogin", true);
	// AuthCodeActivity.this.startActivity(intent);
	// finish();
	// }
	//
	// }else{
	// showToast(resultData.getMessage());
	// }
	// }
	//
	// @Override
	// public void onFailed(int requestCode, Exception error, String msg) {
	// dismissLoadingDialog();
	// showToast(R.string.please_check_netword);
	// }
	// });
	//
	// }

	private void requestCode() {
		startLoadingDialog();
		RequestService.getInstance().requestSecurityCode(this, phone, SecurityCodeEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					showToast("验证码重新发送成功");
					isAuthPempPwd = true;
					SecurityCodeEntity entity = (SecurityCodeEntity) resultData;
					securityCode = entity.data;
					tvUnreceiveIdentify.setVisibility(View.GONE);
					mCallPhoneText.setVisibility(View.VISIBLE);
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

	/**
	 * 提交验证码成功后的执行事件
	 * 
	 * @param result
	 * @param data
	 */
	private void afterSubmit(final int result, final Object data) {
		runOnUiThread(new Runnable() {
			public void run() {

				dismissLoadingDialog();

				if (result == SMSSDK.RESULT_COMPLETE) {
					// HashMap<String, Object> res = new HashMap<String,
					// Object>();
					// res.put("res", true);
					// res.put("page", 2);
					// res.put("phone", data);
					// setResult(res);

					// intent.putExtra("phone", data);

					// setResult(RESULT_OK, intent);

					// finish();

					if (isAuthPhone) {
						setResult(RESULT_OK);
						finish();
					} else {
						Intent intent = new Intent();
						intent.putExtra("res", true);
						intent.putExtra("page", 2);

						@SuppressWarnings("unchecked")
						HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;

						intent.putExtra("country", (String) phoneMap.get("country"));
						intent.putExtra("phone", (String) phoneMap.get("phone"));
						loginRegister((String) phoneMap.get("phone"));
					}

				} else {
					((Throwable) data).printStackTrace();
					// 验证码不正确
					// int resId = getStringRes(activity,
					// "smssdk_virificaition_code_wrong");
					// if (resId > 0) {
					// Toast.makeText(activity, resId,
					// Toast.LENGTH_SHORT).show();
					// }
					showToast(R.string.smssdk_virificaition_code_wrong);

					mFailedCount++;

					if (mFailedCount >= 3) {
						checkUser(phone);
					}

				}
			}
		});
	}

	private void loginRegister(String cellphone) {
		startLoadingDialog();
		String channel = AppInfoUtils.getChannelCode(this);
		RequestService.getInstance().authcodeLoginReg(this, cellphone, channel, appInstitutionId + "", LoginRegEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					LoginRegEntity entity = (LoginRegEntity) resultData;

					AccountManager.saveAccount(AuthCodeActivity.this, entity);

					Class clazz = null;
					boolean isExist = SharedPreferencesHelper.getBoolean(AuthCodeActivity.this, Field.IS_USER_EXIST, true);

					if (isExist) {
						int institutionId = SharedPreferencesHelper.getInt(AuthCodeActivity.this, Field.INSTITUTIONID, 0);
						if (institutionId == 3) {
							Intent splashIntent = new Intent(AuthCodeActivity.this, SplashActivity.class);
							startActivity(splashIntent);
							AuthCodeActivity.this.finish();
							return;
						}
						clazz = MainTabActivity.class;
					} else {
						clazz = RecommendRegistActivity.class;
					}

					Intent intent = new Intent(AuthCodeActivity.this, clazz);
					startActivity(intent);

					finish();
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

	/**
	 * 获取验证码成功后,的执行动作
	 * 
	 * @param result
	 * @param data
	 */
	private void afterGet(final int result, final Object data) {
		runOnUiThread(new Runnable() {
			public void run() {

				dismissLoadingDialog();

				if (result == SMSSDK.RESULT_COMPLETE) {
					// int resId = getStringRes(activity,
					// "smssdk_virificaition_code_sent");
					// if (resId > 0) {
					// Toast.makeText(activity, resId,
					// Toast.LENGTH_SHORT).show();
					// }
					showToast(R.string.smssdk_virificaition_code_sent);
					String unReceive = getString(R.string.smssdk_receive_msg, time);
					tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
					// btnSounds.setVisibility(View.GONE);
					time = RETRY_INTERVAL;
					countDown();
				} else {
					((Throwable) data).printStackTrace();
					Throwable throwable = (Throwable) data;
					// 根据服务器返回的网络错误，给toast提示
					try {
						JSONObject object = new JSONObject(throwable.getMessage());
						String des = object.optString("detail");
						if (!TextUtils.isEmpty(des)) {
							// Toast.makeText(activity, des,
							// Toast.LENGTH_SHORT).show();
							showToast(des);
							return;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					// / 如果木有找到资源，默认提示
					// int resId = getStringRes(activity,
					// "smssdk_network_error");
					// if (resId > 0) {
					// Toast.makeText(activity, resId,
					// Toast.LENGTH_SHORT).show();
					// }
					showToast(R.string.smssdk_network_error);
				}
			}
		});
	}

	/**
	 * 获取语音验证码成功后,的执行动作
	 * 
	 * @param result
	 * @param data
	 */
	private void afterGetVoice(final int result, final Object data) {
		runOnUiThread(new Runnable() {
			public void run() {

				dismissLoadingDialog();

				if (result == SMSSDK.RESULT_COMPLETE) {
					showToast(R.string.smssdk_send_sounds_success);
					// btnSounds.setVisibility(View.GONE);
				} else {
					((Throwable) data).printStackTrace();
					Throwable throwable = (Throwable) data;
					// 根据服务器返回的网络错误，给toast提示
					try {
						JSONObject object = new JSONObject(throwable.getMessage());
						String des = object.optString("detail");
						if (!TextUtils.isEmpty(des)) {
							// Toast.makeText(activity, des,
							// Toast.LENGTH_SHORT).show();
							showToast(des);
							return;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					// 如果木有找到资源，默认提示
					// int resId = getStringRes(activity,
					// "smssdk_network_error");
					// if (resId > 0) {
					// Toast.makeText(activity, resId,
					// Toast.LENGTH_SHORT).show();
					// }
					showToast(R.string.smssdk_network_error);
				}

			}
		});
	}

	/** 按返回键时，弹出的提示对话框 */
	private void showNotifyDialog() {
		final Dialog dialog = new Dialog(this, R.style.CommonDialog);
		dialog.setContentView(R.layout.smssdk_back_verify_dialog);
		TextView tv = (TextView) dialog.findViewById(R.id.tv_dialog_hint);
		tv.setText(R.string.smssdk_close_identify_page_dialog);
		Button waitBtn = (Button) dialog.findViewById(R.id.btn_dialog_ok);
		waitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button backBtn = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
		// backBtn.setText(R.string.smssdk_back);
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				finish();
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			runOnUiThread(new Runnable() {
				public void run() {
					showNotifyDialog();
				}
			});
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}
