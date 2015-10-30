package com.oxygen.www.module.challengs.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.ChallengesUser;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.widget.CircleImageView;

public class PersonChallengesAdapter extends BaseAdapter {

	private ArrayList<ChallengesUser> ranks;
	private Context c;
	private LayoutInflater inflater;
	private JSONObject jsonobject_userinfo;
	DecimalFormat df = new DecimalFormat("#0.00");
	double person_frist;
	int[] myuser_id;
	boolean isgroup;

	// private Boolean[] tags;

	public PersonChallengesAdapter(ArrayList<ChallengesUser> ranks, Context c,
			JSONObject jsonobject_userinfo, int[] myuser_id,boolean isgroup) {
		this.c = c;
		this.ranks = ranks;
		this.jsonobject_userinfo = jsonobject_userinfo;
		inflater = LayoutInflater.from(c);
		this.myuser_id = myuser_id;
		this.isgroup = isgroup;
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

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.item_challenges_rank_person, null);
			holder.tv_posotion = (TextView) convertView
					.findViewById(R.id.tv_posotion);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_score = (TextView) convertView
					.findViewById(R.id.tv_score);
			holder.pb_distance = (ProgressBar) convertView
					.findViewById(R.id.pb_distance);
			holder.iv_head = (CircleImageView) convertView
					.findViewById(R.id.iv_head);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			ChallengesUser challenges = ranks.get(arg0);
			int id = 0;
			if(isgroup){
				id = challenges.getGroup_id();
				ImageUtil.showImage(challenges.pic
						+ Constants.qiniu_photo_head, holder.iv_head,
						R.drawable.icon_def);
				holder.tv_name.setText(challenges.getGroup_name());
			}else{
				User user = UsersConstruct.ToUser(jsonobject_userinfo
						.getJSONObject(challenges.getUser_id() + ""));
				id = user.getId();
				ImageUtil.showImage(user.getHeadimgurl()
						+ Constants.qiniu_photo_head, holder.iv_head,
						R.drawable.icon_def);
				holder.tv_name.setText(user.getNickname());
			}
			
			if(myuser_id!=null&&myuser_id.length>0){
				for (int i = 0; i < myuser_id.length; i++) {
					// 我
					if (id == myuser_id[i]) {
						holder.tv_posotion.setTextColor(c.getResources().getColor(
								R.color.red));
						holder.tv_name.setTextColor(c.getResources().getColor(
								R.color.red));
						holder.tv_score.setTextColor(c.getResources().getColor(
								R.color.red));
						break;
					} else {
						holder.tv_posotion.setTextColor(c.getResources().getColor(
								R.color.black));
						holder.tv_name.setTextColor(c.getResources().getColor(
								R.color.black));
						holder.tv_score.setTextColor(c.getResources().getColor(
								R.color.black));
					}
				}
			}
			holder.tv_posotion.setText(challenges.getRank() + "");
			double distance = challenges.getDistance() / 1000;
			holder.tv_score.setText(df.format(distance) + "km");
			if (arg0 == 0) {
				person_frist = distance;
			}
			if (distance == 0.0) {
				holder.pb_distance.setProgress(1);
			} else {
				holder.pb_distance
						.setProgress((int) (distance / person_frist * 80));
			}

		} catch (JSONException e) {
			e.printStackTrace();

		}
		return convertView;
	}

	public  class ViewHolder {
		public TextView tv_posotion, tv_name, tv_score;
		public CircleImageView iv_head;
		public ProgressBar pb_distance;
	}

}
