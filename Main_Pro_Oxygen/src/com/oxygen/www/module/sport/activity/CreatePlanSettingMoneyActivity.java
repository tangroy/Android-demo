package com.oxygen.www.module.sport.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;

/**
 * 活动创建-付费设置 
 *  
 * @author 张坤
 * 
 */
public class CreatePlanSettingMoneyActivity extends BaseActivity implements OnClickListener {
	
	private ImageView iv_back;
	private TextView tv_save;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_createplan_settingtime);

		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_save = (TextView) findViewById(R.id.tv_save);
	}

	private void initViewsEvent() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(this);
		tv_save.setOnClickListener(this);
	}

	private void initValues() {
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.iv_back:
			finish();
			break;

		case R.id.tv_save:
			
			break;
			
		default:
			break;
		}
		
	}
	
}
