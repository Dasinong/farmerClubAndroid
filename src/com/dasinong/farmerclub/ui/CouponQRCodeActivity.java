package com.dasinong.farmerclub.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.AllCouponEntity.CouponCampaign;
import com.dasinong.farmerclub.entity.AllCouponEntity.Store;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.GraphicUtils;
import com.lidroid.xutils.BitmapUtils;

public class CouponQRCodeActivity extends BaseActivity {

	private ImageView iv_pic;
	private TextView tv_title;
	private TextView tv_time;
	private ImageView iv_qrcode;
	private TextView tv_coupon_id;
	private String picUrl;
	private String name;
	private String time;
	private long id;
	private TopbarView topBar;
	private List<Store> storeList;
	private LinearLayout ll_exchange_place;
	public static final long DAY_MS =  24 * 60 * 60 * 1000;
	private int amount;
	private TextView tv_amount;
	private String type;
	private String comment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon_qrcode);

		boolean isDaren = SharedPreferencesHelper.getBoolean(this, Field.ISDAREN, false);

		picUrl = getIntent().getStringExtra("picUrl");
		name = getIntent().getStringExtra("name");
		long claimedTime = getIntent().getLongExtra("time", 0);
		if (isDaren) {
			time = time2String(claimedTime + 10 * DAY_MS, claimedTime + 30 * DAY_MS);
		} else {
			time = time2String(claimedTime, claimedTime + 30 * DAY_MS);
		}
		id = getIntent().getLongExtra("id", -1);
		amount = getIntent().getIntExtra("amount", 0);
		type = getIntent().getStringExtra("type");
		comment = getIntent().getStringExtra("comment");
		storeList = (List<Store>) getIntent().getSerializableExtra("stores");

		initView();

		setData();

		formatData(storeList);

	}

	private void formatData(List<Store> storeList) {
		Map<String, List<Store>> map = new HashMap<String, List<Store>>();
		if (storeList != null && !storeList.isEmpty()) {
			for (int i = 0; i < storeList.size(); i++) {
				if (!map.containsKey(storeList.get(i).province)) {
					List<Store> list = new ArrayList<Store>();
					list.add(storeList.get(i));
					map.put(storeList.get(i).province, list);
				} else {
					map.get(storeList.get(i).province).add(storeList.get(i));
				}
			}
		}
		setStoreData(map);
	}

	private void setStoreData(Map<String, List<Store>> map) {
		Set<String> keySet = map.keySet();
		for (String province : keySet) {
			TextView tv = new TextView(this);
			LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, GraphicUtils.dip2px(this, 32.5f));
			tv.setText(province);
			tv.setPadding(GraphicUtils.dip2px(this, 15), 0, 0, 0);
			tv.setTextColor(getResources().getColor(R.color.color_666666));
			tv.setBackgroundResource(R.color.color_F5F5F5);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

			tv.setLayoutParams(params);
			ll_exchange_place.addView(tv);
			for (Store store : map.get(province)) {
				View view = View.inflate(this, R.layout.item_exchange_place, null);
				TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
				TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
				TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);

				tv_name.setText(store.name);
				tv_address.setText(store.location);
				tv_phone.setText(store.phone);

				LinearLayout.LayoutParams dividerParams = new LayoutParams(LayoutParams.MATCH_PARENT, GraphicUtils.dip2px(this, 0.5f));
				View divider = new View(this);
				divider.setBackgroundResource(R.color.color_DBE3E5);
				divider.setLayoutParams(dividerParams);
				view.setPadding(0, GraphicUtils.dip2px(this, 15), 0, GraphicUtils.dip2px(this, 15));
				ll_exchange_place.addView(view);
				ll_exchange_place.addView(divider);
			}
		}
	}

	private void initView() {
		topBar = (TopbarView) findViewById(R.id.topbar);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_amount = (TextView) findViewById(R.id.tv_amount);
		iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
		tv_coupon_id = (TextView) findViewById(R.id.tv_coupon_id);
		ll_exchange_place = (LinearLayout) findViewById(R.id.ll_exchange_place);
	}

	private void setData() {
		topBar.setCenterText("大户俱乐部活动");
		topBar.setLeftView(true, true);
		BitmapUtils bitmapUtils = new BitmapUtils(this);
		bitmapUtils.display(iv_pic, NetConfig.COUPON_IMAGE + picUrl);

		tv_title.setText(name);
		tv_time.setText("兑换日期：" + time);
		if("INSURANCE".equals(type)){
			tv_amount.setVisibility(View.VISIBLE);
			comment.replace("；","  ");
			tv_amount.setText("你购买了：" + comment );
		}
		bitmapUtils.display(iv_qrcode, NetConfig.COUPON_QRCODE_URL + id + ".png");
		tv_coupon_id.setText("券号 " + id);
	}

	private String time2String(long start, long end) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		Date date = new Date();

		date.setTime(start);
		String strStart = sdf.format(date).toString();

		date.setTime(end);
		String strEnd = sdf.format(date).toString();

		return strStart + "-" + strEnd;
	}
}
