package com.oxygen.www.module.user.adapter;

import java.util.List;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.enties.Task;
import com.oxygen.www.enties.User;
import com.oxygen.www.utils.ImageUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LevelCoinAdapter extends BaseAdapter {
	private Context c;
	private LayoutInflater mInflater;
	private List<Task> tasks;
	public LevelCoinAdapter(Context c,List<Task> tasks) {
		this.c = c;
		mInflater = LayoutInflater.from(c);
		this.tasks = tasks;
	}
	@Override
	public int getCount() {
		if(tasks == null){
			return 0;
		}
		return tasks.size();
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
		convertView = mInflater.inflate(R.layout.item_levelcoin, null);
		ImageView task_img = (ImageView)convertView.findViewById(R.id.task_img);
		TextView task_name = (TextView)convertView.findViewById(R.id.task_name);
		TextView task_coin = (TextView)convertView.findViewById(R.id.task_coin);
		task_name.setText(tasks.get(position).getTitle());
		String imgUrl = UrlConstants.API_PREFIX+"/m/app/public/images/mylevel/"+"mylevel"+tasks.get(position).getAction().replace("/", "_")+".png";
		ImageUtil.showImage(imgUrl, task_img, R.drawable.icon_def);
		task_coin.setText("+"+tasks.get(position).getCoins());
		if("yes".equals(tasks.get(position).getCompleted())){
			task_img.setImageDrawable(c.getResources().getDrawable(R.drawable.icon_task_copleted));
			task_name.setTextColor(c.getResources().getColor(R.color.mygray));
		}else{
			task_name.setTextColor(c.getResources().getColor(R.color.black));
		}
		return convertView;
	}

}
