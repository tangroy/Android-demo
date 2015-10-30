package com.oxygen.www.module.team.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.widget.ImageView;
import com.oxygen.www.R;
import com.oxygen.www.widget.MyWebView;

public class GroupDataDetailActivity extends Activity implements
		OnClickListener {
	private MyWebView web_groupdetail;
	private ImageView iv_back;
	private String url;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_groupdatadetail);
		url = getIntent().getExtras().getString("Dataurl");
		
		iv_back = (ImageView) findViewById(R.id.iv_back);
		web_groupdetail = (MyWebView) findViewById(R.id.web_groupdetail);
		
		iv_back.setOnClickListener(this);
		WebSettings webSettings = web_groupdetail.getSettings();
		webSettings.setJavaScriptEnabled(true);
		web_groupdetail.loadUrl(url);
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && web_groupdetail.canGoBack()) {
			web_groupdetail.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		 super.onDestroy();
		 if(web_groupdetail != null) {
			 try {
				 web_groupdetail.removeAllViews();
				 web_groupdetail.destroy();
				 web_groupdetail = null;
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }
	}
	
}
