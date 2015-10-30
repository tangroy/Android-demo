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

public class TeamChallengesAdapter extends BaseAdapter {

	private ArrayList<ChallengesUser> ranks;
	private LayoutInflater inflater;
	private JSONObject jsonobject_userinfo;
	int team = 0;
	DecimalFormat df = new DecimalFormat("#0.00");
	double person_frist;

	public TeamChallengesAdapter(ArrayList<ChallengesUser> ranks, Context c,
			JSONObject jsonobject_userinfo, int team) {
		this.ranks = ranks;
		this.jsonobject_userinfo = jsonobject_userinfo;
		inflater = LayoutInflater.from(c);
		this.team = team;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ranks.size();
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
			// team a
			if (team == 0) {
				convertView = inflater.inflate(
						R.layout.item_challenges_rank_team_left, null);
				// team b
			} else {
				convertView = inflater.inflate(
						R.layout.item_challenges_rank_team_right, null);
			}
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
				User user = UsersConstruct.ToUser(jsonobject_userinfo
						.getJSONObject(challenges.getUser_id() + ""));
				ImageUtil.showImage(user.getHeadimgurl()+Constants.qiniu_photo_head, holder.iv_head,
						R.drawable.icon_def);
				holder.tv_name.setText(user.getNickname());
				double distance = challenges.getDistance() / 1000;
				holder.tv_score.setText(df.format(distance) + "km");
				if (arg0 == 0) {
					person_frist = distance;
				}
				// team a
				if (team == 0) {
					if(distance==0.0){
						holder.pb_distance.setProgress(1);
					}else{
						holder.pb_distance.setProgress((int) (distance
								/ person_frist * 80));
					}
					
					// team b
				} else {
					if(distance==0.0){
						holder.pb_distance.setProgress(99);
					}else{
					holder.pb_distance.setProgress(100 - (int) (distance
							/ person_frist * 80));
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		return convertView;
	}

	public  class ViewHolder {
		public TextView tv_name, tv_score;
		public CircleImageView iv_head;
		public ProgressBar pb_distance;
	}

}
