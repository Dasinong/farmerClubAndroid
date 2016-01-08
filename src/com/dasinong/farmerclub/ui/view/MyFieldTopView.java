package com.dasinong.farmerclub.ui.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.components.home.view.dialog.SubStageDialog;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.FieldDetailEntity.Crop;
import com.dasinong.farmerclub.entity.FieldDetailEntity.StageEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.net.RequestService;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyFieldTopView extends LinearLayout {

	private Context mContext;
	private ImageView iv_crop_pic;
	private TextView tv_crop_name;
	private TextView tv_field_size;
	private LinearLayout ll_substage;
	private List<StageEntity> mSubStages;
	private SubStageDialog confirmDialog;
	private int mCurrentPosition;
	private TextView substage_name_text;
	private OnStageItemClickListener mOnStageItemClickListener;

	// private int fieldId;

	public MyFieldTopView(Context context) {
		this(context, null);
	}

	public MyFieldTopView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyFieldTopView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init(context);
	}

	private void init(Context context) {
		initView(context);
		initEvent(context);
	}

	private void initView(Context context) {
		View rootView = View.inflate(context, R.layout.view_my_field_top, this);
		iv_crop_pic = (ImageView) rootView.findViewById(R.id.iv_crop_pic);
		tv_crop_name = (TextView) rootView.findViewById(R.id.tv_crop_name);
		tv_field_size = (TextView) rootView.findViewById(R.id.tv_field_size);
		substage_name_text = (TextView) rootView.findViewById(R.id.substage_name_text);
		ll_substage = (LinearLayout) rootView.findViewById(R.id.ll_substage);

	}

	public void setData(int currentStageId, Crop crop, double area, List<StageEntity> subStageList) {

		tv_crop_name.setText(crop.cropName);
		BitmapUtils bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.display(iv_crop_pic, NetConfig.PET_IMAGE + crop.iconUrl.replace("/pic/", ""));
		if (area == 0) {
			tv_field_size.setVisibility(View.GONE);
		} else {
			tv_field_size.setText(String.valueOf(area) + " 亩");
		}
		Collections.sort(subStageList);
		this.mSubStages = subStageList;
		// this.fieldId = fieldId;
		if (currentStageId == -1) {
			this.mCurrentPosition = 0;
		} else {
			for (StageEntity subStage : subStageList) {
				if (subStage.subStageId == currentStageId) {
					this.mCurrentPosition = subStageList.indexOf(subStage);
				}
			}
		}
		if (mSubStages == null || mSubStages.isEmpty()) {
			ll_substage.setVisibility(View.GONE);
			return;
		}

		if (mSubStages.get(mCurrentPosition).stageName.equals(mSubStages.get(mCurrentPosition).subStageName)) {
			substage_name_text.setText(mSubStages.get(mCurrentPosition).stageName);
		} else {
			substage_name_text.setText(mSubStages.get(mCurrentPosition).stageName + "-" + mSubStages.get(mCurrentPosition).subStageName);
		}
		ll_substage.setVisibility(VISIBLE);
	}

	public void setOnStageItemClickListener(OnStageItemClickListener onStageItemClickListener) {
		this.mOnStageItemClickListener = onStageItemClickListener;
	}

	private void initEvent(Context context) {
		ll_substage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
	}

	private void showDialog() {

		if (mSubStages == null || mSubStages.isEmpty()) {
			return;
		}

		initDialog();
		if (confirmDialog.isShowing()) {
			return;
		}
		confirmDialog.setDataSource(mSubStages, mCurrentPosition);
		confirmDialog.show();

	}

	private void initDialog() {
		if (null == confirmDialog) {
			confirmDialog = new SubStageDialog(getContext());
			confirmDialog.setOnItemClickListener(new SubStageDialog.OnItemClickListener() {
				@Override
				public void onItemClick(int position) {

					if (mCurrentPosition == position) {
						return;
					}

					// mCurrentPosition = position;

					mOnStageItemClickListener.onClick(position);

					// TODO MING 区分水稻和小麦
					if (mSubStages.get(position).stageName.equals(mSubStages.get(position).subStageName)) {
						substage_name_text.setText(mSubStages.get(position).stageName);
					} else {
						substage_name_text.setText(mSubStages.get(position).stageName + "-" + mSubStages.get(position).subStageName);
					}
					int subStageId = mSubStages.get(position).subStageId;

					// normalParentView.setVisibility(VISIBLE);
					// onAddCropClickListener.onArrowViewClick(position);

				}
			});
		}
	}

	public interface OnStageItemClickListener {
		public void onClick(int position);
	}

}
