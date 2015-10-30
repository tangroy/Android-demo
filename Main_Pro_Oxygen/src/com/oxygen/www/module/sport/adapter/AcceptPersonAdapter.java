package com.oxygen.www.module.sport.adapter;

import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.R.color;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.activity.CheckInActivity;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.widget.CircleImageView;

public class AcceptPersonAdapter extends BaseAdapter {

	private Context c;
	private LayoutInflater mInflater;
	List<User> users_accepts;
	/**
	 * 活动地点经纬度
	 */
	private double eventLatitude;
	private double eventLongitude;
	
 
	public AcceptPersonAdapter(Context c, List<User> users_accepts) {
		this.c = c;
		this.users_accepts = users_accepts;
		mInflater = LayoutInflater.from(c);
 	}

	public AcceptPersonAdapter(Context c,
			List<User> users_accepts, double latitude, double longitude) {
		// TODO Auto-generated constructor stub
		this.c = c;
		this.users_accepts = users_accepts;
		mInflater = LayoutInflater.from(c);
		this.eventLatitude = latitude;
		this.eventLongitude = longitude;
		
	}

	@Override
	public int getCount() {
		if(users_accepts == null||users_accepts.size()==0){
			return 0;
		}
		return users_accepts.size();
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
			convertView = mInflater.inflate(R.layout.item_activity_accept, null);
 			holder.head = (CircleImageView) convertView.findViewById(R.id.head);
 			holder.adm = (ImageView) convertView.findViewById(R.id.iv_adm);
 			holder.name = (TextView) convertView.findViewById(R.id.name);
 			holder.time = (TextView) convertView.findViewById(R.id.time);
 			holder.address = (TextView) convertView.findViewById(R.id.address);
 			holder.checkinStatus = (TextView) convertView.findViewById(R.id.check_in_status);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (arg0 == 0) {
			holder.adm.setVisibility(View.VISIBLE);
		}else {
			holder.adm.setVisibility(View.GONE);
		}
			User user= users_accepts.get(arg0);
			String url = user.getHeadimgurl();
			String name = user.getNickname();
			holder.name.setText(name);
			
			String status = user.getCheckin_status();
			if (status.equals("no")) {
				holder.checkinStatus.setTextColor(c.getResources().getColor(color.mygray));
				holder.checkinStatus.setText("未签到");
			
				holder.time.setVisibility(View.GONE);
				holder.address.setVisibility(View.GONE);
				
			} else if (status.equals("by_user")) {
				holder.checkinStatus.setTextColor(c.getResources().getColor(color.mygreen));
				holder.checkinStatus.setText("已签到");
				
			} else if (status.equals("by_admin")) {
				holder.checkinStatus.setTextColor(c.getResources().getColor(color.mygreen));
				holder.checkinStatus.setText("发起者代签到");
				
			} else if (status.equals("decline")) {
				holder.checkinStatus.setTextColor(c.getResources().getColor(color.myred));
				holder.checkinStatus.setText("签到被取消");
				
			}
			
			String time = user.getCheckin_at();
			if (time!=null) {
				// 不在正负一小时
				// TODO...

				long timeDistance = 0;
				if (timeDistance > 1*60*60*1000) {
					holder.time.setTextColor(c.getResources().getColor(color.myred));
				}
				holder.time.setText(time.substring(5, 16));
			}

//			String address = user.getAddress();
			double latitude = user.getLatitude();
			double longitude = user.getLongitude();
			Log.i("checkin", "get["+latitude+", "+longitude+"]");
			
			if (!status.equals("no") && longitude==0 && latitude==0) {
				holder.address.setText("未获取到签到位置");
			} else if (longitude!=0 && latitude!=0) {
				// 计算与活动地点距离
				// TODO...
				double distance = 0;
				distance = CheckInActivity.getDistance(latitude, longitude, eventLatitude, eventLongitude);
				if (distance > 1000.0) {
					// 距离活动地点大于1km
					holder.address.setTextColor(c.getResources().getColor(color.myred));
					holder.address.setText("距离活动地点 " + (distance/1000) + " km");
				} else {
					holder.address.setText("距离活动地点 " + (distance) + " 米");
				}
				
			}
			
			ImageUtil.showImage(url, holder.head, R.drawable.icon_def);

			return convertView;
	}

	public static class ViewHolder {
		public CircleImageView head;
		public ImageView adm;
		public TextView name;
		public TextView time;
		public TextView address;
		public TextView checkinStatus;
		
	}
	
}
