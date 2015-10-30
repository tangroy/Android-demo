package com.oxygen.www.module.sport.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.RankUser;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.activity.EventSalemanageActivity;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.widget.CircleImageView;

/**
 * 战绩秀 适配器
 * @author 杨庆雷
 * 2015-6-19下午2:42:17
 */

public class SaleManageAdapter extends BaseAdapter {

	private EventSalemanageActivity c;
	private LayoutInflater mInflater;
	private ArrayList<RankUser> ranks;
	private JSONObject json_user;
	private String ranking;
	DecimalFormat df = new DecimalFormat("#0.0");
	boolean ismanager;
	public SaleManageAdapter(EventSalemanageActivity c,
			ArrayList<RankUser> ranks, JSONObject json_user, String ranking,boolean ismanager) {
		this.c = c;
		mInflater = LayoutInflater.from(c);
		this.ranks = ranks;
		this.json_user = json_user;
		this.ranking = ranking;
		this.ismanager = ismanager;
	}

	@Override
	public int getCount() {
		return ranks.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("NewApi") @Override
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_rank, null);
			holder.tv_sale = (TextView) convertView.findViewById(R.id.tv_sale);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.iv_headimg = (CircleImageView) convertView.findViewById(R.id.iv_headimg);
			holder.tv_type_data = (TextView) convertView
					.findViewById(R.id.tv_type_data);
			holder.event_record = (ImageView) convertView.findViewById(R.id.event_record);
			holder.st_manage = (CheckBox) convertView
					.findViewById(R.id.st_manage);
 			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(ismanager){
			holder.st_manage.setVisibility(View.VISIBLE);
		}else{
			holder.st_manage.setVisibility(View.GONE);
			
			LayoutParams params = (LayoutParams) holder.tv_type_data.getLayoutParams();
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
			params.rightMargin = (int) (10* OxygenApplication.ppi);
			holder.tv_type_data.setLayoutParams(params);
			
//			holder.st_manage.setVisibility(View.INVISIBLE);
//			holder.st_manage.setClickable(false);
		}
		JSONObject user_jsonobject = null;
		try {
			user_jsonobject = json_user.getJSONObject(ranks.get(arg0)
					.getUser_id() + "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		holder.tv_sale.setText(arg0 + 1 + "");
		if(user_jsonobject!=null){
		User user = UsersConstruct.ToUser(user_jsonobject);
		holder.tv_name.setText(user.getNickname() == null ? "--" : user
				.getNickname());
		ImageUtil.showImage(user.getHeadimgurl(), holder.iv_headimg, R.drawable.icon_def);
		
		if ("yes".equals(ranks.get(arg0).getManual())) {
			holder.event_record.setVisibility(View.VISIBLE);
		} else {
			holder.event_record.setVisibility(View.GONE);
		}
		
		int duration = ranks.get(arg0).getDuration();
		// 距离
		if (ranking.equals(Constants.ranking[0])) {
			holder.tv_type_data.setText(df.format(ranks.get(arg0)
					.getDistance() / 1000) + "公里");
			c.type_title.setText(Constants.ranking_chn[0]);
		}
		// 速度
		else if (ranking.equals(Constants.ranking[1])) {
			holder.tv_type_data
			.setText(ranks.get(arg0).getSpeed() + "公里/时");
			c.type_title.setText(Constants.ranking_chn[1]);
		}
		// pace
		else if (ranking.equals(Constants.ranking[2])) {
			holder.tv_type_data.setText(ranks.get(arg0).getPace() + "/公里");
			c.type_title.setText(Constants.ranking_chn[2]);
		}
		// 卡路里
		else if (ranking.equals(Constants.ranking[3])) {
			holder.tv_type_data
			.setText(ranks.get(arg0).getCalorie() + "大卡");
			c.type_title.setText(Constants.ranking_chn[3]);
		}
		// 比分
		else if (ranking.equals(Constants.ranking[4])) {
			holder.tv_type_data.setText(ranks.get(arg0).getMatch_win()
					+ ":" + ranks.get(arg0).getMatch_lose());
			c.type_title.setText(Constants.ranking_chn[4]);
		}
		// 得分
		else if (ranking.equals(Constants.ranking[5])) {
			holder.tv_type_data.setText(ranks.get(arg0).getScore());
			c.type_title.setText(Constants.ranking_chn[5]);
		}
		// 时长
		else {
			holder.tv_type_data.setText(GDUtil.TransitionTime(duration));
			c.type_title.setText(Constants.ranking_chn[5]);
		}
		
		if(ranks.get(arg0).getIs_rank()==0){
			holder.st_manage.setChecked(false);
		}
		else if(ranks.get(arg0).getIs_rank()==1){
			holder.st_manage.setChecked(true);
		}
		}
		holder.st_manage
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton v,
							boolean arg1) {
						if(arg1){
							c.enable_user_rank(ranks.get(arg0).getUser_id());
						}else
							c.disable_user_rank(ranks.get(arg0).getUser_id());
 						
					}
				});
		return convertView;
	}

	public  class ViewHolder {
		public TextView tv_sale;
		public TextView tv_name;
 		public TextView tv_type_data;
		public CheckBox st_manage;
		public CircleImageView iv_headimg;
		public ImageView event_record;
	}

}
