package com.oxygen.www.module.sport.adapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.GDAcvitity;
import com.oxygen.www.enties.Sport;
import com.oxygen.www.enties.Team;
import com.oxygen.www.module.challengs.construct.ChallengesConstruct;
import com.oxygen.www.module.sport.construct.ActivitiesConstruct;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.TextUtils;

public class MyActivityTimeAdapter extends BaseAdapter {

	private Context c;
	private LayoutInflater mInflater;
	private ArrayList<Sport> sports;
	DecimalFormat df = new DecimalFormat("#0.00");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Typeface typeFace;

	public MyActivityTimeAdapter(Context c, ArrayList<Sport> sports) {
		this.c = c;
		this.sports = sports;
		mInflater = LayoutInflater.from(c);
		typeFace = Typeface.createFromAsset(c.getAssets(), "fonts/Impact.ttf");
	}

	@Override
	public int getCount() {
		return sports != null && sports.size() > 0 ? sports.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return sports.get(arg0);
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
			convertView = mInflater.inflate(R.layout.item_mysport, null);
			
			holder.rl_title = (RelativeLayout) convertView
					.findViewById(R.id.rl_title); 
			holder.rl_xiaomi = (RelativeLayout) convertView
					.findViewById(R.id.rl_xiaomi); 
			holder.tv_xiaomi_date = (TextView) convertView
					.findViewById(R.id.tv_xiaomi_date);
			holder.tv_xiaomi_step = (TextView) convertView
					.findViewById(R.id.tv_xiaomi_step);
			holder.tv_xiaomi_distance = (TextView) convertView
					.findViewById(R.id.tv_xiaomi_distance);
			
			holder.tv_sport_title = (TextView) convertView
					.findViewById(R.id.tv_sport_title);
			holder.tv_sport_time = (TextView) convertView
					.findViewById(R.id.tv_sport_time);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.tv_accecp = (TextView) convertView
					.findViewById(R.id.tv_accecp);
			holder.tv_timeicon = (TextView) convertView
					.findViewById(R.id.tv_timeicon);
			holder.rl_date = (RelativeLayout) convertView
					.findViewById(R.id.rl_date);
			holder.ll_starting_twoteam = (RelativeLayout) convertView
					.findViewById(R.id.ll_starting_twoteam);
			holder.ll_starting = (RelativeLayout) convertView
					.findViewById(R.id.ll_starting);

			holder.rl_challenges = (RelativeLayout) convertView
					.findViewById(R.id.rl_challenges);
			holder.iv_sportpic = (ImageView) convertView
					.findViewById(R.id.iv_sportpic);
			holder.iv_champion_bteam = (ImageView) convertView
					.findViewById(R.id.iv_champion_bteam);
			holder.iv_champion_ateam = (ImageView) convertView
					.findViewById(R.id.iv_champion_ateam);
			holder.iv_me_ateam = (ImageView) convertView
					.findViewById(R.id.iv_me_ateam);
			holder.iv_me_bteam = (ImageView) convertView
					.findViewById(R.id.iv_me_bteam);
			holder.ll_waitplan = (LinearLayout) convertView
					.findViewById(R.id.ll_waitplan);
			holder.ll_bottom_data = (LinearLayout) convertView
					.findViewById(R.id.ll_bottom_data);
			holder.ll_overplan = (FrameLayout) convertView
					.findViewById(R.id.ll_overplan);
			holder.tv_data = (TextView) convertView.findViewById(R.id.tv_data);
			holder.tv_unit = (TextView) convertView.findViewById(R.id.tv_unit);
			holder.tv_duration = (TextView) convertView
					.findViewById(R.id.tv_duration);
			holder.tv_nodata = (TextView) convertView
					.findViewById(R.id.tv_nodata);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			
			holder.tv_lab_left = (TextView) convertView
					.findViewById(R.id.tv_lab_left);
			holder.tv_lab_right = (TextView) convertView
					.findViewById(R.id.tv_lab_right);
			holder.tv_up_left = (TextView) convertView
					.findViewById(R.id.tv_up_left);
			holder.tv_up_right = (TextView) convertView
					.findViewById(R.id.tv_up_right);
			holder.tv_down_left = (TextView) convertView
					.findViewById(R.id.tv_down_left);
			
			holder.tv_nostart_teama = (TextView) convertView
					.findViewById(R.id.tv_nostart_teama);
			holder.tv_nostart_teamb = (TextView) convertView
					.findViewById(R.id.tv_nostart_teamb);
			holder.tv_down_right = (TextView) convertView
					.findViewById(R.id.tv_down_right);
			holder.pb_distance = (ProgressBar) convertView
					.findViewById(R.id.pb_distance);
			holder.tv_data.setTypeface(typeFace);
			holder.tv_duration.setTypeface(typeFace);
			holder.tv_synchronize = (TextView) convertView
					.findViewById(R.id.tv_synchronize);
			holder.tv_synchronize_timeline = (TextView) convertView
					.findViewById(R.id.tv_synchronize_timeline);
			holder.tv_leader = (TextView) convertView
					.findViewById(R.id.tv_leader);
			holder.tv_status = (TextView) convertView
					.findViewById(R.id.tv_status);
			holder.tv_challenges_starttime = (TextView) convertView
					.findViewById(R.id.tv_challenges_starttime);
			holder.tv_declaration = (TextView) convertView
					.findViewById(R.id.tv_declaration);
			holder.tv_rank = (TextView) convertView.findViewById(R.id.tv_rank);
			holder.tv_challenges_distance = (TextView) convertView
					.findViewById(R.id.tv_challenges_distance);
			holder.tv_rank_lab = (TextView) convertView
					.findViewById(R.id.tv_rank_lab);
			holder.tv_distance_lab = (TextView) convertView
					.findViewById(R.id.tv_distance_lab);
			holder.tv_champion = (TextView) convertView
					.findViewById(R.id.tv_champion);
			holder.tv_noteam = (TextView) convertView
					.findViewById(R.id.tv_noteam);
			holder.tv_group_name = (TextView) convertView
					.findViewById(R.id.tv_group_name);
			holder.ll_synchronize_timeline = (LinearLayout) convertView
					.findViewById(R.id.ll_synchronize_timeline);
			holder.ll_nostart = (LinearLayout) convertView
					.findViewById(R.id.ll_nostart);
			holder.ll_nostart_twoteam = (LinearLayout) convertView
					.findViewById(R.id.ll_nostart_twoteam);
			holder.top_link = (View) convertView.findViewById(R.id.top_link);
			holder.champion_link = (View) convertView
					.findViewById(R.id.champion_link);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Sport sport = sports.get(arg0);
		
		// 数据来源
		String source = sport.getSource();
		if ("xiaomi".equals(source)) {
			// 小米手环-类型
			holder.rl_xiaomi.setVisibility(View.VISIBLE);
			
			holder.top_link.setVisibility(View.GONE);
			holder.rl_title.setVisibility(View.GONE);
			holder.ll_bottom_data.setVisibility(View.GONE);
			holder.ll_overplan.setVisibility(View.GONE);
			holder.rl_challenges.setVisibility(View.GONE);
			holder.ll_waitplan.setVisibility(View.GONE);
			holder.ll_synchronize_timeline.setVisibility(View.GONE);
			
			String mi_date = sport.getCreated_at().substring(0, 10);
			int mi_step;
			double mi_distance;
			
			String sport_data = sport.getSport_data();
			try {
				//				Log.i("sport_data", sport_data);
				JSONObject json = new JSONObject(sport_data);

				mi_step  = json.getInt("step");
				mi_distance = json.getDouble("distance");
				holder.tv_xiaomi_date.setText(mi_date.substring(8, 10) + "／"+ mi_date.substring(5, 7) + "月");
				holder.tv_xiaomi_distance.setText(df.format(mi_distance/1000));
				holder.tv_xiaomi_step.setText(""+mi_step);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
				
			
		} else {
			holder.rl_xiaomi.setVisibility(View.GONE);
			holder.top_link.setVisibility(View.VISIBLE);
			holder.rl_title.setVisibility(View.VISIBLE);
		}

		
		String date = sport.start_time;
		if(date==null){
			date = "2015-00-00 00:00:00";
		}
		if (sport.getSynchronize() == 0 && sport.getTarget_id() == -1) {
			holder.ll_synchronize_timeline.setVisibility(View.VISIBLE);
			holder.tv_synchronize.setVisibility(View.GONE);
		} else if (sport.getSynchronize() == 0) {
			holder.ll_synchronize_timeline.setVisibility(View.GONE);
			holder.tv_synchronize.setVisibility(View.VISIBLE);
		} else {
			holder.ll_synchronize_timeline.setVisibility(View.GONE);
			holder.tv_synchronize.setVisibility(View.GONE);
		}
		if (date != null && date.length() > 10) {
			int hour = Integer.parseInt(date.substring(11, 13));
			if (hour > 6 && hour < 18) {
				Drawable drawable = c.getResources().getDrawable(
						R.drawable.icon_am);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				holder.tv_timeicon.setCompoundDrawables(drawable, null, null,
						null);
			} else {
				Drawable drawable = c.getResources().getDrawable(
						R.drawable.icon_pm);
				// / 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				holder.tv_timeicon.setCompoundDrawables(drawable, null, null,
						null);
			}
			holder.tv_timeicon.setText(date.substring(11, 16));
			String title = sport.getTitle();
			if (title != null && !title.equals("null")) {
				holder.tv_sport_title.setText(title);
			} else {
				holder.tv_sport_title.setText(GDUtil.engforchn(c,
						sport.getSport() == null ? "" : sport.getSport()));
			}

		}

		holder.tv_leader.setText( sport.getNickName()==null?"":"发起者:" +sport.getNickName());
		String endtime = sport.getEnd_time();
		if (sport.getTarget_type().equals("Challenge")) {

			if (date != null && date.length() > 10) {
				//					holder.tv_status.setText(GDUtil
//							.getTimeDiffforindexforchallengesday(
//									sdf.parse(date), sdf.parse(endtime)));
				holder.tv_status.setText(sport.getDays_left());
			}
			// nostart未开始，starting 正在挑战中，end 未已结束
			String challenges_status = "nostart";
			try {
				if (!GDUtil.isDateBefore(sdf.parse(date))) {
					if (GDUtil.isDateBefore(sdf.parse(endtime))) {
						challenges_status = "starting";
					} else {
						challenges_status = "end";
					}
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			holder.rl_challenges.setVisibility(View.VISIBLE);
			holder.ll_waitplan.setVisibility(View.GONE);
			holder.ll_overplan.setVisibility(View.GONE);
			holder.ll_bottom_data.setVisibility(View.GONE);
			String challenges_type = sport.getChallenge_type();

			if ("nostart".equals(challenges_status)) {
				holder.top_link.setBackgroundColor(c.getResources().getColor(
						R.color.index_title_bg));

				holder.ll_nostart.setVisibility(View.VISIBLE);
				holder.ll_starting_twoteam.setVisibility(View.GONE);
				holder.ll_starting.setVisibility(View.GONE);
				if (challenges_type.equals("total_distance_team")) {
					// 显示团队信息
					ArrayList<Team> teams = null;
					try {
						teams = ChallengesConstruct.toArrayteam(new JSONArray(
								sport.getSport_data()));
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					holder.ll_nostart_twoteam.setVisibility(View.VISIBLE);
					holder.iv_icon.setImageDrawable(c.getResources()
							.getDrawable(
									R.drawable.icon_index_challenges_twoteam));
					holder.tv_nostart_teama.setText(teams.get(0).getName());
					holder.tv_nostart_teamb.setText(teams.get(1).getName());
				} else if (challenges_type.equals("total_distance")) {
					holder.ll_nostart_twoteam.setVisibility(View.GONE);
					holder.iv_icon.setImageDrawable(c.getResources()
							.getDrawable(
									R.drawable.icon_index_challenges_person));

				} else if (challenges_type.equals("total_distance_group")) {
					holder.ll_nostart_twoteam.setVisibility(View.GONE);
					holder.iv_icon.setImageDrawable(c.getResources()
							.getDrawable(
									R.drawable.icon_index_challenges_moreteam));
				}
				if (date != null && date.length() > 10) {
					holder.tv_challenges_starttime.setText(" "
							+ date.substring(0, date.length() - 3));

				}
				holder.tv_declaration.setText(" 挑战宣言：" + sport.getIntro());
			} else {
				holder.ll_nostart.setVisibility(View.GONE);
				try {
					if (challenges_type.equals("total_distance_team")) {
						holder.ll_starting_twoteam.setVisibility(View.VISIBLE);
						holder.ll_starting.setVisibility(View.GONE);
						// 显示团队信息
						ArrayList<Team> teams = null;
						try {
							teams = ChallengesConstruct
									.toArrayteam(new JSONArray(sport
											.getSport_data()));
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						holder.iv_icon
								.setImageDrawable(c
										.getResources()
										.getDrawable(
												R.drawable.icon_index_challenges_twoteam_o));

						// 保证数据安全，必须要有两个队
						if (teams != null && teams.size() > 1) {
							holder.pb_distance
									.setProgressDrawable(c
											.getResources()
											.getDrawable(
													R.drawable.progressbar_mini_center));
							double teama_distance = teams.get(0).distance / 1000;
							double teamb_distance = teams.get(1).distance / 1000;
							holder.tv_up_left.setText(teams.get(0).getName());
							holder.tv_up_right.setText(teams.get(1).getName());
							holder.tv_down_left.setText(df
									.format(teama_distance) + "公里");
							holder.tv_down_right.setText(df
									.format(teamb_distance) + "公里");
							if (teama_distance == 0 && teamb_distance == 0) {
								holder.pb_distance.setProgress(50);
							} else if (teama_distance == 0) {
								holder.pb_distance.setProgress(1);
							} else if (teamb_distance == 0) {
								holder.pb_distance.setProgress(99);
							} else {
								holder.pb_distance
										.setProgress((int) (teama_distance
												/ (teama_distance + teamb_distance) * 100));
							}
							if (sport.getTeam_id() == teams.get(0).getId()) {
								holder.iv_me_ateam.setVisibility(View.VISIBLE);
								holder.iv_me_bteam.setVisibility(View.GONE);
							} else if (sport.getTeam_id() == teams.get(1)
									.getId()) {
								holder.iv_me_bteam.setVisibility(View.VISIBLE);
								holder.iv_me_ateam.setVisibility(View.GONE);
							} else {
								holder.iv_me_bteam.setVisibility(View.GONE);
								holder.iv_me_ateam.setVisibility(View.GONE);
							}

							if ("end".equals(challenges_status)) {
								holder.top_link.setBackgroundColor(c
										.getResources().getColor(
												R.color.index_title_bg));

								if (teama_distance < teamb_distance) {
									holder.iv_champion_bteam
											.setVisibility(View.VISIBLE);
									holder.iv_champion_ateam
											.setVisibility(View.GONE);
								} else {
									holder.iv_champion_bteam
											.setVisibility(View.GONE);
									holder.iv_champion_ateam
											.setVisibility(View.VISIBLE);
								}
							} else {
								holder.top_link.setBackgroundColor(c
										.getResources().getColor(
												R.color.challenges_twoteam));

								holder.iv_champion_bteam
										.setVisibility(View.GONE);
								holder.iv_champion_ateam
										.setVisibility(View.GONE);
							}
						}
					} else if (challenges_type.equals("total_distance")) {
						holder.tv_group_name.setVisibility(View.GONE);
						holder.ll_starting_twoteam.setVisibility(View.GONE);
						holder.ll_starting.setVisibility(View.VISIBLE);
						holder.tv_rank.setVisibility(View.VISIBLE);
						holder.tv_rank_lab.setVisibility(View.VISIBLE);
						holder.tv_challenges_distance
								.setVisibility(View.VISIBLE);
						holder.tv_distance_lab.setVisibility(View.VISIBLE);
						holder.tv_champion.setVisibility(View.VISIBLE);
						holder.tv_noteam.setVisibility(View.GONE);
						holder.iv_icon
								.setImageDrawable(c
										.getResources()
										.getDrawable(
												R.drawable.icon_index_challenges_person_o));

						// 个人跑量
						JSONObject total_jsonobject = new JSONObject(
								sport.getSport_data());
						double performance = total_jsonobject
								.getInt("performance");

						holder.tv_rank.setText(total_jsonobject.getInt("rank")
								+ "");
						double leader = total_jsonobject.getInt("leader");
						holder.tv_challenges_distance.setText(df
								.format(performance / 1000));
						holder.tv_champion.setText("第一名:"
								+ df.format(leader / 1000) + "公里");
						if ("end".equals(challenges_status)) {
							holder.top_link.setBackgroundColor(c.getResources()
									.getColor(R.color.index_title_bg));
						} else {
							holder.top_link.setBackgroundColor(c.getResources()
									.getColor(R.color.challenges_person));
						}

					} else if (challenges_type.equals("total_distance_group")) {
						holder.ll_starting_twoteam.setVisibility(View.GONE);
						holder.ll_starting.setVisibility(View.VISIBLE);
						holder.iv_icon
								.setImageDrawable(c
										.getResources()
										.getDrawable(
												R.drawable.icon_index_challenges_moreteam_o));
						if (sport.getSport_data() != null) {
							holder.champion_link.setVisibility(View.VISIBLE);
							holder.tv_group_name.setVisibility(View.VISIBLE);

							holder.tv_rank.setVisibility(View.VISIBLE);
							holder.tv_rank_lab.setVisibility(View.VISIBLE);
							holder.tv_challenges_distance
									.setVisibility(View.VISIBLE);
							holder.tv_distance_lab.setVisibility(View.VISIBLE);
							holder.tv_champion.setVisibility(View.VISIBLE);
							holder.tv_noteam.setVisibility(View.GONE);
							JSONObject total_distance_group = new JSONObject(
									sport.getSport_data());
							double leader = total_distance_group.getJSONObject(
									"0").getDouble("distance");
							if (!total_distance_group.isNull("1")) {
								double performance = total_distance_group
										.getJSONObject("1").getDouble(
												"distance");
								holder.tv_rank
										.setText(total_distance_group
												.getJSONObject("1").getInt(
														"rank")
												+ "");
								holder.tv_challenges_distance.setText(df
										.format(performance / 1000));

								holder.tv_group_name
										.setText(total_distance_group
												.getJSONObject("1").getString(
														"group_name"));
							} else {
								holder.tv_rank.setText("1");
								holder.tv_challenges_distance.setText(df
										.format(total_distance_group
												.getJSONObject("0").getDouble(
														"distance") / 1000));
								holder.tv_group_name
										.setText(total_distance_group
												.getJSONObject("0").getString(
														"group_name"));
							}
							holder.tv_champion.setText("第一名:"
									+ df.format(leader / 1000) + "公里");
							if ("end".equals(challenges_status)) {
								holder.top_link.setBackgroundColor(c
										.getResources().getColor(
												R.color.index_title_bg));
							} else {
								holder.top_link.setBackgroundColor(c
										.getResources().getColor(
												R.color.challenges_moreteam));
							}
						} else {
							// 没有团队
							holder.champion_link.setVisibility(View.GONE);
							holder.tv_group_name.setVisibility(View.GONE);
							holder.tv_rank.setVisibility(View.GONE);
							holder.tv_rank_lab.setVisibility(View.GONE);
							holder.tv_challenges_distance
									.setVisibility(View.GONE);
							holder.tv_distance_lab.setVisibility(View.GONE);
							holder.tv_champion.setVisibility(View.GONE);
							holder.tv_noteam.setVisibility(View.VISIBLE);
							if ("end".equals(challenges_status)) {
								holder.top_link.setBackgroundColor(c
										.getResources().getColor(
												R.color.index_title_bg));
								holder.tv_noteam.setText("挑战已结束，无团队应战");
							} else {
								holder.top_link.setBackgroundColor(c
										.getResources().getColor(
												R.color.challenges_moreteam));
								holder.tv_noteam.setText("快邀请团队加入挑战吧");
							}

						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {
			holder.top_link.setBackgroundColor(c.getResources().getColor(
					R.color.index_title_bg));
			holder.rl_challenges.setVisibility(View.GONE);
			holder.ll_waitplan.setVisibility(View.GONE);
			holder.ll_overplan.setVisibility(View.VISIBLE);
			holder.iv_icon.setImageDrawable(GDUtil.engSporttodrawable(c,
					"icon_index_" + sport.getSport()));
			try {
				if (date != null && !GDUtil.isDateBefore(sdf.parse(date))) {
					holder.tv_status.setText(date.substring(8, 10) + "／"
							+ date.substring(5, 7) + "月");
				} else {
					holder.tv_status.setText("未开始");
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			if (sport.getSport_data() != null) {
				try {
					GDAcvitity performance = ActivitiesConstruct
							.Toactivity(new JSONObject(sport.getSport_data()));
					boolean sportstatus = false;
					try {
						sportstatus = GDUtil.isDateBefore(sdf.parse(date));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if (sportstatus) {
						holder.ll_waitplan.setVisibility(View.VISIBLE);
						holder.ll_bottom_data.setVisibility(View.GONE);
						holder.ll_overplan.setVisibility(View.GONE);
						if (date != null && date.length() > 10) {
							holder.tv_sport_time.setText(" "
									+ date.substring(0, date.length() - 3));
						}
						String address = null;
						if (sport.getaddresss() != null
								&& !sport.getAddress().equals("")) {
							address = " " + sport.getaddresss().trim();
						} else {
							address = " 待定";
						}
						holder.tv_address.setText(address);
						holder.tv_accecp.setText(" "
								+ sport.accept_count
								+ "/"
								+ (sport.getLimitation() == 0 ? "不限" : sport
										.getLimitation()));
					} else {
						holder.ll_overplan.setVisibility(View.VISIBLE);
						holder.ll_waitplan.setVisibility(View.GONE);
						if (performance.status.equals("open")) {
							holder.tv_nodata.setVisibility(View.VISIBLE);
						} else {
							holder.tv_nodata.setVisibility(View.GONE);
						}
						int i = c.getResources().getIdentifier(
								"bg_eventdetail_" + sport.getSport(),
								"drawable", c.getPackageName());

						//holder.iv_sportpic.setImageResource(i);

						String imagepath = (performance.getPic() + Constants.qiniu_photo_find)
								.trim();

						if (!imagepath.equals(holder.iv_sportpic.getTag())) {
							holder.iv_sportpic
									.setTag((performance.getPic() + Constants.qiniu_photo_find)
											.trim());
							ImageUtil
									.showImage2(
											(performance.getPic() + Constants.qiniu_photo_find)
													.trim(),
											holder.iv_sportpic, i);
						}

						int duration = performance.getDuration();
						// 平板支撑类和時長类只显示时长
						holder.tv_duration.setText(GDUtil
								.TransitionTime(duration));
						int category = GDUtil
								.SportCategory(performance.sport_eng);
						if (category == Constants.COUNT_CATEGORY_PLANK
								|| category == Constants.COUNT_CATEGORY_DURATION) {
							holder.rl_date.setVisibility(View.GONE);
							holder.tv_lab_right.setText("总计时长");
							holder.ll_bottom_data.setVisibility(View.VISIBLE);
						} else if (category == Constants.COUNT_CATEGORY_OTHER) {
							holder.ll_bottom_data.setVisibility(View.GONE);
						} else if (category == Constants.COUNT_CATEGORY_DISTANCE
								|| category == Constants.COUNT_CATEGORY_RUNNING
								|| category == Constants.COUNT_CATEGORY_SWIMMING) {
							holder.rl_date.setVisibility(View.VISIBLE);
							holder.tv_unit.setVisibility(View.VISIBLE);
							holder.tv_lab_right.setText("总计时长");
							holder.ll_bottom_data.setVisibility(View.VISIBLE);
							holder.tv_data.setText(df.format(performance
									.getDistance() / 1000) + "");
							holder.tv_lab_left.setText("全程距离");

						} else {
							holder.tv_lab_left.setText("比分");
							holder.tv_unit.setVisibility(View.GONE);
							holder.rl_date.setVisibility(View.VISIBLE);
							holder.ll_bottom_data.setVisibility(View.VISIBLE);
							holder.tv_data.setText(performance.match_win + ":"
									+ performance.match_lose);
							if (category == Constants.COUNT_CATEGORY_BASKETBALL
									|| category == Constants.COUNT_CATEGORY_FOOTBALL) {
								int score = performance.getScore();
								if (category == Constants.COUNT_CATEGORY_BASKETBALL) {
									holder.tv_lab_right.setText("得分");
								} else {
									holder.tv_lab_right.setText("进球");
								}
								if (score > 0) {
									holder.tv_duration.setText(score + "");
								} else {
									holder.tv_duration.setText("");
									holder.tv_lab_right.setText("");
								}
							}

						}

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return convertView;
		
	}

	public class ViewHolder {
		public TextView tv_sport_title;
		public TextView tv_sport_time;
		public TextView tv_address;
		public TextView tv_accecp;
		public TextView tv_timeicon, tv_lab_left, tv_lab_right;
		public FrameLayout ll_overplan;
		public LinearLayout ll_waitplan, ll_bottom_data;
		public RelativeLayout rl_challenges, rl_date, ll_starting_twoteam,
				ll_starting;
		public TextView tv_data, tv_unit, tv_duration, tv_nodata, tv_up_left,
				tv_up_right, tv_down_left, tv_down_right, tv_nostart_teama,
				tv_nostart_teamb;
		public ImageView iv_icon, iv_sportpic, iv_champion_bteam,
				iv_champion_ateam, iv_me_ateam, iv_me_bteam;
		public ProgressBar pb_distance;
		public TextView tv_synchronize, tv_synchronize_timeline, tv_leader,
				tv_status, tv_challenges_starttime, tv_declaration, tv_rank,
				tv_challenges_distance, tv_rank_lab, tv_distance_lab,
				tv_champion, tv_noteam, tv_group_name;
		public LinearLayout ll_synchronize_timeline, ll_nostart,
				ll_nostart_twoteam;
		public View top_link, champion_link;
		
		public RelativeLayout rl_title;
		
		public RelativeLayout rl_xiaomi;
		public TextView tv_xiaomi_date;
		public TextView tv_xiaomi_step;
		public TextView tv_xiaomi_distance;

	}

}
