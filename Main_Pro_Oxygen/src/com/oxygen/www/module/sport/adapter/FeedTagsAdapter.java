package com.oxygen.www.module.sport.adapter;

import java.util.List;

import com.oxygen.www.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 *  动态 - 搜索标签
 *  
 * @author 张坤
 *
 */
public class FeedTagsAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<String> mList;
	
	
	public FeedTagsAdapter(Context context, List<String> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_feed_search_tags, null);
			holder.tv_tag = (TextView) convertView.findViewById(R.id.tv_tag);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_tag.setText(mList.get(position));
		
		return convertView;
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
	
	private static class ViewHolder{
		TextView tv_tag;
	}


}
