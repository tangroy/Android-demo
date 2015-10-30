package com.oxygen.www.module.find.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Posts;
import com.oxygen.www.module.find.adapter.PostsListAdapter;
import com.oxygen.www.module.find.construct.PostsConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.widget.RefreshListView;
import com.oxygen.www.widget.RefreshListView.OnRefreshListener;

public class PostsListFrament extends Fragment  {
 	public  PullToRefreshListView prlv_posts_list;
	public  ListView actualListView;
	private Button tv_listisnull;
	private ArrayList<Posts> posts = new ArrayList<Posts>();
	private ProgressBar progressBar;
	private View rootView;
	private Context context;

	public   final int NET_GETPOSTS = 1;
	/**
	 * 精选列表
	 */
	private ArrayList<Posts> posts_net;
	/**
	 * 当前页
	 */
	private int page = 1;
	/**
	 * 每页获取数据数
	 */
	private int limit = 10;
	private PostsListAdapter adapter;
	
	private RefreshListView rlv;

	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
        	rootView = inflater.inflate(R.layout.activity_postslist, container, false);
    		context = getActivity();

    		initViews();
    		initViewsEvent();
    		initValues();
        }
        return rootView;
    }
	
	 

	private void initViews() {
		// TODO Auto-generated method stub
		
		rlv = (RefreshListView) rootView.findViewById(R.id.rlv);
 	/*	prlv_posts_list = (PullToRefreshListView) rootView.findViewById(R.id.prlv_posts_list);
		prlv_posts_list.setMode(Mode.PULL_FROM_END);

		ILoadingLayout endLabels = prlv_posts_list.getLoadingLayoutProxy(false,
				true);
		endLabels.setPullLabel("加载更多...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
		actualListView = prlv_posts_list.getRefreshableView();*/
		tv_listisnull = (Button) rootView.findViewById(R.id.tv_listisnull);
		progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
		

	}

	private void initViewsEvent() {
  
		rlv.setOnItemClickListener(new OnItemClickListener() {
//			actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(context,
						PostsDetailActivity.class);
				i.putExtra("posts_id", posts.get(arg2-1).getId());
				i.putExtra("picurl", posts.get(arg2-1).getPic());
				i.putExtra("posts_token", posts.get(arg2-1).getToken());
				i.putExtra("posts_title", posts.get(arg2-1).getTitle());
				i.putExtra("posts_summary", posts.get(arg2-1).getSummary());
				startActivity(i);
			}
		});
	
		rlv.setCanPullDown(false);
		
		rlv.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onPullDownRefresh() {
				// TODO Auto-generated method stub
//				page = 1;
//				getpostsInNet();
//				new FinishRefresh().execute();
			}
			
			@Override
			public void onLoadingMore() {
				// TODO Auto-generated method stub
				getpostsInNet();
			}
		});

		/*prlv_posts_list
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 下拉刷新
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 上拉加载更多
						getpostsInNet();
						new FinishRefresh().execute();
					}
				});
*/

	}

	private void initValues() {
		getpostsInNet();
	}
 

	/**
	 * 获取服务器post列表
	 */
	private void getpostsInNet() {
//		progressBar.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get((String)UserInfoUtils.getUserInfo(context,Constants.MY_COLLECT,UrlConstants.POSTS_LIST_GET_URL) + "page=" + page
						+ "&limit=" + limit, handler, NET_GETPOSTS);
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
			case NET_GETPOSTS:
				rlv.onRefreshFinish();
				progressBar.setVisibility(View.GONE);
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(strObject);
						if (jsonObject != null) {
							posts_net = PostsConstruct
									.ToPostslist(jsonObject);
							if (posts_net != null && posts_net.size() > 0) {
								notifilist(posts_net);
							} else {
								page = page + 1;
								listtoast();
							}
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
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
	private void notifilist(ArrayList<Posts> posts_list) {
		posts.addAll(posts_list);
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}else{
			adapter = new PostsListAdapter(context, posts);
//			prlv_posts_list.setAdapter(adapter);
			rlv.setAdapter(adapter);
		}
//		actualListView.setSelection((page - 1) * 10);
		rlv.setSelection((page - 1) * 10);
		page = page + 1;
		listtoast();
	}

	/**
	 * 提示运动记录为空
	 */
	private void listtoast() {
		if (posts != null && posts.size() > 0) {
//			prlv_posts_list.setVisibility(View.VISIBLE);
			rlv.setVisibility(View.VISIBLE);
			tv_listisnull.setVisibility(View.GONE);
		} else {
//			prlv_posts_list.setVisibility(View.GONE);
			rlv.setVisibility(View.GONE);
			tv_listisnull.setVisibility(View.VISIBLE);
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
//			prlv_posts_list.onRefreshComplete();
			rlv.onRefreshFinish();
		}
	}

}