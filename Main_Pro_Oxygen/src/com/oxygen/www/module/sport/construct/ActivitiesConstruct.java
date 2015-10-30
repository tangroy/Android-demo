package com.oxygen.www.module.sport.construct;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.oxygen.www.enties.Route;
import com.oxygen.www.enties.GDAcvitity;

public class ActivitiesConstruct {
	public static ArrayList<GDAcvitity> Toactivitylist(JSONObject jsonobject) {
		ArrayList<GDAcvitity> activitys = new ArrayList<GDAcvitity>();
		try {
			if (jsonobject.getInt("status") == 200) {
				JSONArray jsonarray_data = jsonobject.getJSONArray("data");
				for (int i = 0; i < jsonarray_data.length(); i++) {
					JSONObject c = (JSONObject) jsonarray_data.opt(i);
					activitys.add(Toactivity(c));
				}
				return activitys;

			} else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return activitys;
	}

	public static ArrayList<GDAcvitity> Toactivitylist(JSONArray jsonarray_data) {
		ArrayList<GDAcvitity> activitys = new ArrayList<GDAcvitity>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			activitys.add(Toactivity(c));
		}
		return activitys;

	}

	public static GDAcvitity Toactivity(JSONObject c) {

		GDAcvitity gdactivity = new GDAcvitity();

		try {
			if (c != null && !c.isNull("id")) {
				gdactivity.id = c.getInt("id");
			}
			if (!c.isNull("activity_id")) {
				gdactivity.activity_id = c.getInt("activity_id");
			}
			if (!c.isNull("event_id")) {
				gdactivity.event_id = c.getInt("event_id");
			}
			if (!c.isNull("type"))
				gdactivity.type = c.getString("type");
			if (!c.isNull("sport")) {
				gdactivity.sport_eng = c.getString("sport");

			}
			if (!c.isNull("sport_name")) {
				gdactivity.sport_chn = c.getString("sport_name");
			}
			if (!c.isNull("distance"))
				gdactivity.distance = c.getDouble("distance");
			if (!c.isNull("duration"))
				gdactivity.duration = c.getInt("duration");
			if (!c.isNull("latitude"))
				gdactivity.latitude = c.getDouble("latitude");
			if (!c.isNull("longitude"))
				gdactivity.longitude = c.getDouble("longitude");
			if (!c.isNull("location")) {
				JSONObject jsObject_location = c.getJSONObject("location");
				if (!jsObject_location.isNull("address")) {
					gdactivity.address = jsObject_location.getString("address");
				}
			}
			if (!c.isNull("address")) {
				gdactivity.address = c.getString("address");
			}
			if (!c.isNull("start_time"))
				gdactivity.start_time = c.getString("start_time");
			if (!c.isNull("end_time"))
				gdactivity.end_time = c.getString("end_time");
			if (!c.isNull("route"))
				gdactivity.array_route = getRoute(c.getString("route"));
			if (!c.isNull("created_by"))
				gdactivity.created_by = c.getInt("created_by");
			if (!c.isNull("created_at"))
				gdactivity.created_at = c.getString("created_at");
			if (!c.isNull("title"))
				gdactivity.tilte = c.getString("title");
			if (!c.isNull("status"))
				gdactivity.status = c.getString("status");

			if (!c.isNull("altitude")) {
				gdactivity.altitude = c.getString("altitude");
			}

			if (!c.isNull("match_win")) {
				gdactivity.match_win = c.getInt("match_win");
			}
			if (!c.isNull("match_lose")) {
				gdactivity.match_lose = c.getInt("match_lose");
			}
			if (!c.isNull("token")) {
				gdactivity.token = c.getString("token");
			}
			if (!c.isNull("rank")) {
				gdactivity.rank = c.getInt("rank");
			}
			if (!c.isNull("pace")) {
				gdactivity.pace = c.getString("pace");
			}
			if (!c.isNull("speed")) {
				gdactivity.speed = c.getDouble("speed");
			}
			if (!c.isNull("calorie")) {
				gdactivity.calorie = c.getString("calorie");
			}
			if (!c.isNull("score")) {
				gdactivity.score = c.getInt("score");
			}
			if (!c.isNull("manual")) {
				gdactivity.manual = c.getString("manual");
			}
			if (!c.isNull("sport_data")) {
				gdactivity.sport_data = c.getString("sport_data");
			}
			if (!c.isNull("photos")) {
				gdactivity.photos = PhotosConstruct.ToPhotolist(c
						.getJSONArray("photos"));
			}
			if (!c.isNull("intro")) {
				gdactivity.intro = c.getString("intro");
			}
			if (!c.isNull("user_id")) {
				gdactivity.user_id = c.getInt("user_id");
			}
			if (!c.isNull("pic")) {
				gdactivity.pic = c.getString("pic");
			}
			if (!c.isNull("pace_max")) {
				gdactivity.pace_max = c.getString("pace_max");
			}
			if (!c.isNull("pace_min")) {
				gdactivity.pace_min = c.getString("pace_min");
			}
			if (!c.isNull("automatch")) {
				gdactivity.automatch = c.getString("automatch");
			}
			if (!c.isNull("source")) {
				// 数据来源
				gdactivity.source = c.getString("source");
			}
			if (!c.isNull("step")) {
				// 步数
				gdactivity.step = c.getInt("step");
			}
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return gdactivity;

	}

	/**
	 * 解析轨迹数组
	 * 
	 * @param str_route
	 * @return
	 */
	public static ArrayList<Route> getRoute(String str_route) {
		ArrayList<Route> routes = new ArrayList<Route>();
		try {
			JSONArray json_route = new JSONArray(str_route);
			int route_count = json_route.length();
			//设置割多少个点取一个点
			int Merge = 1;
			if (route_count < 500) {
				Merge = 1;
			} else if (route_count < 3000) {
				Merge = 3;
			} else {
				Merge = 5;
			}
			if (json_route != null && route_count > 0) {
				for (int i = 0; i < route_count; i++) {

					JSONArray route_i = ((JSONArray) json_route.get(i));
					if (route_i != null && route_i.length() > 2) {
						Route route = new Route();
						route.start_longitude = (Double) route_i.get(0);
						route.start_latitude = (Double) route_i.get(1);
						route.location_time = Long
								.parseLong(((JSONArray) json_route.get(i)).get(
										2).toString());
						if (i % Merge == 0) {
							routes.add(route);
						}

					}

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return routes;
	}

}
