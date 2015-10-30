package com.oxygen.www.module.user.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
import com.oxygen.www.base.BaseViewHolder;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.MyBaseAdapter;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.module.user.adapter.FriendsAdapter;
import com.oxygen.www.module.user.eventbus_entities.RefreshFriendsList;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.widget.CircleImageView;
import com.oxygen.www.widget.NoScrollListView;

import de.greenrobot.event.EventBus;
/**
 * 我的好友界面
 * @author 杨庆雷
 * 2015-6-8上午9:47:24
 */
public class FriendsActivity extends BaseActivity implements OnClickListener {
	private final int NET_FRIENDLIST = 1;
	private final int ADD_FRIEND = 2;
	private ImageView iv_back;
	private ImageView add_friend;
	private TextView tv_myfriends_count;
	private PullToRefreshListView lv_myfriends_list;
	private TextView tv_title;
	private Button tv_listisnull;
	private List<User> users ;
	private List<User> recommend_users ;
	private String userId;
	private String userName;
	private boolean isSelf;
	/**
	 * 当前页
	 */
	private int page = 1;
	/**
	 * 每页获取数据数
	 */
	private int limit = 10;
	public  ListView actualListView;
	private ProgressBar progressBar;
	private List<User> allUsers = new ArrayList<User>();
	private FriendsAdapter fadapter;
	private NoScrollListView lv_friend_suggest;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		EventBus.getDefault().register(this);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		if(getIntent() != null && getIntent().getExtras() != null){
			userId = getIntent().getExtras().getString("userId");
			userName = getIntent().getExtras().getString("userName");
			isSelf = getIntent().getExtras().getBoolean("isSelf");
		}
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_listisnull = (Button) findViewById(R.id.tv_listisnull);
		add_friend = (ImageView) findViewById(R.id.add_friend);
		tv_myfriends_count  =(TextView) findViewById(R.id.tv_myfriends_count);
		lv_myfriends_list = (PullToRefreshListView) findViewById(R.id.lv_myfriends_list);
		lv_myfriends_list.setMode(Mode.BOTH);
		ILoadingLayout endLabels = lv_myfriends_list.getLoadingLayoutProxy(false,
				true);
		endLabels.setPullLabel("加载更多...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
		actualListView = lv_myfriends_list.getRefreshableView();
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		addHead();
	}
	
