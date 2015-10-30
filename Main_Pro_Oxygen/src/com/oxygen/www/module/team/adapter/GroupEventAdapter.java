package com.oxygen.www.module.team.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.oxygen.www.R;
import com.oxygen.www.enties.Event;
import com.oxygen.www.widget.NoScrollGridView2;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroupEventAdapter extends BaseAdapter {

	private Context c;
	private List<Event> events;
	private LayoutInflater inflater;
	private ViewPager vp_group_photo;
	private ViewGroup.LayoutParams params;
	private int width;
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	
	public GroupEventAdapter(Context c,List<Event> events,ViewPager vp_group_photo){
		this.c = c;
		this.events = events;
		this.vp_group_photo = vp_group_photo;
		inflater = LayoutInflater.from(c);
		WindowManager manager = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		width = manager.getDefaultDisplay().getWidth();
		params = vp_group_photo.getLayoutParams();
	}
	@Override
	public int getCount() {
		if(events == null){
			return 0;
		}
		return events.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_group_photos, null);
			holder.event_time = (TextView) convertView.findViewById(R.id.event_time);
			holder.event_name = (TextView) convertView.findViewById(R.id.event_name);
 			holder.gd_group_ptotos = (NoScrollGridView2) convertView.findViewById(R.id.gd_group_ptotos);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int year = Integer.parseInt(events.get(position).getCreated_at().substring(0, 4));
		int month = Integer.parseInt(events.get(position).getCreated_at().substring(5, 7));
		int day = Integer.parseInt(events.get(position).getCreated_at().substring(8, 10));
		
		holder.event_time.setText(sdf.format(new Date(year-1900, month, day)));
		/*try {
			holder.event_time.setText(sdf.format(sdf.parse(events.get(position).getCreated_at().substring(0, 10))));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		holder.event_name.setText(events.get(position).getTilte()+"( "+events.get(position).getPhoto_count()+" )");
		GroupPhotosAdapter groupPhotosAdapter = new GroupPhotosAdapter(c,events.get(position).getPhotos(),vp_group_photo);
		holder.gd_group_ptotos.setAdapter(groupPhotosAdapter);
		return convertView;
	}
	
	public  class ViewHolder {
		public TextView event_time, event_name;
		public NoScrollGridView2 gd_group_ptotos;
	}

}
