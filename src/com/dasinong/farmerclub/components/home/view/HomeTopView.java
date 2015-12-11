package com.dasinong.farmerclub.components.home.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.components.home.view.popupwidow.CommSelectPopWindow;
import com.dasinong.farmerclub.entity.WeatherEntity;
import com.dasinong.farmerclub.entity.WeatherSubscriptionListEntity;
import com.dasinong.farmerclub.entity.WeatherSubscriptionListEntity.SubscriptionEntity;
import com.dasinong.farmerclub.ui.NotFarmWorkActivity;
import com.umeng.analytics.MobclickAgent;

public class HomeTopView extends LinearLayout implements View.OnClickListener {

	public static final String FARMWORK = "farmwork";
	public static final String PESTICIDE = "pesticide";
	private View mRoot;
	private TextView tvPlace;
	private View addPlace;
	private TextView tvData;
	private TextView tvWeek;
	private TextView tvLunar;
	private TextView tvHumidity;
	private TextView tvLeftStatus;
	private TextView tvRightStatus;
	private OnAddButtonClick onAddButtonClick;
	private Context context;
	private String[] weeks;
	private List<String> mPlaceList = new ArrayList<String>();
	private CommSelectPopWindow popWindow;
	private String mCurrentPlaceName;

	public HomeTopView(Context context) {
		super(context);
		this.context = context;
		init();

	}

	public HomeTopView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public HomeTopView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}

	private void init() {
		mRoot = View.inflate(context, R.layout.view_home_top, this);
		initView();
	}

	public void setOnAddButtonClick(OnAddButtonClick onAddButtonClick) {
		this.onAddButtonClick = onAddButtonClick;
	}

	private void initView() {
		weeks = getResources().getStringArray(R.array.weeks);

		tvPlace = (TextView) mRoot.findViewById(R.id.place);
		addPlace = mRoot.findViewById(R.id.add_place);
		tvData = (TextView) mRoot.findViewById(R.id.data);
		tvWeek = (TextView) mRoot.findViewById(R.id.week);
		tvLunar = (TextView) mRoot.findViewById(R.id.lunar);
		tvHumidity = (TextView) mRoot.findViewById(R.id.humidity);
		tvLeftStatus = (TextView) mRoot.findViewById(R.id.left_status);
		tvRightStatus = (TextView) mRoot.findViewById(R.id.right_status);

		addPlace.setOnClickListener(this);
		tvPlace.setOnClickListener(this);
	}

	public void setDate(WeatherEntity entity) {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		setText2TextView(tvData, month + "月" + entity.data.date.date + "日");

		setText2TextView(tvWeek, weeks[Integer.valueOf(entity.data.date.day)]);
		setText2TextView(tvLunar, entity.data.date.lunar);
		setText2TextView(tvHumidity, entity.data.soilHum);
		setWorkState(entity.data.workable, entity.data.sprayable);
	}

	public void updatePlaceNameMenue(WeatherSubscriptionListEntity entity) {

		tvPlace.setClickable(true);
		mPlaceList.clear();
		// 有田地
		for (SubscriptionEntity itemEntity : entity.data) {
			mPlaceList.add(itemEntity.locationName);
		}

		if (mPlaceList.size() > 1) {
			// 当有2个或者两个以上的农田时，才显示下拉的箭头图标
			tvPlace.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.down_arrow), null);
			tvPlace.setPadding(0, 0, 20, 0);
		}
		mCurrentPlaceName = mPlaceList.get(0);
		tvPlace.setText(mPlaceList.get(0));
	}

	private void setWorkState(int isWork, int isSpray) {
		// 值我改成了1，0，1是宜，0是不宜，-1是不显示
		tvLeftStatus.setVisibility(View.VISIBLE);
		tvRightStatus.setVisibility(View.VISIBLE);
		if (isWork == 1) {
			tvLeftStatus.setText("宜下地");
			tvLeftStatus.setClickable(false);
		} else if (isWork == 0) {
			tvLeftStatus.setText("不宜下地");
			tvLeftStatus.setClickable(true);
		} else {
			tvLeftStatus.setVisibility(View.GONE);
		}
		if (isSpray == 1) {
			tvRightStatus.setText("宜打药");
			tvRightStatus.setClickable(false);
		} else if (isSpray == 0) {
			tvRightStatus.setText("不宜打药");
			tvRightStatus.setClickable(true);
		} else {
			tvRightStatus.setVisibility(View.GONE);
		}
	}

	private void setText2TextView(TextView textView, String textValue) {
		if (!TextUtils.isEmpty(textValue) && null != textView) {
			textView.setText(textValue);
		}
	}

	public void setCenterString(String locationName) {
		if (!TextUtils.isEmpty(locationName)) {
			tvPlace.setText(locationName);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_place:
			onAddButtonClick.onClick();
			break;
		case R.id.left_status:
			MobclickAgent.onEvent(context, "NoFarming");
			Intent farmIntent = new Intent(context, NotFarmWorkActivity.class);
			farmIntent.putExtra("type", FARMWORK);
			context.startActivity(farmIntent);
			break;
		case R.id.right_status:
			MobclickAgent.onEvent(context, "NoSpray");
			Intent pesticideIntent = new Intent(context, NotFarmWorkActivity.class);
			pesticideIntent.putExtra("type", PESTICIDE);
			context.startActivity(pesticideIntent);
			break;
		case R.id.place:
			if (null == mPlaceList || mPlaceList.size() <= 1) {
				return;
			}
			initPoPuWindow();
			popWindow.showAsDropDown(tvPlace);
			break;
		}
	}
	
	private void initPoPuWindow() {
		if (popWindow != null) {
			return;
		}
		popWindow = new CommSelectPopWindow(context);
		popWindow.setDatas(mPlaceList);
		popWindow.setPopWidth(tvPlace.getMeasuredWidth());
		popWindow.setmPopItemSelectListener(new CommSelectPopWindow.PopItemSelectListener() {


			@Override
			public void itemSelected(CommSelectPopWindow window, int position, CharSequence fieldName) {

				if (fieldName.toString().equalsIgnoreCase(mCurrentPlaceName)) {
					// 点击的位置和当前显示的一样--不请求网络
					return;
				}
				mCurrentPlaceName = fieldName.toString();

				updatePlaceName();
				popWindow.disMiss();
				if (null != onAddButtonClick) {
					onAddButtonClick.onPopWindowItemClick(mCurrentPlaceName);
				}
			}
		});
	}
	
	private void updatePlaceName() {
		if (!TextUtils.isEmpty(mCurrentPlaceName)) {
			tvPlace.setText(mCurrentPlaceName);
		}
	}

	public interface OnAddButtonClick {
		public void onClick();
		public void onPopWindowItemClick(String currentPlaceName);
	}
}
