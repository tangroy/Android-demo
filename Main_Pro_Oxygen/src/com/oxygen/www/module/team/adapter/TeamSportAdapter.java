package com.oxygen.www.module.team.adapter;

import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.utils.GDUtil;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TeamSportAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context c;
	public TeamSportAdapter(Context c) {
		this.c = c;
		inflater = LayoutInflater.from(c);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Constants.sports.length;
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
			convertView = inflater.inflate(R.layout.item_teamsport, parent,false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.sport_image);
			holder.name = (TextView) convertView.findViewById(R.id.sport_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final String sport = Constants.sports[position];
		Drawable drawable = GDUtil.engSporttodrawable(c,"icon_created_" + sport);
		holder.image.setImageDrawable(drawable);
		holder.name.setText(GDUtil.engforchn(c, "created_type_" + sport));
		return convertView;
	}
	
	public class ViewHolder {
		public ImageView image;
		private TextView name;
	}

}
