package com.oxygen.www.module.sport.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.MenuActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.FeedMoment;
import com.oxygen.www.enties.HotTags;
import com.oxygen.www.enties.UserInfo;
import com.oxygen.www.module.sport.adapter.FeedHotTagAdapter;
import com.oxygen.www.module.sport.eventbus_enties.FeedClose;
import com.oxygen.www.module.sport.eventbus_enties.FeedData;
import com.oxygen.www.module.sport.eventbus_enties.HotTag;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.KeyBoardUtils;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.RefreshListView;
import com.oxygen.www.widget.RefreshListView.OnRefreshListener;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 动态 - 标签列表(热门 tag)
 * 
 * @author 张坤
 *
 */
public class FeedHotTagActivity extends Activity implements OnClickListener {
	
	/**
	 * 显示列表数据
	 */
	private static final int NET_SHOW_LIST = 1;
	
	
	private RelativeLayout rl_loading;
	private RelativeLayout rl_error;
	private Context mContext;
	private FeedHotTagAdapter mAdapter;
	
	private RefreshListView mListView;
	private ListViewOnItemClickListener mItemClickListener;
	private ListViewOnRefreshListener mRefreshListener;
	/**
	 * 列表数据
	 */
	private List<FeedMoment> mList;
	/**
	 * 用户信息
	 */
	private Map<String, UserInfo> mUsersInfo;
	/**
	 * 当前页
	 */
	private int page = 1;
	/**
	 * 搜索 tag
	 */
	private String tag;
	/**
	 * 每页获取数据数
	 */
	private int limit = 10;
	/**
	 * gson
	 */
	private Gson gson;
	/**
	 * 是否加载更多
	 */
	private boolean isLoadingMore;
	/**
	 * 第一次进来
	 */
	private boolean isFirstIn;
	
	/**
	 * Feed-hot-tag-list 评论框
	 */
	public static LinearLayout ll_comment;
	public static EditText et_comment;
	public static Button bt_comment;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_hot_tag_list);
		
		initViews();
		initEvents();
		isFirstIn = true;
		initValues();
	}

	private void initViews() {
		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
		rl_error = (RelativeLayout) findViewById(R.id.rl_error);
		
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_nav_title = (TextView) findViewById(R.id.tv_nav_title);
		
		mListView = (RefreshListView) findViewById(R.id.rlv);
		
		ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
		et_comment = (EditText) findViewById(R.id.et_comment);
		bt_comment = (Button) findViewById(R.id.bt_comment);
		
	}

	private void initEvents() {
		EventBus.getDefault().register(this);
		
		iv_back.setOnClickListener(this);
		
		mItemClickListener = new ListViewOnItemClickListener();
		mListView.setOnItemClickListener(mItemClickListener);
		
//		mListView.setCanPullDown(true);
		mListView.setCanPullDown(false);
		
		mRefreshListener = new ListViewOnRefreshListener();
		mListView.setOnRefreshListener(mRefreshListener);
		rl_error.setOnClickListener(this);
		
	}
	

	private void initValues() {
		
		mList = new ArrayList<FeedMoment>();
		mUsersInfo = new HashMap<String, UserInfo>();
		
		tag = getIntent().getStringExtra("tag");
		if (tag != null) {
			tv_nav_title.setText(tag);
		}
		
		// 获取数据
		getData();
		
	}
	
	
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

			case NET_SHOW_LIST:
				// 显示列表数据
				mListView.onRefreshFinish();

				rl_loading.setVisibility(View.GONE);

				if (msg.obj != null) {
					rl_error.setVisibility(View.GONE);

					gson = new Gson();
//					Log.i("feeds", (String) msg.obj);
					
					HotTags data;
					try {
						data = gson.fromJson((String) msg.obj,
								HotTags.class);
						if (data.getStatus() == 200) {

							List<FeedMoment> list = data.getData();

							Map<String, UserInfo> users_info = data.getUsers_info();
							UserInfo current_user = data.getCurrent_user();

							if (list != null && list.size() > 0) {
								notifyList(list, users_info, current_user);
							} else {
								// 数据为空
								if (isLoadingMore) {
									ToastUtil.showShort(FeedHotTagActivity.this,
											"已无更多数据!");
								}

								// TODO...
							}
						} else {
							// 服务器异常
							rl_error.setVisibility(View.VISIBLE);
						}
						
					} catch (JsonSyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					

				}
				break;

			default:
				break;
			}
		};

	};


	private TextView tv_nav_title;


	private ImageView iv_back;
	
	
	/**
	 * 请求网络获取数据
	 */
	private void getData() {
			
		if (isFirstIn) {
			rl_loading.setVisibility(View.VISIBLE);
			isFirstIn = false;
		}
		
//		Log.i("tagClick", "search:"+tag);

		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			String str;
			public void run() {
				try {
					 str = URLEncoder.encode(tag, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				HttpUtil.Get(UrlConstants.FEEDS_HOT_TAG_LIST +"?tag="+str +"&page=" + page
						+ "&limit=" + limit, handler, NET_SHOW_LIST);
			}
		});

	}

	@Override
	public void onClick(View v) {

		Intent intent;
		
		switch (v.getId()) {
		
		case R.id.iv_back:
			finish();

		case R.id.rl_error:
			isLoadingMore = false;
			page = 1;
			getData();
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 刷新列表数据
	 */
	private void notifyList(List<FeedMoment> list, Map<String, UserInfo> users_info, UserInfo current_user) {
		if (isLoadingMore) {
			// 加载更多
		} else {
			// 下拉刷新
			mList.clear();
		}
		mList.addAll(list);
		mUsersInfo.putAll(users_info);
		
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}else{
			mAdapter = new FeedHotTagAdapter(mList, mUsersInfo, current_user, this, ll_comment, et_comment, bt_comment);
			mListView.setAdapter(mAdapter);
		}

	}
	
	/**
	 * ListView 加载监听
	 */
	private class ListViewOnRefreshListener implements OnRefreshListener {

		@Override
		public void onPullDownRefresh() {
			isLoadingMore = false;
			page = 1;
			getData();
			
		}

		@Override
		public void onLoadingMore() {
			isLoadingMore = true;
			page += 1;
			getData();
		}
		
	}

	/**
	 * ListView 条目点击监听
	 */
	private class ListViewOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
			/*Feed feed = mList.get(position - 1);
			
			String type = feed.getTarget_type();
			String target_id = feed.getTarget_id();
			int target_id_int = Integer.parseInt(target_id);  
			Intent intent;
			Map<String, Object> feed_data = feed.getFeed_data();
			if ("Event".equals(type)) {
				// 活动
				intent = new Intent(mContext, EventsResultActivity.class);
				intent.putExtra("eventid", target_id_int);
				startActivity(intent);
				
			} */

		}
		
	}
	
	/**
	 * 修改 mList 数据
	 * 
	 * @param msg
	 */
	public void onEventMainThread(HotTag msg) {
		mList = msg.getList();
		mUsersInfo = msg.getUsersInfo();
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 关闭评论框
	 * 
	 * @param msg
	 */
	public void onEventMainThread(FeedClose msg) {
		ll_comment.setVisibility(View.GONE);
		KeyBoardUtils.closeKeybord(et_comment, mContext);
	}
	
	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	
}
