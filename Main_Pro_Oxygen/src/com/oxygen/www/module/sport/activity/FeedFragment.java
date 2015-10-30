package com.oxygen.www.module.sport.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.MenuActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Banner;
import com.oxygen.www.enties.Feed;
import com.oxygen.www.enties.FeedUser;
import com.oxygen.www.enties.Feeds;
import com.oxygen.www.enties.UserInfo;
import com.oxygen.www.module.challengs.activity.ChallengesDetailActivity;
import com.oxygen.www.module.find.activity.PostsDetailActivity;
import com.oxygen.www.module.sport.adapter.BannerAdapter;
import com.oxygen.www.module.sport.adapter.FeedAdapter;
import com.oxygen.www.module.sport.eventbus_enties.FeedClose;
import com.oxygen.www.module.sport.eventbus_enties.FeedData;
import com.oxygen.www.module.team.activity.GroupDetailActivity;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.KeyBoardUtils;
import com.oxygen.www.utils.TextUtils;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.RefreshListView;
import com.oxygen.www.widget.RefreshListView.OnRefreshListener;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
/**
 * 动态 Fragment
 * 
 * @author 张坤
 *
 */
public class FeedFragment extends Fragment implements OnClickListener {
	
	/**
	 * 显示列表数据
	 */
	private static final int NET_SHOW_LIST = 1;
	
	private RelativeLayout rl_loading;
	private RelativeLayout rl_error;
	private View rootView;
	/**
	 * 发布动态按钮
	 */
	private ImageView iv_pub;
	private Context mContext;
	
	private ViewPager mViewPager;
	private BannerAdapter mBannerAdapter;
	private List<String> mUrls;
	
	private RefreshListView mListView;
	private FeedAdapter mAdapter;
	private ListViewOnItemClickListener mItemClickListener;
	private ListViewOnRefreshListener mRefreshListener;
	/**
	 * 列表数据
	 */
	private List<Feed> mList;
	/**
	 * 用户信息
	 */
	private Map<String, UserInfo> mUsersInfo;
	/**
	 * 推荐好友
	 */
	private List<FeedUser> mFriends;
	/**
	 * 当前页
	 */
	private int page = 1;
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
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
        	rootView = inflater.inflate(R.layout.activity_dynamic, container, false);
        	mContext = getActivity();

