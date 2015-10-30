package com.oxygen.www.module.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.widget.MyWebView;

/**
 * 使用帮助界面
 * @author 杨庆雷
 * 2015-9-1上午11:08:31
 */
public class HelpActivity extends BaseActivity implements OnClickListener {
	private MyWebView web_detail;
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		web_detail = (MyWebView) findViewById(R.id.web_detail);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		WebSettings webSettings = web_detail.getSettings();
		webSettings.setJavaScriptEnabled(true);

		web_detail.setWebViewClient(new WebViewClient()

		{
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				if (url.startsWith("tel:")) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(url));
					startActivity(intent);
				}
				return true;
			}

		});
		web_detail.loadUrl(UrlConstants.HELP_URL);
	}

	/**
	 * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && web_detail.canGoBack()) {
			// 返回键退回
			web_detail.goBack();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.equals(iv_back)) {
			finish();
		}

	}
}
