package com.oxygen.www.module.sport.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.GDMessage;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.CircleImageView;

public class MessageAdapter extends BaseAdapter {
	private final static int ADD_FRIEND_ACCEPT = 1;
	private final static int ADD_FRIEND_SETRESPONSE = 2;
	private Context c;
	private LayoutInflater mInflater;
	private int mposition = 0;
	private List<GDMessage> messagelist;
 	SimpleDateFormat dwf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

	public MessageAdapter(Context c,List<GDMessage> messagelist) {
		this.c = c;
		mInflater = LayoutInflater.from(c);
		this.messagelist = messagelist;
 	}

	@Override
	public int getCount() {
		return messagelist.size();
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
	public View getView(final int position, View convertView, ViewGroup arg2) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_message, null);
			holder.iv_head = (CircleImageView) convertView.findViewById(R.id.iv_head);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.add_friend_accept = (TextView) convertView.findViewById(R.id.add_friend_accept);
			holder.add_friend_accept.setTag(position);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.add_friend_accept.setVisibility(View.GONE);
			holder.tv_time.setVisibility(View.VISIBLE);
		}
		
		GDMessage msg = messagelist.get(position);
		holder.tv_name.setText(msg.getTitle()+"");
		holder.tv_message.setText(msg.getContent()+"");
		String time = msg.getCreated_at().trim();
		try {
			holder.tv_time.setText(GDUtil.getTimeDiff2(dwf.parse(time)));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 		ImageUtil.showImage(
		 				msg.getHeadimgurl()+Constants.qiniu_photo_head, holder.iv_head,R.drawable.icon_def);
		if("friend_request".equals(messagelist.get(position).getType())){
			if("YES".equals(messagelist.get(position).getResponsed())){
				holder.add_friend_accept.setBackgroundColor(c.getResources().getColor(R.color.white));
				holder.add_friend_accept.setTextColor(c.getResources().getColor(R.color.grey));
				holder.add_friend_accept.setText("已同意");
				holder.add_friend_accept.setClickable(false);
			}
			holder.add_friend_accept.setVisibility(View.VISIBLE);
			holder.tv_time.setVisibility(View.GONE);
		}
		holder.add_friend_accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mposition = position;
				holder.add_friend_accept.setText("已同意");
				holder.add_friend_accept.setBackgroundColor(c.getResources().getColor(R.color.white));
				holder.add_friend_accept.setTextColor(c.getResources().getColor(R.color.grey));
				//接受添加好友前(把response设置为yes)
				OxygenApplication.cachedThreadPool.execute(new Runnable() {
					public void run() {
						HttpUtil.Get(UrlConstants.ADD_FRIEND_SETRESPONSE + messagelist.get(position).getId() + ".json",
								handler, ADD_FRIEND_SETRESPONSE);
					}
				});
			}

		});
		return convertView;
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case ADD_FRIEND_ACCEPT:
				String addInfo = (String) msg.obj;
				if (addInfo != null) {
					try {
						JSONObject jsonenlist = new JSONObject(addInfo);
						if (jsonenlist.getInt("status") == 200) {
							ToastUtil.show(c, "接受添加好友成功");
						}else{
							ToastUtil.show(c, "接受添加好友失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					ToastUtil.show(c, "网络连接不可用，请稍后重试");
				}
				break;			
			case ADD_FRIEND_SETRESPONSE:
				String responseInfo = (String) msg.obj;
				if (responseInfo != null) {
					try {
						JSONObject jsonenlist = new JSONObject(responseInfo);
						if (jsonenlist.getInt("status") == 200) {
							acceptFriend();
						}else{
							ToastUtil.show(c, "接受添加好友失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					ToastUtil.show(c, "网络连接不可用，请稍后重试");
				}
				break;			
			default:
				break;
			}
		}
		
	};
	/**
	 * 接受添加好友请求
	 */
	private void acceptFriend() {
		//添加好友
		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				HttpUtil.Get(UrlConstants.ADD_FRIEND_ACCEPT + messagelist.get(mposition).getSender() + ".json",
						handler, ADD_FRIEND_ACCEPT);
			}
		});
	}

	public  class ViewHolder {
		public CircleImageView iv_head;
		public TextView tv_name;
		public TextView tv_message;
		public TextView tv_time;
		public TextView add_friend_accept;
	}
	
}
