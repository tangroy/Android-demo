package com.oxygen.www.module.sport.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.enties.GDMessage;
import com.oxygen.www.utils.GDUtil;

/**
 * 活动详情页-报名列表-发送群消息-群发消息历史列表的 adapter
 * 
 * @author 张坤
 *
 */
public class EventsMessageAdapter extends BaseAdapter {
	
	@SuppressLint("SimpleDateFormat") 
	SimpleDateFormat dwf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
	
	private Context c;
	private LayoutInflater mInflater;
	private List<GDMessage> list;
	

	public EventsMessageAdapter(Context c, List<GDMessage> list) {
		this.c = c;
		mInflater = LayoutInflater.from(this.c);
		this.list = list;
 	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_events_message, null);
			holder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		GDMessage msg = list.get(position);
		// 去掉前面的  
		// content=来自 跑步群发消息:嘟嘟嘟
		String message = msg.getContent().substring(msg.getContent().indexOf(":")+1);
		holder.tv_message.setText(message);
		String time = msg.getCreated_at().trim();
		try {
			holder.tv_time.setText(GDUtil.getTimeDiff2(dwf.parse(time)));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return convertView;
	}

	public  class ViewHolder {
		public TextView tv_message;
		public TextView tv_time;
	}
	
}
