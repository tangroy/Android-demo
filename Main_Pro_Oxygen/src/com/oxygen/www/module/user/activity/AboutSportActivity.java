package com.oxygen.www.module.user.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.utils.GDUtil;



public class AboutSportActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_back;
	private TextView version_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutsport);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		version_name = (TextView) findViewById(R.id.version_name);
	}
	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
	}

	private void initValues() {
		String versionName = GDUtil.GetVersionCodename(this);
		version_name.setText("android V "+versionName);
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
