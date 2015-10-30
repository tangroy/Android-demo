package com.oxygen.www.module.challengs.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.utils.GDUtil;

public class GalleryAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context c;

	public GalleryAdapter(Context c) {
		inflater = LayoutInflater.from(c);
		this.c = c;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 60;
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
		 TextView textView = new TextView(c);
		 textView.setText(arg0*5+5+"");
		 textView.setTextColor(c.getResources().getColor(R.color.white));
		 textView.setBackground(c.getResources().getDrawable(R.drawable.wane_shape_black));
		 textView.setGravity(Gravity.CENTER);
 		 textView.setLayoutParams(new Gallery.LayoutParams(80, 80));
	        return textView;
	}

	public class ViewHolder {
 
	}
}