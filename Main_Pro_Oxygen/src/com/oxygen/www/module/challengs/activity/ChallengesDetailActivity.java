package com.oxygen.www.module.challengs.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.MenuActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Challenges;
import com.oxygen.www.enties.ChallengesUser;
import com.oxygen.www.enties.Group;
import com.oxygen.www.enties.MessageConfig;
import com.oxygen.www.enties.Moment;
import com.oxygen.www.enties.Team;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.challengs.adapter.ChallengeMomentAdapter;
import com.oxygen.www.module.challengs.adapter.PersonChallengesAdapter;
import com.oxygen.www.module.challengs.construct.ChallengesConstruct;
import com.oxygen.www.module.challengs.eventbus_enties.ListViewAddHeight;
import com.oxygen.www.module.sport.activity.EventsResultActivity;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.module.team.activity.ChooseGroupToChallengesActivity;
import com.oxygen.www.module.team.activity.ChooseSelectTeamtoChanllengesActivity;
import com.oxygen.www.module.team.activity.TeamQRActivity;
import com.oxygen.www.module.user.activity.InviteFriendActivity;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.QQUtils;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.Untilly;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.utils.WxUtil;
import com.oxygen.www.widget.CircleImageView;
import com.oxygen.www.widget.NoScrollListView;

import de.greenrobot.event.EventBus;

/**
 * 挑战详情页
 * 
 * @author sambatang
 * 
 */
