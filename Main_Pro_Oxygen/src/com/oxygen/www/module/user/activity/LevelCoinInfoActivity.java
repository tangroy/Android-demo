package com.oxygen.www.module.user.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.widget.MyWebView;

public class LevelCoinInfoActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_back;
	private RelativeLayout rl_web;
//	private WebView webView;
	private MyWebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_levelcoininfo);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		rl_web = (RelativeLayout) findViewById(R.id.rl_web);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
	}

	private void initValues() {
//		webView = new WebView(getApplication());
		webView = new MyWebView(getApplication());
		webView.loadUrl(UrlConstants.API_PREFIX + "/awstats/mylevel_declare.html");
		webView.setBackgroundColor(getResources().getColor(R.color.transparent3));
		rl_web.addView(webView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		 super.onDestroy();
		 if (webView != null) {
			 webView.removeAllViews();
			 webView.destroy();
			 webView = null;
		}
	}
}
