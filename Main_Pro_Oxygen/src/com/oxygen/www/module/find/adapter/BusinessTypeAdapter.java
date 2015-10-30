package com.oxygen.www.module.find.adapter;

import java.util.List;
import com.oxygen.www.R;
import com.oxygen.www.enties.Categories;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BusinessTypeAdapter extends BaseAdapter {
	private List<Categories> categories;
	private LayoutInflater mInflater;
	public BusinessTypeAdapter(Context context,List<Categories> categories){
		this.categories = categories;
		mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return categories.size()+1;
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
		if(position == 0){
			holder.tv_type_name.setText("全部类型");
		}else{
			holder.tv_type_name.setText(categories.get(position-1).getName());
		}
		return convertView;
	}
	
	public  class ViewHolder {
		public TextView tv_type_name;
	}

}
