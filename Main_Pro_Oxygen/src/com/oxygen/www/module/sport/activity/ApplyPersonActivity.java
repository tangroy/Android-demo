package com.oxygen.www.module.sport.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.oxygen.www.R;
import com.oxygen.www.R.color;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Event;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.adapter.AcceptPersonAdapter;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.module.user.activity.InviteFriendActivity;
import com.oxygen.www.module.user.activity.UserInfoActivity;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.WxUtil;
import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * 参与成员界面
 * @author 杨庆雷
 * 2015-6-11上午11:13:41
 */
public class ApplyPersonActivity extends BaseActivity implements OnClickListener {
	
	/**
	 * 获取参与成员列表
	 */
	protected static final int NET_FRIENDLIST = 1;
	/**
	 * 更改签到状态
	 */
	protected static final int EVENTS_SET_CHECKIN_STATUS_POST = 2;
	
 	private ImageView iv_back;
 	private TextView tv_manager;
	private Button tv_listisnull;
	private PullToRefreshListView gv_accept_person;
	private TextView accept_count;
	private RelativeLayout rl_share;
	private Event event;
	private User current_user;
 	public  ListView actualListView;
	private ProgressBar progressBar;
	private PopupWindow popupWindow;
	ArrayList<User> users_accepts = new ArrayList<User>();
	AcceptPersonAdapter dailadapter;
	private String Shareurl;
	
	/**
	 * 当前页
	 */
	private int page = 1;
	/**
	 * 每页获取数据数
	 */
	private int limit = 15;
	private TextView iv_share_friend;
	private TextView iv_share_weixin;
	private TextView iv_share_qq;
	private TextView iv_share_weixin_friends;
	private TextView cancel;
	
	/**
	 * 被更改签到状态的成员条目位置
	 */
	private int theitem = -1;
	/**
	 * 被设置成的状态
	 */
	private String newStatus = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applyperson);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		
 		event = (Event) getIntent().getSerializableExtra("event");
 		current_user = (User) getIntent().getSerializableExtra("current_user");
 		
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_manager = (TextView) findViewById(R.id.tv_manager);

		tv_listisnull = (Button) findViewById(R.id.tv_listisnull);
		accept_count = (TextView) findViewById(R.id.accept_count);
		rl_share = (RelativeLayout) findViewById(R.id.rl_share);
