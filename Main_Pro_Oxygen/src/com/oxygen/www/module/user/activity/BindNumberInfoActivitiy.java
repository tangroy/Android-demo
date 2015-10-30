package com.oxygen.www.module.user.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.utils.UserInfoUtils;

/**
 * 绑定手机号信息的界面
 * @author 杨庆雷
 * 2015-9-15下午5:18:54
 */
public class BindNumberInfoActivitiy extends BaseActivity implements OnClickListener {
	private TextView binded_number;
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bindnumber_info);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		binded_number = (TextView) findViewById(R.id.binded_number);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
	}

	private void initValues() {
		binded_number.setText("已绑定手机号：  "+(String)UserInfoUtils.getUserInfo(this,Constants.MOBILE, ""));
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
