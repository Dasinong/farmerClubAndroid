package com.dasinong.farmerclub.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.ui.BindActivity;
import com.dasinong.farmerclub.ui.CaptureActivity;
import com.dasinong.farmerclub.ui.ContactUsActivity;
import com.dasinong.farmerclub.ui.GuideActivity;
import com.dasinong.farmerclub.ui.MainTabActivity;
import com.dasinong.farmerclub.ui.MyCouponActivity;
import com.dasinong.farmerclub.ui.MyInfoActivity;
import com.dasinong.farmerclub.ui.RecommendActivity;
import com.dasinong.farmerclub.ui.RegisterPhoneActivity;
import com.dasinong.farmerclub.ui.SelectUserTypeActivity;
import com.dasinong.farmerclub.ui.SmsSettingActivity;
import com.dasinong.farmerclub.ui.WebBrowserActivity;
import com.dasinong.farmerclub.ui.manager.AccountManager;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.AppInfoUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * @ClassName MeFragment
 * @author linmu
 * @Decription 我
 * @2015-6-9 下午10:29:55
 */
public class MeFragment extends Fragment implements OnClickListener {

	private View mContentView;

	private TopbarView mTopbarView;

	private RelativeLayout mMyInfoLayout;
	private RelativeLayout mDarenLayout;
	private RelativeLayout mScancodeLayout;
	private RelativeLayout mBindLayout;
	private RelativeLayout mRecomentLayout;
	private RelativeLayout mSmsSettingLayout;
	private RelativeLayout mHelpLayout;
	private RelativeLayout mUseLayout;
	private RelativeLayout mContactUsLayout;
	private RelativeLayout mCheckUpdateLayout;

	private RelativeLayout mMyCouponLayout;

	private String userType;
	private boolean isDaren;
	private int memberPoints;
	private String pictureId;

	private boolean enableWelfare;

	private boolean isShow = true;

	private int serverInstitutionId;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		userType = SharedPreferencesHelper.getString(getActivity(), Field.USER_TYPE, SelectUserTypeActivity.FARMER);
		
		isDaren = SharedPreferencesHelper.getBoolean(getActivity(), Field.ISDAREN, false);
		
		serverInstitutionId = SharedPreferencesHelper.getInt(getActivity(), Field.INSTITUTIONID, 0);
		int refuId = SharedPreferencesHelper.getInt(getActivity(), Field.REFUID, -1);
		int appInstitutionId = AppInfoUtils.getInstitutionId(getActivity());

		
		if (refuId > 0 || serverInstitutionId > 0 || appInstitutionId > 0) {
			isShow = false;
		}
		
		enableWelfare = SharedPreferencesHelper.getBoolean(getActivity(), Field.ENABLEWELFARE, false);
		
		pictureId = SharedPreferencesHelper.getString(getActivity(), Field.PICTURE_ID, "");
		memberPoints = SharedPreferencesHelper.getInt(getActivity(), Field.MEMBER_POINTS, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_my, null);