//		cancel = (TextView) findViewById(R.id.cancel);
		// 分享链接
		Shareurl = UrlConstants.SHARE_INVIT_URL + event.get_id()
							+ "?token=" + event.getToken();

		// 是否是管理员(活动发起者) 
		if (event.getCreated_by() == current_user.getId()) {
			tv_manager.setVisibility(View.VISIBLE);
		} 
		
		gv_accept_person = (PullToRefreshListView) findViewById(R.id.gv_accept_person);
		gv_accept_person.setMode(Mode.PULL_FROM_END);

		ILoadingLayout endLabels = gv_accept_person.getLoadingLayoutProxy(false,
				true);
		endLabels.setPullLabel("加载更多...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
		actualListView = gv_accept_person.getRefreshableView();
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		
	}

	private void initViewsEvent() {
		
		iv_back.setOnClickListener(this);
		rl_share.setOnClickListener(this);
		tv_manager.setOnClickListener(this);
		
//		cancel.setOnClickListener(this);
		
		actualListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view,
					int position, long id) {
				
				TextView check_in_status = (TextView) view.findViewById(R.id.check_in_status);
				final String status = check_in_status.getText().toString();

				// 活动发起者权限
				if (event.getCreated_by() == current_user.getId()) {
					
					Builder builder = new Builder(ApplyPersonActivity.this);
					final AlertDialog dialog = builder.create();
					View dialogview = View.inflate(ApplyPersonActivity.this, R.layout.dialog_check_in, null);
					final TextView tv_dialog = (TextView) dialogview.findViewById(R.id.tv_check_in);
					
					if ("已签到".equals(status)) {
						tv_dialog.setText("取消签到");
						
					} else if ("签到被取消".equals(status)) {
						tv_dialog.setText("恢复签到");
						
					}
					else if ("未签到".equals(status)) {
						tv_dialog.setText("代签到");
					} else if ("发起者代签到".equals(status)) {
						tv_dialog.setText("取消签到");
					}
					
					dialog.setView(dialogview, 0, 0, 0, 0);
					dialog.show();

					final int user_id = users_accepts.get(position-1).getUser_id();
					// 记录下被点击的条目位置
					theitem = position;
					
					tv_dialog.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							if ("已签到".equals(status)) {
								setCheckInStatus(user_id, "decline");
								newStatus = "签到被取消";
								
							} else if ("签到被取消".equals(status)) {
								setCheckInStatus(user_id, "by_user");
								newStatus = "已签到";
								
							}
							else if ("未签到".equals(status)) {
								setCheckInStatus(user_id, "by_admin");
								newStatus = "发起者代签到";
							}
							else if ("发起者代签到".equals(status)) {
								setCheckInStatus(user_id, "no");
								newStatus = "未签到";
							}
							dialog.dismiss();
						}
					});
					
				} else {
//					ToastUtil.show(ApplyPersonActivity.this, position+"被长按了"+"然并卵");
					
				}
				
				return true;
			}
		});
		
		gv_accept_person.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ApplyPersonActivity.this, UserInfoActivity.class);
				intent.putExtra("userid", users_accepts.get(position-1).user_id+"");
				intent.putExtra("isme", false);
				startActivity(intent);
				
			}
		});
		gv_accept_person
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
				initValues();
				new FinishRefresh().execute();
			}
		});
		
	}
	
	/**
	 * 请求网络更改签到状态
	 */
	private void setCheckInStatus(int user_id, String checkin_status) {
		
		progressBar.setVisibility(View.VISIBLE);
		
		// POST 参数
		final Map<String, String> params = new HashMap<String, String>();
		
		params.put("user_id", user_id+"");
		params.put("checkin_status", checkin_status);
	
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.EVENTS_SET_CHECKIN_STATUS_POST + event.getId() + ".json",
						handler, EVENTS_SET_CHECKIN_STATUS_POST, params);
			}
		} );
		
	}
	

	private void initValues() {
		
		accept_count.setText("("+event.getAccept_count()+"/"+(event.getLimitation()==0?"不限":event.getLimitation())+")");
		
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.EVENT_ACCEPT+event.getId()+".json?"+ "page=" + page
						+ "&limit=" + limit, handler,NET_FRIENDLIST);
			}
		});

	}
	
	@SuppressLint("HandlerLeak") 
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_FRIENDLIST:
				progressBar.setVisibility(View.GONE);
				String strObject = (String) msg.obj;
				if(strObject != null){
					try {
						JSONObject json = new JSONObject(strObject);
						int status = json.getInt("status");
						if(status == 200){
 							JSONObject users = json.getJSONObject("users_info");
 							
							List<User> users_accept_list = UsersConstruct.ToUserlist(json.getJSONArray("data"),users);
							
							if (users_accept_list != null && users_accept_list.size() > 0) {
//								accept_count.setText(users_accept_list.size());
								notifilist(users_accept_list);
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
				
			case EVENTS_SET_CHECKIN_STATUS_POST:
				// 更改签到状态
				progressBar.setVisibility(View.GONE);
				try {
					if (msg.obj != null && 200 == ((JSONObject)msg.obj).getInt("status")) {
					
						// 刷新界面
						ToastUtil.show(ApplyPersonActivity.this, "succeed");
						if (theitem != -1) {
							View view = (View) actualListView.getChildAt(theitem);
//							TextView tv_time = (TextView) view.findViewById(R.id.time);
//				 			TextView address = (TextView) view.findViewById(R.id.address);
				 			TextView tv_status = (TextView) view.findViewById(R.id.check_in_status);
				 			
				 			if ("签到被取消".equals(newStatus)) {
								
				 				tv_status.setTextColor(getResources().getColor(color.myred));
							} else if ("已签到".equals(newStatus)) {

								tv_status.setTextColor(getResources().getColor(color.mygreen));
							} else if ("发起者代签到".equals(newStatus)) {
								
								tv_status.setTextColor(getResources().getColor(color.mygreen));
							} else if ("未签到".equals(newStatus)) {
								
								tv_status.setTextColor(getResources().getColor(color.mygray));
							}
				 			tv_status.setText(newStatus);
							
						}
						
					}else {
						ToastUtil.show(ApplyPersonActivity.this, "网络异常, 请重试");
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

				default:
					break;
				
			}
		}
	};
	private LinearLayout ll_share;
	private TextView tv_lab_share;
	
	/**
	 * 刷新列表数据
	 */
	private void notifilist(List<User> users_accept_list) {
		users_accepts.addAll(users_accept_list);
		if(dailadapter!=null){
			dailadapter.notifyDataSetChanged();
		}else{
			 dailadapter = new AcceptPersonAdapter(
						ApplyPersonActivity.this, users_accepts, event.getLatitude(), event.getLongitude());
				gv_accept_person.setAdapter(dailadapter);
		}
		actualListView.setSelection((page - 1) * 15);
		page = page + 1;
		listtoast();
	}
	
	/**
	 * 提示运动记录为空
	 */
	private void listtoast() {
		if (users_accepts != null && users_accepts.size() > 0) {
			gv_accept_person.setVisibility(View.VISIBLE);
			tv_listisnull.setVisibility(View.GONE);
		} else {
			gv_accept_person.setVisibility(View.GONE);
			tv_listisnull.setVisibility(View.VISIBLE);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			
		case R.id.tv_manager:
			
			Builder builder = new Builder(ApplyPersonActivity.this);
			final AlertDialog dialog = builder.create();
			View dialogview = View.inflate(ApplyPersonActivity.this, R.layout.dialog_manager, null);
			
			TextView tv_send_message = (TextView) dialogview.findViewById(R.id.tv_send_message);
			TextView tv_export = (TextView) dialogview.findViewById(R.id.tv_export);
			TextView tv_cancel = (TextView) dialogview.findViewById(R.id.tv_cancel);
			
			tv_send_message.setOnClickListener(this);
			tv_export.setOnClickListener(this);
			tv_cancel.setOnClickListener(this);
			
			dialog.setView(dialogview, 0, 0, 0, 0);
			dialog.show();

			dialog.getWindow().setGravity(Gravity.BOTTOM);

			dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
		
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.width = getWindowManager().getDefaultDisplay().getWidth();
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			dialog.getWindow().setAttributes(lp);

			// 群发消息
			tv_send_message.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent_send = new Intent(ApplyPersonActivity.this, EventSendMessageActivity.class);
					intent_send.putExtra("event", event);
					startActivity(intent_send);
					dialog.dismiss();
				}
			});
			
			// 导出报名者列表
			tv_export.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 导出参与人列表
					Intent intent = new Intent(ApplyPersonActivity.this, ExportAcceptListActivity.class);
					intent.putExtra("event_id", event.get_id());
					intent.putExtra("token", event.getToken());
					startActivity(intent);
					
					dialog.dismiss();
					
				}
			});
			
			// 取消
			tv_cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			
			break;
			
		case R.id.rl_share:
			// 邀请小伙伴
			getPopupWindow(rl_share);
			// 这里是位置显示方式,在屏幕的左侧
			popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			break;
			
			// 分享乐运动好友
		case R.id.iv_share_friend:
			Intent inviteIntent = new Intent(this, InviteFriendActivity.class);
			inviteIntent.putExtra("event", event);
			startActivity(inviteIntent);
			break;
		case R.id.iv_share_weixin:
			share2weixin(0);
			break;
		case R.id.iv_share_weixin_friends:
			share2weixin(1);
			break;
		case R.id.iv_share_qq:
			share2qq();
			break;
			
		case R.id.cancel:
			dismissPopWindow();
			break;

		case R.id.iv_back:
			this.finish();
			break;
		default:
			break;
		}
	}
	
	private void share2weixin(int flag) {
		WxUtil.share2weixin(flag, ApplyPersonActivity.this, Shareurl, "来报名吧-"
				+ event.getTilte(), "icon_index_" + event.sport_eng + "_s",
				event.getIntro());
	}

	private void share2qq() {
		// if (OxygenApplication.tencent.isSessionValid() &&
		// OxygenApplication.tencent.getOpenId() != null) {
		Bundle params = new Bundle();
		// 分享的标题
		params.putString(QQShare.SHARE_TO_QQ_TITLE, "来报名吧-" + event.getTilte());
		// 分享的图片URL
		String imageUrl = Constants.qiuniushare + "icon_index_"
				+ event.sport_eng + "_s.jpg";
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);

		// 用户分享时的评论内容，可由用户输入
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, event.getIntro());
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Shareurl);
		doShareToQQ(params);
	}

	private void doShareToQQ(final Bundle params) {
		// QQ分享要在主线程做
		ThreadManager.getMainHandler().post(new Runnable() {

			@Override
			public void run() {
				if (null != OxygenApplication.tencent) {
					OxygenApplication.tencent.shareToQQ(
							ApplyPersonActivity.this, params, qqShareListener);
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
			ToastUtil.show(ApplyPersonActivity.this, "成功");
		}

		@Override
		public void onError(UiError e) {
			ToastUtil.show(ApplyPersonActivity.this, e.errorMessage);
		}
	};
	
	
	private void initPopViewEvENT() {
		iv_share_friend.setOnClickListener(this);
		iv_share_weixin.setOnClickListener(this);
		iv_share_weixin_friends.setOnClickListener(this);
		iv_share_qq.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}
	
	private void initPopViews(View popupWindow_view) {
		iv_share_friend = (TextView) popupWindow_view
				.findViewById(R.id.iv_share_friend);
		iv_share_weixin = (TextView) popupWindow_view
				.findViewById(R.id.iv_share_weixin);
		iv_share_weixin_friends = (TextView) popupWindow_view
				.findViewById(R.id.iv_share_weixin_friends);
		iv_share_qq = (TextView) popupWindow_view
				.findViewById(R.id.iv_share_qq);
		
		ll_share = (LinearLayout) popupWindow_view.findViewById(R.id.ll_share);
		tv_lab_share = (TextView) popupWindow_view.findViewById(R.id.tv_lab_share);
		cancel = (TextView) popupWindow_view.findViewById(R.id.cancel);
		
	}
	
	/**
	 * 创建PopupWindow
	 */
	protected void initPopuptWindow(View v) {
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(
				R.layout.pop_share, null, false);
		initPopViews(popupWindow_view);
		initPopViewEvENT();

		if (v == rl_share) {
			tv_lab_share.setVisibility(View.VISIBLE);
			ll_share.setVisibility(View.VISIBLE);
		} 

		// 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		// 点击其他地方消失
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				dismissPopWindow();
				
				return false;
			}
		});
	}
	
	protected void dismissPopWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	/***
	 * 获取PopupWindow实例
	 */
	private void getPopupWindow(View v) {
		if (null != popupWindow) {
			popupWindow.dismiss();
			return;
		} else {
			initPopuptWindow(v);
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
			gv_accept_person.onRefreshComplete();
		}
	}
}
