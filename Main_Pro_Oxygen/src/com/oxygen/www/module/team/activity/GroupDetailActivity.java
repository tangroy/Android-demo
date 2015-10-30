package com.oxygen.www.module.team.activity;

import java.util.ArrayList;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.ChallengesUser;
import com.oxygen.www.enties.Event;
import com.oxygen.www.enties.EventFeed;
import com.oxygen.www.enties.Group;
import com.oxygen.www.enties.UserInfo;
import com.oxygen.www.module.challengs.activity.ChallengesDetailActivity;
import com.oxygen.www.module.sport.activity.CreatedPlanActivity;
import com.oxygen.www.module.sport.activity.EventsResultActivity;
import com.oxygen.www.module.sport.adapter.SportDetailAcceptAdapter;
import com.oxygen.www.module.team.adapter.GroupEventsAdapter;
import com.oxygen.www.module.team.construt.GroupsConstruct;
import com.oxygen.www.module.team.eventbus_enties.EventFeedData;
import com.oxygen.www.module.team.eventbus_enties.RefreshTeamFragment;
import com.oxygen.www.module.user.activity.UserInfoActivity;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.utils.WxUtil;
import com.oxygen.www.widget.CircleImageView;
import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import de.greenrobot.event.EventBus;

/**
 * 团队详情页
 * 
 * @author yang 2015-5-21下午4:04:21
 */
