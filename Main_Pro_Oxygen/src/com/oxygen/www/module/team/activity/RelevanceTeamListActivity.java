package com.oxygen.www.module.team.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Group;
import com.oxygen.www.module.team.adapter.RelevanceGroupAdapter;
import com.oxygen.www.module.team.construt.GroupsConstruct;
import com.oxygen.www.module.team.eventbus_enties.BindTeamId;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.NoScrollListView;

import de.greenrobot.event.EventBus;

/**
 * 我的团队列表界面
 * 
 * @author yang 2015-5-19下午2:36:42
 */
public class RelevanceTeamListActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_back;
	private TextView tv_save;
	private TextView tv_nogroup;
	private NoScrollListView lv_team_list;
	private ProgressBar progressbar;
	private Button btn_relevance;
	private final int NET_GETGROUPS = 1;
	private ArrayList<Group> group;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myteamlist);

		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {

		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_save = (TextView) findViewById(R.id.tv_save);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		tv_nogroup = (TextView) findViewById(R.id.tv_nogroup);
		btn_relevance = (Button) findViewById(R.id.btn_relevance);
		lv_team_list = (NoScrollListView) findViewById(R.id.lv_team_list);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		btn_relevance.setOnClickListener(this);
		tv_save.setOnClickListener(this);
		lv_team_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(group != null){
					String teamId = group.get(position).getId()+"";
					String name = group.get(position).getName();
					EventBus.getDefault().post(new BindTeamId(teamId, name));
					RelevanceTeamListActivity.this.finish();
				}
			}
		});
	}

	private void initValues() {

		getGroupsnNet();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_save:
			break;
		case R.id.btn_relevance:
			String teamId = "";
			String name = "";
			EventBus.getDefault().post(new BindTeamId(teamId, name));
			this.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取服务器我的团队列表
	 */
	private void getGroupsnNet() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.GROUPS_INDEX_GET + "?group_by=none",
						handler, NET_GETGROUPS);
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
				if (msg.obj != null) {

					String strObject = (String) msg.obj;
					progressbar.setVisibility(View.GONE);
					try {
						JSONObject jsoninfos = new JSONObject(strObject);
						if (jsoninfos.getInt("status") == 200) {
							group = GroupsConstruct
									.ToGrouplist(jsoninfos.getJSONArray("data"));
							UpdateUi(group);
	
						}
	
					} catch (JSONException e) {
						ToastUtil.show(RelevanceTeamListActivity.this, getResources()
								.getString(R.string.errcode_wx));
						e.printStackTrace();
					}
				} else {
					tv_nogroup.setText("好像出错喽, 请稍后再试...");
					tv_nogroup.setVisibility(View.VISIBLE);
					progressbar.setVisibility(View.GONE);
				} 
				break;

			default:
				break;
			}
		}

		private void UpdateUi(ArrayList<Group> group) {
			if (group.size() == 0) {
				tv_nogroup.setVisibility(View.VISIBLE);
			} else {
				btn_relevance.setVisibility(View.VISIBLE);
				
				RelevanceGroupAdapter adminadapter = new RelevanceGroupAdapter(group,
						RelevanceTeamListActivity.this);
				ViewGroup.LayoutParams params = lv_team_list.getLayoutParams();
				params.height = group.size()* ((int) (72 * OxygenApplication.ppi));
				lv_team_list.setLayoutParams(params);
				lv_team_list.setAdapter(adminadapter);
			}

		}

	};
	
}
