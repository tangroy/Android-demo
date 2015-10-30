package com.oxygen.www.module.challengs.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.module.sport.activity.EventsResultActivity;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.CircleImageView;
import com.qiniu.android.http.ResponseInfo;

/**
 * 创建挑战
 * 
 * @author sambatang
 * 
 */
public class CreatChallengesActivity extends BaseActivity implements
		OnClickListener {
	private RelativeLayout ll_starttime, rl_top;
	private LinearLayout ll_challenges_type, ll_ateam, ll_bteam;
	private ImageView iv_back, iv_challenges_type;
	private CircleImageView iv_ateam_head, iv_bteam_head;
	private TextView tv_starttime, tv_declaration_local, tv_title, tv_rule,
			tv_thisweek, tv_nextweek;
	private Button btn_sumbit;
	private Intent intent;
	private String type;
	private String is_team = "no";
	private String is_group = "no";
	private PopupWindow popupWindow;
	private TextView xuan_cancel;
	private ListView xuan_list;
	private EditText et_declaration, et_title, et_ateam_name, et_bteam_name;
	private View view;
	private String[] challenges_xuan = null;
	private String starttime, thisweekstarttime, nextweekstarttime;
	private String thisweektime, nextweektime;
	private String[] teamlogourl = { "", "" };
	private final int NET_CREATCHALLENGES = 1;
	private final int NET_GETTOKEN = 2;
	public final int NET_UPLOAP = 3;
	private final int CHOOSE_PICTURE_A = 4;
	private final int CHOOSE_PICTURE_B = 5;
	private final int CURT_PICTURE_A = 6;
	private final int CURT_PICTURE_B = 7;
	Calendar cal = Calendar.getInstance();
	private ProgressBar progressbar;
	private Bitmap logo_a, logo_b;
	private int updowncount = 0;
	/**
	 * 下一周的时间
	 */
	Calendar nextcalendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challengs_createdchallenges);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		btn_sumbit = (Button) findViewById(R.id.btn_sumbit);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_challenges_type = (ImageView) findViewById(R.id.iv_challenges_type);
		iv_ateam_head = (CircleImageView) findViewById(R.id.iv_ateam_head);
		iv_bteam_head = (CircleImageView) findViewById(R.id.iv_bteam_head);
		view = findViewById(R.id.view);
		et_declaration = (EditText) findViewById(R.id.et_declaration);
		et_title = (EditText) findViewById(R.id.et_title);
		et_ateam_name = (EditText) findViewById(R.id.et_ateam_name);
		et_bteam_name = (EditText) findViewById(R.id.et_bteam_name);
		tv_starttime = (TextView) findViewById(R.id.tv_starttime);
		ll_starttime = (RelativeLayout) findViewById(R.id.ll_starttime);
		rl_top = (RelativeLayout) findViewById(R.id.rl_top);
		ll_challenges_type = (LinearLayout) findViewById(R.id.ll_challenges_type);
		ll_ateam = (LinearLayout) findViewById(R.id.ll_ateam);
		ll_bteam = (LinearLayout) findViewById(R.id.ll_bteam);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		tv_declaration_local = (TextView) findViewById(R.id.tv_declaration_local);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_rule = (TextView) findViewById(R.id.tv_rule);

	}

	private void initViewsEvent() {
		btn_sumbit.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		et_declaration.setOnClickListener(this);
		ll_starttime.setOnClickListener(this);
		tv_rule.setOnClickListener(this);
		tv_declaration_local.setOnClickListener(this);
		iv_ateam_head.setOnClickListener(this);
		iv_bteam_head.setOnClickListener(this);
		et_declaration.setOnClickListener(this);
	}

	private void initValues() {
		intent = getIntent();
		type = intent.getStringExtra("type");

		int color = 0;
		int type_drawable = 0;
		String title = null;
		if (Constants.CHALLENGES_TYPE_PERSON.equals(type)) {
			color = R.color.challenges_person;
			type_drawable = R.drawable.icon_challenges_person_w;
			challenges_xuan = getResources()
					.getStringArray(R.array.xuan_person);
			title = "发起个人挑战";
		} else if (Constants.CHALLENGES_TYPE_TWOTEAM.equals(type)) {
			is_team = "yes";
			color = R.color.challenges_twoteam;
			type_drawable = R.drawable.icon_challenges_moreperple_w;
			challenges_xuan = getResources().getStringArray(R.array.xuan_team);
			ll_ateam.setVisibility(View.VISIBLE);
			ll_bteam.setVisibility(View.VISIBLE);
			title = "发起两队较量";
		} else if (Constants.CHALLENGES_TYPE_MORETEAM.equals(type)) {
			is_group = "yes";
			color = R.color.challenges_moreteam;
			type_drawable = R.drawable.icon_challenges_moreteam_w;
			challenges_xuan = getResources().getStringArray(R.array.xuan_team);
			title = "发起多队竞赛";
		}
		tv_title.setText(title);
		rl_top.setBackgroundColor(getResources().getColor(color));
		ll_challenges_type.setBackgroundColor(getResources().getColor(color));
		iv_challenges_type.setImageDrawable(getResources().getDrawable(
				type_drawable));
		thisweektime = "本周" + Initthisweek(Calendar.getInstance(), true);
		nextweektime = "下周" + Initthisweek(nextcalendar, false);
		tv_starttime.setText(thisweektime);
		starttime = thisweekstarttime;
		et_declaration.setText(challenges_xuan[0]);
	}

	private String Initthisweek(Calendar calendar, boolean isthisweek) {
		String endtime = null;
		String starttimeq = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			calendar.add(Calendar.DATE, -1);
		}
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				starttimeq = dateFormat.format(calendar.getTime());
				if (isthisweek) {
					thisweekstarttime = dateFormat.format(calendar.getTime());
				} else {
					nextweekstarttime = dateFormat.format(calendar.getTime());
				}
			} else if (i == 6) {
				endtime = dateFormat.format(calendar.getTime());
				nextcalendar = calendar;
				nextcalendar.setTime(calendar.getTime());

			}
			if (i == 0 && isthisweek) {
				thisweekstarttime = dateFormat.format(calendar.getTime());
			}
			calendar.add(Calendar.DATE, 1);
		}

		return "(" + starttimeq.substring(5) + "~" + endtime.substring(5) + ")";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			exit();
			break;
		case R.id.tv_declaration_local:
			getPopupWindow(v);
			// 这里是位置显示方式,在屏幕的左侧
			popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			view.setVisibility(View.VISIBLE);
			break;
		case R.id.et_declaration:
			et_declaration.setText("");
			break;
		case R.id.ll_starttime:
			getPopupWindow(v);
			// 这里是位置显示方式,在屏幕的左侧
			popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			view.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_sumbit:
			if (et_title.getText().toString().isEmpty()) {
				ToastUtil.show(CreatChallengesActivity.this, "请输入挑战标题");
				et_title.setFocusable(true);
				return;
			}

			if ("yes".equals(is_team)) {
				if (TextUtils.isEmpty(et_bteam_name.getText())
						|| TextUtils.isEmpty(et_ateam_name.getText())) {
					ToastUtil.show(CreatChallengesActivity.this, "请完善战队名称");
					return;
				}
				if (logo_a == null || logo_b == null) {
					ToastUtil.show(CreatChallengesActivity.this, "请完善战队头像");
					return;
				} else {
					btn_sumbit.setEnabled(false);
					progressbar.setVisibility(View.VISIBLE);
					getQiuniuToken();
				}
			} else {
				btn_sumbit.setEnabled(false);
				progressbar.setVisibility(View.VISIBLE);
				creatchallenges();
			}

			break;
		case R.id.iv_ateam_head:
			Intent intent_a = new Intent();
			intent_a.setType("image/*");
			intent_a.setAction(Intent.ACTION_GET_CONTENT);
			// 相片类型
			startActivityForResult(intent_a, CHOOSE_PICTURE_A);
			break;
		case R.id.iv_bteam_head:
			Intent intent_b = new Intent();
			intent_b.setType("image/*");
			intent_b.setAction(Intent.ACTION_GET_CONTENT);
			// 相片类型
			startActivityForResult(intent_b, CHOOSE_PICTURE_B);
			break;
		case R.id.tv_thisweek:
			tv_starttime.setText(thisweektime);
			starttime = thisweekstarttime;
			popupWindow.dismiss();
			view.setVisibility(View.GONE);
			break;
		case R.id.tv_nextweek:
			tv_starttime.setText(nextweektime);
			starttime = nextweekstarttime;
			popupWindow.dismiss();
			view.setVisibility(View.GONE);
			break;
		case R.id.tv_rule:
			Intent intent_rule = new Intent(CreatChallengesActivity.this,
					RuleStateActivity.class);
			intent_rule.putExtra("type", type);
			startActivity(intent_rule);
			break;
		default:
			break;
		}
	}

	/***
	 * 获取PopupWindow实例
	 */
	private void getPopupWindow(View v) {
		if (null != popupWindow) {
			popupWindow.dismiss();
			view.setVisibility(View.GONE);
			return;
		} else {
			initPopuptWindow(v);
		}
	}

	/**
	 * 创建PopupWindow
	 */
	protected void initPopuptWindow(View v) {
		View popupWindow_view = null;
		if (v == tv_declaration_local) {
			// 获取自定义布局文件activity_popupwindow_left.xml的视图
			popupWindow_view = getLayoutInflater().inflate(
					R.layout.pop_challenges_xuan, null, false);
			xuan_cancel = (TextView) popupWindow_view
					.findViewById(R.id.xuan_cancel);
			xuan_list = (ListView) popupWindow_view
					.findViewById(R.id.xuan_list);
			xuan_list.setAdapter(new ArrayAdapter<String>(
					CreatChallengesActivity.this,
					R.layout.item_challenges_xuan_tv, challenges_xuan));
			xuan_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
						popupWindow = null;
						view.setVisibility(View.GONE);
					}
				}
			});
			xuan_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					et_declaration.setText(challenges_xuan[arg2]);
					et_declaration.setFocusable(false);
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
						popupWindow = null;
						view.setVisibility(View.GONE);
					}
				}
			});

		} else if (v == ll_starttime) {
			popupWindow_view = getLayoutInflater().inflate(
					R.layout.pop_choosechallengestime, null, false);
			tv_thisweek = (TextView) popupWindow_view
					.findViewById(R.id.tv_thisweek);
			tv_nextweek = (TextView) popupWindow_view
					.findViewById(R.id.tv_nextweek);
			tv_thisweek.setText(thisweektime);
			tv_nextweek.setText(nextweektime);
			tv_thisweek.setOnClickListener(this);
			tv_nextweek.setOnClickListener(this);
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
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
					view.setVisibility(View.GONE);
				}
				return false;
			}
		});

	}

	private void creatchallenges() {

		// POST 参数
		final Map<String, String> params = new HashMap<String, String>();
		if (TextUtils.isEmpty(et_declaration.getText())) {
			params.put("intro", challenges_xuan[0]);
		} else {
			params.put("intro", et_declaration.getText().toString());
		}
		params.put("title", et_title.getText().toString());
		params.put("sport", "running");
		params.put("type", "total_distance");
		params.put("days", "7");
		params.put("is_team", is_team);
		params.put("is_group", is_group);
		params.put("ranking", "distance");
		params.put("start_time", starttime);
		if (is_team.equals("yes")) {
			JSONArray array = new JSONArray();
			try {
				JSONObject object1 = new JSONObject();
				object1.put("pic", teamlogourl[0]);
				object1.put("name", et_ateam_name.getText().toString());
				JSONObject object2 = new JSONObject();
				object2.put("pic", teamlogourl[1]);
				object2.put("name", et_bteam_name.getText().toString());
				array.put(object1);
				array.put(object2);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			params.put("teams", array.toString());
		}
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				String URL = UrlConstants.CHALLENGES_CREAT_POST;
				HttpUtil.Post(URL, handler, NET_CREATCHALLENGES, params);
			}
		});

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

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_CREATCHALLENGES:

				progressbar.setVisibility(View.GONE);
				if (msg.obj == null) {
					btn_sumbit.setEnabled(true);
					ToastUtil.show(CreatChallengesActivity.this,
							"网络连接不可用，请稍后重试");
				} else {

					JSONObject jsonobeObject = (JSONObject) msg.obj;
					try {
						if (jsonobeObject.getInt("status") == 200) {
							GDUtil.setGlobal(
									CreatChallengesActivity.this, "timeline_is_rerfresh",true);
							Intent intent = new Intent(
									CreatChallengesActivity.this,
									ChallengesDetailActivity.class);
							intent.putExtra("challengesid", jsonobeObject
									.getJSONObject("data").getInt("id"));
							startActivity(intent);
						} else {
							btn_sumbit.setEnabled(true);
							ToastUtil.show(CreatChallengesActivity.this,
									"创建挑战失败");
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
							final String token = jsonenlist.getJSONObject(
									"data").getString("token");
							final String domain = jsonenlist.getJSONObject(
									"data").getString("domain");
							if (logo_a != null) {
								OxygenApplication.cachedThreadPool
										.execute(new Runnable() {
											public void run() {
												String key = "challenges/"
														+ cal.get(Calendar.YEAR)
														+ ""
														+ cal.get(Calendar.MONTH)
														+ 1
														+ "/"
														+ System.currentTimeMillis()
														+ "_"
														+ (int) (Math.random() * 900)
														+ 100 + ".jpg";
												teamlogourl[0] = "http://"
														+ domain + "/" + key;
												HttpUtil.UploadPhotoForQiuniu(
														token,
														GDUtil.Bitmap2Bytes(logo_a),
														key, handler,
														NET_UPLOAP);
											}
										});
							}
							if (logo_b != null) {
								OxygenApplication.cachedThreadPool
										.execute(new Runnable() {
											public void run() {
												String key = "events/"
														+ cal.get(Calendar.YEAR)
														+ ""
														+ cal.get(Calendar.MONTH)
														+ 1
														+ "/"
														+ System.currentTimeMillis()
														+ "_"
														+ (int) (Math.random() * 900)
														+ 100 + ".jpg";
												teamlogourl[1] = "http://"
														+ domain + "/" + key;
												HttpUtil.UploadPhotoForQiuniu(
														token,
														GDUtil.Bitmap2Bytes(logo_b),
														key, handler,
														NET_UPLOAP);
											}
										});

							}

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			case NET_UPLOAP:
				ResponseInfo responseInfo = (ResponseInfo) msg.obj;
				if (responseInfo.statusCode != 200) {
					ToastUtil.show(CreatChallengesActivity.this, "标志上传失败");
				} else {
					updowncount = updowncount + 1;
					if (updowncount == 2) {
						creatchallenges();
					}
				}
				break;

			default:
				break;
			}

		}
	};

	protected void onActivityResult(int requestCode, int arg1, Intent arg2) {
		if (arg2 != null) {
			Uri uri;
			// 可以根据多个请求代码来作相应的操作
			switch (requestCode) {

			case CHOOSE_PICTURE_A:
				try {
					uri = Uri.parse(MediaStore.Images.Media.insertImage(
							getContentResolver(),
							MediaStore.Images.Media.getBitmap(
									this.getContentResolver(), arg2.getData()),
							null, null));
					startPhotoZoom(uri, CURT_PICTURE_A);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case CHOOSE_PICTURE_B:
				try {
					uri = Uri.parse(MediaStore.Images.Media.insertImage(
							getContentResolver(),
							MediaStore.Images.Media.getBitmap(
									this.getContentResolver(), arg2.getData()),
							null, null));
					startPhotoZoom(uri, CURT_PICTURE_B);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case CURT_PICTURE_A:
				if (arg2 != null) {
					setPicToView(arg2, iv_ateam_head);
				}
				break;
			case CURT_PICTURE_B:
				if (arg2 != null) {
					setPicToView(arg2, iv_bteam_head);
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
	public void startPhotoZoom(Uri uri, int imageview) {
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
		startActivityForResult(intent, imageview);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata, ImageView imageview) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			if (imageview.equals(iv_ateam_head)) {
				logo_a = bitmap;
			} else {
				logo_b = bitmap;
			}
			imageview.setImageBitmap(bitmap);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			exit();
		}
		return super.onKeyDown(keyCode, event);
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
	
	
}
