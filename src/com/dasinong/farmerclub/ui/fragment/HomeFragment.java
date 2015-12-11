package com.dasinong.farmerclub.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.components.home.view.BannerView;
import com.dasinong.farmerclub.components.home.view.HomeTopView;
import com.dasinong.farmerclub.components.home.view.HomeTopView.OnAddButtonClick;
import com.dasinong.farmerclub.components.home.view.HomeWeatherView;
import com.dasinong.farmerclub.entity.BannerEntity;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.LocationResult;
import com.dasinong.farmerclub.entity.WeatherEntity;
import com.dasinong.farmerclub.entity.WeatherSubscriptionListEntity;
import com.dasinong.farmerclub.entity.WeatherSubscriptionListEntity.SubscriptionEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.AddPlaceActivity;
import com.dasinong.farmerclub.ui.BaseActivity;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.utils.LocationUtils;
import com.umeng.analytics.MobclickAgent;

public class HomeFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

	private ViewGroup mRoot;

	private BGARefreshLayout mRefreshLayout;

	private HomeWeatherView mHomeWeatherView;

	private static final String TAG = "HomeFragment";

	public static final long DEFAULT_FIELD_ID = -1;

	private long mStartTime = -1L;
	/**
	 * unite is minute
	 */
	public static final long TIME_DISTANCE = 20 * DateUtils.MINUTE_IN_MILLIS;

	private BaseActivity mBaseActivity;

	private boolean isShowDialog = true;

	private LocationListener mLocationListener;

	private HomeTopView mHomeTopView;

	private List<String> localNameList = new ArrayList<String>();

	private List<Long> monitorIdList = new ArrayList<Long>();

	private long monitorLocationId = -1;

	private BannerView mHomeBanner;

	public static final String CURRRENT_MONITOR_LOCATION_ID = "currentMontiorLocationId";

	private WeatherSubscriptionListEntity weatherSubscriptionListentity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof BaseActivity) {
			mBaseActivity = (BaseActivity) activity;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mRoot != null) {
			ViewGroup parent = (ViewGroup) mRoot.getParent();

			if (parent != null) {

				parent.removeView(mRoot);

			}

			return mRoot;
		}
		mRoot = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

		mStartTime = -1L;
		isShowDialog = true;
		initView();
		initRefreshLayout();
		initEvent();
		return mRoot;
	}

	private void initView() {

		mHomeWeatherView = (HomeWeatherView) mRoot.findViewById(R.id.weather);

		mHomeTopView = (HomeTopView) mRoot.findViewById(R.id.home_top_view);

		mHomeBanner = (BannerView) mRoot.findViewById(R.id.home_banner);

		mRefreshLayout = (BGARefreshLayout) mRoot.findViewById(R.id.rl_modulename_refresh);

		querySuscription();
	}

	private void querySuscription() {
		RequestService.getInstance().weatherSubscriptions(getActivity(), WeatherSubscriptionListEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					weatherSubscriptionListentity = (WeatherSubscriptionListEntity) resultData;
					if (weatherSubscriptionListentity != null && weatherSubscriptionListentity.data != null && !weatherSubscriptionListentity.data.isEmpty()) {

						mHomeTopView.updatePlaceNameMenue(weatherSubscriptionListentity);

						for (SubscriptionEntity item : weatherSubscriptionListentity.data) {
							localNameList.add(item.locationName);
							monitorIdList.add(item.monitorLocationId);
						}
						if (!monitorIdList.isEmpty()) {
							monitorLocationId = monitorIdList.get(0);
							loadWeather(monitorLocationId + "", "", "");
							loadBanner(monitorLocationId + "", "", "");
						} else {
							initLocation();
						}
					} else {
						initLocation();
					}
				} else {
					initLocation();
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				mBaseActivity.showToast(R.string.please_check_netword);
				mRefreshLayout.endRefreshing();
			}
		});
	}

	private void initEvent() {
		mHomeTopView.setOnAddButtonClick(new OnAddButtonClick() {

			@Override
			public void onClick() {
				MobclickAgent.onEvent(getActivity(), "AddPlace");
				Intent intent = new Intent(getActivity(), AddPlaceActivity.class);
				startActivityForResult(intent, 0);
			}

			@Override
			public void onPopWindowItemClick(String currentPlaceName) {
				if (weatherSubscriptionListentity != null && weatherSubscriptionListentity.data != null) {
					for (SubscriptionEntity itemEntity : weatherSubscriptionListentity.data) {
						if (itemEntity.locationName.equals(currentPlaceName)) {
							monitorLocationId = itemEntity.monitorLocationId;
						}
					}
					loadWeather(monitorLocationId + "", "", "");
					loadBanner(monitorLocationId + "", "", "");
				}
			}
		});
	}

	private void initRefreshLayout() {
		mRefreshLayout.setDelegate(this);
		BGARefreshViewHolder refreshViewHolder = new BGAStickinessRefreshViewHolder(getActivity(), false);
		refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.color_2D8DFA);
		mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
	}

	@Override
	public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

		if (monitorLocationId != -1) {
			loadWeather(monitorLocationId + "", "", "");
			loadBanner(monitorLocationId + "", "", "");
		} else {
			initLocation();
		}
		isShowDialog = false;
	}

	@Override
	public void onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {

		mRefreshLayout.endRefreshing();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		querySuscription();
	}

	private void loadWeather(String monitorLocationId, String lat, String lon) {
		mBaseActivity.startLoadingDialog();
		RequestService.getInstance().loadWeather(getActivity(), monitorLocationId, lat, lon, WeatherEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					WeatherEntity entity = (WeatherEntity) resultData;
					mHomeWeatherView.setWeatherData(entity);

					mHomeTopView.setDate(entity);
				} else {
					mBaseActivity.showToast(R.string.please_check_netword);
				}
				mBaseActivity.dismissLoadingDialog();
				mRefreshLayout.endRefreshing();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				mBaseActivity.showToast(R.string.please_check_netword);
				mBaseActivity.dismissLoadingDialog();
				mRefreshLayout.endRefreshing();
			}
		});
	}

	private void loadBanner(String monitorLocationId, String lat, String lon) {
		mBaseActivity.startLoadingDialog();
		RequestService.getInstance().loadBanner(mBaseActivity, monitorLocationId, lat, lon, BannerEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					BannerEntity entity = (BannerEntity) resultData;

					if (entity.data != null && entity.data.laonongs != null && !entity.data.laonongs.isEmpty()) {
						mHomeBanner.initView(entity);
					} else {
						mHomeBanner.setVisibility(View.GONE);
					}
				} else {
					mBaseActivity.showToast(R.string.please_check_netword);
				}
				mBaseActivity.dismissLoadingDialog();
				mRefreshLayout.endRefreshing();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				mBaseActivity.showToast(R.string.please_check_netword);
				mBaseActivity.dismissLoadingDialog();
				mRefreshLayout.endRefreshing();
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		LocationUtils.getInstance().unRegisterLocationListener();
	}

	private void initLocation() {
		if (mLocationListener == null) {
			mLocationListener = new LocationListener();
		} else {
			LocationUtils.getInstance().unRegisterLocationListener();
			mLocationListener = new LocationListener();
		}
		LocationUtils.getInstance().registerLocationListener(mLocationListener);
	}

	private class LocationListener implements LocationUtils.LocationListener {

		private String mAddress;

		@Override
		public void locationNotify(LocationResult result) {

			String lat = String.valueOf(result.getLatitude());
			String lon = String.valueOf(result.getLongitude());

			mAddress = result.getCity();

			mHomeTopView.setCenterString(mAddress);

			loadWeather("-1", lat, lon);
			loadBanner("-1", lat, lon);

		}
	}
}