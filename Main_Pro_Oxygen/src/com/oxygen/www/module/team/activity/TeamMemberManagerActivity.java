package com.oxygen.www.module.team.activity;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.oxygen.www.enties.Group;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.module.team.adapter.TeamMemberManagerAdapter;
import com.oxygen.www.module.user.activity.UserInfoActivity;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import de.greenrobot.event.EventBus;

public class TeamMemberManagerActivity extends BaseActivity implements
		OnClickListener {
	private ImageView iv_back;
	private TextView tv_manager;
	public static ProgressBar progressbar;
	private ListView lv_member;
	private Group group;
	private JSONObject jsonobject_userinfo;
	private boolean isdel = false;
	TeamMemberManagerAdapter adapter;
	private final int REMOVE_MEMBER_NET = 1;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_membermanager);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_manager = (TextView) findViewById(R.id.tv_manager);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		lv_member = (ListView) findViewById(R.id.lv_member);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_manager.setOnClickListener(this);
		lv_member.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				User user = null;
				int userid = 0;
				try {
					userid = group.getMembers().get(arg2).getUser_id();
					user = UsersConstruct.ToUser(jsonobject_userinfo
							.getJSONObject(userid + ""));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (user != null) {
					if (isdel & arg2 > 0) {
						position = arg2;
						showDialog(TeamMemberManagerActivity.this, "确定移除'"
								+ user.getNickname() + "'吗？", userid);
					} else if (!isdel) {
						Intent intent = new Intent(
								TeamMemberManagerActivity.this,
								UserInfoActivity.class);
						intent.putExtra("userid", group.getMembers().get(arg2)
								.getUser_id()
								+ "");
						intent.putExtra("isme", false);
						startActivity(intent);
					}

				} else {
					ToastUtil.show(TeamMemberManagerActivity.this, "异常数据");
				}

			}
		});

	}

	private void initValues() {
		group = (Group) getIntent().getSerializableExtra("group");
		try {
			jsonobject_userinfo = new JSONObject(getIntent().getStringExtra(
					"users"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (group.getCurrent_group_user()!=null&&group.getCurrent_group_user().getRole().equals("admin")) {
			tv_manager.setVisibility(View.VISIBLE);
		} else {
			tv_manager.setVisibility(View.GONE);
		}
		adapter = new TeamMemberManagerAdapter(this, group.getMembers(),
				jsonobject_userinfo, isdel);
		lv_member.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_manager:
			if (isdel) {
				tv_manager.setText("管理");
				isdel = false;
			} else {
				tv_manager.setText("完成");
				isdel = true;
			}
			adapter = new TeamMemberManagerAdapter(this, group.getMembers(),
					jsonobject_userinfo, isdel);
			lv_member.setAdapter(adapter);
			break;
		default:
			break;
		}

	}

	/**
	 * 确认退出框
	 * 
	 * @param content
	 */
	private void showDialog(Context content, String title, final int userid) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(content);
		dialog.setMessage(title)
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();// 取消弹出框

						remove_member(userid);
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();// 取消弹出框
					}
				}).create().show();
	}

	/**
	 * 
	 * 删除成员
	 * 
	 * @param challengesid
	 */
	private void remove_member(final int userid) {
		progressbar.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(
						UrlConstants.GROUPS_REMOVE_MEMBER_GET + group.getId()
								+ "/" + userid + ".json", handler,
						REMOVE_MEMBER_NET);
			}
		});

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REMOVE_MEMBER_NET:
				progressbar.setVisibility(View.GONE);
				String strObject_del = (String) msg.obj;
				if (strObject_del != null && strObject_del.length() > 10) {

					try {
						JSONObject jsoninfos = new JSONObject(strObject_del);
						if (jsoninfos.getInt("status") == 200) {
							group.getMembers().remove(position);
							adapter.notifyDataSetChanged();
							EventBus.getDefault().post(group.getMembers());
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					ToastUtil.show(TeamMemberManagerActivity.this, "删除失败,请重试");
				}
				break;
			default:
				break;
			}
		}

	};
}
