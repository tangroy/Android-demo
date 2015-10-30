package com.oxygen.www.module.sport.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.db.DBManager;
import com.oxygen.www.enties.Event;
import com.oxygen.www.enties.GDAcvitity;
import com.oxygen.www.enties.Route;
import com.oxygen.www.module.sport.construct.ActivitiesConstruct;
import com.oxygen.www.utils.AMapUtil;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.SoundUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.MyChronometer;
import com.oxygen.www.widget.MyChronometer.OnChronometerTickListener;
import com.oxygen.www.widget.TasksCompletedView;

public class RunActivity extends BaseActivity implements OnClickListener,
		LocationSource, OnChronometerTickListener {
	private final int STARTTIME = 10000;
	public final int NET_CREATEDACTIVITIES = 1;
	public final int LOCAL_CREATEDACTIVITIES = 2;
	public final int TimerTask = 3;
	private TextView tv_journey, tv_startnumber, tv_pace, tv_altitude;
	private TextView tv_starttime;
	private MapView mapView;
	private ImageButton ib_startandsuspend;
	private TasksCompletedView ib_over;
	private LinearLayout ll_over;
	DecimalFormat df = new DecimalFormat("#0.00");
	SimpleDateFormat daf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

	private OnLocationChangedListener mListener;

	private Polyline polyline;
	private AMap aMap;
	private Double geoLat = 0.0;
	private Double geoLng = 0.0;
	private Double geoLat_old = 0.0;
	private Double geoLng_old = 0.0;
	private long newlocationtime = 0;
	private long oldlocationtime = 0;
	private Double start_latitude = 0.0;
	private Double start_longitude = 0.0;
	private String start_address = null;
	/**
	 * 最低海拔
	 */
	private double minAltitude = 0.0;
	/**
	 * 最高海拔
	 */
	private double maxAltitude = 0.0;
	/**
	 * 最低配速
	 */
	private String min_pace = "00'00''";
	/**
	 * 最高配速
	 */
	private String max_pace = "00'00''";
	/**
	 * 开始时间
	 */
	private String start_time;
	private String end_time;
	/**
	 * 移动路程
	 */
	private Double journey = 0.0;

	/**
	 * 每间隔1公里播报一次语音
	 */
	private int journey_interval = 1000;
	/**
	 * 跑步过程中播报语音次数
	 */
	private int sound_count = 0;

	/**
	 * 是否为暂停状态
	 */
	private boolean ispuased = false;
	/**
	 * 记录每一个移动点
	 */
	ArrayList<Route> routes = new ArrayList<Route>();

	private boolean ininmap = true;
	/**
	 * 用户统计点的个数，第四个点开始点击
	 */
	private int point = 0;
	private Intent runServiceIntent;
	private UpdateLinkReceiver updatelinkReceiver;
	Intent coIntent = new Intent(Constants.RECEVIER_RUN_CO);

	private GDAcvitity gdactivity = null;;
	private ProgressBar progressbar;
	private MyChronometer tv_loding;
	private RelativeLayout rl_rundata;
	private ImageView iv_tomap, iv_todate, iv_gps_level;
	private TextView tv_journey_in, tv_starttime_in;
	private View view_bg_running;
	private Intent intent;
	String type;
	private Event event;
	// 请求失败在请求一次
	private boolean fristrequest = true;
	private int startnumber = 3;
	private boolean isfristtime = true;
	private DBManager mgr;
	private Bundle savedInstanceState;
	MyLocationStyle myLocationStyle;
	/**
	 * 当前累计飘移点
	 */
	int count = 0;
	/**
	 * 跑步时时间
	 */
	int runtime = 0;
	/**
	 * 跑步时时间(结束时间－开始时间)
	 */
	int runtime_2 = 0;
	/**
	 * 定位倒计时时间
	 */
	int start_locationtime = 11;

	int locatid = -1;
	TranslateAnimation mShowAction, mHiddenAction;
	/**
	 * 每隔100米计算一次最快最慢配速
	 */
	private int pace_m = 100;
	/**
	 * 计算配速次数
	 */
	private int pace_count = 0;
	/**
	 * 计算配速100米时间
	 */
	private int pace_time = 0;

	Timer timer;
	/**
	 * 暂停点
	 */
	private String pausepoints = "0,";
	/**
	 * 当前暂停点
	 */
	private int currtpause = 0;
	/**
	 * 暂停时间
	 */
	private int pausetime = 0;

	int notification_id = 19901207;
	Notification notification;
	NotificationManager nm;
	private int mTotalProgress = 100;
	private int mCurrentProgress = -1;
	ArrayList<String> sound_run_start = new ArrayList<String>();
	ArrayList<String> sound_run_continue = new ArrayList<String>();
	ArrayList<String> sound_run_over = new ArrayList<String>();
	ArrayList<String> sound_run_pause = new ArrayList<String>();
	/**
	 * 运动类型
	 */
	private int sportcategory = 0;
	private String sport_eng = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sport_run);
		this.savedInstanceState = savedInstanceState;
		initViews();
		initViewsEvent();
		initValues();
		registerReceiver();
	}

	/**
	 * 初始化组件
	 */
	private void initViews() {
		tv_journey = (TextView) findViewById(R.id.tv_journey);
		tv_pace = (TextView) findViewById(R.id.tv_pace);
		tv_starttime = (TextView) findViewById(R.id.tv_starttime);
		ib_over = (TasksCompletedView) findViewById(R.id.ib_over);
		ll_over = (LinearLayout) findViewById(R.id.ll_over);
		ib_startandsuspend = (ImageButton) findViewById(R.id.ib_startandsuspend);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		tv_startnumber = (TextView) findViewById(R.id.tv_startnumber);
		tv_journey_in = (TextView) findViewById(R.id.tv_journey_in);
		tv_starttime_in = (TextView) findViewById(R.id.tv_starttime_in);
		tv_altitude = (TextView) findViewById(R.id.tv_altitude);
		tv_loding = (MyChronometer) findViewById(R.id.tv_loding);
		rl_rundata = (RelativeLayout) findViewById(R.id.rl_rundata);
		iv_tomap = (ImageView) findViewById(R.id.iv_tomap);
		iv_todate = (ImageView) findViewById(R.id.iv_todate);
		iv_gps_level = (ImageView) findViewById(R.id.iv_gps_level);
		view_bg_running = findViewById(R.id.view_bg_running);
	}

	/**
	 * 初始化监听事件
	 */
	private void initViewsEvent() {
		// ib_over.setOnClickListener(this);
		ib_startandsuspend.setOnClickListener(this);
		tv_loding.setOnChronometerTickListener(this);
		iv_tomap.setOnClickListener(this);
		iv_todate.setOnClickListener(this);
		ib_over.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (MotionEvent.ACTION_DOWN == arg1.getAction()) {
					if (mCurrentProgress == mTotalProgress
							|| mCurrentProgress == -1) {
						new Thread(new ProgressRunable()).start();
					}
					mCurrentProgress = 0;
				} else if (MotionEvent.ACTION_UP == arg1.getAction()) {
					if (mCurrentProgress == mTotalProgress) {
						ib_over.setEnabled(false);
						ib_startandsuspend.setEnabled(false);
						timer.cancel();
						if (journey < 10) {
							mindistanceDialog(RunActivity.this);
						} else {
							showDialog(RunActivity.this);
						}
						point = 0;
						startnumber = 3;
						ispuased = true;
						ib_startandsuspend.setImageDrawable(getResources()
								.getDrawable(R.drawable.icon_start));
						ib_over.setProgress(0);
					} else {
						mCurrentProgress = -1;
						ib_over.setProgress(mCurrentProgress);
					}

				}
				return true;
			}
		});
	}

	/**
	 * 初始化默认值
	 */
	private void initValues() {
		ib_startandsuspend.setEnabled(false);
		intent = getIntent();
		type = intent.getStringExtra("type");
		event = (Event) getIntent().getSerializableExtra("event");
		// 缓存本地
		mgr = new DBManager(RunActivity.this);
		if (Constants.SPORTTYPE_UPDATE.equals(type)) {
			gdactivity = (GDAcvitity) getIntent().getSerializableExtra(
					"gdactivity");
			sport_eng = gdactivity.getsport_eng();
		} else {
			sport_eng = intent.getStringExtra("sport_eng");
			gdactivity = new GDAcvitity();
			gdactivity.setIntro("一起运动更快乐，约么？");
			gdactivity.setTilte(GDUtil.engforchn(RunActivity.this,
					"created_type_" + sport_eng));
			gdactivity.setsport_eng(sport_eng);
		}
		sportcategory = GDUtil.SportCategory(sport_eng);
		if (!GDUtil.isGpsEnable(this)) {
			showgps(this);
		} else {
			tv_loding.start();
			Intent mIntent = new Intent();
			mIntent.setAction(Constants.SERVICE_RUN_UPDATELINK);
			Intent eintent = new Intent(GDUtil.getExplicitIntent(this, mIntent));
			startService(eintent);
			// 初始化声音
			sound_run_start.add("run_start");
			sound_run_continue.add("run_continue");
			sound_run_over.add("run_over");
			sound_run_pause.add("run_pause");
			myLocationStyle = new MyLocationStyle();
			myLocationStyle.myLocationIcon(BitmapDescriptorFactory
					.fromResource(R.drawable.mylocayion));// 设置小蓝点的图标
			myLocationStyle.strokeColor(Color.BLUE);// 设置圆形的边框颜色
			myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
			myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
			mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
					0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			mShowAction.setDuration(500);
			mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
					0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -1.0f);
			mHiddenAction.setDuration(500);
			nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notification = new Notification(R.drawable.logo, "跑步开始啦",
					System.currentTimeMillis());
			notification.contentView = new RemoteViews(getPackageName(),
					R.layout.notification);

		}

	}

	private void registerReceiver() {
		// 动态注册广播接收器
		updatelinkReceiver = new UpdateLinkReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.RECEVIER_RUN_UPDATELINK);
		registerReceiver(updatelinkReceiver, intentFilter);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View arg0) {
		if (arg0.equals(ib_startandsuspend)) {
			if (ispuased) {
				SoundUtil.player_array(RunActivity.this, sound_run_continue,
						sportcategory);
				ispuased = false;
				ib_over.setVisibility(View.GONE);
				ll_over.setVisibility(View.GONE);
				view_bg_running.setVisibility(View.GONE);
				timer = new Timer(true);
				MyTimerTask task = new MyTimerTask();
				timer.schedule(task, 0, 1000);
				ib_startandsuspend.setImageDrawable(getResources().getDrawable(
						R.drawable.icon_suspend));
			} else {
				SoundUtil.player_array(RunActivity.this, sound_run_pause,
						sportcategory);
				point = 0;
				startnumber = 3;
				ispuased = true;
				ib_over.setVisibility(View.VISIBLE);
				ll_over.setVisibility(View.VISIBLE);
				timer.cancel();
				ib_startandsuspend.setImageDrawable(getResources().getDrawable(
						R.drawable.icon_start));
				view_bg_running.setVisibility(View.VISIBLE);
				if (routes != null && routes.size() > 0) {
					if (routes.size() != 0 && routes.size() != currtpause) {
						pausepoints = pausepoints + routes.size() + ",";
						currtpause = routes.size();
					}

				}
			}
		}

		else if (arg0.equals(iv_tomap)) {
			rl_rundata.setVisibility(View.GONE);
			rl_rundata.startAnimation(mHiddenAction);

		} else if (arg0.equals(iv_todate)) {
			rl_rundata.setVisibility(View.VISIBLE);
			rl_rundata.startAnimation(mShowAction);
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();

		if (mapView != null)
			mapView.onResume();

	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (mapView != null)
			mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mapView != null)
			mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mgr != null) {
			mgr.closeDB();
		}
		updateserVerStatus(Constants.RUN_SERVICE_CO_KILL);
		if (updatelinkReceiver != null) {
			unregisterReceiver(updatelinkReceiver);
		}
		if (mapView != null)
			mapView.onDestroy();
	}

	/**
	 * 广播接收器
	 * 
	 * @author len
	 * 
	 */
	public class UpdateLinkReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constants.RECEVIER_RUN_UPDATELINK)) {
				AMapLocation location = OxygenApplication.location;
				if (location != null) {
					drawlink(location);
				}
			}

		}

	}

	/**
	 * 控制service改变状态
	 * 
	 * @param action
	 */
	private void updateserVerStatus(int action) {
		coIntent.putExtra("action", action);
		sendBroadcast(coIntent);
	}

	/*
	 * 绘画线路
	 * 
	 * @param location
	 */
	private void drawlink(AMapLocation location) {
		double accuracy = location.getAccuracy();
		if (accuracy < 0) {
			iv_gps_level.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_gps_1));
		} else if (accuracy < 30) {
			iv_gps_level.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_gps_4));

		} else if (accuracy < 100) {
			iv_gps_level.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_gps_3));

		} else {
			iv_gps_level.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_gps_2));

		}
		if (ininmap) {
			ininmap = false;
			mapView = (MapView) findViewById(R.id.map);
			mapView.removeAllViews();
			mapView.onCreate(savedInstanceState);// 此方法必须重写
			if (aMap == null) {
				aMap = mapView.getMap();
				// 自定义系统定位小蓝点
				aMap.setMyLocationStyle(myLocationStyle);
				aMap.setLocationSource(this);// 设置定位监听
				aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
				aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
				UiSettings uiSettings = aMap.getUiSettings();
				uiSettings.setZoomControlsEnabled(false);
			}
			tv_loding.stop();
			tv_loding.setVisibility(View.GONE);
			mapView.setVisibility(View.VISIBLE);

		}
		if (!ispuased) {
			point = point + 1;
			if (point == 1) {
				aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						location.getLatitude(), location.getLongitude()), 18));
				MyCount mc = new MyCount(4000, 1000);
				mc.start();
			}
			if (location != null && point > 2) {
				geoLat = location.getLatitude();
				geoLng = location.getLongitude();
				long locationtime = new Date().getTime();
				newlocationtime = locationtime;
				double Altitude = location.getAltitude();
				// 第一次定位
				if (geoLat_old == 0.0) {
					oldlocationtime = newlocationtime;
					geoLat_old = geoLat;
					geoLng_old = geoLng;
					start_latitude = geoLat;
					start_address = location.getAddress();
					start_longitude = geoLng;
					start_time = daf.format(new Date());
					maxAltitude = Altitude;
					minAltitude = Altitude;

				}
				// 中途偏移矫正
				else if (geoLng_old == -1.0) {
					geoLat_old = geoLat;
					geoLng_old = geoLng;
					oldlocationtime = newlocationtime;
				} else {
					LatLng marker = new LatLng(geoLat, geoLng);
					LatLng old_marker = new LatLng(geoLat_old, geoLng_old);
					double journey_temp = AMapUtils.calculateLineDistance(
							old_marker, marker);
					long time_differ = newlocationtime - oldlocationtime;

					if (journey_temp > 0 && (journey_temp / time_differ) < 10
							&& accuracy < 100 && journey_temp < 100) {
						count = 0;
						if (mListener != null && location != null) {
							mListener.onLocationChanged(location);// 显示系统小蓝点
						}
						// 目的地（target）缩放级别（zoom）
						aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
								marker, 18));
						journey = journey + journey_temp;

						if (Altitude > maxAltitude) {
							maxAltitude = Altitude;
						}
						if (Altitude < minAltitude) {
							minAltitude = Altitude;
						}
						// 计算最高最低配速
						int s = (int) (journey / pace_m);
						if (s > pace_count) {
							if (pace_count == 0) {
								pace_time = runtime_2;
								min_pace = GDUtil.CalculationPace(pace_m,
										pace_time);
								max_pace = min_pace;
							} else {
								String pace = GDUtil.CalculationPace(pace_m,
										runtime_2 - pace_time);
								if (GDUtil.Comparisonpace(pace, max_pace)) {
									max_pace = pace;
								}
								if (GDUtil.Comparisonpace(min_pace, pace)) {
									min_pace = pace;
								}
								pace_time = runtime_2;
							}
							pace_count++;
						}
						end_time = daf.format(new Date());
						runtime_2 = GDUtil.getTimeDiff_send(start_time,
								end_time);
						tv_altitude.setText(""
								+ (int) (maxAltitude - minAltitude));
						polyline = aMap.addPolyline((new PolylineOptions())
								.add(new LatLng(geoLat_old, geoLng_old),
										new LatLng(geoLat, geoLng)).color(
										Color.GREEN));
						tv_journey.setText(df.format(journey / 1000));
						tv_journey_in.setText(df.format(journey / 1000));
						tv_pace.setText(GDUtil.CalculationPace(journey,
								runtime_2));
						polyline.setWidth(10);
						Route route = new Route();
						route.start_latitude = geoLng;
						route.start_longitude = geoLat;
						route.setLocation_time(AMapUtil
								.convertToTimestamp(locationtime));
						routes.add(route);
						geoLat_old = geoLat;
						geoLng_old = geoLng;
						oldlocationtime = newlocationtime;

					} else {
						count = count + 1;
						if (count == 2) {
							// 归零定位
							count = 0;
							geoLng_old = -1.0;
						}

					}

				}
				int interval = (int) (journey / journey_interval);
				if (sound_count < interval) {
					sound_count = interval;
					SoundUtil.player_array(
							RunActivity.this,
							SpeakSoundRunnig(Double.parseDouble(df
									.format(journey / 1000)), runtime_2),
							sportcategory);
				}

			}
		}
	}

	/**
	 * 确认退出框
	 * 
	 * @param content
	 */
	private void showDialog(Context content) {
		// if (progressbar.getVisibility() == View.GONE) {
		// AlertDialog.Builder dialog = new AlertDialog.Builder(content);
		// dialog.setIcon(android.R.drawable.ic_dialog_info)
		// .setCancelable(false)
		// .setMessage("是否继续运动?")
		// .setPositiveButton("继续",
		// new DialogInterface.OnClickListener() {
		//
		// @SuppressLint("NewApi")
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// dialog.cancel();// 取消弹出框
		// SoundUtil.player_array(RunActivity.this,
		// sound_run_continue);
		// ib_over.setEnabled(true);
		// ib_startandsuspend.setEnabled(true);
		// ib_over.setVisibility(View.GONE);
		// ll_over.setVisibility(View.GONE);
		// view_bg_running.setVisibility(View.GONE);
		// ispuased = false;
		// timer = new Timer(true);
		// MyTimerTask task = new MyTimerTask();
		// timer.schedule(task, 0, 1000);
		// // updateserVerStatus(Constants.RUN_SERVICE_CO_PLAY);
		// // geoLng_old = -1.0;
		// ib_startandsuspend
		// .setImageDrawable(getResources()
		// .getDrawable(
		// R.drawable.icon_suspend));
		// }
		// })
		// .setNegativeButton("结束",
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int which) {
		SoundUtil.player_array(RunActivity.this, sound_run_over, sportcategory);
		progressbar.setVisibility(View.VISIBLE);
		tv_loding.setVisibility(View.VISIBLE);
		tv_loding.setText("数据已保存在本地，正在上传到网络...");
		mapView.setVisibility(View.GONE);
		updateserVerStatus(Constants.RUN_SERVICE_CO_KILL);
		toGdactivity();
		if (Constants.SPORTTYPE_UPDATE.equals(type)) {
			updategdactivityNet();
		} else {
			addgdactivityNet();
		}

	}

	// }).create().show();
	// }
	// }

	/**
	 * 播放跑步途中的语音
	 * 
	 * @return
	 */
	private ArrayList<String> SpeakSoundRunnig(double distance, int duration) {
		ArrayList<String> sound_running = new ArrayList<String>();
		sound_running.add("run");
		sound_running.addAll(SoundUtil.player_number(distance));
		sound_running.add("km");
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("costtime");
		sound_running.addAll(SoundUtil.player_time(duration));
		sound_running.add("another_comeon");
		return sound_running;
	}

	private void toGdactivity() {
		gdactivity.setDistance(Double.parseDouble(df.format(journey)));
		gdactivity.setStart_time(start_time);
		gdactivity.setDuration(GDUtil.getTimeDiff_send(start_time, end_time));
		if (maxAltitude - minAltitude != 0)
			gdactivity.setAltitude(df.format((maxAltitude - minAltitude) / 2));
		gdactivity.setlatitude(start_latitude);
		gdactivity.setlongitude(start_longitude);
		gdactivity.setaddresss(start_address);
		gdactivity.setEnd_time(end_time);
		gdactivity.setRoute(routes == null ? null : routes);
		gdactivity.setCreated_time(start_time);
		gdactivity.setStatus("close");
		gdactivity.setPace_max(max_pace);
		gdactivity.setPace_min(min_pace);
	}

	/**
	 * 确认退出框
	 * 
	 * @param content
	 */
	private void showgps(Context content) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(content);
		dialog.setIcon(android.R.drawable.ic_dialog_info).setCancelable(false)
				.setMessage("无法定位，请打开 ‘gps’ 或者 ‘授权定位’ 后重新进入!")
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();// 取消弹出框
						finish();
					}
				}).create().show();
	}

	/**
	 * 重复确认
	 * 
	 * @param content
	 */
	private void DoubleshowDialog(Context content) {
		if (Double.parseDouble(df.format(journey / 1000)) > 0.0) {
			if (progressbar.getVisibility() == View.GONE) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(content);
				dialog.setIcon(android.R.drawable.ic_dialog_info)
						.setCancelable(false)
						.setMessage("网络连接异常，保存失败")
						.setPositiveButton("重试",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										progressbar.setVisibility(View.VISIBLE);
										if (Constants.SPORTTYPE_UPDATE
												.equals(type)) {
											updategdactivityNet();
										} else {
											addgdactivityNet();
										}

									}
								})
						.setNegativeButton("缓存本地",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										locatid = mgr.addsport(gdactivity,
												locatid);
										GDUtil.setGlobal(RunActivity.this,
												"update_loca_data", true);
										nm.cancel(notification_id);
										finish();
									}
								}).create().show();
			}
		} else {
			updateserVerStatus(Constants.RUN_SERVICE_CO_KILL);
			finish();
			nm.cancel(notification_id);
		}
	}

	/**
	 * 立即运动添加activities
	 */
	private void addgdactivityNet() {
		// POST 参数
		final Map<String, String> params = new HashMap<String, String>();
		params.put("sport", sport_eng);
		params.put("distance", Double.parseDouble(df.format(journey)) + "");
		params.put("duration", GDUtil.getTimeDiff_send(start_time, end_time)
				+ "");
		if (maxAltitude - minAltitude != 0)
			params.put("altitude", df.format((maxAltitude - minAltitude) / 2)
					+ "");
		params.put("latitude", start_latitude + "");
		params.put("longitude", start_longitude + "");
		params.put("address", start_address);
		params.put("start_time", start_time);
		params.put("end_time", end_time);
		params.put("route", routes == null ? null : routes.toString());
		params.put("created_at", start_time);
		params.put("title",
				GDUtil.engforchn(RunActivity.this, "created_type_" + sport_eng));
		params.put("intro", "一起运动更快乐，约么？");
		params.put("status", "close");
		params.put("manual", "no");
		params.put("pace_min", min_pace);
		params.put("pace_max", max_pace);
		params.put("pause", pausepoints.substring(0, pausepoints.length() - 1));
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.ACTIVITIES_CREATE_URL_POST, handler,
						NET_CREATEDACTIVITIES, params);
			}
		});

	}

	/**
	 * 计划跑步完成更新数据
	 */
	private void updategdactivityNet() {

		// POST 参数
		final Map<String, String> params = new HashMap<String, String>();
		params.put("sport", sport_eng);
		params.put("distance", Double.parseDouble(df.format(journey)) + "");
		params.put("duration", GDUtil.getTimeDiff_send(start_time, end_time)
				+ "");
		params.put("latitude", start_latitude + "");
		params.put("longitude", start_longitude + "");
		params.put("address", start_address);
		params.put("start_time", start_time);
		params.put("end_time", end_time);
		if (maxAltitude - minAltitude != 0)
			params.put("altitude", df.format((maxAltitude - minAltitude) / 2)
					+ "");
		params.put("route", routes == null ? null : routes.toString());
		params.put("status", "close");
		params.put("manual", "no");
		params.put("pace_min", min_pace);
		params.put("pace_max", max_pace);
		params.put("pause", pausepoints.substring(0, pausepoints.length() - 1));
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.ACTIVITIES_UPDATE_URL_POST
						+ gdactivity.id + ".json", handler,
						NET_CREATEDACTIVITIES, params);
			}
		});

	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			tv_startnumber.setVisibility(View.GONE);
			if (isfristtime && !ispuased) {
				SoundUtil.player_array(RunActivity.this, sound_run_start,
						sportcategory);
				timer = new Timer(true);
				MyTimerTask task = new MyTimerTask();
				timer.schedule(task, 0, 1000);
				isfristtime = false;
				ib_startandsuspend.setEnabled(true);
			}
		}

		@Override
		public void onTick(long arg0) {
			tv_startnumber.setText("" + startnumber);
			startnumber = startnumber - 1;
		}
	}

	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			Message message = new Message();
			message.what = TimerTask;
			handler.sendMessage(message);
		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_CREATEDACTIVITIES:
				progressbar.setVisibility(View.GONE);
				if (msg.obj == null) {
					if (fristrequest) {
						fristrequest = false;
						if (Constants.SPORTTYPE_CREATED.equals(type)) {
							addgdactivityNet();
						} else if (Constants.SPORTTYPE_UPDATE.equals(type)) {
							updategdactivityNet();
						}
					} else {
						DoubleshowDialog(RunActivity.this);

					}
				} else {
					JSONObject jsonobeObject = (JSONObject) msg.obj;
					try {
						if (jsonobeObject.getInt("status") == 200) {
							if (runtime > 60) {
								mgr.delsport(locatid);
							}
							if (Constants.SPORTTYPE_CREATED.equals(type)) {
								gdactivity = ActivitiesConstruct
										.Toactivity(new JSONObject(
												jsonobeObject.getString("data")));
								if ("yes".equals(gdactivity.getAutomatch())) {
									GDUtil.setGlobal(RunActivity.this,
											"automatch", true);
								}
							}

							Intent intent = new Intent(RunActivity.this,
									GDActivityResultActivity.class);
							intent.putExtra("activityid", gdactivity.get_id());
							intent.putExtra("sportcategory", sportcategory);
							intent.putExtra("type", Constants.SPORTTYPE_CREATED);
							intent.putExtra("event", event);
							intent.putExtra("screenshoot", true);
							startActivity(intent);
							nm.cancel(notification_id);
							finish();

						} else {
							DoubleshowDialog(RunActivity.this);
							ToastUtil.show(RunActivity.this, "创建活动失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;

			case TimerTask:
				++runtime;
				tv_starttime.setText(GDUtil.TransitionTime(runtime));
				tv_starttime_in.setText(GDUtil.TransitionTime(runtime));
				if (runtime % 60 == 0 && journey > 10) {
					toGdactivity();
					locatid = mgr.addsport(gdactivity, locatid);
				}
				notification.contentView.setTextViewText(R.id.tv_journey_in,
						df.format(journey / 1000) + "");
				notification.contentView.setTextViewText(R.id.tv_starttime_in,
						GDUtil.TransitionTime(runtime));
				nm.notify(notification_id, notification);
				break;

			default:
				break;
			}

		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (tv_loding.getVisibility() == View.VISIBLE) {
				updateserVerStatus(Constants.RUN_SERVICE_CO_KILL);
				nm.cancel(notification_id);
				finish();
			}
		}
		return true;
		// return super.onKeyDown(keyCode, event);
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
	}

	@Override
	public void deactivate() {

		mListener = null;

	}

	@Override
	public void onChronometerTick(MyChronometer chronometer) {
		if (chronometer == tv_loding) {
			if (start_locationtime > 1) {
				start_locationtime = start_locationtime - 1;
				tv_loding.setText("正在拼命定位中...(" + start_locationtime + "秒)");
			} else {
				tv_loding.stop();
				LocatoinDialog(RunActivity.this);
			}

		}
	}

	/**
	 * 重复确认
	 * 
	 * @param content
	 */
	private void LocatoinDialog(Context content) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(content);
		dialog.setIcon(android.R.drawable.ic_dialog_info)
				.setCancelable(false)
				.setMessage("当前GPS信号较弱，请确认是否对乐运动授权开启定位，或者选择户外信号较强的区域开始运动")
				.setPositiveButton("继续定位",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								start_locationtime = 11;
								tv_loding.start();
								updateserVerStatus(Constants.RUN_SERVICE_CO_STOP);
								updateserVerStatus(Constants.RUN_SERVICE_CO_PLAY);
							}
						})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						nm.cancel(notification_id);
						finish();
					}
				}).create().show();
	}

	/**
	 * 重复确认
	 * 
	 * @param content
	 */
	private void mindistanceDialog(Context content) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(content);
		dialog.setIcon(android.R.drawable.ic_dialog_info)
				.setCancelable(false)
				.setMessage("当前运动距离过短，结果无法保存，确定结束此次运动？")
				.setPositiveButton("结束运动",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								SoundUtil.player_array(RunActivity.this,
										sound_run_over, sportcategory);
								if (runtime > 60) {
									mgr.delsport(locatid);
								}
								updateserVerStatus(Constants.RUN_SERVICE_CO_KILL);
								nm.cancel(notification_id);
								finish();
							}
						})
				.setNegativeButton("继续运动",
						new DialogInterface.OnClickListener() {
							@SuppressLint("NewApi")
							public void onClick(DialogInterface dialog,
									int which) {
								SoundUtil.player_array(RunActivity.this,
										sound_run_continue, sportcategory);
								dialog.dismiss();
								ib_over.setEnabled(true);
								ib_startandsuspend.setEnabled(true);
								ispuased = false;
								ib_over.setVisibility(View.GONE);
								ll_over.setVisibility(View.GONE);
								view_bg_running.setVisibility(View.GONE);
								timer = new Timer(true);
								MyTimerTask task = new MyTimerTask();
								timer.schedule(task, 0, 1000);
								ib_startandsuspend
										.setImageDrawable(getResources()
												.getDrawable(
														R.drawable.icon_suspend));
							}
						}).create().show();
	}

	class ProgressRunable implements Runnable {
		@Override
		public void run() {
			while (mCurrentProgress < mTotalProgress) {
				if (mCurrentProgress > -1) {
					mCurrentProgress += 1;
					ib_over.setProgress(mCurrentProgress);
					try {
						Thread.sleep(10);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
