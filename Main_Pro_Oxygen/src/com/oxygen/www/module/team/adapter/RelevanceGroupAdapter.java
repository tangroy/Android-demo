package com.oxygen.www.module.team.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.Group;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.widget.CircleImageView;

public class RelevanceGroupAdapter extends BaseAdapter {
	ArrayList<Group> groups;
	Context c;
 	private LayoutInflater inflater;
	private String groupId;
	public RelevanceGroupAdapter(ArrayList<Group> groups, Context c) {
		this.c = c;
		this.groups = groups;
 		inflater = LayoutInflater.from(c);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_group_relevance, null);
			holder.iv_head = (CircleImageView) convertView
					.findViewById(R.id.iv_head);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_member_count = (TextView) convertView.findViewById(R.id.tv_member_count);
			holder.cb_team = (ImageView) convertView.findViewById(R.id.cb_team);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Group group = groups.get(arg0);
		ImageUtil.showImage(group.getPic()+Constants.qiniu_photo_head, holder.iv_head,R.drawable.icon_def);
		holder.tv_name.setText(group.getName());
		holder.tv_member_count.setText("成员："+group.getMember_count()+"人");
		
		return convertView;
	}

	public  class ViewHolder {
		public TextView tv_name, tv_member_count;
		public CircleImageView iv_head;
		public ImageView cb_team;
	}
}
