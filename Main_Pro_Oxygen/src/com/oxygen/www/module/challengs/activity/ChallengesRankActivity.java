package com.oxygen.www.module.challengs.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.style.TtsSpan.ElectronicBuilder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Challenges;
import com.oxygen.www.enties.ChallengesUser;
import com.oxygen.www.enties.Team;
import com.oxygen.www.module.challengs.adapter.PersonChallengesAdapter;
import com.oxygen.www.module.challengs.adapter.TeamChallengesAdapter;
import com.oxygen.www.module.challengs.construct.ChallengesConstruct;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.CircleImageView;
import com.oxygen.www.widget.NoScrollListView;

public class ChallengesRankActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	
	/**
	 * 挑战 id
	 */
	private int challengesid;
	/**
	 * 获取数据
	 */
	private final int NET_SHOWCHALLENGES = 1;
	private JSONObject Current_userinfo;
	private RelativeLayout rl_loading;
	
	private ImageView iv_back;
	private ListView lv_persondatalist, lv_moreteamdatalist;
	private CircleImageView iv_ateam_head, iv_bteam_head, iv_head_group_2,
			iv_head_group_1, iv_head_group_3;
	private TextView tv_ateam_name, tv_bteam_name, tv_atema_distance,
			tv_btema_distance, tv_joined_a, tv_joined_b, tv_name_group_2,
			tv_name_group_1, tv_name_group_3, tv_score_group_2,
			tv_score_group_1, tv_score_group_3;
	private LinearLayout ll_joined;
	private ProgressBar pb_distance;
	private String type;
	private ArrayList<ArrayList<ChallengesUser>> leaderboard;
	private ArrayList<ChallengesUser> team_rank_a;
	private ArrayList<ChallengesUser> team_rank_b;
	private JSONObject jsonobject_userinfo;
	int myuser_id[];
	private Challenges challenges;
	private RelativeLayout rl_twoteam_date;
	private LinearLayout ll_rank_moreteam_data, ll_rank_group_2,
			ll_rank_group_1, ll_rank_group_3;
	private NoScrollListView lv_persondatalist_a, lv_persondatalist_b;
	DecimalFormat df = new DecimalFormat("#0.00");
	ArrayList<ChallengesUser> other_ranks;
	private ScrollView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challengs_rank);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		lv_persondatalist = (ListView) findViewById(R.id.lv_persondatalist);
		rl_twoteam_date = (RelativeLayout) findViewById(R.id.rl_twoteam_date);
		iv_ateam_head = (CircleImageView) findViewById(R.id.iv_ateam_head);
		iv_bteam_head = (CircleImageView) findViewById(R.id.iv_bteam_head);
		tv_joined_a = (TextView) findViewById(R.id.tv_joined_a);
		tv_joined_b = (TextView) findViewById(R.id.tv_joined_b);
		ll_joined = (LinearLayout) findViewById(R.id.ll_joined);
		tv_atema_distance = (TextView) findViewById(R.id.tv_atema_distance);
		tv_btema_distance = (TextView) findViewById(R.id.tv_btema_distance);
		pb_distance = (ProgressBar) findViewById(R.id.pb_distance);
		tv_ateam_name = (TextView) findViewById(R.id.tv_ateam_name);
		tv_bteam_name = (TextView) findViewById(R.id.tv_bteam_name);
		lv_persondatalist_a = (NoScrollListView) findViewById(R.id.lv_persondatalist_a);
		lv_persondatalist_b = (NoScrollListView) findViewById(R.id.lv_persondatalist_b);
		sv = (ScrollView) findViewById(R.id.sv);
		ll_rank_moreteam_data = (LinearLayout) findViewById(R.id.ll_rank_moreteam_data);
		tv_name_group_2 = (TextView) findViewById(R.id.tv_name_group_2);
		tv_name_group_1 = (TextView) findViewById(R.id.tv_name_group_1);
		tv_name_group_3 = (TextView) findViewById(R.id.tv_name_group_3);
		tv_score_group_2 = (TextView) findViewById(R.id.tv_score_group_2);
		tv_score_group_1 = (TextView) findViewById(R.id.tv_score_group_1);
		tv_score_group_3 = (TextView) findViewById(R.id.tv_score_group_3);
		iv_head_group_2 = (CircleImageView) findViewById(R.id.iv_head_group_2);
		iv_head_group_1 = (CircleImageView) findViewById(R.id.iv_head_group_1);
		iv_head_group_3 = (CircleImageView) findViewById(R.id.iv_head_group_3);
		lv_moreteamdatalist = (ListView) findViewById(R.id.lv_moreteamdatalist);
		ll_rank_group_2 = (LinearLayout) findViewById(R.id.ll_rank_group_2);
		ll_rank_group_1 = (LinearLayout) findViewById(R.id.ll_rank_group_1);
		ll_rank_group_3 = (LinearLayout) findViewById(R.id.ll_rank_group_3);

	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		lv_persondatalist_b.setOnItemClickListener(this);
		lv_persondatalist_a.setOnItemClickListener(this);
		lv_persondatalist.setOnItemClickListener(this);
		lv_moreteamdatalist.setOnItemClickListener(this);
		ll_rank_group_2.setOnClickListener(this);
		ll_rank_group_1.setOnClickListener(this);
		ll_rank_group_3.setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		if (arg0 == lv_persondatalist || arg0== lv_persondatalist_a
				|| arg0 == lv_persondatalist_b) {
			String userid = null;
			double distance = 0.0;
			if (arg0 == lv_persondatalist) {
				userid = leaderboard.get(0).get(arg2).getUser_id() + "";
				distance = leaderboard.get(0).get(arg2).getDistance();
			}
			if (arg0 == lv_persondatalist_a) {
				distance = team_rank_a.get(arg2).getDistance();
				userid = team_rank_a.get(arg2).getUser_id() + "";
			}
			if (arg0 == lv_persondatalist_b) {
				distance = team_rank_b.get(arg2).getDistance();
				userid = team_rank_b.get(arg2).getUser_id() + "";
			}

			double maxdistanceorfrist = 0.0;
			if (type.equals(Constants.CHALLENGES_TYPE_PERSON)) {
				maxdistanceorfrist = leaderboard.get(0).get(0).getDistance();
			} else if (type.equals(Constants.CHALLENGES_TYPE_TWOTEAM)) {
				if (arg0.getId() == R.id.lv_persondatalist_a) {
					maxdistanceorfrist = team_rank_a.get(0).getDistance();
				} else if (arg0.getId() == R.id.lv_persondatalist_b) {
					maxdistanceorfrist = team_rank_b.get(0).getDistance();
				}
			}
			Intent intent = new Intent(ChallengesRankActivity.this,
					PersonPerformance.class);
			intent.putExtra("type", type);
			try {
				intent.putExtra("user", UsersConstruct
						.ToUser(jsonobject_userinfo.getJSONObject(userid)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			intent.putExtra("maxdistanceorfrist", maxdistanceorfrist);
			intent.putExtra("challengesid", challenges.getId());
			startActivity(intent);
		} else if (arg0 == lv_moreteamdatalist) {
			Intent intent_m = new Intent(ChallengesRankActivity.this,
					GroupLeaderboardActivity.class);
			intent_m.putExtra("challenge_id", challenges.getId());
			intent_m.putExtra("group_id", other_ranks.get(arg2).getGroup_id());
			startActivity(intent_m);
		}
	}
	
	private void initValues() {
		challengesid = getIntent().getIntExtra("challengesid", 0);
		type = this.getIntent().getStringExtra("type");
		getChallengesDetailNet(challengesid);
	}
	
	/**
	 * 获取服务器events信息
	 */
	private void getChallengesDetailNet(final int challengesid) {
		rl_loading.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.CHALLENGES_SHOW_GET + challengesid
						+ ".json", handler, NET_SHOWCHALLENGES);
			}
		});
	}

	private void UpdateUi() {
		leaderboard = challenges.getLeaderboard();
		if (type.equals(Constants.CHALLENGES_TYPE_PERSON)) {
			myuser_id = new int[1];
			if (null == challenges.getCurrent_challenge_user()) {
				myuser_id = null;
			} else {
				myuser_id[0] = challenges.getCurrent_challenge_user().getUser_id();
			}

			lv_persondatalist.setVisibility(View.VISIBLE);
			if (jsonobject_userinfo != null) {
				PersonChallengesAdapter person_adapter = new PersonChallengesAdapter(
						leaderboard.get(0), ChallengesRankActivity.this,
						jsonobject_userinfo, myuser_id, false);
				lv_persondatalist.setAdapter(person_adapter);
			}
		} else if (type.equals(Constants.CHALLENGES_TYPE_TWOTEAM)) {
			sv.setVisibility(View.VISIBLE);
			ChallengesUser current_challenge_user = challenges
					.getCurrent_challenge_user();
			String accept_status;
			if (current_challenge_user != null) {
				accept_status = current_challenge_user.getStatus();
			} else {
				// 未产生任何关联
				accept_status = "notaccept";
			}
			// 未报名状态
			if (!accept_status.equals("accept")) {
				ll_joined.setVisibility(View.GONE);
			} else {
				ll_joined.setVisibility(View.VISIBLE);
				// 判断加入了哪个队伍
				if (current_challenge_user.getTeam_id() == challenges
						.getAteam().getId()) {
					tv_joined_a.setText("已加入");
					tv_joined_b.setText("");
				} else {
					tv_joined_a.setText("");
					tv_joined_b.setText("已加入");
				}
			}
			Team ateam = challenges.getAteam();
			Team bteam = challenges.getBteam();
			ImageUtil.showImage(ateam.getPic(), iv_ateam_head,
					R.drawable.icon_def);
			ImageUtil.showImage(bteam.getPic(), iv_bteam_head,
					R.drawable.icon_def);
			ArrayList<ArrayList<ChallengesUser>> leaderboard = challenges
					.getLeaderboard();
			if (leaderboard != null && leaderboard.size() > 0) {
				tv_ateam_name.setText((ateam.getName().length() > 15 ? ateam
						.getName().substring(0, 15) : ateam.getName())
						+ "("
						+ leaderboard.get(0).size() + "人)");
				tv_bteam_name.setText("("
						+ leaderboard.get(1).size()
						+ "人)"
						+ (bteam.getName().length() > 15 ? bteam.getName()
								.substring(0, 15) : bteam.getName()));
			}
			tv_atema_distance.setText(df.format(ateam.getDistance() / 1000)
					+ "km");
			tv_btema_distance.setText(df.format(bteam.getDistance() / 1000)
					+ "km");
			double Adistance = ateam.getDistance();
			double Bdistance = bteam.getDistance();
			if (Adistance == 0 && Bdistance == 0) {
				pb_distance.setProgress(50);
			} else if (Adistance == 0) {
				pb_distance.setProgress(1);
			} else if (Bdistance == 0) {
				pb_distance.setProgress(99);
			} else {
				pb_distance.setProgress((int) (Adistance
						/ (Adistance + Bdistance) * 100));
			}

			if (leaderboard != null && leaderboard.size() > 0) {
				team_rank_a = leaderboard.get(0);
				TeamChallengesAdapter teamadaptera = new TeamChallengesAdapter(
						team_rank_a, ChallengesRankActivity.this,
						jsonobject_userinfo, 0);
				ViewGroup.LayoutParams params_a = lv_persondatalist_a
						.getLayoutParams();
				params_a.height = leaderboard.get(0).size()
						* ((int) (110 * OxygenApplication.ppi));
				lv_persondatalist_a.setLayoutParams(params_a);
				lv_persondatalist_a.setAdapter(teamadaptera);
				if (leaderboard.size() > 1) {
					team_rank_b = leaderboard.get(1);
					TeamChallengesAdapter teamadapterb = new TeamChallengesAdapter(
							team_rank_b, ChallengesRankActivity.this,
							jsonobject_userinfo, 1);
					ViewGroup.LayoutParams params_b = lv_persondatalist_b
							.getLayoutParams();
					params_b.height = leaderboard.get(1).size()
							* ((int) (110 * OxygenApplication.ppi));
					lv_persondatalist_b.setLayoutParams(params_b);
					lv_persondatalist_b.setAdapter(teamadapterb);
				}
				sv.smoothScrollTo(0, 0);
			}
		} else if (type.equals(Constants.CHALLENGES_TYPE_MORETEAM)) {
			ll_rank_moreteam_data.setVisibility(View.VISIBLE);
			UpdateGroupChanllengesRank(challenges.getGroups_leaderboard());
		}
	}

	/**
	 * 更新多队挑战排名
	 * 
	 * @param leaderboard
	 */
	void UpdateGroupChanllengesRank(ArrayList<ChallengesUser> leaderboard) {
		int group_count = leaderboard.size();
		// 第一名
		if (group_count > 0) {
			ChallengesUser groups_1 = leaderboard.get(0);
			ImageUtil.showImage(groups_1.getPic(), iv_head_group_1,
					R.drawable.icon_def);
			tv_name_group_1.setText(groups_1.getGroup_name());
			tv_score_group_1.setText(df.format(groups_1.getDistance() / 1000)
					+ "km");
		}
		// 第二名
		if (group_count > 1) {
			ChallengesUser groups_2 = leaderboard.get(1);
			ImageUtil.showImage(groups_2.getPic(), iv_head_group_2,
					R.drawable.icon_def);
			tv_name_group_2.setText(groups_2.getGroup_name());
			tv_score_group_2.setText(df.format(groups_2.getDistance() / 1000)
					+ "km");
		}
		// 第三名
		if (group_count > 2) {
			ChallengesUser groups_3 = leaderboard.get(2);
			ImageUtil.showImage(groups_3.getPic(), iv_head_group_3,
					R.drawable.icon_def);
			tv_name_group_3.setText(groups_3.getGroup_name());
			tv_score_group_3.setText(df.format(groups_3.getDistance() / 1000)
					+ "km");
		}
		if (group_count > 3) {
			other_ranks = new ArrayList<ChallengesUser>();
			// 获取属于我的团队的id
			ArrayList<ChallengesUser> my_group_performance = challenges
					.getMy_group_performance();
			int my_group_count = my_group_performance.size();
			if (my_group_count > 0) {
				myuser_id = new int[my_group_count];
				for (int i = 0; i < my_group_count; i++) {
					myuser_id[i] = my_group_performance.get(i).getGroup_id();
				}
			}
			// 获取前三名之外的团队的成绩
			for (int i = 0; i < group_count - 3; i++) {
				other_ranks.add(leaderboard.get(i + 3));
			}
			if (other_ranks.size() > 0) {
				PersonChallengesAdapter other_adapter = new PersonChallengesAdapter(
						other_ranks, ChallengesRankActivity.this,
						jsonobject_userinfo, myuser_id, true);
				lv_moreteamdatalist.setAdapter(other_adapter);
			}

		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.ll_rank_group_2:
			Intent intent_2 = new Intent(ChallengesRankActivity.this,
					GroupLeaderboardActivity.class);
			intent_2.putExtra("challenge_id", challenges.getId());
			if (challenges.groups_leaderboard.size() > 1) {
				intent_2.putExtra("group_id", challenges.groups_leaderboard
						.get(1).getGroup_id());
				startActivity(intent_2);
			}
			break;
		case R.id.ll_rank_group_1:
			Intent intent_1 = new Intent(ChallengesRankActivity.this,
					GroupLeaderboardActivity.class);
			intent_1.putExtra("challenge_id", challenges.getId());
			if (challenges.groups_leaderboard.size() > 0) {
				intent_1.putExtra("group_id", challenges.groups_leaderboard
						.get(0).getGroup_id());
				startActivity(intent_1);
			}
			break;
		case R.id.ll_rank_group_3:
			Intent intent_3 = new Intent(ChallengesRankActivity.this,
					GroupLeaderboardActivity.class);
			intent_3.putExtra("challenge_id", challenges.getId());
			if (challenges.groups_leaderboard.size() > 2) {
				intent_3.putExtra("group_id", challenges.groups_leaderboard
						.get(2).getGroup_id());
				startActivity(intent_3);
			}
			break;
		default:
			break;
		}

	}
	
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			
			case NET_SHOWCHALLENGES:
				if (msg.obj != null) {
					try {
						JSONObject jsoninfos = new JSONObject((String) msg.obj);
						if (jsoninfos.getInt("status") == 200) {
							
							challenges = ChallengesConstruct.tocChallenges(jsoninfos.getJSONObject("data"), true);
							
							jsonobject_userinfo = jsoninfos.getJSONObject("users_info");
							
							Current_userinfo = jsoninfos.getJSONObject("current_user");
							
							UpdateUi();

						} else {
							ToastUtil.show(ChallengesRankActivity.this, "请求服务器异常");
						}
						rl_loading.setVisibility(View.GONE);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					rl_loading.setVisibility(View.GONE);
				}

				break;
			default:
				break;
			}
		}

	};

}
