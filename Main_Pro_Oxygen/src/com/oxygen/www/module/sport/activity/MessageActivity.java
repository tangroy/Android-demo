package com.oxygen.www.module.sport.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.GDMessage;
import com.oxygen.www.module.challengs.activity.ChallengesDetailActivity;
import com.oxygen.www.module.sport.adapter.MessageAdapter;
import com.oxygen.www.module.sport.construct.MessageConstruct;
import com.oxygen.www.module.team.activity.GroupDetailActivity;
import com.oxygen.www.module.user.activity.UserInfoActivity;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;

public class MessageActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_back;
	private TextView tv_nomessage;
	private PullToRefreshListView lv_message_list;
	private ListView actualListView;
	private final int NET_GETMESSAGE = 1;
	private JSONObject json;
	private ArrayList<GDMessage> event_net;
	private Button tv_listisnull;
	private ProgressBar progressBar;
	/**
	 * 当前页
	 */
	private int page = 1;
	/**
	 * 每页获取数据数
	 */
	private int limit = 10;
	private List<GDMessage> allMessages = new ArrayList<GDMessage>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_nomessage = (TextView) findViewById(R.id.tv_nomessage);
		lv_message_list = (PullToRefreshListView) findViewById(R.id.lv_message_list);
		lv_message_list.setMode(Mode.BOTH);

		ILoadingLayout endLabels = lv_message_list.getLoadingLayoutProxy(false,
				true);
		endLabels.setPullLabel("加载更多...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
		actualListView = lv_message_list.getRefreshableView();
		tv_listisnull = (Button) findViewById(R.id.tv_listisnull);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		try {
			lv_message_list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String Target_type = allMessages.get(position - 1)
							.getTarget_type();
					int Target_id = allMessages.get(position - 1)
							.getTarget_id();
					if (Target_type.equals("challenge")) {
						Intent intent = new Intent(MessageActivity.this,
								ChallengesDetailActivity.class);
						intent.putExtra("challengesid", Target_id);

						startActivity(intent);
					} else if (Target_type.equals("event")) {
						Intent intent = new Intent(MessageActivity.this,
								EventsResultActivity.class);
						intent.putExtra("eventid", Target_id);
						MessageActivity.this.startActivity(intent);
					} else if (Target_type.equals("group")) {
						Intent intent = new Intent(MessageActivity.this,
								GroupDetailActivity.class);
						intent.putExtra("groupid", Target_id);
						startActivity(intent);
					} else if (Target_type.equals("friend")) {
						Intent intent = new Intent(MessageActivity.this,
								UserInfoActivity.class);
						intent.putExtra("userid", allMessages.get(position - 1)
								.getSender() + "");
						startActivity(intent);
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initViewsEvent() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(this);
		lv_message_list
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 下拉刷新
						allMessages.clear();
						page = 1;
						getMessage();
						new FinishRefresh().execute();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 上拉加载更多
						getMessage();
						new FinishRefresh().execute();
					}
				});

	}

	private void initValues() {
		// TODO Auto-generated method stub
		getMessage();
	}

	/**
	 * 
	 */
	private void getMessage() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.NOTIFICATION_INDEX_POST + "?limit="
						+ limit + "&page=" + page, handler, NET_GETMESSAGE);
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
			case NET_GETMESSAGE:
				String strObject = (String) msg.obj;
				progressBar.setVisibility(View.GONE);
				if (strObject != null && strObject.length() > 10) {
					try {
						json = new JSONObject(strObject);
						if (json.getInt("status") == 200) {
							JSONObject users = json.getJSONObject("users_info");
							event_net = MessageConstruct.ToGDMessage(
									json.getJSONArray("data"), users);
							// MessageAdapter adapter = new MessageAdapter(
							// MessageActivity.this, event_net, users);
							// lv_message_list.setAdapter(adapter);
							if (event_net != null && event_net.size() > 0) {
								notifilist(event_net);
							} else {
								if (event_net.size() == 0 && page == 1) {
									tv_listisnull.setVisibility(View.VISIBLE);
								}
								page = page + 1;
								// listtoast();
							}

						} else {
							ToastUtil.show(MessageActivity.this,
									json.getString("msg"));
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
	};
	private MessageAdapter adapter;

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.equals(iv_back)) {
			finish();
		}
	}

	/**
	 * 刷新列表数据
	 */
	private void notifilist(List<GDMessage> Message_List) {
		allMessages.addAll(Message_List);
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		} else {
			adapter = new MessageAdapter(MessageActivity.this, allMessages);
			lv_message_list.setAdapter(adapter);
		}
		actualListView.setSelection((page - 1) * 10);
		page = page + 1;
		listtoast();
	}

	/**
	 * 提示运动记录为空
	 */
	private void listtoast() {
		if (allMessages != null && allMessages.size() > 0) {
			lv_message_list.setVisibility(View.VISIBLE);
			tv_listisnull.setVisibility(View.GONE);
		} else {
			lv_message_list.setVisibility(View.GONE);
			tv_listisnull.setText("暂无消息");
			tv_listisnull.setVisibility(View.VISIBLE);
		}
	}

	private class FinishRefresh extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// adapter.notifyDataSetChanged();
			lv_message_list.onRefreshComplete();
		}
	}
}
