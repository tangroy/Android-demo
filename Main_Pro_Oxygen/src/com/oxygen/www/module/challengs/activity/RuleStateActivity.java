package com.oxygen.www.module.challengs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;

/**
 * 挑战规则
 * 
 * @author sambatang
 * 
 */
public class RuleStateActivity extends BaseActivity {
	private Intent intent;
	private String type;
	private ImageView iv_back;
	private TextView tv_title, tv_context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challengs_rule);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_context = (TextView) findViewById(R.id.context);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		intent = getIntent();
		type = intent.getStringExtra("type");
		String title = null;
		String context = null;
		if (type.equals(Constants.CHALLENGES_TYPE_PERSON)) {
			title = getResources().getString(R.string.rule_name_person);
			context = getResources().getString(R.string.rule_context_person);
		} else if (type.equals(Constants.CHALLENGES_TYPE_TWOTEAM)) {
			title = getResources().getString(R.string.rule_name_twoteam);
			context = getResources().getString(R.string.rule_context_twoteam);
		} else if (type.equals(Constants.CHALLENGES_TYPE_MORETEAM)) {
			title = getResources().getString(R.string.rule_name_moreteam);
			context = getResources().getString(R.string.rule_context_moreteam);
		}
		tv_title.setText(title);
		tv_context.setText(context);

	}
}
