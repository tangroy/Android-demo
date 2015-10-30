package com.oxygen.www.module.sport.adapter;

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
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.CurrentEventUser;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.utils.ImageUtil;

public class SportAcceptAdapter extends BaseAdapter {

	private Context c;
	private LayoutInflater mInflater;
	JSONObject json_user;
	ArrayList<CurrentEventUser> currentEventUserlist;

	public SportAcceptAdapter(Context c, JSONObject json_user,
			ArrayList<CurrentEventUser> currentEventUserlist) {
		this.c = c;
		mInflater = LayoutInflater.from(c);
		this.currentEventUserlist = currentEventUserlist;
		this.json_user = json_user;
	}

	@Override
	public int getCount() {
		return currentEventUserlist != null ? currentEventUserlist.size():0;
	}

	@Override
	public Object getItem(int arg0) {
		return currentEventUserlist.get(arg0);
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
			convertView = mInflater.inflate(R.layout.item_acceptlist, null);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.head = (ImageView) convertView
					.findViewById(R.id.iv_head);
			holder.created_at = (TextView) convertView
					.findViewById(R.id.tv_created_at);
			holder.sex = (ImageView) convertView.findViewById(R.id.iv_sex);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			JSONObject user_jsonobject = json_user.getJSONObject(currentEventUserlist.get(arg0).getUser_id()+"");
			User user = UsersConstruct.ToUser(user_jsonobject);
			holder.name.setText(user.getNickname()==null?"无名英雄":user.getNickname());
			int sex = user.getSex();
			if(sex==1){
				holder.sex.setImageDrawable(c.getResources().getDrawable(R.drawable.icon_man));
			}else if(sex==2){
				holder.sex.setImageDrawable(c.getResources().getDrawable(R.drawable.icon_women));
			}else{
				holder.sex.setImageDrawable(null);
			}
			holder.created_at.setText(currentEventUserlist.get(arg0).getCreated_at());
			ImageUtil.showImage(
					user.getHeadimgurl()+Constants.qiniu_photo_head, holder.head,R.drawable.icon_def);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return convertView;
	}

	public  class ViewHolder {

		public ImageView head;
		public TextView name;
		public ImageView sex;
		public TextView created_at;

	}

}
