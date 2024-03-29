package com.dasinong.farmerclub.ui;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.LoginRegEntity;
import com.dasinong.farmerclub.entity.User;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.manager.AccountManager;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.Logger;
import com.lidroid.xutils.BitmapUtils;
import com.tencent.tauth.Tencent;

public class MyInfoActivity extends BaseActivity implements OnClickListener, CropHandler {

	private TopbarView mTopbarView;

	private RelativeLayout mHeadviewLayout;
	private ImageView mHeadviewImage;

	private RelativeLayout mPhoneLayout;
	private TextView mPhoneText;
	private Button mAuthPhoneButton;

	private RelativeLayout mResetPwdLayout;

	private RelativeLayout mNameLayout;
	private TextView mNameText;

	private RelativeLayout mAddressLayout;
	private TextView mAddressText;

	private RelativeLayout mHomephoneLayout;
	private TextView mHomephoneText;

	private View mLogoutLayout;

	protected String mPhotoPath;
	private String mCropPath;

	CropParams mCropParams = new CropParams();

	private String userId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);
		userId = SharedPreferencesHelper.getString(this, Field.USER_ID, "");

		initView();
		setUpView();

		requestInfo();
	}

	private void requestInfo() {
		startLoadingDialog();
		RequestService.getInstance().getMyInfo(this, LoginRegEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					LoginRegEntity entity = (LoginRegEntity) resultData;
					updateUi(entity);
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

	protected void updateUi(LoginRegEntity entity) {
		User user = entity.getData();
		if (user != null) {

			BitmapUtils bitmapUtils = new BitmapUtils(this);
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.header_default);
			if (user.getPictureId().startsWith("qqapp")) {
				String url = null;
				try {
					url = URLDecoder.decode(user.getPictureId(), "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				url = "http://q.qlogo.cn/" + url;
				bitmapUtils.display(mHeadviewImage, url);
				// LoadUtils.getInstance().loadImage(mHeadviewImage, url);
			} else if (user.getPictureId().startsWith("mmopen")) {
				String url = null;
				try {
					url = URLDecoder.decode(user.getPictureId(), "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				url = "http://wx.qlogo.cn/" + url;
				bitmapUtils.display(mHeadviewImage, url);
				// LoadUtils.getInstance().loadImage(mHeadviewImage, url);
			} else {
				// LoadUtils.getInstance().loadImage(mHeadviewImage,
				// NetConfig.IMAGE_URL + user.getPictureId());
				bitmapUtils.display(mHeadviewImage, NetConfig.IMAGE_URL + user.getPictureId());
			}

			if (TextUtils.isEmpty(user.getCellPhone())) {
				mNameText.setTextColor(Color.RED);
				mNameText.setText("未添加");
				mAuthPhoneButton.setVisibility(View.GONE);
			} else {
				mAuthPhoneButton.setVisibility(View.GONE);
				mPhoneText.setText(user.getCellPhone());
				// if(user.isAuthenticated()){
				// mAuthPhoneButton.setVisibility(View.GONE);
				// mPhoneText.setText(user.getCellPhone());
				// } else {
				// mAuthPhoneButton.setVisibility(View.VISIBLE);
				// }
			}

			if (TextUtils.isEmpty(user.getUserName())) {

				mNameText.setTextColor(Color.RED);
				mNameText.setText("未添加");
			} else {
				mNameText.setTextColor(Color.parseColor("#999999"));
				mNameText.setText(user.getUserName());
			}

			if (TextUtils.isEmpty(user.getAddress())) {
				mAddressText.setTextColor(Color.RED);
				mAddressText.setText("未添加");
			} else {
				mAddressText.setTextColor(Color.parseColor("#999999"));
				mAddressText.setText(user.getAddress());
			}
			if (TextUtils.isEmpty(user.getTelephone())) {
				mHomephoneText.setTextColor(Color.RED);
				mHomephoneText.setText("未添加");
			} else {
				mHomephoneText.setTextColor(Color.parseColor("#999999"));
				mHomephoneText.setText(user.getTelephone());
			}
			// if (user.isAuthenticated()) {
			// mAuthPhoneButton.setVisibility(View.GONE);
			// } else {
			// mAuthPhoneButton.setVisibility(View.VISIBLE);
			// }
		}
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mHeadviewLayout = (RelativeLayout) this.findViewById(R.id.layout_headview);
		mHeadviewImage = (ImageView) this.findViewById(R.id.imageview_headview);
		mPhoneLayout = (RelativeLayout) this.findViewById(R.id.layout_phoneNumber);
		mPhoneText = (TextView) this.findViewById(R.id.textview_phone_number);
		mAuthPhoneButton = (Button) this.findViewById(R.id.button_auth_phone);
		mResetPwdLayout = (RelativeLayout) this.findViewById(R.id.layout_reset_password);
		mNameLayout = (RelativeLayout) this.findViewById(R.id.layout_name);
		mNameText = (TextView) this.findViewById(R.id.textview_name);
		mAddressLayout = (RelativeLayout) this.findViewById(R.id.layout_address);
		mAddressText = (TextView) this.findViewById(R.id.textview_address);
		mHomephoneLayout = (RelativeLayout) this.findViewById(R.id.layout_home_phone);
		mHomephoneText = (TextView) this.findViewById(R.id.textview_home_phone);
		
		mLogoutLayout = this.findViewById(R.id.layout_logout);
		
	}

	private void setUpView() {
		mTopbarView.setCenterText("个人信息");
		mTopbarView.setLeftView(true, true);
		// mTopbarView.setRightText("提交");
		// mTopbarView.setRightClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// uploadInfo();
		// }
		// });

		mHeadviewLayout.setOnClickListener(this);
		mPhoneLayout.setOnClickListener(this);
		mAuthPhoneButton.setOnClickListener(this);
		mResetPwdLayout.setOnClickListener(this);
		mNameLayout.setOnClickListener(this);
		mAddressLayout.setOnClickListener(this);
		mHomephoneLayout.setOnClickListener(this);
		mLogoutLayout.setOnClickListener(this);

	}

	protected void uploadInfo() {
		// String userName = mNameText.getText().toString().trim();
		// String cellphone = mPhoneText.getText().toString().trim();
		// String address = mAddressText.getText().toString().trim();
		// String telephone = mHomephoneText.getText().toString().trim();
		//
		// if(TextUtils.isEmpty(userName)){
		// Toast.makeText(this, "请输入用户名", 0).show();
		// return;
		// }
		// if(TextUtils.isEmpty(cellphone)){
		// Toast.makeText(this, "请输入手机号", 0).show();
		// return;
		// }
		//
		// startLoadingDialog();
		//
		// RequestService.getInstance().uploadInfo(this, userName,
		// cellphone,password, address, telephone, BaseEntity.class, new
		// RequestListener() {
		//
		// @Override
		// public void onSuccess(int requestCode, BaseEntity resultData) {
		// dismissLoadingDialog();
		// if(resultData.isOk()){
		// showToast("个人信息更新成功");
		// finish();
		// }else{
		// showToast(resultData.getMessage());
		// }
		// }
		//
		// @Override
		// public void onFailed(int requestCode, Exception error, String msg) {
		// dismissLoadingDialog();
		// }
		// });
		if (mCropParams == null || mCropParams.uri == null) {
			showToast("请选择图片");
			return;
		}

		startLoadingDialog();
		RequestService.getInstance().uploadHeadImage(this, mCropParams.uri.getPath(), new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					showToast("头像更新成功");
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_headview:
			showHeadViewDialog();
			break;
		case R.id.layout_phoneNumber:
			editInfo(MyInfoSetActivity.EDIT_PHONE);
			break;
		case R.id.button_auth_phone:
			authPhoneCode();
			break;
		case R.id.layout_reset_password:
			editInfo(MyInfoSetActivity.EDIT_PASSWORD);
			break;
		case R.id.layout_name:
			editInfo(MyInfoSetActivity.EDIT_NAME);
			break;
		case R.id.layout_address:
			editInfo(MyInfoSetActivity.EDIT_ADDRESS);
			break;
		case R.id.layout_home_phone:
			editInfo(MyInfoSetActivity.EDIT_HOME_PHONE);
			break;
		case R.id.layout_logout:
			showLogoutDialog();
			break;
		}
	}

	private void showLogoutDialog() {
		final Dialog dialog = new Dialog(this, R.style.CommonDialog);
		dialog.setContentView(R.layout.smssdk_back_verify_dialog);
		TextView tv = (TextView) dialog.findViewById(R.id.tv_dialog_hint);
		tv.setText("确定退出?");
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

				uploadFileLog();
				logout();

				dialog.dismiss();
				finish();
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
	
	protected void uploadFileLog() {
		String appFileDir = getFilesDir().getAbsolutePath();
		File dir = new File(appFileDir + File.separator + userId);
		if(dir.exists() && dir.listFiles().length > 0){
			for (final File file : dir.listFiles()) {
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

	private void logout() {
		Tencent mTencent = Tencent.createInstance("1104723671", getApplicationContext());
		if (mTencent != null && mTencent.isSessionValid()) {
			mTencent.logout(MyInfoActivity.this);
		}
		startLoadingDialog();
		RequestService.getInstance().logout(MyInfoActivity.this, BaseEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					AccountManager.logout(MyInfoActivity.this);
					showToast(resultData.getMessage());
					dismissLoadingDialog();
					Intent intent = new Intent(MyInfoActivity.this, RegisterPhoneActivity.class);
					startActivity(intent);
					if(MainTabActivity.activity != null){
						MainTabActivity.activity.finish();
					}
				} else {
					showToast(resultData.getMessage());
					dismissLoadingDialog();
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	private void authPhoneCode() {

		String phone = mPhoneText.getText().toString().trim();

		if (TextUtils.isEmpty(phone)) {
			showToast("请填写您的手机号");
			return;
		}

		Intent intent = new Intent(this, RegisterPhoneActivity.class);
		intent.putExtra("isAuthPhone", true);
		intent.putExtra("phone", phone);
		startActivityForResult(intent, MyInfoSetActivity.EDIT_AUTH_PHONE);
	}

	private void showHeadViewDialog() {

		final Dialog dialog = new Dialog(this, R.style.CommonDialog);
		View view = View.inflate(this, R.layout.dialog_authcode_no, null);
		Button againBt = (Button) view.findViewById(R.id.button_get_code_again);
		Button skipBt = (Button) view.findViewById(R.id.button_skip_auth);

		againBt.setText("拍照");
		skipBt.setText("相册");

		againBt.setTextSize(18);
		skipBt.setTextSize(18);

		dialog.setContentView(view);
		againBt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// String fileName = System.currentTimeMillis() + ".png";
				// mPhotoPath = PhotoUtils.PHOTOS_DIR + File.separator +
				// fileName;
				// PhotoUtils.fromCapture(MyInfoActivity.this,
				// PhotoUtils.PHOTOS_DIR, fileName);

				Intent intent = CropHelper.buildCaptureIntent(mCropParams.uri);
				startActivityForResult(intent, CropHelper.REQUEST_CAMERA);

				dialog.dismiss();
			}
		});

		skipBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// PhotoUtils.fromGallery(MyInfoActivity.this);

				CropHelper.clearCachedCropFile(mCropParams.uri);
				startActivityForResult(CropHelper.buildCropFromGalleryIntent(mCropParams), CropHelper.REQUEST_CROP);

				dialog.dismiss();
			}
		});

		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	private void editInfo(int type) {
		Intent intent = new Intent(this, MyInfoSetActivity.class);
		intent.putExtra("editType", type);
		startActivityForResult(intent, type);
	}

	// @Override
	// protected void onActivityResult(int requestCode, int arg1, Intent data) {
	// super.onActivityResult(requestCode, arg1, data);
	//
	// if(arg1 == RESULT_OK){
	//
	// String fileName = "";
	// switch (requestCode) {
	// case PhotoUtils.REQUESTCODE_CAPTURE:
	// File file = null;
	// int degree = PhotoUtils.readPictureDegree(mPhotoPath);
	// if (degree != 0) {
	// Toast.makeText(MyInfoActivity.this, "图片处理中", Toast.LENGTH_SHORT).show();
	// Bitmap bitmap = PhotoUtils.rotateBitmap(mPhotoPath, degree);
	// fileName = System.currentTimeMillis() + ".png";
	// String photoPath = PhotoUtils.PHOTOS_DIR + File.separator + fileName;
	// PhotoUtils.writeBitmapToPath(bitmap, PhotoUtils.PHOTOS_DIR, fileName);
	// file = new File(photoPath);
	// } else {
	// file = new File(mPhotoPath);
	// }
	// Uri uri = Uri.fromFile(file);
	// fileName = System.currentTimeMillis() + ".png";
	// mCropPath = PhotoUtils.PHOTOS_DIR + File.separator + fileName;
	// PhotoUtils.startPhotoCrop(MyInfoActivity.this, uri, 1, 1, 300, 300,
	// PhotoUtils.PHOTOS_DIR, fileName);
	// break;
	// case PhotoUtils.REQUESTCODE_GALLERY:
	// if (data != null && data.getData() != null) {
	// fileName = System.currentTimeMillis() + ".png";
	// mCropPath = PhotoUtils.PHOTOS_DIR + File.separator + fileName;
	// PhotoUtils.startPhotoCrop(MyInfoActivity.this, data.getData(), 1, 1, 300,
	// 300, PhotoUtils.PHOTOS_DIR, fileName);
	// } else {
	// Toast.makeText(MyInfoActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
	// }
	// break;
	// case PhotoUtils.REQUESTCODE_CROP:
	// LoadUtils.getInstance().loadImage(mHeadviewImage, "file://" + mCropPath);
	// upload(mCropPath);
	// break;
	// case MyInfoSetActivity.EDIT_PHONE:
	// String info = data.getStringExtra("info");
	// mPhoneText.setText(info);
	// break;
	// case MyInfoSetActivity.EDIT_NAME:
	// String info1 = data.getStringExtra("info");
	// mNameText.setText(info1);
	// break;
	// case MyInfoSetActivity.EDIT_PASSWORD:
	// String info2 = data.getStringExtra("info");
	// password = info2;
	// break;
	// case MyInfoSetActivity.EDIT_ADDRESS:
	// String info3 = data.getStringExtra("info");
	// mAddressText.setText(info3);
	// break;
	// case MyInfoSetActivity.EDIT_HOME_PHONE:
	// String info4 = data.getStringExtra("info");
	// mHomephoneText.setText(info4);
	// break;
	// }
	// }
	//
	// }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("mPhotoPath", mPhotoPath);
		outState.putString("mCropPath", mCropPath);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		mPhotoPath = savedInstanceState.getString("mPhotoPath");
		mCropPath = savedInstanceState.getString("mCropPath");
	}

	private void upload(String path) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		CropHelper.handleResult(this, requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case MyInfoSetActivity.EDIT_PHONE:
				String info = data.getStringExtra("info");
				mPhoneText.setText(info);
				break;
			case MyInfoSetActivity.EDIT_NAME:
				String info1 = data.getStringExtra("info");
				mNameText.setText(info1);
				break;
			case MyInfoSetActivity.EDIT_PASSWORD:
				break;
			case MyInfoSetActivity.EDIT_ADDRESS:
				String info3 = data.getStringExtra("info");
				mAddressText.setText(info3);
				mAddressText.setTextColor(Color.parseColor("#999999"));
				break;
			case MyInfoSetActivity.EDIT_HOME_PHONE:
				String info4 = data.getStringExtra("info");
				mHomephoneText.setText(info4);
				mHomephoneText.setTextColor(Color.parseColor("#999999"));
				break;
			case MyInfoSetActivity.EDIT_AUTH_PHONE:
				authPhone();
				break;
			}
		}

	}

	private void authPhone() {
		startLoadingDialog();
		RequestService.getInstance().setPhoneAuthState(this, BaseEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					showToast("手机验证成功");
					mAuthPhoneButton.setVisibility(View.GONE);
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

	@Override
	public CropParams getCropParams() {
		return mCropParams;
	}

	@Override
	public void onPhotoCropped(Uri uri) {
		Logger.d("MyInfoActivity", "Crop Uri in path: " + uri.getPath());
		// Toast.makeText(this, "Photo cropped!", Toast.LENGTH_LONG).show();
		mHeadviewImage.setImageBitmap(CropHelper.decodeUriAsBitmap(this, mCropParams.uri));
		uploadInfo();
	}

	@Override
	public void onCropCancel() {
		// Toast.makeText(this, "Crop canceled!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCropFailed(String message) {
		Toast.makeText(this, "Crop failed:" + message, Toast.LENGTH_LONG).show();
	}

	@Override
	public Activity getContext() {
		return this;
	}

	@Override
	protected void onDestroy() {
		if (getCropParams() != null)
			CropHelper.clearCachedCropFile(getCropParams().uri);
		super.onDestroy();
	}

}