public class GroupDetailActivity extends BaseActivity implements
		OnClickListener {
	private ImageView iv_back, iv_share, iv_modify, iv_share_weixin,
			iv_share_qq, iv_share_weixin_friends, iv_group_createvent,
			iv_group_share, iv_sport;
	private CircleImageView iv_head;
	private TextView tv_title, tv_started_at, tv_location, tv_exit,
			disband_team, tv_openQR, tv_intro, tv_member_count,
			tv_events_count;
	private ProgressBar progressbar;
	private LinearLayout ll_comment;
	private EditText et_comment;
	private Button bt_comment;
	private RelativeLayout rl_photos, rl_data, rl_member_count;
	private GridView gv_member;
	private PullToRefreshListView pull_eventsList;
	private int groupid = 0;
	private final int NET_SHOWGROUPS = 1;
	private final int NET_JOINGROUPS = 2;
	private final int NET_EXITGROUPS = 3;
	private final int DISBAND_GROUP = 4;
	/**
	 * 获取弹出金币个数
	 */
	private static final int NET_COIN_CNT = 5;
	static Group group;
	private PopupWindow popupWindow;
	private String Shareurl;
	private String Dataurl;
	private JSONObject jsonobject_userinfo;
	private String jsonString_userinfo;
	private JSONObject current_user;
	private ListView eventsList;
	private ArrayList<EventFeed> feeds;
	private Map<String, UserInfo> usersMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groupdetail);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		pull_eventsList = (PullToRefreshListView) findViewById(R.id.lv_events);
		iv_group_createvent = (ImageView) findViewById(R.id.iv_group_createvent);
		iv_group_share = (ImageView) findViewById(R.id.iv_group_share);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
		et_comment = (EditText) findViewById(R.id.et_comment);
		bt_comment = (Button) findViewById(R.id.bt_comment);
		pull_eventsList.setMode(Mode.MANUAL_REFRESH_ONLY);
		ILoadingLayout endLabels = pull_eventsList.getLoadingLayoutProxy(false,
				true);
		endLabels.setPullLabel("加载更多...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
		eventsList = pull_eventsList.getRefreshableView();
		addHead();

	}

	/**
	 * 添加头部
	 */
	private void addHead() {
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT,
				AbsListView.LayoutParams.WRAP_CONTENT);
		View header = getLayoutInflater().inflate(
				R.layout.activity_groupdatadetail_head, pull_eventsList, false);
		iv_modify = (ImageView) header.findViewById(R.id.iv_modify);
		iv_head = (CircleImageView) header.findViewById(R.id.iv_head);
		tv_title = (TextView) header.findViewById(R.id.tv_title);
		tv_started_at = (TextView) header.findViewById(R.id.tv_started_at);
		tv_location = (TextView) header.findViewById(R.id.tv_location);
		tv_intro = (TextView) header.findViewById(R.id.tv_intro);
		tv_member_count = (TextView) header.findViewById(R.id.tv_member_count);
		tv_events_count = (TextView) header.findViewById(R.id.tv_events_count);
		rl_photos = (RelativeLayout) header.findViewById(R.id.rl_photos);
		gv_member = (GridView) header.findViewById(R.id.gv_member);
		rl_data = (RelativeLayout) header.findViewById(R.id.rl_data);
		rl_member_count = (RelativeLayout) header.findViewById(R.id.rl_member_count);
		iv_sport = (ImageView) header.findViewById(R.id.iv_sport);
		header.setLayoutParams(layoutParams);
		eventsList.addHeaderView(header);
	}

	private void initPopViews(View popupWindow_view) {
		iv_share_weixin = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_weixin);
		iv_share_weixin_friends = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_weixin_friends);
		iv_share_qq = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_qq);
		tv_exit = (TextView) popupWindow_view.findViewById(R.id.tv_exit);
		disband_team = (TextView) popupWindow_view
				.findViewById(R.id.disband_team);
		if ((group.getCreated_by() + "").equals((String) UserInfoUtils
				.getUserInfo(getApplicationContext(), Constants.USERID, ""))) {
			disband_team.setVisibility(View.VISIBLE);
			tv_exit.setVisibility(View.GONE);
		}
		tv_openQR = (TextView) popupWindow_view.findViewById(R.id.tv_openQR);

	}

	private void initPopViewEvENT() {
		iv_share_weixin.setOnClickListener(this);
		iv_share_weixin_friends.setOnClickListener(this);
		iv_share_qq.setOnClickListener(this);
		tv_exit.setOnClickListener(this);
		disband_team.setOnClickListener(this);
		tv_openQR.setOnClickListener(this);

	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		rl_photos.setOnClickListener(this);
		rl_data.setOnClickListener(this);
		iv_modify.setOnClickListener(this);
		rl_member_count.setOnClickListener(this);
		iv_group_createvent.setOnClickListener(this);
		iv_group_share.setOnClickListener(this);
		gv_member.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(GroupDetailActivity.this,
						UserInfoActivity.class);
				intent.putExtra("userid", group.getMembers().get(arg2)
						.getUser_id()
						+ "");
				intent.putExtra("isme", false);
				startActivity(intent);

			}
		});
		eventsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				EventFeed eventFeed = feeds.get(arg2-2);
				String targetId = eventFeed.getTarget_id();
				String targetType = eventFeed.getTarget_type();
				Intent intent = null;
				if("Challenge".equals(targetType)){
					intent = new Intent(GroupDetailActivity.this,
							ChallengesDetailActivity.class);
					intent.putExtra("challengesid", Integer.parseInt(targetId));
				}else if("Event".equals(targetType)){
					intent = new Intent(GroupDetailActivity.this,
							EventsResultActivity.class);
					intent.putExtra("eventid", Integer.parseInt(targetId));
				}
				if(intent != null){
					startActivity(intent);
				}
			}
		});
		
		pull_eventsList.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新
				getgroupsInfoInNet(groupid);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 上拉加载更多
			}
		});
	}

	private void initValues() {
		EventBus.getDefault().register(this);
		groupid = getIntent().getIntExtra("groupid", 0);
		getgroupsInfoInNet(groupid);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		// 修改团队资料
		case R.id.iv_modify:
			Intent updateTeamIntent = new Intent(this,
					CompleteTeamInfoActivity.class);
			updateTeamIntent.putExtra("group", group);
			startActivity(updateTeamIntent);
			break;
		case R.id.iv_share:
			if (group != null) {
				getPopupWindow(v);
				popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}
			break;
		case R.id.tv_exit:
			// 退出团队
			ExitGroup();
			break;
		case R.id.disband_team:
			// 解散团队
			disbandGroup();
			break;
		case R.id.tv_openQR:
			Intent intent_qr = new Intent();
			intent_qr.setClass(GroupDetailActivity.this, TeamQRActivity.class);
			Bundle b = new Bundle();
			b.putString("title", "团队二维码");
			b.putString("shareurl", Shareurl);
			b.putString("name", group.getName());
			b.putString("pic", group.getPic());
			b.putString("description", "成员：" + group.getMembers().size() + "人");
			intent_qr.putExtra("data", b);
			startActivity(intent_qr);
			break;
		case R.id.rl_data:
			Intent dataIntent = new Intent(this, GroupDataDetailActivity.class);
			dataIntent.putExtra("Dataurl", Dataurl);
			startActivity(dataIntent);
			break;
		case R.id.rl_photos:
			Intent photos_intent = new Intent(this, GroupPhotosActivity.class);
			photos_intent.putExtra("group", group);
			startActivity(photos_intent);
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
		case R.id.rl_member_count:
			// 跳转到排名人员列表
			Intent member_intent = new Intent();
			member_intent.putExtra("group", group);
			member_intent.setClass(GroupDetailActivity.this,
					TeamMemberManagerActivity.class);
			member_intent.putExtra("users", jsonobject_userinfo.toString());
			startActivity(member_intent);
			break;
		case R.id.iv_group_createvent:
			if (group != null && group.getCurrent_group_user() == null) {
				// 加入团队
				JoinGroup();
			} else if (group != null && group.getCurrent_group_user() != null) {
				Intent intent = new Intent();
				intent.setClass(GroupDetailActivity.this,
						CreatedPlanActivity.class);
				intent.putExtra("sport", group.getSport());
				intent.putExtra("type", Constants.SPORTTYPE_CREATED);
				intent.putExtra("group", group);
				startActivity(intent);
			}
			break;
		case R.id.iv_group_share:
			if (group != null) {
				getPopupWindow(v);
				// 这里是位置显示方式,在屏幕的左侧
				popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 获取服务器groups
	 */
	private void getgroupsInfoInNet(final int groups) {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.GROUPS_SHOW_GET + groups + ".json?list=feeds",
						handler, NET_SHOWGROUPS);
			}
		});

	}

	/**
	 * 加入团队
	 * 
	 */
	private void JoinGroup() {
		progressbar.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.GROUP_JOIN_GET + groupid + ".json",
						handler, NET_JOINGROUPS);
			}
		});

	}

	/**
	 * 退出团队
	 * 
	 * @param
	 */
	private void ExitGroup() {
		progressbar.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.GROUP_EXIT_GET + groupid + ".json",
						handler, NET_EXITGROUPS);
			}
		});

	}

	/**
	 * 解散团队
	 * 
	 * @param
	 */
	private void disbandGroup() {
		progressbar.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.DISBAND_GROUP + groupid + ".json",
						handler, DISBAND_GROUP);
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
			case NET_SHOWGROUPS:
				progressbar.setVisibility(View.GONE);
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsoninfos = new JSONObject(strObject);
						if (jsoninfos.getInt("status") == 200) {
							jsonobject_userinfo = jsoninfos
									.getJSONObject("users_info");
							jsonString_userinfo = jsoninfos
									.getString("users_info");
							Gson gson = new Gson();
							usersMap = gson.fromJson(jsonString_userinfo, new TypeToken<Map<String,UserInfo>>(){}.getType());
							current_user = jsoninfos
									.getJSONObject("current_user");
							group = GroupsConstruct.ToGroup(jsoninfos
									.getJSONObject("data"));
							feeds = group.getFeeds();
							if (group != null) {
								UpdateUi();
							}
						} else {
							ToastUtil.show(GroupDetailActivity.this, "请求异常");

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(GroupDetailActivity.this, "网络连接不可用，请稍后重试");

				}
				break;
			case NET_JOINGROUPS:
				progressbar.setVisibility(View.GONE);
				String strObject1 = (String) msg.obj;
				if (strObject1 != null && strObject1.length() > 10) {

					try {
						JSONObject jsoninfos = new JSONObject(strObject1);
						if (jsoninfos.getInt("status") == 200) {
							getgroupsInfoInNet(groupid);
							EventBus.getDefault().post(
									new RefreshTeamFragment());
							if (!jsoninfos.isNull("user_action_id")) {

								int userActionId = jsoninfos
										.getInt("user_action_id");
								getToastContent(userActionId);
							}

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					ToastUtil.show(GroupDetailActivity.this, "团队加入失败,请重试");
				}
				break;
			case NET_EXITGROUPS:
				progressbar.setVisibility(View.GONE);
				String strObject2 = (String) msg.obj;
				if (strObject2 != null && strObject2.length() > 10) {

					try {
						JSONObject jsoninfos = new JSONObject(strObject2);
						if (jsoninfos.getInt("status") == 200) {
							finish();
							EventBus.getDefault().post(
									new RefreshTeamFragment());
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					ToastUtil.show(GroupDetailActivity.this, "团队退出失败,请重试");
				}
				break;
			case DISBAND_GROUP:
				progressbar.setVisibility(View.GONE);
				String strObject3 = (String) msg.obj;
				if (strObject3 != null && strObject3.length() > 10) {

					try {
						JSONObject jsoninfos = new JSONObject(strObject3);
						if (jsoninfos.getInt("status") == 200) {
							ToastUtil.show(GroupDetailActivity.this, "解散团队成功");
							EventBus.getDefault().post(
									new RefreshTeamFragment());
							finish();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					ToastUtil.show(GroupDetailActivity.this, "解散团队失败,请重试");
				}
				break;

			case NET_COIN_CNT:
				// 弹出金币提示
				if (msg.obj == null) {
					ToastUtil.show(GroupDetailActivity.this, "网络连接不可用，请稍后重试");
				} else {

					// Log.i("Coin", msg.obj.toString());

					try {
						JSONObject jsonobeObject = new JSONObject(
								(String) msg.obj);
						if (jsonobeObject.getInt("status") == 200) {
							JSONObject data = jsonobeObject
									.getJSONObject("data");
							String content = data.getString("alert");
							int coins = data.getInt("coins");
							ToastUtil.showCoin(GroupDetailActivity.this,
									content + " +" + coins + " 金币!");

						} else {
							ToastUtil.show(GroupDetailActivity.this,
									"网络连接不可用，请稍后重试");
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
	 * 获取弹金币吐司内容
	 */
	private void getToastContent(final int user_action_id) {

		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.OPERATIONS_ACTION_REWARD
						+ user_action_id + ".json", handler, NET_COIN_CNT);
			}
		});

	}

	private void UpdateUi() {
		if (group.getCurrent_group_user() == null) {
			iv_group_createvent.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_team_add));
		} else {
			iv_group_createvent.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_team_createvent));
			if (group.getCurrent_group_user().getRole().equals("admin")) {
				iv_modify.setVisibility(View.VISIBLE);
			} else {
				iv_modify.setVisibility(View.GONE);
			}
		}

		// 分享链接
		Shareurl = UrlConstants.GROUPS_H5 + groupid + "?token="
				+ group.getToken();
		Dataurl = UrlConstants.GROUPS_DATA + groupid + "?token="
				+ group.getToken();
		ImageUtil.showImage(group.getPic(), iv_head, R.drawable.icon_def);
		tv_started_at.setText(group.getName());
		iv_sport.setImageDrawable(GDUtil.engSporttodrawable(
				GroupDetailActivity.this, "icon_index_" + group.getSport()));
		tv_location.setText("ID: " + groupid);
		if (group.getIntro() == null || group.getIntro().length() < 2) {
			tv_intro.setText("暂无介绍");
		} else {
			tv_intro.setText(group.getIntro());
		}
		int member_count = group.getMembers().size();
		tv_member_count.setText("成员(" + member_count + ")");
		String userid[] = new String[member_count];
		if (member_count > 0) {
			for (int i = 0; i < member_count; i++) {
				userid[i] = group.getMembers().get(i).getUser_id() + "";
			}
			SportDetailAcceptAdapter dailadapter = new SportDetailAcceptAdapter(
					true, GroupDetailActivity.this, userid,
					jsonobject_userinfo, member_count > 5 ? 5 : member_count);
			gv_member.setAdapter(dailadapter);
		}
		event_adapter = new GroupEventsAdapter(
				GroupDetailActivity.this, feeds,usersMap, current_user,ll_comment, et_comment, bt_comment);
		eventsList.setAdapter(event_adapter);

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

	/**
	 * 创建PopupWindow
	 */
	protected void initPopuptWindow(View v) {
		// TODO Auto-generated method stub
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(
				R.layout.pop_groupdetail, null, false);
		initPopViews(popupWindow_view);
		initPopViewEvENT();
		if (v == iv_group_share) {
			tv_exit.setVisibility(View.GONE);
			tv_openQR.setVisibility(View.GONE);
		} else {
			if (group.getCurrent_group_user() == null) {
				tv_exit.setVisibility(View.GONE);
			} else if (group.getCurrent_group_user().getRole().equals("admin")) {
				tv_exit.setVisibility(View.GONE);
			} else {
				tv_exit.setVisibility(View.VISIBLE);
			}
			tv_openQR.setVisibility(View.VISIBLE);
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
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
	}

	private void share2weixin(int flag) {
		WxUtil.share2weixin(
				flag,
				GroupDetailActivity.this,
				Shareurl,
				UserInfoUtils.getUserInfo(getApplicationContext(),
						Constants.NICKNAME, "") + "邀请你加入" + group.getName(),
				group.getPic(), group.getIntro());
	}

	private void share2qq() {
		// if (OxygenApplication.tencent.isSessionValid() &&
		// OxygenApplication.tencent.getOpenId() != null) {
		Bundle params = new Bundle();
		// 分享的标题
		params.putString(
				QQShare.SHARE_TO_QQ_TITLE,
				UserInfoUtils.getUserInfo(getApplicationContext(),
						Constants.NICKNAME, "") + "邀请你加入-" + group.getName());
		// 分享的图片URL
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, group.getPic());

		// 用户分享时的评论内容，可由用户输入
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, group.getIntro());
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
							GroupDetailActivity.this, params, qqShareListener);
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
			ToastUtil.show(GroupDetailActivity.this, "成功");
		}

		@Override
		public void onError(UiError e) {
			ToastUtil.show(GroupDetailActivity.this, e.errorMessage);
		}
	};
	private GroupEventsAdapter event_adapter;

	private void onEventMainThread(Group msg) {
		tv_intro.setText(msg.getIntro());
		tv_started_at.setText(msg.getName());
		ImageUtil.showImage2(msg.getPic(), iv_head, R.drawable.icon_def);
		iv_sport.setImageDrawable(GDUtil.engSporttodrawable(
				GroupDetailActivity.this, "icon_index_" + msg.getSport()));
		group.setIntro(msg.getIntro());
		group.setName(msg.getName());
		group.setStarted_at(msg.getStarted_at());
		group.setAddress(msg.getAddress());

	}

	private void onEventMainThread(ArrayList<ChallengesUser> members) {
		group.setMembers(members);
		int member_count = group.getMembers().size();
		tv_member_count.setText("成员(" + member_count + ")");
		String userid[] = new String[member_count];
		if (member_count > 0) {
			for (int i = 0; i < member_count; i++) {
				userid[i] = group.getMembers().get(i).getUser_id() + "";
			}
			SportDetailAcceptAdapter dailadapter = new SportDetailAcceptAdapter(
					true, GroupDetailActivity.this, userid,
					jsonobject_userinfo, member_count > 5 ? 5 : member_count);
			gv_member.setAdapter(dailadapter);
		}
	}
	
	/**
	 * 修改 mList 数据
	 * 
	 * @param msg
	 */
	public void onEventMainThread(EventFeedData msg) {
		feeds = msg.getList();
		usersMap = msg.getUsersInfo();
		event_adapter.notifyDataSetChanged();
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
