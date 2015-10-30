package com.oxygen.www.module.sport.activity;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Event;
import com.oxygen.www.enties.Group;
import com.oxygen.www.module.sport.eventbus_enties.MoreMore;
import com.oxygen.www.module.sport.writemoment.util.FileUtils;
import com.oxygen.www.utils.AppManager;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.TextUtils;
import com.oxygen.www.utils.ToastUtil;
import com.qiniu.android.http.ResponseInfo;
import de.greenrobot.event.EventBus;

/**
 * 活动创建页
 * 
 * @author 张坤
 * 
 */
public class CreatedPlanActivity extends BaseActivity implements
		OnClickListener {

	private SharedPreferences sp;
	private boolean isFromTeamDetailActivity = false;

	/**
	 * 新建活动/ 更改活动信息
	 */
	public static final int NET_CREATE_EVENT_UPDATE = 1;
	/**
	 * 获取上传图片的 token
	 */
	public static final int NET_GETTOKEN = 2;
	/**
	 * 上传图片至七牛服务器
	 */
	public static final int NET_UPLOAP = 3;
	/**
	 * 请求失败再接着请求一次 (完成按钮中..)
	 */
	private boolean fristrequest = true;
	/**
	 * 请求码 打开活动时间设置页面
	 */
	public static final int requestCode_time = 10;
	/**
	 * 请求码 打开活动地点设置页面
	 */
	public static final int requestCode_address = 20;
	/**
	 * 请求码 打开活动设置更多页面
	 */
	public static final int requestCode_more = 30;
	/**
	 * 请求码 打开设置富文本页面
	 */
	public static final int requestRich = 40;

	private DecimalFormat df2 = new DecimalFormat("#0.00");

	private ImageView iv_back, iv_sport;
	private RelativeLayout rl_sport_time, rl_sport_address2;
	private RelativeLayout rl_settingmore, rl_rich_tag, rl_rich;
	private TextView tv_sport_time, tv_created_post, tv_sport_address,
			tv_takephoto, tv_rich, tv_rich_tag;
	private EditText et_sport_title, et_sport_intro, et_money;
	private ProgressBar progressbar;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	Calendar a = Calendar.getInstance();
	private final int TAKE_PICTURE = 0x000001;
	private final int TAKE_TYPE = 0x000002;
	private final int CHOOSE_PICTURE = 0x000004;

	/**
	 * 保存顶部墙纸(拍照/相册选择)
	 */
	private Bitmap photoBp;
	/**
	 * 顶部墙纸图片地址
	 */
	private String picurl = null;
	/**
	 * 活动分类: 默认类型为 running
	 */
	private String activities_sport = "running";
	/**
	 * 活动类型: 新建/修改
	 */
	private String type;
	/**
	 * 活动
	 */
	private Event event;
	/**
	 * 团队, 从团队详情页 发起团队活动入口 进入 即有此字段
	 */
	private Group group;
	/**
	 * 时间格式化器
	 */
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
	/**
	 * 活动时间
	 */
	private String time = df.format(new Date(
			System.currentTimeMillis() + 3600 * 1 * 1000))
			+ "~"
			+ df.format(new Date(System.currentTimeMillis() + 3600 * 3 * 1000));
	/**
	 * 活动地点
	 */
	private String address;
	/**
	 * 活动地点经纬度
	 */
	private double latitude = 0.0;
	/**
	 * 活动地点经纬度
	 */
	private double longitude = 0.0;
	/**
	 * 关联了的团队的Id(如果关联了团队)
	 */
	private String teamId = "";
	/**
	 * 关联团队的名称
	 */
	private String teamName = "";
	/**
	 * 隐私设置 popwindow
	 */
	private PopupWindow popupWindow;
	/**
	 * 隐私状态
	 */
	private String privacyStatus = "仅限邀请";
	/**
	 * 活动人数
	 */
	private int limitation;
	/**
	 * 更多报名者填写项
	 */
	private String moremore = "0000000";
	/**
	 * 更多报名者填写项
	 */
	private String entry_form;

	/**
	 * 富文本
	 */
	private String html;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sport_createdplan);
		initViews();
		initViewsEvent();
		initValues();

	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_created_post = (TextView) findViewById(R.id.tv_created_post);

		iv_top = (ImageView) findViewById(R.id.iv_sportpic);
		tv_takephoto = (TextView) findViewById(R.id.tv_takephoto);

		iv_sport = (ImageView) findViewById(R.id.iv_sport);
		et_sport_title = (EditText) findViewById(R.id.et_sport_title);
		et_sport_intro = (EditText) findViewById(R.id.et_sport_intro);
		
		rl_rich_tag = (RelativeLayout) findViewById(R.id.rl_rich_tag);
		rl_rich = (RelativeLayout) findViewById(R.id.rl_rich);
		tv_rich_tag = (TextView) findViewById(R.id.tv_rich_tag);
		tv_rich = (TextView) findViewById(R.id.tv_rich);

		rl_sport_time = (RelativeLayout) findViewById(R.id.rl_sport_time);
		tv_sport_time = (TextView) findViewById(R.id.tv_sport_time);
		rl_sport_address2 = (RelativeLayout) findViewById(R.id.rl_sport_address2);
		tv_sport_address = (TextView) findViewById(R.id.tv_sport_address);
		et_money = (EditText) findViewById(R.id.et_money);
		rl_settingmore = (RelativeLayout) findViewById(R.id.rl_settingmore);

		progressbar = (ProgressBar) findViewById(R.id.progressbar);

	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		iv_sport.setOnClickListener(this);
		iv_top.setOnClickListener(this);
		tv_takephoto.setOnClickListener(this);

		rl_rich.setOnClickListener(this);

		et_sport_title.setOnClickListener(this);
		et_sport_title.setOnFocusChangeListener(mOnFocusChangeListener);

		et_sport_intro.setOnFocusChangeListener(mOnFocusChangeListener);

		rl_sport_time.setOnClickListener(this);
		tv_created_post.setOnClickListener(this);
		tv_rich_tag.setOnClickListener(this);

		rl_sport_address2.setOnClickListener(this);

		rl_settingmore.setOnClickListener(this);

	}

	private void initValues() {
		sp = getSharedPreferences("com.oxygen.www_", MODE_MULTI_PROCESS);
		EventBus.getDefault().register(this);
		// 团队详情页 发起团队活动 入口
		group = (Group) getIntent().getSerializableExtra("group");
		if (group != null) {
			isFromTeamDetailActivity = true;
			teamId = group.getId() + "";
			teamName = group.getName();
			// et_relate_team_name.setText(teamName);
		}

		type = getIntent().getStringExtra("type");
		if (Constants.SPORTTYPE_CREATED.equals(type)) {
			// 新创建活动
			activities_sport = this.getIntent().getStringExtra("sport");
			String formatTime = formatTime(time.substring(0, 16),
					time.substring(17));
			tv_sport_time.setText(formatTime);

			// 顶部墙纸
			// iv_top.setImageDrawable(GDUtil.engSporttodrawable(
			// CreatedPlanActivity.this, "bg_eventdetail_" + activities_sport));

		} else if (Constants.SPORTTYPE_UPDATE.equals(type)) {
			// 更改活动信息
			event = (Event) getIntent().getSerializableExtra("event");
			activities_sport = event.getSport_eng();

			et_sport_title.setText(event.getTilte());
			
			String intro = event.getIntro();

			if (intro != null) {
				if (intro.contains("<") && intro.contains(">")) {
					html = intro;
					rl_rich_tag.setVisibility(View.VISIBLE);
					et_sport_intro.setVisibility(View.GONE);
					
				} else {
					rl_rich_tag.setVisibility(View.GONE);
					et_sport_intro.setVisibility(View.VISIBLE);
						
					et_sport_intro.setText(intro);
				}
				
			} else {
				rl_rich_tag.setVisibility(View.GONE);
				et_sport_intro.setVisibility(View.VISIBLE);
			}
			

			// 时间
			time = event.getStart_time().substring(0, 16) + "~"
					+ event.getEnd_time().substring(0, 16);
			tv_sport_time.setText(event.getStart_time().substring(5, 16) + "~"
					+ event.getEnd_time().subSequence(5, 16));

			// 地点, 经纬度
			tv_sport_address.setText(event.getAddress());
			latitude = event.getLatitude();
			longitude = event.getLongitude();

			// 付费设置
			if (event.getFees() != 0) {
				// 转换成 元
				et_money.setText(df2.format(event.getFees() / 100.0));
			}

			// 保存一份信息(必要)
			// 活动人数
			if (event.getLimitation() != 0) {
				limitation = event.getLimitation();
			}
			// 关联的团队
			if (!TextUtils.isEmpty(event.getGroup_id())) {
				Group group = event.getGroup();
				if (group != null) {
					teamName = group.getName();
					teamId = group.getId() + "";
				}
			}
			// 隐私设置
			privacyStatus = (event.getPrivacy() == 1 ? "仅限邀请" : "广场可见");
			// 报名者填写项
			if (!TextUtils.isEmpty(event.getEntry_form())) {
				entry_form = event.getEntry_form();

				String str = entry_form;
				StringBuilder sb = new StringBuilder();
				sb.append(str.contains("姓名") ? '1' : '0');
				sb.append(str.contains("手机") ? '1' : '0');
				sb.append(str.contains("部门") ? '1' : '0');
				sb.append(str.contains("工号") ? '1' : '0');
				sb.append(str.contains("公司") ? '1' : '0');
				sb.append(str.contains("邮箱") ? '1' : '0');
				sb.append(str.contains("备注") ? '1' : '0');
				moremore = sb.toString();

			}

			int i = getResources().getIdentifier(
					"bg_eventdetail_" + event.sport_eng, "drawable",
					getPackageName());
			// 如果有背景图则隐藏相机,显示背景图
			if (!TextUtils.isEmpty(event.getPic())) {
				ImageUtil.showImage2(
						(event.getPic() + Constants.qiniu_photo_find).trim(),
						iv_top, i);
				tv_takephoto.setVisibility(View.GONE);
			}
		}
		// 头部运动类型图片
		iv_sport.setImageDrawable(GDUtil.engSporttodrawable(
				CreatedPlanActivity.this, "icon_index_" + activities_sport));

	}
	
	/**
	 * 确定退出
	 */
	private void exit() {
		// 对话框
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		
		builder.setMessage("退出此次编辑?");
		builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   // User clicked OK button
		        	   dialog.dismiss();
		        	   finish();
		           }
		       });
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User cancelled the dialog
		        	   dialog.dismiss();
		           }
		       });

		AlertDialog dialog = builder.create();
		builder.show();
		
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_back:
			// 返回
			exit();
			break;

		case R.id.tv_created_post:
			// 完成 按钮
			tv_created_post.setEnabled(false);
			progressbar.setVisibility(View.VISIBLE);
			if (photoBp != null) {
				// 是否需要上传图片(更该页面顶部壁纸)
				getQiuniuToken();
			} else {
				if (Constants.SPORTTYPE_CREATED.equals(type)) {
					addsportPlanToNet();
				} else if (Constants.SPORTTYPE_UPDATE.equals(type)) {
					UpdatesportPlanToNet();
				}
			}
			break;

		case R.id.tv_takephoto:// 点击相机
			// 拍照按钮 pop 窗
			initPop();
			// 更换顶部墙纸
			ll_popup.startAnimation(AnimationUtils.loadAnimation(
					CreatedPlanActivity.this, R.anim.default_anim_in));
			pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.iv_sportpic:// 点击顶部图片
			// 拍照按钮 pop 窗
			initPop();
			// 更换顶部墙纸
			ll_popup.startAnimation(AnimationUtils.loadAnimation(
					CreatedPlanActivity.this, R.anim.default_anim_in));
			pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			break;

		case R.id.rl_sport_time:
			// 设置时间
			Intent intent = new Intent(this,
					CreatePlanSettingTimeActivity.class);
			intent.putExtra("sportTime", time);
			startActivityForResult(intent, requestCode_time);
			break;

		case R.id.rl_sport_address2:
			// 设置地址
			Intent intent_adress = new Intent(this,
					CreatePLanSettingAddressActivity.class);
			startActivityForResult(intent_adress, requestCode_address);
			break;

		case R.id.rl_settingmore:

			// 更多设置
			Intent intent_more = new Intent(this,
					CreatePlanSettingMoreActivity.class);
			intent_more.putExtra("limitation", limitation);
			intent_more.putExtra("teamId", teamId);
			intent_more.putExtra("teamName", teamName);
			intent_more.putExtra("privacyStatus", privacyStatus);
			intent_more.putExtra("moremore", moremore);

			if (isFromTeamDetailActivity) {
				// 团队详情页 发起的活动
				intent_more.putExtra("group", group);
			}
			startActivityForResult(intent_more, requestCode_more);
			break;

		case R.id.rl_rich:
			// 图文编辑
			Intent rich = new Intent(this, CreatePlanRichActivity.class);
			if (Constants.SPORTTYPE_UPDATE.equals(type)) {
				rich.putExtra("event_id", event.get_id());
			}
			rich.putExtra("htmldata", html);
			startActivityForResult(rich, requestRich);
			break;
			
		case R.id.tv_rich_tag:
			// 富文本
			Intent rich_tag = new Intent(this, CreatePlanRichActivity.class);
			if (Constants.SPORTTYPE_UPDATE.equals(type)) {
				rich_tag.putExtra("event_id", event.get_id());
			}
			rich_tag.putExtra("htmldata", html);
			startActivityForResult(rich_tag, requestRich);

			break;
			
		default:
			break;
		}

	}

	/**
	 * 初始化 pop 窗(拍照按钮)
	 * 
	 */
	private void initPop() {
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);
		pop = new PopupWindow(view,LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		// 整个popwindow
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		// 拍照
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		// 打开图库
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				// 相片类型
				// startActivityForResult(intent, CHOOSE_PICTURE);
				// if(android.os.Build.VERSION.SDK_INT >=
				// android.os.Build.VERSION_CODES.KITKAT){
				// startActivityForResult(intent,SELECT_PICTURE_KITKAT);
				// }else{
				startActivityForResult(intent, CHOOSE_PICTURE);
				// }
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		// 取消
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

	}

	/**
	 * 格式化时间(临时使用, 待抽取...)
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	private String formatTime(String startTime, String endTime) {
		// 格式化时间(临时使用, 待抽取到时间格式化类中...)
		String formatTime = time.substring(5, 17) + time.substring(22);

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
	 * 拍照
	 */
	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
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
		intent.putExtra("aspectX", 2);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 500);
		intent.putExtra("outputY", 250);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	@SuppressWarnings("deprecation")
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			photoBp = extras.getParcelable("data");
			if (photoBp != null) {
				Drawable drawable = new BitmapDrawable(photoBp);
				iv_top.setImageDrawable(drawable);
				tv_takephoto.setVisibility(View.GONE);
			}
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

	/**
	 * 创建活动, 提交数据
	 */
	private void addsportPlanToNet() {
		// POST 参数
		final Map<String, String> params = new HashMap<String, String>();
		params.put("sport", activities_sport);
		if (TextUtils.isEmpty(et_sport_title.getText())) {
			params.put(
					"title",
					GDUtil.engforchn(CreatedPlanActivity.this, "created_type_"
							+ activities_sport));
		} else {
			params.put("title", et_sport_title.getText().toString());
		}
		if (!TextUtils.isEmpty(et_sport_intro.getText())) {
			params.put("intro", et_sport_intro.getText().toString());
		}
		
		if (!TextUtils.isEmpty(html)) {
			params.put("intro", html);
		} else {
			if (!TextUtils.isEmpty(et_sport_intro.getText())) {
				params.put("intro", et_sport_intro.getText().toString());
			}
			
		}

		params.put("start_time", time.substring(0, 16) + ":00");
		params.put("end_time", time.substring(17) + ":00");
		if (!TextUtils.isEmpty(tv_sport_address.getText())) {
			params.put("address", tv_sport_address.getText().toString());
		}
		params.put("latitude", latitude + "");
		params.put("longitude", longitude + "");

		params.put("status", "open");
		params.put("pic", picurl);

		params.put("limitation", limitation + "");
		// 如果关联了团队 创建活动的时候添加一个字段
		if (!TextUtils.isEmpty(teamId)) {
			params.put("group_id", teamId);
		}
		params.put("privacy", privacyStatus.equals("仅限邀请") ? "1" : "0");
		params.put("entry_form", entry_form);

		if (TextUtils.isEmpty(et_money.getText().toString())) {
			params.put("pricing", "free");
		} else {
			params.put("pricing", "fixed");
			// 单位转换为 分
			params.put("fees",
					(Double.parseDouble(et_money.getText().toString()) * 100)
							+ "");

		}

		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				String URL = UrlConstants.EVENTS_CREATE_URL_POST;
				HttpUtil.Post(URL, handler, NET_CREATE_EVENT_UPDATE, params);
			}
		});

	}

	/**
	 * 更改活动信息
	 */
	private void UpdatesportPlanToNet() {
		// POST 参数
		final Map<String, String> params = new HashMap<String, String>();
		if (TextUtils.isEmpty(et_sport_title.getText())) {
			params.put("title", event.getSport_chn());
		} else {
			params.put("title", et_sport_title.getText().toString());
		}

		
		if (!TextUtils.isEmpty(html)) {
			params.put("intro", html);
		} else {
			if (!TextUtils.isEmpty(et_sport_intro.getText())) {
				params.put("intro", et_sport_intro.getText().toString());
			}
			
		}
		
		
		params.put("sport", activities_sport);
		if (!TextUtils.isEmpty(tv_sport_address.getText())) {
			params.put("address", tv_sport_address.getText().toString());
		}
		params.put("latitude", latitude + "");
		params.put("longitude", longitude + "");

		params.put("start_time", time.substring(0, 16) + ":00");
		params.put("end_time", time.substring(17) + ":00");

		params.put("status", "open");
		if (!TextUtils.isEmpty(picurl)) {
			params.put("pic", picurl);
		}

		params.put("limitation", limitation + "");
		// 如果关联了团队 创建活动的时候添加一个字段
		if (!TextUtils.isEmpty(teamId)) {
			params.put("group_id", teamId);
		}
		params.put("privacy", privacyStatus.equals("仅限邀请") ? "1" : "0");
		params.put("entry_form", entry_form);

		if (TextUtils.isEmpty(et_money.getText().toString())) {
			params.put("pricing", "free");
		} else {
			params.put("pricing", "fixed");
			params.put("fees",
					(Double.parseDouble(et_money.getText().toString()) * 100)
							+ "");

		}

		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				String URL = UrlConstants.EVENTS_UPDATE_URL_POST
						+ event.get_id() + ".json";
				HttpUtil.Post(URL, handler, NET_CREATE_EVENT_UPDATE, params);
			}
		});

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_CREATE_EVENT_UPDATE:
				// 创建活动/更改活动信息
				progressbar.setVisibility(View.GONE);

				if (msg.obj == null) {
					// 请求失败
					tv_created_post.setEnabled(true);
					if (fristrequest) {
						fristrequest = false;
						// 重新发送请求
						if (Constants.SPORTTYPE_CREATED.equals(type)) {
							// 创建活动
							addsportPlanToNet(); // 如果去关联了团队，则在创建活动的时候加上一个sport_id字段
						} else if (Constants.SPORTTYPE_UPDATE.equals(type)) {
							// 修改活动信息
							UpdatesportPlanToNet();
						}
					} else {
						ToastUtil.show(CreatedPlanActivity.this,
								"网络连接不可用，请稍后重试");
					}
				} else {
					// 请求成功
					JSONObject jsonobeObject = (JSONObject) msg.obj;
					try {
						if (jsonobeObject.getInt("status") == 200) {
							// 创建活动/修改活动信息 成功
							int eventid = 0;
							if (Constants.SPORTTYPE_CREATED.equals(type)) {
								// 创建活动
								eventid = jsonobeObject.getJSONObject("data")
										.getInt("id");
							} else if (Constants.SPORTTYPE_UPDATE.equals(type)) {
								// 修改活动信息
								eventid = event.get_id();
							}

							GDUtil.setGlobal(CreatedPlanActivity.this, "timeline_is_rerfresh",true);

							Intent intent = new Intent(
									CreatedPlanActivity.this,
									EventsResultActivity.class);
							intent.putExtra("isFromCreatedPlanActivity", true);
							intent.putExtra("eventid", eventid);
							startActivity(intent);

							finish();
						} else {
							// 创建活动/修改活动信息 失败
							tv_created_post.setEnabled(true);
							ToastUtil.show(CreatedPlanActivity.this, "创建活动失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;

			case NET_GETTOKEN:
				// 获取上传图片的 token
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
							HttpUtil.UploadPhotoForQiuniu(token,
									GDUtil.Bitmap2Bytes(photoBp), key, handler,
									NET_UPLOAP);
							picurl = "http://" + domain + "/" + key;

						} else {
							ToastUtil.show(CreatedPlanActivity.this,
									jsonenlist.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;

			case NET_UPLOAP:
				// 上传图片
				ResponseInfo responseInfo = (ResponseInfo) msg.obj;
				if (responseInfo.statusCode == 200) {
					if (Constants.SPORTTYPE_CREATED.equals(type)) {
						addsportPlanToNet();
					} else if (Constants.SPORTTYPE_UPDATE.equals(type)) {
						UpdatesportPlanToNet();
					}
				} else {
					progressbar.setVisibility(View.GONE);
					tv_created_post.setEnabled(true);
					ToastUtil.show(CreatedPlanActivity.this, "图片上传失败");
				}
				break;

			default:
				break;
			}

		}
	};

	@SuppressWarnings("deprecation")
	protected void onActivityResult(int requestCode, int arg1, Intent data) {
		if (data != null) {
			// 可以根据多个请求代码来作相应的操作
			switch (requestCode) {
			case TAKE_PICTURE:
				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);
				// File temp = new File(FileUtils.SDPATH + fileName + ".JPEG");
				Drawable drawable = new BitmapDrawable(bm);
				iv_top.setImageDrawable(drawable);
				tv_takephoto.setVisibility(View.GONE);
				photoBp = bm;

				// startPhotoZoom(Uri.fromFile(temp));
				break;
			case CHOOSE_PICTURE:
				try {
					Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
							getContentResolver(),
							MediaStore.Images.Media.getBitmap(
									this.getContentResolver(), data.getData()),
							null, null));
					startPhotoZoom(uri);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			// case SELECT_PICTURE_KITKAT:
			// photopsth = UrlUtil.getPath(this, arg2.getData());
			// setPicToView(arg2);
			// break;
			case TAKE_TYPE:
				activities_sport = data.getStringExtra("sport");
				initValues();
				break;
			case 3:
				if (data != null) {
					setPicToView(data);
				}
				break;

			case requestCode_time:
				// 设置时间
				time = data.getStringExtra("time");
				if (time != null) {
					String formatTime = formatTime(time.substring(0, 16),
							time.substring(17));
					tv_sport_time.setText(formatTime);
				}
				break;

			case requestCode_address:
				// 设置地址
				address = data.getStringExtra("address");
				latitude = data.getDoubleExtra("latitude", 0.0);
				longitude = data.getDoubleExtra("longitude", 0.0);

				if (!TextUtils.isEmpty(address)) {
					tv_sport_address.setText(address);
				}
				break;

			case requestCode_more:
				// 更多设置
				if (data != null) {
					limitation = data.getIntExtra("people", 0);
					teamId = data.getStringExtra("teamId");
					teamName = data.getStringExtra("teamName");
					privacyStatus = data.getStringExtra("privacyStatus");

//					Log.i("settingmore", "limitation:" + limitation);
//					Log.i("settingmore", "teamId:" + teamId);
//					Log.i("settingmore", "teamName:" + teamName);
//					Log.i("settingmore", "privacyStatus:" + privacyStatus);

				}

				break;

				
			case requestRich:
				// 富文本
				if (data !=null) {
					
					html = data.getStringExtra("html");
					
					Log.i("html", "html:" + html);
					
					if (!TextUtils.isEmpty(html)) {
						et_sport_intro.setVisibility(View.GONE);
						rl_rich_tag.setVisibility(View.VISIBLE);
						
					} else {
						rl_rich_tag.setVisibility(View.GONE);
						et_sport_intro.setVisibility(View.VISIBLE);
						
					}
					
				}
				
				break;
				

			default:
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getInstance().finishActivity(this);
		EventBus.getDefault().unregister(this);
		// 恢复默认设置, 避免发新活动时混乱
		sp.edit().putString("moremore", "0000000").commit();
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	/**
	 * 设置报名者填写项
	 * 
	 * @param msg
	 */
	public void onEventMainThread(MoreMore msg) {
		moremore = msg.getMoremore();
		StringBuilder sb = new StringBuilder();
		char[] chs = moremore.toCharArray();
		if (chs[0] == '1') {
			sb.append("姓名");
		}
		if (chs[1] == '1') {
			sb.append(",手机");
		}
		if (chs[2] == '1') {
			sb.append(",部门");
		}
		if (chs[3] == '1') {
			sb.append(",工号");
		}
		if (chs[4] == '1') {
			sb.append(",公司");
		}
		if (chs[5] == '1') {
			sb.append(",邮箱");
		}
		if (chs[6] == '1') {
			sb.append(",备注");
		}

		entry_form = sb.toString();
		if (!TextUtils.isEmpty(entry_form) && entry_form.startsWith(",")) {
			// 去掉可能的 , 开头
			entry_form = entry_form.substring(1);
		}

	}

	private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			EditText textView = (EditText) v;
			String hint;
			if (hasFocus) {
				hint = textView.getHint().toString();
				textView.setTag(hint);
				textView.setHint("");
			} else {
				hint = textView.getTag().toString();
				textView.setHint(hint);
			}
		}
	};
	private ImageView iv_top;

	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK){  
        	exit();
        	return true;
        }  
        return super.onKeyDown(keyCode, event);
          
    }  
	
}
