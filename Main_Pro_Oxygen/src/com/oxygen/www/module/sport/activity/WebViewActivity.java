package com.oxygen.www.module.sport.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.module.challengs.activity.ChallengesDetailActivity;
import com.oxygen.www.widget.MyWebView;

/**
 * 动态-Banner-跳转
 * 
 * @author 张坤
 *
 */
public class WebViewActivity extends Activity implements OnClickListener {
	
	private RelativeLayout rl_top;
	private ImageView iv_back;
	private TextView tv_title;
	private MyWebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		rl_top = (RelativeLayout) findViewById(R.id.rl_top);	
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		webView = (MyWebView) findViewById(R.id.webview);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		initWebView();
	}

	@SuppressLint("SetJavaScriptEnabled") 
	private void initWebView() {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		// 只让本应用程序的webview加载网页，而不调用外部浏览器
		webView.setWebViewClient(new WebViewClient(){
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("leyundong://")) {
					// leyundong://event/123
					
					Intent intent;
					int id;
					id = Integer.parseInt(url.substring(url.lastIndexOf("/") +1));
					if (url.contains("event")) {
						intent = new Intent(WebViewActivity.this, EventsResultActivity.class);
						intent.putExtra("eventid", id);
						startActivity(intent);
						
					} else if (url.contains("challenge")) {
						intent = new Intent(WebViewActivity.this, ChallengesDetailActivity.class);
						intent.putExtra("challengesid", id);
						startActivity(intent);
					}
					
				} else {
					view.loadUrl(url);
				}
				return true;
			}
		});
	}

	private void initValues() {
		Intent intent = getIntent();
		String target_url = intent.getStringExtra("target_url");
		String htmldata = intent.getStringExtra("htmldata");
		boolean hasTitle = intent.getBooleanExtra("hasTitle", false);
		if (hasTitle) {
			rl_top.setVisibility(View.VISIBLE);
			tv_title.setText(intent.getStringExtra("title"));
		}
		if (target_url != null) {
			
			webView.loadUrl(target_url);
		} else if (htmldata != null) {
			webView.loadData(htmldata, "text/html; charset=UTF-8", null);
		} else {
			
		}
		webView.setBackgroundColor(getResources().getColor(R.color.transparent3));
		
	}

	
	 @Override
	 public void onDestroy() {
		 super.onDestroy();
		 if(webView != null) {
			 try {
				 webView.removeAllViews();
				 webView.destroy();
				 webView = null;
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }
		 System.exit(0);
		 
	 }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);

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
}
