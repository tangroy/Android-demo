package com.oxygen.www.module.user.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.BaseViewHolder;
import com.oxygen.www.base.MyBaseAdapter;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Event;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;


/**
 * 运动详情页  邀请app内好友界面
 * @author 杨庆雷
 * 2015-6-4下午3:08:05
 */
public class InviteFriendActivity extends BaseActivity implements OnClickListener {
	protected   final int NET_FRIENDLIST = 1;
	protected   final int EVENT_INVITE = 2;
	/**
	 * 获取弹出金币个数
	 */
	private static final int NET_COIN_CNT = 3;
	private ImageView iv_back;
	private TextView tv_save;
	private TextView friends_count;
	private TextView select_all_friend;
	private ListView invite_friends;
	private List<Integer> idList;
	private boolean select_all = false;
	private List<User> users = new ArrayList<User>();
	private SelectAdapter adapter;
	private Event event;
	private boolean fromChallenge;
	private int challengeId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_friend);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		event = (Event) getIntent().getSerializableExtra("event");
		fromChallenge = getIntent().getBooleanExtra("fromChallenge", false);
		challengeId = getIntent().getIntExtra("challengeId", -1);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_save = (TextView) findViewById(R.id.tv_save);
		friends_count = (TextView) findViewById(R.id.friends_count);
		select_all_friend = (TextView) findViewById(R.id.select_all_friend);
		invite_friends = (ListView) findViewById(R.id.invite_friends);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_save.setOnClickListener(this);
		select_all_friend.setOnClickListener(this);
	}

	private <T> void initValues() {
		idList = new ArrayList<Integer>();
		getUserFriendListInNet();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.tv_save:
			if(idList == null || idList.size() == 0){
				ToastUtil.show(this, "请选择需要邀请的好友");
				return;
			}
			showInviteDialog();
			break;
		case R.id.select_all_friend:
			select_all = !select_all;
			if(users.size()>0){
				selectAll();
				addOrDeleteIdToList();
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 确定邀请好友的dialog
	 */
	private void showInviteDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.dialog_invite, null );
        //对话框
        final Dialog dialog = new AlertDialog.Builder(InviteFriendActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        TextView dialog_info = (TextView) layout.findViewById(R.id.dialog_info);
        TextView invite_cancel = (TextView) layout.findViewById(R.id.invite_cancel);
        TextView invite_sure = (TextView) layout.findViewById(R.id.invite_sure);
        String name = "";
        for (int i = 0; i < users.size(); i++) {
			if(idList.get(0) == users.get(i).getId()){
				name = users.get(i).getNickname();
			}
		}
        dialog_info.setText("确定发送活动邀请给"+name+"等"+idList.size()+"人吗？");
        invite_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
        invite_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				postInviteToNet();
				dialog.dismiss();
			}
		});
	}

	/**
	 * 提交邀请的好友到服务器
	 */
	protected void postInviteToNet() {
		final Map<String, String> params = new HashMap<String, String>();
		StringBuilder friends = new StringBuilder("");
		for (int i = 0; i < idList.size(); i++) {
			if(i == 0){
				friends.append(idList.get(i));
			}else{
				friends.append(","+idList.get(i));
			}
		}
		if(fromChallenge){
			params.put("users", friends.toString());
			OxygenApplication.cachedThreadPool.execute(new Runnable() {
				public void run() {
					HttpUtil.Post(UrlConstants.CHALLENGE_INVITE+challengeId+".json",
							handler, EVENT_INVITE,params);
				}
			});
		}else{
			params.put("event_id", event.getId()+"");
			params.put("friends", friends.toString());
			OxygenApplication.cachedThreadPool.execute(new Runnable() {
				public void run() {
					HttpUtil.Post(UrlConstants.EVENT_INVITE,
							handler, EVENT_INVITE,params);
				}
			});
		}
	}

	/**
	 * 全选时 向id的集合中添加或者删除id
	 */
	private void addOrDeleteIdToList() {
		if(select_all){
	        for (int i = 0; i < users.size(); i++) {
				idList.add(users.get(i).getId());
			}
		}else if(idList != null){
			idList.clear();
		}
	}

	/**
	 * 全选或者全不选(但是只是对当前显示的条目进行操作，那些没有显示的条目在GetView中根据idList中id操作)
	 */
	private void selectAll() {
		CheckBox friend_choose = (CheckBox)findViewById(R.id.friend_choose);
		friend_choose.setChecked(true);
        int count = invite_friends.getChildCount();
        for(int i = 0; i < count; i++){
            RelativeLayout layout = (RelativeLayout)invite_friends.getChildAt(i);
            int c = layout.getChildCount();
            for(int j = 0; j < c; j++){
                View view = layout.getChildAt(j);
                if(view instanceof CheckBox){
                    ((CheckBox)view).setChecked(select_all);
                    break;
                }
            }
        }
	}

	/**
	 * 个人用户获得自己的好友列表
	 */
	private void getUserFriendListInNet() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.USERS_FRIEND_LIST_URL+"?limit=100&page=1", handler,
						NET_FRIENDLIST);
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
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsonfriendlist = new JSONObject(strObject);
						if (jsonfriendlist.getInt("status") == 200) {
							UpdateUI(jsonfriendlist);
						} else {
							ToastUtil.show(InviteFriendActivity.this,
									jsonfriendlist.getString("msg"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					ToastUtil.show(InviteFriendActivity.this, "网络连接不可用，请稍后重试");
				}
				break;
			case EVENT_INVITE:
				JSONObject inviteInfo = (JSONObject) msg.obj;
				if(inviteInfo != null){
					try {
						int status = inviteInfo.getInt("status");
						if(status == 200){
							
							if (!inviteInfo.isNull("user_action_id")) {
								
								int userActionId = inviteInfo.getInt("user_action_id");
								getToastContent(userActionId);
							} else {
								ToastUtil.show(InviteFriendActivity.this, "邀请好友成功");
								InviteFriendActivity.this.finish();
							}
							
							
						}else{
							ToastUtil.show(InviteFriendActivity.this, "邀请好友失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					ToastUtil.show(InviteFriendActivity.this, "网络连接不可用，请稍后重试");
				}
				break;
				
			case NET_COIN_CNT:
				// 弹出金币提示
				if (msg.obj == null) {
					ToastUtil.show(InviteFriendActivity.this, "网络连接不可用，请稍后重试");
				} else {
					
//					Log.i("Coin", msg.obj.toString());
					
					try {
						JSONObject jsonobeObject = new JSONObject((String)msg.obj);
						if (jsonobeObject.getInt("status") == 200) {
							JSONObject data = jsonobeObject.getJSONObject("data");
							String content = data.getString("alert");
							int coins = data.getInt("coins");
							ToastUtil.showCoin(InviteFriendActivity.this, content + " +" + coins +" 金币!");
							
						} else {
							ToastUtil.show(InviteFriendActivity.this, "网络连接不可用，请稍后重试");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				InviteFriendActivity.this.finish();

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
				HttpUtil.Get(UrlConstants.OPERATIONS_ACTION_REWARD + user_action_id +".json" , handler, NET_COIN_CNT);
			}
		});
		
	}
	
	
	/**
	 * 解析数据
	 * @param jsonfriendlist
	 */
	private void UpdateUI(JSONObject jsonfriendlist) {
		JSONArray datajsonarray;
		try {
			datajsonarray = jsonfriendlist.getJSONArray("data");
			if(datajsonarray!=null&datajsonarray.length()>0){
				friends_count.setText("我的好友("+datajsonarray.length()+"人)");
				users = UsersConstruct.ToUserlist(datajsonarray);
				//当数据获取到之后 才让全选按钮可以点击啊
				select_all_friend.setClickable(true);
				adapter = new SelectAdapter(InviteFriendActivity.this,users,R.layout.item_invite_friend);
				invite_friends.setAdapter(adapter);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	class SelectAdapter extends MyBaseAdapter<User>{

		public SelectAdapter(Context context, List<User> datas, int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(BaseViewHolder holder, final User user) {
			ImageView friend_headimg = (ImageView)holder.getView(R.id.friend_headimg);
			TextView friend_name = (TextView)holder.getView(R.id.friend_name);
			final CheckBox friend_choose = (CheckBox)holder.getView(R.id.friend_choose);
			ImageUtil.showImage(user.getHeadimgurl(), friend_headimg, R.drawable.icon_def);
			friend_name.setText(user.getNickname());
			if(idList.contains(user.getId())){
				friend_choose.setBackground(getResources().getDrawable(R.drawable.invite_friend_select));
			}else{
				friend_choose.setBackground(getResources().getDrawable(R.drawable.invite_friend));
			}
			friend_choose.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					int id = user.getId();
					if(isChecked && !idList.contains(id)){
						idList.add(id);
						friend_choose.setBackground(getResources().getDrawable(R.drawable.invite_friend_select));
					}else if(idList.contains(id)){
						idList.remove(idList.indexOf(id));
						friend_choose.setBackground(getResources().getDrawable(R.drawable.invite_friend));
					}
				}
			});
		}
		
	}
}
