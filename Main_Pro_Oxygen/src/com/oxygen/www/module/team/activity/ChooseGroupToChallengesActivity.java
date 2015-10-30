package com.oxygen.www.module.team.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.ChallengesUser;
import com.oxygen.www.enties.Group;
import com.oxygen.www.module.team.adapter.GroupToChallengesAdapter;
import com.oxygen.www.module.team.construt.GroupsConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;

/**
 * 我的团队列表页面
 * 
 * @author sambatang
 * 
 */
public class ChooseGroupToChallengesActivity extends BaseActivity implements
		OnClickListener {
	private ImageView iv_back;
	private TextView tv_submit, tv_title;
	private ListView lv_team_mycreated;
	private ProgressBar progressbar;
	private final int NET_GETGROUPS = 1;
	private ArrayList<Group> admin_group;
	private int group_id = 0;
	String type;
	private ArrayList<ChallengesUser> Groups_leaderboard;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chooseteamtochallenges);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_submit = (TextView) findViewById(R.id.tv_submit);
		lv_team_mycreated = (ListView) findViewById(R.id.lv_team_mycreated);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_submit.setOnClickListener(this);
		lv_team_mycreated.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (type.equals("join")) {
					int curr_id = admin_group.get(arg2).getId();
					boolean joined = false;
					if (Groups_leaderboard != null
							&& Groups_leaderboard.size() > 0) {
						for (int i = 0; i < Groups_leaderboard.size(); i++) {
							if (curr_id == Groups_leaderboard.get(i)
									.getGroup_id()) {
								joined = true;
							}
						}
					}
					if (joined) {
						group_id = 0;
					} else {
						group_id = admin_group.get(arg2).getId();
					}

				} else if (type.equals("del")) {
					group_id = groups.get(arg2).getId();
					
					
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void initValues() {
		type = getIntent().getStringExtra("type");
		if (type.equals("join")) {
			Groups_leaderboard = (ArrayList<ChallengesUser>) getIntent()
					.getSerializableExtra("Groups_leaderboard");
			tv_title.setText(getResources().getString(R.string.choosegrouptochallenges));
			getGroupsnNet();
		} else if (type.equals("del")) {
			progressbar.setVisibility(View.GONE);
			tv_title.setText(getResources().getString(R.string.delgrouptochallenges));

			groups =  (ArrayList<Group>) getIntent()
					.getSerializableExtra("groups");
			if(groups!=null){
				UpdateUidel(groups);
			}
		}

	}
	
	private void UpdateUidel(ArrayList<Group> groups) {
		GroupToChallengesAdapter adminadapter = new GroupToChallengesAdapter(
				groups, ChooseGroupToChallengesActivity.this, null);
		lv_team_mycreated.setAdapter(adminadapter);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.iv_back:

			Intent i3 = new Intent();
			i3.putExtra("group_id", 0);
			setResult(0, i3);
			finish();
			break;
		case R.id.tv_submit:
			if (group_id != 0) {
				Intent i = new Intent();
				i.putExtra("group_id", group_id);
				if(type.equals("del")){
					setResult(1, i);
				}else{
					setResult(0, i);
				}
				finish();
			} else {
				Intent i2 = new Intent();
				i2.putExtra("group_id", 0);
				setResult(0, i2);
				finish();
			}

			break;
		default:
			break;
		}
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
			case NET_GETGROUPS:
				String strObject = (String) msg.obj;
				if (strObject != null) {
					progressbar.setVisibility(View.GONE);
					try {
						JSONObject jsoninfos = new JSONObject(strObject);
						if (jsoninfos.getInt("status") == 200) {
							admin_group = GroupsConstruct.ToGrouplist(jsoninfos
									.getJSONObject("data")
									.getJSONArray("admin"));
							if (admin_group.size() > 0) {
								UpdateUi();
							} else {
								ToastUtil.show(
										ChooseGroupToChallengesActivity.this,
										"您还没有创建自己的团队");
							}
						} else {
							ToastUtil.show(
									ChooseGroupToChallengesActivity.this,
									"获取数据异常");
						}

					} catch (JSONException e) {
						ToastUtil.show(ChooseGroupToChallengesActivity.this,
								getResources().getString(R.string.errcode_wx));
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(ChooseGroupToChallengesActivity.this,
							getResources().getString(R.string.errcode_wx));
				}
				break;

			default:
				break;
			}
		}

	

		private void UpdateUi() {
			if (admin_group.size() > 0) {
				GroupToChallengesAdapter adminadapter = new GroupToChallengesAdapter(
						admin_group, ChooseGroupToChallengesActivity.this,
						Groups_leaderboard);
				lv_team_mycreated.setAdapter(adminadapter);
			}

		}
	};
	private ArrayList<Group> groups;

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent();
			i.putExtra("group_id", 0);
			setResult(0, i);
		}
		return super.onKeyDown(keyCode, event);
	};
}
