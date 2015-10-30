package com.oxygen.www.module.find.adapter;

import com.oxygen.www.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SelectCityAdapter extends BaseAdapter {
	private String[] cities;
	private LayoutInflater mInflater;

	public SelectCityAdapter(Context context, String[] cities) {
		this.cities = cities;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (cities != null && cities.length <= 0) {
			return 0;
		}
		return cities.length;
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
			holder.name = (TextView) convertView.findViewById(R.id.tv_type_name);
 			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(cities[position]);
		return convertView;
	}
	
	public  class ViewHolder {
		public TextView name;
	}

}
