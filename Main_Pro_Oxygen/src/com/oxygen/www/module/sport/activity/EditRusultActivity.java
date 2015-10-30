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
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Event;
import com.oxygen.www.enties.GDAcvitity;
import com.oxygen.www.module.sport.construct.ActivitiesConstruct;
import com.oxygen.www.module.sport.eventbus_enties.SmallScore;
import com.oxygen.www.module.sport.eventbus_enties.UpdateGDA;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.qiniu.android.http.ResponseInfo;

import de.greenrobot.event.EventBus;

/**
 * 运动记录界面
 * 
 * @author kunyuan
 * 
 */
public class EditRusultActivity extends BaseActivity implements
		OnClickListener, OnValueChangeListener {
	private ImageView iv_back, iv_edit_sport;
	private TextView tv_submit, tv_edit_time;
	private Event event;
	private LinearLayout ll_important_other, ll_important_distance,
			ll_important_match, rl_record;
	private RelativeLayout rl_edit_duration, rl_edit_time, rl_edit_title,
			rl_edit_address, rl_edit_score;
	private TextView tv_other_duration, tv_distance_distance,
			tv_distance_duration, tv_match_lab_win, tv_match_win,
			tv_match_lab_gap, tv_match_lose, tv_match_lab_lose,
			tv_edit_duration, et_edit_score;
	private EditText et_edit_title;
	private TextView et_edit_address;
	// 选择参数组件
	private LinearLayout ll_choose_date, ll_choose_time, tv_choose_duration,
			tv_choose_distance, tv_choose_macth, ll_distance_distance,
			ll_distance_duration, tv_choose_score;
	private NumberPicker np_duration_hour, np_duration_minute,
			np_duration_second, np_distance_km, np_distance_m, np_macth_win,
			np_macth_lose, np_score;
	private DatePicker date_picker;
	private TimePicker time_picker;
	private TextView tv_lab_win, tv_lab_lose, chooseover, tv_edit_score_lab;
	private ProgressBar progressbar;
	private final int NET_UPDATEACTIVITIES = 1;
	private int category;
	DecimalFormat df = new DecimalFormat("#0.0");
	Typeface typeface;
	int hour = 0, minute = 0, second = 0, km = 0, m = 0, win = 0, lose = 0;
	int duration = 0;
	Double distance = 0.0;
	int score = 0;
	private String activities_date = "1970-01-01";
	private String activities_time = "00:00";
	String type;
	private boolean fromGDActivity;
	String sport_eng = null;
	private LinearLayout hsv_small_score;
	/**
	 * 运动截图布局
	 */
	private LinearLayout ll_screenshots;
	/**
	 * 运动截图
	 */
	private GridView sport_screenshots;
	private LinearLayout small_record;
	private View parentView;
	private PopupWindow pop;
	private LinearLayout ll_popup;
	private final int NET_GETTOKEN = 0x000002;
	private final int NET_UPLOAP = 0x000003;
	private final int CHOOSE_PICTURE = 0x000004;
	private final int UPDATE_GRIDVIEW = 0x000005;
	private final int POST_PHOTE_URL = 0x000006;
	Calendar a = Calendar.getInstance();

	// 小局分类的单双 和 双方比分
	private List<String> s_doubles;
	private List<Integer> ourScores;
	private List<Integer> otherScores;

	private Map<String, String> params;
	private GridAdapter adapter;
	/**
	 * 图片的本地地址
	 */
	private String photopsth;
	/**
	 * 图片在七牛的地址
	 */
	private String picurl;
	private List<String> picurls;
	// 上传图片你地址的参数
	private Map<String, String> photesParams;
	int eventid = 0;
	private static final int requestCode_address = 10;
	GDAcvitity performance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		parentView = getLayoutInflater().inflate(R.layout.activity_editresult,
				null);
		setContentView(parentView);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_edit_sport = (ImageView) findViewById(R.id.iv_edit_sport);
		et_edit_title = (EditText) findViewById(R.id.et_edit_title);
		tv_edit_time = (TextView) findViewById(R.id.tv_edit_time);
		tv_edit_duration = (TextView) findViewById(R.id.tv_edit_duration);
		et_edit_address = (TextView) findViewById(R.id.et_edit_address);
		rl_record = (LinearLayout) findViewById(R.id.rl_record);
		et_edit_score = (TextView) findViewById(R.id.et_edit_score);
		tv_other_duration = (TextView) findViewById(R.id.tv_other_duration);
		tv_distance_distance = (TextView) findViewById(R.id.tv_distance_distance);
		tv_distance_duration = (TextView) findViewById(R.id.tv_distance_duration);
		tv_match_lab_win = (TextView) findViewById(R.id.tv_match_lab_win);
		tv_match_win = (TextView) findViewById(R.id.tv_match_win);
		tv_match_lab_gap = (TextView) findViewById(R.id.tv_match_lab_gap);
		tv_match_lose = (TextView) findViewById(R.id.tv_match_lose);
		tv_match_lab_lose = (TextView) findViewById(R.id.tv_match_lab_lose);
		ll_important_distance = (LinearLayout) findViewById(R.id.ll_important_distance);
		ll_important_other = (LinearLayout) findViewById(R.id.ll_important_other);
		ll_important_match = (LinearLayout) findViewById(R.id.ll_important_match);
		tv_submit = (TextView) findViewById(R.id.tv_submit);
		rl_edit_duration = (RelativeLayout) findViewById(R.id.rl_edit_duration);
		rl_edit_score = (RelativeLayout) findViewById(R.id.rl_edit_score);
		ll_choose_date = (LinearLayout) findViewById(R.id.ll_choose_date);
		ll_choose_time = (LinearLayout) findViewById(R.id.ll_choose_time);
		tv_choose_duration = (LinearLayout) findViewById(R.id.tv_choose_duration);
		tv_choose_distance = (LinearLayout) findViewById(R.id.tv_choose_distance);
		tv_choose_score = (LinearLayout) findViewById(R.id.tv_choose_score);
		tv_choose_macth = (LinearLayout) findViewById(R.id.tv_choose_macth);
		np_duration_hour = (NumberPicker) findViewById(R.id.np_duration_hour);
		np_duration_minute = (NumberPicker) findViewById(R.id.np_duration_minute);
		np_duration_second = (NumberPicker) findViewById(R.id.np_duration_second);
		np_distance_km = (NumberPicker) findViewById(R.id.np_distance_km);
		np_distance_m = (NumberPicker) findViewById(R.id.np_distance_m);
		np_macth_win = (NumberPicker) findViewById(R.id.np_macth_win);
		np_macth_lose = (NumberPicker) findViewById(R.id.np_macth_lose);
		np_score = (NumberPicker) findViewById(R.id.np_score);
		date_picker = (DatePicker) findViewById(R.id.date_picker);
		time_picker = (TimePicker) findViewById(R.id.time_picker);
		tv_lab_win = (TextView) findViewById(R.id.tv_lab_win);
		tv_lab_lose = (TextView) findViewById(R.id.tv_lab_lose);
		tv_edit_score_lab = (TextView) findViewById(R.id.tv_edit_score_lab);
		ll_distance_distance = (LinearLayout) findViewById(R.id.ll_distance_distance);
		ll_distance_duration = (LinearLayout) findViewById(R.id.ll_distance_duration);
		rl_edit_time = (RelativeLayout) findViewById(R.id.rl_edit_time);
		rl_edit_title = (RelativeLayout) findViewById(R.id.rl_edit_title);
		rl_edit_address = (RelativeLayout) findViewById(R.id.rl_edit_address);
		chooseover = (TextView) findViewById(R.id.chooseover);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		hsv_small_score = (LinearLayout) findViewById(R.id.hsv_small_score);
		ll_screenshots = (LinearLayout) findViewById(R.id.ll_screenshots);
		sport_screenshots = (GridView) findViewById(R.id.sport_screenshots);
		small_record = (LinearLayout) findViewById(R.id.small_record);

		typeface = Typeface.createFromAsset(this.getAssets(),
				"fonts/Impact.ttf");
		tv_distance_distance.setTypeface(typeface);
		tv_distance_duration.setTypeface(typeface);
		tv_match_lose.setTypeface(typeface);
		tv_match_win.setTypeface(typeface);
		tv_other_duration.setTypeface(typeface);

	}

	private void initViewsEvent() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(this);
		tv_submit.setOnClickListener(this);
		ll_important_other.setOnClickListener(this);
		ll_distance_distance.setOnClickListener(this);
		ll_distance_duration.setOnClickListener(this);
		ll_important_match.setOnClickListener(this);
		tv_edit_time.setOnClickListener(this);
		chooseover.setOnClickListener(this);
		rl_edit_score.setOnClickListener(this);
		rl_edit_address.setOnClickListener(this);
		rl_edit_duration.setOnClickListener(this);
		np_duration_hour.setOnValueChangedListener(this);
		np_duration_minute.setOnValueChangedListener(this);
		np_duration_second.setOnValueChangedListener(this);
		np_distance_km.setOnValueChangedListener(this);
		np_distance_m.setOnValueChangedListener(this);
		np_macth_win.setOnValueChangedListener(this);
		np_macth_lose.setOnValueChangedListener(this);
		np_score.setOnValueChangedListener(this);
		tv_choose_score.setOnClickListener(this);
		rl_record.setOnClickListener(this);

		adapter = new GridAdapter();
		sport_screenshots.setAdapter(adapter);
		sport_screenshots.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// 打开图库
				if (arg2 == picurls.size()) {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					// 相片类型
					startActivityForResult(intent, CHOOSE_PICTURE);
				}
			}

		});

		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		activities_date = cal.get(Calendar.YEAR) + "-"
				+ (cal.get(Calendar.MONTH) + 1) + "-"
				+ cal.get(Calendar.DAY_OF_MONTH);
		activities_time = cal.get(Calendar.HOUR_OF_DAY) + ":"
				+ cal.get(Calendar.MINUTE);
		tv_edit_time.setText(activities_date + " " + activities_time);
		date_picker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker arg0, int year,
							int monthOfYear, int dayOfMonth) {
						activities_date = year + "-" + (monthOfYear + 1) + "-"
								+ dayOfMonth;
						tv_edit_time.setText(activities_date + " "
								+ activities_time);

					}
				});
		time_picker.setIs24HourView(true);
		time_picker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
		time_picker.setCurrentMinute(cal.get(Calendar.MINUTE));
		time_picker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker arg0, int hourOfDay, int minute) {
				activities_time = (hourOfDay > 9 ? hourOfDay
						: ("0" + hourOfDay))
						+ ":"
						+ (minute > 9 ? minute : ("0" + minute));
				tv_edit_time.setText(activities_date + " " + activities_time);

			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			switch (requestCode) {
			case CHOOSE_PICTURE:
				if (data != null) {
					try {
						Uri uri = Uri.parse(MediaStore.Images.Media
								.insertImage(
										getContentResolver(),
										MediaStore.Images.Media.getBitmap(
												this.getContentResolver(),
												data.getData()), null, null));
						startPhotoZoom(uri);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;

			case requestCode_address:
				if (data.getStringExtra("address") != null) {
					et_edit_address.setText(data.getStringExtra("address"));
				}

				break;
			case 3:
				if (data != null) {
					getQiuniuToken();
				}
				break;
			}

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
		intent.putExtra("aspectX", 640);
		intent.putExtra("aspectY", 640);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		photopsth = Environment.getExternalStorageDirectory() + "/"
				+ System.currentTimeMillis() + "_"
				+ (int) (Math.random() * 900) + ".jpg";
		intent.putExtra("output", Uri.fromFile(new File(photopsth)));
		intent.putExtra("return-data", false);
		startActivityForResult(intent, 3);
	}

	private void initNumerParkerValues() {
		np_duration_hour.setMaxValue(99);
		np_duration_hour.setMinValue(0);
		np_duration_minute.setMaxValue(59);
		np_duration_minute.setMinValue(0);
		np_duration_second.setMaxValue(59);
		np_duration_second.setMinValue(0);
		np_macth_win.setMaxValue(999);
		np_macth_win.setMinValue(0);
		np_macth_lose.setMaxValue(999);
		np_macth_lose.setMinValue(0);
		np_distance_km.setMaxValue(999);
		np_distance_km.setMinValue(0);
		np_distance_m.setMaxValue(9);
		np_distance_m.setMinValue(0);
		np_score.setMinValue(0);
		np_score.setMaxValue(99);
	}

	private void initValues() {
		picurls = new ArrayList<String>();
		initNumerParkerValues();
		Intent intent = this.getIntent();
		type = intent.getStringExtra("type");
		fromGDActivity = intent.getBooleanExtra("fromGDActivity", false);
		if (Constants.SPORTTYPE_CREATED.equals(type)) {
			sport_eng = intent.getStringExtra("sport_eng");
			et_edit_title.setEnabled(true);
			tv_edit_time.setEnabled(true);
		} else {
			event = (Event) getIntent().getSerializableExtra("event");
			s_doubles = intent.getExtras().getStringArrayList("s_doubles");
			ourScores = intent.getExtras().getIntegerArrayList("ourScores");
			otherScores = intent.getExtras().getIntegerArrayList("otherScores");
			if (s_doubles == null) {
				s_doubles = new ArrayList<String>();
				ourScores = new ArrayList<Integer>();
				otherScores = new ArrayList<Integer>();
			}
			if (event == null) {
				performance = (GDAcvitity) getIntent().getSerializableExtra(
						"gdactivity");
				sport_eng = performance.getsport_eng();
			} else {
				sport_eng = event.getSport_eng();
				performance = event.getPerformance();
			}
			duration = performance.getDuration();
			tv_distance_distance
					.setText(df.format(performance.getDistance() / 1000) + "");
			tv_distance_duration.setText(GDUtil.TransitionTime(duration));
			tv_edit_duration.setText(GDUtil.TransitionTime(duration));
			tv_match_lose.setText(performance.getMatch_lose() + "");
			tv_match_win.setText(performance.getMatch_win() + "");
			win = performance.getMatch_win();
			lose = performance.getMatch_lose();
			score = performance.getScore();
			distance = performance.getDistance();
			et_edit_score.setText(score + "");
			tv_other_duration.setText(GDUtil.TransitionTime(duration));
			rl_edit_title.setVisibility(View.GONE);
			rl_edit_duration.setVisibility(View.GONE);
			rl_edit_time.setVisibility(View.GONE);
			rl_edit_address.setVisibility(View.GONE);
			rl_edit_score.setVisibility(View.GONE);
		}
		category = GDUtil.SportCategory(sport_eng);
		if (category == Constants.COUNT_CATEGORY_BAR) {
			rl_record.setVisibility(View.VISIBLE);
			initSmallScore();
		}
		iv_edit_sport.setImageDrawable(GDUtil.engSporttodrawable(this,
				"icon_index_" + sport_eng));

		if (category == Constants.COUNT_CATEGORY_DISTANCE
				|| category == Constants.COUNT_CATEGORY_RUNNING
				|| category == Constants.COUNT_CATEGORY_SWIMMING) {
			ll_important_distance.setVisibility(View.VISIBLE);
			if (category != Constants.COUNT_CATEGORY_SWIMMING) {
				ll_screenshots.setVisibility(View.VISIBLE);
			}

		} else if (category == Constants.COUNT_CATEGORY_JU
				|| category == Constants.COUNT_CATEGORY_BAR
				|| category == Constants.COUNT_CATEGORY_BASKETBALL
				|| category == Constants.COUNT_CATEGORY_FOOTBALL) {
			ll_important_match.setVisibility(View.VISIBLE);
			rl_edit_duration.setVisibility(View.VISIBLE);

			if (category == Constants.COUNT_CATEGORY_BASKETBALL
					|| category == Constants.COUNT_CATEGORY_FOOTBALL) {
				if (category == Constants.COUNT_CATEGORY_BASKETBALL) {
					tv_edit_score_lab.setText("我的得分         ");
				}
				rl_edit_score.setVisibility(View.VISIBLE);
				tv_match_lab_gap.setText("：");
				tv_match_lab_lose.setText("对方");
				tv_match_lab_win.setText("我方");
				tv_lab_win.setText("我方");
				tv_lab_lose.setText("对方");
			} else {
				tv_match_lab_gap.setText("-");
				tv_match_lab_lose.setText("负");
				tv_match_lab_win.setText("胜");
				tv_lab_win.setText("胜");
				tv_lab_lose.setText("负");
			}
		} else {
			ll_important_other.setVisibility(View.VISIBLE);
		}

	}

	// 显示小局分类的View
	private void initSmallScore() {
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_submit:
			tv_submit.setEnabled(false);
			params = new HashMap<String, String>();
			if (Constants.SPORTTYPE_CREATED.equals(type)) {
				addgdactivityNet();
			} else {
				updateActivityfornet();
			}
			break;

		case R.id.rl_edit_address:
			Intent intent_adress = new Intent(this,
					CreatePLanSettingAddressActivity.class);
			startActivityForResult(intent_adress, requestCode_address);

			break;

		case R.id.rl_edit_score:
			ll_choose_date.setVisibility(View.VISIBLE);
			tv_choose_score.setVisibility(View.VISIBLE);
			tv_choose_duration.setVisibility(View.GONE);
			tv_choose_macth.setVisibility(View.GONE);
			ll_choose_time.setVisibility(View.GONE);
			break;
		case R.id.ll_important_other:
			ll_choose_date.setVisibility(View.VISIBLE);
			tv_choose_duration.setVisibility(View.VISIBLE);
			ll_choose_time.setVisibility(View.GONE);
			break;
		case R.id.ll_distance_distance:
			ll_choose_date.setVisibility(View.VISIBLE);
			tv_choose_distance.setVisibility(View.VISIBLE);
			tv_choose_duration.setVisibility(View.GONE);
			ll_choose_time.setVisibility(View.GONE);
			break;
		case R.id.ll_distance_duration:
			ll_choose_date.setVisibility(View.VISIBLE);
			tv_choose_distance.setVisibility(View.GONE);
			tv_choose_duration.setVisibility(View.VISIBLE);
			ll_choose_time.setVisibility(View.GONE);
			tv_choose_score.setVisibility(View.GONE);

			break;
		case R.id.ll_important_match:
			tv_choose_duration.setVisibility(View.GONE);
			ll_choose_date.setVisibility(View.VISIBLE);
			tv_choose_macth.setVisibility(View.VISIBLE);
			ll_choose_time.setVisibility(View.GONE);
			tv_choose_score.setVisibility(View.GONE);

			break;
		case R.id.chooseover:
			ll_choose_date.setVisibility(View.GONE);
			tv_choose_duration.setVisibility(View.GONE);
			ll_choose_date.setVisibility(View.GONE);
			tv_choose_macth.setVisibility(View.GONE);
			ll_choose_time.setVisibility(View.GONE);
			tv_choose_score.setVisibility(View.GONE);
			break;
		case R.id.rl_edit_duration:
			tv_choose_macth.setVisibility(View.GONE);
			ll_choose_date.setVisibility(View.VISIBLE);
			tv_choose_duration.setVisibility(View.VISIBLE);
			ll_choose_time.setVisibility(View.GONE);

			break;
		case R.id.tv_edit_time:
			ll_choose_date.setVisibility(View.VISIBLE);
			tv_choose_macth.setVisibility(View.GONE);
			tv_choose_distance.setVisibility(View.GONE);
			tv_choose_duration.setVisibility(View.GONE);
			ll_choose_time.setVisibility(View.VISIBLE);
			break;
		// 分局纪录按钮
		case R.id.rl_record:
			Intent recordIntent = new Intent(this, RecordActivity.class);
			recordIntent.putStringArrayListExtra("s_doubles",
					(ArrayList<String>) s_doubles);
			recordIntent.putIntegerArrayListExtra("ourScores",
					(ArrayList<Integer>) ourScores);
			recordIntent.putIntegerArrayListExtra("otherScores",
					(ArrayList<Integer>) otherScores);
			startActivity(recordIntent);
			break;
		default:
			break;
		}

	}

	@Override
	public void onValueChange(NumberPicker arg0, int oldVal, int newVal) {
		switch (arg0.getId()) {
		case R.id.np_duration_hour:
			hour = newVal;
			RefreshDuration();
			break;
		case R.id.np_duration_minute:
			minute = newVal;
			RefreshDuration();
			break;
		case R.id.np_duration_second:
			second = newVal;
			RefreshDuration();
			break;
		case R.id.np_distance_km:
			km = newVal;
			distance = km * 1000 + m * 100 + 0.0;
			tv_distance_distance.setText(km + "." + m);
			break;
		case R.id.np_distance_m:
			m = newVal;
			distance = km * 1000 + m * 100 + 0.0;
			tv_distance_distance.setText(km + "." + m);
			break;
		case R.id.np_macth_lose:
			lose = newVal;
			tv_match_lose.setText(newVal + "");
			break;
		case R.id.np_macth_win:
			win = newVal;
			tv_match_win.setText(newVal + "");
			break;
		case R.id.np_score:
			score = newVal;
			et_edit_score.setText(score + "");
			break;
		default:
			break;
		}
	}

	private void RefreshDuration() {
		duration = hour * 3600 + minute * 60 + second;
		tv_distance_duration.setText(GDUtil.TransitionTime(duration));
		tv_edit_duration.setText(GDUtil.TransitionTime(duration));
		tv_other_duration.setText(GDUtil.TransitionTime(duration));

	}

	private void updateActivityfornet() {
		progressbar.setVisibility(View.VISIBLE);
		// POST 参数
		params.put("duration", duration + "");
		params.put("distance", distance + "");
		params.put("match_win", win + "");
		params.put("match_lose", lose + "");
		params.put("status", "close");
		params.put("score", score + "");
		params.put("manual", "yes");
		if (s_doubles != null && s_doubles.size() > 0) {
			params.put("sport_data", toJsonString().replace("双", "doubles")
					.replace("单", "singles"));
		}
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				if (event == null) {
					HttpUtil.Post(UrlConstants.ACTIVITIES_UPDATE_URL_POST
							+ performance.get_id() + ".json", handler,
							NET_UPDATEACTIVITIES, params);
				} else {
					HttpUtil.Post(UrlConstants.ACTIVITIES_UPDATE_URL_POST
							+ event.getPerformance().get_id() + ".json",
							handler, NET_UPDATEACTIVITIES, params);
				}
			}
		});

	}

	private void addgdactivityNet() {
		progressbar.setVisibility(View.VISIBLE);
		params.put("duration", duration + "");
		params.put("distance", distance + "");
		params.put("match_win", win + "");
		params.put("match_lose", lose + "");
		params.put("status", "close");
		params.put("sport", sport_eng);
		params.put("score", score + "");
		params.put("manual", "yes");
		if (s_doubles != null && s_doubles.size() > 0) {
			params.put("sport_data", toJsonString());
		}
		if (TextUtils.isEmpty(et_edit_title.getText())) {
			params.put(
					"title",
					GDUtil.engforchn(EditRusultActivity.this, "created_type_"
							+ sport_eng));
		} else {
			params.put("title", et_edit_title.getText().toString());
		}
		params.put("start_time", activities_date + " " + activities_time);
		params.put("address", et_edit_address.getText().toString());
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.ACTIVITIES_CREATE_URL_POST, handler,
						NET_UPDATEACTIVITIES, params);
			}
		});

	}

	// 提交数据
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_UPDATEACTIVITIES:
				progressbar.setVisibility(View.GONE);
				if (msg.obj == null) {
					tv_submit.setEnabled(true);
					ToastUtil.show(EditRusultActivity.this, "网络连接不可用，请稍后重试");
				} else {
					JSONObject jsonobeObject = (JSONObject) msg.obj;
					try {
						if (jsonobeObject.getInt("status") == 200) {
							int activityid = 0;
							if (Constants.SPORTTYPE_CREATED.equals(type)) {
								GDAcvitity gdactivity = ActivitiesConstruct
										.Toactivity(new JSONObject(
										jsonobeObject.getString("data"))); 
								eventid = gdactivity.getEvent_id();
								activityid = gdactivity.get_id();
								sport_eng = gdactivity.getsport_eng();
								if("yes".equals(gdactivity.getAutomatch())){
									GDUtil.setGlobal(EditRusultActivity.this,"automatch",true);
								}
							} else {
								if (event == null) {
									activityid = performance.get_id();
									eventid = performance.getEvent_id();
								} else {
									eventid = event.get_id();
									activityid = event.getPerformance()
											.get_id();
								}
							}

							submitUserInfoToNet(activityid);

							GDUtil.setGlobal(
									EditRusultActivity.this,"timeline_is_rerfresh", true);
							// Intent intent;
							if (fromGDActivity) {
								EventBus.getDefault().post(
										new UpdateGDA(activityid));
							} else {
								Intent intent = new Intent(
										EditRusultActivity.this,
										GDActivityResultActivity.class);
								intent.putExtra("activityid", activityid);
								intent.putExtra("sport_eng", sport_eng);
								intent.putExtra("type",
										Constants.SPORTTYPE_CREATED);
								startActivity(intent);
							}
							//
							finish();
						} else {
							tv_submit.setEnabled(true);
							ToastUtil.show(EditRusultActivity.this,
									jsonobeObject.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			case NET_GETTOKEN:
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsonenlist = new JSONObject(strObject);
						if (jsonenlist.getInt("status") == 200) {
							String token = jsonenlist.getJSONObject("data")
									.getString("token");
							String domain = jsonenlist.getJSONObject("data")
									.getString("domain");
							String key = "events/" + a.get(Calendar.YEAR) + ""
									+ a.get(Calendar.MONTH) + 1 + "/"
									+ System.currentTimeMillis() + "_"
									+ (int) (Math.random() * 900) + 100
									+ ".jpg";
							// 上传图片到七牛
							picurl = "http://" + domain + "/" + key;
							HttpUtil.UploadPhotoForQiuniu(token, photopsth,
									key, handler, NET_UPLOAP);
						} else {
							ToastUtil.show(EditRusultActivity.this,
									jsonenlist.getString("msg"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			// 上传图片到七牛成功后 把图片在七牛服务器上的地址上传到服务器
			case NET_UPLOAP:
				ResponseInfo responseInfo = (ResponseInfo) msg.obj;

				if (responseInfo.statusCode == 200) {
					picurls.add(picurl);
					Message message = Message.obtain();
					message.what = UPDATE_GRIDVIEW;
					handler.sendMessage(message);
				}
				break;
			case UPDATE_GRIDVIEW:
				// adapter.notifyDataSetChanged();
				sport_screenshots.setAdapter(adapter);
				break;
			// 上传图片地址
			case POST_PHOTE_URL:
				JSONObject json = (JSONObject) msg.obj;
				try {
					if (json.getInt("status") == 200) {
						// 成功后更新图片gridview的布局
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}

		}
	};

	/**
	 * 上传图片在七牛的地址到服务器
	 */
	private void submitUserInfoToNet(final int activityid) {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				photesParams = new HashMap<String, String>();
				for (int i = 0; i < picurls.size(); i++) {
					photesParams.clear();
					photesParams.put("url", picurls.get(i));
					photesParams.put("target_id", activityid + "");
					photesParams.put("target_type", "Activity");
					HttpUtil.Post(UrlConstants.POST_PHOTE_URL, handler,
							POST_PHOTE_URL, photesParams);
				}
			}
		});
	}

	public class GridAdapter extends BaseAdapter {

		public int getCount() {
			if (picurls == null) {
				return 1;
			}
			if (picurls.size() >= 5) {
				return 5;
			}
			return picurls.size() + 1;
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(EditRusultActivity.this,
						R.layout.item_published_grida, null);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == picurls.size()) {
				holder.image.setImageResource(R.drawable.icon_addpic_focused);
			} else {
				ImageUtil.showImage(picurls.get(position)
						+ Constants.qiniu_photo_business, holder.image,
						R.drawable.nopic);
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (ll_choose_date.getVisibility() == View.VISIBLE) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private String toJsonString() {
		JSONArray score_1 = new JSONArray();
		for (int i = 0; i < s_doubles.size(); i++) {
			try {
				JSONObject score_2 = new JSONObject();
				score_2.put("type", s_doubles.get(i));
				score_2.put("our_score", ourScores.get(i));
				score_2.put("other_score", otherScores.get(i));
				score_1.put(score_2);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return score_1.toString();
	}

	private void onEventMainThread(SmallScore ss) {
		s_doubles = ss.getS_doubles();
		ourScores = ss.getOurScores();
		otherScores = ss.getOtherScores();

		// 显示小局分类的listView
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

}