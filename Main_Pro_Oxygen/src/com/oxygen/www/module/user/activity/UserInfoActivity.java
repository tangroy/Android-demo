package com.oxygen.www.module.user.activity;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.module.team.activity.TeamListActivity;
import com.oxygen.www.module.user.eventbus_entities.RefreshFriendsList;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.widget.CircleImageView;

import de.greenrobot.event.EventBus;

/**
 * 个人资料界面
 * 
 * @author 杨庆雷 2015-6-24上午11:09:28
 */
public class UserInfoActivity extends BaseActivity implements OnClickListener {
	protected final int NET_USER_SHOW = 1;
	protected final int ADD_FRIEND = 2;
	protected final int REMOVE_FRIEND = 3;
	protected final int ADD_FRIEND_ACCEPT = 4;
	private ImageView iv_back;
	private TextView tv_name;
	private ImageView modify_user_info;
	private String nickname;
	private String userId;
	private String relationship;
	private String headImgUrl;
	private String sex;
	private String age;
	private String intro;
	private String sport;
	private String calorie;
	private String duration;
	private int eventCounts;
	private int friendCounts;
	private int groupCounts;
	private CircleImageView user_headimg;
	private ImageView user_sex;
	private TextView user_age;
	private LinearLayout ll_sports;
	private RelativeLayout rl_level_coin;
	private RelativeLayout rl_add_friend;
	private ImageView user_addfriend;
	private TextView user_invalidation;
	private TextView tv_level;
	private TextView tv_coin;
	private TextView accept_addfriend;
	private RelativeLayout user_friends;
	private RelativeLayout user_groups;
	private TextView user_friends_count;
	private TextView user_group_count;
	private TextView add_event_count;
	private TextView sport_time_hour;
	private TextView sport_time_minute;
	private TextView tv_calorie;
	private LinearLayout user_friend_group;
	private User user;
	private Intent infoIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);
		initViews();
		initViewsEvent();
		initValues();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		initValues();
	}

	private void initViews() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			userId = getIntent().getExtras().getString("userid");
		} else {
			userId = (String)UserInfoUtils.getUserInfo(getApplicationContext(),Constants.USERID, "");
		}
		modify_user_info = (ImageView) findViewById(R.id.modify_user_info);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_name = (TextView) findViewById(R.id.tv_name);
		user_headimg = (CircleImageView) findViewById(R.id.user_headimg);
		user_sex = (ImageView) findViewById(R.id.user_sex);
		user_age = (TextView) findViewById(R.id.user_age);
		ll_sports = (LinearLayout) findViewById(R.id.ll_sports);
		rl_level_coin = (RelativeLayout) findViewById(R.id.rl_level_coin);
		rl_add_friend = (RelativeLayout) findViewById(R.id.rl_add_friend);
		tv_level = (TextView) findViewById(R.id.tv_level);
		tv_coin = (TextView) findViewById(R.id.tv_coin);
		
		user_addfriend = (ImageView) findViewById(R.id.user_addfriend);
		user_invalidation = (TextView) findViewById(R.id.user_invalidation);
		accept_addfriend = (TextView) findViewById(R.id.accept_addfriend);
		user_friends = (RelativeLayout) findViewById(R.id.user_friends);
		user_groups = (RelativeLayout) findViewById(R.id.user_groups);
		user_friends_count = (TextView) findViewById(R.id.user_friends_count);
		user_group_count = (TextView) findViewById(R.id.user_group_count);
		add_event_count = (TextView) findViewById(R.id.add_event_count);
		sport_time_hour = (TextView) findViewById(R.id.sport_time_hour);
		sport_time_minute = (TextView) findViewById(R.id.sport_time_minute);
		tv_calorie = (TextView) findViewById(R.id.tv_calorie);
		user_friend_group = (LinearLayout) findViewById(R.id.user_friend_group);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		modify_user_info.setOnClickListener(this);
		user_addfriend.setOnClickListener(this);
		user_friends.setOnClickListener(this);
		user_groups.setOnClickListener(this);
		accept_addfriend.setOnClickListener(this);
		rl_level_coin.setOnClickListener(this);
	}

	private void initValues() {
		getUserInfoFromNet();
	}

	/**
	 * 获取个人资料信息
	 */
	private void getUserInfoFromNet() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.USERS_SHOW_URL + userId + ".json",
						handler, NET_USER_SHOW);
			}
		});
	}

	/**
	 * 解析用户信息
	 */
	private void getUserInfo() {
		relationship = user.getNew_relationship();
		if ("myself".equals(relationship)) {
			rl_level_coin.setClickable(true);
			rl_add_friend.setVisibility(View.GONE);
			modify_user_info.setVisibility(View.VISIBLE);
			
			UserInfoUtils.setUserInfo(getApplicationContext(),Constants.LEVEL, user.level);
			UserInfoUtils.setUserInfo(getApplicationContext(),Constants.COINS, user.coins);
			UserInfoUtils.setUserInfo(getApplicationContext(), Constants.POINTS,user.points);
		} else if ("stranger".equals(relationship)) {
			rl_add_friend.setVisibility(View.VISIBLE);
			user_addfriend.setVisibility(View.VISIBLE);
			modify_user_info.setVisibility(View.GONE);
			Drawable drawable = getResources().getDrawable(R.drawable.icon_goldcoin);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
			tv_coin.setCompoundDrawables(drawable, null, null, null);
			rl_level_coin.setClickable(false);
		} else if ("pending".equals(relationship)) {
			rl_add_friend.setVisibility(View.VISIBLE);
			user_invalidation.setVisibility(View.VISIBLE);
			Drawable drawable = getResources().getDrawable(R.drawable.icon_goldcoin);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
			tv_coin.setCompoundDrawables(drawable, null, null, null);
			rl_level_coin.setClickable(false);
		} else if ("friend".equals(relationship)) {
			modify_user_info.setVisibility(View.VISIBLE);
			Drawable drawable = getResources().getDrawable(R.drawable.icon_goldcoin);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
			tv_coin.setCompoundDrawables(drawable, null, null, null);
			rl_level_coin.setClickable(false);
			// 1 代表小乐助手  则不显示好友和团队
			if("1".equals(userId)){
				user_friend_group.setVisibility(View.GONE);
				modify_user_info.setVisibility(View.GONE);
			//不是自己时修改右上角按钮的样式
			}else if(!((String)UserInfoUtils.getUserInfo(getApplicationContext(),Constants.USERID, "")).equals(userId)){
				modify_user_info.setImageResource(R.drawable.icon_eventdetail_rankmore);
				modify_user_info.setVisibility(View.VISIBLE);
			}else{
				modify_user_info.setVisibility(View.VISIBLE);
			}
			
		} else if ("tbc".equals(relationship)) {// 别人请求添加我为好友 等待我确认
			rl_add_friend.setVisibility(View.VISIBLE);
			accept_addfriend.setVisibility(View.VISIBLE);
			Drawable drawable = getResources().getDrawable(R.drawable.icon_goldcoin);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
			tv_coin.setCompoundDrawables(drawable, null, null, null);
			rl_level_coin.setClickable(false);
		}
		headImgUrl = user.getHeadimgurl();
		nickname = user.getNickname();
		sex = user.getSex() + "";
		age = user.getAge() + "";
		intro = user.getIntro();
		sport = user.getSports(); 
		eventCounts = user.getEvent_count();
		friendCounts = user.getFriend_count();
		groupCounts = user.getGroup_count();
		calorie = user.getTotal_calorie();
		duration = user.getTotal_duration();
		setUserInfo();
	}

	/**
	 * 设置用户的信息
	 */
	private void setUserInfo() {
		tv_name.setText(nickname);
		ImageUtil.showImage(headImgUrl, user_headimg,
				R.drawable.icon_default_head);
		if ("1".equals(sex)) {
			user_sex.setBackgroundResource(R.drawable.user_sex_man);
		} else if ("2".equals(sex)) {
			user_sex.setBackgroundResource(R.drawable.user_sex_woman);
		}
		user_age.setText(age);
		user_friends_count.setText("( " + friendCounts + " )");
		user_group_count.setText("( " + groupCounts + " )");
		add_event_count.setText(eventCounts + "");
		tv_level.setText("LV"+user.getLevel());
		tv_coin.setText("  "+user.getCoins()+"  ");
		int hourIndex = 0;
		int minuteIndex = 0;
		if (duration.contains("小时")) {
			hourIndex = duration.indexOf("小时");
			sport_time_hour.setText(duration.substring(0, hourIndex));
			minuteIndex = duration.indexOf("分钟");
			if (minuteIndex != -1) {
				sport_time_minute.setText(duration.substring(hourIndex + 2,
						minuteIndex));
			} else {
				sport_time_minute.setText("0");
			}
		} else if (duration.contains("分钟")) {
			minuteIndex = duration.indexOf("分钟");
			sport_time_hour.setText("0");
			sport_time_minute.setText(duration.substring(0, minuteIndex));
		} else {
			sport_time_hour.setText("0");
			sport_time_minute.setText("0");
		}
		tv_calorie.setText(calorie.replace("大卡", ""));
		setUserSports();
	}

	/**
	 * 兴趣运动
	 */
	private void setUserSports() {
		if (!TextUtils.isEmpty(user.getSports())) {
			ll_sports.removeAllViews();
			String[] picNames = user.getSports().split(",");
			for (int i = 0; i < picNames.length; i++) {
				if (i < 5) {
					ImageView imageView = new ImageView(this); 
					imageView.setLayoutParams(new LinearLayout.LayoutParams((int)(20 * OxygenApplication.ppi), (int)(20*OxygenApplication.ppi)));
					imageView
							.setBackgroundDrawable(GDUtil.engSporttodrawable(
									this,
									"icon_index_"
											+ picNames[i].replace(" ", "")));
					ll_sports.addView(imageView);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.modify_user_info:
			if (!((String)UserInfoUtils.getUserInfo(getApplicationContext(),Constants.USERID, "")).equals(userId)) {
				showDeleteFriendDialog();
			} else {
				infoIntent = new Intent(this, DataActivity.class);
				infoIntent.putExtra("isFromUserInfoActivity", true);
				startActivity(infoIntent);
			}
			break;
		case R.id.user_addfriend:
			addFriend();
			break;

		case R.id.user_friends:
			Intent friendsIntent = new Intent(this, FriendsActivity.class);
			// friendsIntent.putExtra("user", user);
			friendsIntent.putExtra("userId", user.getId() + "");
			friendsIntent.putExtra("userName", user.getNickname());
			friendsIntent.putExtra("isSelf", false);
			startActivity(friendsIntent);
			break;
		case R.id.user_groups:
			Intent groupsIntent = new Intent(this, TeamListActivity.class);
			groupsIntent.putExtra("userId", user.getId() + "");
			groupsIntent.putExtra("userName", user.getNickname());
			startActivity(groupsIntent);
			break;
		case R.id.accept_addfriend:
			// 接受添加好友
			OxygenApplication.cachedThreadPool.execute(new Runnable() {
				public void run() {
					HttpUtil.Get(UrlConstants.ADD_FRIEND_ACCEPT + userId
							+ ".json", handler, ADD_FRIEND_ACCEPT);
				}
			});
			break;
		case R.id.rl_level_coin:
			Intent levelIntent = new Intent(this,LevelCoinActivity.class);
			startActivity(levelIntent);
			break;
		default:
			break;
		}
	}

	/**
	 * 删除好友弹框(下部)
	 */
	private void showDeleteFriendDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.dialog_delete_friend, null);
		// 对话框
		final Dialog dialog = new AlertDialog.Builder(UserInfoActivity.this)
				.create();
		dialog.show();
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		dialog.getWindow().setContentView(layout);
		TextView delete_friend = (TextView) layout
				.findViewById(R.id.delete_friend);
		TextView cancel = (TextView) layout.findViewById(R.id.cancel);
		delete_friend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				showDeleteFriendDialog2();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * 删除好友对话框(中部)
	 */
	private void showDeleteFriendDialog2() {
		LayoutInflater inflater = LayoutInflater.from(this);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.dialog_invite, null);
		// 对话框
		final Dialog dialog = new AlertDialog.Builder(UserInfoActivity.this)
				.create();
		dialog.show();
		dialog.getWindow().setContentView(layout);
		TextView dialog_info = (TextView) layout.findViewById(R.id.dialog_info);
		TextView invite_cancel = (TextView) layout
				.findViewById(R.id.invite_cancel);
		TextView invite_sure = (TextView) layout.findViewById(R.id.invite_sure);
		dialog_info.setText("你真的要狠心删除Ta吗?");
		invite_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		invite_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				deleteFriend();
			}
		});
	}

	/**
	 * 删除好友操作
	 */
	protected void deleteFriend() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.REMOVE_FRIEND + userId + ".json",
						handler, REMOVE_FRIEND);
			}
		});
	}

	/**
	 * 添加好友
	 */
	private void addFriend() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.ADD_FRIEND + userId + ".json",
						handler, ADD_FRIEND);
			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_USER_SHOW:
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsonenlist = new JSONObject(strObject);
						if (jsonenlist.getInt("status") == 200) {
							user = UsersConstruct.ToUser(jsonenlist
									.getJSONObject("data"));
							if (user != null) {
								getUserInfo();
							}
						} else {
							ToastUtil.show(UserInfoActivity.this,
									jsonenlist.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(UserInfoActivity.this, "网络连接不可用，请稍后重试");
				}
				break;
			case REMOVE_FRIEND:
				String removeInfo = (String) msg.obj;
				if (removeInfo != null) {
					try {
						JSONObject jsonenlist = new JSONObject(removeInfo);
						if (jsonenlist.getInt("status") == 200) {
							ToastUtil.show(UserInfoActivity.this, "删除好友成功");
							UserInfoActivity.this.finish();
							EventBus.getDefault()
									.post(new RefreshFriendsList());
							// rl_add_friend.setVisibility(View.VISIBLE);
							// user_addfriend.setVisibility(View.VISIBLE);
						} else {
							ToastUtil.show(UserInfoActivity.this, "删除好友失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(UserInfoActivity.this, "网络连接不可用，请稍后重试");
				}
				break;
			case ADD_FRIEND:
				String addInfo = (String) msg.obj;
				if (addInfo != null) {
					try {
						JSONObject jsonenlist = new JSONObject(addInfo);
						if (jsonenlist.getInt("status") == 200) {
							ToastUtil.show(UserInfoActivity.this, "加好友请求已发送");
							user_addfriend.setVisibility(View.GONE);
							user_invalidation.setVisibility(View.VISIBLE);
						} else {
							ToastUtil
									.show(UserInfoActivity.this, "加好友失败或已添加好友");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(UserInfoActivity.this, "网络连接不可用，请稍后重试");
				}
				break;
			case ADD_FRIEND_ACCEPT:
				String acceptAddInfo = (String) msg.obj;
				if (acceptAddInfo != null) {
					try {
						JSONObject jsonenlist = new JSONObject(acceptAddInfo);
						if (jsonenlist.getInt("status") == 200) {
							ToastUtil.show(UserInfoActivity.this, "接受添加好友成功");
							rl_add_friend.setVisibility(View.GONE);
							modify_user_info.setVisibility(View.VISIBLE);
						} else {
							ToastUtil.show(UserInfoActivity.this, "接受添加好友失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(UserInfoActivity.this, "网络连接不可用，请稍后重试");
				}
				break;
			default:
				break;
			}
		}

	};

}
