package com.oxygen.www.module.sport.adapter;

import java.util.List;
import java.util.Map;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.enties.Comment;
import com.oxygen.www.enties.UserInfo;

public class FeedCommentAdapter extends BaseAdapter {

	private List<Comment> mList;
	private Context mContext;
	private LayoutInflater mInflater;
	/**
	 * 当前用户信息
	 */
	private UserInfo mCurrent_User;
	/**
	 * 用户信息
	 */
	private Map<String, UserInfo> mUsersInfo;
	
	
	public FeedCommentAdapter(List<Comment> list, Context context, UserInfo currentUser, Map<String, UserInfo> usersInfo) {
		mList = list;
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mCurrent_User = currentUser;
		mUsersInfo = usersInfo;
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
			convertView = mInflater.inflate(R.layout.item_fragment_feed_comment, null);
			// 初始化 view
			initViews(holder, convertView);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		// TODO...
		Comment feedComment = mList.get(position);
		if (mCurrent_User.getId() == feedComment.getCreated_by()) {
			holder.tv_comment_name.setText(mCurrent_User.getNickname()+": ");
		} else {
			String nick = mUsersInfo.get(""+feedComment.getCreated_by()).getNickname();
			if (null != nick) {
				holder.tv_comment_name.setText(nick+": ");
			} else {
				holder.tv_comment_name.setText("null"+": ");
			}
		}
		
		holder.tv_comment_content.setText(feedComment.getContent());
		
		initEvents(holder, position);
		
		return convertView;
	}
	
	private void initViews(ViewHolder holder, View convertView) {
		holder.tv_comment_name = (TextView) convertView.findViewById(R.id.tv_comment_name);
		holder.tv_comment_content = (TextView) convertView.findViewById(R.id.tv_comment_content);
	}
	
	private void initEvents(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Feed 评论
	 * @author 张坤
	 *
	 */
	private static class ViewHolder {
		TextView tv_comment_name;
		TextView tv_comment_content;
		
	}
	
}
