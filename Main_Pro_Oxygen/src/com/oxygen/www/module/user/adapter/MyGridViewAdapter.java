package com.oxygen.www.module.user.adapter;

import java.util.List;
import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.utils.GDUtil;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyGridViewAdapter extends BaseAdapter {

	private Context c;
	public List<String> sportSelectedNames;
	public MyGridViewAdapter(Context c,List<String> sportSelectedNames) {
		this.c = c;
		this.sportSelectedNames = sportSelectedNames;
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
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = View.inflate(c, R.layout.item_sport,
					null);
		} else {
			view = convertView;
		}
		CheckBox cb_sport = (CheckBox) view.findViewById(R.id.cb_sport);
		TextView tv_sport = (TextView) view.findViewById(R.id.tv_sport);
		if (sportSelectedNames != null && sportSelectedNames.contains(Constants.sports[position])) {
			cb_sport.setChecked(true);
		}
		cb_sport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sportSelectedNames != null 
						&& !sportSelectedNames.contains(Constants.sports[position])) {
					sportSelectedNames.add(Constants.sports[position]);
				} else {
					sportSelectedNames.remove(Constants.sports[position]);
				}
			}
		});
		cb_sport.setBackgroundDrawable(GDUtil.engSporttodrawable(c,"iv_userinfo_selector_"+Constants.sports[position]));
		tv_sport.setText(GDUtil.engforchn(c, "created_type_"+Constants.sports[position]));
		return view;
	}

}
