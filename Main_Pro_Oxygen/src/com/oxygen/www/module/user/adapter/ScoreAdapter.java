package com.oxygen.www.module.user.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.Summary;
import com.oxygen.www.utils.GDUtil;

public class ScoreAdapter extends BaseAdapter {

	private ArrayList<Summary> summarys;
	private LayoutInflater mLi;
	private Context context;
	DecimalFormat df = new DecimalFormat("#0.0");

	public ScoreAdapter(ArrayList<Summary> summarys, Context context) {
		this.summarys = summarys;
		// 将要分页显示的View装入数组中
		mLi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return summarys.size();
	}

	public static class ViewHolder {
		ImageView iv_sport;
		TextView tv_count;
		TextView tv_date;
		TextView tv_time;
		TextView tv_dataname,lab_time;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return summarys.get(arg0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		holder = new ViewHolder();
		// 初始化
		convertView = mLi.inflate(R.layout.item_score, null);
		holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
		holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
		holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
		holder.iv_sport = (ImageView) convertView.findViewById(R.id.iv_sport);
		holder.lab_time = (TextView) convertView.findViewById(R.id.lab_time);
		holder.tv_dataname = (TextView) convertView
				.findViewById(R.id.tv_dataname);
		holder.tv_count.setText(summarys.get(position).getCount() + "");
		holder.lab_time.setText("累计小时");
		holder.iv_sport.setImageDrawable(GDUtil.engSporttodrawable(context,
				"icon_eventdetail_" + summarys.get(position).getSport()));
		holder.tv_time.setText(df.format(summarys.get(position)
				.getTotal_duration() / 3600));
		int category =GDUtil.SportCategory(summarys.get(position).getSport());
		if ( category== Constants.COUNT_CATEGORY_DISTANCE||category==Constants.COUNT_CATEGORY_RUNNING||category==Constants.COUNT_CATEGORY_SWIMMING) {
			holder.tv_date.setText(df.format(summarys.get(position)
					.getTotal_distance() / 1000));
			holder.tv_dataname.setText("总公里数");
		} else if(category==Constants.COUNT_CATEGORY_OTHER) {
 			holder.tv_date.setText("");
			holder.tv_dataname.setText("");
			holder.tv_time.setText("");
			holder.lab_time.setText("");
		}else if(category== Constants.COUNT_CATEGORY_BASKETBALL||category==Constants.COUNT_CATEGORY_FOOTBALL){
			holder.tv_date.setText(summarys.get(position).getMatch());
			holder.tv_dataname.setText("总比分");
		}else{
			holder.tv_date.setText("");
			holder.tv_dataname.setText("");
		}
		if(position%2==0){
			holder.iv_sport.setBackgroundColor(Color.parseColor("#00A2FF"));
		}
		return convertView;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
}
