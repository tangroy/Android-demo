package com.oxygen.www.module.sport.construct;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.oxygen.www.enties.EventFeed;

public class FeedConstruct {
	public static ArrayList<EventFeed> ToFeedlist(JSONArray jsonarray_data) {
		ArrayList<EventFeed> feeds = new ArrayList<EventFeed>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			feeds.add(ToEventFeed(c));
		}

		return feeds;
	}

	public static EventFeed ToEventFeed(JSONObject c) {
		EventFeed feed = new EventFeed();
		try {
			if (!c.isNull("action")) {
				feed.action = c.getString("action");
			}

			if (!c.isNull("comment_count")) {
				feed.comment_count = c.getString("comment_count");
			}
			if (!c.isNull("content")) {
				feed.content = c.getString("content");
			}
			if (!c.isNull("created_by")) {
				feed.created_by = c.getString("created_by");
			}
			if (!c.isNull("created_at")) {
				feed.created_at = c.getString("created_at");
			}
			if (!c.isNull("curr_user_voted")) {
				feed.curr_user_voted = c.getString("curr_user_voted");
			}
			if (!c.isNull("group_id")) {
				feed.group_id = c.getString("group_id");
			}
			if (!c.isNull("id")) {
				feed.id = c.getString("id");
			}
			if (!c.isNull("target_id")) {
				feed.target_id = c.getString("target_id");
			}
			if (!c.isNull("target_type")) {
				feed.target_type = c.getString("target_type");
			}
			if (!c.isNull("title")) {
				feed.title = c.getString("title");
			}
			if (!c.isNull("user_id")) {
				feed.user_id = c.getString("user_id");
			}
			if (!c.isNull("vote_count")) {
				feed.vote_count = c.getString("vote_count");
			}
			if (!c.isNull("comments")) {
				feed.comments = CommentsConstruct.ToCommentlist(c
						.getJSONArray("comments"));
			}
			if (!c.isNull("votes")) {
				feed.votes = VotesConstruct.ToVotelist(c.getJSONArray("votes"));
			}
			if (!c.isNull("feed_data")) {
				JSONObject jsObject_location = c.getJSONObject("feed_data");
				if (!jsObject_location.isNull("address")) {
					feed.address = jsObject_location.getString("address");
				}
				if (!jsObject_location.isNull("type")) {
					feed.type = jsObject_location.getString("type");
				}
				if (!jsObject_location.isNull("pic")) {
					feed.pic = jsObject_location.getString("pic");
				}
				if (!jsObject_location.isNull("intro")) {
					feed.intro = jsObject_location.getString("intro");
				}
				if (!jsObject_location.isNull("name")) {
					feed.name = jsObject_location.getString("name");
				}
				if (!jsObject_location.isNull("title")) {
					feed.data_title = jsObject_location.getString("title");
				}
				if (!jsObject_location.isNull("days_left")) {
					feed.days_left = jsObject_location.getString("days_left");
				}
				if (!jsObject_location.isNull("sport")) {
					feed.sport = jsObject_location.getString("sport");
				}
				if (!jsObject_location.isNull("is_group")) {
					feed.is_group = jsObject_location.getString("is_group");
				}
				if (!jsObject_location.isNull("is_team")) {
					feed.is_team = jsObject_location.getString("is_team");
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return feed;
	}
}
