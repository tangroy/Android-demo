package com.oxygen.www.module.user.construct;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.oxygen.www.enties.LevelInfo;
import com.oxygen.www.enties.Task;
import com.oxygen.www.module.sport.construct.UsersConstruct;

public class LevelInfoConstruct {
	public static List<Task> ToTasklist(JSONArray jsonarray_data) {
		List<Task> tasks = new ArrayList<Task>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			tasks.add(ToTask(c));
		}

		return tasks;
	}
	
	public static LevelInfo TolevelInfo(JSONObject c) {
		LevelInfo levelInfo = new LevelInfo();
		try {
			if (!c.isNull("completed")) {
				levelInfo.completed = c.getInt("completed");
			}
			if (!c.isNull("total")) {
				levelInfo.total = c.getInt("total");
			}
			if (!c.isNull("total_coins")) {
				levelInfo.total_coins = c.getInt("total_coins");
			}
			if (!c.isNull("user")) {
				levelInfo.user = UsersConstruct.ToUser(c.getJSONObject("user"));
			}
			if (!c.isNull("tasks")) {
				levelInfo.tasks = ToTasklist(c
						.getJSONArray("tasks"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return levelInfo;
	}

	public static Task ToTask(JSONObject c) {
		Task task = new Task();
		try {
			if (!c.isNull("action")) {
				task.action = c.getString("action");
			}
			if (!c.isNull("count")) {
				task.count = c.getInt("count");
			}
			if (!c.isNull("coins")) {
				task.coins = c.getInt("coins");
			}
			if (!c.isNull("title")) {
				task.title = c.getString("title");
			}
			if (!c.isNull("completed")) {
				task.completed = c.getString("completed");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return task;

	}
}
