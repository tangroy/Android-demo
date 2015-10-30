package com.oxygen.www.module.sport.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.MenuActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.db.DBManager;
import com.oxygen.www.enties.Comment;
import com.oxygen.www.enties.CurrentEventUser;
import com.oxygen.www.enties.Event;
import com.oxygen.www.enties.GDAcvitity;
import com.oxygen.www.enties.Group;
import com.oxygen.www.enties.Moment;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.challengs.activity.ChallengesDetailActivity;
import com.oxygen.www.module.sport.adapter.EventMomentAdapter;
import com.oxygen.www.module.sport.adapter.RankAdapter;
import com.oxygen.www.module.sport.adapter.SportDetailAcceptAdapter;
import com.oxygen.www.module.sport.construct.EventConstruct;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.module.sport.eventbus_enties.WeChatPay;
import com.oxygen.www.module.sport.activity.CreatedPlanActivity;
import com.oxygen.www.module.sport.writemoment.activity.WriteMomentsActivity;
import com.oxygen.www.module.team.activity.GroupDetailActivity;
import com.oxygen.www.module.team.activity.TeamQRActivity;
import com.oxygen.www.module.user.activity.InviteFriendActivity;
import com.oxygen.www.module.user.activity.UserInfoActivity;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.KeyBoardUtils;
import com.oxygen.www.utils.QQUtils;
import com.oxygen.www.utils.TextUtils;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.utils.WxUtil;
import com.oxygen.www.widget.MyWebView;
import com.oxygen.www.widget.NoScrollGridView;
import com.oxygen.www.widget.NoScrollListView;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

/**
 * 活动详情页
 * 
 * @author sambatang
 * 
 */