			initView();
			setUpView();

		} else {
			ViewGroup parent = (ViewGroup) mContentView.getParent();
			if (parent != null) {
				parent.removeView(mContentView);
			}
		}
		return mContentView;
	}

	private void initView() {
		mTopbarView = (TopbarView) mContentView.findViewById(R.id.topbar);

		mMyInfoLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_my_info);
		mMyCouponLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_my_coupon);
		mDarenLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_daren);
		
		mScancodeLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_scancode);
		mBindLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_bind);
		mRecomentLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_recommend);
		mSmsSettingLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_sms_setting);
		mHelpLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_help_center);
		mUseLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_use);
		mContactUsLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_contact_us);
		mCheckUpdateLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_check_update);
		
		if(serverInstitutionId == 3){
			mSmsSettingLayout.setVisibility(View.GONE);
			mContactUsLayout.setVisibility(View.GONE);
		}
		
		if(!isShow){
			mBindLayout.setVisibility(View.GONE);
		}
		
		if(enableWelfare){
			mMyCouponLayout.setVisibility(View.VISIBLE);
		}
		
		if(!isDaren){
			mDarenLayout.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onResume() {
		super.onResume();
		
		MobclickAgent.onEvent(getActivity(), "MeFragment");
		
		if (!AccountManager.isLogin(getActivity())) {
			mTopbarView.setRightText("登录");
			mTopbarView.setRightClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), RegisterPhoneActivity.class);
					getActivity().startActivity(intent);
				}
			});
		} else {
			mTopbarView.setRightText("");
			mTopbarView.setRightClickListener(null);
		}
	}

	private void setUpView() {

		mTopbarView.setCenterText("我");

		mMyInfoLayout.setOnClickListener(this);
		mDarenLayout.setOnClickListener(this);
		mScancodeLayout.setOnClickListener(this);
		mBindLayout.setOnClickListener(this);
		mRecomentLayout.setOnClickListener(this);
		mMyCouponLayout.setOnClickListener(this);
		mSmsSettingLayout.setOnClickListener(this);
		mHelpLayout.setOnClickListener(this);
		mUseLayout.setOnClickListener(this);
		mContactUsLayout.setOnClickListener(this);
		mCheckUpdateLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_my_info:// 个人信息
			if (!AccountManager.isLogin(getActivity())) {
				Intent myInfoIntent = new Intent(getActivity(), RegisterPhoneActivity.class);
				getActivity().startActivity(myInfoIntent);
			} else {
				MobclickAgent.onEvent(getActivity(), "MyInfo");
				Intent myInfoIntent = new Intent(getActivity(), MyInfoActivity.class);
				myInfoIntent.putExtra("isRetailer", false);
				getActivity().startActivity(myInfoIntent);
			}
			break;
		case R.id.layout_daren:
			Intent darenIntent = new Intent(getActivity(), WebBrowserActivity.class);
			String url = "http://120.26.208.198/jifen/index.html?avatarUrl=" + pictureId + "&memberPoints=" + memberPoints;
			darenIntent.putExtra(WebBrowserActivity.URL, url);
			darenIntent.putExtra(WebBrowserActivity.TITLE, "达人积分");
			startActivity(darenIntent);
			break;
		case R.id.layout_scancode:// 扫一扫
			Intent scanIntent = new Intent(getActivity(), CaptureActivity.class);

			// 友盟统计自定义统计事件
			MobclickAgent.onEvent(getActivity(), "ScanQRcode");

			startActivity(scanIntent);
			break;
		case R.id.layout_bind:// 绑定机构
			Intent bindIntent = new Intent(getActivity(), BindActivity.class);
			startActivity(bindIntent);
			break;
		case R.id.layout_recommend:// 有奖推荐

			// 友盟统计自定义统计事件
			MobclickAgent.onEvent(getActivity(), "Recommend");
			if(AccountManager.checkLogin(getActivity())){
				Intent intent = new Intent(getActivity(), RecommendActivity.class);
				getActivity().startActivity(intent);
			}
			break;
		case R.id.layout_my_coupon:
			MobclickAgent.onEvent(getActivity(), "MyCoupon");
			// TODO 判断用户类型
			if(AccountManager.checkLogin(getActivity())){
				Intent intent = new Intent(getActivity(), MyCouponActivity.class);
				getActivity().startActivity(intent);
			}
			
			break;
		case R.id.layout_sms_setting:// 短信订阅设置

			// 友盟统计自定义统计事件
			MobclickAgent.onEvent(getActivity(), "SmsSetting");

			if (!AccountManager.isLogin(getActivity())) {
				Intent myInfoIntent = new Intent(getActivity(), RegisterPhoneActivity.class);
				getActivity().startActivity(myInfoIntent);
			} else {
				Intent smsIntent = new Intent(getActivity(), SmsSettingActivity.class);
				getActivity().startActivity(smsIntent);
			}
			break;
		case R.id.layout_help_center:// 帮助中心

			// 友盟统计自定义统计事件
			MobclickAgent.onEvent(getActivity(), "HelpCenter");
			Intent helpIntent = new Intent(getActivity(), WebBrowserActivity.class);
			helpIntent.putExtra("url", "file:///android_asset/HelpCenter.html");
			helpIntent.putExtra("title", "帮助中心");
			startActivity(helpIntent);
			break;
		case R.id.layout_use:// 使用教程

			// 友盟统计自定义统计事件
			MobclickAgent.onEvent(getActivity(), "UseLesson");

			Intent guideIntent = new Intent(getActivity(), GuideActivity.class);
			guideIntent.putExtra("isFirst", false);
			startActivity(guideIntent);
			break;
		case R.id.layout_contact_us:// 联系我们
			MobclickAgent.onEvent(getActivity(), "ContactUs");
			Intent contactIntent = new Intent(getActivity(), ContactUsActivity.class);
			getActivity().startActivity(contactIntent);
			break;
		case R.id.layout_check_update:// 检查更新
			
			Toast.makeText(getActivity(), "正在检测更新", 0).show();

			// 友盟更新
			UmengUpdateAgent.forceUpdate(this.getActivity());
			UmengUpdateAgent.setDefault();
			UmengUpdateAgent.setUpdateAutoPopup(false);
			
			
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				
				@Override
				public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
					switch (updateStatus) {
					case UpdateStatus.Yes: // has update
						if(getActivity() == null){
							return;
						}
						UmengUpdateAgent.showUpdateDialog(getActivity(), updateInfo);
						UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
							
							@Override
							public void onClick(int status) {
								if(MainTabActivity.isMustUpdate){
									((MainTabActivity)getActivity()).finish();
								}
							}
						});
						break;
					case UpdateStatus.No: // has no update
						if(getActivity() == null){
							return;
						}
						Toast.makeText(getActivity(), "没有更新", Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.NoneWifi: // none wifi
						if(getActivity() == null){
							return;
						}
						Toast.makeText(getActivity(), "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.Timeout: // time out
						if(getActivity() == null){
							return;
						}
						Toast.makeText(getActivity(), "超时", Toast.LENGTH_SHORT).show();
						break;
					}
				}
			});

			// Intent taskIntent = new Intent(getActivity(),
			// TaskDetailsActivity.class);
			// getActivity().startActivity(taskIntent);
			// break;
		}
	}

	protected void checkUpdate() {

	}

}
