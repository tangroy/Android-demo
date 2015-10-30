package com.oxygen.www.module.user.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.User;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.CircleImageView;

public class FriendsAdapter extends BaseAdapter {
	protected final int ADD_FRIEND = 1;
	protected final int ADD_FRIEND_ACCEPT = 2;
	private Context c;
	private LayoutInflater mInflater;
 	private List<User> users;
 	private boolean acceptFlag;
	public FriendsAdapter(Context c,List<User> users,boolean acceptFlag) {
		this.c = c;
		mInflater = LayoutInflater.from(c);
		this.users = users;
		this.acceptFlag = acceptFlag;
	}

	@Override
	public int getCount() {
		if(users == null){
			return 0;
		}
		return users.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_friendlist, null);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.add_friend_accept = (TextView) convertView.findViewById(R.id.add_friend_accept);
			holder.head = (CircleImageView) convertView.findViewById(R.id.iv_head);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(TextUtils.isEmpty(users.get(arg0).getNickname())?"--":users.get(arg0).getNickname());
		if(!TextUtils.isEmpty(users.get(arg0).getHeadimgurl())){
			ImageUtil.showImage(users.get(arg0).getHeadimgurl(),holder.head,R.drawable.icon_def);
		}else{
			holder.head.setImageDrawable(c.getResources().getDrawable(R.drawable.icon_def));
		}
		if(acceptFlag){
			holder.add_friend_accept.setVisibility(View.VISIBLE);
			holder.add_friend_accept.setTag(arg0);
			String relationShip = users.get(arg0).getNew_relationship();
			if ("myself".equals(relationShip)) {
				holder.add_friend_accept.setVisibility(View.GONE);
			} else if ("stranger".equals(relationShip)) {
				holder.add_friend_accept.setBackground(c.getResources().getDrawable(R.drawable.icon_addfriend));
				holder.add_friend_accept.setText("");
				holder.add_friend_accept.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						holder.add_friend_accept.setText("验证中");
						holder.add_friend_accept.setTextColor(c.getResources().getColor(R.color.grey));
						holder.add_friend_accept.setBackgroundColor(c.getResources().getColor(R.color.white));
						OxygenApplication.cachedThreadPool.execute(new Runnable() {
							public void run() {
								HttpUtil.Get(UrlConstants.ADD_FRIEND + users.get(arg0).getId() + ".json",
										handler, ADD_FRIEND);
							}
						});
					}
				});
			} else if ("pending".equals(relationShip)) {
				holder.add_friend_accept.setText("验证中");
				holder.add_friend_accept.setTextColor(c.getResources().getColor(R.color.grey));
				holder.add_friend_accept.setBackgroundColor(c.getResources().getColor(R.color.white));
			} else if ("friend".equals(relationShip)) {
				holder.add_friend_accept.setText("已添加");
				holder.add_friend_accept.setTextColor(c.getResources().getColor(R.color.grey));
				holder.add_friend_accept.setBackgroundColor(c.getResources().getColor(R.color.white));
			} else if ("tbc".equals(relationShip)) {// 别人请求添加我为好友 等待我确认
				holder.add_friend_accept.setText("接受");
				holder.add_friend_accept.setBackground(c.getResources().getDrawable(R.drawable.wane_shape_green));
				holder.add_friend_accept.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 接受添加好友
						holder.add_friend_accept.setText("已接受");
						holder.add_friend_accept.setTextColor(c.getResources().getColor(R.color.grey));
						holder.add_friend_accept.setBackgroundColor(c.getResources().getColor(R.color.white));
						OxygenApplication.cachedThreadPool.execute(new Runnable() {
							public void run() {
								HttpUtil.Get(UrlConstants.ADD_FRIEND_ACCEPT + users.get(arg0).getId()
										+ ".json", handler, ADD_FRIEND_ACCEPT);
							}
						});
					}
				});
			}
		}
		return convertView;
	}

	public  class ViewHolder {
		public CircleImageView head;
		public TextView name;
		public TextView add_friend_accept;
	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ADD_FRIEND:
				String addInfo = (String) msg.obj;
				if (addInfo != null) {
					try {
						JSONObject jsonenlist = new JSONObject(addInfo);
						if (jsonenlist.getInt("status") == 200) {
							ToastUtil.show(c, "加好友请求已发送");
						} else {
							ToastUtil.show(c, "加好友失败或已添加好友");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(c, "网络连接不可用，请稍后重试");
				}
				break;
			case ADD_FRIEND_ACCEPT:
				String acceptAddInfo = (String) msg.obj;
				if (acceptAddInfo != null) {
					try {
						JSONObject jsonenlist = new JSONObject(acceptAddInfo);
						if (jsonenlist.getInt("status") == 200) {
							ToastUtil.show(c, "接受添加好友成功");
						} else {
							ToastUtil.show(c, "接受添加好友失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(c, "网络连接不可用，请稍后重试");
				}
				break;	
			default:
				break;
			}
		}

	};
}