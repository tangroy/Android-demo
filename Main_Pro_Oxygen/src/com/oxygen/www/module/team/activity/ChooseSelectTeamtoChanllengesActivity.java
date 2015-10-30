package com.oxygen.www.module.team.activity;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Group;
import com.oxygen.www.module.team.adapter.GroupToChallengesAdapter;
import com.oxygen.www.module.team.construt.GroupsConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;

public class ChooseSelectTeamtoChanllengesActivity extends BaseActivity
		implements OnClickListener {
	private ImageView iv_back;
	private Button btn_select;
	private EditText et_select;
	private PullToRefreshListView prlv_team_list;
	private ProgressBar progressbar;
	private final int NET_SELECT_GROUPS = 1;
	private final int NET_GETGROUPS = 2;
	ArrayList<Group> groups = new ArrayList<Group>();
	private GroupToChallengesAdapter adapter;
	public static ListView actualListView;
	private TextView tv_submit;
	String key;
	/**
	 * 当前页
	 */
	private int page = 1;
	/**
	 * 每页获取数据数
	 */
	private int limit = 10;
	private int group_id = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_tochallenges);
		initViews();
		initViewsEvent();
		getGroupsnNet();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		btn_select = (Button) findViewById(R.id.btn_select);
		et_select = (EditText) findViewById(R.id.et_select);
		prlv_team_list = (PullToRefreshListView) findViewById(R.id.prlv_team_list);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		tv_submit = (TextView) findViewById(R.id.tv_submit);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		btn_select.setOnClickListener(this);
		tv_submit.setOnClickListener(this);
		actualListView = prlv_team_list.getRefreshableView();
		prlv_team_list.setMode(Mode.BOTH);
		ILoadingLayout endLabels = prlv_team_list.getLoadingLayoutProxy(false,
				true);
		endLabels.setPullLabel("加载更多...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
		prlv_team_list.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 上拉翻页
				if (!TextUtils.isEmpty(et_select.getText())) {
					selectGroupsInNet(key);
				}
				
				new FinishRefresh().execute();

			}
		});
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				group_id = groups.get(arg2 - 1).getId();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:

			Intent i = new Intent();
			i.putExtra("group_id", 0);
			setResult(Constants.INVETE_GROUP, i);

			finish();
			break;
		case R.id.tv_submit:
			if (group_id != 0) {
				Intent i1 = new Intent();
				i1.putExtra("group_id", group_id);
				setResult(Constants.INVETE_GROUP, i1);
			} else {
				Intent i3 = new Intent();
				i3.putExtra("group_id", 0);
				setResult(Constants.INVETE_GROUP, i3);

			}
			finish();
			break;
		case R.id.btn_select:
			if (TextUtils.isEmpty(et_select.getText())) {
				ToastUtil.show(ChooseSelectTeamtoChanllengesActivity.this,
						getResources().getString(R.string.team_select_null));
			} else {
				key = et_select.getText().toString();
				page = 1;
				if (groups != null) {
					groups.clear();
				}
				selectGroupsInNet(key);
			}

			break;

		default:
			break;
		}
	}

	/**
	 * 查找服务器group列表
	 */
	private void selectGroupsInNet(final String key) {
		progressbar.setVisibility(View.VISIBLE);

		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				try {
					HttpUtil.Get(UrlConstants.GROUPS_SELECT_GET + "page="
							+ page + "&limit=" + limit + "&keyword="
							+ URLEncoder.encode(key, "utf-8"),handler,
							NET_SELECT_GROUPS);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	/**
	 * 获取服务器我的团队列表
	 */
	private void getGroupsnNet() {
		final String url = UrlConstants.GROUPS_INDEX_GET + "?group_by=owner";
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(url, handler, NET_GETGROUPS);
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
			case NET_SELECT_GROUPS:
				progressbar.setVisibility(View.GONE);
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(strObject);
						if (jsonObject != null) {
							ArrayList<Group> groups_new = GroupsConstruct
									.ToGrouplist(jsonObject
											.getJSONArray("data"));
							if (groups_new.size() > 0) {
								notifilist(groups_new);
							} else {
								String context;
								if (page == 1) {
									context = "没有找到符合的团队";
								}

								else {
									context = "已经加载全部内容";
								}
								ToastUtil
										.show(ChooseSelectTeamtoChanllengesActivity.this,
												context);

							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						ToastUtil.show(
								ChooseSelectTeamtoChanllengesActivity.this,
								"请求异常，请重试");
						e.printStackTrace();
					}
				}
				break;
			case NET_GETGROUPS:

				String strObject1 = (String) msg.obj;
				if (strObject1 != null) {
					progressbar.setVisibility(View.GONE);
					try {
						JSONObject jsoninfos = new JSONObject(strObject1);
						if (jsoninfos.getInt("status") == 200) {
							groups = GroupsConstruct.ToGrouplist(jsoninfos
									.getJSONObject("data").getJSONArray(
											"member"));
							if (groups.size() > 0) {
								if (adapter != null) {
									adapter.notifyDataSetChanged();
								} else {
									adapter = new GroupToChallengesAdapter(groups,
											ChooseSelectTeamtoChanllengesActivity.this, null);
									prlv_team_list.setAdapter(adapter);
								}
							} else {
								ToastUtil
										.show(ChooseSelectTeamtoChanllengesActivity.this,
												"您还没有加入自己的团队");
							}
						} else {
							ToastUtil.show(
									ChooseSelectTeamtoChanllengesActivity.this,
									"获取数据异常");
						}

					} catch (JSONException e) {
						ToastUtil.show(
								ChooseSelectTeamtoChanllengesActivity.this,
								getResources().getString(R.string.errcode_wx));
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(ChooseSelectTeamtoChanllengesActivity.this,
							getResources().getString(R.string.errcode_wx));
				}
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 刷新列表数据
	 */
	private void notifilist(ArrayList<Group> group) {
		groups.addAll(group);
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		} else {
			adapter = new GroupToChallengesAdapter(groups,
					ChooseSelectTeamtoChanllengesActivity.this, null);
			prlv_team_list.setAdapter(adapter);
		}
		actualListView.setSelection(((page - 1) * limit));
		page = page + 1;
	}

	private class FinishRefresh extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// adapter.notifyDataSetChanged();
			prlv_team_list.onRefreshComplete();
		}
	}

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent();
			i.putExtra("group_id", 0);
			setResult(Constants.INVETE_GROUP, i);
		}
		return super.onKeyDown(keyCode, event);
	};
}
