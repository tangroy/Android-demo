package com.oxygen.www.module.sport.activity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Event;
import com.oxygen.www.enties.GDAcvitity;
import com.oxygen.www.enties.Photo;
import com.oxygen.www.enties.Route;
import com.oxygen.www.module.sport.adapter.PhotosAdapter;
import com.oxygen.www.module.sport.construct.ActivitiesConstruct;
import com.oxygen.www.module.sport.eventbus_enties.SmallScore;
import com.oxygen.www.module.sport.eventbus_enties.UpdateGDA;
import com.oxygen.www.utils.AMapUtil;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.QQUtils;
import com.oxygen.www.utils.SoundUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.qiniu.android.http.ResponseInfo;

import de.greenrobot.event.EventBus;

/**
 * 个人成绩界面
 * 
 * @author 杨庆雷 2015-6-12下午2:13:02
 */
public class GDActivityResultActivity extends BaseActivity implements
		OnClickListener {
	public final static int SHARE_WX = 0;
	public final static int SHARE_WXF = 1;
	public final static int SHARE_QQ = 3;
	private final int NET_GETTOKEN = 2;
	private final int NET_UPLOAP = 3;
	private final int CHOOSE_PICTURE = 4;
	private final int NET_UPDATEACTIVITIES = 7;
	private final int NET_UPDATERUN = 8;
	private final int BG_FLAG = 9;
	private MapView mapView;
	private ImageView iv_back, iv_share, iv_sport, iv_manual, iv_modify,
			iv_photo, iv_paragraph, iv_amplification;
	private AMap aMap;
	private LinearLayout ll_speed, ll_calorie, ll_altitude, small_record,
			hsv_small_score;
	private ProgressBar progressbar;
	private TextView tv_starttime, tv_duration, tv_distance, tv_pace,
			tv_speed_time, tv_pace_unit, tv_calorie, tv_altitude, tv_speed,
			score_score_right, score_score_left, score_score;
	private final int NET_GETACTIVITIEINFO = 1;
	private int activityid;
	private int sportcategory;
	private int bestDuration;
	private String sport_eng;
	private boolean screenshoot;
	private Event event;
	private GridView gv_photes;
	private ViewPager vp_group_photo;
	DecimalFormat df = new DecimalFormat("#0.00");
	private PopupWindow popupWindow;
	Calendar a = Calendar.getInstance();
	private int shareFlag;
	private String type = "";
	private int eventid = 0;
	private List<String> s_doubles = new ArrayList<String>();
	private List<Integer> ourScores = new ArrayList<Integer>();
	private List<Integer> otherScores = new ArrayList<Integer>();
	private Bundle savedInstanceState;
	private GDAcvitity gdactivity;
	private ArrayList<Photo> photos;
	private String imagePath;
	private String QQImageUrl;
	private String bgPicUrl;
	private LinearLayout sporttype_run;
	private RelativeLayout sporttype_basketball;
	private RelativeLayout sporttype_blank;
	private Button camera;
	private Button picture;
	private Button back;
	private Bitmap photoBp;
	private boolean updatebackGround = true;
	private View speed_line;
	private RelativeLayout rl_speed;
	private TextView speed_low;
	private TextView speed_high;
	private ArrayList<Route> routes;
	private LinearLayout ll_hori_bar;
	private RelativeLayout ll_bg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		sportcategory = getIntent().getIntExtra("sportcategory", -1);
		setContentView(R.layout.activity_sport_runresult);
		this.savedInstanceState = savedInstanceState;
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		ll_hori_bar = (LinearLayout) findViewById(R.id.ll_hori_bar);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		tv_starttime = (TextView) findViewById(R.id.tv_starttime);
		tv_duration = (TextView) findViewById(R.id.tv_duration);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		score_score_left = (TextView) findViewById(R.id.score_score_left);
		score_score_right = (TextView) findViewById(R.id.score_score_right);
		score_score = (TextView) findViewById(R.id.score_score);
		tv_pace = (TextView) findViewById(R.id.tv_pace);
		tv_calorie = (TextView) findViewById(R.id.tv_calorie);
		tv_altitude = (TextView) findViewById(R.id.tv_altitude);
		tv_speed_time = (TextView) findViewById(R.id.tv_speed_time);
		tv_pace_unit = (TextView) findViewById(R.id.tv_pace_unit);
		iv_sport = (ImageView) findViewById(R.id.iv_sport);
		ll_speed = (LinearLayout) findViewById(R.id.ll_speed);
		ll_calorie = (LinearLayout) findViewById(R.id.ll_calorie);
		ll_altitude = (LinearLayout) findViewById(R.id.ll_altitude);
		small_record = (LinearLayout) findViewById(R.id.small_record);
		hsv_small_score = (LinearLayout) findViewById(R.id.hsv_small_score);
		tv_speed = (TextView) findViewById(R.id.tv_speed);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		iv_manual = (ImageView) findViewById(R.id.iv_manual);
		iv_amplification = (ImageView) findViewById(R.id.iv_amplification);
		iv_paragraph = (ImageView) findViewById(R.id.iv_paragraph);
		gv_photes = (GridView) findViewById(R.id.gv_photes);
		vp_group_photo = (ViewPager) findViewById(R.id.vp_group_photo);
		iv_modify = (ImageView) findViewById(R.id.iv_modify);
		iv_photo = (ImageView) findViewById(R.id.iv_take_photo);
		sporttype_run = (LinearLayout) findViewById(R.id.sporttype_run);
		sporttype_basketball = (RelativeLayout) findViewById(R.id.sporttype_basketball);
		sporttype_blank = (RelativeLayout) findViewById(R.id.sporttype_blank);
		speed_line = findViewById(R.id.speed_line);
		rl_speed = (RelativeLayout) findViewById(R.id.rl_speed);
		speed_low = (TextView) findViewById(R.id.speed_low);
		speed_high = (TextView) findViewById(R.id.speed_high);
		ll_bg = (RelativeLayout) findViewById(R.id.ll_bg);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		iv_modify.setOnClickListener(this);
		iv_photo.setOnClickListener(this);
		iv_amplification.setOnClickListener(this);
	}

	private void initValues() {
		activityid = this.getIntent().getIntExtra("activityid", 0);
		bestDuration = this.getIntent().getIntExtra("bestDuration", -1);
		sport_eng = this.getIntent().getStringExtra("sport_eng");
		screenshoot = this.getIntent().getBooleanExtra("screenshoot", false);
		event = (Event) getIntent().getSerializableExtra("event");
		type = this.getIntent().getStringExtra("type");
		progressbar.setVisibility(View.VISIBLE);
		if (sportcategory == -1) {
			sportcategory = GDUtil.SportCategory(sport_eng);
		}
		getActivityInfoInNet(activityid);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			if (Constants.SPORTTYPE_CREATED.equals(type)) {
				reback(eventid);
			} else {
				finish();
			}
			break;
		case R.id.iv_modify:
			Intent modifyIntent = new Intent(this, EditRusultActivity.class);
			modifyIntent.putExtra("sport_eng", sport_eng);
			modifyIntent.putExtra("event", event);
			modifyIntent.putExtra("gdactivity", gdactivity);
			modifyIntent.putStringArrayListExtra("s_doubles",
					(ArrayList<String>) s_doubles);
			modifyIntent.putIntegerArrayListExtra("ourScores",
					(ArrayList<Integer>) ourScores);
			modifyIntent.putIntegerArrayListExtra("otherScores",
					(ArrayList<Integer>) otherScores);
			modifyIntent.putExtra("fromGDActivity", true);
			startActivity(modifyIntent);
			break;
		case R.id.iv_take_photo:
			showPopWindow();
			break;
		case R.id.picture:
			dismissPopupWindow();
			openPicture();
			break;
		case R.id.camera:
			dismissPopupWindow();
			openCamera();
			break;
		case R.id.back:
			dismissPopupWindow();
			break;
		case R.id.iv_share:
			getPopupWindow(v);
			popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.iv_share_weixin:
			// progressbar.setVisibility(View.VISIBLE);
			shareFlag = SHARE_WX;
			shootPicAndLoadUp();
			break;
		case R.id.iv_share_weixin_friends:
			// progressbar.setVisibility(View.VISIBLE);
			shareFlag = SHARE_WXF;
			shootPicAndLoadUp();
			break;
		case R.id.iv_share_qq:
			progressbar.setVisibility(View.VISIBLE);
			shareFlag = SHARE_QQ;
			shootPicAndLoadUp();
			break;
		case R.id.cancel:
			popupWindow.dismiss();
			break;
		case R.id.iv_amplification:
			if (aMap != null && routes != null) {
				AMapUtil.drawline(routes, aMap);
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		getActivityInfoInNet(activityid);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (mapView != null) {
			mapView.onResume();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (mapView != null) {
			mapView.onPause();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mapView != null) {
			mapView.onDestroy();
		}
		EventBus.getDefault().unregister(this);
	}

	private void shootPicAndLoadUp() {
		imagePath = Environment.getExternalStorageDirectory() + "/"
				+ System.currentTimeMillis() + "_"
				+ (int) (Math.random() * 900) + ".jpg";
		ImageUtil.screenShoot(this, new File(imagePath), shareFlag);
		getQiuniuToken();
	}

	/**
	 * 获取服务器events邀请信息列表
	 */
	private void getQiuniuToken() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.GET_QINIU_TOKEN, handler,
						NET_GETTOKEN);
			}
		});
	}

	/**
	 * 创建PopupWindow
	 */
	protected void initPopuptWindow(View v) {
		// TODO Auto-generated method stub
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(
				R.layout.pop_eventdetail, null, false);
		initPopViews(popupWindow_view);

		// 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		// 点击其他地方消失
		popupWindow_view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
	}

	/***
	 * 获取PopupWindow实例
	 */
	private void getPopupWindow(View v) {
		if (null != popupWindow) {
			popupWindow.dismiss();
			return;
		} else {
			initPopuptWindow(v);
		}
	}

	private void initPopViews(View popupWindow_view) {
		TextView tv_lab_share = (TextView) popupWindow_view
				.findViewById(R.id.tv_lab_share);
		LinearLayout ll_share = (LinearLayout) popupWindow_view
				.findViewById(R.id.ll_share);
		TextView iv_share_friend = (TextView) popupWindow_view
				.findViewById(R.id.iv_share_friend);
		TextView iv_share_weixin = (TextView) popupWindow_view
				.findViewById(R.id.iv_share_weixin);
		TextView iv_share_weixin_friends = (TextView) popupWindow_view
				.findViewById(R.id.iv_share_weixin_friends);
		TextView iv_share_qq = (TextView) popupWindow_view
				.findViewById(R.id.iv_share_qq);
		TextView checkin = (TextView) popupWindow_view
				.findViewById(R.id.checkin);
		TextView update_evnent = (TextView) popupWindow_view
				.findViewById(R.id.update_evnent);
		TextView watch_QR_cord = (TextView) popupWindow_view
				.findViewById(R.id.watch_QR_cord);
		TextView exit_evnent = (TextView) popupWindow_view
				.findViewById(R.id.exit_evnent);
		TextView cancel_evnent = (TextView) popupWindow_view
				.findViewById(R.id.cancel_evnent);
		TextView cancel = (TextView) popupWindow_view.findViewById(R.id.cancel);

		tv_lab_share.setVisibility(View.VISIBLE);
		tv_lab_share.setText("分享到");
		ll_share.setVisibility(View.VISIBLE);
		iv_share_friend.setVisibility(View.GONE);

		checkin.setVisibility(View.GONE);
		update_evnent.setVisibility(View.GONE);
		watch_QR_cord.setVisibility(View.GONE);
		exit_evnent.setVisibility(View.GONE);
		cancel_evnent.setVisibility(View.GONE);

		iv_share_weixin.setOnClickListener(this);
		iv_share_weixin_friends.setOnClickListener(this);
		iv_share_qq.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	/**
	 * 获取服务器activity信息
	 */
	private void getActivityInfoInNet(final int userid) {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.ACTIVITIES_ACTIVITYINFO_GET + userid
						+ ".json", handler, NET_GETACTIVITIEINFO);
			}
		});

	}

	/**
	 * handler 更新UI
	 */
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_GETACTIVITIEINFO:
				String strObject = (String) msg.obj;

				if (strObject != null && strObject.length() > 10) {
					try {
						final JSONObject jsoninfos = new JSONObject(strObject);
						if (jsoninfos.getInt("status") == 200) {

							if (sportcategory == Constants.COUNT_CATEGORY_DISTANCE
									|| sportcategory == Constants.COUNT_CATEGORY_RUNNING) {
								mapView = (MapView) findViewById(R.id.map_result);
								mapView.removeAllViews();
								mapView.onCreate(savedInstanceState);
								
								if (aMap == null) {
									aMap = mapView.getMap();
									UiSettings uiSettings = aMap.getUiSettings();
									uiSettings.setZoomControlsEnabled(false);
								}

							}
							OxygenApplication.cachedThreadPool
									.execute(new Runnable() {
										public void run() {
											try {
												GDAcvitity gdactivity = ActivitiesConstruct.Toactivity(jsoninfos
														.getJSONObject("data"));
												Message msg = new Message();
												msg.what = NET_UPDATERUN;
												msg.obj = gdactivity;
												handler.sendMessage(msg);
												
											} catch (JSONException e) {
												e.printStackTrace();
											}

										}
									});

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(GDActivityResultActivity.this,
							getResources().getString(R.string.errcode_wx));
				}
				break;
			case NET_UPDATERUN:
				gdactivity = (GDAcvitity) msg.obj;
				eventid = gdactivity.getEvent_id();
				updateUI(gdactivity);
				break;
			case NET_GETTOKEN:
				String tokenObject = (String) msg.obj;
				if (tokenObject != null && tokenObject.length() > 10) {
					try {
						JSONObject jsonenlist = new JSONObject(tokenObject);
						if (jsonenlist.getInt("status") == 200) {
							String token = jsonenlist.getJSONObject("data")
									.getString("token");
							String domain = jsonenlist.getJSONObject("data")
									.getString("domain");
							String key = "users/" + a.get(Calendar.YEAR) + ""
									+ a.get(Calendar.MONTH) + 1 + "/"
									+ System.currentTimeMillis() + "_"
									+ (int) (Math.random() * 900) + 100
									+ ".jpg";
							if (updatebackGround) {// 更新背景
								HttpUtil.UploadPhotoForQiuniu(token,
										GDUtil.Bitmap2Bytes(photoBp), key,
										handler, NET_UPLOAP);
								bgPicUrl = "http://" + domain + "/" + key;
							} else {// 分享截图
								HttpUtil.UploadPhotoForQiuniu(token, imagePath,
										key, handler, NET_UPLOAP);
								QQImageUrl = "http://" + domain + "/" + key;
							}

						} else {
							ToastUtil.show(GDActivityResultActivity.this,
									jsonenlist.getString("msg"));
							progressbar.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					progressbar.setVisibility(View.GONE);
				}
				break;
			case NET_UPLOAP:
				ResponseInfo responseInfo = (ResponseInfo) msg.obj;
				progressbar.setVisibility(View.GONE);
				if (responseInfo.statusCode == 200) {
					if (updatebackGround) {
						updatebackGround = false;
						final Map<String, String> params = new HashMap<String, String>();
						params.put("pic", bgPicUrl);
						if (event != null && event.getPerformance() != null) {
							activityid = event.getPerformance().get_id();
						}
						OxygenApplication.cachedThreadPool
								.execute(new Runnable() {
									public void run() {
										HttpUtil.Post(
												UrlConstants.ACTIVITIES_UPDATE_URL_POST
														+ activityid + ".json",
												handler, NET_UPDATEACTIVITIES,
												params);
									}
								});
					} else {
						if (shareFlag == SHARE_QQ) {
							QQUtils.doShareToQQ(GDActivityResultActivity.this,
									event.getTilte(), QQImageUrl, " ",
									QQImageUrl);
						}
					}
				}
				break;
			case NET_UPDATEACTIVITIES:
				if (msg.obj != null) {
					JSONObject jsonobeObject = (JSONObject) msg.obj;
					try {
						if (jsonobeObject.getInt("status") == 200) {
							GDUtil.setGlobal(
									GDActivityResultActivity.this,"timeline_is_rerfresh", true);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			default:
				break;
			}
		}

	};

	private void updateUIOnType(final GDAcvitity gdactivity) {

		switch (sportcategory) {
		case 4:// 距离类
			sporttype_run.setVisibility(View.VISIBLE);
			speed_line.setVisibility(View.VISIBLE);
			iv_paragraph.setVisibility(View.GONE);
			iv_amplification.setVisibility(View.GONE);
			ll_bg.setBackgroundColor(getResources().getColor(
					R.color.transparent3));
			updateRunUi(gdactivity);
			break;
		case 8:// 跑步类
			sporttype_run.setVisibility(View.VISIBLE);
			speed_line.setVisibility(View.VISIBLE);
			ll_bg.setBackgroundColor(getResources().getColor(
					R.color.transparent3));
			updateRunUi(gdactivity);
			break;
		case 7:// 局数类
			sporttype_basketball.setVisibility(View.VISIBLE);
			tv_distance.setVisibility(View.GONE);
			ll_speed.setVisibility(View.GONE);
			ll_altitude.setVisibility(View.GONE);
			iv_paragraph.setVisibility(View.GONE);
			iv_amplification.setVisibility(View.GONE);
			score_score_left.setText("胜");
			score_score_right.setText("负");
			score_score.setText(gdactivity.getMatch_win() + "  -  "
					+ gdactivity.getMatch_lose());
			tv_speed_time.setText("时长");
			tv_speed.setText(GDUtil.TransitionTime(gdactivity.getDuration())
					+ "");
			tv_calorie.setText(gdactivity.getCalorie());
			break;
		case 9:// 平板
			sporttype_basketball.setVisibility(View.VISIBLE);
			tv_distance.setVisibility(View.GONE);
			ll_speed.setVisibility(View.GONE);
			ll_altitude.setVisibility(View.GONE);
			iv_paragraph.setVisibility(View.GONE);
			iv_amplification.setVisibility(View.GONE);
			score_score_left.setText("时长");
			score_score.setText(GDUtil.TransitionTime(gdactivity.getDuration())
					+ "");
			score_score.setGravity(Gravity.RIGHT);
			tv_speed_time.setText("最佳成绩");
			tv_speed.setText(bestDuration == -1 ? GDUtil
					.TransitionTime(gdactivity.getDuration()) + "" : GDUtil
					.TransitionTime(bestDuration) + "");
			tv_calorie.setText(gdactivity.getCalorie() + "");
			// iv_manual.setImageDrawable(GDUtil.engSporttodrawable(this,
			// "bg_eventdetail_" + gdactivity.getsport_eng()));
			break;
		case 10:// 小局分类
			sporttype_basketball.setVisibility(View.VISIBLE);
			tv_distance.setVisibility(View.GONE);
			ll_speed.setVisibility(View.GONE);
			ll_altitude.setVisibility(View.GONE);
			iv_paragraph.setVisibility(View.GONE);
			iv_amplification.setVisibility(View.GONE);
			score_score_left.setText("胜");
			score_score_right.setText("负");
			score_score.setText(gdactivity.getMatch_win() + "  -  "
					+ gdactivity.getMatch_lose());
			tv_speed_time.setText("时长");
			tv_speed.setText(GDUtil.TransitionTime(gdactivity.getDuration())
					+ "");
			tv_calorie.setText(gdactivity.getCalorie());
			// iv_manual.setImageDrawable(GDUtil.engSporttodrawable(this,
			// "bg_eventdetail_" + gdactivity.getsport_eng()));
			initSmallScore(gdactivity);
			break;
		case 11:// 篮球类
			sporttype_basketball.setVisibility(View.VISIBLE);
			ll_speed.setVisibility(View.VISIBLE);
			tv_distance.setVisibility(View.GONE);
			ll_altitude.setVisibility(View.GONE);
			iv_paragraph.setVisibility(View.GONE);
			iv_amplification.setVisibility(View.GONE);
			score_score_right.setVisibility(View.GONE);
			score_score_left.setText("比分");
			score_score.setText(gdactivity.getMatch_win() + "  :  "
					+ gdactivity.getMatch_lose());
			tv_speed_time.setText("时长");
			tv_speed.setText(GDUtil.TransitionTime(gdactivity.getDuration())
					+ "");
			tv_calorie.setText(gdactivity.getCalorie());
			tv_pace.setText(gdactivity.getScore() + "");
			tv_pace_unit.setText("我的得分");
			// iv_manual.setImageDrawable(GDUtil.engSporttodrawable(this,
			// "bg_eventdetail_" + gdactivity.getsport_eng()));
			break;
		case 12:// 游泳类
			sporttype_run.setVisibility(View.VISIBLE);
			ll_speed.setVisibility(View.GONE);
			ll_altitude.setVisibility(View.GONE);
			iv_paragraph.setVisibility(View.GONE);
			iv_amplification.setVisibility(View.GONE);
			score_score.setVisibility(View.GONE);
			tv_distance.setText(df.format(gdactivity.getDistance() / 1000));
			tv_duration.setText(GDUtil.TransitionTime(gdactivity.getDuration())
					+ "");
			tv_speed_time.setText("速度：公里/小时");
			tv_speed.setText(gdactivity.getSpeed() + "");
			tv_calorie.setText(gdactivity.getCalorie());
			// iv_manual.setImageDrawable(GDUtil.engSporttodrawable(this,
			// "bg_eventdetail_" + gdactivity.getsport_eng()));
			break;
		case 13:// 足球类
			sporttype_basketball.setVisibility(View.VISIBLE);
			ll_speed.setVisibility(View.VISIBLE);
			tv_distance.setVisibility(View.GONE);
			ll_altitude.setVisibility(View.GONE);
			iv_paragraph.setVisibility(View.GONE);
			iv_amplification.setVisibility(View.GONE);
			score_score_right.setVisibility(View.GONE);
			score_score_left.setText("比分");
			score_score.setText(gdactivity.getMatch_win() + "  :  "
					+ gdactivity.getMatch_lose());
			tv_speed_time.setText("时长");
			tv_speed.setText(GDUtil.TransitionTime(gdactivity.getDuration())
					+ "");
			tv_calorie.setText(gdactivity.getCalorie());
			tv_pace.setText(gdactivity.getScore() + "");
			tv_pace_unit.setText("我的进球");
			// iv_manual.setImageDrawable(GDUtil.engSporttodrawable(this,
			// "bg_eventdetail_" + gdactivity.getsport_eng()));
			break;
		case 14:// 时长类
			// sporttype_run.setVisibility(View.GONE);
			// sporttype_run.setVisibility(View.VISIBLE);
			ll_speed.setVisibility(View.GONE);
			ll_altitude.setVisibility(View.GONE);
			iv_paragraph.setVisibility(View.GONE);
			iv_amplification.setVisibility(View.GONE);
			score_score.setVisibility(View.GONE);
			tv_speed_time.setText("时长");
			tv_speed.setText(GDUtil.TransitionTime(gdactivity.getDuration())
					+ "");
			tv_calorie.setText(gdactivity.getCalorie() + "");
			// iv_manual.setImageDrawable(GDUtil.engSporttodrawable(this,
			// "bg_eventdetail_" + gdactivity.getsport_eng()));
			break;
		default:
			break;
		}
		progressbar.setVisibility(View.GONE);
	}

	// 显示小局分类的View
	private void initSmallScore(GDAcvitity gdactivity) {
		try {
			s_doubles = new ArrayList<String>();
			ourScores = new ArrayList<Integer>();
			otherScores = new ArrayList<Integer>();
			if (gdactivity.getSport_data() != null) {
				small_record.setVisibility(View.VISIBLE);
				JSONArray sportDatas = new JSONArray(gdactivity.getSport_data());
				for (int i = 0; i < sportDatas.length(); i++) {
					JSONObject jsonObject = sportDatas.getJSONObject(i);
					s_doubles.add(jsonObject.getString("type"));
					ourScores.add(new Integer(jsonObject.getInt("our_score")));
					otherScores.add(new Integer(jsonObject
							.getInt("other_score")));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (s_doubles != null && s_doubles.size() > 0) {
			hsv_small_score.removeAllViews();
			for (int i = 0; i < s_doubles.size(); i++) {
				View scores = View.inflate(this, R.layout.item_record_result,
						null);
				scores.setPadding(30, 0, 30, 0);
				TextView tv_our_score = (TextView) scores
						.findViewById(R.id.tv_our_score);
				TextView tv_opposite_score = (TextView) scores
						.findViewById(R.id.tv_opposite_score);
				TextView single_double = (TextView) scores
						.findViewById(R.id.single_double);
				tv_our_score.setText(ourScores.get(i) + "");
				tv_opposite_score.setText(otherScores.get(i) + "");
				single_double.setText(s_doubles.get(i).replace("doubles", "双")
						.replace("singles", "单"));
				hsv_small_score.addView(scores);
				small_record.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 更新UI
	 */
	private void updateUI(GDAcvitity gdactivity) {
		// 手动记录
		String manual = gdactivity.getManual();
		if ("yes".equals(manual)) {
			iv_modify.setVisibility(View.VISIBLE);
		}
		int userId = gdactivity.getCreated_by();
		if (!((String) UserInfoUtils.getUserInfo(getApplicationContext(),
				Constants.USERID, "")).equals(userId + "")) {
			iv_modify.setVisibility(View.INVISIBLE);
			iv_photo.setVisibility(View.INVISIBLE);
		}
		iv_sport.setImageDrawable(GDUtil.engSporttodrawable(this, "icon_start_"
				+ gdactivity.getsport_eng()));
		tv_starttime.setText(gdactivity.getStart_time());
		if (!TextUtils.isEmpty(gdactivity.getPic())) {
			ImageUtil.showImage(
					gdactivity.getPic(),
					iv_manual,
					GDUtil.drawableId("bg_eventdetail_"
							+ gdactivity.getsport_eng()));
		} else {
			iv_manual.setImageDrawable(GDUtil.engSporttodrawable(this,
					"bg_eventdetail_" + gdactivity.getsport_eng()));
		}
		updateUIOnType(gdactivity);
		progressbar.setVisibility(View.GONE);
	}

	private void updateRunUi(GDAcvitity gdactivity) {
		int duration = gdactivity.getDuration();
		tv_duration.setText(GDUtil.TransitionTime(duration));
		tv_distance.setText(df.format(gdactivity.getDistance() / 1000) + "");
		tv_calorie.setText(gdactivity.getCalorie());
		if (TextUtils.isEmpty(gdactivity.getAltitude())) {
			tv_altitude.setText("0.0");
		} else {
			tv_altitude.setText(gdactivity.getAltitude() + "");
		}
		tv_speed.setText(gdactivity.getSpeed() + "");
		tv_pace.setText(gdactivity.getPace() + "");
		if (GDUtil.SportCategory(gdactivity.getsport_eng()) == Constants.COUNT_CATEGORY_RUNNING) {
			ll_speed.setVisibility(View.VISIBLE);
		}
		routes = gdactivity.getRoute();
		if (routes != null && routes.size() > 0) {
			speed_low.setText("最慢" + gdactivity.getPace_min());
			speed_high.setText("最快" + gdactivity.getPace_max());
			mapView.setVisibility(View.VISIBLE);
			iv_photo.setVisibility(View.GONE);
			AMapUtil.drawline(routes, aMap);
			if (Constants.SPORTTYPE_CREATED.equals(getIntent().getStringExtra(
					"type"))) {
				// 播报语音
				SoundUtil.player_array(GDActivityResultActivity.this,
						SpeakSoundRun(gdactivity), sportcategory);
			}
		} else {
			photos = gdactivity.getPhotos();
			if (photos != null && photos.size() > 0) {
				PhotosAdapter ptotesAdapter = new PhotosAdapter(
						GDActivityResultActivity.this, photos, vp_group_photo);
				gv_photes.setAdapter(ptotesAdapter);
			}
		}

	}

	/**
	 * 播放跑步结果语音
	 * 
	 * @return
	 */
	private ArrayList<String> SpeakSoundRun(GDAcvitity gdactivity) {
		ArrayList<String> sound_running = new ArrayList<String>();
		sound_running.add("this");
		sound_running.add("run");
		sound_running.addAll(SoundUtil.player_number(Double.parseDouble(df
				.format(gdactivity.getDistance() / 1000))));
		sound_running.add("km");
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("costtime");
		sound_running.addAll(SoundUtil.player_time(gdactivity.getDuration()));
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("speed");
		sound_running.addAll(SoundUtil.player_number(gdactivity.getSpeed()));
		sound_running.add("km");
		sound_running.add("per_hour");
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("pace");
		sound_running.add("per_km");
		String pace = gdactivity.getPace();
		if (pace != null && pace.length() > 4) {
			String paca_array[] = pace.split("'");

			int pace_min = Integer.parseInt(paca_array[0]);
			int pace_sencond = Integer.parseInt(paca_array[1].substring(0,
					paca_array[1].length() - 1));
			if (pace_min > 9) {
				sound_running.addAll(SoundUtil.player_numberint(pace_min));

			} else {
				sound_running.add(pace_min + "");
			}
			sound_running.add("min");
			if (pace_sencond > 9) {
				sound_running.addAll(SoundUtil.player_numberint(pace_sencond));
			} else {
				sound_running.add(pace_sencond + "");
			}
			sound_running.add("second");
		}
		
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("music_null");
		sound_running.add("consume");

		int kcal = Integer.parseInt(gdactivity.getCalorie());
		if (kcal > 9) {
			sound_running.addAll(SoundUtil.player_numberint(kcal));
		} else {
			sound_running.add(kcal + "");
		}
		sound_running.add("kcal");
		return sound_running;
	}

	private void reback(int eventid) {
		if (Constants.SPORTTYPE_CREATED.equals(getIntent().getStringExtra(
				"type"))) {
			Intent intent = new Intent(GDActivityResultActivity.this,
					EventsResultActivity.class);
			intent.putExtra("eventid", eventid);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			GDUtil.setGlobal(GDActivityResultActivity.this,"timeline_is_rerfresh", true);
			startActivity(intent);

		}
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (vp_group_photo.isShown()) {
			vp_group_photo.setVisibility(View.GONE);
			return false;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (Constants.SPORTTYPE_CREATED.equals(type)) {
				reback(eventid);
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 打开popWindow(相机和相册)
	 */
	private void showPopWindow() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		if (popupWindow == null || !popupWindow.isShowing()) {
			View contentView = View.inflate(this,
					R.layout.activity_psw_popupwindow, null);
			camera = (Button) contentView.findViewById(R.id.camera);
			picture = (Button) contentView.findViewById(R.id.picture);
			back = (Button) contentView.findViewById(R.id.back);
			camera.setOnClickListener(this);
			picture.setOnClickListener(this);
			back.setOnClickListener(this);
			popupWindow = new PopupWindow(contentView);
			popupWindow.setWidth(width);
			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);// 设置弹出框大小
			popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
			popupWindow.setOutsideTouchable(false);
		}
	}

	/**
	 * 关闭当前界面的弹出窗体
	 */
	private void dismissPopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	/**
	 * 打开相机的图库
	 */
	private void openPicture() {
		dismissPopupWindow();
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, CHOOSE_PICTURE);
	}

	/**
	 * 打开相机
	 */
	private void openCamera() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, 3);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case CHOOSE_PICTURE:
			if (data != null) {
				try {
					photoBp = MediaStore.Images.Media.getBitmap(
							this.getContentResolver(), data.getData());
					Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
							getContentResolver(), photoBp, null, null));
					startPhotoZoom(uri);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case 3:
			if (data != null) {
				setPicToView(data);
			}
			break;
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			photoBp = extras.getParcelable("data");
			if (photoBp != null) {
				// iv_manual.setImageBitmap(photoBp);
				// iv_manual.setVisibility(View.VISIBLE);
				// if(mapView != null){
				// mapView.setVisibility(View.GONE);
				// }
				getQiuniuToken();
				updatebackGround = true;
			}
		}
	}

	private void updateBg() {
		imagePath = Environment.getExternalStorageDirectory() + "/"
				+ System.currentTimeMillis() + "_"
				+ (int) (Math.random() * 900) + ".jpg";
		ImageUtil.screenShoot(this, new File(imagePath), BG_FLAG);
		photoBp = ImageUtil.getSmallBitmap(imagePath, 480, 800);
		// 截图之后删除本地图片
		ImageUtil.deleteTempFile(imagePath);
		updatebackGround = true;
		getQiuniuToken();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (photoBp != null) {
			photoBp.recycle();
		}
		// 如果是跑步，且个人成绩页之前没有背景 则需要截取当前的界面作为时间轴的界面
		if ((event != null
				&& TextUtils.isEmpty(event.getPerformance().getPic())
				&& routes != null && routes.size() > 10)
				|| ("created".equals(type)&&"no".equals(gdactivity.getManual()))) {
			updateBg();
		}

	}

	private void onEventMainThread(SmallScore ss) {
		s_doubles = ss.getS_doubles();
		ourScores = ss.getOurScores();
		otherScores = ss.getOtherScores();
	}

	private void onEventMainThread(UpdateGDA info) {
		getActivityInfoInNet(info.getActivityId());
	}
}
