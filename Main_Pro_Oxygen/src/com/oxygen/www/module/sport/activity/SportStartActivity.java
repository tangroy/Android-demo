package com.oxygen.www.module.sport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.Event;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.UserInfoUtils;

/**
 * 立即开始运动页
 * 
 * @author kunyuan
 */
public class SportStartActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_sport,iv_back;
	private ImageButton ib_start, ib_input;
	private RelativeLayout rl_updatesport;
	private String sport_eng = Constants.sports[0];
	private int sportcategory;
	private int requestCode = 1;
	String type;
	Event event;
	private boolean isFromEventsResult;
	/**
	 * 判断是否可以立即开始，只能手动纪录为false
	 */
	private boolean isstart = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sportstart);
		type = getIntent().getStringExtra("type");
		isFromEventsResult = getIntent().getBooleanExtra("isFromEventsResult",
				false);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		rl_updatesport = (RelativeLayout) findViewById(R.id.rl_updatesport);
		iv_sport = (ImageView) findViewById(R.id.iv_sport);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		ib_start = (ImageButton) findViewById(R.id.ib_start);
		ib_input = (ImageButton) findViewById(R.id.ib_input);

	}

	private void initViewsEvent() {
		rl_updatesport.setOnClickListener(this);
		ib_start.setOnClickListener(this);
		ib_input.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}

	private void initValues() {
		if (Constants.SPORTTYPE_CREATED.equals(type)) {
			sport_eng = (String) UserInfoUtils.getUserInfo(
					getApplicationContext(), Constants.DEFT_SPORT_TYPE,
					Constants.sports[0]);
		} else {
			event = (Event) getIntent().getSerializableExtra("event");
			sport_eng = event.getSport_eng();
		}
		sportcategory = GDUtil.SportCategory(sport_eng);
		iv_sport.setImageDrawable(GDUtil.engSporttodrawable(this, "icon_start"
				+ sport_eng));
		if (sportcategory == Constants.COUNT_CATEGORY_PLANK
				|| sportcategory == Constants.COUNT_CATEGORY_DISTANCE
				|| sportcategory == Constants.COUNT_CATEGORY_RUNNING) {
			isstart = true;
			ib_input.setVisibility(View.VISIBLE);
			ib_start.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_st_start));
		} else {
			isstart = false;
			ib_input.setVisibility(View.GONE);
			ib_start.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_start_input));
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		ib_start.setEnabled(true);
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.rl_updatesport:

			if (isFromEventsResult) {
				return;
			}

			Intent intent_choose = new Intent(SportStartActivity.this,
					ChooseSportActivity.class);
			intent_choose.putExtra("type", Constants.SPORTTYPE_UPDATE);
			startActivityForResult(intent_choose, requestCode);
			break;
		case R.id.ib_input:
			Intent intent_input = new Intent(SportStartActivity.this,
					EditRusultActivity.class);
			if (Constants.SPORTTYPE_CREATED.equals(type)) {
				intent_input.putExtra("type", Constants.SPORTTYPE_CREATED);
				intent_input.putExtra("sport_eng", sport_eng);
			} else {
				intent_input.putExtra("type", Constants.SPORTTYPE_UPDATE);
				intent_input.putExtra("event", event);
			}
			startActivity(intent_input);
			break;
		case R.id.ib_start:
			if (isstart) {
				ib_start.setEnabled(false);
				Intent intent = null;
				// 平板支撑
				if (sportcategory == Constants.COUNT_CATEGORY_PLANK) {
					intent = new Intent(SportStartActivity.this,
							StartPlankActivity.class);
				}
				// 跑步或者距离类
				else {
					intent = new Intent(SportStartActivity.this,
							RunActivity.class);
					intent.putExtra("event", event);
				}

				if (Constants.SPORTTYPE_CREATED.equals(type)) {
					intent.putExtra("sport_eng", sport_eng);
					intent.putExtra("type", Constants.SPORTTYPE_CREATED);
				} else {
					if (sportcategory == Constants.COUNT_CATEGORY_PLANK) {
						intent.putExtra("event", event);
					} else {
						intent.putExtra("gdactivity", event.getPerformance());
					}
					intent.putExtra("type", Constants.SPORTTYPE_UPDATE);

				}

				startActivity(intent);
				finish();
			} else {
				Intent intent_input1 = new Intent(SportStartActivity.this,
						EditRusultActivity.class);
				if (Constants.SPORTTYPE_CREATED.equals(type)) {
					intent_input1.putExtra("type", Constants.SPORTTYPE_CREATED);
					intent_input1.putExtra("sport_eng", sport_eng);
				} else {
					intent_input1.putExtra("type", Constants.SPORTTYPE_UPDATE);
					intent_input1.putExtra("event", event);
				}
				startActivity(intent_input1);
			}
			break;
		default:
			break;
		}

	}

	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// 可以根据多个请求代码来作相应的操作
		if (arg0 == requestCode & arg2 != null) {
			// 保存当前的运动类型 下次开始运动时 默认选择这次运动 不需要用户再次选择运动类型
			sport_eng = arg2.getStringExtra("sport");
			UserInfoUtils.setUserInfo(getApplicationContext(),
					Constants.DEFT_SPORT_TYPE, sport_eng);
			initValues();
		}
	};

}