package com.oxygen.www.module.sport.adapter;

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
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.widget.CircleImageView;
/**
 * 活动详情页报名成员的列表的适配器
 * @author 杨庆雷
 * 2015-6-11下午3:42:01
 */
public class SportDetailAcceptAdapter extends BaseAdapter {

	private Context c;
	private LayoutInflater mInflater;
	String usersid[];
	JSONObject jsonobject_userinfo;
	int count =0;
	private boolean tag = false;

	public SportDetailAcceptAdapter(boolean tag,Context c, String usersid[],JSONObject jsonobject_userinfo,int count) {
		this.c = c;
		this.usersid = usersid;
		mInflater = LayoutInflater.from(this.c);
		this.jsonobject_userinfo = jsonobject_userinfo;
		this.count = count;
		this.tag = tag;
	}

	@Override
	public int getCount() {
		return count+1;
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
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_accept, null);
 			holder.head = (CircleImageView) convertView.findViewById(R.id.head);
 			holder.adm = (ImageView) convertView.findViewById(R.id.iv_adm);
 			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		//第一名
		if(arg0 == 0){
			holder.adm.setVisibility(View.VISIBLE);
		}
		
		try {
			if(count == arg0){
				if(tag){
					holder.head.setVisibility(View.GONE);
				}else{
					holder.head.setImageResource(R.drawable.add_friend);
					holder.name.setText("邀请好友");
				}
			}else{
				String url = jsonobject_userinfo.getJSONObject(usersid[arg0]+"").getString("headimgurl");
				String name = jsonobject_userinfo.getJSONObject(usersid[arg0]+"").getString("nickname");
				if(name.length()>4){
					name = name.substring(0, 4);
				}
				holder.name.setText(name);
				ImageUtil.showImage(url, holder.head,R.drawable.icon_def);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return convertView;
	}

	public  class ViewHolder {
		public CircleImageView head;
		public ImageView adm;
		public TextView name;
	}

}
