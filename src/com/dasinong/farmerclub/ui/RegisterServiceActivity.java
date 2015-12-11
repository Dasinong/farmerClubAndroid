package com.dasinong.farmerclub.ui;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.ui.view.TopbarView;

public class RegisterServiceActivity extends BaseActivity {

	private TextView mServiceText;

	private WebView mWebView;

	private TopbarView mTopbarView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register_service);

		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);

		mServiceText = (TextView) this.findViewById(R.id.textview_text);

		mWebView = (WebView) this.findViewById(R.id.webview);
		
		String title = getIntent().getStringExtra("title");
		String url = getIntent().getStringExtra("url");

		mTopbarView.setCenterText(title);
		mTopbarView.setLeftView(true, true);
		
		mWebView.loadUrl("file:///android_asset/" + url);

	}
}
