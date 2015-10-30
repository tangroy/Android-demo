package com.oxygen.www.module.sport.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;

public class EventsResultIntroActivity extends BaseActivity implements OnClickListener {

	private ImageView iv_back;
	private TextView tv_intro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_eventresult_intro);
		
		initViews();
		initEvents();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_intro = (TextView) findViewById(R.id.tv_intro);
	}

	private void initEvents() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(this);
	}

	private void initValues() {
		// TODO Auto-generated method stub
		String intro = "";
		intro = getIntent().getStringExtra("intro");
		tv_intro.setText(intro);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}
}
