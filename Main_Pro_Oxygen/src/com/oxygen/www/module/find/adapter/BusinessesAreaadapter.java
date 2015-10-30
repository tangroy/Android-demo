package com.oxygen.www.module.find.adapter;

import java.util.List;
import com.oxygen.www.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 选择场馆距离范围的适配器
 * @author kunyuan
 *
 */
public class BusinessesAreaadapter extends BaseAdapter {
	private Context context;
	private List<String> areas;
	private LayoutInflater mInflater;
	public BusinessesAreaadapter(Context context,List<String> areas){
		this.context = context;
		this.areas = areas;
		mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		if(areas != null){
			return areas.size();
		}
		return 0;
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
			convertView = mInflater.inflate(R.layout.item_business_type, null);
			holder.tv_type_name = (TextView) convertView.findViewById(R.id.tv_type_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_type_name.setText(areas.get(position));
		return convertView;
	}
	
	public  class ViewHolder {
		public TextView tv_type_name;
	}
}
