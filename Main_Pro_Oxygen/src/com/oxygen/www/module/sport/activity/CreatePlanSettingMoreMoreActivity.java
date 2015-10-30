package com.oxygen.www.module.sport.activity;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.module.sport.eventbus_enties.MoreMore;

import de.greenrobot.event.EventBus;

/**
 * 发起活动 - 设置报名者填写项
 * 
 * @author 张坤
 * 
 */
public class CreatePlanSettingMoreMoreActivity extends BaseActivity implements OnClickListener {
	
	private SharedPreferences sp;
	
	private ImageView iv_back;
	private TextView tv_save;
	
//	private RelativeLayout rl_name, rl_mobile, rl_number, rl_department, rl_company, rl_email, rl_remarks;
	private CheckBox cb_name, cb_mobile, cb_number, cb_department, cb_company, cb_email, cb_remarks;
	private ArrayList<CheckBox> cbs;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_createplan_settingmoremore);

		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		
		
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_save = (TextView) findViewById(R.id.tv_save);
		
//		rl_name = (RelativeLayout) findViewById(R.id.rl_name);
//		rl_mobile = (RelativeLayout) findViewById(R.id.rl_mobile);
//		rl_name = (RelativeLayout) findViewById(R.id.rl_number);
//		rl_department = (RelativeLayout) findViewById(R.id.rl_department);
//		rl_company = (RelativeLayout) findViewById(R.id.rl_company);
//		rl_email = (RelativeLayout) findViewById(R.id.rl_email);
//		rl_remarks = (RelativeLayout) findViewById(R.id.rl_remarks);

		cb_name = (CheckBox) findViewById(R.id.cb_name);
		cb_mobile = (CheckBox) findViewById(R.id.cb_mobile);
		cb_number = (CheckBox) findViewById(R.id.cb_number);
		cb_department = (CheckBox) findViewById(R.id.cb_department);
		cb_company = (CheckBox) findViewById(R.id.cb_company);
		cb_email = (CheckBox) findViewById(R.id.cb_email);
		cb_remarks = (CheckBox) findViewById(R.id.cb_remarks);
		
		cbs = new ArrayList<CheckBox>();
		cbs.add(cb_name);
		cbs.add(cb_mobile);
		cbs.add(cb_number);
		cbs.add(cb_department);
		cbs.add(cb_company);
		cbs.add(cb_email);
		cbs.add(cb_remarks);
		
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_save.setOnClickListener(this);
		
	}

	private void initValues() {
		
		String moremore = getIntent().getStringExtra("moremore");
		
		sp = getSharedPreferences("com.oxygen.www_", MODE_MULTI_PROCESS);
 		if (moremore == null) {
			// 新建
			moremore = sp.getString("moremore", "0000000");
//			sp = getSharedPreferences("com.oxygen.www_", MODE_PRIVATE);
			
		} 
		
		if (!moremore.equals("0000000")) {
			// 无论 修改 或者 新建 都需要
			// 初始化
			char [] chs = moremore.toCharArray();
			for (int i=0; i<chs.length; i++) {
				if (chs[i] == '1') {
					cbs.get(i).setChecked(true);
				}
				
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.iv_back:
			finish();
			break;

		case R.id.tv_save:
			// 确定
			
			// 临时存储
			StringBuilder moremore = new StringBuilder();
			moremore.append(cb_name.isChecked() ? '1' : '0');
			moremore.append(cb_mobile.isChecked() ? '1' : '0');
			moremore.append(cb_department.isChecked() ? '1' : '0');
			moremore.append(cb_number.isChecked() ? '1' : '0');
			moremore.append(cb_company.isChecked() ? '1' : '0');
			moremore.append(cb_email.isChecked() ? '1' : '0');
			moremore.append(cb_remarks.isChecked() ? '1' : '0');
			sp.edit().putString("moremore", moremore.toString()).commit();
			
			// 通知 活动创建页, 设置报名者填写项
			EventBus.getDefault().post(new MoreMore(moremore.toString()));
			
	        finish();
			break;
			
		default:
			break;
		}
		
	}

}
