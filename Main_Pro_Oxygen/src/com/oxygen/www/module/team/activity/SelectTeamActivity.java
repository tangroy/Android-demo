package com.oxygen.www.module.team.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.oxygen.www.base.BaseViewHolder;
import com.oxygen.www.base.MyBaseAdapter;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Group;
import com.oxygen.www.module.team.adapter.TeamIndexActivityAdapter;
import com.oxygen.www.module.team.construt.GroupsConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;

/**
 * 搜索团队界面
 * @author 杨庆雷
 * 2015-9-25上午10:06:58
 */
public class SelectTeamActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_back;
	private Button btn_select;
	private EditText et_select;
	private PullToRefreshListView prlv_team_list;
	private ProgressBar progressbar;
	private final int NET_SELECT_GROUPS = 1;
	private final int NET_GETGROUPS_SUGGEST = 2;
	private List<Group> groups;
	private List<Group> suggestGroups;
	private List<Group> allGroups = new ArrayList<Group>();
	private List<Group> allSuggestGroups = new ArrayList<Group>();
	private TeamIndexActivityAdapter adapter;
	public static ListView actualListView;
	String key;
	/**
	 * 搜索团队页
	 */
	private int page = 1;
	/**
	 * 推荐团队页
	 */
	private int suggestPage = 1;
	/**
	 * 每页获取数据数
	 */
	private int limit = 10;
	private boolean searchFlag = false;
	private TextView tv_group_suggest;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_select);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		btn_select = (Button) findViewById(R.id.btn_select);
		et_select = (EditText) findViewById(R.id.et_select);
		prlv_team_list = (PullToRefreshListView) findViewById(R.id.prlv_team_list);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		tv_group_suggest = (TextView) findViewById(R.id.tv_group_suggest);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		btn_select.setOnClickListener(this);
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
				if(searchFlag){
					page = 1;
					allGroups.clear();
					selectGroupsInNet(key);
				}else{
					suggestPage = 1;
					allSuggestGroups.clear();
					getGroupsSuggest();
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 上拉翻页
				if(searchFlag){
					selectGroupsInNet(key);
				}else{
					getGroupsSuggest();
				}
			}
		});
		actualListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(SelectTeamActivity.this,
						GroupDetailActivity.class);
				if(searchFlag){
					intent.putExtra("groupid",allGroups.get(arg2-1).getId());
				}else{
					intent.putExtra("groupid",allSuggestGroups.get(arg2-1).getId());
				}
				startActivity(intent);
			}
		});
	}
	
	private void initValues() {
		progressbar.setVisibility(View.VISIBLE);
		getGroupsSuggest();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_select:
			progressbar.setVisibility(View.VISIBLE);
			tv_group_suggest.setVisibility(View.GONE);
			searchFlag = true;
			if (TextUtils.isEmpty(et_select.getText())) {
				ToastUtil.show(SelectTeamActivity.this, getResources()
						.getString(R.string.team_select_null));
			} else {
				key = et_select.getText().toString();
				page = 1;
				if(allGroups!=null){
					allGroups.clear();
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
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				try {
					HttpUtil.Get(UrlConstants.GROUPS_SELECT_GET+"page="+page+"&limit="+limit+"&keyword="+URLEncoder.encode(key, "utf-8"), handler,
							NET_SELECT_GROUPS);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}
	
	/**
	 * 推荐团队
	 */
	protected void getGroupsSuggest() {
		final String url = UrlConstants.GROUP_SUGGEST+"?limit=" + limit+ "&page=" + suggestPage;
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(url, handler, NET_GETGROUPS_SUGGEST);
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
							groups = GroupsConstruct
									.ToGrouplist(jsonObject
											.getJSONArray("data"));
							if (groups != null && groups.size() > 0) {
								notifilist(groups);
							} else {
								String context;
								if(page==1){
									context = "没有找到符合的团队";
								}else{
									context = "没有更多团队了";
								}
								ToastUtil.show(SelectTeamActivity.this,
										context);
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						ToastUtil.show(SelectTeamActivity.this, "请求异常，请重试");
						e.printStackTrace();
					}
				}
				prlv_team_list.onRefreshComplete();
				break;
			case NET_GETGROUPS_SUGGEST:
				String strObjectSuggest = (String) msg.obj;
				if (strObjectSuggest != null) {
					try {
						JSONObject jsoninfos = new JSONObject(strObjectSuggest);
						if (jsoninfos.getInt("status") == 200) {
							suggestGroups = GroupsConstruct.ToGrouplist(jsoninfos
									.getJSONArray("data"));
							if (page != 1 && suggestGroups.size() == 0) {
								ToastUtil.show(SelectTeamActivity.this, "没有更多团队了");
							} else {
								UpdateSuggestUi(suggestGroups);
								progressbar.setVisibility(View.GONE);
							}
						} else {
							ToastUtil.show(SelectTeamActivity.this, jsoninfos.getString("msg"));
						}
					} catch (JSONException e) {
						ToastUtil.show(SelectTeamActivity.this,
								getResources().getString(R.string.errcode_wx));
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(SelectTeamActivity.this,
							getResources().getString(R.string.errcode_wx));
					prlv_team_list.onRefreshComplete();
				}
				prlv_team_list.onRefreshComplete();
				progressbar.setVisibility(View.GONE);
			default:
				break;
			}
		}

	};

	/**
	 * 刷新列表数据
	 */
	private void notifilist(List<Group> groups) {
		allGroups.addAll(groups);
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		} else {
			adapter = new TeamIndexActivityAdapter(allGroups, SelectTeamActivity.this,1);
			prlv_team_list.setAdapter(adapter);
		}
		actualListView.setSelection(((page - 1) * limit));
		page = page + 1;
	}
	
	protected void UpdateSuggestUi(List<Group> suggestGroups) {
		if (suggestPage == 1 && allSuggestGroups != null) {
			allSuggestGroups.clear();
		}
		if(suggestGroups != null){
			allSuggestGroups.addAll(suggestGroups);
		}
		prlv_team_list.setAdapter(
				new MyBaseAdapter<Group>(getApplicationContext(), allSuggestGroups, R.layout.item_team_activity) {
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
		actualListView.setSelection((suggestPage - 1) * 10);
		suggestPage = suggestPage + 1;
	}
}
