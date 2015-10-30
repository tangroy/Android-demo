package com.oxygen.www.module.sport.activity;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * 导出 活动报名列表 页
 * 
 * @author 张坤
 *
 */
public class ExportAcceptListActivity extends Activity implements OnClickListener {
	
	private ImageView iv_back;
	private TextView tv_link;
	private TextView tv_copy;
	
	private int event_id;
	private String token;
	private String link;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_export_accept_list);
		
		initViews();
		initEvents();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_link = (TextView) findViewById(R.id.tv_link);
		tv_copy = (TextView) findViewById(R.id.tv_copy);
		
	}

	private void initEvents() {
		iv_back.setOnClickListener(this);
		tv_copy.setOnClickListener(this);
	}

	private void initValues() {
		Intent intent = getIntent();
		event_id = intent.getIntExtra("event_id", 0);
		token = intent.getStringExtra("token");
		
		link = UrlConstants.API_PREFIX + UrlConstants.EVENTS_EXPORT_ACCEPT_LIST + event_id +"?token="+token;
		tv_link.setText(link);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.iv_back:
			finish();
			break;

		case R.id.tv_copy:
			// TODO...
			break;

		default:
			break;
		}
		
	}

}
