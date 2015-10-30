package com.oxygen.www.module.user.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.Posts;
import com.oxygen.www.enties.PostsTag;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.ImageUtil;

public class FaviconsAdapter extends BaseAdapter {

	private Context c;
	private LayoutInflater mInflater;
	private List<Posts> postses;

	public FaviconsAdapter(Context c, List<Posts> postses) {
		this.c = c;
		this.postses = postses;
		mInflater = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		return postses != null && postses.size() > 0 ? postses.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return postses.get(arg0);
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
			convertView = mInflater.inflate(R.layout.item_postslist, null);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.tv_summary = (TextView) convertView
					.findViewById(R.id.tv_summary);
			holder.iv_head = (ImageView) convertView
					.findViewById(R.id.iv_head);
			holder.tv_votecount = (TextView) convertView.findViewById(R.id.tv_votecount);
			holder.tv_readcount = (TextView) convertView.findViewById(R.id.tv_readcount);
			holder.tv_created_at =(TextView) convertView.findViewById(R.id.tv_created_at);
			holder.tv_tag = (TextView) convertView.findViewById(R.id.tv_tag);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Posts posts =  postses.get(arg0);
		ArrayList<PostsTag> posttags = posts.getTags();  
		if(posttags!=null&&posttags.size()>0){
			holder.tv_tag.setVisibility(View.VISIBLE);
			holder.tv_tag.setBackgroundDrawable(GDUtil.engSporttodrawable(c, "icon_tag_"+arg0%6));
			holder.tv_tag.setText(posttags.get(0).getName());
		}else{
			holder.tv_tag.setVisibility(View.GONE);
		}
		holder.tv_created_at.setText(posts.getPublish_time());	
		holder.tv_readcount.setText(""+posts.getRead_count());
		holder.tv_votecount.setText(""+posts.getVote_count());
		holder.tv_title.setText(posts.getTitle());
		holder.tv_summary.setText(posts.getSummary());
		ImageUtil.showImage(
					posts.getPic()+Constants.qiniu_photo_head, holder.iv_head,R.drawable.icon_def);
		
		return convertView;
	}

	public  class ViewHolder {
		public TextView tv_title;
		public ImageView iv_head;
		public TextView tv_summary;
		public TextView tv_votecount;
		public TextView tv_readcount;
		public TextView tv_created_at;
		public TextView tv_tag;
		

	}

}
