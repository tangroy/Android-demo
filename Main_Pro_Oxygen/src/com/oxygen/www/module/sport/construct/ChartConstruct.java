package com.oxygen.www.module.sport.construct;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.oxygen.www.enties.Chart;

public class ChartConstruct {
	public static ArrayList<Chart> ToChartlist(JSONArray jsonarray_data) {
		ArrayList<Chart> Charts = new ArrayList<Chart>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			Charts.add(ToChart(c));
		}

		return Charts;
	}

	public static Chart ToChart(JSONObject c) {
		Chart chart = new Chart();
		try {
			if (!c.isNull("sport")) {
				chart.sport = c.getString("sport");
			}
			if (!c.isNull("percentage")) {
				chart.percentage = c.getInt("percentage");
			}
			if (!c.isNull("times")) {
				chart.times = c.getInt("times");
			}
			if (!c.isNull("time")) {
				chart.time = c.getString("time");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return chart;

	}
}
