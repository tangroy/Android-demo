package com.oxygen.www.module.sport.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.GDAcvitity;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.widget.CircleImageView;

/**
 * 战绩秀排名适配器
 * @author 杨庆雷
 * 2015-6-11上午11:48:42
 */
public class RankAdapter extends BaseAdapter {

	private Context c;
	private LayoutInflater mInflater;
	private ArrayList<GDAcvitity> ranklist;
	private JSONObject json_user;
	private String ranking;
	DecimalFormat df = new DecimalFormat("#0.00");
	private User current_user;
	public RankAdapter(Context c, ArrayList<GDAcvitity> ranklist,
			JSONObject json_user, String ranking,User current_user) {
		this.c = c;
		mInflater = LayoutInflater.from(c);
		this.ranklist = ranklist;
		this.json_user = json_user;
		this.ranking = ranking;
		this.current_user = current_user;
	}

	@Override
	public int getCount() {
		return ranklist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_result_rank, null);
			holder.tv_sale = (TextView) convertView.findViewById(R.id.tv_sale);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.iv_head = (CircleImageView) convertView.findViewById(R.id.iv_head);
			holder.tv_type_data = (TextView) convertView.findViewById(R.id.tv_type_data);
			holder.event_rankfirst = (ImageView) convertView.findViewById(R.id.event_rankfirst);
			holder.event_record = (ImageView) convertView.findViewById(R.id.event_record);
 			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//第一名
		if(arg0 == 0){
			holder.event_rankfirst.setVisibility(View.VISIBLE);
		}else {
			holder.event_rankfirst.setVisibility(View.GONE);
		}
		if("yes".equals(ranklist.get(arg0).getManual())){
			holder.event_record.setVisibility(View.VISIBLE);
		}else {
			holder.event_record.setVisibility(View.GONE);
		}
		JSONObject user_jsonobject = null;
		try {
			user_jsonobject = json_user.getJSONObject(ranklist.get(arg0).
					getUser_id()+ "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		holder.tv_sale.setText(ranklist.get(arg0).getRank() + "");
		User user = null;
		if(user_jsonobject!=null){
		user = UsersConstruct.ToUser(user_jsonobject);
		holder.tv_name.setText(user.getNickname() == null ? "--" : user
				.getNickname());
		if(user.getId()==current_user.getId()){
			holder.tv_name.setTextColor(c.getResources().getColor(R.color.red));
		}else{
			holder.tv_name.setTextColor(c.getResources().getColor(R.color.black));
		}
		}
		else{
			holder.tv_name.setText("");

		}
		int duration = ranklist.get(arg0).getDuration();
		//距离
		if (ranking.equals(Constants.ranking[0])) {
			 holder.tv_type_data.setText(df
			 .format(ranklist.get(arg0).getDistance() / 1000) + "公里");
		}
		// 速度
		else if (ranking.equals(Constants.ranking[1])) {
			holder.tv_type_data.setText(ranklist.get(arg0).getSpeed()+"公里/时");
		} 
		//pace
		else if (ranking.equals(Constants.ranking[2])) {
			holder.tv_type_data.setText(ranklist.get(arg0).getPace()+"/公里");
		}
		//卡路里
		else if (ranking.equals(Constants.ranking[3])) {
			holder.tv_type_data.setText(ranklist.get(arg0).getCalorie()+"大卡");
		}
		//比分
		else if (ranking.equals(Constants.ranking[4])) {
			holder.tv_type_data.setText(ranklist.get(arg0).getMatch_win()+":"+ranklist.get(arg0).getMatch_lose());
		}
		//得分
		else if (ranking.equals(Constants.ranking[5])) {
			holder.tv_type_data.setText(ranklist.get(arg0).getScore()+"");
		}
		//时长
		else{
			 holder.tv_type_data.setText(GDUtil.TransitionTime(duration));
		}
		if(user!=null){
			ImageUtil.showImage(user.getHeadimgurl()+Constants.qiniu_photo_head, holder.iv_head, R.drawable.icon_def);

		}
		return convertView;
	}

	public  class ViewHolder {
		public TextView tv_sale;
		public TextView tv_name;
 		public TextView tv_type_data;
 		public ImageView iv_head;
 		public ImageView event_rankfirst;
 		public ImageView event_record;

	}

}
