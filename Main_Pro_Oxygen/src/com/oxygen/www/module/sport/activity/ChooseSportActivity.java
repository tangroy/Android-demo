package com.oxygen.www.module.sport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.module.sport.adapter.ChooseSportAdapter;
import com.oxygen.www.module.team.activity.CreateTeamActivity;
import com.oxygen.www.utils.UserInfoUtils;

/**
 * 选择运动类型
 * @author 杨庆雷
 * 2015-9-25上午11:54:02
 */
public class ChooseSportActivity extends BaseActivity {

	private GridView gridview, gridview_interest;
	private ImageView iv_back;
	private TextView tv_title;
	private TextView lab_xingqu;
	private TextView lab_all;
	private Intent intent;
	private String type;
	private String[] interest_sport;
	private String fromTeamIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choosesport);
		gridview = (GridView) findViewById(R.id.gridview_all);
		gridview_interest = (GridView) findViewById(R.id.gridview_interest);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		lab_xingqu = (TextView) findViewById(R.id.lab_xingqu);
		lab_all = (TextView) findViewById(R.id.lab_all);
		intent = this.getIntent();
		type = intent.getStringExtra("type");
		fromTeamIndex = intent.getStringExtra("fromTeamIndex");
		if(!TextUtils.isEmpty(fromTeamIndex)){
			tv_title.setText("标签类型");
			lab_xingqu.setText("感兴趣标签");
			lab_all.setText("全部标签");
		}
		String interest = (String) UserInfoUtils.getUserInfo(getApplicationContext(), Constants.SPORT_SELECTED, "");
		if (interest.length() > 3) {
			interest_sport = interest.replace("[", "").replace("]", "").replace(" ", "")
					.split(",");
			ChooseSportAdapter adapter_interest = new ChooseSportAdapter(this,
					interest_sport);
			gridview_interest.setAdapter(adapter_interest);
			gridview_interest.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					//代表选择活动类型 是创建团队时选择的
					if(!TextUtils.isEmpty(fromTeamIndex)){
						intent.putExtra("sport", Constants.sports[arg2]);
						setResult(RESULT_OK, intent);
					}else{
						intent.putExtra("sport", interest_sport[arg2].trim());
						if (Constants.SPORTTYPE_CREATED.equals(type)) {
							intent.setClass(ChooseSportActivity.this,
									CreatedPlanActivity.class);
							intent.putExtra("type", Constants.SPORTTYPE_CREATED);
							startActivity(intent);
						} else if (Constants.SPORTTYPE_UPDATE.equals(type)) {
							setResult(RESULT_OK, intent);
						}
						finish();
					}
				}
			});

		}
		ChooseSportAdapter adapter_all = new ChooseSportAdapter(this,
				Constants.sports);
		gridview.setAdapter(adapter_all);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//代表选择活动类型 是创建团队时选择的
				if(!TextUtils.isEmpty(fromTeamIndex)){
					intent.putExtra("sport", Constants.sports[arg2]);
					setResult(RESULT_OK, intent);
				}else{
					intent.putExtra("sport", Constants.sports[arg2]);
					if (Constants.SPORTTYPE_CREATED.equals(type)) {
						intent.setClass(ChooseSportActivity.this,
								CreatedPlanActivity.class);
						intent.putExtra("type", Constants.SPORTTYPE_CREATED);
						startActivity(intent);
					} else if (Constants.SPORTTYPE_UPDATE.equals(type)) {
						setResult(RESULT_OK, intent);
					}
				}
				finish();
			}
		});

		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
