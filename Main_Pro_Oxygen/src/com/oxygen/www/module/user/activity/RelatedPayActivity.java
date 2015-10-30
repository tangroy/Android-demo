package com.oxygen.www.module.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;

/**
 * (设置)收款账户
 * @author 杨庆雷
 * 2015-8-17下午2:57:15
 */
public class RelatedPayActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_back;
	private RelativeLayout rl_bank_number;
	private String balance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relatedpay);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		rl_bank_number = (RelativeLayout) findViewById(R.id.rl_bank_number);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		rl_bank_number.setOnClickListener(this);
	}

	private void initValues() {
		balance = getIntent().getExtras().getString("acount_balance");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		case R.id.rl_bank_number:
			Intent intent = new Intent(this,SetBankNumberActivity.class);
			intent.putExtra("acount_balance", balance);
			startActivity(intent);
			break;
			
		default:
			break;
		}
	}
}