public class ChallengesDetailActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	private LinearLayout ll_accept, ll_join, ll_joined, ll_rank_person,
			ll_rank_moreteam, ll_rank_moreteam_data, ll_rank_group_2,
			ll_rank_group_1, ll_rank_group_3;
	private RelativeLayout rl_twoteam_date, rl_rank_moreteam,
			ll_rank_moreteam_nodata;
	private ImageView iv_back, iv_share, iv_challenges_type, iv_share_weixin,
			iv_share_appfriend, iv_share_qq, iv_share_weixin_friends,
			iv_banner;
	private CircleImageView iv_ateam_head, iv_bteam_head, iv_head_me,
			iv_head_group_2, iv_head_group_1, iv_head_group_3;
	private TextView tv_join_a, tv_join_b, tv_rule, tv_title,
			tv_challenges_title, tv_challenger, tv_accept, tv_deline,
			tv_declaration, tv_ateam_name, tv_bteam_name, tv_atema_distance,
			tv_btema_distance, cancel_challenges, exit_challenges,
			challenges_QR,challenges_data, tv_joined_a, tv_joined_b, tv_personrank_count,
			tv_moreteamrank_count, iv_share_challenges, btn_invate,
			tv_moreteam_join, tv_name_group_2, tv_name_group_1,
			tv_name_group_3, tv_score_group_2, tv_score_group_1,
			tv_score_group_3, tv_challenges_time;
	private ScrollView sv;
	private PopupWindow popupWindow;
	private String type;
	private Intent intent;
	private ProgressBar progressbar, pb_distance;
	private final int NET_SHOWCHALLENGES = 1;
	private final int NET_JOIN_TEAM = 2;
	private final int NET_CANCEL = 3;
	private final int NET_DECLINE = 4;
	private final int DIALOG_CANCELCHALLENGES = 5;
	private final int DIALOG_EXITCHALLENGES = 6;
	private final int CHALLENGES_GROUP_NET = 7;
	protected static final int CHALLENGES_GROUP_DECLINE = 8;
	protected static final int CHALLENGES_GROUP_INVITE = 9;
	protected static final int POST_COMENT = 10;
	/**
	 * 获取弹出金币个数
	 */
	private static final int NET_COIN_CNT = 11;
	private final int NET_JOIN_TEAM_EXIT = 12;
	protected static final int NOTIFICATION_SET = 13;
	private int challengesid = 0;
	private String Shareurl;
	private Challenges challenges;
	private JSONObject jsonobject_userinfo;
	private JSONObject Current_userinfo;
	private ArrayList<ChallengesUser> Groups_leaderboard;
	private ArrayList<ChallengesUser> my_group_performance;
	int[] myuser_id;
	private List<Moment> moments;
	/**
	 * 当前用户
	 */
	ChallengesUser current_challenge_user;
	/**
	 * 报名状态
	 */
	String accept_status;
	DecimalFormat df = new DecimalFormat("#0.00");
	// private ArrayList<ChallengesUser> person_rank;

	private NoScrollListView lv_persondatalist, lv_persondatalist_a,
			lv_persondatalist_b;
	private ListView lv_moreteamdatalist;
	private NoScrollListView lv_chanlleng_message;
	private TextView message_count;
	private ArrayList<ChallengesUser> other_ranks;
	private ArrayList<Group> groups = null;
	private String is_group_leader = "no";
	private String is_created_by = "no";
	private boolean is_setNessage ;
	/**
	 * 个人挑战排行榜
	 */
	private ArrayList<ChallengesUser> ranks_new;
	private String set_message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		setContentView(R.layout.activity_challengs_detail);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_share = (ImageView) findViewById(R.id.iv_share);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_banner = (ImageView) findViewById(R.id.iv_banner);
		iv_challenges_type = (ImageView) findViewById(R.id.iv_challenges_type);
		iv_head_me = (CircleImageView) findViewById(R.id.iv_head_me);
		iv_ateam_head = (CircleImageView) findViewById(R.id.iv_ateam_head);
		iv_bteam_head = (CircleImageView) findViewById(R.id.iv_bteam_head);
		iv_head_group_2 = (CircleImageView) findViewById(R.id.iv_head_group_2);
		iv_head_group_1 = (CircleImageView) findViewById(R.id.iv_head_group_1);
		iv_head_group_3 = (CircleImageView) findViewById(R.id.iv_head_group_3);
		tv_challenger = (TextView) findViewById(R.id.tv_challenger);
		tv_declaration = (TextView) findViewById(R.id.tv_declaration);
		tv_join_a = (TextView) findViewById(R.id.tv_join_a);
		tv_join_b = (TextView) findViewById(R.id.tv_join_b);
		tv_joined_a = (TextView) findViewById(R.id.tv_joined_a);
		tv_joined_b = (TextView) findViewById(R.id.tv_joined_b);
		tv_challenges_time = (TextView) findViewById(R.id.tv_challenges_time);
		tv_rule = (TextView) findViewById(R.id.tv_rule);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_challenges_title = (TextView) findViewById(R.id.tv_challenges_title);
		tv_challenger = (TextView) findViewById(R.id.tv_challenger);
		tv_accept = (TextView) findViewById(R.id.tv_accept);
		tv_deline = (TextView) findViewById(R.id.tv_deline);
		tv_ateam_name = (TextView) findViewById(R.id.tv_ateam_name);
		tv_bteam_name = (TextView) findViewById(R.id.tv_bteam_name);
		tv_atema_distance = (TextView) findViewById(R.id.tv_atema_distance);
		tv_btema_distance = (TextView) findViewById(R.id.tv_btema_distance);
		tv_personrank_count = (TextView) findViewById(R.id.tv_personrank_count);
		tv_moreteamrank_count = (TextView) findViewById(R.id.tv_moreteamrank_count);
		iv_share_challenges = (TextView) findViewById(R.id.iv_share_challenges);
		tv_moreteam_join = (TextView) findViewById(R.id.tv_moreteam_join);
		tv_name_group_2 = (TextView) findViewById(R.id.tv_name_group_2);
		tv_name_group_1 = (TextView) findViewById(R.id.tv_name_group_1);
		tv_name_group_3 = (TextView) findViewById(R.id.tv_name_group_3);
		tv_score_group_2 = (TextView) findViewById(R.id.tv_score_group_2);
		tv_score_group_1 = (TextView) findViewById(R.id.tv_score_group_1);
		tv_score_group_3 = (TextView) findViewById(R.id.tv_score_group_3);
		ll_join = (LinearLayout) findViewById(R.id.ll_join);
		ll_accept = (LinearLayout) findViewById(R.id.ll_accept);
		ll_joined = (LinearLayout) findViewById(R.id.ll_joined);
		ll_rank_person = (LinearLayout) findViewById(R.id.ll_rank_person);
		ll_rank_moreteam = (LinearLayout) findViewById(R.id.ll_rank_moreteam);
		ll_rank_moreteam_data = (LinearLayout) findViewById(R.id.ll_rank_moreteam_data);
		ll_rank_group_2 = (LinearLayout) findViewById(R.id.ll_rank_group_2);
		ll_rank_group_1 = (LinearLayout) findViewById(R.id.ll_rank_group_1);
		ll_rank_group_3 = (LinearLayout) findViewById(R.id.ll_rank_group_3);
		rl_twoteam_date = (RelativeLayout) findViewById(R.id.rl_twoteam_date);
		rl_rank_moreteam = (RelativeLayout) findViewById(R.id.rl_rank_moreteam);
		ll_rank_moreteam_nodata = (RelativeLayout) findViewById(R.id.ll_rank_moreteam_nodata);
		lv_persondatalist = (NoScrollListView) findViewById(R.id.lv_persondatalist);
		sv = (ScrollView) findViewById(R.id.sv);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		pb_distance = (ProgressBar) findViewById(R.id.pb_distance);
		btn_invate = (TextView) findViewById(R.id.btn_invate);
		lv_moreteamdatalist = (ListView) findViewById(R.id.lv_moreteamdatalist);
		lv_chanlleng_message = (NoScrollListView) findViewById(R.id.lv_chanlleng_message);
		message_count = (TextView) findViewById(R.id.message_count);
		ll_moment = (LinearLayout) findViewById(R.id.ll_moment);
		et_moment = (EditText) findViewById(R.id.et_moment);
		bt_commit = (Button) findViewById(R.id.bt_commit);
		cb_message_set = (CheckBox) findViewById(R.id.cb_message_set);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		tv_join_a.setOnClickListener(this);
		tv_join_b.setOnClickListener(this);
		tv_rule.setOnClickListener(this);
		tv_deline.setOnClickListener(this);
		tv_accept.setOnClickListener(this);
		rl_rank_moreteam.setOnClickListener(this);
		lv_persondatalist.setOnItemClickListener(this);
		lv_moreteamdatalist.setOnItemClickListener(this);
		btn_invate.setOnClickListener(this);
		iv_share_challenges.setOnClickListener(this);
		tv_moreteam_join.setOnClickListener(this);
		ll_rank_group_2.setOnClickListener(this);
		ll_rank_group_1.setOnClickListener(this);
		ll_rank_group_3.setOnClickListener(this);
		// bt_commit.setOnClickListener(this);
		sv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (ll_moment != null) {
					ll_moment.setVisibility(View.INVISIBLE);
				}
				return false;
			}
		});
		lv_chanlleng_message.setOnItemClickListener(this);
		cb_message_set.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(is_setNessage){
					if(isChecked){
						set_message = "on";
					}else{
						set_message = "off";
					}
					setMessage();
				}
			}
		});
	}

	/**
	 * 设置消息开关
	 */
	protected void setMessage() {
		final Map<String, String> comentParams = new HashMap<String, String>();
		comentParams.put("target_type", "Challenge");
		comentParams.put("target_id", challenges.getId()+"");
		comentParams.put("notification", set_message);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.NOTIFICATION_SET, handler, NOTIFICATION_SET, comentParams);
			}
		});
	}

	private void initValues() {
		intent = getIntent();
		challengesid = intent.getIntExtra("challengesid", 0);
		getChallengesDetailNet(challengesid);
	}

	/**
	 * 动态更新数据
	 * 
	 * @param data
	 * @param jsonobject_userinfo
	 */
	private void UpdateUi() {
		int type_drawable = 0;
		String title = null;
		User CreatedUser = new User();
		try {
			if (!jsonobject_userinfo.isNull(challenges.getCreated_by() + "")) {
				CreatedUser = UsersConstruct.ToUser(jsonobject_userinfo
						.getJSONObject(challenges.getCreated_by() + ""));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(challenges.getNotification_config() == null){
			cb_message_set.setVisibility(View.GONE);
		}else{
			MessageConfig config = challenges.getNotification_config();
			if("on".equals(config.notification)){
				cb_message_set.setChecked(true);
			}else{
				cb_message_set.setChecked(false);
			}
			//第一次进来时不需要触发checkBook的状态改变方法
			//第一次进来设置完后，以后点击需要触发
			is_setNessage = true;
		}
		
		// 获取挑战类型
		if (challenges.getIs_team().equals("yes")) {
			type = Constants.CHALLENGES_TYPE_TWOTEAM;
			type_drawable = R.drawable.icon_challenges_moreperple;
			title = "两队较量";
			btn_invate.setText("邀请小伙伴");
		} else if (challenges.getIs_group().equals("yes")) {
			type = Constants.CHALLENGES_TYPE_MORETEAM;
			type_drawable = R.drawable.icon_challenges_moreteam;
			title = "多队竞赛";
			btn_invate.setText("邀请团队加入");
			iv_share_challenges.setVisibility(View.VISIBLE);
		} else {
			type = Constants.CHALLENGES_TYPE_PERSON;
			type_drawable = R.drawable.icon_challenges_person;
			title = "个人挑战";
			btn_invate.setText("邀请小伙伴");
		}

		tv_title.setText(challenges.getTitle());
		iv_challenges_type.setImageDrawable(getResources().getDrawable(
				type_drawable));
		ImageUtil.showImage(CreatedUser.getHeadimgurl(), iv_head_me,
				R.drawable.icon_def);
		tv_challenger.setText("发起人：" + CreatedUser.getNickname());
		tv_declaration.setText(challenges.getIntro());
		tv_challenges_title.setText(title);
		tv_challenges_time.setText(challenges.getDays_left());

		String banner = challenges.getBanner();
		if (null != banner) {
			iv_banner.setVisibility(View.VISIBLE);
			ImageUtil.showImage2(banner, iv_banner, R.drawable.iv_loading);
		}

		current_challenge_user = challenges.getCurrent_challenge_user();
		if (current_challenge_user != null) {
			accept_status = current_challenge_user.getStatus();
		} else {
			// 未产生任何关联
			accept_status = "notaccept";
		}
		updateaccept(accept_status);
		updateMoment(challenges.getMoments());
		if (type.equals(Constants.CHALLENGES_TYPE_PERSON)) {
			ll_rank_person.setVisibility(View.VISIBLE);
			UpdateRanking(challenges.getLeaderboard(), jsonobject_userinfo);
		} else if (type.equals(Constants.CHALLENGES_TYPE_TWOTEAM)) {
			rl_twoteam_date.setVisibility(View.VISIBLE);
		} else {
			ll_rank_moreteam.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 更新报名状态
	 * 
	 * @param accept_status2
	 */
	@SuppressLint("NewApi")
	private void updateaccept(String accept_status2) {
		// 分享链接
		Shareurl = UrlConstants.SHARE_CHALLENGES_URL + challenges.getId()
				+ "?token=" + challenges.getToken();
		if (type.equals(Constants.CHALLENGES_TYPE_PERSON)) {
			// 未报名状态
			if (!accept_status2.equals("accept")) {
				ll_accept.setVisibility(View.VISIBLE);
				if (accept_status2.equals("decline")) {
					tv_accept.setText("重新接受");
					tv_deline.setText("已婉拒");
					tv_deline.setBackground(null);
					tv_deline.setEnabled(false);
				}
			} else {
				ll_accept.setVisibility(View.GONE);
			}
		} else if (type.equals(Constants.CHALLENGES_TYPE_TWOTEAM)) {
			// 未报名状态
			if (!accept_status2.equals("accept")) {
				ll_joined.setVisibility(View.GONE);
				ll_join.setVisibility(View.VISIBLE);
			} else {
				ll_joined.setVisibility(View.VISIBLE);
				ll_join.setVisibility(View.GONE);
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
			// tv_ateam_name.setText(ateam.getName()+"("++"人)");
			// tv_bteam_name.setText(bteam.getName());
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

		} else if (type.equals(Constants.CHALLENGES_TYPE_MORETEAM)) {
			Groups_leaderboard = challenges.getGroups_leaderboard();
			int group_count = Groups_leaderboard.size();
			tv_moreteamrank_count.setText("排行榜(" + group_count + ")");
			if (group_count > 0) {
				ll_rank_moreteam_data.setVisibility(View.VISIBLE);
				ll_rank_moreteam_nodata.setVisibility(View.GONE);
				rl_rank_moreteam.setVisibility(View.VISIBLE);
				UpdateGroupChanllengesRank(Groups_leaderboard, group_count);
			} else {
				ll_rank_moreteam_nodata.setVisibility(View.VISIBLE);
				rl_rank_moreteam.setVisibility(View.GONE);
				ll_rank_moreteam_data.setVisibility(View.GONE);

			}
		}
	}

	/**
	 * 更新战况信息
	 */
	private void updateMoment(List<Moment> moments) {
		momentAdapter = new ChallengeMomentAdapter(ll_moment, et_moment,
				bt_commit, this, moments, jsonobject_userinfo,
				Current_userinfo, handler);
		lv_chanlleng_message.setAdapter(momentAdapter);
		if (moments != null) {
			message_count.setText("战况直播(" + moments.size() + ")");
			Untilly.setListViewHeightBasedOnChildren(lv_chanlleng_message, 0);
		}
		lv_chanlleng_message.setSelection(lastVisiblePosition);
	}

	/**
	 * 更新多队挑战排名
	 * 
	 * @param leaderboard
	 */
	void UpdateGroupChanllengesRank(ArrayList<ChallengesUser> leaderboard,
			int group_count) {
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
			my_group_performance = challenges.getMy_group_performance();
			int my_group_count = my_group_performance.size();
			if (my_group_count > 0) {
				myuser_id = new int[my_group_count];
				for (int i = 0; i < my_group_count; i++) {
					myuser_id[i] = my_group_performance.get(i).getGroup_id();
				}
			}
			// 获取前三名之外的我的团队的成绩
			for (int i = 0; i < group_count - 3; i++) {
				for (int j = 0; j < myuser_id.length; j++) {
					if (leaderboard.get(i + 3).getGroup_id() == myuser_id[j]) {
						other_ranks.add(leaderboard.get(i + 3));
						break;
					}
				}
			}
			if (other_ranks.size() > 0) {
				PersonChallengesAdapter other_adapter = new PersonChallengesAdapter(
						other_ranks, ChallengesDetailActivity.this,
						jsonobject_userinfo, myuser_id, true);
				ViewGroup.LayoutParams params = lv_moreteamdatalist
						.getLayoutParams();
				params.height = other_ranks.size()
						* ((int) (110 * OxygenApplication.ppi));
				lv_moreteamdatalist.setLayoutParams(params);
				lv_moreteamdatalist.setAdapter(other_adapter);
			}

		}
	}

	/**
	 * 个人挑战排名
	 * 
	 * @param ranks
	 * @param jsonobject_userinfo
	 */
	private void UpdateRanking(ArrayList<ArrayList<ChallengesUser>> ranks,
			JSONObject jsonobject_userinfo) {
		ArrayList<ChallengesUser> ranks_new = new ArrayList<ChallengesUser>();
		int rank_count = challenges.leardBoard[0];
		tv_personrank_count.setText("排行榜(" + rank_count + ")");

		// 未报名
		if (!accept_status.equals("accept")) {
			ranks_new = ranks.get(0);
		} else {
			// 已报名
			ranks_new = ranks.get(0);

			int myrank = challenges.getMy_performance().getRank();
			if (myrank > 3) {
				ranks_new.add(challenges.getMy_performance());
			}

			myuser_id = new int[1];
			myuser_id[0] = challenges.getMy_performance().getUser_id();

		}

		this.ranks_new = ranks_new;
		

		PersonChallengesAdapter person_adapter = new PersonChallengesAdapter(
				ranks_new, ChallengesDetailActivity.this, jsonobject_userinfo,
				myuser_id, false);
		ViewGroup.LayoutParams params = lv_persondatalist.getLayoutParams();
		params.height = ranks_new.size()
				* ((int) (110 * OxygenApplication.ppi));
		lv_persondatalist.setLayoutParams(params);
		lv_persondatalist.setAdapter(person_adapter);

	}

	/**
	 * 获取服务器events信息
	 */
	private void getChallengesDetailNet(final int challengesid) {
		progressbar.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.CHALLENGES_SHOW_GET + challengesid
						+ ".json", handler, NET_SHOWCHALLENGES);
			}
		});
		// 让scrollView中的一个控件 获取焦点 这样scrollView就能自动显示在最顶端(下面三行)
		iv_challenges_type.setFocusable(true);
		iv_challenges_type.setFocusableInTouchMode(true);
		iv_challenges_type.requestFocus();
	}

	/**
	 * 加入战队
	 * 
	 * @param team_id
	 */
	private void Jointeam(final int team_id) {
		progressbar.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.CHALLENGES_JOIN_TEAM + challengesid
						+ "/" + team_id + ".json", handler, NET_JOIN_TEAM);
			}
		});

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			progressbar.setVisibility(View.GONE);
			switch (msg.what) {
			case NET_SHOWCHALLENGES:
				if (msg.obj != null) {
					try {
						JSONObject jsoninfos = new JSONObject((String) msg.obj);
						if (jsoninfos.getInt("status") == 200) {
							challenges = ChallengesConstruct.tocChallenges(
									jsoninfos.getJSONObject("data"), false);
							jsonobject_userinfo = jsoninfos
									.getJSONObject("users_info");
							Current_userinfo = jsoninfos
									.getJSONObject("current_user");
							challengesid = challenges.getId();
							moments = challenges.getMoments();

							if (!jsoninfos.getJSONObject("data").isNull(
									"current_user_role")) {
								is_group_leader = jsoninfos
										.getJSONObject("data")
										.getJSONObject("current_user_role")
										.getString("is_group_leader");
								is_created_by = jsoninfos.getJSONObject("data")
										.getJSONObject("current_user_role")
										.getString("is_created_by");
							}

							UpdateUi();
						} else {
							ToastUtil.show(ChallengesDetailActivity.this,
									"请求服务器异常");
						}
						progressbar.setVisibility(View.GONE);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					progressbar.setVisibility(View.GONE);
				}

				break;

			case NET_JOIN_TEAM:
				String strObject1 = (String) msg.obj;
				if (strObject1 != null && strObject1.length() > 10) {
					try {
						JSONObject jsoninfos = new JSONObject(strObject1);
						if (jsoninfos.getInt("status") == 200) {
							getChallengesDetailNet(challengesid);
							if (!jsoninfos.isNull("user_action_id")) {

								int userActionId = jsoninfos
										.getInt("user_action_id");
								getToastContent(userActionId);
							}

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					ToastUtil.show(ChallengesDetailActivity.this, "战队加入失败,请重试");
				}
				break;
			case NET_JOIN_TEAM_EXIT:
				String strObject2 = (String) msg.obj;
				if (strObject2 != null && strObject2.length() > 10) {
					try {
						JSONObject jsoninfos = new JSONObject(strObject2);
						if (jsoninfos.getInt("status") == 200) {
							GDUtil.setGlobal(ChallengesDetailActivity.this,"timeline_is_rerfresh", true);
							reback();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					ToastUtil.show(ChallengesDetailActivity.this, "战队加入失败,请重试");
				}
				break;
			case NET_CANCEL:
				String strObject_cancel = (String) msg.obj;
				if (strObject_cancel != null && strObject_cancel.length() > 10) {

					try {
						JSONObject jsoninfos = new JSONObject(strObject_cancel);
						if (jsoninfos.getInt("status") == 200) {
							GDUtil.setGlobal(ChallengesDetailActivity.this,"timeline_is_rerfresh", true);
							reback();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					ToastUtil.show(ChallengesDetailActivity.this, "退出挑战失败,请重试");
				}
				break;
			case NET_DECLINE:
				String strObject_decline = (String) msg.obj;
				if (strObject_decline != null
						&& strObject_decline.length() > 10) {
					try {
						JSONObject jsoninfos = new JSONObject(strObject_decline);
						if (jsoninfos.getInt("status") == 200) {
							GDUtil.setGlobal(ChallengesDetailActivity.this,"timeline_is_rerfresh", true);
							reback();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					ToastUtil.show(ChallengesDetailActivity.this, "取消挑战失败,请重试");
				}
				break;
			case CHALLENGES_GROUP_NET:
				if (msg.obj == null) {
					ToastUtil.show(ChallengesDetailActivity.this,
							"网络连接不可用，请稍后重试");
				} else {
					JSONObject jsonobeObject = (JSONObject) msg.obj;
					try {
						if (jsonobeObject.getInt("status") == 200) {
							getChallengesDetailNet(challengesid);
						} else {
							ToastUtil.show(ChallengesDetailActivity.this,
									"加入挑战失败");
						}
					} catch (JSONException e) {
						ToastUtil.show(ChallengesDetailActivity.this, "加入挑战失败");
						e.printStackTrace();
					}
				}
				break;

			case CHALLENGES_GROUP_DECLINE:
				if (msg.obj == null) {
					ToastUtil.show(ChallengesDetailActivity.this,
							"网络连接不可用，请稍后重试");
				} else {
					JSONObject jsonobeObject = (JSONObject) msg.obj;
					try {
						if (jsonobeObject.getInt("status") == 200) {
							getChallengesDetailNet(challengesid);
						} else {
							ToastUtil.show(ChallengesDetailActivity.this,
									"退出挑战失败");
						}
					} catch (JSONException e) {
						ToastUtil.show(ChallengesDetailActivity.this, "退出挑战失败");
						e.printStackTrace();
					}
				}
				break;
			case CHALLENGES_GROUP_INVITE:
				if (msg.obj == null) {
					ToastUtil.show(ChallengesDetailActivity.this,
							"网络连接不可用，请稍后重试");
				} else {
					JSONObject jsonobeObject = (JSONObject) msg.obj;
					try {
						if (jsonobeObject.getInt("status") == 200) {
							ToastUtil.show(ChallengesDetailActivity.this,
									"发送邀请成功");
						} else {
							ToastUtil.show(ChallengesDetailActivity.this,
									"发送邀请失败");
						}
					} catch (JSONException e) {
						ToastUtil.show(ChallengesDetailActivity.this, "发送邀请失败");
						e.printStackTrace();
					}
				}
				break;
			case POST_COMENT:
				if (msg.obj == null) {
					ToastUtil.show(ChallengesDetailActivity.this,
							"网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonobeObject = (JSONObject) msg.obj;
						if (jsonobeObject.getInt("status") == 200) {
							ToastUtil.show(ChallengesDetailActivity.this,
									"评论成功");
							et_moment.setText("");
							getChallengesDetailNet(challengesid);
						} else {
							ToastUtil.show(ChallengesDetailActivity.this,
									"评论失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				progressbar.setVisibility(View.GONE);
				break;
			case NOTIFICATION_SET:
				if (msg.obj == null) {
					ToastUtil.show(ChallengesDetailActivity.this,
							"网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonobeObject = (JSONObject) msg.obj;
						if (jsonobeObject.getInt("status") == 200) {
							if("on".equals(set_message)){
								ToastUtil.show(getApplicationContext(), "挑战推送消息已开启");
							}else{
								ToastUtil.show(getApplicationContext(), "挑战推送消息已关闭");
							}
						} else {
							ToastUtil.show(ChallengesDetailActivity.this,
									"网络连接不可用，请稍后重试");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;

			case NET_COIN_CNT:
				// 弹出金币提示
				if (msg.obj == null) {
					ToastUtil.show(ChallengesDetailActivity.this,
							"网络连接不可用，请稍后重试");
				} else {

					try {
						JSONObject jsonobeObject = new JSONObject(
								(String) msg.obj);

						if (jsonobeObject.getInt("status") == 200) {
							JSONObject data = jsonobeObject
									.getJSONObject("data");
							String content = data.getString("alert");
							int coins = data.getInt("coins");
							ToastUtil.showCoin(ChallengesDetailActivity.this,
									content + " +" + coins + " 金币!");

						} else {
							ToastUtil.show(ChallengesDetailActivity.this,
									"网络连接不可用，请稍后重试");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				break;

			default:
				break;
			}
		}

	};

	/**
	 * 获取弹金币吐司内容
	 */
	private void getToastContent(final int user_action_id) {

		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.OPERATIONS_ACTION_REWARD
						+ user_action_id + ".json", handler, NET_COIN_CNT);
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			reback();
			break;
		case R.id.tv_accept:
			acceptChallenges(challengesid);
			break;
		case R.id.tv_deline:
			exitChallenges(challengesid);
			break;
		case R.id.iv_share:
			getPopupWindow(v);
			iv_share_appfriend.setVisibility(View.GONE);
			if (popupWindow != null) {
				popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}
			break;
		case R.id.iv_share_weixin:
			share2weixin(0);
			if (null != popupWindow) {
				popupWindow.dismiss();
				return;
			}
			dismissPopWindow();
			break;
		case R.id.iv_share_appfriend:
			Intent intent = new Intent(this, InviteFriendActivity.class);
			intent.putExtra("fromChallenge", true);
			intent.putExtra("challengeId", challengesid);
			startActivity(intent);
			if (null != popupWindow) {
				popupWindow.dismiss();
				return;
			}
			dismissPopWindow();
			break;
		case R.id.iv_share_qq:
			share2qq();
			if (null != popupWindow) {
				popupWindow.dismiss();
				return;
			}
			dismissPopWindow();
			break;
		case R.id.iv_share_weixin_friends:
			share2weixin(1);
			if (null != popupWindow) {
				popupWindow.dismiss();
				return;
			}
			dismissPopWindow();
			break;
		case R.id.tv_join_a:
			Jointeam(challenges.getAteam().getId());
			break;
		case R.id.tv_join_b:
			Jointeam(challenges.getBteam().getId());
			break;
		case R.id.cancel_challenges:
			showDialog(ChallengesDetailActivity.this, "您确认要取消本次挑战么？",
					DIALOG_CANCELCHALLENGES);
			if (null != popupWindow) {
				popupWindow.dismiss();
				return;
			}
			dismissPopWindow();
			break;
		case R.id.exit_challenges:
			if (!type.equals(Constants.CHALLENGES_TYPE_MORETEAM)) {
				showDialog(ChallengesDetailActivity.this, "您确认要退出本次挑战么？",
						DIALOG_EXITCHALLENGES);
				if (null != popupWindow) {
					popupWindow.dismiss();
					return;
				}
			} else {
				// 团队退出挑战
				Intent choosegroup = new Intent(ChallengesDetailActivity.this,
						ChooseGroupToChallengesActivity.class);
				choosegroup.putExtra("type", "del");
				choosegroup.putExtra("groups", groups);
				startActivityForResult(choosegroup, 1);
			}
			dismissPopWindow();
			break;
		case R.id.challenges_QR:
			Intent QRIntent = new Intent(this, TeamQRActivity.class);
			Bundle b = new Bundle();
			b.putString("title", "挑战二维码");
			b.putString("type", Constants.QR_CHALLENGES);
			b.putString("shareurl", Shareurl);
			b.putString("name", challenges.getTitle());
			// b.putString("pic", current_user.getHeadimgurl());
			b.putString("description", "");
			QRIntent.putExtra("data", b);
			startActivity(QRIntent);

			dismissPopWindow();
			break;
		case R.id.challenges_data:
			Intent dataIntent = new Intent(this,ChallengeDataActivity.class);
			dataIntent.putExtra("challengeId", challenges.getId()+"");
			dataIntent.putExtra("challengeToken", challenges.getToken());
			startActivity(dataIntent);
			dismissPopWindow();
			break;
		case R.id.tv_rule:
			if (type != null) {
				Intent intent_rule = new Intent(ChallengesDetailActivity.this,
						RuleStateActivity.class);
				intent_rule.putExtra("type", type);
				startActivity(intent_rule);
			}
			break;
		case R.id.rl_rank_moreteam:
			if (challenges != null) {
				Intent intent_morerank = new Intent(
						ChallengesDetailActivity.this,
						ChallengesRankActivity.class);
				intent_morerank.putExtra("type", type);
				intent_morerank.putExtra("challengesid", challengesid);
				// intent_morerank.putExtra("challenges", challenges);
				// intent_morerank.putExtra("jsonobject_userinfo",
				// jsonobject_userinfo.toString());
				startActivity(intent_morerank);
			}
			break;
		case R.id.tv_moreteam_join:
			Intent choosegroup = new Intent(ChallengesDetailActivity.this,
					ChooseGroupToChallengesActivity.class);
			choosegroup.putExtra("type", "join");
			choosegroup.putExtra("Groups_leaderboard", Groups_leaderboard);
			startActivityForResult(choosegroup, 0);
			break;
		case R.id.iv_share_challenges:
			getPopupWindow(v);
			if (popupWindow != null) {
				popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}
			break;
		case R.id.ll_rank_group_2:
			Intent intent_2 = new Intent(ChallengesDetailActivity.this,
					GroupLeaderboardActivity.class);
			intent_2.putExtra("challenge_id", challengesid);
			if (challenges.groups_leaderboard.size() > 1) {
				intent_2.putExtra("group_id", challenges.groups_leaderboard
						.get(1).getGroup_id());
				startActivity(intent_2);
			}
			break;
		case R.id.ll_rank_group_1:
			Intent intent_1 = new Intent(ChallengesDetailActivity.this,
					GroupLeaderboardActivity.class);
			intent_1.putExtra("challenge_id", challengesid);
			if (challenges.groups_leaderboard.size() > 0) {
				intent_1.putExtra("group_id", challenges.groups_leaderboard
						.get(0).getGroup_id());
				startActivity(intent_1);
			}
			break;
		case R.id.ll_rank_group_3:
			Intent intent_3 = new Intent(ChallengesDetailActivity.this,
					GroupLeaderboardActivity.class);
			intent_3.putExtra("challenge_id", challengesid);
			if (challenges.groups_leaderboard.size() > 2) {
				intent_3.putExtra("group_id", challenges.groups_leaderboard
						.get(2).getGroup_id());
				startActivity(intent_3);
			}
			break;
		case R.id.btn_invate:
			if (Constants.CHALLENGES_TYPE_MORETEAM.equals(type)) {
				Intent i = new Intent(ChallengesDetailActivity.this,
						ChooseSelectTeamtoChanllengesActivity.class);
				startActivityForResult(i, Constants.INVETE_GROUP);
			} else {
				getPopupWindow(v);
				if (popupWindow != null) {
					popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
				}
			}
			break;
		// 评论说两句
		// case R.id.bt_commit:
		// ll_moment.setVisibility(View.INVISIBLE);
		// postComentToNet();
		// break;
		default:
			break;
		}

	}

	protected void dismissPopWindow() {
		// TODO Auto-generated method stub
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		switch (arg0.getId()) {
		case R.id.lv_persondatalist:
			// String userid = person_rank.get(arg2).getUser_id() + "";
			// double maxdistanceorfrist = person_rank.get(0).getDistance();
			String userid = this.ranks_new.get(arg2).getUser_id() + "";
			double maxdistanceorfrist = this.ranks_new.get(0).getDistance();
			Intent intent = new Intent(ChallengesDetailActivity.this,
					PersonPerformance.class);
			try {
				intent.putExtra("user", UsersConstruct
						.ToUser(jsonobject_userinfo.getJSONObject(userid)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			intent.putExtra("maxdistanceorfrist", maxdistanceorfrist);
			intent.putExtra("challengesid", challengesid);
			startActivity(intent);
			break;
		case R.id.lv_moreteamdatalist:
			Intent intent_m = new Intent(ChallengesDetailActivity.this,
					GroupLeaderboardActivity.class);
			intent_m.putExtra("challenge_id", challengesid);
			intent_m.putExtra("group_id", other_ranks.get(arg2).getGroup_id());
			startActivity(intent_m);
			break;
		case R.id.lv_chanlleng_message:
			// 小乐助手不做跳转
			if (moments.get(arg2).getCreated_by() == 1) {
				return;
			}
			Intent intentFormance;
			// 多队挑战跳转到团队排行界面
			// if (challenges.getIs_group().equals("yes")) {
			// intentFormance = new Intent(this,
			// GroupLeaderboardActivity.class);
			// intentFormance.putExtra("group_id",
			// Integer.parseInt(moments.get(arg2).getGroup_id()));
			// intentFormance.putExtra("challenge_id", challengesid);
			// // 个人挑战和两队跳转个人运动成绩页面
			// } else {
			intentFormance = new Intent(this, PersonPerformance.class);
			try {
				intentFormance.putExtra("user", UsersConstruct
						.ToUser(jsonobject_userinfo.getJSONObject(moments.get(
								arg2).getCreated_by()
								+ "")));
				intentFormance.putExtra("challengesid", challengesid);
			} catch (JSONException e) {
				e.printStackTrace();
				// }
			}
			startActivity(intentFormance);
			break;
		default:
			break;
		}

	}

	private void share2weixin(int flag) {
		WxUtil.share2weixin(flag, ChallengesDetailActivity.this, Shareurl,
				"来挑战吧-" + challenges.getTitle(), "icon_vs_running_s",
				challenges.getIntro());
	}

	private void share2qq() {
		String imageUrl = Constants.qiuniushare + "icon_vs_running_s.jpg";
		// Bundle params = new Bundle();
		// params.putString(QQShare.SHARE_TO_QQ_TITLE, "来挑战吧-" +
		// challenges.getTitle());
		// params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
		// params.putString(QQShare.SHARE_TO_QQ_SUMMARY, challenges.getIntro());
		// params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Shareurl);
		QQUtils.doShareToQQ(this, "来挑战吧-" + challenges.getTitle(), imageUrl,
				challenges.getIntro(), Shareurl);
	}

	private LinearLayout ll_moment;
	private EditText et_moment;
	private Button bt_commit;
	private ChallengeMomentAdapter momentAdapter;
	private int lastVisiblePosition;
	private CheckBox cb_message_set;

	/***
	 * 获取PopupWindow实例
	 */
	private void getPopupWindow(View v) {
		if (challenges != null) {
			if (null != popupWindow) {
				popupWindow.dismiss();
				return;
			} else {
				initPopuptWindow(v);
			}
		}
	}

	/**
	 * 创建PopupWindow
	 */
	protected void initPopuptWindow(View v) {
		// TODO Auto-generated method stub
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(
				R.layout.pop_challengesdetail, null, false);
		initPopViews(popupWindow_view);
		if (v == iv_share) {
			if (type.equals(Constants.CHALLENGES_TYPE_TWOTEAM)) {
				// 发起者
				if (current_challenge_user != null
						&& challenges.getCreated_by() == current_challenge_user
								.getUser_id()) {
					// 未加入
					if (!accept_status.equals("accept")) {
						exit_challenges.setVisibility(View.GONE);
					}
				}
				// 参与者
				else {
					// 已加入
					if (accept_status.equals("accept")) {
						cancel_challenges.setVisibility(View.GONE);
					}
					// 未加入
					else {
						cancel_challenges.setVisibility(View.GONE);
						exit_challenges.setVisibility(View.GONE);
					}
					challenges_data.setVisibility(View.GONE);
				}

			} else if (type.equals(Constants.CHALLENGES_TYPE_PERSON)) {
				// 发起者
				if (current_challenge_user != null
						&& challenges.getCreated_by() == current_challenge_user
								.getUser_id()) {
					exit_challenges.setVisibility(View.GONE);
				}
				// 参与者
				else {
					// 已加入
					if (accept_status.equals("accept")) {
						cancel_challenges.setVisibility(View.GONE);
					}
					// 未加入
					else {
						cancel_challenges.setVisibility(View.GONE);
						exit_challenges.setVisibility(View.GONE);
					}
					challenges_data.setVisibility(View.GONE);
				}

			} else {
				// 没有团队加入的时候退出挑战都隐藏
				if (challenges.getMy_group_performance() == null) {
					exit_challenges.setVisibility(View.GONE);
				} else {
					// 发起者
					if (is_created_by.equals("yes")) {
						exit_challenges.setText("移除团队");

						if (challenges.getGroups_leaderboard() != null
								&& challenges.getGroups_leaderboard().size() > 0) {
							groups = new ArrayList<Group>();
							ChallengesUser challengesUser;
							Group group;
							for (int i = 0; i < challenges
									.getGroups_leaderboard().size(); i++) {
								challengesUser = challenges
										.getGroups_leaderboard().get(i);
								group = new Group();
								group.id = challengesUser.getGroup_id();
								group.name = challengesUser.getGroup_name();
								group.pic = challengesUser.getPic();
								groups.add(group);
							}
						}

					}
					// 参与者
					else {
						if (challenges.getMy_group_performance() != null
								&& challenges.getMy_group_performance().size() > 0
								& is_group_leader.equals("yes")) {
							groups = new ArrayList<Group>();
							ChallengesUser challengesUser;
							Group group;
							for (int i = 0; i < challenges
									.getMy_group_performance().size(); i++) {
								challengesUser = challenges
										.getMy_group_performance().get(i);
								group = new Group();
								group.id = challengesUser.getGroup_id();
								group.name = challengesUser.getGroup_name();
								group.pic = challengesUser.getPic();
								if (challengesUser.getIs_group_leader().equals(
										"yes")) {
									groups.add(group);
								}
							}
						} else {
							exit_challenges.setVisibility(View.GONE);
						}
						cancel_challenges.setVisibility(View.GONE);
						challenges_data.setVisibility(View.GONE);
					}

				}

			}
		} else {
			cancel_challenges.setVisibility(View.GONE);
			exit_challenges.setVisibility(View.GONE);

		}
		initPopViewEvENT();

		// 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		// 点击其他地方消失
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
	}

	private void initPopViews(View popupWindow_view) {
		iv_share_appfriend = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_appfriend);
		iv_share_weixin = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_weixin);
		iv_share_qq = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_qq);
		iv_share_weixin_friends = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_weixin_friends);
		cancel_challenges = (TextView) popupWindow_view
				.findViewById(R.id.cancel_challenges);
		exit_challenges = (TextView) popupWindow_view
				.findViewById(R.id.exit_challenges);
		challenges_QR = (TextView) popupWindow_view
				.findViewById(R.id.challenges_QR);
		challenges_data = (TextView) popupWindow_view
				.findViewById(R.id.challenges_data);

	}

	private void initPopViewEvENT() {
		iv_share_appfriend.setOnClickListener(this);
		iv_share_weixin.setOnClickListener(this);
		iv_share_qq.setOnClickListener(this);
		iv_share_weixin_friends.setOnClickListener(this);
		cancel_challenges.setOnClickListener(this);
		exit_challenges.setOnClickListener(this);
		challenges_QR.setOnClickListener(this);
		challenges_data.setOnClickListener(this);
	}

	/**
	 * 
	 * 退出挑战(拒绝挑战)
	 * 
	 * @param challengesid
	 */
	private void exitChallenges(final int challengesid) {
		progressbar.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.CHALLENGES_DECLINE + challengesid
						+ ".json", handler, NET_JOIN_TEAM_EXIT);
			}
		});

	}

	/**
	 * 
	 * 接受挑战
	 * 
	 * @param challengesid
	 */
	private void acceptChallenges(final int challengesid) {
		progressbar.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.CHALLENGES_ACCEPT + challengesid
						+ ".json", handler, NET_JOIN_TEAM);
			}
		});

	}

	/**
	 * 取消挑战（活动组织者调用）
	 * 
	 * @param challengesid
	 */
	private void cancelChallenges(final int challengesid) {
		progressbar.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.CHALLENGES_CANCEL + challengesid
						+ ".json", handler, NET_CANCEL);
			}
		});
	}

	/**
	 * 确认退出框
	 * 
	 * @param content
	 */
	private void showDialog(Context content, String title, final int dtype) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(content);
		dialog.setMessage(title)
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();// 取消弹出框
						if (dtype == DIALOG_CANCELCHALLENGES) {
							cancelChallenges(challengesid);
						} else if (dtype == DIALOG_EXITCHALLENGES) {
							exitChallenges(challengesid);
						}
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();// 取消弹出框
					}
				}).create().show();
	}

	private void reback() {
		if (GDUtil.getGlobal(ChallengesDetailActivity.this,"timeline_is_rerfresh")) {
			Intent intent = new Intent(ChallengesDetailActivity.this,
					MenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			GDUtil.setGlobal(ChallengesDetailActivity.this, "timeline_is_rerfresh",false);
		}
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			reback();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 加入团队挑战
	 * 
	 * @param group_id
	 */
	private void group_accept(int group_id) {
		progressbar.setVisibility(View.VISIBLE);
		final Map<String, String> params = new HashMap<String, String>();
		// POST 参数
		params.put("group_id", group_id + "");
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.CHALLENGES_GROUP_ACCEPT_POST
						+ challengesid + ".json", handler,
						CHALLENGES_GROUP_NET, params);
			}
		});

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == 0 && arg2 != null) {
			int group_id = arg2.getIntExtra("group_id", 0);
			if (group_id != 0) {
				group_accept(group_id);
			}
		}
		if (arg1 == Constants.INVETE_GROUP) {
			int group_id = arg2.getIntExtra("group_id", 0);
			if (group_id != 0) {

				progressbar.setVisibility(View.VISIBLE);
				final Map<String, String> params = new HashMap<String, String>();
				// POST 参数
				params.put("group_id", group_id + "");
				OxygenApplication.cachedThreadPool.execute(new Runnable() {
					public void run() {
						HttpUtil.Post(UrlConstants.CHALLENGES_GROUP_INVITE
								+ challengesid + ".json", handler,
								CHALLENGES_GROUP_INVITE, params);
					}
				});

			}

		} else if (arg1 == 1) {
			int group_id = arg2.getIntExtra("group_id", 0);
			if (group_id != 0) {

				progressbar.setVisibility(View.VISIBLE);
				final Map<String, String> params = new HashMap<String, String>();
				// POST 参数
				params.put("group_id", group_id + "");
				OxygenApplication.cachedThreadPool.execute(new Runnable() {
					public void run() {
						HttpUtil.Post(UrlConstants.CHALLENGES_GROUP_DECLINE
								+ challengesid + ".json", handler,
								CHALLENGES_GROUP_DECLINE, params);
					}
				});

			}

		}

	}

	/**
	 * 对coment进行评论
	 */
	public void postComentToNet() {
		String content = et_moment.getText().toString().trim();
		if(TextUtils.isEmpty(content)){
			ToastUtil.show(getApplicationContext(), "品论内容不能为空");
			return;
		}
		lastVisiblePosition = lv_chanlleng_message.getFirstVisiblePosition();
		final Map<String, String> comentParams = new HashMap<String, String>();
		comentParams.put("target_id", (String)UserInfoUtils.getUserInfo(getApplicationContext(), Constants.SET_COMENT_ID, ""));
		comentParams.put("content", content);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.MOMENT_CREATE_GET, handler,
						POST_COMENT, comentParams);
			}
		});

	}

	/**
	 * 更新评论信息
	 */
	private void UpdateMomentsUi() {
		if (moments != null && moments.size() > 0) {
			momentAdapter = new ChallengeMomentAdapter(ll_moment, et_moment,
					bt_commit, this, moments, jsonobject_userinfo,
					Current_userinfo, handler);
			ViewGroup.LayoutParams params = lv_chanlleng_message
					.getLayoutParams();
			params.height = LayoutParams.WRAP_CONTENT;
			lv_chanlleng_message.setAdapter(momentAdapter);
			Untilly.setListViewHeightBasedOnChildren(lv_chanlleng_message, 100);
		}
	}

	private void onEventMainThread(ListViewAddHeight msg) {
		// momentAdapter.notifyDataSetChanged();
		// Untilly.setListViewHeightBasedOnChildren(lv_chanlleng_message,
		// msg.getHeight());
		getChallengesDetailNet(challengesid);
	}

}
