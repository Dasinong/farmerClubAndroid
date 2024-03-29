package com.dasinong.farmerclub.components.home.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dasinong.farmerclub.R;

import com.dasinong.farmerclub.entity.BannerEntity;
import com.dasinong.farmerclub.entity.BannerEntity.Banner;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.ui.WebBrowserActivity;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;

public class BannerView extends ViewPager {

	private Context context;
	private BannerEntity entity;

	public BannerView(Context context) {
		super(context);
		this.context = context;
	}

	public BannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void initView(BannerEntity banner) {

		if (banner == null) {
			return;
		}

		if (banner.data == null) {
			return;
		}
		
		if (banner.data.laonongs == null && banner.data.laonongs.isEmpty()){
			return;
		}

		entity = banner;

		List<View> viewList = new ArrayList<View>();
		List<Banner> dataList = banner.data.laonongs;
		
		for (final Banner itemEntity : dataList) {
			switch (itemEntity.type) {
			case 1:
				View adBanner = View.inflate(context, R.layout.view_home_banner_ad, null);
				ImageView adImage = (ImageView) adBanner.findViewById(R.id.banner_ad_img);
				BitmapUtils bitmapUtils = new BitmapUtils(context);
				
				if (!TextUtils.isEmpty(itemEntity.picName)) {
					if(itemEntity.picName.startsWith("http://")){
						bitmapUtils.display(adImage, itemEntity.picName);
					} else {
						bitmapUtils.display(adImage, "http://" + itemEntity.picName);
					}
				}

				viewList.add(adBanner);
				break;
			case 2:
				View warnBanner = View.inflate(context, R.layout.view_home_banner_warn, null);
				ImageView warnIcon = (ImageView) warnBanner.findViewById(R.id.banner_title_container_icon);
				if (!TextUtils.isEmpty(itemEntity.picName)) {

					String name = itemEntity.picName.substring(0, itemEntity.picName.lastIndexOf("."));
					int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
					if (resId != 0) {
						warnIcon.setImageResource(resId);
					}
				}
				TextView warnTitle = (TextView) warnBanner.findViewById(R.id.banner_title_container_title);
				warnTitle.setText(itemEntity.content);

				viewList.add(warnBanner);
				break;
			case 3:
				View sayingBanner = View.inflate(context, R.layout.view_home_banner_saying, null);
				ImageView sayingIcon = (ImageView) sayingBanner.findViewById(R.id.banner_all_container_icon);

				if (!TextUtils.isEmpty(itemEntity.picName)) {
					String name = itemEntity.picName.substring(0, itemEntity.picName.lastIndexOf("."));
					int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());

					if (resId != 0) {
						sayingIcon.setImageResource(resId);
					}
				}

				TextView sayingTitle = (TextView) sayingBanner.findViewById(R.id.banner_all_container_title);
				TextView sayingContent = (TextView) sayingBanner.findViewById(R.id.banner_all_container_content_above);

				sayingTitle.setText(itemEntity.title);
				sayingContent.setText(itemEntity.content);

				viewList.add(sayingBanner);
				break;
			}
		}
		initViewPager(viewList);
	}

	private void initViewPager(final List<View> viewList) {
		if (viewList == null || viewList.isEmpty()) {
			return;
		}

		setAdapter(new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return viewList.size();
			}

			@Override
			public Object instantiateItem(ViewGroup container, final int position) {
				container.addView(viewList.get(position));
				viewList.get(position).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!TextUtils.isEmpty(entity.data.laonongs.get(position).url)) {
							
							MobclickAgent.onEvent(context, "ClickBanner");
							
							Intent intent = new Intent(context, WebBrowserActivity.class);
							if(entity.data.laonongs.get(position).type == 1){
								intent.putExtra(WebBrowserActivity.URL, "http://" + entity.data.laonongs.get(position).url);
							} else if(entity.data.laonongs.get(position).type == 2){
								intent.putExtra(WebBrowserActivity.TITLE, "天气预警");
								intent.putExtra(WebBrowserActivity.URL, NetConfig.BASE_URL + entity.data.laonongs.get(position).url);
							}
							getContext().startActivity(intent);
						}
					}
				});
				return viewList.get(position);
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView(viewList.get(position));
			}

		});
	}
}
