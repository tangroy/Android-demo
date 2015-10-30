package com.oxygen.www.module.find.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.Businesses;
import com.oxygen.www.utils.ImageUtil;

public class BusinessesListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Businesses> businessess;

	public BusinessesListAdapter(Context c, List<Businesses> businessess) {
		this.businessess = businessess;
		mInflater = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		return businessess != null && businessess.size() > 0 ? businessess
				.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return businessess.get(arg0);
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
			convertView = mInflater.inflate(R.layout.item_businesslist, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_tel = (TextView) convertView.findViewById(R.id.tv_tel);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.iv_head = (ImageView) convertView
					.findViewById(R.id.iv_head);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setText(businessess.get(arg0).getName());
		holder.tv_tel.setText(businessess.get(arg0).getTelephone());
		holder.tv_address.setText(businessess.get(arg0).getAddress());
		ImageUtil.showImage((businessess.get(arg0)
				.getS_photo_url()+Constants.qiniu_photo_business).trim(), holder.iv_head,R.drawable.nopic);
		return convertView;
	}

	public  class ViewHolder {
		public TextView tv_name;
		public ImageView iv_head;
		public TextView tv_tel;
		public TextView tv_address;

	}

}
