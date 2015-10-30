package com.oxygen.www.module.sport.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.CurrentEventUser;
import com.oxygen.www.module.sport.adapter.SportAcceptAdapter;
import com.oxygen.www.module.sport.construct.EventConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.NoScrollListView;

public class EventAcceptDetailActivity extends BaseActivity implements
		OnClickListener {
	private final int NET_GETENLIST = 1;
	private ImageView iv_back;
	private TextView tv_invite_accept, tv_invite_refuse;
	private NoScrollListView lv_invite_accept, lv_invite_refuse;
	ArrayList<CurrentEventUser> accept_CurrentEventUserlist = new ArrayList<CurrentEventUser>();
	ArrayList<CurrentEventUser> decline_CurrentEventUserlist = new ArrayList<CurrentEventUser>();
	private JSONObject json_user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acceptdetail);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_invite_accept = (TextView) findViewById(R.id.tv_invite_accept);
		tv_invite_refuse = (TextView) findViewById(R.id.tv_invite_refuse);
		lv_invite_accept = (NoScrollListView) findViewById(R.id.lv_invite_accept);
		lv_invite_refuse = (NoScrollListView) findViewById(R.id.lv_invite_refuse);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);

	}

	private void initValues() {
		getEventsInfoInNet(this.getIntent().getIntExtra("event_id", 0));
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.equals(iv_back)) {
			finish();
		}
	}

	/**
	 * 获取服务器events邀请信息列表
	 */
	private void getEventsInfoInNet(final int eventsid) {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(
						UrlConstants.EVENT_ENLIST_GET + eventsid + ".json",
						handler, NET_GETENLIST);
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
			case NET_GETENLIST:
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsonenlist = new JSONObject(strObject);
						if (jsonenlist.getInt("status") == 200) {
							UpdateUI(jsonenlist);
						} else {
							ToastUtil.show(EventAcceptDetailActivity.this,
									jsonenlist.getString("msg"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;

			default:
				break;
			}
		}

		private void UpdateUI(JSONObject jsonenlist) {
			try {
				json_user = jsonenlist.getJSONObject("users_info");
				JSONObject jsonObject = jsonenlist.getJSONObject("data");
				if (!jsonObject.isNull("accept")) {
					JSONArray acceptjsonarray = jsonObject
							.getJSONArray("accept");
					if (acceptjsonarray != null & acceptjsonarray.length() > 0) {
						for (int i = 0; i < acceptjsonarray.length(); i++) {
							JSONObject c = (JSONObject) acceptjsonarray.opt(i);
							accept_CurrentEventUserlist.add(EventConstruct
									.toCurrentEventUser(c));
						}
					}
				}
				if (!jsonObject.isNull("decline")) {
					JSONArray declinejsonarray = jsonObject
							.getJSONArray("decline");
					if (declinejsonarray != null
							& declinejsonarray.length() > 0) {
						for (int i = 0; i < declinejsonarray.length(); i++) {
							JSONObject c = (JSONObject) declinejsonarray.opt(i);
							decline_CurrentEventUserlist.add(EventConstruct
									.toCurrentEventUser(c));
						}
					}
				}
				tv_invite_accept
						.setText("接受邀请("
								+ (accept_CurrentEventUserlist != null ? accept_CurrentEventUserlist
										.size() : 0) + ")");
				tv_invite_refuse
						.setText("拒绝邀请("
								+ (decline_CurrentEventUserlist != null ? decline_CurrentEventUserlist
										.size() : 0) + ")");
				SportAcceptAdapter accept_adapter = new SportAcceptAdapter(
						EventAcceptDetailActivity.this, json_user,
						accept_CurrentEventUserlist);
				lv_invite_accept.setAdapter(accept_adapter);
				SportAcceptAdapter decline_adapter = new SportAcceptAdapter(
						EventAcceptDetailActivity.this, json_user,
						decline_CurrentEventUserlist);
				lv_invite_refuse.setAdapter(decline_adapter);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	};
}
