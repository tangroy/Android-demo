package com.oxygen.www.module.team.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.Group;
import com.oxygen.www.utils.ImageUtil;

public class GroupindexAdapter extends BaseAdapter {
	private List<Group> groups;
	private LayoutInflater inflater;
	private boolean isMe;

	/**
	 * 
	 * @param groups
	 * @param c
	 * @param type
	 *            类型 0表示团队列表 1表示搜索列表
	 */
	public GroupindexAdapter(List<Group> groups, Context c,boolean isMe) {
		this.groups = groups;
		inflater = LayoutInflater.from(c);
		this.isMe = isMe;
	}

	@Override
	public int getCount() {
		if(isMe){
			return groups == null ? 1 : groups.size() + 1;
		}else{
			return groups == null ? 0 : groups.size();
		}
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
		View view;
//		if (convertView == null) {
			view = inflater.inflate(R.layout.item_group_index, null);
//		} else{
//			view = convertView;
//		}
		TextView tv_team_name = (TextView) view.findViewById(R.id.tv_team_name);
		TextView tv_team_number_count = (TextView) view.findViewById(R.id.tv_team_number_count);
		ImageView iv_team_head_pic = (ImageView) view.findViewById(R.id.iv_team_head_pic);
		ImageView iv_team_role = (ImageView) view.findViewById(R.id.iv_team_role);
		RelativeLayout rl_team_info = (RelativeLayout) view.findViewById(R.id.rl_team_info);
		
		if (arg0 == groups.size()) {
			tv_team_name.setText("创建团队");
			rl_team_info.setVisibility(View.GONE);
			iv_team_head_pic.setImageResource(R.drawable.icon_create_team);
			iv_team_head_pic.setScaleType(ScaleType.FIT_CENTER);
		} else {
			Group group = groups.get(arg0);
			if ("admin".equals(group.role)) {
				iv_team_role.setImageResource(R.drawable.icon_team_mine);
			} else {
				iv_team_role.setImageResource(R.drawable.icon_team_join);
			}
			ImageUtil.showImage(group.getPic() + Constants.qiniu_photo_head,
					iv_team_head_pic, 0);
			tv_team_name.setText(group.getName() + "");
			tv_team_number_count.setText(group.getMember_count() + "");
		}
		return view;
	}
}
