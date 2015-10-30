package com.oxygen.www.module.sport.construct;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.enties.Sport;

public class SportConstruct {
	public static ArrayList<Sport> Tosportlist(JSONObject jsonobject,
			JSONObject jsonobject_userinfo_net) {
		ArrayList<Sport> sports = new ArrayList<Sport>();
		try {
			if (jsonobject.getInt("status") == 200) {
				JSONArray jsonarray_data = jsonobject.getJSONArray("data");
				for (int i = 0; i < jsonarray_data.length(); i++) {
					JSONObject c = (JSONObject) jsonarray_data.opt(i);
					Sport sport = Tosport(c);
					if (jsonobject_userinfo_net != null) {
						if (!jsonobject_userinfo_net.isNull(sport
								.getCreated_by() + "")) {
							sport.NickName = jsonobject_userinfo_net
									.getJSONObject(sport.getCreated_by() + "")
									.getString("nickname");
						}
					}
					sports.add(sport);
				}
				return sports;

			} else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sports;
	}

	public static ArrayList<Sport> Tosportlist(JSONArray jsonArray,
			JSONObject jsonobject_userinfo_net) {
		ArrayList<Sport> sports = new ArrayList<Sport>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject c = (JSONObject) jsonArray.opt(i);
			Sport sport = Tosport(c);
			try {
				if (jsonobject_userinfo_net != null) {
					sport.NickName = jsonobject_userinfo_net.getJSONObject(
							sport.getCreated_by() + "").getString("nickname");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sports.add(sport);
		}
		return sports;
	}

	public static Sport Tosport(JSONObject c) {

		Sport sport = new Sport();
		if (!c.isNull("id")) {
			try {
				sport.id = c.getInt("id");
				if (!c.isNull("sport")) {
					sport.sport = c.getString("sport");
				}
				if (!c.isNull("address")) {
					sport.address = c.getString("address");

				}
				if (!c.isNull("start_time"))
					sport.start_time = c.getString("start_time");
				if (!c.isNull("end_time"))
					sport.end_time = c.getString("end_time");
				if (!c.isNull("created_by"))
					sport.created_by = c.getInt("created_by");
				if (!c.isNull("created_at"))
					sport.created_at = c.getString("created_at");
				if (!c.isNull("title"))
					sport.title = c.getString("title");
				if (!c.isNull("status"))
					sport.status = c.getString("status");

				if (!c.isNull("limitation")) {
					sport.limitation = c.getInt("limitation");
				}

				if (!c.isNull("accept_count")) {
					sport.accept_count = c.getInt("accept_count");
				}
				if (!c.isNull("intro")) {
					sport.intro = c.getString("intro");
				}
				if (!c.isNull("token")) {
					sport.token = c.getString("token");
				}

				if (!c.isNull("pic")) {
					sport.pic = c.getString("pic");
				}
				if (!c.isNull("target_id")) {
					sport.target_id = c.getInt("target_id");
				}
				if (!c.isNull("target_type")) {
					sport.target_type = c.getString("target_type");
				}
				if (!c.isNull("challenge_type")) {
					sport.challenge_type = c.getString("challenge_type");
				}
				if (!c.isNull("sport_data")) {
					sport.sport_data = c.getString("sport_data");
				}
				if (!c.isNull("days_left")) {
					sport.days_left = c.getString("days_left");
				}
				if (!c.isNull("team_id")) {
					sport.team_id = c.getInt("team_id");
				}
				if (!c.isNull("source")) {
					/**
					 * 数据来源: native 或者 xiaomi
					 */
					sport.source = c.getString("source");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return sport;

	}

}
