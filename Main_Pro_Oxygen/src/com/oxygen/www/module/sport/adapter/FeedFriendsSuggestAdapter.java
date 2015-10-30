package com.oxygen.www.module.sport.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.FeedUser;
import com.oxygen.www.module.sport.eventbus_enties.FeedData;
import com.oxygen.www.module.sport.eventbus_enties.FeedFriends;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.CircleImageView;

import de.greenrobot.event.EventBus;

public class FeedFriendsSuggestAdapter extends BaseAdapter {

	private Context mContext;
	private List<FeedUser> mList;
	private LayoutInflater mInflater;
	
	
	public FeedFriendsSuggestAdapter(List<FeedUser> friends, Context context) {
		// 前三名
		if (friends!= null && friends.size()>3) {
			mList = friends.subList(0, 3);
		} else {
			mList = friends;
		}
		
//		Log.i("friends", "size = "+mList.size());
		
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 初始化
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_fragment_feed_friend_item, null);
			// 初始化 view
			initViews(holder, convertView);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		// TODO...
		FeedUser feedUser = mList.get(position);
		
		holder.iv_suggest_head.setImageResource(R.drawable.icon_def);
		ImageUtil.showImage2(feedUser.getHeadimgurl(), holder.iv_suggest_head, R.drawable.icon_def);

		String nick = feedUser.getNickname();
		holder.tv_suggest_name.setText(nick);
		
		String relation = feedUser.getNew_relationship();
		if ("stranger".equals(relation)) {
			// 陌生人
			holder.tv_add_friend_status.setBackgroundResource(R.drawable.icon_addfriend);

			holder.tv_add_friend_status.setClickable(true);
			initEvents(holder, position);
			
		} else if ("pending".equals(relation)) {
			// 等待对方确认
			holder.tv_add_friend_status.setText("验证中");
			holder.tv_add_friend_status.setTextColor(mContext.getResources().getColor(R.color.mygray));
			holder.tv_add_friend_status.setBackgroundColor(Color.WHITE);
			holder.tv_add_friend_status.setClickable(false);

		} else if ("friend".equals(relation)) {
			// 好友
			holder.tv_add_friend_status.setText("已同意");
			holder.tv_add_friend_status.setTextColor(mContext.getResources().getColor(R.color.mygreen));
			holder.tv_add_friend_status.setBackgroundColor(Color.WHITE);
			holder.tv_add_friend_status.setClickable(false);
			
		}
		
		return convertView;
	}
	
	private void initViews(ViewHolder holder, View convertView) {
		holder.iv_suggest_head = (CircleImageView) convertView.findViewById(R.id.iv_suggest_head);
		holder.tv_suggest_name = (TextView) convertView.findViewById(R.id.tv_suggest_name);
//		holder.iv_add_friend = (ImageView) convertView.findViewById(R.id.iv_add_friend);
		holder.tv_add_friend_status = (TextView) convertView.findViewById(R.id.tv_add_friend_status);
		
	}
	
	private void initEvents(final ViewHolder holder, final int position) {
		
		holder.tv_add_friend_status.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO...
				
				holder.tv_add_friend_status.setText("验证中");
				holder.tv_add_friend_status.setTextColor(mContext.getResources().getColor(R.color.mygray));
				holder.tv_add_friend_status.setBackgroundColor(Color.WHITE);
				
				OxygenApplication.cachedThreadPool.execute(new Runnable() {
					public void run() {
						HttpUtil.Get(UrlConstants.ADD_FRIEND + mList.get(position).getId() + ".json",
								handler, ADD_FRIEND);
					}
				});
				
				// 改变数据
				changeData(position);
				
			}
		});
	}
	
	
	/**
	 * 改变 mList
	 */
	private void changeData(int position) {
		mList.get(position).setNew_relationship("pending");
		EventBus.getDefault().post(new FeedFriends(110, mList));
	}
	

	/**
	 * Feed 好友推荐
	 * @author 张坤
	 *
	 */
	private static class ViewHolder {
		CircleImageView iv_suggest_head;
		TextView tv_suggest_name;
		TextView tv_add_friend_status;
//		ImageView iv_add_friend;
		
	}
	
	/**
	 * 添加好友
	 */
	private static final int ADD_FRIEND = 1;
	
	private Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ADD_FRIEND:
				// 添加好友
				/*String addInfo = (String) msg.obj;
				if (addInfo != null) {
					try {
						JSONObject jsonenlist = new JSONObject(addInfo);
						if (jsonenlist.getInt("status") == 200) {
							
						} else {
							ToastUtil.show(mContext, "加好友失败或已添加好友");
							
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(mContext, "网络连接不可用，请稍后重试");
				}*/
				break;
			
			default:
				break;
			}
		}

	};
	
}
