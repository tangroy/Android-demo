package com.oxygen.www.module.sport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.utils.GDUtil;

public class ChooseSportAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context c;
	String[] sports;

	/**
	 * 
	 * @param c
	 * @param sports
	 * @param type 0表示感興趣。1表示全部
	 */
	public ChooseSportAdapter(Context c, String sports[]) {
		inflater = LayoutInflater.from(c);
		this.c = c;
		this.sports = sports;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sports.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_choosesport, arg2,
					false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.image);
			holder.name = (TextView) convertView
					.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.image.setImageDrawable(GDUtil.engSporttodrawable(c,
					"icon_created_" + sports[arg0].trim()));
		holder.name.setText(GDUtil.engforchn(c, "created_type_"+sports[arg0].trim()));
		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
		private TextView name;
	}
}