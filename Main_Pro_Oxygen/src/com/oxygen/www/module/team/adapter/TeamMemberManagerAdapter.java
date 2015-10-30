package com.oxygen.www.module.team.adapter;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.enties.ChallengesUser;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.widget.CircleImageView;

public class TeamMemberManagerAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	ArrayList<ChallengesUser> members;
	JSONObject jsonobject_userinfo;
	private boolean isdel;

	public TeamMemberManagerAdapter(Context c,
			ArrayList<ChallengesUser> members, JSONObject jsonobject_userinfo,boolean isdel) {
		inflater = LayoutInflater.from(c);
		this.members = members;
		this.jsonobject_userinfo = jsonobject_userinfo;
		this.isdel = isdel;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return members.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_team_member, parent,
					false);
			holder = new ViewHolder();
			holder.iv_head = (CircleImageView) convertView
					.findViewById(R.id.iv_head);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_admin = (TextView) convertView
					.findViewById(R.id.tv_admin);
			holder.iv_del = (ImageView) convertView.findViewById(R.id.iv_del);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			ChallengesUser member = members.get(position);
			if (member.getRole().equals("admin")) {
				holder.tv_admin.setVisibility(View.VISIBLE);
				holder.iv_del.setVisibility(View.GONE);
			} else {
				holder.tv_admin.setVisibility(View.GONE);
				if(isdel){
					holder.iv_del.setVisibility(View.VISIBLE);
				}else{
					holder.iv_del.setVisibility(View.GONE);
				}
			}
			User user = UsersConstruct.ToUser(jsonobject_userinfo.getJSONObject(""+member.getUser_id()));
			ImageUtil.showImage(user.getHeadimgurl(), holder.iv_head, R.drawable.icon_def);
			holder.tv_name.setText(user.getNickname());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	public class ViewHolder {
		public CircleImageView iv_head;
		private ImageView iv_del;
		private TextView tv_name, tv_admin;
	}

}
