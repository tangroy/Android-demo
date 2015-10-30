package com.oxygen.www.module.team.construt;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.enties.Group;
import com.oxygen.www.module.sport.construct.EventConstruct;
import com.oxygen.www.module.sport.construct.FeedConstruct;

public class GroupsConstruct {
	public static ArrayList<Group> ToGrouplist(JSONArray jsonarray_data) {
		ArrayList<Group> groups = new ArrayList<Group>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			groups.add(ToGroup(c));
		}
		return groups;
	}

	public static Group ToGroup(JSONObject c) {
		Group group = new Group();
		try {
			if (!c.isNull("id")) {
				group.id = c.getInt("id");
			}
			if (!c.isNull("event_count")) {
				group.event_count = c.getInt("event_count");
			}
			if (!c.isNull("member_count")) {
				group.member_count = c.getInt("member_count");
			}
			if (!c.isNull("sport")) {
				group.sport = c.getString("sport");
			}
			if (!c.isNull("name")) {
				group.name = c.getString("name");
			}
			if (!c.isNull("intro")) {
				group.intro = c.getString("intro");
			}
			if (!c.isNull("created_by")) {
				group.created_by = c.getInt("created_by");
			}
			if (!c.isNull("created_at")) {
				group.created_at = c.getString("created_at");
			}
			if (!c.isNull("modified_at")) {
				group.modified_at = c.getString("modified_at");
			}
			if (!c.isNull("modified_by")) {
				group.modified_by = c.getInt("modified_by");
			}
			if (!c.isNull("location_id")) {
				group.location_id = c.getInt("location_id");
			}
			if (!c.isNull("pic")) {
				group.pic = c.getString("pic");
			}

			if (!c.isNull("started_at")) {
				group.started_at = c.getString("started_at");
			}

			if (!c.isNull("token")) {
				group.token = c.getString("token");
			}

			if (!c.isNull("role")) {
				group.role = c.getString("role");
			}
			if(!c.isNull("friend_count")){
				group.friend_count = c.getInt("friend_count");
			}
			
			if(!c.isNull("events")){
				group.events = EventConstruct.Toeventlist(c.getJSONArray("events"));
			}
			if(!c.isNull("members")){
				group.members = MembersConstruct.Tomemberlist(c.getJSONArray("members"));
			}
			if(!c.isNull("feeds")){
				group.feeds = FeedConstruct.ToFeedlist(c.getJSONArray("feeds"));
			}
			if(!c.isNull("current_group_user")){
				group.current_group_user = MembersConstruct.ToMember(c.getJSONObject("current_group_user"));
			}
			if (!c.isNull("location")) {
				JSONObject jsObject_location = c.getJSONObject("location");
				if (!jsObject_location.isNull("address")) {
					group.address = jsObject_location
							.getString("address");
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return group;

	}

}