    		initViews();
    		initEvents();
    		isFirstIn = true;
    		initValues();
        }
        return rootView;
    }

	private void initViews() {
		rl_loading = (RelativeLayout) rootView.findViewById(R.id.rl_loading);
		rl_error = (RelativeLayout) rootView.findViewById(R.id.rl_error);
		iv_pub = (ImageView) rootView.findViewById(R.id.iv_pub);
		
		mListView = (RefreshListView) rootView.findViewById(R.id.rlv);
		
	}

	private void initEvents() {
		
		EventBus.getDefault().register(this);
		iv_pub.setOnClickListener(this);
		
		mItemClickListener = new ListViewOnItemClickListener();
		mListView.setOnItemClickListener(mItemClickListener);
		
//		mListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
		// 设置 listview 滑动时不加载图片
		mListView.setImageLoader(ImageLoader.getInstance());
		
		
		mListView.setCanPullDown(true);
		mRefreshListener = new ListViewOnRefreshListener();
		mListView.setOnRefreshListener(mRefreshListener);
		rl_error.setOnClickListener(this);
	}
	

	private void initValues() {
		
		mList = new ArrayList<Feed>();
		mUsersInfo = new HashMap<String, UserInfo>();
		
		mFriends = new ArrayList<FeedUser>(); 
		
		mViewPager = new ViewPager(mContext);
		
		LayoutParams params = new LayoutParams();
		params.height = (int)(130 * OxygenApplication.ppi); 
//		params.height = LayoutParams.WRAP_CONTENT; 
		params.width = LayoutParams.MATCH_PARENT;
		mViewPager.setLayoutParams(params);
		mUrls = new ArrayList<String>();
		
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
					Feeds data;
					try {
						data = gson.fromJson((String) msg.obj, Feeds.class);
						if (data.getStatus() == 200) {
							List<Feed> list = data.getData();
							UserInfo current_user = data.getCurrent_user();
							if (!isLoadingMore) {
								// 下拉刷新
								// 刷新顶部 banners
								List<Banner> banners = data.getBanners();
								List<String> urls = new ArrayList<String>();
								if (banners != null && banners.size() > 0) {
									for (Banner banner : banners) {
										String url = banner.getPic();
										if (!TextUtils.isEmpty(url)) {
											urls.add(url);
										}
									}
								}

								if (urls.size() > 0) {
									// Log.i("banners", "urls.size = "+urls.size());
									mUrls.clear();
									mUrls.addAll(urls);
									if (mBannerAdapter == null) {
										mBannerAdapter = new BannerAdapter(
												mContext, mUrls, banners);
										mViewPager.setAdapter(mBannerAdapter);
										mListView.addCustomHeaderView(mViewPager);
									} else {
										mBannerAdapter.notifyDataSetChanged();
									}

									// 动态的让轮播图切换起来.
									/**
									 * -> 1.使用handler执行一个延时任务: postDelayed ->
									 * 2.任务类runnable的run方法会被执行 -> 3.使用handler发送一个消息
									 * -> 4.Handler类的handleMessage方法接收到消息. ->
									 * 5.在handleMessage方法中, 把ViewPager的页面切换到下一个,
									 * 同时:1.使用handler执行一个延时任务
									 */
									if (mHandler == null) {
										mHandler = new InternalHandler();
									}
									mHandler.removeCallbacksAndMessages(null); // 把所有的消息和任务清空
									mHandler.postDelayed(
											new AutoSwitchPagerRunnable(), 5000);

								}
							}

							Map<String, UserInfo> users_info = data.getUsers_info();

							List<FeedUser> friend_suggest = data.getFriend_suggest();

							if (list != null && list.size() > 0) {
								notifyList(list, users_info, current_user,
										friend_suggest);
							} else {
								// 数据为空
								if (isLoadingMore) {
									ToastUtil.showShort(mContext, "已无更多数据!");
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

					

				} else {
					// 网络异常
					rl_error.setVisibility(View.VISIBLE);
				}

				break;

			default:
				break;
			}
		};

	};
	
	/**
	 * @author andong
	 * 内部的handler
	 */
	class InternalHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			int currentItem = mViewPager.getCurrentItem() + 1;
			
			if (mUrls != null && mUrls.size() > 1) {
				mViewPager.setCurrentItem(currentItem);
//				Log.i("ViewPager", "动了, currentItem = "+ currentItem);
				mHandler.postDelayed(new AutoSwitchPagerRunnable(), 5000);
			}
			
		}
	}
	private InternalHandler mHandler;
	
	/**
	 * @author andong
	 * 自动切换页面任务类
	 */
	class AutoSwitchPagerRunnable implements Runnable {

		@Override
		public void run() {
			mHandler.obtainMessage().sendToTarget();
		}
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
		if (mHandler != null) {
			// 把所有的消息和任务清空
			mHandler.removeCallbacksAndMessages(null); 
//			Log.i("ViewPager", "停止切换");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mHandler != null) {
			// 把所有的消息和任务清空
			mHandler.removeCallbacksAndMessages(null); 
//			Log.i("ViewPager", "重新开始");
			// 重新动起来
			mHandler.postDelayed(new AutoSwitchPagerRunnable(), 5000);
		}
		
		if(GDUtil.getGlobal(mContext, "feedpulish")){
			GDUtil.setGlobal(mContext, "feedpulish", false);
			isLoadingMore = false;
			page = 1;
			getData();
		}
	}
	
	/**
	 * 请求网络获取数据
	 */
	private void getData() {
		if (isFirstIn) {
			rl_loading.setVisibility(View.VISIBLE);
			isFirstIn = false;
		}
			
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.FEEDS_LIST + "page=" + page
						+ "&limit=" + limit+"&banner=1&friend_suggest=1", handler, NET_SHOW_LIST);
			}
		});

	}
	

	@Override
	public void onClick(View v) {

		Intent intent;
		
		switch (v.getId()) {
		
		case R.id.iv_pub:
			// 发布动态
			// TODO...
//			intent = new Intent(mContext, FeedPublishActivity.class);
//			startActivity(intent);
			
			showPopWindow();
			break;
			
		case R.id.camera:
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, TAKE_PICTURE);
			dismissPopupWindow();
			
			break;
			
		case R.id.picture:
			intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, CHOOSE_PICTURE);
			dismissPopupWindow();
			break;
			
		case R.id.back:
			dismissPopupWindow();
			break;

		case R.id.rl_error:
			getData();
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 刷新列表数据
	 */
	private void notifyList(List<Feed> list, Map<String, UserInfo> users_info, UserInfo current_user, List<FeedUser> friends) {
		if (isLoadingMore) {
			// 加载更多
		} else {
			// 下拉刷新
			mList.clear();
		}
		mList.addAll(list);
		mUsersInfo.putAll(users_info);
		
		mFriends.clear();
		mFriends.addAll(friends);
		
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}else{
			mAdapter = new FeedAdapter(mList, mUsersInfo, current_user, mFriends, mContext, MenuActivity.ll_comment, MenuActivity.et_comment, MenuActivity.bt_comment);
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
			
			Feed feed = mList.get(position - 1);
			
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
				
			} else if ("Challenge".equals(type)) {
				// 挑战
				intent = new Intent(mContext, ChallengesDetailActivity.class);
				intent.putExtra("challengesid", target_id_int);
				startActivity(intent);
				
			} else if ("Post".equals(type)) {
				// 精选
				intent = new Intent(mContext, PostsDetailActivity.class);
				intent.putExtra("posts_id", Integer.parseInt((String)feed_data.get("id")));
				intent.putExtra("picurl", (String)feed_data.get("pic"));
				intent.putExtra("posts_token", (String)feed_data.get("token"));
				intent.putExtra("posts_title", (String)feed_data.get("title"));
				intent.putExtra("posts_summary", (String)feed_data.get("summary"));
				startActivity(intent);
				
			} else if ("Group".equals(type)) {
				// 团队
				intent = new Intent(mContext, GroupDetailActivity.class);
				intent.putExtra("groupid", target_id_int);
				startActivity(intent);
				
			} else if ("Activity".equals(type)) {
				// 成绩
				intent = new Intent(mContext, GDActivityResultActivity.class);
				intent.putExtra("activityid", target_id_int);
				intent.putExtra("sportcategory", GDUtil.SportCategory((String)feed_data.get("sport")));
				intent.putExtra("bestDuration", Integer.parseInt((String)feed_data.get("duration")));
				intent.putExtra("sport_eng", (String)feed_data.get("sport"));
				startActivity(intent);
				
			}

		}
		
	}
	
	
	/**
	 * 修改 mList 数据
	 * 
	 * @param msg
	 */
	public void onEventMainThread(FeedData msg) {
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
		MenuActivity.ll_comment.setVisibility(View.GONE);
		KeyBoardUtils.closeKeybord(MenuActivity.et_comment, mContext);
	}
	
	/**
	 * 修改 mFriends 数据
	 * 
	 * @param msg
	 */
