package com.oxygen.www.module.sport.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Event;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;

public class EventManageActivity extends BaseActivity implements
		OnClickListener {
	private final int EVENT_DEL = 1;
	private ImageView iv_back;
	private TextView tv_sport_title, tv_sport_time, tv_address, tv_intro,
			tv_accept_count;
	private Event event;
	private RelativeLayout rl_cancel, rl_accept, rl_sale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sporteventmanage);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_accept_count = (TextView) findViewById(R.id.tv_accept_count);
		tv_sport_title = (TextView) findViewById(R.id.tv_sport_title);
		tv_sport_time = (TextView) findViewById(R.id.tv_sport_time);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_intro = (TextView) findViewById(R.id.tv_intro);
		rl_cancel = (RelativeLayout) findViewById(R.id.rl_cancel);
		rl_accept = (RelativeLayout) findViewById(R.id.rl_accept);
		rl_sale = (RelativeLayout) findViewById(R.id.rl_sale);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		rl_cancel.setOnClickListener(this);
		rl_accept.setOnClickListener(this);
		rl_sale.setOnClickListener(this);
	}

	private void initValues() {
		event = (Event) getIntent().getSerializableExtra("event");
		updateUI();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_back:
			finish();

			break;
		case R.id.rl_cancel:

			showDialog(EventManageActivity.this);

			break;

		case R.id.rl_accept:

			Intent intent_accept = new Intent(EventManageActivity.this,
					EventAcceptDetailActivity.class);
			intent_accept.putExtra("event_id", event.get_id());
			startActivity(intent_accept);

			break;
		case R.id.rl_sale:

			Intent intent_sale = new Intent(EventManageActivity.this,
					EventSalemanageActivity.class);
			intent_sale.putExtra("event", event);
			startActivity(intent_sale);

			break;

		}
	}

	private void updateUI() {
		Drawable drawable = GDUtil.engSporttodrawable(this, "icon_sporttype_"
				+ event.getSport_eng());
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		tv_sport_title.setCompoundDrawables(drawable, null, null, null);
		String title = event.getTilte();
		if (title != null) {
			tv_sport_title.setText(title);
		} else {
			tv_sport_title.setText(event.getSport_chn());
		}

		tv_sport_time.setText(event.getStart_time());
		String address = event.getAddress();
		if (address != null) {
			tv_address.setText(address.equals("null") ? "未知" : address);
		}
		tv_intro.setText(event.getIntro());
		tv_accept_count.setText(event.accept_count
				+ (event.limitation == 0 ? "/不限" : "/" + event.limitation));
	}

	/**
	 * 确认退出框
	 * 
	 * @param content
	 */
	private void showDialog(Context content) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(content);
		dialog.setTitle("darling").setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("确认删除此活动么？")
				.setPositiveButton("删除", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DelEvent();
					}

				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();// 取消弹出框
					}
				}).create().show();
	}

	/**
	 * 删除活动
	 * 
	 * @param activityid
	 */
	private void DelEvent() {
		if (event.get_id() > 0) {
			OxygenApplication.cachedThreadPool.execute(new Runnable() {
				public void run() {
					HttpUtil.Get(
							UrlConstants.EVENT_DESTROY_URL_POST
									+ event.get_id() + ".json", handler,
							EVENT_DEL);
				}
			});
		}
	}

	/**
	 * handler 更新UI
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case EVENT_DEL:
				if (msg.obj == null) {
					ToastUtil.show(EventManageActivity.this, "网络连接不可用，请稍后重试");
				} else {
					String strObject1 = (String) msg.obj;
					JSONObject jsonobeObject;
					try {
						jsonobeObject = new JSONObject(strObject1);

						if (jsonobeObject.getInt("status") == 200) {
							finish();
						} else {
							ToastUtil.show(EventManageActivity.this,
									jsonobeObject.getString("msg"));
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				break;

			default:
				break;
			}
		}

	};
}
