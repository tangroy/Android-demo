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
import android.widget.RelativeLayout;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.widget.MyWebView;

/**
 * 使用帮助界面
 * @author 杨庆雷
 * 2015-9-1上午11:08:31
 */
public class RunsettintActivity extends BaseActivity implements OnClickListener {
 	private ImageView iv_back;
 	private RelativeLayout rl_runset_lyd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_runsetting);
 		iv_back = (ImageView) findViewById(R.id.iv_back);
 		rl_runset_lyd = (RelativeLayout) findViewById(R.id.rl_runset_lyd);
		iv_back.setOnClickListener(this);
		rl_runset_lyd.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.equals(iv_back)) {
			finish();
		}
		if (arg0.equals(rl_runset_lyd)) {
			Intent intent = new Intent(this, RunSoundSettingActivity.class);
			startActivity(intent);
		}

	}
}
