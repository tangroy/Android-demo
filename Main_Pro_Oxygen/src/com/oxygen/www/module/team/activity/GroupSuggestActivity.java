package com.oxygen.www.module.team.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.BaseViewHolder;
import com.oxygen.www.base.MyBaseAdapter;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Group;
import com.oxygen.www.module.team.construt.GroupsConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;

/**
 * 优秀图团队推荐界面
 * @author 杨庆雷
 * 2015-9-24下午4:00:30
 */
public class GroupSuggestActivity extends BaseActivity implements OnClickListener {
	protected static final int NET_GETGROUPS_SUGGEST = 1;
	private ImageView iv_back;
	private PullToRefreshListView pull_group_suggest;
	private List<Group> allGroups = new ArrayList<Group>();
	private List<Group> groups;
	/**
	 * 当前页
	 */
	private int page = 1;
	/**
	 * 每页获取数据数
	 */
	private int limit = 10;
	private ListView groupsListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_suggest);
		initViews();// 控件初始化
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		pull_group_suggest = (PullToRefreshListView) findViewById(R.id.pull_group_suggest);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		
		groupsListView = pull_group_suggest.getRefreshableView();
		pull_group_suggest.setMode(Mode.BOTH);
		ILoadingLayout endLabels = pull_group_suggest.getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel("加载更多...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
		pull_group_suggest.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page = 1;
				if (allGroups != null) {
					allGroups.clear();
				}
				getGroupsnNet();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				getGroupsnNet();
			}
		});
		
		groupsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent groupIntent = new Intent(GroupSuggestActivity.this,GroupDetailActivity.class);
				groupIntent.putExtra("groupid", allGroups.get(position-1).getId());
				startActivity(groupIntent);
			}
		});
		
	}


	private void initValues() {
		getGroupsnNet();
	}
	
	protected void getGroupsnNet() {
		final String url = UrlConstants.GROUP_SUGGEST+"?limit=" + limit
				+ "&page=" + page;
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(url, handler, NET_GETGROUPS_SUGGEST);
			}
		});
	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case NET_GETGROUPS_SUGGEST:
				String strObject = (String) msg.obj;
				if (strObject != null) {
					try {
						JSONObject jsoninfos = new JSONObject(strObject);
						if (jsoninfos.getInt("status") == 200) {
							groups = GroupsConstruct.ToGrouplist(jsoninfos
									.getJSONArray("data"));
							if (page != 1 && groups.size() == 0) {
								ToastUtil.show(GroupSuggestActivity.this, "没有更多团队了");
							} else {
								UpdateUi(groups);
							}
							pull_group_suggest.onRefreshComplete();
						} else {
							ToastUtil.show(GroupSuggestActivity.this, jsoninfos.getString("msg"));
							pull_group_suggest.onRefreshComplete();
						}
					} catch (JSONException e) {
						ToastUtil.show(GroupSuggestActivity.this,
								getResources().getString(R.string.errcode_wx));
						e.printStackTrace();
						pull_group_suggest.onRefreshComplete();
					}
				} else {
					ToastUtil.show(GroupSuggestActivity.this,
							getResources().getString(R.string.errcode_wx));
					pull_group_suggest.onRefreshComplete();
				}
				progressbar.setVisibility(View.GONE);
			}
		}
	};
	private ProgressBar progressbar;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;

		default:
			break;
		}
	}

	protected void UpdateUi(List<Group> groups) {
		if (page == 1 && allGroups != null) {
			allGroups.clear();
		}
		if(groups != null){
			allGroups.addAll(groups);
		}
		pull_group_suggest.setAdapter(
				new MyBaseAdapter<Group>(getApplicationContext(), allGroups, R.layout.item_team_activity) {
			@Override
			public void convert(BaseViewHolder holder, Group group) {
				ImageView iv_head = (ImageView)holder.getView(R.id.iv_head);
				TextView tv_name = (TextView)holder.getView(R.id.tv_name);
				TextView tv_member_count = (TextView)holder.getView(R.id.tv_member_count);
				TextView tv_event_info = (TextView)holder.getView(R.id.tv_event_info);
				
				ImageUtil.showImage(group.getPic(), iv_head, R.drawable.icon_def);
				tv_name.setText(group.getName());
				tv_member_count.setText(group.getMember_count()+"人");
				tv_event_info.setText(group.getIntro());
			}
		});
		groupsListView.setSelection((page - 1) * 10);
		page = page + 1;
	}
}
