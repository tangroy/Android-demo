package com.oxygen.www.module.sport.construct;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.enties.Moment;

public class MomentConstruct {
	public static ArrayList<Moment> ToMomentlist(JSONArray jsonarray_data) {
		ArrayList<Moment> Moments = new ArrayList<Moment>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			Moments.add(ToMoment(c));
		}

		return Moments;
	}

	public static Moment ToMoment(JSONObject c) {
		Moment moment = new Moment();
		try {
			if (!c.isNull("id")) {
				moment.id = c.getInt("id");
			}
			if (!c.isNull("activity_id")) {
				moment.activity_id = c.getInt("activity_id");
			}
			if (!c.isNull("created_at")) {
				moment.created_at = c.getString("created_at");
			}
			if (!c.isNull("created_by")) {
				moment.created_by = c.getInt("created_by");
			}if (!c.isNull("event_id")) {
				moment.event_id = c.getInt("event_id");
			}
			if (!c.isNull("modified_at")) {
				moment.modified_at = c.getString("modified_at");
			}
			if (!c.isNull("modified_by")) {
				moment.modified_by = c.getInt("modified_by");
			}
			if (!c.isNull("photos")) {
				moment.photos = PhotosConstruct.ToPhotolist(c.getJSONArray("photos"));
			}
			if (!c.isNull("comments")) {
				moment.comments = CommentsConstruct.ToCommentlist(c.getJSONArray("comments"));
			}
			if (!c.isNull("votes")) {
				moment.votes = VotesConstruct.ToVotelist(c.getJSONArray("votes"));
			}
			if (!c.isNull("series_id")) {
				moment.series_id = c.getString("series_id");
			}
			if (!c.isNull("words")) {
				moment.words = c.getString("words");
			}
			if (!c.isNull("challenge_id")) {
				moment.challenge_id = c.getString("challenge_id");
			}
			if (!c.isNull("group_id")) {
				moment.group_id = c.getString("group_id");
			}
			if (!c.isNull("team_id")) {
				moment.team_id = c.getString("team_id");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return moment;

	}

}
