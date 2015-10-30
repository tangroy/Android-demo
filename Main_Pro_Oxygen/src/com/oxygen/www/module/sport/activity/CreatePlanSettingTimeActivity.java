package com.oxygen.www.module.sport.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.wheelview.ScreenInfo;
import com.oxygen.www.widget.wheelview.TimePopupWindow;
import com.oxygen.www.widget.wheelview.TimePopupWindow.OnTimeSelectListener;
import com.oxygen.www.widget.wheelview.TimePopupWindow.Type;

/**
 * 修改活动开始和结束时间
 * @author 杨庆雷
 * 2015-7-21上午9:36:13
 */
public class CreatePlanSettingTimeActivity extends BaseActivity implements OnClickListener {
	private final static int START_TIME = 1;
	private final static int END_TIME = 2;
	private ImageView iv_back;
	private TextView tv_save; ;
	private RelativeLayout rl_start_time, rl_sport_endtime;
	private TextView tv_sport_time, tv_sport_endtime;
	private String time = "";
	private String activities_statrtime;
	private String activities_endtime;
	@SuppressLint("SimpleDateFormat") 
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private int dateInitYear;
	private int dateInitMonth;
	private int dateInitDay;
	private int timeInitHour;
	private int timeInitMinute;
	private TimePopupWindow pwTime;
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
		rl_start_time = (RelativeLayout) findViewById(R.id.rl_start_time);
		rl_sport_endtime = (RelativeLayout) findViewById(R.id.rl_sport_endtime);
		tv_sport_time = (TextView) findViewById(R.id.tv_sport_time);
		tv_sport_endtime = (TextView) findViewById(R.id.tv_sport_endtime);
	}

	private void initViewsEvent() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(this);
		tv_save.setOnClickListener(this);
		rl_start_time.setOnClickListener(this);
		rl_sport_endtime.setOnClickListener(this);
//		choosetime.setOnClickListener(this);
	}

	private void initValues() {
		activities_statrtime = getIntent().getStringExtra("sportTime").substring(0, 16);
		activities_endtime = getIntent().getStringExtra("sportTime").substring(17);
		tv_sport_time.setText(activities_statrtime);
		tv_sport_endtime.setText(activities_endtime);

		dateInitYear = Integer.valueOf(activities_statrtime.substring(0, 4));
		dateInitMonth = Integer.valueOf(activities_statrtime.substring(5, 7));
		dateInitDay = Integer.valueOf(activities_statrtime.substring(8, 10));
		timeInitHour = Integer.valueOf(activities_statrtime.substring(11, 13));
		timeInitMinute = Integer.valueOf(activities_statrtime.substring(14, 16));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_save:
			checkTime();
			break;
	
		case R.id.rl_start_time:
			dateInitYear = Integer.parseInt(tv_sport_time.getText().toString().substring(0, 4));
			dateInitMonth = Integer.parseInt(tv_sport_time.getText().toString().substring(5, 7));
			dateInitDay = Integer.parseInt(tv_sport_time.getText().toString().substring(8, 10));
			timeInitHour = Integer.parseInt(tv_sport_time.getText().toString().substring(11, 13));
			timeInitMinute = Integer.parseInt(tv_sport_time.getText().toString().substring(14, 16));
			initTime(START_TIME,"开始时间");
			break;
		
		case R.id.rl_sport_endtime:
			dateInitYear = Integer.parseInt(tv_sport_endtime.getText().toString().substring(0, 4));
			dateInitMonth = Integer.parseInt(tv_sport_endtime.getText().toString().substring(5, 7));
			dateInitDay = Integer.parseInt(tv_sport_endtime.getText().toString().substring(8, 10));
			timeInitHour = Integer.parseInt(tv_sport_endtime.getText().toString().substring(11, 13));
			timeInitMinute = Integer.parseInt(tv_sport_endtime.getText().toString().substring(14, 16));
			initTime(END_TIME,"结束时间");
			break;
		
		default:
			break;
		}
	}
	
	private void initTime(final int flag,String timeType){
		// 时间选择器
		pwTime = new TimePopupWindow(this, Type.ALL);
//		pwTime.setTitle(timeType);
		pwTime.setTime(new Date(dateInitYear,dateInitMonth,dateInitDay,timeInitHour,timeInitMinute));
		pwTime.showAtLocation(tv_sport_endtime, Gravity.BOTTOM, 0, 0,new Date());
		//回调
		pwTime.setOnTimeSelectListener(new OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
            	if(flag == START_TIME){
            		tv_sport_time.setText(getTime(date));
				}else{
					tv_sport_endtime.setText(getTime(date));
				}
            }
        });
//        //弹出时间选择器
//		tv_sport_time.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//            	pwTime.setTime(new Date(dateInitYear,dateInitMonth,dateInitDay,timeInitHour,timeInitMinute));
//				pwTime.showAtLocation(tv_sport_time, Gravity.CENTER, 0, 0,new Date());
//            }
//        });
//		//弹出时间选择器
//		tv_sport_endtime.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				pwTime.setTime(new Date(dateInitYear,dateInitMonth,dateInitDay,timeInitHour,timeInitMinute));
//				pwTime.showAtLocation(tv_sport_endtime, Gravity.CENTER, 0, 0,new Date());
//			}
//		});
	}
	
	public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
	
	/**
	 * 检查时间的合法性
	 */
	private void checkTime() {
		long startime = Long.parseLong(tv_sport_time.getText().toString().trim()
				.replace(" ", "").replace(":", "").replace("-", ""));
		
		long endtime = Long.parseLong(tv_sport_endtime.getText().toString().trim()
				.replace(" ", "").replace(":", "").replace("-", ""));
		
		if (endtime - startime <= 0) {
			ToastUtil.show(CreatePlanSettingTimeActivity.this, "活动结束时间不能早于开始时间");
		} else {
			Intent intent = new Intent();
			time = tv_sport_time.getText().toString().trim()+"~"+tv_sport_endtime.getText().toString().trim();
			intent.putExtra("time", time);
            setResult(RESULT_OK, intent);
            finish();
		}
	}
}
