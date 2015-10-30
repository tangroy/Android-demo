package com.oxygen.www.module.sport.construct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.oxygen.www.enties.User;
import com.oxygen.www.enties.UserInfo;
import com.oxygen.www.enties.UserInfos;

public class UsersConstruct {
	public static List<User> ToUserlist(JSONArray jsonarray_data) {
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			users.add(ToUser(c));
		}

		return users;
	}

	public static List<User> ToUserlist(JSONArray jsonarray_data,
			JSONObject jsonobject_userinfo_net) {
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			User user = ToUser(c);
			try {
				if (jsonobject_userinfo_net != null) {
					user.nickname = jsonobject_userinfo_net.getJSONObject(
							c.getString("user_id") + "").getString("nickname");
					user.headimgurl = jsonobject_userinfo_net.getJSONObject(
							c.getString("user_id") + "")
							.getString("headimgurl");

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			users.add(user);
		}
		return users;
	}

	public static User ToUser(JSONObject c) {
		User user = new User();
		if (!c.isNull("id")) {
			try {
				user.id = c.getInt("id");
				if (!c.isNull("type")) {
					user.type = c.getString("type");
				}
				if (!c.isNull("username")) {
					user.username = c.getString("username");
				}
				if (!c.isNull("mobile")) {
					user.mobile = c.getString("mobile");
				}
				if (!c.isNull("email")) {
					user.email = c.getString("email");
				}
				if (!c.isNull("openid")) {
					user.openid = c.getString("openid");
				}
				if (!c.isNull("unionid")) {
					user.unionid = c.getString("unionid");
				}
				if (!c.isNull("nickname")) {
					user.nickname = c.getString("nickname");
				}
				if (!c.isNull("sex")) {
					user.sex = c.getInt("sex");
				}
				if (!c.isNull("country")) {
					user.country = c.getString("country");
				}
				if (!c.isNull("province")) {
					user.province = c.getString("province");
				}
				if (!c.isNull("city")) {
					user.city = c.getString("city");
				}
				if (!c.isNull("headimgurl")) {
					user.headimgurl = c.getString("headimgurl");
				}
				if (!c.isNull("intro")) {
					user.intro = c.getString("intro");
				}
				if (!c.isNull("weight")) {
					user.weight = c.getInt("weight");
				}
				if (!c.isNull("height")) {
					user.height = c.getInt("height");
				}
				if (!c.isNull("age")) {
					user.age = c.getInt("age");
				}
				if (!c.isNull("sports")) {
					user.sports = c.getString("sports");
				}
				if (!c.isNull("created_at")) {
					user.created_at = c.getString("created_at");
				}

				if (!c.isNull("qq_openid")) {
					user.qq_openid = c.getString("qq_openid");
				}
				if (!c.isNull("summary")) {
					user.summarys = SummaryConstruct.ToSummarylist(c
							.getJSONArray("summary"));
				}
				if (!c.isNull("pie_chart")) {
					user.pie_charts = ChartConstruct.ToChartlist(c
							.getJSONArray("pie_chart"));
				}
				if (!c.isNull("user_id")) {
					user.user_id = c.getInt("user_id");
				}
				if (!c.isNull("event_count")) {
					user.event_count = c.getInt("event_count");
				}
				if (!c.isNull("friend_count")) {
					user.friend_count = c.getInt("friend_count");
				}
				if (!c.isNull("group_count")) {
					user.group_count = c.getInt("group_count");
				}
				if (!c.isNull("account_balance")) {
					user.account_balance = c.getString("account_balance");
				}
				if (!c.isNull("total_calorie")) {
					user.total_calorie = c.getString("total_calorie");
				}
				if (!c.isNull("total_duration")) {
					user.total_duration = c.getString("total_duration");
				}
				if (!c.isNull("relationship")) {
					user.relationship = c.getString("relationship");
				}
				if (!c.isNull("new_relationship")) {
					user.new_relationship = c.getString("new_relationship");
				}
				if (!c.isNull("token")) {
					user.token = c.getString("token");
				}
				if (!c.isNull("checkin_status")) {
					user.checkin_status = c.getString("checkin_status");
				}
				if (!c.isNull("checkin_at")) {
					user.checkin_at = c.getString("checkin_at");
				}
				if (!c.isNull("address")) {
					user.address = c.getString("address");
				}
				if (!c.isNull("latitude")) {
					user.latitude = c.getDouble("latitude");
				}
				if (!c.isNull("longitude")) {
					user.longitude = c.getDouble("longitude");
				}
				if (!c.isNull("level")) {
					user.level = c.getString("level");
				}
				if (!c.isNull("coins")) {
					user.coins = c.getString("coins");
				}
				if (!c.isNull("points")) {
					user.points = c.getString("points");
				}
				if (!c.isNull("is_create")) {
					user.is_create = c.getString("is_create");
				}
				if (!c.isNull("bookmark_count")) {
					user.bookmark_count = c.getInt("bookmark_count");
				}
				if (!c.isNull("period")) {
					user.period = c.getString("period");
				}
				if (!c.isNull("total_duration_format")) {
					user.total_duration_format = c
							.getString("total_duration_format");
				}
				if (!c.isNull("total_duration_hour")) {
					user.total_duration_hour = c.getInt("total_duration_hour");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return user;
	}

	public static UserInfo ToUserInfo(JSONObject c) {
		UserInfo userInfo = new UserInfo();
		if (!c.isNull("id")) {
			try {
				userInfo.id = c.getInt("id");
				if (!c.isNull("nickname")) {
					userInfo.nickname = c.getString("nickname");
				}
				if (!c.isNull("sex")) {
					userInfo.sex = c.getString("sex");
				}
				if (!c.isNull("headimgurl")) {
					userInfo.headimgurl = c.getString("headimgurl");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return userInfo;
	}
}
