package com.oxygen.www.module.team.construt;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.enties.ChallengesUser;

public class MembersConstruct {
	public static ArrayList<ChallengesUser> Tomemberlist(JSONArray jsonarray_data) {
		ArrayList<ChallengesUser> groups = new ArrayList<ChallengesUser>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			groups.add(ToMember(c));
		}

		return groups;
	}

	public static ChallengesUser ToMember(JSONObject c) {
		ChallengesUser member = new ChallengesUser();
		try {
			if (!c.isNull("id")) {
				member.id = c.getInt("id");
			}

			if (!c.isNull("group_id")) {
				member.group_id = c.getInt("group_id");
			}
			if (!c.isNull("user_id")) {
				member.user_id = c.getInt("user_id");
			}
			if (!c.isNull("created_by")) {
				member.created_by = c.getInt("created_by");
			}
			if (!c.isNull("created_at")) {
				member.created_at = c.getString("created_at");
			}
			if (!c.isNull("modified_at")) {
				member.modified_at = c.getString("modified_at");
			}
			if (!c.isNull("modified_by")) {
				member.modified_by = c.getInt("modified_by");
			}
			if (!c.isNull("challenge_id")) {
				member.challenge_id = c.getInt("challenge_id");
			}

			if (!c.isNull("role")) {
				member.role = c.getString("role");
			}
			if (!c.isNull("status")) {
				member.status = c.getString("status");
			}
			if (!c.isNull("pic")) {
				member.pic = c.getString("pic");
			}
			if (!c.isNull("is_my_group")) {
				member.is_my_group = c.getString("is_my_group");
			}
			if (!c.isNull("rank")) {
				member.rank = c.getInt("rank");
			}
			if (!c.isNull("group_name")) {
				member.group_name = c.getString("group_name");
			}
			if (!c.isNull("is_group_leader")) {
				member.is_group_leader = c.getString("is_group_leader");
			}
			if (!c.isNull("distance")) {
				member.distance = c.getDouble("distance");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return member;

	}

}
