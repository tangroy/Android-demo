package com.oxygen.www.module.sport.construct;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.enties.Vote;

public class VotesConstruct {
	public static ArrayList<Vote> ToVotelist(JSONArray jsonarray_data) {
		ArrayList<Vote> votes = new ArrayList<Vote>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			votes.add(ToVote(c));
		}

		return votes;
	}

	public static Vote ToVote(JSONObject c) {
		Vote vote = new Vote();
		try {
			if (!c.isNull("id")) {
				vote.id = c.getInt("id");
			}
			if (!c.isNull("vote")) {
				vote.vote = c.getInt("vote");
			}
			if (!c.isNull("target_type")) {
				vote.target_type = c.getString("target_type");
			}if (!c.isNull("target_id")) {
				vote.target_id = c.getInt("target_id");
			}
			if (!c.isNull("created_by")) {
				vote.created_by = c.getInt("created_by");
			}
			if (!c.isNull("created_at")) {
				vote.created_at = c.getString("created_at");
			}
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return vote;

	}

}
