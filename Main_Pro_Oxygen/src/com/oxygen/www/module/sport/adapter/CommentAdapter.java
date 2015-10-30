package com.oxygen.www.module.sport.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.enties.Comment;

public class CommentAdapter extends BaseAdapter {

 	private LayoutInflater mInflater;
	List<Comment> commentlist;
	JSONObject users;
	public CommentAdapter(Context c,List<Comment> commentlist,JSONObject users) {
 		mInflater = LayoutInflater.from(c);
		this.commentlist = commentlist;
		this.users = users;
	}

	@Override
	public int getCount() {
		if(commentlist == null){
			return 0;
		}
		return commentlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return commentlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_comment, null);
 			holder.namenick = (TextView) convertView.findViewById(R.id.namenick);
			holder.content = (TextView) convertView.findViewById(R.id.content);
 			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			holder.namenick.setText(users.getJSONObject(
					commentlist.get(arg0).getCreated_by()+"").getString("nickname"));
		} catch (JSONException e) {
 			e.printStackTrace();
		}
		holder.content.setText(commentlist.get(arg0).getContent()+"");
		return convertView;
	}

	public  class ViewHolder {
 		public TextView namenick;
		public TextView content;
 	}

}
