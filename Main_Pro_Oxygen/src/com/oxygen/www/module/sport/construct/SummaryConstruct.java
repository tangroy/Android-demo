package com.oxygen.www.module.sport.construct;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.enties.Summary;

public class SummaryConstruct {
	public static ArrayList<Summary> ToSummarylist(JSONArray jsonarray_data) {
		ArrayList<Summary> summarys = new ArrayList<Summary>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			summarys.add(ToSummary(c));
		}

		return summarys;
	}

	public static Summary ToSummary(JSONObject c) {
		Summary summary = new Summary();
		try {
			if (!c.isNull("sport")) {
				summary.sport = c.getString("sport");
			}
			if (!c.isNull("count")) {
				summary.count = c.getInt("count");
			}
			if (!c.isNull("total_duration")) {
				summary.total_duration = c.getInt("total_duration");
			}
			if (!c.isNull("total_distance")) {
				summary.total_distance = c.getDouble("total_distance");
			}
			if (!c.isNull("match")) {
				summary.match = c.getString("match");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return summary;

	}

}
