package com.oxygen.www.module.sport.construct;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.oxygen.www.enties.CurrentEventUser;
import com.oxygen.www.enties.Photo;
import com.oxygen.www.enties.RankUser;
import com.oxygen.www.enties.Route;
import com.oxygen.www.enties.Event;
import com.oxygen.www.module.team.construt.GroupsConstruct;

public class EventConstruct {
	public static ArrayList<Event> Toeventlist(JSONObject jsonobject) {
		ArrayList<Event> events = new ArrayList<Event>();
		try {
			if (jsonobject.getInt("status") == 200) {
				JSONArray jsonarray_data = jsonobject.getJSONArray("data");
				for (int i = 0; i < jsonarray_data.length(); i++) {
					JSONObject c = (JSONObject) jsonarray_data.opt(i);
					events.add(Toevent(c));
				}
				return events;

			} else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return events;
	}

	public static ArrayList<Event> Toeventlist(JSONArray jsonarray_data) {
		ArrayList<Event> events = new ArrayList<Event>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			events.add(Toevent(c));
		}
		return events;
	}

	public static ArrayList<RankUser> ToRankUserlist(JSONArray jsonarray_data) {
		ArrayList<RankUser> ranks = new ArrayList<RankUser>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			ranks.add(toRankUser(c));
		}
		return ranks;
	}
	
	public static ArrayList<Photo> toGroupPhotos(JSONArray jsonarray_data) {
		ArrayList<Photo> groupPhotos = new ArrayList<Photo>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			groupPhotos.add(toGroupPhoto(c));
		}
		return groupPhotos;
	}

	public static Event Toevent(JSONObject c) {

		Event event = new Event();
		if (!c.isNull("id")) {
			try {
				event.id = c.getInt("id");
				if (!c.isNull("sport_name")) {
					event.sport_chn = c.getString("sport_name");
				}
				if (!c.isNull("sport")) {
					event.sport_eng = c.getString("sport");
				}
				if (!c.isNull("duration"))
					event.duration = c.getInt("duration");
				/*if (!c.isNull("latitude"))
					event.latitude = c.getDouble("latitude");
				if (!c.isNull("longitude"))
					event.longitude = c.getDouble("longitude");*/
				if (!c.isNull("location")) {
					JSONObject jsObject_location = c.getJSONObject("location");
					if (!jsObject_location.isNull("address")) {
						event.address = jsObject_location.getString("address");
					}
					if (!jsObject_location.isNull("latitude")) {
						event.latitude = jsObject_location.getDouble("latitude");
					}
					if (!jsObject_location.isNull("longitude")) {
						event.longitude = jsObject_location.getDouble("longitude");
					}
					
				}
				if (!c.isNull("start_time"))
					event.start_time = c.getString("start_time");
				if (!c.isNull("end_time"))
					event.end_time = c.getString("end_time");
				if (!c.isNull("created_by"))
					event.created_by = c.getInt("created_by");
				if (!c.isNull("created_at"))
					event.created_at = c.getString("created_at");
				if (!c.isNull("title"))
					event.tilte = c.getString("title");
				if (!c.isNull("status"))
					event.status = c.getString("status");

				if (!c.isNull("altitude")) {
					event.altitude = c.getDouble("altitude");
				}

				if (!c.isNull("limitation")) {
					event.limitation = c.getInt("limitation");
				}

				if (!c.isNull("accept_count")) {
					event.accept_count = c.getInt("accept_count");
				}
				if (!c.isNull("show_rank")) {
					event.show_rank = c.getInt("show_rank");
				}
				if (!c.isNull("ranklist_limit")) {
					event.ranklist_limit = c.getInt("ranklist_limit");
				}
				if (!c.isNull("ranking")) {
					event.ranking = c.getString("ranking");
				}
				if (!c.isNull("intro")) {
					event.intro = c.getString("intro");
				}
				if (!c.isNull("token")) {
					event.token = c.getString("token");
				}
				if (!c.isNull("current_event_user")) {
					event.current_event_user = toCurrentEventUser(c
							.getJSONObject("current_event_user"));
				}
				if (!c.isNull("accept_list")) {
					event.acceptlist = UsersConstruct.ToUserlist(c
							.getJSONArray("accept_list"));
				}
				if (!c.isNull("moments")) {
					event.moments = MomentConstruct.ToMomentlist(c
							.getJSONArray("moments"));
				}
				if (!c.isNull("performance")) {
					event.performance = ActivitiesConstruct.Toactivity(c
							.getJSONObject("performance"));
				}
				if (!c.isNull("bpp")) {
					JSONArray bpp = c.getJSONArray("bpp");
					if(bpp!=null&&bpp.length()>0){
						event.bpp = ActivitiesConstruct.Toactivity((JSONObject)bpp.opt(0));
					}
				
				}
				if (!c.isNull("leaderboard")) {
					event.ranklist = ActivitiesConstruct.Toactivitylist(c
							.getJSONObject("leaderboard").getJSONArray(
									"ranklist"));
					event.rank_count = c.getJSONObject("leaderboard").getInt(
							"count");
				}
				if (!c.isNull("pic")) {
					event.pic = c.getString("pic");
				}
				if (!c.isNull("group_id")) {
					event.group_id = c.getString("group_id");
				}
				if (!c.isNull("group")) {
					event.group = GroupsConstruct.ToGroup((c
							.getJSONObject("group")));
				}
				if (!c.isNull("photo_count")) {
					event.photo_count = c.getInt("photo_count");
				}
				if (!c.isNull("photos")) {
					event.photos = toGroupPhotos((c
							.getJSONArray("photos")));
				}
				if (!c.isNull("privacy")) {
					event.privacy = c.getInt("privacy");
				}
				if (!c.isNull("pricing")) {
					event.pricing = c.getString("pricing");
				}
				if (!c.isNull("fees")) {
					event.fees = c.getInt("fees");
				}
				if (!c.isNull("max_fees")) {
					event.max_fees = c.getInt("max_fees");
				}
				if (!c.isNull("entry_form")) {
					event.entry_form = c.getString("entry_form");
				}
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return event;

	}

	public static ArrayList<Route> getRoute(String str_route) {
		ArrayList<Route> routes = new ArrayList<Route>();
		try {
			JSONArray json_route = new JSONArray(str_route);
			if (json_route != null && json_route.length() > 0) {
				for (int i = 0; i < json_route.length(); i++) {
					Route route = new Route();
					route.start_longitude = (Double) ((JSONArray) json_route
							.get(i)).get(0);
					if (route.start_longitude > 0) {
						route.start_latitude = (Double) ((JSONArray) json_route
								.get(i)).get(1);
						route.location_time =Long.parseLong(((JSONArray) json_route
								.get(i)).get(2).toString());
						routes.add(route);
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return routes;
	}

	public static CurrentEventUser toCurrentEventUser(JSONObject json_user) {
		CurrentEventUser currentEventUser = new CurrentEventUser();
		try {
			if (!json_user.isNull("checkin"))
				currentEventUser.checkin = json_user.getInt("checkin");
			if (!json_user.isNull("event_id"))
				currentEventUser.event_id = json_user.getInt("event_id");
			if (!json_user.isNull("id"))
				currentEventUser.id = json_user.getInt("id");
			if (!json_user.isNull("is_rank"))
				currentEventUser.is_rank = json_user.getInt("is_rank");
			if (!json_user.isNull("user_id"))
				currentEventUser.user_id = json_user.getInt("user_id");
			if (!json_user.isNull("role"))
				currentEventUser.role = json_user.getString("role");
			if (!json_user.isNull("status"))
				currentEventUser.status = json_user.getString("status");
			if (!json_user.isNull("created_at"))
				currentEventUser.created_at = json_user.getString("created_at");
			if (!json_user.isNull("modified_at"))
				currentEventUser.modified_at = json_user
						.getString("modified_at");
			if (!json_user.isNull("checkin_status")) {
				currentEventUser.checkin_staus = json_user.getString("checkin_status");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return currentEventUser;
	}

	public static RankUser toRankUser(JSONObject json_user) {
		RankUser rankUser = new RankUser();
		try {
			if (!json_user.isNull("checkin"))
				rankUser.checkin = json_user.getInt("checkin");
			if (!json_user.isNull("activity_id"))
				rankUser.activity_id = json_user.getInt("activity_id");
			if (!json_user.isNull("event_id"))
				rankUser.event_id = json_user.getInt("event_id");
			if (!json_user.isNull("id"))
				rankUser.id = json_user.getInt("id");
			if (!json_user.isNull("is_rank"))
				rankUser.is_rank = json_user.getInt("is_rank");
			if (!json_user.isNull("user_id"))
				rankUser.user_id = json_user.getInt("user_id");
			if (!json_user.isNull("role"))
				rankUser.role = json_user.getString("role");
			if (!json_user.isNull("status"))
				rankUser.status = json_user.getString("status");
			if (!json_user.isNull("created_at"))
				rankUser.created_at = json_user.getString("created_at");
			if (!json_user.isNull("modified_at"))
				rankUser.modified_at = json_user.getString("modified_at");
			if (!json_user.isNull("duration"))
				rankUser.duration = json_user.getInt("duration");
			if (!json_user.isNull("distance"))
				rankUser.distance = json_user.getDouble("distance");
			if (!json_user.isNull("calorie"))
				rankUser.calorie = json_user.getString("calorie");
			if (!json_user.isNull("pace"))
				rankUser.pace = json_user.getString("pace");
			if (!json_user.isNull("speed"))
				rankUser.speed = json_user.getString("speed");
			if (!json_user.isNull("score"))
				rankUser.score = json_user.getString("score");
			if (!json_user.isNull("match_lose"))
				rankUser.match_lose = json_user.getInt("match_lose");
			if (!json_user.isNull("match_win"))
				rankUser.match_win = json_user.getInt("match_win");
			if (!json_user.isNull("manual"))
				rankUser.manual = json_user.getString("manual");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return rankUser;
	}
	
	public static Photo toGroupPhoto(JSONObject json_user) {
		Photo groupPhoto = new Photo();
		try {
			if (!json_user.isNull("created_at"))
				groupPhoto.created_at = json_user.getString("created_at");
			if (!json_user.isNull("created_by"))
				groupPhoto.created_by = json_user.getInt("created_by");
			if (!json_user.isNull("filename"))
				groupPhoto.filename = json_user.getString("filename");
			if (!json_user.isNull("id"))
				groupPhoto.created_at = json_user.getString("id");
			if (!json_user.isNull("modified_at"))
				groupPhoto.modified_at = json_user.getString("modified_at");
			if (!json_user.isNull("modified_by"))
				groupPhoto.modified_by = json_user.getInt("modified_by");
			if (!json_user.isNull("path"))
				groupPhoto.path = json_user.getString("path");
			if (!json_user.isNull("target_id"))
				groupPhoto.target_id = json_user.getInt("target_id");
			if (!json_user.isNull("target_type"))
				groupPhoto.target_type = json_user.getString("target_type");
			if (!json_user.isNull("url"))
				groupPhoto.url = json_user.getString("url");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return groupPhoto;
	}

}