	private void addHead() {
			AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
	        View header = getLayoutInflater().inflate(R.layout.head_friendacrivity, lv_myfriends_list, false);
	        lv_friend_suggest = (NoScrollListView) header.findViewById(R.id.lv_friend_suggest);
	        lv_friend_suggest.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(FriendsActivity.this, UserInfoActivity.class);
					intent.putExtra("userid", recommend_users.get(arg2).getId()+"");
					startActivity(intent);
				}
			});
	        header.setLayoutParams(layoutParams);
	        if(isSelf){
				actualListView.addHeaderView(header);
			}
	}

	private void initViewsEvent() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(this);
		add_friend.setOnClickListener(this);
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FriendsActivity.this, UserInfoActivity.class);
				intent.putExtra("userid", allUsers.get(arg2-2).getId()+"");
				intent.putExtra("isme", false);
				startActivity(intent);
			}
		});
		
		lv_myfriends_list
		.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新
				if(fadapter != null){
					fadapter.notifyDataSetChanged();
				}else{
					fadapter =new FriendsAdapter(FriendsActivity.this, allUsers,false);
					lv_myfriends_list.setAdapter(fadapter);
				}
				listtoast();
				new FinishRefresh().execute();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 上拉加载更多
				getUserFriendListInNet();
				new FinishRefresh().execute();
			}
		});


	}

	private void initValues() {
		// TODO Auto-generated method stub
		getUserFriendListInNet();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.add_friend:
			Intent addFriendIntent = new Intent(this,AddFriendActivity.class);
			startActivity(addFriendIntent);
			break;
		default:
			break;
		}
	}

	/**
	 * 个人用户获得自己的好友列表
	 */
	private void getUserFriendListInNet() {
		final String url;
		String loginId = (String)UserInfoUtils.getUserInfo(getApplicationContext(),Constants.USERID, "");
		if(!TextUtils.isEmpty(userId) && !loginId.equals(userId)){
			url = UrlConstants.USERS_FRIEND_LIST_URL+"?limit="+limit+"&page="+page+"&user_id="+userId;
			tv_title.setText(userName+"的好友");
			add_friend.setVisibility(View.GONE);
		}else{
			url = UrlConstants.USERS_FRIEND_LIST_URL+"?limit="+limit+"&page="+page;
		}
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(url, handler,NET_FRIENDLIST);
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
			case NET_FRIENDLIST:
				progressBar.setVisibility(View.GONE);
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsonfriendlist = new JSONObject(strObject);
						if (jsonfriendlist.getInt("status") == 200) {
							int friendsCount = jsonfriendlist.getInt("friend_count");
							if(friendsCount == 0 && page == 1){
								tv_listisnull.setVisibility(View.VISIBLE);
							}
							tv_myfriends_count.setText("我的好友（"+friendsCount+"人)");
							JSONArray datajsonarray = jsonfriendlist.getJSONArray("data");
							JSONArray suggestjsonarray = jsonfriendlist.getJSONArray("friend_suggest");
							if(datajsonarray != null&datajsonarray.length() > 0){
								users = UsersConstruct.ToUserlist(datajsonarray);
								recommend_users = UsersConstruct.ToUserlist(suggestjsonarray);
								if(page == 1 && recommend_users != null && recommend_users.size() > 0){
									UpdateSuggestUI();
								}
								if(users != null && users.size() > 0){
									UpdateUI(users);
								}else{
									page = page + 1;
									listtoast();
								}
							}
						} else {
							ToastUtil.show(FriendsActivity.this,
									jsonfriendlist.getString("msg"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case ADD_FRIEND:
				String addInfo = (String) msg.obj;
				if (addInfo != null) {
					try {
						JSONObject jsonenlist = new JSONObject(addInfo);
						if (jsonenlist.getInt("status") == 200) {
							ToastUtil.show(FriendsActivity.this, "加好友请求已发送");
						} else {
							ToastUtil
									.show(FriendsActivity.this, "加好友失败或已添加好友");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(FriendsActivity.this, "网络连接不可用，请稍后重试");
				}
				break;
			default:
				break;
			}
		}
	};
	
	private void UpdateSuggestUI() {
		lv_friend_suggest.setAdapter(
				new MyBaseAdapter<User>(getApplicationContext(), recommend_users, R.layout.item_friend_suggest) {
			@Override
			public void convert(BaseViewHolder holder, final User user) {
				CircleImageView iv_suggest_head = (CircleImageView)holder.getView(R.id.iv_suggest_head);
				TextView tv_suggest_name = (TextView)holder.getView(R.id.tv_suggest_name);
				final TextView tv_add_friend = (TextView)holder.getView(R.id.tv_add_friend);
				final Button bt_add_friend = (Button)holder.getView(R.id.bt_add_friend);
				ImageUtil.showImage(user.getHeadimgurl(), iv_suggest_head, R.drawable.icon_def);
				tv_suggest_name.setText(user.getNickname());
				String relationShip = user.getNew_relationship();
				if("stranger".equals(relationShip)){
					tv_add_friend.setVisibility(View.GONE);
					bt_add_friend.setVisibility(View.VISIBLE);
					bt_add_friend.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							tv_add_friend.setVisibility(View.VISIBLE);
							bt_add_friend.setVisibility(View.GONE);
							OxygenApplication.cachedThreadPool.execute(new Runnable() {
								public void run() {
									HttpUtil.Get(UrlConstants.ADD_FRIEND + user.getId() + ".json",
											handler, ADD_FRIEND);
								}
							});
						}
					});
				}else if("pending".equals(relationShip)){
					tv_add_friend.setVisibility(View.VISIBLE);
					bt_add_friend.setVisibility(View.GONE);
				}
			}
		});
	}
	private void UpdateUI(List<User> user_list) {
		allUsers.addAll(user_list);
		if(fadapter != null){
			fadapter.notifyDataSetChanged();
		}else{
			fadapter =new FriendsAdapter(getApplicationContext(), allUsers,false);
			lv_myfriends_list.setAdapter(fadapter);
		}
		actualListView.setSelection((page - 1) * 10);
		page = page + 1;
		listtoast();
	}
	
	/**
	 * 提示运动记录为空
	 */
	private void listtoast() {
		if (allUsers != null && allUsers.size() > 0) {
			lv_myfriends_list.setVisibility(View.VISIBLE);
		} else {
			lv_myfriends_list.setVisibility(View.GONE);
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
			lv_myfriends_list.onRefreshComplete();
		}
	}
	
	private void onEventMainThread(RefreshFriendsList rfl) {
		progressBar.setVisibility(View.VISIBLE);
		allUsers.clear();
		page = 1 ;
		getUserFriendListInNet();
		
	}
}
