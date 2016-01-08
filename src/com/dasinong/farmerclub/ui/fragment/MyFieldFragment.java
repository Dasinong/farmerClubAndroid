package com.dasinong.farmerclub.ui.fragment;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.CropSubscriptionsEntity;
import com.dasinong.farmerclub.entity.CropSubscriptionsEntity.Subscriptions;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.BaseActivity;
import com.dasinong.farmerclub.ui.BaseActivity.MyDialogClickListener;
import com.dasinong.farmerclub.ui.IsInFieldActivity;
import com.dasinong.farmerclub.ui.MyFieldDetailActivity;
import com.dasinong.farmerclub.ui.SelectCropActivity;
import com.dasinong.farmerclub.ui.adapter.MyFieldCropAdapter;
import com.dasinong.farmerclub.ui.adapter.MyFieldCropAdapter.OnDelClickListener;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.SerializableMap;

public class MyFieldFragment extends Fragment {

	private View mRoot;
	private TopbarView topBar;
	private ListView lv_crop;
	private BaseActivity mBaseActivity;
	private CropSubscriptionsEntity entity;
	private MyFieldCropAdapter adapter;
	private Button btn_add_crop;

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
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mRoot != null) {
			ViewGroup parent = (ViewGroup) mRoot.getParent();
			if (parent != null) {
				parent.removeView(mRoot);
			}
			return mRoot;
		}
		mRoot = View.inflate(getActivity(), R.layout.fragment_my_field, null);
		initView();
		initEvent();
		return mRoot;
	}

	private void initEvent() {
		
		btn_add_crop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SelectCropActivity.class);
				startActivityForResult(intent, 0);
			}
		});

		lv_crop.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {

					if (entity.data.subscriptions.get(arg2).fields != null) {
						Map<Long, String> fields = entity.data.subscriptions.get(arg2).fields;
						SerializableMap myMap = new SerializableMap();
						myMap.setMap(fields);
						Intent intent = new Intent(getActivity(), MyFieldDetailActivity.class);
						intent.putExtra("isUpdate", true);
						Bundle bundle = new Bundle();
						bundle.putSerializable("fields", myMap);
						intent.putExtras(bundle);
						startActivity(intent);
					} else {
						mBaseActivity.showRemindDialog("正在种植", "加田后能收到更多针对这块田的种植指导哦！", "马上加田", "暂时没种", new MyDialogClickListener() {
							
							@Override
							public void onSureButtonClick() {
								Intent intent = new Intent(getActivity(), IsInFieldActivity.class);
								String strCropId = String.valueOf(entity.data.subscriptions.get(arg2).crop.cropId);
								SharedPreferencesHelper.setString(getActivity(), Field.CROP_ID, strCropId);
								startActivity(intent);
							}

							@Override
							public void onCancelButtonClick() {
								String strCropId = String.valueOf(entity.data.subscriptions.get(arg2).crop.cropId);
								SharedPreferencesHelper.setString(getActivity(), Field.CROP_ID, strCropId);
								Intent intent = new Intent(getActivity(), MyFieldDetailActivity.class);
								intent.putExtra("cropId", strCropId);
								intent.putExtra("cropName", entity.data.subscriptions.get(arg2).crop.cropName);
								startActivity(intent);
							}
						});
					}
				}
		});
	}

	private void initView() {
		topBar = (TopbarView) mRoot.findViewById(R.id.topbar);
		lv_crop = (ListView) mRoot.findViewById(R.id.lv_crop);
		btn_add_crop = (Button) mRoot.findViewById(R.id.btn_add_crop);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		queryAllCrop();
	}

	private void initTopBar() {
		topBar.setCenterText("关注中的作物");
		
		/**
		 * 下边注释的代码是删除田地，这段代码不可删除
		 */
		
//		if (adapter != null) {
//			if (adapter.getBtnVisible()) {
//				topBar.setRightText("完成");
//				topBar.setRightClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						adapter.setDelVisible(false);
//						initTopBar();
//						queryAllCrop();
//					}
//				});
//			} else {
//				topBar.setRightText("编辑");
//				topBar.setRightClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						adapter.setDelVisible(true);
//						initTopBar();
//						adapter.notifyDataSetChanged();
//					}
//				});
//			}
//		}
	}

	private void queryAllCrop() {
		mBaseActivity.startLoadingDialog();
		RequestService.getInstance().getCropSubscriptions(getActivity(), CropSubscriptionsEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					entity = (CropSubscriptionsEntity) resultData;
					if (entity.data != null && entity.data.subscriptions != null) {
						setData(entity.data.subscriptions);
					}
				} else {
					mBaseActivity.showToast(R.string.please_check_netword);
				}
				mBaseActivity.dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				mBaseActivity.dismissLoadingDialog();
			}
		});
	}

	protected void setData(List<Subscriptions> subscriptions) {
		adapter = new MyFieldCropAdapter(getActivity(), subscriptions, false);
		adapter.setOnDelClickListener(new OnDelClickListener() {

			@Override
			public void onClick(int position) {
				Subscriptions item = entity.data.subscriptions.get(position);
				deleteItem(item);
			}
		});
		lv_crop.setAdapter(adapter);
		initTopBar();
	}

	protected void deleteItem(final Subscriptions item) {
		// TODO 删除条目
		mBaseActivity.startLoadingDialog();
		String id = String.valueOf(item.id);
		RequestService.getInstance().deleteCropSubscription(getActivity(), id, BaseEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					entity.data.subscriptions.remove(item);
					adapter.setData(entity.data.subscriptions);
					adapter.notifyDataSetChanged();
					mBaseActivity.showToast("删除成功");
				} else if (resultData.getRespCode().equals("404")) {
					mBaseActivity.showToast("请勿重复删除");
				} else {
					mBaseActivity.showToast(R.string.please_check_netword);
				}
				mBaseActivity.dismissLoadingDialog();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				mBaseActivity.dismissLoadingDialog();
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		queryAllCrop();
	}

}
