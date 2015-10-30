package com.oxygen.www.module.find.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.module.challengs.activity.ChallengesDetailActivity;
import com.oxygen.www.module.sport.activity.EventsResultActivity;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.QQUtils;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.WxUtil;
import com.oxygen.www.widget.MyWebView;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * 精选详情页
 * @author 杨庆雷
 * 创建时间：2015-4-20 上午10:00:16
 */
public class PostsDetailActivity extends Activity implements
		OnClickListener {
	protected   final int COLLECT_SELLECTION = 1;
	protected   final int COLLECT_UNSELLECTION = 2;
	protected   final int COLLECT_VOTE = 3;
	protected   final int COLLECT_UNVOTE = 4;
	protected   final int NET_GETISCELECT = 5;
	
	private MyWebView web_detail;
//	private WebView web_detail;
	private ImageView iv_back;
	private ImageView collect_back;
	private CheckBox collect_collect;
	private CheckBox collect_praise;
	private ImageView collect_share;
	private int id;
	private String token;
	private String title;
	private String summary;
	// 声明PopupWindow对象的引用
	private PopupWindow popupWindow;
	private ImageView iv_share_appfriend;
	private ImageView iv_share_weixin;
	private ImageView iv_share_qq;
	private ImageView iv_share_weixin_friends;
	private TextView challenges_QR;
	private TextView exit_evnent;
	private TextView cancel_evnent;
	private TextView cancel;
	/**
	 * 是否被点赞(0没有点赞，1点赞)
	 */
	private int curruser_voted = 0;
	/**
	 * 是否被收藏(0没有收藏，1收藏)
	 */
	private int curruser_bookmarked = 0;
	private String selectStrObject;
	
	private RelativeLayout rl_option;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_businessesdetail);
		fl_content = (FrameLayout) findViewById(R.id.fl_content);
		rl_option = (RelativeLayout) findViewById(R.id.rl_option);
		web_detail = (MyWebView) findViewById(R.id.web_detail);
		
		iv_back = (ImageView) findViewById(R.id.iv_back);
		id = getIntent().getIntExtra("posts_id", 0);
		token = getIntent().getStringExtra("posts_token");
		title = getIntent().getStringExtra("posts_title");
		summary = getIntent().getStringExtra("posts_summary");
		collect_back = (ImageView) findViewById(R.id.collect_back);
		collect_collect = (CheckBox) findViewById(R.id.collect_collect);
		collect_share = (ImageView) findViewById(R.id.collect_share);
		collect_praise = (CheckBox) findViewById(R.id.collect_praise);

		iv_back.setOnClickListener(this);
		collect_back.setOnClickListener(this);
		// 收藏
		collect_collect
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							if(null == selectStrObject){
								collectToNet();
							}
						} else {
							if(null == selectStrObject){
								unCollectToNet();
							}
						}
					}
				});
		// 分享
		collect_share.setOnClickListener(this);
		// 点赞
		collect_praise
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							if(null == selectStrObject){
								voteToNet();
							}
						} else {
							if(null == selectStrObject){
								unVoteToNet();
							}
						}
					}
				});

		WebSettings webSettings = web_detail.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		web_detail.loadUrl(UrlConstants.POSTS_DETAIL_URL + id+"?token="+token);
		web_detail.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				if (url.startsWith("leyundong://")) {
					// leyundong://event/123
					
					Intent intent;
					int id;
					id = Integer.parseInt(url.substring(url.lastIndexOf("/") +1));
					if (url.contains("event")) {
						intent = new Intent(PostsDetailActivity.this, EventsResultActivity.class);
						intent.putExtra("eventid", id);
						startActivity(intent);
						
					} else if (url.contains("challenge")) {
						intent = new Intent(PostsDetailActivity.this, ChallengesDetailActivity.class);
						intent.putExtra("challengesid", id);
						startActivity(intent);
					}
					
				} else {
					view.loadUrl(url);
				}
				
				return true;
			}
			
			
		});
		
	
		isselected();
	}

	/**
	 * 访问网络是否收藏和点赞
	 */
	private void isselected() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.POSTS_LIST_GET_URL2+id+".json", handler, NET_GETISCELECT);
			}
		});
	}

	private void initPopViews(View popupWindow_view) {
		iv_share_appfriend = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_appfriend);
		iv_share_weixin = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_weixin);
		iv_share_qq = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_qq);
		iv_share_weixin_friends = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_weixin_friends);
		
		challenges_QR = (TextView) popupWindow_view
				.findViewById(R.id.challenges_QR);
		exit_evnent = (TextView) popupWindow_view
				.findViewById(R.id.exit_challenges);
		cancel_evnent = (TextView) popupWindow_view
				.findViewById(R.id.cancel_challenges);
		cancel = (TextView) popupWindow_view
				.findViewById(R.id.cancel);
	}

	private void initPopViewEvENT() {
		iv_share_weixin.setOnClickListener(this);
		iv_share_qq.setOnClickListener(this);
		iv_share_weixin_friends.setOnClickListener(this);
		exit_evnent.setOnClickListener(this);
		cancel_evnent.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	
	@Override
	protected void onPause() {
 		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:

			finish();
			break;
		case R.id.collect_back:
			
			finish();
			break;
		case R.id.collect_share:
			getPopupWindow();
			// 这里是位置显示方式,在屏幕的底部
			popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			break;
		// 点滴分享弹出popwindow后 取消分享
		case R.id.cancel:
			if (null != popupWindow) {
				popupWindow.dismiss();
			}
			break;
		// 分享到微信
		case R.id.iv_share_weixin:
			if (null != popupWindow) {
				popupWindow.dismiss();
			}
			shareurl = UrlConstants.POSTS_DETAIL_URL + id + "?token="
					+ token;
			WxUtil.share2weixin(0, PostsDetailActivity.this, shareurl, title,
					"icon_index_" + title, summary);
			break;
		// 分享到微信朋友圈
		case R.id.iv_share_weixin_friends:
			if (null != popupWindow) {
				popupWindow.dismiss();
			}
			shareurl = UrlConstants.POSTS_DETAIL_URL + id + "?token="
					+ token;
			WxUtil.share2weixin(1, PostsDetailActivity.this, shareurl, title,
					"icon_index_" + title, summary);
			break;
			// 分享到QQ
		case R.id.iv_share_qq:
			if (null != popupWindow) {
				popupWindow.dismiss();
			}
			shareurl = UrlConstants.POSTS_DETAIL_URL + id + "?token="
					+ token;
			picurl = getIntent().getStringExtra("picurl");
			share2qq(shareurl);
			break;
		}
	}
	
	private void share2qq(String Shareurl) {
		String imageUrl = Constants.qiuniushare + "icon_index_"+ "running" + "_s.jpg";
//		Bundle params = new Bundle();
//		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
//		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
//		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
//		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Shareurl);
		QQUtils.doShareToQQ(this,title,imageUrl,summary,shareurl);
	}
	
	private void doShareToQQ(final Bundle params) {
		// QQ分享要在主线程做
		ThreadManager.getMainHandler().post(new Runnable() {

			@Override
			public void run() {
				if (null != OxygenApplication.tencent) {
					OxygenApplication.tencent.shareToQQ(
							PostsDetailActivity.this, params, qqShareListener);
				}
			}
		});
	}

	IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onCancel() {

		}

		@Override
		public void onComplete(Object response) {
			ToastUtil.show(PostsDetailActivity.this, "成功");
		}

		@Override
		public void onError(UiError e) {
			ToastUtil.show(PostsDetailActivity.this, e.errorMessage);
		}
	};
	

	/**
	 * 收藏
	 */
	private void collectToNet() {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("target_type", "Post");
		params.put("target_id", id + "");
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.POST_BOOKMARK, handler,
						COLLECT_SELLECTION, params);
			}
		});
	}

	/**
	 * 取消收藏
	 */
	private void unCollectToNet() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {			public void run() {
				HttpUtil.Get(UrlConstants.GET_UNBOOKMARK + id + "/Post"
						+ ".json", handler, COLLECT_UNSELLECTION);
			}
		});
	}

	/**
	 * 点赞
	 */
	private void voteToNet() {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("target_type", "Post");
		params.put("target_id", id + "");
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.POST_VOTE, handler, COLLECT_VOTE,
						params);
			}
		});
	}

	/**
	 * 取消点赞
	 */
	private void unVoteToNet() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {			public void run() {
				HttpUtil.Get(UrlConstants.GET_UNVOTE + id + "/Post" + ".json",
						handler, COLLECT_UNVOTE);
			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case COLLECT_SELLECTION:
				if (msg.obj == null) {
					ToastUtil.show(PostsDetailActivity.this, "网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonObject = (JSONObject) msg.obj;
						if (jsonObject.getInt("status") == 200) {
							ToastUtil.show(PostsDetailActivity.this, "收藏成功");
						} else if (jsonObject.getInt("status") == 400) {
							ToastUtil.show(PostsDetailActivity.this, "收藏失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			case COLLECT_UNSELLECTION:
				if (msg.obj == null) {
					ToastUtil.show(PostsDetailActivity.this, "网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						if (jsonObject.getInt("status") == 200) {
							ToastUtil.show(PostsDetailActivity.this, "取消收藏成功");
						} else if (jsonObject.getInt("status") == 400) {
							ToastUtil.show(PostsDetailActivity.this, "取消收藏失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			case COLLECT_VOTE:
				if (msg.obj == null) {
					ToastUtil.show(PostsDetailActivity.this, "网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonObject = (JSONObject) msg.obj;
						if (jsonObject.getInt("status") == 200) {
							ToastUtil.show(PostsDetailActivity.this, "点赞成功");
						} else if (jsonObject.getInt("status") == 400) {
							ToastUtil.show(PostsDetailActivity.this, "点赞失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			case COLLECT_UNVOTE:
				if (msg.obj == null) {
					ToastUtil.show(PostsDetailActivity.this, "网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						if (jsonObject.getInt("status") == 200) {
							ToastUtil.show(PostsDetailActivity.this, "取消点赞成功");
						} else if (jsonObject.getInt("status") == 400) {
							ToastUtil.show(PostsDetailActivity.this, "取消点赞失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			case NET_GETISCELECT:
				selectStrObject = (String) msg.obj;
				if (selectStrObject != null) {
					try {
						JSONObject jsonObject = new JSONObject(selectStrObject);
						if (jsonObject != null) {
//							JSONObject data = new JSONObject(jsonObject.getString("data"));
							JSONObject data = jsonObject.getJSONObject("data");
							
							if(data.getInt("curruser_voted") == 1){
//								collect_praise.setBackgroundResource(R.drawable.collect_praise_checked);
								collect_praise.setChecked(true);
							}
							if(data.getInt("curruser_bookmarked") == 1){
//								collect_collect.setBackgroundResource(R.drawable.collect_collect_checked);
								collect_collect.setChecked(true);
							}
							selectStrObject = null;
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			default:
				break;
			}
		}
	};
	private String shareurl;
	private String picurl;
	private FrameLayout fl_content;

	/***
	 * 获取PopupWindow实例
	 */
	private void getPopupWindow() {
		if (null != popupWindow) {
			popupWindow.dismiss();
			return;
		} else {
			initPopuptWindow();
		}
	}

	/**
	 * 创建PopupWindow
	 */
	protected void initPopuptWindow() {
		// TODO Auto-generated method stub
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(
				R.layout.pop_challengesdetail, null, false);
		initPopViews(popupWindow_view);
		initPopViewEvENT();
		iv_share_appfriend.setVisibility(View.GONE);
		cancel_evnent.setVisibility(View.GONE);
		exit_evnent.setVisibility(View.GONE);
		challenges_QR.setVisibility(View.GONE);
		// 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		// 点击其他地方消失
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && web_detail.canGoBack()) {
			web_detail.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		 if(web_detail != null) {
			 try {
				 web_detail.destroy();
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }
	}


}
