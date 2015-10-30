package com.oxygen.www.module.challengs.construct;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.enties.Challenges;
import com.oxygen.www.enties.ChallengesUser;
import com.oxygen.www.enties.MessageConfig;
import com.oxygen.www.enties.Team;
import com.oxygen.www.module.sport.construct.ActivitiesConstruct;
import com.oxygen.www.module.sport.construct.MomentConstruct;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.module.team.construt.GroupsConstruct;
import com.oxygen.www.module.team.construt.MembersConstruct;

public class ChallengesConstruct {
	public static Challenges tocChallenges(JSONObject c, boolean isAll) {
		Challenges cl = new Challenges();
		if (!c.isNull("id")) {
			try {
				cl.id = c.getInt("id");
				if (!c.isNull("calorie"))
					cl.calorie = c.getString("calorie");
				if (!c.isNull("created_at")) {
					cl.created_at = c.getString("created_at");
				}
				if (!c.isNull("days")) {
					cl.days = c.getInt("days");
				}
				if (!c.isNull("distance"))
					cl.distance = c.getDouble("distance");
				if (!c.isNull("intro"))
					cl.intro = c.getString("intro");
				if (!c.isNull("is_team"))
					cl.is_team = c.getString("is_team");
				if (!c.isNull("is_group"))
					cl.is_group = c.getString("is_group");
				if (!c.isNull("modified_at"))
					cl.modified_at = c.getString("modified_at");
				if (!c.isNull("modified_by")) {
					cl.modified_by = c.getInt("modified_by");
				}
				if (!c.isNull("start_time"))
					cl.start_time = c.getString("start_time");
				if (!c.isNull("end_time"))
					cl.end_time = c.getString("end_time");

				if (!c.isNull("created_by"))
					cl.created_by = c.getInt("created_by");
				if (!c.isNull("created_at"))
					cl.created_at = c.getString("created_at");
				if (!c.isNull("title"))
					cl.title = c.getString("title");
				if (!c.isNull("status"))
					cl.status = c.getString("status");

				if (!c.isNull("token")) {
					cl.token = c.getString("token");
				}

				if (!c.isNull("pace")) {
					cl.pace = c.getString("pace");
				}
				if (!c.isNull("ranking")) {
					cl.ranking = c.getString("ranking");
				}
				if (!c.isNull("speed")) {
					cl.speed = c.getString("speed");
				}
				if (!c.isNull("type")) {
					cl.type = c.getString("type");
				}
				if (!c.isNull("banner")) {
					cl.banner = c.getString("banner");
				}
				if (!c.isNull("teams")) {
					cl.teams = toArrayteam(c.getJSONArray("teams"));
				}
				if (!c.isNull("groups_leaderboard")) {
					cl.groups_leaderboard = MembersConstruct.Tomemberlist(c
							.getJSONArray("groups_leaderboard"));
				}
				if (!c.isNull("my_group_performance")) {
					cl.my_group_performance = MembersConstruct.Tomemberlist(c
							.getJSONArray("my_group_performance"));
				}
				if (!c.isNull("days_left")) {
					cl.days_left = c.getString("days_left");
				}
				if (!c.isNull("my_performance")) {
					cl.my_performance = ChallengesConstruct.tocChallengesUser(c
							.getJSONObject("my_performance"));
				}
				if (!c.isNull("notification_config")) {
					cl.notification_config = ChallengesConstruct.tocMessageConfig(c
							.getJSONObject("notification_config"));
				}
				if (!c.isNull("moments")) {
					cl.moments = MomentConstruct.ToMomentlist(c
							.getJSONArray("moments"));
				}
				if (!c.isNull("leaderboard")) {

					if (isAll) {
						cl.leaderboard = toRankings(c
								.getJSONArray("leaderboard"));
					} else {
						// 只取最多前三名
						cl.leardBoard = new Integer[2];
						cl.leaderboard = toRankings2(
								c.getJSONArray("leaderboard"), cl.leardBoard);
					}

				}
				if (!c.isNull("current_challenge_user")) {
					cl.current_challenge_user = tocChallengesUser(c
							.getJSONObject("current_challenge_user"));
				}
				if (!c.isNull("teams")) {
					JSONArray team = c.getJSONArray("teams");
					JSONObject a_c = team.optJSONObject(0);
					if (a_c != null) {
						cl.ateam = toTeam(a_c);
					}
					JSONObject b_c = team.optJSONObject(1);
					if (b_c != null) {
						cl.bteam = toTeam(b_c);
					}
				}

				if (!c.isNull("team_distances")) {
					JSONArray team = c.getJSONArray("team_distances");
					if (team.length() == 2) {
						JSONObject a_c = team.optJSONObject(0);
						if (a_c != null
								&& cl.ateam.getId() == toTeam(a_c).getTeam_id()) {
							cl.ateam.distance = toTeam(a_c).getDistance();
						} else if (a_c != null
								&& cl.bteam.getId() == toTeam(a_c).getTeam_id()) {
							cl.bteam.distance = toTeam(a_c).getDistance();
						}
						JSONObject b_c = team.optJSONObject(1);
						if (b_c != null
								&& cl.ateam.getId() == toTeam(b_c).getTeam_id()) {
							cl.ateam.distance = toTeam(b_c).getDistance();
						} else if (b_c != null
								&& cl.bteam.getId() == toTeam(b_c).getTeam_id()) {
							cl.bteam.distance = toTeam(b_c).getDistance();
						}
					} else if (team.length() == 1) {
						JSONObject aa = team.optJSONObject(0);
						// 仅a队有数据
						if (cl.ateam.getId() == toTeam(aa).getTeam_id()) {
							cl.ateam.distance = toTeam(aa).getDistance();
						} else {
							cl.bteam.distance = toTeam(aa).getDistance();
						}
					}

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cl;
	}

	public static ArrayList<Team> toArrayteam(JSONArray jsonarray_teams) {
		ArrayList<Team> teams = new ArrayList<Team>();
		for (int i = 0; i < jsonarray_teams.length(); i++) {

			JSONObject c = (JSONObject) jsonarray_teams.opt(i);

			teams.add(toTeam(c));
		}
		return teams;
	}

	public static Team toTeam(JSONObject c) {
		Team team = new Team();

		try {
			if (!c.isNull("created_at")) {
				team.created_at = c.getString("created_at");
			}
			if (!c.isNull("intro")) {
				team.intro = c.getString("intro");
			}
			if (!c.isNull("modified_at")) {
				team.modified_at = c.getString("modified_at");
			}
			if (!c.isNull("name")) {
				team.name = c.getString("name");
			}
			if (!c.isNull("pic")) {
				team.pic = c.getString("pic");
			}
			if (!c.isNull("id")) {
				team.id = c.getInt("id");
			}
			if (!c.isNull("challenge_id")) {
				team.challenge_id = c.getInt("challenge_id");
			}
			if (!c.isNull("created_by")) {
				team.created_by = c.getInt("created_by");
			}
			if (!c.isNull("modified_by")) {
				team.modified_by = c.getInt("modified_by");
			}
			if (!c.isNull("rank")) {
				team.rank = c.getInt("rank");
			}
			if (!c.isNull("distance")) {
				team.distance = c.getDouble("distance");
			}
			if (!c.isNull("team_id")) {
				team.team_id = c.getInt("team_id");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return team;
	}

	public static ArrayList<ArrayList<ChallengesUser>> toRankings(
			JSONArray array) {
		ArrayList<ArrayList<ChallengesUser>> ranks = null;
		if (array != null && array.length() > 0) {
			ranks = new ArrayList<ArrayList<ChallengesUser>>();
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONArray arrays = array.getJSONArray(i);
					ArrayList<ChallengesUser> rank = new ArrayList<ChallengesUser>();
					for (int j = 0; j < arrays.length(); j++) {
						ChallengesUser challenges = tocChallengesUser(arrays
								.getJSONObject(j));

						rank.add(challenges);
					}
					ranks.add(rank);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return ranks;
	}

	/**
	 * 只构造必要部分
	 * 
	 * @param array
	 * @return
	 */
	public static ArrayList<ArrayList<ChallengesUser>> toRankings2(
			JSONArray array, Integer[] leardBoard) {
		ArrayList<ArrayList<ChallengesUser>> ranks = null;
		if (array != null && array.length() > 0) {
			ranks = new ArrayList<ArrayList<ChallengesUser>>();
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONArray arrays = array.getJSONArray(i);
					leardBoard[i] = arrays.length();
					ArrayList<ChallengesUser> rank = new ArrayList<ChallengesUser>();
					// 只取必要部分
					int length = arrays.length() > 3 ? 3 : arrays.length();
					for (int j = 0; j < length; j++) {
						ChallengesUser challenges = tocChallengesUser(arrays
								.getJSONObject(j));

						rank.add(challenges);
					}
					ranks.add(rank);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return ranks;
	}

	public static ChallengesUser tocChallengesUser(JSONObject c) {
		ChallengesUser clu = new ChallengesUser();

		try {
			if (!c.isNull("id")) {
				clu.id = c.getInt("id");
			}
			if (!c.isNull("calorie"))
				clu.calorie = c.getString("calorie");
			if (!c.isNull("created_at")) {
				clu.created_at = c.getString("created_at");
			}
			if (!c.isNull("challenge_id")) {
				clu.challenge_id = c.getInt("challenge_id");
			}
			if (!c.isNull("distance"))
				clu.distance = c.getDouble("distance");
			if (!c.isNull("finished_at"))
				clu.finished_at = c.getString("finished_at");
			if (!c.isNull("is_finished"))
				clu.is_finished = c.getString("is_finished");
			if (!c.isNull("modified_at"))
				clu.modified_at = c.getString("modified_at");
			if (!c.isNull("modified_by")) {
				clu.modified_by = c.getInt("modified_by");
			}
			if (!c.isNull("team_name"))
				clu.team_name = c.getString("team_name");

			if (!c.isNull("created_by"))
				clu.created_by = c.getInt("created_by");
			if (!c.isNull("created_at"))
				clu.created_at = c.getString("created_at");
			if (!c.isNull("team_id"))
				clu.team_id = c.getInt("team_id");
			if (!c.isNull("status"))
				clu.status = c.getString("status");

			if (!c.isNull("user_id")) {
				clu.user_id = c.getInt("user_id");
			}

			if (!c.isNull("pace")) {
				clu.pace = c.getString("pace");
			}
			if (!c.isNull("speed")) {
				clu.speed = c.getString("speed");
			}
			if (!c.isNull("rank")) {
				clu.rank = c.getInt("rank");
			}
			if (!c.isNull("group_id")) {
				clu.group_id = c.getInt("group_id");
			}
			if (!c.isNull("role")) {
				clu.role = c.getString("role");
			}
			if (!c.isNull("group_name")) {
				clu.group_name = c.getString("group_name");
			}
			if (!c.isNull("is_my_group")) {
				clu.is_my_group = c.getString("is_my_group");
			}
			if (!c.isNull("is_group_leader")) {
				clu.is_group_leader = c.getString("is_group_leader");
			}
			if (!c.isNull("pic")) {
				clu.pic = c.getString("pic");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return clu;
	}
	public static MessageConfig tocMessageConfig(JSONObject c) {
		MessageConfig messageConfig = new MessageConfig();
		try {
			if (!c.isNull("notification")) {
				messageConfig.notification = c.getString("notification");
			}
			if (!c.isNull("push"))
				messageConfig.push = c.getString("push");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return messageConfig;
	}
}
