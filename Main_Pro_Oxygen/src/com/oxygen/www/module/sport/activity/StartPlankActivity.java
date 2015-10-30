package com.oxygen.www.module.sport.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Event;
import com.oxygen.www.enties.GDAcvitity;
import com.oxygen.www.module.sport.construct.ActivitiesConstruct;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;

public class StartPlankActivity extends BaseActivity implements OnClickListener {

	public TextView tv_nowtime;
	public TextView tv_best;
	public ImageView iv_close;
	public Event event;
	private final int NET_UPDATEACTIVITIES = 1;
	private final int TIMERTASK = 2;
	private final int NET_BPP = 3;
	private int duration = 0;
	Timer timer;
	private Intent intent;
	String type;
	private boolean fristrequest = true;
	private boolean suspend = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startplank);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		tv_nowtime = (TextView) findViewById(R.id.tv_nowtime);
		tv_best = (TextView) findViewById(R.id.tv_best);
		iv_close = (ImageView) findViewById(R.id.iv_close);
	}

	private void initViewsEvent() {
		iv_close.setOnClickListener(this);
	}

	private void initValues() {
		intent = getIntent();
		type = intent.getStringExtra("type");
		if (Constants.SPORTTYPE_UPDATE.equals(type)) {
			event = (Event) getIntent().getSerializableExtra("event");

		}
		getBpp();
		timer = new Timer(true);
		timer.schedule(task, 1000, 1000);

	}

	/**
	 * 确认退出框
	 * 
	 * @param content
	 */
	private void showDialog(Context content, String title) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(content);
		dialog.setMessage(title)
				.setPositiveButton("结束", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();// 取消弹出框
						timer.cancel();
						if (Constants.SPORTTYPE_CREATED.equals(type)) {
							createdActivityfornet(duration);
						} else if (Constants.SPORTTYPE_UPDATE.equals(type)) {
							updateActivityfornet(duration);
						}
					}
				})
				.setNegativeButton("继续", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();// 取消弹出框
						suspend = false;
					}
				}).create().show();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		showDialog(this, "确认是否结束？");
		suspend = true;
	}

	TimerTask task = new TimerTask() {
		public void run() {
			if (!suspend) {
				Message message = new Message();
				message.what = TIMERTASK;
				handler.sendMessage(message);
			}
		}
	};

	/**
	 * 修改成績
	 * 
	 * @param duration
	 */
	private void updateActivityfornet(int duration) {
		if (duration > 0) {
			// POST 参数
			final Map<String, String> params = new HashMap<String, String>();
			params.put("duration", duration + "");
			params.put("status", "close");
			params.put("manual", "no");
			OxygenApplication.cachedThreadPool.execute(new Runnable() {
				public void run() {
					HttpUtil.Post(UrlConstants.ACTIVITIES_UPDATE_URL_POST
							+ event.getPerformance().get_id() + ".json",
							handler, NET_UPDATEACTIVITIES, params);
				}
			});

		}
	}

	/**
	 * 获取最佳成绩
	 */
	private void getBpp() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.ACTIVITIES_BPP_URL_POST
						+ (String)UserInfoUtils.getUserInfo(StartPlankActivity.this.getApplicationContext(),Constants.USERID, "")
						+ "/plank.json", handler, NET_BPP);
			}
		});
	}

	/**
	 * 創建並且修改
	 * 
	 * @param duration
	 */
	private void createdActivityfornet(int duration) {
		if (duration > 0) {
			// POST 参数
			final Map<String, String> params = new HashMap<String, String>();
			params.put("duration", duration + "");
			params.put("sport", "plank");
			params.put("title", "平板支撑");
			params.put("start_time", getstart_time());
			params.put("status", "close");
			params.put("intro", "一起运动更快乐，约么？");
			params.put("manual", "no");

			OxygenApplication.cachedThreadPool.execute(new Runnable() {
				public void run() {
					HttpUtil.Post(UrlConstants.ACTIVITIES_CREATE_URL_POST,
							handler, NET_UPDATEACTIVITIES, params);
				}
			});

		}
	}

	private String getstart_time() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(now);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_UPDATEACTIVITIES:
				if (msg.obj == null) {
					if (fristrequest) {
						fristrequest = false;
						if (Constants.SPORTTYPE_CREATED.equals(type)) {
							createdActivityfornet(duration);
						} else if (Constants.SPORTTYPE_UPDATE.equals(type)) {
							updateActivityfornet(duration);
						}
					} else {
						DoubleshowDialog(StartPlankActivity.this);

					}
				} else {
					JSONObject jsonobeObject = (JSONObject) msg.obj;
					try {
						if (jsonobeObject.getInt("status") == 200) {
							int eventid = 0;
							if (Constants.SPORTTYPE_CREATED.equals(type)) {
								GDAcvitity activty = ActivitiesConstruct
										.Toactivity(jsonobeObject
												.getJSONObject("data"));
								eventid = activty.getEvent_id();
							} else {
								eventid = event.get_id();
							}
							GDUtil.setGlobal(
									StartPlankActivity.this,"timeline_is_rerfresh", true);
							Intent intent = new Intent(StartPlankActivity.this,
									EventsResultActivity.class);

							intent.putExtra("eventid", eventid);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							finish();
						} else {
							ToastUtil.show(StartPlankActivity.this,
									jsonobeObject.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			case TIMERTASK:
				duration = duration + 1;
				tv_nowtime.setText(GDUtil.TransitionTime(duration));

				break;
			case NET_BPP:
				if (msg.obj == null) {
					ToastUtil.show(StartPlankActivity.this, "网络连接不可用，请稍后重试");
				} else {
					String strObject1 = (String) msg.obj;
					JSONObject jsonobeObject;
					try {
						jsonobeObject = new JSONObject(strObject1);
						if (jsonobeObject.getInt("status") == 200) {
							GDAcvitity bpp = ActivitiesConstruct
									.Toactivity(jsonobeObject.getJSONArray(
											"data").getJSONObject(0));
							int duration_best = bpp.getDuration();
							tv_best.setText(GDUtil
									.TransitionTime(duration_best));
						} else {
							ToastUtil.show(StartPlankActivity.this,
									jsonobeObject.getString("msg"));
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			default:
				break;
			}

		}
	};

	/**
	 * 重复确认
	 * 
	 * @param content
	 */
	private void DoubleshowDialog(Context content) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(content);
		dialog.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("网络连接异常，保存失败")
				.setPositiveButton("重试", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (Constants.SPORTTYPE_CREATED.equals(type)) {
							createdActivityfornet(duration);
						} else if (Constants.SPORTTYPE_UPDATE.equals(type)) {
							updateActivityfornet(duration);
						}

					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).create().show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
