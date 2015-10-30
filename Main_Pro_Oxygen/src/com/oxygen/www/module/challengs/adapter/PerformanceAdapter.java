package com.oxygen.www.module.challengs.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.enties.GDAcvitity;
import com.oxygen.www.enties.Photo;

public class PerformanceAdapter extends BaseAdapter {

	private ArrayList<GDAcvitity> activitice;
	private Context c;
	private LayoutInflater inflater;
	DecimalFormat df = new DecimalFormat("#0.00");

	public PerformanceAdapter(ArrayList<GDAcvitity> activitice, Context c) {
		inflater = LayoutInflater.from(c);
		this.c = c;
		this.activitice = activitice;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return activitice.size();
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
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.item_challenges_performance_running, null);
			holder.tv_distance = (TextView) convertView
					.findViewById(R.id.tv_distance);
			holder.tv_starttime = (TextView) convertView
					.findViewById(R.id.tv_starttime);
			holder.tv_source = (TextView) convertView
					.findViewById(R.id.tv_source);
			holder.iv_manual = (ImageView) convertView
					.findViewById(R.id.iv_manual);
			holder.arrow = (ImageView) convertView
					.findViewById(R.id.arrow);
			holder.gv_photes = (GridView) convertView
					.findViewById(R.id.gv_photes);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (activitice.get(arg0).getManual().equals("yes")) {
			holder.iv_manual.setVisibility(View.VISIBLE);
		} else {
			holder.iv_manual.setVisibility(View.GONE);
		}
		
		GDAcvitity gdAcvitity = activitice.get(arg0);
		
		String source = gdAcvitity.getSource();
		if ("xiaomi".equals(source)) {
			//  小米手环数据
			holder.tv_source.setVisibility(View.VISIBLE);
			holder.arrow.setVisibility(View.INVISIBLE);
			holder.tv_starttime.setText(gdAcvitity.getCreated_at().substring(0, 10));
			holder.tv_distance.setText(df.format((gdAcvitity.getDistance()/1000)) + "km");
			
			
		} else {
			// native
			holder.tv_source.setVisibility(View.GONE);
			holder.arrow.setVisibility(View.VISIBLE);
			holder.tv_distance.setText(df
					.format(activitice.get(arg0).getDistance() / 1000) + "km");
			holder.tv_starttime.setText(activitice.get(arg0).getStart_time());
			ArrayList<Photo> photos = activitice.get(arg0).getPhotos();
//		if (photos != null && photos.size() > 0) {
//			holder.gv_photes.setVisibility(View.VISIBLE);
//			PhotosAdapter ptotesAdapter = new PhotosAdapter(c, photos);
//			holder.gv_photes.setAdapter(ptotesAdapter);
//		} else {
			holder.gv_photes.setVisibility(View.GONE);
//		}
		}
		

		return convertView;
	}

	public  class ViewHolder {
		public TextView tv_distance, tv_starttime;
		public TextView tv_source;
		public ImageView iv_manual;
		public ImageView arrow;
		public GridView gv_photes;
	}

}
