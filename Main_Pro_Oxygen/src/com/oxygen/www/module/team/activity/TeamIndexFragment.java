package com.oxygen.www.module.team.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Group;
import com.oxygen.www.module.team.adapter.GroupindexFragmentAdapter;
import com.oxygen.www.module.team.construt.GroupsConstruct;
import com.oxygen.www.module.team.eventbus_enties.RefreshTeamFragment;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import de.greenrobot.event.EventBus;

/**
 * 我的团队列表页面
 * 
 * @author sambatang
 * 
 */
public class TeamIndexFragment extends Fragment implements OnClickListener {
	private ImageView iv_creatteam;
	private RelativeLayout rl_search_team;
	private PullToRefreshGridView gv_teams;
	private ProgressBar progressbar;
	private final int NET_GETGROUPS = 1;
	private ArrayList<Group> groups;
	private View rootView;
	private Context context;
	private GridView actualGridView;
	private GroupindexFragmentAdapter groupAdapter;
	/**
	 * 当前页
	 */
	private int page = 1;
	/**
	 * 每页获取数据数
	 */
	private int limit = 10;
	private List<Group> allGroups = new ArrayList<Group>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != rootView) {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (null != parent) {
				parent.removeView(rootView);
			}
		} else {
			rootView = inflater.inflate(R.layout.fragment_teamindex, null);
			initViews();// 控件初始化
			initViewsEvent();
			initValues();
		}
		return rootView;
	}

	private void initViews() {
		EventBus.getDefault().register(this);
		context = getActivity();
		iv_creatteam = (ImageView) rootView.findViewById(R.id.iv_creatteam);
		rl_search_team = (RelativeLayout) rootView
				.findViewById(R.id.rl_search_team);
		gv_teams = (PullToRefreshGridView) rootView.findViewById(R.id.gv_teams);
		progressbar = (ProgressBar) rootView.findViewById(R.id.progressbar);
	}

	private void initViewsEvent() {
		rl_search_team.setOnClickListener(this);
		iv_creatteam.setOnClickListener(this);
		actualGridView = gv_teams.getRefreshableView();
		gv_teams.setMode(Mode.BOTH);
		ILoadingLayout endLabels = gv_teams.getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel("加载更多...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
		gv_teams.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				page = 1;
				if (allGroups != null) {
					allGroups.clear();
				}
				getGroupsnNet();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				getGroupsnNet();
			}
		});

		actualGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				//创建团队
				if ((allGroups.size() == 0 && 0 == position) || 
						(allGroups.size() != 0 && allGroups.size() == position)) {
					Intent teamIntent = new Intent(context,
							CreateTeamActivity.class);
					teamIntent.putExtra("fromTeamIndex", "fromTeamIndex");
					context.startActivity(teamIntent);
				//推荐团队
				} else if ((allGroups.size() == 0 && 1 == position) || 
						(allGroups.size() != 0 && allGroups.size() + 1 == position)){
					Intent suggestIntent = new Intent(context.getApplicationContext(),GroupSuggestActivity.class);
					context.startActivity(suggestIntent);
				//我的团队
				}else{
					ToDetail(allGroups.get(position));
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
		// 创建团队
		case R.id.iv_creatteam:
			Intent teamIntent = new Intent(context, CreateTeamActivity.class);
			teamIntent.putExtra("sport", "running");
			teamIntent.putExtra("fromTeamIndex", "fromTeamIndex");
			startActivity(teamIntent);
			break;
		// 查找团队
		case R.id.rl_search_team:
			Intent intent_select = new Intent(context, SelectTeamActivity.class);
			startActivity(intent_select);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取服务器我的团队列表
	 */
	private void getGroupsnNet() {
		final String url = UrlConstants.GROUPS_LIST + "?limit=" + limit
				+ "&page=" + page + "&user_id="
				+ (String)UserInfoUtils.getUserInfo(context.getApplicationContext(), Constants.USERID, "");
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
							groups = GroupsConstruct.ToGrouplist(jsoninfos
									.getJSONArray("data"));
							if (page != 1 && groups.size() == 0) {
								ToastUtil.show(context, "没有更多团队了");
							} else {
								UpdateUi(groups);
							}
							gv_teams.onRefreshComplete();
						} else {
							ToastUtil.show(context, jsoninfos.getString("msg"));
							gv_teams.onRefreshComplete();
						}
					} catch (JSONException e) {
						ToastUtil.show(context,
								getResources().getString(R.string.errcode_wx));
						gv_teams.onRefreshComplete();
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(context,
							getResources().getString(R.string.errcode_wx));
					gv_teams.onRefreshComplete();
				}
				break;

			default:
				break;
			}
		}

	};

	private void UpdateUi(List<Group> groups) {
		if (page == 1 && allGroups != null) {
			allGroups.clear();
		}
		allGroups.addAll(groups);
		if (groupAdapter == null) {
			groupAdapter = new GroupindexFragmentAdapter(allGroups, context,
					true);
			gv_teams.setAdapter(groupAdapter);
		} else {
			groupAdapter.notifyDataSetChanged();
		}
		actualGridView.setSelection((page - 1) * 10);
		page = page + 1;
	}

	/**
	 * 查看团队详情
	 * 
	 * @param group
	 */
	private void ToDetail(Group group) {
		Intent intent = new Intent(context, GroupDetailActivity.class);
		intent.putExtra("groupid", group.getId());
		startActivity(intent);
	}

	private void onEventMainThread(RefreshTeamFragment rtf) {
		page = 1;
		getGroupsnNet();
	}
}
