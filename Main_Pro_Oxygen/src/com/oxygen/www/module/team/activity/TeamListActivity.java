package com.oxygen.www.module.team.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Group;
import com.oxygen.www.module.sport.activity.ChooseSportActivity;
import com.oxygen.www.module.team.adapter.TeamIndexActivityAdapter;
import com.oxygen.www.module.team.construt.GroupsConstruct;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.widget.NoScrollListView;

/**
 * 我的团队列表页面
 * 
 * @author sambatang
 * 
 */
public class TeamListActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_back, iv_creatteam;
	private TextView tv_lb_mycreated, tv_title, tv_lb_myjoin, iv_nogroup;
	private NoScrollListView lv_team_mycreated, lv_team_join;
	private ProgressBar progressbar;
	private final int NET_GETGROUPS = 1;
	private ArrayList<Group> admin_group;
	private ArrayList<Group> member_group;
	private ArrayList<Group> allGroup;
	private String userId;
	private String userName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teamindex);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_lb_mycreated = (TextView) findViewById(R.id.tv_lb_mycreated);
		tv_lb_myjoin = (TextView) findViewById(R.id.tv_lb_myjoin);
		lv_team_mycreated = (NoScrollListView) findViewById(R.id.lv_team_mycreated);
		lv_team_join = (NoScrollListView) findViewById(R.id.lv_team_join);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		iv_nogroup = (TextView) findViewById(R.id.iv_nogroup);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		lv_team_join.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ToDetail(member_group.get(arg2));

			}
		});

		lv_team_mycreated.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (allGroup != null) {
					ToDetail(allGroup.get(arg2));
				} else {
					ToDetail(admin_group.get(arg2));
				}
			}
		});
	}

	private void ToDetail(Group group) {
		Intent intent = new Intent(TeamListActivity.this,
				GroupDetailActivity.class);
		intent.putExtra("groupid", group.getId());
		startActivity(intent);
	}

	private void initValues() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			userId = getIntent().getExtras().getString("userId");
			userName = getIntent().getExtras().getString("userName");
		}
		if (((String) UserInfoUtils.getUserInfo(getApplicationContext(),
				Constants.USERID, "")).equals(userId)) {
			tv_title.setText("我的团队");
		} else {
			tv_title.setText(userName + "的团队");
		}
		getGroupsnNet();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getGroupsnNet();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (GDUtil.getGlobal(this, "groups_is_rerfresh")) {
			getGroupsnNet();
		}
		GDUtil.setGlobal(this, "groups_is_rerfresh", false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取服务器我的团队列表
	 */
	private void getGroupsnNet() {
		final String url;
		if (TextUtils.isEmpty(userId)
				|| ((String) UserInfoUtils.getUserInfo(getApplicationContext(),
						Constants.USERID, "")).equals(userId)) {
			url = UrlConstants.GROUPS_INDEX_GET + "?group_by=owner";
		} else {
			url = UrlConstants.GROUPS_LIST + "?user_id=" + userId;
		}
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
				progressbar.setVisibility(View.GONE);
				String strObject = (String) msg.obj;
				if (strObject != null) {
					try {
						JSONObject jsoninfos = new JSONObject(strObject);
						if (jsoninfos.getInt("status") == 200) {
							if (!jsoninfos.isNull("data")) {
								// 获取当前登录用户的团队
								if (TextUtils.isEmpty(userId)
										|| ((String) UserInfoUtils.getUserInfo(
												getApplicationContext(),
												Constants.USERID, ""))
												.equals(userId)) {
									admin_group = GroupsConstruct
											.ToGrouplist(jsoninfos
													.getJSONObject("data")
													.getJSONArray("admin"));
									member_group = GroupsConstruct
											.ToGrouplist(jsoninfos
													.getJSONObject("data")
													.getJSONArray("member"));
									UpdateUi();
								} else {
									allGroup = GroupsConstruct
											.ToGrouplist(jsoninfos
													.getJSONArray("data"));
									UpdateUiByUserId();
								}
							} else {
								iv_nogroup.setVisibility(View.VISIBLE);
							}

						}

					} catch (JSONException e) {
						ToastUtil.show(TeamListActivity.this, getResources()
								.getString(R.string.errcode_wx));
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(TeamListActivity.this, getResources()
							.getString(R.string.errcode_wx));
				}
				break;

			default:
				break;
			}
		}

		private void UpdateUi() {
			if (admin_group.size() == 0 && member_group.size() == 0) {
				iv_nogroup.setVisibility(View.VISIBLE);
			} else {
				if (admin_group.size() > 0) {
					if (View.GONE == tv_lb_mycreated.getVisibility()) {
						tv_lb_mycreated.setVisibility(View.VISIBLE);
					}
					TeamIndexActivityAdapter adminadapter = new TeamIndexActivityAdapter(
							admin_group, TeamListActivity.this, 0);
					ViewGroup.LayoutParams params = lv_team_mycreated
							.getLayoutParams();
					params.height = admin_group.size()
							* ((int) (72 * OxygenApplication.ppi));
					lv_team_mycreated.setLayoutParams(params);
					lv_team_mycreated.setAdapter(adminadapter);
				} else {
					// 解散团队后 团队个数为 0 的情况
					tv_lb_mycreated.setVisibility(View.GONE);
					lv_team_mycreated.setAdapter(null);
				}
				if (member_group.size() > 0) {
					TeamIndexActivityAdapter memberadapter = new TeamIndexActivityAdapter(
							member_group, TeamListActivity.this, 0);
					ViewGroup.LayoutParams params = lv_team_join
							.getLayoutParams();
					params.height = member_group.size()
							* ((int) (72 * OxygenApplication.ppi));
					lv_team_join.setLayoutParams(params);
					lv_team_join.setAdapter(memberadapter);
				} else {
					tv_lb_myjoin.setVisibility(View.GONE);
				}
			}

		}

		private void UpdateUiByUserId() {
			String loginId = (String)UserInfoUtils.getUserInfo(getApplicationContext(),Constants.USERID, "");
			if (loginId.equals(userId)) {
				tv_title.setText("我的团队");
				// iv_creatteam.setVisibility(View.GONE);
			} else {
				tv_title.setText(userName + "的团队");
			}
			if (allGroup.size() != 0) {
				TeamIndexActivityAdapter adminadapter = new TeamIndexActivityAdapter(
						allGroup, TeamListActivity.this, 0);
				ViewGroup.LayoutParams params = lv_team_mycreated
						.getLayoutParams();
				params.height = allGroup.size()
						* ((int) (72 * OxygenApplication.ppi));
				lv_team_mycreated.setLayoutParams(params);
				lv_team_mycreated.setAdapter(adminadapter);
				tv_lb_mycreated.setVisibility(View.GONE);
				tv_lb_myjoin.setVisibility(View.GONE);
			} else {
				iv_nogroup.setVisibility(View.VISIBLE);
			}
		}
	};
}
