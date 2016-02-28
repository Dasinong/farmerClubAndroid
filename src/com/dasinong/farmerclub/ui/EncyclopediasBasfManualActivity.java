package com.dasinong.farmerclub.ui;

import java.util.HashMap;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * @ClassName EncyclopediasPesticideActivity
 * @author linmu
 * @Decription basf产品手册
 */
public class EncyclopediasBasfManualActivity extends BaseActivity implements OnClickListener{

	private TopbarView mTopbarView;

	private EditText mSearchEdit;
	private RelativeLayout mAskforLayout;
	private RelativeLayout mNongyaoLayout;
	private RelativeLayout mBinghaiLayout;
	private RelativeLayout mIntelligentLayout;
	private RelativeLayout mShamanjiLayout;
	
	private ImageView mSearchView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basf_manual);
		
		initView();
		setUpView();
		
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mSearchEdit = (EditText) this.findViewById(R.id.edittext_search);
		// 杀菌剂
		mAskforLayout = (RelativeLayout) this.findViewById(R.id.layout_ask_for);
		// 杀虫剂
		mNongyaoLayout = (RelativeLayout) this.findViewById(R.id.layout_nongyao);
		// 除草剂
		mBinghaiLayout = (RelativeLayout) this.findViewById(R.id.layout_bingchongcaohai);
		// 种衣剂
		mIntelligentLayout = (RelativeLayout) this.findViewById(R.id.layout_intelligent);
		// 公共卫生
		mShamanjiLayout = (RelativeLayout) this.findViewById(R.id.layout_shamanji);
		
		mSearchView = (ImageView) this.findViewById(R.id.imageview_search);
	}

	private void setUpView() {
		mTopbarView.setCenterText("巴斯夫产品介绍");
		mTopbarView.setLeftView(true, true);
		
		mAskforLayout.setOnClickListener(this);
		mNongyaoLayout.setOnClickListener(this);
		mBinghaiLayout.setOnClickListener(this);
		mIntelligentLayout.setOnClickListener(this);
		mShamanjiLayout.setOnClickListener(this);
		
		mSearchEdit.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
					
//					DeviceHelper.hideIME(mSearchEdit);
					
					search();
					return true;
				}
				return false;
			}

			
		});
		
		mSearchView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				search();
			}
		});
	}
	
	private void search() {
		String keywords = mSearchEdit.getText().toString().trim();
		if(TextUtils.isEmpty(keywords)){
			Toast.makeText(this, "请输入要搜索的内容", 0).show();
			return;
		}
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("keyWords",keywords);
		MobclickAgent.onEvent(this, "Search",map);
		
		Intent intent = new Intent(this,SearchTypeResultActivity.class);
		intent.putExtra("keywords", keywords);
		intent.putExtra("type", "cpproduct");
		this.startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_ask_for:
			Intent intent1 = new Intent(this,SearchBasfPesticideResultActivity.class);
			intent1.putExtra("type", "杀菌剂");
			intent1.putExtra("manufacturer", "巴斯夫");
			startActivity(intent1);
			break;
		case R.id.layout_nongyao:
			Intent intent2 = new Intent(this,SearchBasfPesticideResultActivity.class);
			intent2.putExtra("type", "杀虫剂");
			intent2.putExtra("manufacturer", "巴斯夫");
			startActivity(intent2);
			break;
		case R.id.layout_bingchongcaohai:
			Intent intent3 = new Intent(this,SearchBasfPesticideResultActivity.class);
			intent3.putExtra("type", "除草剂");
			intent3.putExtra("manufacturer", "巴斯夫");
			startActivity(intent3);
			break;
		case R.id.layout_intelligent:
			Intent intent4 = new Intent(this,SearchBasfPesticideResultActivity.class);
			intent4.putExtra("type", "种衣剂");
			intent4.putExtra("manufacturer", "巴斯夫");
			startActivity(intent4);
			break;
		case R.id.layout_shamanji:
			Intent intent5 = new Intent(this,SearchBasfPesticideResultActivity.class);
			intent5.putExtra("type", "专业解决方案 有害生物控制");
			intent5.putExtra("manufacturer", "巴斯夫");
			startActivity(intent5);
			break;
		}
	}
	
}
