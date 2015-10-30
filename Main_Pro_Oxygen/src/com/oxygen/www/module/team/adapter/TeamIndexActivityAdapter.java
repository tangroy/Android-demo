package com.oxygen.www.module.team.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.Group;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.widget.CircleImageView;

public class TeamIndexActivityAdapter extends BaseAdapter {
	private List<Group> groups;
	private Context c;
 	private LayoutInflater inflater;
 	private int type =0;
/**
 * 
 * @param groups
 * @param c
 * @param type 类型  0表示团队列表  1表示搜索列表
 */
	public TeamIndexActivityAdapter(List<Group> groups, Context c,int type) {
		this.c = c;
		this.groups = groups;
 		inflater = LayoutInflater.from(c);
 		this.type = type;
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
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_team_activity, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_member_count = (TextView) convertView
					.findViewById(R.id.tv_member_count);
 			holder.iv_head = (CircleImageView) convertView
					.findViewById(R.id.iv_head);
 			holder.tv_event_info = (TextView) convertView
					.findViewById(R.id.tv_event_info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Group group = groups.get(arg0);
		ImageUtil.showImage(group.getPic()+Constants.qiniu_photo_head, holder.iv_head,R.drawable.icon_def);
		holder.tv_name.setText(group.getName());
		holder.tv_member_count.setText(group.getMember_count()+"人");
		holder.tv_event_info.setText(group.getIntro());
		
 		return convertView;
	}

	public  class ViewHolder {
		public TextView tv_name, tv_member_count,tv_event_info;
		public CircleImageView iv_head;
	}
}