public class EventsResultActivity extends Activity implements
		SensorEventListener, OnClickListener {

	/** 请求码, 打开发表动态页面 */
	private static final int writeMomentRequestCode = 20;
	/** 请求码, 打开签到页面 */
	private static final int checkInRequestCode = 30;

	/** 显示页面信息 */
	private static final int NET_SHOWEVENTINFO = 1;
	/** 取消活动 */
	private static final int NET_CANCELEVENT = 2;
	/** * 处理接受/拒绝邀请 (退出活动) */
	private static final int NET_ACCEPT_DECLINE_EXIT = 3;
	/**
	 * 
	 */
	private static final int NET_CREATEDACTIVITIES = 4;
	/** * 发表评论 */
	private static final int POST_COMENT = 5;
	/** * 更新发表的动态 (发表动态后刷新页面) */
	private static final int NET_UPDATEMOMENTS = 6;
	/** * 获取预支付id */
	private static final int NET_EVENTS_PRE_PAY = 7;
	/** * 获取弹出金币个数 */
	private static final int NET_COIN_CNT = 8;
	/** * 报名者填写项 */
	private static final int NET_ENTRY_FORM = 9;
	/** * 获取用户扩展信息 */
	private static final int NET_USER_EXTEND_INFO = 10;

	private ImageView iv_back, iv_event_name, iv_address_info,
			iv_checked_status, iv_sportpic, iv_team_info;
	private ImageButton iv_share;
	private TextView tv_sport_time, tv_address, tv_accept_count, tv_status,
			tv_event_name, iv_share_friend, iv_share_weixin, iv_share_qq,
			iv_share_weixin_friends, tv_intro, iv_share_bottom,
			tv_title_ranking, tv_ranking_tip;
	public static TextView moment_defttext, tv_moments_more;
	public static ImageView iv_nomoment;

	/** * 更多的 popupwindow 窗内的按钮 */
	private TextView tv_lab_share, checkin, update_evnent, exit_evnent,
			cancel_evnent, watch_QR_cord;
	/** * 更多的 popupwindow 窗内的分享布局 */
	private LinearLayout ll_share;
	/** * 顶部墙纸 */
	private FrameLayout ll_waitplan;

	/** * 报名列表 */
	private NoScrollGridView gv_accept;
	/** * 战绩秀 */
	private NoScrollListView lv_rank;
	/** * 活动动态 */
	public NoScrollListView lv_moments;
	/** * 活动标题 */
	private RelativeLayout rl_eventname;
	/** * 活动时间 */
	private RelativeLayout rl_sport_time;
	/** * 战绩秀 */
	private RelativeLayout rl_ranking;
	/** * 活动地址 */
	private RelativeLayout rl_address;
	/** * 活动关联团队 */
	private RelativeLayout rl_created_team;
	/** * 活动介绍 */
	private RelativeLayout rl_event_info;
	/** * 已报名 */
	private RelativeLayout rl_accept;
	/** * 战绩秀标题提示 */
	private RelativeLayout rl_title_ranking;
	/** * 活动动态 图片 点击放大 全屏浏览 */
	private ViewPager vp_group_photo;
	/**
	 * 
	 */
	public TextView btn_publishmood;
	/** * event 的 id */
	private int eventid;
	/** * event 活动的所有信息 */
	private Event event;
	/** * 活动关联的团队 */
	private Group group;
	/** 活动标题 */
	private String title;
	/** 活动限制人数 */
	private String limitation;
	/** * 分享链接 */
	private String Shareurl;
	/** * 活动角色(组织者/参与者/被邀请者) */
	private String role;
	JSONObject jsonobject_userinfo;
	/** * 当前用户 */
	private User current_user;
	DecimalFormat df = new DecimalFormat("#0.00");
	/** * 字体信息 */
	Typeface typeface;
	/** * 日期显示格式 */
	public static SimpleDateFormat dwf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	/** * 报名人数 */
	List<User> users_accept_list;
	/** * 动态集合 */
	ArrayList<Moment> moments;
	/** * accept 为报名，decline为拒绝，exit为退出 */
	private String Invite_handlestatus;
	/** * 运动所属归类 */
	private int sportcategory;
	/** * 声明PopupWindow对象的引用 */
	private PopupWindow popupWindow;
	/** * 发表评论线性布局 */
	private LinearLayout ll_moment;
	/** * 邀请小伙伴-发表动态 */
	private LinearLayout ll_vote;
	private Button bt_commit;
	private TextView tv_team_name;
	private ScrollView sv_parent;
	/** * 发表评论输入框 */
	private EditText et_moment;
	public static List<String> s_doubles;
	public static List<Integer> ourScores;
	public static List<Integer> otherScores;
	String[] userid;

	private RelativeLayout rl_synchronize;
	private TextView tv_local_data;
	private ImageView iv_synchronize;
	/** * 加载中... */
	private RelativeLayout rl_loading;
	/** * 加载失败 */
	private RelativeLayout rl_error;
	/** * 数据为空 */
	private RelativeLayout rl_empty;
	/** * 数据为空 */
	private TextView tv_empty;
	DBManager dmr;
	private GDAcvitity localdata;

	/** * 用户与当前活动的关系 (没有任何关系则为 null, ) */
	private CurrentEventUser currentEventUser;

	/** * 报名状态 (与活动关系: 没有关系, invite, accept, decline) */
	private String accept_status;
	/** * 活动开始时间 */
	String startTime;
	/** * 活动开始时间 */
	String endTime;
	/** * 活动介绍信息 */
	private String intro;

	/** * 是否已签到 */
	private boolean isCheckIn;
	/** * 摇一摇签到浮窗 */
	private RelativeLayout rl_shake_check_in;

	/** * 摇一摇签到浮窗关闭按钮 */
	private ImageView iv_close;
	/** * 活动地点经纬度(纬度) */
	private double latitude;
	/** * 活动地点经纬度(经度) */
	private double longitude;
	/** * 活动地点 */
	private String address;
	/** * 报名者填写项 */
	private String entry_form;

	/** * sensor 管理器 */
	private SensorManager mSensorManager;
	/** * 震动器 */
	private Vibrator mVibrator;
	/** * 是否刚创建活动 */
	private boolean isFromCreatedPlanActivity;
	int bestDuration = 0;

	/** * 报名者填写项 */
	private EditText et_name, et_mobile, et_department, et_number, et_company,
			et_email, et_remarks;
	/** * 报名者填写项-完成 */
	private TextView tv_finish;
	/** * 活动是否免费 */
	private boolean isFree = true;
	/** * 付费活动 特殊标记, 刚付完费报名, 不出现签到按钮 */
	private boolean Flag = false;
	/** * entry_form 转换成的数组(有序) */
	private String[] entrys;
	/** * 从服务器获取到的 entry_form 的 map */
	private Map<String, String> map;

	/** * 微信支付 api */
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

	/**
	 * 富文本
	 */
	private MyWebView web_rich;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eventsresultl);

		initViews();
		initEvents();
		initValues();

	}

	private void initViews() {
		sv_parent = (ScrollView) findViewById(R.id.sv_parent);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_share = (ImageButton) findViewById(R.id.iv_share);

		ll_waitplan = (FrameLayout) findViewById(R.id.ll_waitplan);
		iv_sportpic = (ImageView) findViewById(R.id.iv_sportpic);

		rl_shake_check_in = (RelativeLayout) findViewById(R.id.rl_shake_check_in);
		iv_close = (ImageView) findViewById(R.id.iv_close);

		tv_status = (TextView) findViewById(R.id.tv_status);
		iv_checked_status = (ImageView) findViewById(R.id.iv_checked_status);

		rl_eventname = (RelativeLayout) findViewById(R.id.rl_eventname);
		rl_sport_time = (RelativeLayout) findViewById(R.id.rl_sport_time);
		rl_address = (RelativeLayout) findViewById(R.id.rl_address);
		rl_created_team = (RelativeLayout) findViewById(R.id.rl_created_team);
		rl_event_info = (RelativeLayout) findViewById(R.id.rl_event_info);

		tv_event_name = (TextView) findViewById(R.id.tv_event_name);
		tv_sport_time = (TextView) findViewById(R.id.tv_sport_time);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_team_name = (TextView) findViewById(R.id.tv_team_name);
		tv_intro = (TextView) findViewById(R.id.tv_intro);
		web_rich = (MyWebView) findViewById(R.id.web_rich);

		iv_event_name = (ImageView) findViewById(R.id.iv_event_name);
		iv_address_info = (ImageView) findViewById(R.id.iv_address_info);
		iv_team_info = (ImageView) findViewById(R.id.iv_team_info);
		// iv_intro_arrows = (ImageView) findViewById(R.id.iv_intro_arrows);

		tv_accept_count = (TextView) findViewById(R.id.tv_accept_count);
		iv_share_friend = (TextView) findViewById(R.id.iv_share_friend);
		iv_share_weixin = (TextView) findViewById(R.id.iv_share_weixin);
		iv_share_qq = (TextView) findViewById(R.id.iv_share_qq);
		iv_share_weixin_friends = (TextView) findViewById(R.id.iv_share_weixin_friends);

		rl_accept = (RelativeLayout) findViewById(R.id.rl_accept);
		gv_accept = (NoScrollGridView) findViewById(R.id.gv_accept);

		rl_ranking = (RelativeLayout) findViewById(R.id.rl_ranking);
		rl_title_ranking = (RelativeLayout) findViewById(R.id.rl_title_ranking);
		tv_ranking_tip = (TextView) findViewById(R.id.tv_ranking_tip);
		lv_rank = (NoScrollListView) findViewById(R.id.lv_rank);
		tv_title_ranking = (TextView) findViewById(R.id.tv_title_ranking);

		moment_defttext = (TextView) findViewById(R.id.moment_defttext);
		tv_moments_more = (TextView) findViewById(R.id.tv_moments_more);
		lv_moments = (NoScrollListView) findViewById(R.id.lv_moments);
		iv_nomoment = (ImageView) findViewById(R.id.iv_nomoment);
		ll_moment = (LinearLayout) findViewById(R.id.ll_moment);
		ll_vote = (LinearLayout) findViewById(R.id.ll_vote);

		btn_publishmood = (TextView) findViewById(R.id.btn_publishmood);
		iv_share_bottom = (TextView) findViewById(R.id.iv_share_bottom);
		et_moment = (EditText) findViewById(R.id.et_moment);
		bt_commit = (Button) findViewById(R.id.bt_commit);

		vp_group_photo = (ViewPager) findViewById(R.id.vp_group_photo);

		tv_local_data = (TextView) findViewById(R.id.tv_local_data);
		rl_synchronize = (RelativeLayout) findViewById(R.id.rl_synchronize);
		iv_synchronize = (ImageView) findViewById(R.id.iv_synchronize);

		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
		rl_error = (RelativeLayout) findViewById(R.id.rl_error);
		rl_empty = (RelativeLayout) findViewById(R.id.rl_empty);
		tv_empty = (TextView) findViewById(R.id.tv_empty);

	}

	private void initEvents() {
		iv_back.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		iv_address_info.setOnClickListener(this);
		btn_publishmood.setOnClickListener(this);
		iv_share_bottom.setOnClickListener(this);
		rl_accept.setOnClickListener(this);
		rl_title_ranking.setOnClickListener(this);
		tv_moments_more.setOnClickListener(this);
		bt_commit.setOnClickListener(this);
		rl_eventname.setOnClickListener(this);
		rl_sport_time.setOnClickListener(this);
		rl_address.setOnClickListener(this);
		rl_event_info.setOnClickListener(this);
		rl_created_team.setOnClickListener(this);
		iv_synchronize.setOnClickListener(this);
		sv_parent.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (ll_moment != null) {
					ll_moment.setVisibility(View.INVISIBLE);
				}
				return false;
			}
		});

		iv_close.setOnClickListener(this);
		rl_error.setOnClickListener(this);

	}

	private void initPopViews(View popupWindow_view) {
		iv_share_friend = (TextView) popupWindow_view
				.findViewById(R.id.iv_share_friend);
		iv_share_weixin = (TextView) popupWindow_view
				.findViewById(R.id.iv_share_weixin);
		iv_share_weixin_friends = (TextView) popupWindow_view
				.findViewById(R.id.iv_share_weixin_friends);
		iv_share_qq = (TextView) popupWindow_view
				.findViewById(R.id.iv_share_qq);
		checkin = (TextView) popupWindow_view.findViewById(R.id.checkin);
		update_evnent = (TextView) popupWindow_view
				.findViewById(R.id.update_evnent);
		ll_share = (LinearLayout) popupWindow_view.findViewById(R.id.ll_share);
		tv_lab_share = (TextView) popupWindow_view
				.findViewById(R.id.tv_lab_share);
		exit_evnent = (TextView) popupWindow_view
				.findViewById(R.id.exit_evnent);
		cancel_evnent = (TextView) popupWindow_view
				.findViewById(R.id.cancel_evnent);
		watch_QR_cord = (TextView) popupWindow_view
				.findViewById(R.id.watch_QR_cord);
	}

	private void initPopViewEvENT() {
		iv_share_friend.setOnClickListener(this);
		iv_share_weixin.setOnClickListener(this);
		iv_share_weixin_friends.setOnClickListener(this);
		iv_share_qq.setOnClickListener(this);
		checkin.setOnClickListener(this);
		update_evnent.setOnClickListener(this);
		exit_evnent.setOnClickListener(this);
		cancel_evnent.setOnClickListener(this);
		watch_QR_cord.setOnClickListener(this);
	}

	private void initValues() {
		// 注册 event bus
		EventBus.getDefault().register(this);
		// 将该app注册到微信
		msgApi.registerApp(Constants.WEIXIN_APPID);
		// 字体 ?
		typeface = Typeface.createFromAsset(this.getAssets(),
				"fonts/Impact.ttf");
		// 获取传感器管理服务
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// 震动
		mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

		eventid = getIntent().getIntExtra("eventid", 0);
		OxygenApplication.Eventid = eventid;
		isFromCreatedPlanActivity = getIntent().getBooleanExtra(
				"isFromCreatedPlanActivity", false);
		getEventsInfoInNet(OxygenApplication.Eventid);
		dmr = new DBManager(EventsResultActivity.this);

	}

	@Override
	protected void onResume() {
		// 加速度传感器
		mSensorManager.registerListener(
				this,
				// 还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
				// 根据不同应用，需要的反应速率不同，具体根据实际情况设定
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

		lv_rank.setEnabled(true);

		if (eventid != 0) {
			ArrayList<GDAcvitity> gds = dmr.querysportsforevent(eventid);
			if (gds.size() > 0) {
				localdata = gds.get(0);
				rl_synchronize.setVisibility(View.VISIBLE);
				tv_local_data.setText("距离"
						+ df.format(localdata.distance / 1000) + "公里  时长"
						+ GDUtil.TransitionTime(localdata.getDuration()));
			}
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	/**
	 * 更新 event 信息
	 * 
	 * @param json_date
	 */
	private void Updateui(JSONObject json_date) {

		event = EventConstruct.Toevent(json_date);
		if (event != null) {
			// 分享链接
			Shareurl = UrlConstants.SHARE_INVIT_URL + event.get_id()
			 + "?token=" + event.getToken();

			// 基本信息
			UpdateEventUi();

			// 战绩秀
			sportcategory = GDUtil.SportCategory(event.getSport_eng());
			if (sportcategory == Constants.COUNT_CATEGORY_OTHER) {
				// 其他 类型, 不显示战绩秀
				rl_ranking.setVisibility(View.GONE);
			} else {
				UpdateRanking();
			}

			// 活动动态
			UpdateMomentsUi();

		}

	}

	/**
	 * 是否是限定时间内
	 * 
	 * @return
	 */
	private boolean isLimitTime() {
		Date startDate = null;
		Date endDate = null;
		Date now = new Date(System.currentTimeMillis());

		try {
			startDate = dwf.parse(event.getStart_time());
			endDate = dwf.parse(event.getEnd_time());

		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (now.getTime() - startDate.getTime() < -1000 * 3600
				|| now.getTime() - endDate.getTime() > 1000 * 3600) {
			// 正负一小时以外
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 更新EVENT基本信息UI
	 */
	private void UpdateEventUi() {

		// 显示摇一摇签到的时机
		currentEventUser = event.getCurrent_event_user();
		if (currentEventUser != null) {
			// 签到状态
			String checkinStatus = currentEventUser.getCheckin_staus();

			if (checkinStatus.equals("by_user")
					|| checkinStatus.equals("by_admin")) {
				// 已签到(自己签到, 管理者代签到, 已记录成绩)
				isCheckIn = true;
				iv_checked_status.setVisibility(View.VISIBLE);
			} else if (checkinStatus.equals("no")) {
				// 没有签到
				if (!isFromCreatedPlanActivity
						&& currentEventUser.getStatus().equals("accept")
						&& !(TextUtils.isEmpty(event.getAddress()) || "待定"
								.equals(event.getAddress())) && isLimitTime()
						&& !Flag) {
					// 没有签到(不是刚创建活动, 已报名, 活动地点不为"待定", 正负一小时, (付费活动 Flag 为false))
					rl_shake_check_in.setVisibility(View.VISIBLE);

				}
			}
		}

		// 让scrollView中的一个控件 获取焦点 这样scrollView就能自动显示在最顶端(下面三行代码)
		ll_waitplan.setFocusable(true);
		ll_waitplan.setFocusableInTouchMode(true);
		ll_waitplan.requestFocus();
		// 头部背景
		int i = getResources().getIdentifier(
				"bg_eventdetail_" + event.sport_eng, "drawable",
				getPackageName());
		ImageUtil.showImage2(
				(event.getPic() + Constants.qiniu_photo_find).trim(),
				iv_sportpic, i);
		// 头部运动类型图片
		iv_event_name
				.setImageDrawable(GDUtil.engSporttodrawable(
						EventsResultActivity.this,
						"icon_index_" + event.getSport_eng()));

		if (currentEventUser != null) {
			if ("accept".equals(currentEventUser.getStatus())) {
				// 角色
				role = currentEventUser.getRole();
				// 已记录成绩，地步不现实记录成绩按钮
				if (event.getPerformance() != null
						&& "close".equals(event.getPerformance().getStatus())) {
					btn_publishmood.setVisibility(View.GONE);
				}
			}
			accept_status = currentEventUser.getStatus();

		} else {
			// 未产生任何关联
			accept_status = "notaccept";
		}

		String eventstatus = "报名中";
		// 活动已取消
		if (event.status.equals("cancel")) {
			eventstatus = "活动已取消";
			iv_share_bottom.setVisibility(View.GONE);
			btn_publishmood.setText(eventstatus);
			btn_publishmood.setGravity(Gravity.CENTER);
		} else {
			try {
				startTime = event.getStart_time();
				if (startTime != null
						&& !GDUtil.isDateBefore(dwf.parse(startTime))) {
					rl_ranking.setVisibility(View.VISIBLE);
					String end_time = event.getEnd_time();
					if (end_time != null
							&& !GDUtil.isDateBefore(dwf.parse(end_time))) {
						event.status = "HAVE_FINISHED";
						eventstatus = "已结束";
					} else {
						eventstatus = "已开始";
						event.status = "HAVE_STARTED";
					}
					// 其他类隐藏记录成绩按钮
					if (sportcategory == Constants.COUNT_CATEGORY_OTHER) {
						btn_publishmood.setVisibility(View.GONE);
					}
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

		}
		tv_status.setText(eventstatus);

		if (event.status.equals("cancel")) {
			btn_publishmood.setText("活动已取消");
		} else {
			if ("accept".equals(accept_status)) {
				try {
					if (startTime != null
							&& !GDUtil.isDateBefore(dwf.parse(startTime))) {
						btn_publishmood.setText("记录成绩");
					} else {
						btn_publishmood.setText("邀请小伙伴");
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				iv_share_bottom
						.setBackgroundResource(R.drawable.wane_shape_red_hollow);
				iv_share_bottom.setText("发表动态");
			} else if ("decline".equals(accept_status)) {
				btn_publishmood.setText("重新报名");
				iv_share_bottom.setText("已婉拒");
				iv_share_bottom.setBackgroundColor(EventsResultActivity.this
						.getResources().getColor(R.color.transparent));
				;
			} else if ("notaccept".equals(accept_status)
					|| "invite".equals(accept_status)) {
				btn_publishmood.setText("我要报名");
				iv_share_bottom.setText("下次吧");
				iv_share_bottom
						.setBackgroundResource(R.drawable.wane_shape_red_hollow);
			}
		}

		// 标题
		title = event.getTilte();
		tv_event_name.setText(title);

		// 时间
		startTime = event.getStart_time();
		endTime = event.getEnd_time();
		tv_sport_time.setText(formatTime());

		// 地点
		address = TextUtils.isEmpty(event.getAddress()) ? "待定" : event
				.getAddress();
		tv_address.setText(address);

		if (!"待定".equals(address)) {
			iv_address_info.setVisibility(View.VISIBLE);
		}
		latitude = event.getLatitude();
		longitude = event.getLongitude();

		// 团队
		if (!TextUtils.isEmpty(event.getGroup_id())) {
			group = event.getGroup();
			if (group != null) {
				iv_team_info.setVisibility(View.VISIBLE);
				tv_team_name.setText(group.getName());
			}
		}

		// 活动介绍
		intro = TextUtils.isEmpty(event.getIntro()) ? "一起运动更快乐, 约吗?" : event
				.getIntro();
		
		Log.i("intro", intro);

		if (intro.contains("<") && intro.contains(">")) {
			web_rich.setVisibility(View.VISIBLE);
			tv_intro.setVisibility(View.GONE);
			
			// 补全
			String html = "<html> <head> <meta charset= \"utf-8\"> <meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1, user-scalable=no\"> </head>  <body>";
			html += intro;
			html += "</body> </html>";
			
			web_rich.loadData(html, "text/html; charset=UTF-8", null);

		} else {
			tv_intro.setVisibility(View.VISIBLE);
			web_rich.setVisibility(View.GONE);
			tv_intro.setText(intro);
		}

		// 报名者填写项
		if (!TextUtils.isEmpty(event.getEntry_form())) {
			entry_form = event.getEntry_form();
		}

		// TODO...
		//
		limitation = event.getLimitation() == 0 ? "不限" : event.getLimitation()
				+ "";
		users_accept_list = event.getAcceptlist();
		tv_accept_count.setText("(" + event.getAccept_count() + "/"
				+ limitation + ")");
		userid = new String[users_accept_list.size()];

		if (users_accept_list != null && users_accept_list.size() > 0) {
			final int count = users_accept_list.size();
			for (int j = 0; j < users_accept_list.size(); j++) {
				userid[j] = users_accept_list.get(j).getUser_id() + "";
			}
			SportDetailAcceptAdapter dailadapter;
			if (count > 5) {
				dailadapter = new SportDetailAcceptAdapter(false,
						EventsResultActivity.this, userid, jsonobject_userinfo,
						5);

			} else {
				dailadapter = new SportDetailAcceptAdapter(false,
						EventsResultActivity.this, userid, jsonobject_userinfo,
						count);

			}

			gv_accept.setAdapter(dailadapter);
			gv_accept.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (count > 5) {
						if (position == 5) {
							// 邀请小伙伴
							getPopupWindow(btn_publishmood);
							// 这里是位置显示方式,在屏幕的左侧
							popupWindow.showAtLocation(view, Gravity.BOTTOM, 0,
									0);
						} else {// 查看信息
							String userid = users_accept_list.get(position)
									.getUser_id() + "";
							Intent intent = new Intent(
									EventsResultActivity.this,
									UserInfoActivity.class);
							intent.putExtra("userid", userid);
							if (userid.equals((String) UserInfoUtils
									.getUserInfo(getApplicationContext(),
											Constants.USERID, ""))) {
								intent.putExtra("isme", true);
							} else {
								intent.putExtra("isme", false);
							}
							startActivity(intent);
						}
					} else {
						if (position == count) {
							// 邀请小伙伴
							getPopupWindow(btn_publishmood);
							// 这里是位置显示方式,在屏幕的左侧
							popupWindow.showAtLocation(view, Gravity.BOTTOM, 0,
									0);
						} else {// 查看信息
							String userid = users_accept_list.get(position)
									.getUser_id() + "";
							Intent intent = new Intent(
									EventsResultActivity.this,
									UserInfoActivity.class);
							intent.putExtra("userid", userid);
							if (userid.equals((String) UserInfoUtils
									.getUserInfo(getApplicationContext(),
											Constants.USERID, ""))) {
								intent.putExtra("isme", true);
							} else {
								intent.putExtra("isme", false);
							}
							startActivity(intent);
						}
					}
				}
			});
		}
	}

	/**
	 * 格式化时间
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	private String formatTime() {
		String formatTime = startTime.substring(5, 16) + "~"
				+ endTime.substring(5, 16);

		int startYear = Integer.parseInt(startTime.substring(0, 4));
		int startMonth = Integer.parseInt(startTime.substring(5, 7));
		int startDay = Integer.parseInt(startTime.substring(8, 10));

		int endYear = Integer.parseInt(endTime.substring(0, 4));
		int endMonth = Integer.parseInt(endTime.substring(5, 7));
		int endDay = Integer.parseInt(endTime.substring(8, 10));

		if (endYear > startYear) {
			formatTime = startTime.substring(0, 16) + "~"
					+ endTime.substring(0, 16);
		} else if (endMonth > startMonth) {
			formatTime = startTime.substring(5, 16) + "~"
					+ endTime.substring(5, 16);
		} else if (endDay == startDay) {
			formatTime = startTime.substring(5, 16) + "~"
					+ endTime.substring(11, 16);
		}

		return formatTime;
	}

	/**
	 * 更新活动动态
	 */
	private void UpdateMomentsUi() {

		moments = event.getMoments();
		if (moments != null && moments.size() > 0) {
			iv_nomoment.setVisibility(View.GONE);
			moment_defttext.setVisibility(View.GONE);
			int count = moments.size();
			EventMomentAdapter momentAdapter;
			// if (count > 1) {
			// tv_moments_more.setVisibility(View.VISIBLE);
			// tv_moments_more.setText("查看全部评论(" + count + ")");
			// momentAdapter = new EventMomentAdapter(ll_moment, et_moment,
			// EventsResultActivity.this, moments,
			// jsonobject_userinfo, current_user.getId(), 1, false,
			// vp_group_photo);
			// } else {
			momentAdapter = new EventMomentAdapter(ll_moment, et_moment,
					lv_moments, EventsResultActivity.this, moments,
					jsonobject_userinfo, current_user.getId(),
					event.getCreated_by(), count, true, vp_group_photo);
			// }
			ViewGroup.LayoutParams params = lv_moments.getLayoutParams();
			params.height = LayoutParams.WRAP_CONTENT;
			lv_moments.setLayoutParams(params);
			lv_moments.setAdapter(momentAdapter);
			// Untilly.setListViewHeightBasedOnChildren(lv_moments, 0);
		} else {
			iv_nomoment.setVisibility(View.VISIBLE);
			moment_defttext.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 更新战绩秀
	 */
	private void UpdateRanking() {

		final ArrayList<GDAcvitity> ranklist = event.getRanklist();
		String ranking = event.getRanking();
		// 按照创建时间排名不显示
		if (ranklist != null && ranklist.size() > 0) {

			tv_title_ranking.setText(Html.fromHtml("战绩秀("
					+ event.getRank_count() + "人)"));
			rl_ranking.setVisibility(View.VISIBLE);
			tv_ranking_tip.setVisibility(View.GONE);

			RankAdapter rankadapter = new RankAdapter(
					EventsResultActivity.this, ranklist, jsonobject_userinfo,
					ranking, current_user);
			ViewGroup.LayoutParams params_b = lv_rank.getLayoutParams();
			// params_b.height = ranklist.size()
			// * ((int) (55 * OxygenApplication.ppi + 0.5));
			params_b.height = LayoutParams.WRAP_CONTENT;
			lv_rank.setLayoutParams(params_b);
			lv_rank.setAdapter(rankadapter);
			if (event.getBpp() != null) {
				bestDuration = event.getBpp().getDuration();
			}

			lv_rank.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						final int position, long id) {
					lv_rank.setEnabled(false);
					Intent personalScoreIntent = new Intent(
							EventsResultActivity.this,
							GDActivityResultActivity.class);
					personalScoreIntent
							.putExtra("sportcategory", sportcategory);
					personalScoreIntent.putExtra("bestDuration", bestDuration);
					personalScoreIntent.putExtra("event", event);
					personalScoreIntent.putExtra("sport_eng",
							event.getSport_eng());
					personalScoreIntent.putExtra("activityid",
							ranklist.get(position).getActivity_id());
					startActivity(personalScoreIntent);
					lv_rank.setEnabled(true);
				}
			});
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// 返回键
		case R.id.iv_back:
			reback();
			break;

		// 关闭摇一摇浮窗
		case R.id.iv_close:
			rl_shake_check_in.setVisibility(View.GONE);
			break;

		// 更多按钮
		case R.id.iv_share:
			if (event != null) {
				getPopupWindow(iv_share);
				// 这里是位置显示方式,在屏幕的左侧
				popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}
			break;

		/** 更多按钮 弹出popupwindow 内部按钮 开始开始开始开始开始开始开始开始开始开始开始开始 */
		// 签到
		case R.id.checkin:
			if (isCheckIn) {
				// 如果已签到, 提示 是否重复签到
				Builder builder = new Builder(EventsResultActivity.this);
				builder.setTitle("你之前已经签到过, 是否重新签到?");
				builder.setNegativeButton("取消", null);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 签到
								checkIn();
							}

						});

				builder.show();

			} else {
				// 签到
				checkIn();
			}
			dismissPopWindow();
			break;

		// 修改活动信息
		case R.id.update_evnent:
			Intent intent = new Intent(EventsResultActivity.this,
					CreatedPlanActivity.class);
			intent.putExtra("event", event);
			intent.putExtra("type", Constants.SPORTTYPE_UPDATE);
			startActivity(intent);
			dismissPopWindow();
			break;

		// 查看团队二维码
		case R.id.watch_QR_cord:
			Intent QRIntent = new Intent(this, TeamQRActivity.class);
			Bundle b = new Bundle();
			b.putString("title", "活动二维码");
			b.putString("shareurl", Shareurl);
			b.putString("name", title);
			b.putString("pic", current_user.getHeadimgurl());
			b.putString("description", "已报名(" + event.getAccept_count() + "/"
					+ limitation + ")");
			QRIntent.putExtra("data", b);
			startActivity(QRIntent);
			dismissPopWindow();
			break;

		// 取消活动
		case R.id.cancel_evnent:
			showDialog(EventsResultActivity.this, "您确定取消本次活动么？",
					NET_CANCELEVENT);
			dismissPopWindow();
			break;

		// 退出活动
		case R.id.exit_evnent:
			showDialog(EventsResultActivity.this, "您确定退出本次活动么？",
					NET_ACCEPT_DECLINE_EXIT);
			dismissPopWindow();
			break;

		// 分享乐运动好友
		case R.id.iv_share_friend:
			Intent inviteIntent = new Intent(this, InviteFriendActivity.class);
			inviteIntent.putExtra("event", event);
			startActivity(inviteIntent);
			dismissPopWindow();
			break;
		// 分享至微信
		case R.id.iv_share_weixin:
			share2weixin(0);
			dismissPopWindow();
			break;
		// 分享到 QQ
		case R.id.iv_share_qq:
			share2qq();
			dismissPopWindow();
			break;
		// 分享至微信朋友圈
		case R.id.iv_share_weixin_friends:
			share2weixin(1);
			dismissPopWindow();
			break;
		/** 结束结束结束结束结束结束结束 更多按钮 弹出popupwindow 内部按钮 结束结束结束结束结束结束结束结束 */

		// 活动地址
		case R.id.rl_address:
			if (!address.equals("待定")) {
				Intent addIntent = new Intent(this,
						EventAddressMapInfoActivity.class);
				addIntent.putExtra("address", address);
				addIntent.putExtra("latitude", latitude);
				addIntent.putExtra("longitude", longitude);
				startActivity(addIntent);
			}
			break;

		// 关联团队的信息
		case R.id.rl_created_team:
			if (!TextUtils.isEmpty(event.getGroup_id())) {

				Group group = event.getGroup();
				if (group != null) {
					// 查看团队信息
					Intent teamIntent = new Intent(this,
							GroupDetailActivity.class);
					teamIntent.putExtra("groupid",
							Integer.parseInt(event.getGroup_id()));
					startActivity(teamIntent);
				}
			}
			break;

		// 活动介绍详细信息
		/*
		 * case R.id.rl_event_info: Intent introIntent = new Intent(this,
		 * EventsResultIntroActivity.class); introIntent.putExtra("intro",
		 * intro); startActivity(introIntent); break;
		 */

		// 已报名
		case R.id.rl_accept:
			Intent applyIntent = new Intent(this, ApplyPersonActivity.class);
			applyIntent.putExtra("event", event);
			applyIntent.putExtra("current_user", current_user);
			startActivity(applyIntent);
			break;

		// 战绩秀
		case R.id.rl_title_ranking:
			Intent intent_rankmore = new Intent(EventsResultActivity.this,
					EventSalemanageActivity.class);
			intent_rankmore.putExtra("event", event);
			startActivity(intent_rankmore);
			break;

		// 查看全部动态
		case R.id.tv_moments_more:
			tv_moments_more.setVisibility(View.GONE);
			EventMomentAdapter momentAdapter = new EventMomentAdapter(
					ll_moment, et_moment, lv_moments,
					EventsResultActivity.this, moments, jsonobject_userinfo,
					current_user.getId(), event.getCreated_by(),
					moments.size(), true, vp_group_photo);
			ViewGroup.LayoutParams params = lv_moments.getLayoutParams();
			params.height = LayoutParams.WRAP_CONTENT;
			lv_moments.setLayoutParams(params);
			lv_moments.setAdapter(momentAdapter);
			// Untilly.setListViewHeightBasedOnChildren(lv_moments, 0);
			break;

		// 底部状态栏, 左侧: 邀请小伙伴,我要报名 或者 记录成绩(根据不同的状态)
		case R.id.btn_publishmood:
			if (event != null) {
				if (!event.status.equals("cancel")) {
					if (accept_status.equals("accept")) {
						if ("HAVE_STARTED".equals(event.status)
								|| "HAVE_FINISHED".equals(event.status)) {// 已开始状态-记录成绩
							if (sportcategory == Constants.COUNT_CATEGORY_RUNNING
									|| sportcategory == Constants.COUNT_CATEGORY_PLANK
									|| sportcategory == Constants.COUNT_CATEGORY_DISTANCE) {
								if (event.getPerformance() != null) {
									Intent intent_start = new Intent(
											EventsResultActivity.this,
											SportStartActivity.class);
									intent_start.putExtra("type",
											Constants.SPORTTYPE_UPDATE);
									intent_start.putExtra("isFromEventsResult",
											true);
									intent_start.putExtra("event", event);
									startActivity(intent_start);
								} else {
									ToastUtil.show(EventsResultActivity.this,
											"异常数据");
								}
								// 不为其他类的时候记录成绩
							} else if (sportcategory != Constants.COUNT_CATEGORY_OTHER) {
								Intent intent_edit = new Intent(
										EventsResultActivity.this,
										EditRusultActivity.class);
								intent_edit.putExtra("event", event);
								intent_edit.putExtra("type",
										Constants.SPORTTYPE_OTHER);
								startActivity(intent_edit);
							}
						} else {// 未开始状态-邀请好友(报名中)
							getPopupWindow(btn_publishmood);
							// 这里是位置显示方式,在屏幕的左侧
							popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
						}
					} else {
						// 点击报名
						Invite_handlestatus = "accept";

						if ("free".equals(event.getPricing())) {
							// 免费活动
							if (!TextUtils.isEmpty(entry_form)) {
								// 先填写表格
//								Log.i("entry_form", "entry_form:" + entry_form);

								// 获取用户缓存信息
								getUserExtendInfo();

							} else {
								// 直接报名
								Invite_handle(eventid, "accept");

							}

						} else {
							// 付费活动
							if ("已结束".equals(tv_status)) {
								// 活动已结束
								ToastUtil.show(EventsResultActivity.this,
										"对不起, 活动已结束!");
								return;
							}

							Builder builder = new Builder(
									EventsResultActivity.this);
							final AlertDialog dialog = builder.create();
							View dialogview = View.inflate(
									EventsResultActivity.this,
									R.layout.dialog_pay_money, null);

							// TextView tv_money_total = (TextView)
							// dialogview.findViewById(R.id.tv_money_total);
							TextView tv_money_pay = (TextView) dialogview
									.findViewById(R.id.tv_money_pay);
							final CheckBox cb_sure = (CheckBox) dialogview
									.findViewById(R.id.cb_sure);
							final Button bt_pay = (Button) dialogview
									.findViewById(R.id.bt_pay);
							tv_money_pay
									.setText(df.format(event.getFees() / 100.0)
											+ " 元");

							dialog.show();
							dialog.getWindow().setContentView(dialogview);

							dialog.getWindow().setGravity(Gravity.BOTTOM);

							dialog.getWindow().getDecorView()
									.setPadding(0, 0, 0, 0);
							WindowManager.LayoutParams lp = dialog.getWindow()
									.getAttributes();
							lp.width = getWindowManager().getDefaultDisplay()
									.getWidth();
							lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
							dialog.getWindow().setAttributes(lp);

							bt_pay.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if (cb_sure.isChecked()) {
										// 支付...
										// 获取预支付 id
										getPrepayId();
										dialog.dismiss();

									} else {
										ToastUtil.show(
												EventsResultActivity.this,
												"请先勾选同意支付报名费用");
									}

								}
							});

						}

					}
				}
			}
			break;

		// 底部状态栏, 右侧: 发表动态 或者 下次再说(根据不同状态)
		case R.id.iv_share_bottom:
			if (accept_status.equals("notaccept")
					|| accept_status.equals("invite")) {
				// 拒绝操作
				Invite_handlestatus = "decline";
				Invite_handle(eventid, "decline");
			} else if (accept_status.equals("decline")) {
				// 以婉拒状态下不做其他操作
			} else {
				Intent intent_w = new Intent(EventsResultActivity.this,
						WriteMomentsActivity.class);
				intent_w.putExtra("type", Constants.SPORTTYPE_EVENT);
				startActivityForResult(intent_w, writeMomentRequestCode);
			}
			break;

		// 评论 提交按钮
		case R.id.bt_commit:
			if (et_moment.getText().toString().trim().isEmpty()) {
				ToastUtil.show(EventsResultActivity.this, "评论内容不能为空");
				return;
			}
			// 收起软键盘
			KeyBoardUtils.closeKeybord(et_moment, EventsResultActivity.this);
			ll_moment.setVisibility(View.GONE);
			ll_vote.setVisibility(View.VISIBLE);
			// 评论
			postComentToNet();
			// 清除 et_moment 残留垃圾, 以免再次打开后出现上次评论内容
			et_moment.setText("");
			break;

		// 同步
		case R.id.iv_synchronize:
			updategdactivityNet(localdata);
			break;

		// 加载失败, 重新加载
		case R.id.rl_error:
			rl_error.setVisibility(View.GONE);
			getEventsInfoInNet(eventid);
			break;
		// 加载失败, 重新加载
		case R.id.rl_empty:
			// ?
			break;

		case R.id.tv_finish:
			// 报名者填写项 完成
			// TODO...
			final Map<String, String> comentParams = new HashMap<String, String>();
			if (entry_form.contains("姓名")) {
				String name = et_name.getText().toString();
				if (TextUtils.isEmpty(name)) {
					ToastUtil.show(EventsResultActivity.this, "姓名 不能为空!");
//					Log.i("entry_form", "姓名 不能为空!");
					return;
				} else {
					comentParams.put("姓名", name);
				}
			}
			if (entry_form.contains("手机")) {
				String mobile = et_mobile.getText().toString();
				if (TextUtils.isEmpty(mobile)) {
					ToastUtil.show(EventsResultActivity.this, "手机号 不能为空!");
//					Log.i("entry_form", "手机号 不能为空!");
					return;
				} else if (!TextUtils.isMobileNO(mobile)) {
					ToastUtil.show(EventsResultActivity.this, "手机号 格式不正确!");
					return;
				} else {
					comentParams.put("手机", mobile);
				}
			}
			if (entry_form.contains("部门")) {
				String department = et_department.getText().toString();
				if (TextUtils.isEmpty(department)) {
					ToastUtil.show(EventsResultActivity.this, "部门 不能为空!");
					return;
				} else {
					comentParams.put("部门", department);
				}
			}
			if (entry_form.contains("工号")) {
				String number = et_number.getText().toString();
				if (TextUtils.isEmpty(number)) {
					ToastUtil.show(EventsResultActivity.this, "工号 不能为空!");
					return;
				} else {
					comentParams.put("工号", number);
				}
			}
			if (entry_form.contains("公司")) {
				String company = et_company.getText().toString();
				if (TextUtils.isEmpty(company)) {
					ToastUtil.show(EventsResultActivity.this, "公司 不能为空!");
					return;
				} else {
					comentParams.put("公司", company);
				}
			}
			if (entry_form.contains("邮箱")) {
				String email = et_email.getText().toString();
				if (TextUtils.isEmpty(email)) {
					ToastUtil.show(EventsResultActivity.this, "邮箱 不能为空!");
					return;
				} else if (!TextUtils.isEmail(email)) {
					ToastUtil.show(EventsResultActivity.this, "邮箱 格式不正确!");
					return;
				} else {
					comentParams.put("邮箱", email);
				}
			}
			if (entry_form.contains("备注")) {
				String remarks = et_remarks.getText().toString();
				if (TextUtils.isEmpty(remarks)) {
					ToastUtil.show(EventsResultActivity.this, "备注 不能为空!");
					return;
				} else {
					comentParams.put("备注", remarks);
				}
			}

			dismissPopWindow();

			// 提交
			rl_loading.setVisibility(View.VISIBLE);
			OxygenApplication.cachedThreadPool.execute(new Runnable() {

				public void run() {
					HttpUtil.Post(UrlConstants.EVENTS_ENTRY_FORM + eventid
							+ ".json", handler, NET_ENTRY_FORM, comentParams);
				}
			});

			break;

		default:
			break;
		}

	}

	/**
	 * 弹出报名者填写项
	 * 
	 * @param form
	 */
	private void showEntryForm(String form) {
//		Log.i("entry_form", "show ing..." + form);
		// 填写信息
		View popView = getLayoutInflater().inflate(
				R.layout.pop_eventdetai_entry_form, null, false);

		RelativeLayout rl_name = (RelativeLayout) popView
				.findViewById(R.id.rl_name);
		RelativeLayout rl_mobile = (RelativeLayout) popView
				.findViewById(R.id.rl_mobile);
		RelativeLayout rl_department = (RelativeLayout) popView
				.findViewById(R.id.rl_department);
		RelativeLayout rl_number = (RelativeLayout) popView
				.findViewById(R.id.rl_number);
		RelativeLayout rl_company = (RelativeLayout) popView
				.findViewById(R.id.rl_company);
		RelativeLayout rl_email = (RelativeLayout) popView
				.findViewById(R.id.rl_email);
		RelativeLayout rl_remarks = (RelativeLayout) popView
				.findViewById(R.id.rl_remarks);

		et_name = (EditText) popView.findViewById(R.id.et_name);
		et_mobile = (EditText) popView.findViewById(R.id.et_mobile);
		et_department = (EditText) popView.findViewById(R.id.et_department);
		et_number = (EditText) popView.findViewById(R.id.et_number);
		et_company = (EditText) popView.findViewById(R.id.et_company);
		et_email = (EditText) popView.findViewById(R.id.et_email);
		et_remarks = (EditText) popView.findViewById(R.id.et_remarks);

		tv_finish = (TextView) popView.findViewById(R.id.tv_finish);

		tv_finish.setOnClickListener(this);

		if (form.contains("姓名")) {
			rl_name.setVisibility(View.VISIBLE);
			String value = map.get("姓名");
			if (null != value) {
				et_name.setText(value);
			}
		}
		if (form.contains("手机")) {
			rl_mobile.setVisibility(View.VISIBLE);
			String value = map.get("手机");
			if (null != value) {
				et_mobile.setText(value);
			}
		}
		if (form.contains("部门")) {
			rl_department.setVisibility(View.VISIBLE);
			String value = map.get("部门");
			if (null != value) {
				et_department.setText(value);
			}
		}
		if (form.contains("工号")) {
			rl_number.setVisibility(View.VISIBLE);
			String value = map.get("工号");
			if (null != value) {
				et_number.setText(value);
			}
		}
		if (form.contains("公司")) {
			rl_company.setVisibility(View.VISIBLE);
			String value = map.get("公司");
			if (null != value) {
				et_company.setText(value);
			}
		}
		if (form.contains("邮箱")) {
			rl_email.setVisibility(View.VISIBLE);
			String value = map.get("邮箱");
			if (null != value) {
				et_email.setText(value);
			}
		}
		if (form.contains("备注")) {
			rl_remarks.setVisibility(View.VISIBLE);
			String value = map.get("备注");
			if (null != value) {
				et_remarks.setText(value);
			}
		}

		// 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
		popupWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.AnimationFade);

		// 点击其他地方消失
		popView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (isFree) {
					// 付费活动不能消失
					dismissPopWindow();
				}
				return false;
			}
		});
		// /在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);

	}

	/**
	 * 获取预支付 id
	 */
	private void getPrepayId() {
		rl_loading.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {

			public void run() {
				HttpUtil.Get(UrlConstants.EVENTS_PRE_PAY + eventid + ".json",
						handler, NET_EVENTS_PRE_PAY);
			}
		});

	}

	/**
	 * 签到
	 */
	private void checkIn() {
		Intent checkinIntent = new Intent(EventsResultActivity.this,
				CheckInActivity.class);
		checkinIntent.putExtra("eventid", eventid);
		checkinIntent.putExtra("latitude", latitude);
		checkinIntent.putExtra("longitude", longitude);
		checkinIntent.putExtra("address", address);
		startActivityForResult(checkinIntent, checkInRequestCode);
	}

	/**
	 * 对coment进行评论
	 */
	private void postComentToNet() {
		rl_loading.setVisibility(View.VISIBLE);
		final Map<String, String> comentParams = new HashMap<String, String>();
		String content = et_moment.getText().toString().trim();
		comentParams.put("target_id", (String) UserInfoUtils.getUserInfo(this,
				Constants.SET_COMENT_ID, ""));
		comentParams.put("content", content);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {

			public void run() {
				HttpUtil.Post(UrlConstants.MOMENT_CREATE_GET, handler,
						POST_COMENT, comentParams);
			}
		});

	}

	/**
	 * 确认退出框
	 * 
	 * @param content
	 */
	private void showDialog(Context content, String title, final int type) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(content);
		dialog.setMessage(title)
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();// 取消弹出框
						// 取消活动
						if (type == NET_CANCELEVENT) {
							cancelEvent(eventid);
						} else if (type == NET_ACCEPT_DECLINE_EXIT) {
							// 退出活动
							Invite_handlestatus = "exit";
							Invite_handle(eventid, "decline");
						}
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();// 取消弹出框
					}
				}).create().show();
	}

	/**
	 * 获取服务器events信息
	 */
	private void getEventsInfoInNet(final int eventsid) {
		rl_loading.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {

			public void run() {
				HttpUtil.Get(
						UrlConstants.EVENTS_EVENTSINFO_GET
								+ eventsid
								+ ".json?accept_count=1&accept_list=1&ranking=1&performance=1&moments=1&bpp=1",
						handler, NET_SHOWEVENTINFO);
			}
		});

	}

	/**
	 * 支付成功
	 * 
	 * @param msg
	 */
	public void onEventMainThread(WeChatPay msg) {
		isFree = false;
		// 刚报完名
		Flag = true;

		// 报名
		Invite_handle(eventid, "accept");

	}

	/**
	 * 处理接受/拒绝邀请(推出活动) <event_id: 活动ID，必选> <status:
	 * 接受或拒绝(status＝accept｜decline), 必选>
	 * 
	 * @param eventsid
	 */
	private void Invite_handle(final int eventsid, String status) {
		rl_loading.setVisibility(View.VISIBLE);
		final Map<String, String> params = new HashMap<String, String>();
		params.put("event_id", eventsid + "");
		params.put("status", status);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {

			public void run() {
				HttpUtil.Post(UrlConstants.EVENTS_INVITE_HANDLE_POST, handler,
						NET_ACCEPT_DECLINE_EXIT, params);
			}
		});

	}

	/**
	 * 取消活动
	 * 
	 * @param eventsid
	 */
	private void cancelEvent(final int eventsid) {
		rl_loading.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {

			public void run() {
				HttpUtil.Get(UrlConstants.EVENTS_CANCELEVENT_GET + eventsid
						+ ".json", handler, NET_CANCELEVENT);
			}
		});
	}

	/**
	 * 更新本地缓存成绩
	 */
	private void updategdactivityNet(final GDAcvitity gavt) {
		// POST 参数
		final Map<String, String> params = new HashMap<String, String>();
		params.put("sport", gavt.getsport_eng() + "");
		params.put("distance", gavt.getDistance() + "");
		params.put("duration", gavt.getDuration() + "");
		params.put("latitude", gavt.getlatitude() + "");
		params.put("longitude", gavt.getlongitude() + "");
		params.put("address", gavt.getaddresss());
		params.put("start_time", gavt.getStart_time());
		params.put("end_time", gavt.getEnd_time());
		params.put("altitude", gavt.getAltitude() + "");
		params.put("route", gavt.getRoute() == null ? null : gavt.getRoute()
				.toString());
		params.put("status", gavt.getStatus());
		params.put("manual", "no");
		if (event.getPerformance() != null) {
			rl_loading.setVisibility(View.VISIBLE);
			OxygenApplication.cachedThreadPool.execute(new Runnable() {

				public void run() {
					HttpUtil.Post(UrlConstants.ACTIVITIES_UPDATE_URL_POST
							+ event.getPerformance().get_id() + ".json",
							handler, NET_CREATEDACTIVITIES, params);
				}
			});
		} else {
			ToastUtil.show(EventsResultActivity.this, "异常数据");
		}

	}

	/**
	 * handler 接受消息, 更新 UI
	 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {

			case NET_SHOWEVENTINFO:
				// 显示页面信息
				String strObject = (String) msg.obj;
				if (!TextUtils.isEmpty(strObject)) {
					try {
						JSONObject jsoninfos = new JSONObject(strObject);
						if (jsoninfos.getInt("status") == 200) {

							String str_data = jsoninfos.getString("data");
							jsonobject_userinfo = jsoninfos
									.getJSONObject("users_info");
							current_user = UsersConstruct.ToUser(jsoninfos
									.getJSONObject("current_user"));
							if (!TextUtils.isEmpty(str_data)) {
								JSONObject json_date = new JSONObject(str_data);
								Updateui(json_date);

								// 弹出自动匹配的提示
								if (GDUtil.getGlobal(EventsResultActivity.this,
										"automatch")) {
									GDUtil.setGlobal(EventsResultActivity.this,
											"automatch", false);
									ToastUtil.show(EventsResultActivity.this,
											"已自动关联到参加的活动");
								}
							}

							rl_loading.setVisibility(View.GONE);
							if (isFromCreatedPlanActivity && group != null) {
								ToastUtil.show(EventsResultActivity.this,
										"活动邀请已经发送给所有成员");
								isFromCreatedPlanActivity = false;
							}

						} else {
							// 服务器返回异常
							// 没有有效数据
							rl_loading.setVisibility(View.GONE);
							rl_error.setVisibility(View.GONE);
							String text = jsoninfos.getString("msg");
							if (!TextUtils.isEmail(text)) {
								tv_empty.setText(text);
							}
							rl_empty.setFocusable(true);
							rl_empty.setOnTouchListener(new OnTouchListener() {

								@Override
								public boolean onTouch(View v, MotionEvent event) {
									// TODO Auto-generated method stub
									return true;
								}
							});
							rl_empty.setVisibility(View.VISIBLE);

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					// 加载失败
					// 网络出错
					rl_loading.setVisibility(View.GONE);
					rl_error.setVisibility(View.VISIBLE);
				}
				break;

			case NET_EVENTS_PRE_PAY:
				// 显示页面信息
				// TODO...
				if (msg.obj == null) {
					ToastUtil.show(EventsResultActivity.this, "网络连接不可用，请稍后重试");
				} else {
					String str = (String) msg.obj;
					try {
						JSONObject jsonObject = new JSONObject(str);
//						Log.i("pay", str);

						if (jsonObject.getInt("status") == 200) {

							JSONObject data = jsonObject.getJSONObject("data");
							PayReq request = new PayReq();

							if (data.getString("appid") != null) {
								request.appId = data.getString("appid");
							}
							if (data.getString("mch_id") != null) {
								request.partnerId = data.getString("mch_id");
							}
							if (data.getString("prepay_id") != null) {
								request.prepayId = data.getString("prepay_id");
							}
							if (data.getString("package") != null) {
								// request.packageValue =
								// data.getString("package");
								request.packageValue = "Sign=WXPay";
							}
							if (data.getString("nonce_str") != null) {
								request.nonceStr = data.getString("nonce_str");
							}
							if (data.getString("timestamp") != null) {
								request.timeStamp = data.getString("timestamp");
							}
							if (data.getString("sign") != null) {
								request.sign = data.getString("sign");
							}

							msgApi.registerApp(Constants.WEIXIN_APPID);
							msgApi.sendReq(request);

						} else {
							ToastUtil.show(EventsResultActivity.this,
									jsonObject.getString("msg"));
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				rl_loading.setVisibility(View.GONE);
				break;

			case NET_UPDATEMOMENTS:
				// 更新动态
				String newStrObject = (String) msg.obj;
				if (newStrObject != null && newStrObject.length() > 10) {
					try {
						JSONObject jsoninfos = new JSONObject(newStrObject);
						if (jsoninfos.getInt("status") == 200) {
							//
							String str_data = jsoninfos.getString("data");
							jsonobject_userinfo = jsoninfos
									.getJSONObject("users_info");
							current_user = UsersConstruct.ToUser(jsoninfos
									.getJSONObject("current_user"));
							if (str_data != null && str_data.length() > 0) {
								JSONObject json_date = new JSONObject(str_data);

								Event event_new = EventConstruct
										.Toevent(json_date);
								event_new.status = event.status;
								event = event_new;
							}

							// 更新动态
							UpdateMomentsUi();

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				rl_loading.setVisibility(View.GONE);
				break;

			case NET_CANCELEVENT:
				// 取消活动
				String strObject_cancel = (String) msg.obj;
				if (strObject_cancel != null && strObject_cancel.length() > 10) {
					try {
						JSONObject jsoninfos = new JSONObject(strObject_cancel);
						if (jsoninfos.getInt("status") == 200) {
							GDUtil.setGlobal(EventsResultActivity.this,
									"timeline_is_rerfresh", true);
							reback();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				rl_loading.setVisibility(View.GONE);
				break;

			case NET_ACCEPT_DECLINE_EXIT:
				// 处理接受/拒绝邀请 (退出活动)
				if (msg.obj == null) {
					ToastUtil.show(EventsResultActivity.this, "网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonobeObject = (JSONObject) msg.obj;
						if (jsonobeObject.getInt("status") == 200) {

							if ("exit".equals(Invite_handlestatus)) {
								GDUtil.setGlobal(EventsResultActivity.this,
										"timeline_is_rerfresh", true);
								ToastUtil.show(EventsResultActivity.this,
										"成功退出活动!");
								reback();
							} else if ("accept".equals(Invite_handlestatus)
									|| "decline".equals(Invite_handlestatus)) {

								// 刷新界面
								getEventsInfoInNet(eventid);
								GDUtil.setGlobal(EventsResultActivity.this,
										"timeline_is_rerfresh", true);

								if ("accept".equals(Invite_handlestatus)) {

									if (!jsonobeObject.isNull("user_action_id")) {
										// 弹出金币
										int userActionId = jsonobeObject
												.getInt("user_action_id");
										getToastContent(userActionId);
									} else {
										ToastUtil.show(
												EventsResultActivity.this,
												"报名成功 !");
									}

									if (!isFree) {
										// 付费活动
										getUserExtendInfo();
									}

								} else {
									ToastUtil.show(EventsResultActivity.this,
											"已拒绝参与该活动!");
								}

								// getEventsInfoInNet(eventid);
								// OxygenApplication.timeline_is_rerfresh =
								// true;

							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				rl_loading.setVisibility(View.GONE);
				break;

			case POST_COMENT:
				// 发表评论
				if (msg.obj == null) {
					ToastUtil.show(EventsResultActivity.this, "网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonobeObject = (JSONObject) msg.obj;
						if (jsonobeObject.getInt("status") == 200) {
							ToastUtil.show(EventsResultActivity.this, "评论成功");
							JSONObject JSONData = new JSONObject(
									jsonobeObject.getString("data"));
							int created_by = JSONData.getInt("created_by");
							String content = JSONData.getString("content");
							Comment comment = new Comment();
							comment.setContent(content);
							comment.setCreated_by(created_by);
							if (moments != null) {
								// 添加评论
								for (int i = 0; i < moments.size(); i++) {
									if (moments.get(i).getId() == Integer
											.parseInt((String) UserInfoUtils
													.getUserInfo(
															EventsResultActivity.this
																	.getApplicationContext(),
															Constants.SET_COMENT_ID,
															"1"))) {
										moments.get(i).comments.add(comment);
									}
								}
							}

							// 更新动态
							UpdateMomentsUi();
						} else {
							ToastUtil.show(EventsResultActivity.this, "评论失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				rl_loading.setVisibility(View.GONE);
				break;

			case NET_CREATEDACTIVITIES:
				// ??
				// TODO...
				if (msg.obj == null) {
					ToastUtil.show(
							EventsResultActivity.this,
							EventsResultActivity.this.getResources().getString(
									R.string.errcode_wx));

				} else {
					JSONObject jsonobeObject = (JSONObject) msg.obj;
					try {
						if (jsonobeObject.getInt("status") == 200) {
							dmr.delsport(localdata.getLocal());
							getEventsInfoInNet(eventid);
							rl_synchronize.setVisibility(View.GONE);
							GDUtil.setGlobal(EventsResultActivity.this,
									"timeline_is_rerfresh", true);
						} else {
							ToastUtil.show(EventsResultActivity.this, "数据上传失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				rl_loading.setVisibility(View.GONE);
				break;

			case NET_COIN_CNT:
				// 弹出金币提示
				if (msg.obj == null) {
					ToastUtil.show(EventsResultActivity.this, "网络连接不可用，请稍后重试");
				} else {

					try {
						JSONObject jsonobeObject = new JSONObject(
								(String) msg.obj);
						if (jsonobeObject.getInt("status") == 200) {
							JSONObject data = jsonobeObject
									.getJSONObject("data");
							String content = data.getString("alert");
							int coins = data.getInt("coins");
							ToastUtil.showCoin(EventsResultActivity.this,
									content + " +" + coins + " 金币!");

						} else {
							ToastUtil.show(EventsResultActivity.this,
									"网络连接不可用，请稍后重试");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
				break;

			case NET_ENTRY_FORM:
				// 报名者填写项
				rl_loading.setVisibility(View.GONE);
				if (msg.obj == null) {
					ToastUtil.show(EventsResultActivity.this, "网络连接不可用，请稍后重试");
				} else {

					try {
						JSONObject jsonobeObject = (JSONObject) msg.obj;
						if (jsonobeObject.getInt("status") == 200) {

							if (isFree) {
								// 免费活动, 报名
								Invite_handle(eventid, "accept");
							}

						} else {
							ToastUtil.show(EventsResultActivity.this,
									"网络连接不可用，请稍后重试");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
				break;

			case NET_USER_EXTEND_INFO:
				// 获取用户扩展信息
				rl_loading.setVisibility(View.GONE);
				if (msg.obj == null) {
					ToastUtil.show(EventsResultActivity.this, "网络连接不可用，请稍后重试");
				} else {
//					Log.i("entry_form", "msg.obj" + msg.obj.toString());
					try {
						JSONObject jsonobeObject = new JSONObject(
								(String) msg.obj);
						if (jsonobeObject.getInt("status") == 200) {

							JSONObject data = jsonobeObject
									.getJSONObject("data");

							entrys = entry_form.split(",");

							map = new HashMap<String, String>();

							for (String key : entrys) {

								String value = data.getString(key);

								if (!TextUtils.isEmpty(value)) {
									map.put(key, data.getString(key));

								}
							}

							// 弹出报名者填写项
							showEntryForm(entry_form);

						} else {
							ToastUtil.show(EventsResultActivity.this,
									"网络连接不可用，请稍后重试");
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

	/**
	 * 获取用户扩展信息
	 */
	private void getUserExtendInfo() {
		rl_loading.setVisibility(View.VISIBLE);
		String params = "?entry_form=" + entry_form;
//		Log.i("entry_form", "url=" + UrlConstants.USERS_USER_EXTEND_INFO
//				+ params);

		OxygenApplication.cachedThreadPool.execute(new Runnable() {

			public void run() {
				try {
					HttpUtil.Get(
							UrlConstants.USERS_USER_EXTEND_INFO
									+ "?entry_form="
									+ URLEncoder.encode(entry_form, "utf-8"),
							handler, NET_USER_EXTEND_INFO);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 获取弹金币吐司内容
	 */
	private void getToastContent(final int user_action_id) {

		OxygenApplication.cachedThreadPool.execute(new Runnable() {

			public void run() {
				HttpUtil.Get(UrlConstants.OPERATIONS_ACTION_REWARD
						+ user_action_id + ".json", handler, NET_COIN_CNT);
			}
		});

	}

	private void share2weixin(int flag) {
		String weixinintro = null;
		// 用户分享时的评论内容，可由用户输入
		if (intro.contains(">")&&intro.contains("<")) {
			weixinintro = "一起运动更快乐,约么?";
		} else {
			weixinintro = intro;
		}
		WxUtil.share2weixin(flag, EventsResultActivity.this, Shareurl, "来报名吧-"
				+ title, "icon_index_" + event.sport_eng + "_s", weixinintro);
	}

	private void share2qq() {
		Bundle params = new Bundle();
		// 分享的标题
		params.putString(QQShare.SHARE_TO_QQ_TITLE, "来报名吧-" + title);
		// 分享的图片URL
		String imageUrl = Constants.qiuniushare + "icon_index_"
				+ event.sport_eng + "_s.jpg";
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);

		// 用户分享时的评论内容，可由用户输入
		if (intro.contains(">")&&intro.contains("<")) {
			params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "一起运动更快乐,约么?");
		} else {
			params.putString(QQShare.SHARE_TO_QQ_SUMMARY, intro);
		}
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Shareurl);
		QQUtils.doShareToQQ(params, this);
	}

	/**
	 * 创建PopupWindow
	 */
	protected void initPopuptWindow(View v) {
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(
				R.layout.pop_eventdetail, null, false);
		initPopViews(popupWindow_view);
		initPopViewEvENT();

		if (v == btn_publishmood) {
			checkin.setVisibility(View.GONE);
			update_evnent.setVisibility(View.GONE);
			cancel_evnent.setVisibility(View.GONE);
			exit_evnent.setVisibility(View.GONE);
			watch_QR_cord.setVisibility(View.GONE);
			tv_lab_share.setVisibility(View.VISIBLE);
			ll_share.setVisibility(View.VISIBLE);
		} else {
			// 不出现 补签到 按钮的时机
			if ((event.getAddress() == null || "待定".equals(event.getAddress()))
					|| !isLimitTime()) {
				// 不出现补签到按钮(活动地点为"待定", 或者不在正负一小时)
				checkin.setVisibility(View.GONE);
			}

			if (role != null && role.equals("organizer")) {
				exit_evnent.setVisibility(View.GONE);
			} else {
				update_evnent.setVisibility(View.GONE);
				cancel_evnent.setVisibility(View.GONE);
			}
		}

		// 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		// 点击其他地方消失
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dismissPopWindow();
				return false;
			}
		});
	}

	/**
	 * 关闭 popupwindow
	 */
	protected void dismissPopWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// activity 回传数据
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == writeMomentRequestCode
				&& resultCode == Activity.RESULT_OK) {

			// 发表动态成功, 刷新页面, 更新动态
			rl_loading.setVisibility(View.VISIBLE);
			OxygenApplication.cachedThreadPool.execute(new Runnable() {

				public void run() {
					HttpUtil.Get(
							UrlConstants.EVENTS_EVENTSINFO_GET
									+ eventid
									+ ".json?accept_count=1&accept_list=1&ranking=1&performance=1&moments=1&bpp=1",
							handler, NET_UPDATEMOMENTS);
				}
			});

		}

		if (requestCode == checkInRequestCode
				&& resultCode == Activity.RESULT_OK) {
			// 获取签到状态
			isCheckIn = data.getBooleanExtra("isCheckIn", false);
			if (isCheckIn) {
				iv_checked_status.setVisibility(View.VISIBLE);
			}

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (vp_group_photo.isShown()) {
			vp_group_photo.setVisibility(View.GONE);
			return false;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 回退
			reback();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 回退, 刷新时间轴
	 */
	private void reback() {
		if (GDUtil.getGlobal(EventsResultActivity.this, "timeline_is_rerfresh")) {
			Intent intent = new Intent(EventsResultActivity.this,
					MenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			GDUtil.setGlobal(EventsResultActivity.this, "timeline_is_rerfresh",
					false);
		}
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 反注册EventBus
		EventBus.getDefault().unregister(this);
		dismissPopWindow();
		if (dmr != null) {
			dmr.closeDB();
		}
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		// 当传感器值改变的时候调用
		int sensorType = sensorEvent.sensor.getType();
		// values[0]:X轴，values[1]：Y轴，values[2]：Z轴
		float[] values = sensorEvent.values;

		// 传感器类型为 加速度传感器
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
			// 没有签到(摇一摇浮窗可见)
			if (View.VISIBLE == rl_shake_check_in.getVisibility()) {
				/*
				 * 因为一般正常情况下，任意轴数值最大就在9.8~10之间，只有在你突然摇动手机的时候，瞬时加速度才会突然增大或减少。
				 * 所以，经过实际测试，只需监听任一轴的加速度大于14的时候，改变你需要的设置就OK了~~~
				 */
				if ((Math.abs(values[0]) > 13 || Math.abs(values[1]) > 13 || Math
						.abs(values[2]) > 13)) {

					// 摇动手机后，再伴随震动提示~~
					mVibrator.vibrate(500);

					Intent checkinIntent = new Intent(
							EventsResultActivity.this, CheckInActivity.class);
					checkinIntent.putExtra("eventid", eventid);
					checkinIntent.putExtra("latitude", latitude);
					checkinIntent.putExtra("longitude", longitude);
					checkinIntent.putExtra("address", address);
					startActivityForResult(checkinIntent, checkInRequestCode);
					// 关闭摇一摇浮窗
					rl_shake_check_in.setVisibility(View.GONE);

				}
			}

		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// 当传感器精度改变时回调该方法，Do nothing.
	}

}
