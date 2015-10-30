package com.oxygen.www.module.user.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.module.user.adapter.FriendsAdapter;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.utils.WxUtil;
import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * 添加好友界面
 * @author 杨庆雷 2015-6-8上午11:54:05
 */
public class AddFriendActivity extends BaseActivity implements OnClickListener {
	protected   final int NET_FRIENDLIST = 1;
	protected   final int SEARCH_FRIEND = 2;
	private ImageView iv_back;
	private ProgressBar progressBar;
	private EditText search_people_name;
	private TextView invite_qqfriend;
	private TextView invite_wxfriend;
	private TextView invite_wxfriends;
	private ListView lv_know_people;
	private TextView tv_search_cancel;
	private TextView tv_know_people;
	private ImageView iv_search_cancel;
	private LinearLayout ll_invite_friends;
	private String shareurl;
	private List<User> users;
	private String searchName;
	private FriendsAdapter fadapter;
	private List<User> allUsers = new ArrayList<User>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addfriend);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		final Intent friendIntent = new Intent(this, UserInfoActivity.class);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		search_people_name = (EditText) findViewById(R.id.search_people_name);
		invite_qqfriend = (TextView) findViewById(R.id.invite_qqfriend);
		invite_wxfriend = (TextView) findViewById(R.id.invite_wxfriend);
		invite_wxfriends = (TextView) findViewById(R.id.invite_wxfriends);
		tv_search_cancel = (TextView) findViewById(R.id.tv_search_cancel);
		tv_know_people = (TextView) findViewById(R.id.tv_know_people);
		lv_know_people = (ListView) findViewById(R.id.lv_know_people);
		iv_search_cancel = (ImageView) findViewById(R.id.iv_search_cancel);
		ll_invite_friends = (LinearLayout) findViewById(R.id.ll_invite_friends);
		lv_know_people.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				if (allUsers != null) {
					friendIntent.putExtra("userid", allUsers.get(position).getId()
							+ "");
//				}else if(allSearchUsers != null){
//					friendIntent.putExtra("userid", searchUser.get(position).getId()
//							+ "");
//				}
				startActivity(friendIntent);
			}
		});
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		search_people_name.setImeOptions(EditorInfo.IME_ACTION_GO);
		invite_qqfriend.setOnClickListener(this);
		invite_wxfriend.setOnClickListener(this);
		invite_wxfriends.setOnClickListener(this);
		tv_search_cancel.setOnClickListener(this);
		iv_search_cancel.setOnClickListener(this);
		search_people_name.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					InputMethodManager imm = (InputMethodManager) v
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					if (imm.isActive()) {
						imm.hideSoftInputFromWindow(
								v.getApplicationWindowToken(), 0);
						searchName = search_people_name.getText().toString()
								.trim();
						tv_search_cancel.setVisibility(View.VISIBLE);
						iv_search_cancel.setVisibility(View.VISIBLE);
						ll_invite_friends.setVisibility(View.GONE);
						tv_know_people.setVisibility(View.GONE);
						lv_know_people.setVisibility(View.GONE);
						progressBar.setVisibility(View.VISIBLE);
						searchPersonFromNet();
					}
				}
				return false;
			}
		});
	}

	/**
	 * 搜索好友
	 */
	protected void searchPersonFromNet() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				try {
					HttpUtil.Get(UrlConstants.SEARCH_FRIEND + "keyword="
							+ URLEncoder.encode(searchName, "utf-8") + "&page="
							+ 1 + "&limit=" + 1000, handler, SEARCH_FRIEND);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void initValues() {
		getFriendSuggest();
	}

	/**
	 * 可能认识的人(推荐好友)
	 */
	private void getFriendSuggest() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				HttpUtil.Get(UrlConstants.FRIEND_SUGGEST, handler,
						NET_FRIENDLIST);
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.invite_qqfriend:
			share2qq();
			break;
		case R.id.invite_wxfriend:
			share2weixin(0);
			break;
		case R.id.invite_wxfriends:
			share2weixin(1);
			break;
		case R.id.tv_search_cancel:
			ll_invite_friends.setVisibility(View.VISIBLE);
			tv_know_people.setVisibility(View.VISIBLE);
			tv_search_cancel.setVisibility(View.GONE);
			iv_search_cancel.setVisibility(View.GONE);
			UpdateUI(new ArrayList<User>());
			break;
		case R.id.iv_search_cancel:
			search_people_name.setText("");
			break;

		default:
			break;
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_FRIENDLIST:
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject friendSuggestList = new JSONObject(strObject);
						if (friendSuggestList.getInt("status") == 200) {
							JSONArray friendsSuggest = friendSuggestList
									.getJSONArray("data");
							if (friendsSuggest != null&& friendsSuggest.length() > 0) {
								users = UsersConstruct.ToUserlist(friendsSuggest);
							}
							UpdateUI(users);
						} else {
							ToastUtil.show(AddFriendActivity.this,
									friendSuggestList.getString("msg"));
							progressBar.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						progressBar.setVisibility(View.GONE);
						e.printStackTrace();
					}
				} else {
					progressBar.setVisibility(View.GONE);
				}
				break;
			case SEARCH_FRIEND:
				String strSearchObject = (String) msg.obj;
				if (strSearchObject != null && strSearchObject.length() > 10) {
					try {
						JSONObject friendSearchList = new JSONObject(strSearchObject);
						if (friendSearchList.getInt("status") == 200) {
							JSONArray friendsSaerch = friendSearchList
									.getJSONArray("data");
							if (friendsSaerch != null&& friendsSaerch.length() > 0) {
								users = UsersConstruct.ToUserlist(friendsSaerch);
							}
							UpdateSearchUI(users);
						} else {
							ToastUtil.show(AddFriendActivity.this,friendSearchList.getString("msg"));
							progressBar.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						progressBar.setVisibility(View.GONE);
						e.printStackTrace();
					}
				} else {
					progressBar.setVisibility(View.GONE);
				}
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 获取可能认识的人信息后 加载列表信息
	 * 
	 * @param jsonfriendlist
	 */
	private void UpdateUI(List<User> users) {
		if(users != null){
			allUsers.addAll(users);
		}
		if (fadapter != null) {
			fadapter.notifyDataSetChanged();
		} else {
			fadapter = new FriendsAdapter(AddFriendActivity.this, allUsers,true);
			lv_know_people.setAdapter(fadapter);
		}
		lv_know_people.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
	}
	
	/**
	 * 搜索用户
	 */
	private void UpdateSearchUI(List<User> searchUsers) {
		allUsers.clear();
		if(searchUsers != null){
			allUsers.addAll(searchUsers);
		}
//		if(users != null){
//			allSearchUsers.addAll(users);
//		}
//		if (fadapter != null) {
//			fadapter.notifyDataSetChanged();
//		} else {
			fadapter = new FriendsAdapter(AddFriendActivity.this, allUsers,true);
			lv_know_people.setAdapter(fadapter);
//		}
		lv_know_people.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
	}

	/**
	 * 邀请微信好友
	 * 
	 * @param flag
	 */
	private void share2weixin(int flag) {
		shareurl = UrlConstants.INVITE_FRIEND
				+ (String)UserInfoUtils.getUserInfo(getApplicationContext(),Constants.USERID, "") + "?token="
				+ (String)UserInfoUtils.getUserInfo(getApplicationContext(),Constants.TOKEN, "");
		WxUtil.share2weixin(flag, AddFriendActivity.this, shareurl,
				(String)UserInfoUtils.getUserInfo(getApplicationContext(), Constants.NICKNAME, "") + "的好友邀请名片",
				(String)UserInfoUtils.getUserInfo(getApplicationContext(), Constants.HEADIMG_URL, ""), 
				"快来成为我的乐运动好友，我们一起运动起来吧!");
	}

	/**
	 * 邀请QQ好友
	 */
	private void share2qq() {
		// if (OxygenApplication.tencent.isSessionValid() &&
		// OxygenApplication.tencent.getOpenId() != null) {
		Bundle params = new Bundle();
		// 分享卡的链接
		shareurl = UrlConstants.INVITE_FRIEND
				+ (String)UserInfoUtils.getUserInfo(getApplicationContext(),Constants.USERID, "") + "?token="
				+ (String)UserInfoUtils.getUserInfo(getApplicationContext(),Constants.TOKEN, "");
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareurl);
		// 分享的标题
		params.putString(QQShare.SHARE_TO_QQ_TITLE,
				(String)UserInfoUtils.getUserInfo(getApplicationContext(), Constants.NICKNAME, "") + "的好友邀请名片");
		// 分享的图片URL
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
				(String)UserInfoUtils.getUserInfo(getApplicationContext(), Constants.HEADIMG_URL, ""));

		// 用户分享时的评论内容，可由用户输入
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "快来成为我的乐运动好友，我们一起运动起来吧!");
		doShareToQQ(params);
	}

	private void doShareToQQ(final Bundle params) {
		// QQ分享要在主线程做
		ThreadManager.getMainHandler().post(new Runnable() {

			@Override
			public void run() {
				if (null != OxygenApplication.tencent) {
					OxygenApplication.tencent.shareToQQ(AddFriendActivity.this,
							params, qqShareListener);
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
			ToastUtil.show(AddFriendActivity.this, "成功");
		}

		@Override
		public void onError(UiError e) {
			ToastUtil.show(AddFriendActivity.this, e.errorMessage);
		}
	};
}