//	public void onEventMainThread(FeedFriends msg) {
//		mFriends = msg.getList();
//	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dismissPopupWindow();
		EventBus.getDefault().unregister(this);
		
	}
	
	
	private PopupWindow popupWindow;
	private Button picture;
	private Button camera;
	private Button back;
	
	/**
	 * 打开popWindow(相机和相册)
	 */
	private void showPopWindow() {
		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int width = manager.getDefaultDisplay().getWidth();
		if (popupWindow == null || !popupWindow.isShowing()) {
			View contentView = View.inflate(mContext,
					R.layout.activity_psw_popupwindow, null);
			camera = (Button) contentView.findViewById(R.id.camera);
			picture = (Button) contentView.findViewById(R.id.picture);
			back = (Button) contentView.findViewById(R.id.back);
			camera.setOnClickListener(this);
			picture.setOnClickListener(this);
			back.setOnClickListener(this);
			popupWindow = new PopupWindow(contentView);
			popupWindow.setWidth(width);
			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);// 设置弹出框大小
			// /在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
			popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			popupWindow.showAtLocation(iv_pub, Gravity.BOTTOM, 0, 0);
			popupWindow.setOutsideTouchable(false);
			
		}
	}
	
	/**
	 * 关闭当前界面的弹出窗体
	 */
	private void dismissPopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}
	
	private final int TAKE_PICTURE = 10;
	private final int CHOOSE_PICTURE = 20;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (TAKE_PICTURE == requestCode && Activity.RESULT_OK == resultCode) {
			Bitmap bitmap = (Bitmap) data.getExtras().get("data");
			
			/*if (bitmap != null) {
				Log.i("process", "有结果");
				
				Intent intent = new Intent(mContext, FeedPublishActivity.class);
				intent.putExtra("bitmap", bitmap);
				startActivity(intent);*/
			Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, null,null));
			if (uri != null) {
				Intent intent = new Intent(mContext, FeedPublishActivity.class);
				intent.putExtra("uri", uri);
				startActivity(intent);
				
			} else {
				Log.i("process", "结果为 null");
				
			}
			
		}
		if (CHOOSE_PICTURE == requestCode && Activity.RESULT_OK == resultCode) {
			try {
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), data.getData());
				
				Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, null, null));
				
//				intent.putExtra("bitmap", bitmap);
				if (uri != null) {
					Intent intent = new Intent(mContext, FeedPublishActivity.class);
					intent.putExtra("uri", uri);
					startActivity(intent);
					
				}else {
					Log.i("process", "结果为 null");
					
				}
					
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
