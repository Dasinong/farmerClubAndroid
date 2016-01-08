package com.dasinong.farmerclub.ui;

import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.ui.view.LoadingDialog;
import com.dasinong.farmerclub.utils.ViewHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {

	protected final String tag = getClass().getSimpleName();
	// private LoadingDialog mLoadingDiag;
	private Dialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
	}

	// 友盟统计，页面可见
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	// protected void initView(){}
	// protected void setView(){}

	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
	}

	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int res) {
		Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
	}

	public void startLoadingDialog(String loadingText) {
		startLoadingDialog(loadingText, false);
	}

	public void startLoadingDialog() {
		startLoadingDialog("", true);
	}

	public void startLoadingDialog(int resId, boolean outeSiteCanceled) {
		startLoadingDialog(getResources().getString(resId), outeSiteCanceled);
	}

	public void startLoadingDialog(String loadingText, boolean outeSiteCanceled) {
		// mLoadingDiag = ViewHelper.getLoadingDialog(this, loadingText, true,
		// outeSiteCanceled);
		// mLoadingDiag.show();

		if (mDialog == null) {
			// mDialog = ProgressDialog.show(this, "",
			// getString(R.string.loading), true);
			mDialog = new Dialog(this, R.style.dialog);
			View contentView = LayoutInflater.from(this).inflate(R.layout.layout_loadingdialog, null);
			mDialog.setContentView(contentView);
			Window window = mDialog.getWindow();
			window.setWindowAnimations(R.style.customDialog_anim_style);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setCancelable(true);
		}

		mDialog.show();

	}

	public void dismissLoadingDialog() {
		// if (mLoadingDiag != null) {
		// mLoadingDiag.dismiss();
		// }
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	// 友盟统计页面不可见
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// if (mLoadingDiag != null) {
		// mLoadingDiag.dismiss();
		// }
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public void showRemindDialog(String title, String content, String sureButton, String cancelButton, final MyDialogClickListener dialogClickListener) {
		final Dialog dialog = new Dialog(this, R.style.CommonDialog);
		dialog.setContentView(R.layout.confirm_gps_network_dialog);
		TextView tv = (TextView) dialog.findViewById(R.id.tv_dialog_hint);
		TextView tv_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);

		tv_title.setText(title);
		tv.setTextSize(22);

		tv.setText(content);
		tv.setTextSize(18);

		Button waitBtn = (Button) dialog.findViewById(R.id.btn_dialog_ok);
		waitBtn.setText(cancelButton);
		waitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialogClickListener.onCancelButtonClick();
				dialog.dismiss();
			}
		});
		Button backBtn = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
		backBtn.setText(sureButton);
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialogClickListener.onSureButtonClick();
				dialog.dismiss();
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	public interface MyDialogClickListener {
		public void onSureButtonClick();

		public void onCancelButtonClick();
	}

}
