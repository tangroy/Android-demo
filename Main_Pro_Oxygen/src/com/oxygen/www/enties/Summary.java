package com.oxygen.www.enties;

public class Summary {
	/**
	 * 运动类型
	 */
	public String sport;
	/**
	 * 次数统计
	 */
	public int count = 0;
	/**
	 * 总时长 秒
	 */
	public int total_duration;
	/**
	 * 总距离
	 */
	public double total_distance;
	/**
	 * 总胜负
	 */
	public String match;

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTotal_duration() {
		return total_duration;
	}

	public void setTotal_duration(int total_duration) {
		this.total_duration = total_duration;
	}

	public double getTotal_distance() {
		return total_distance;
	}

	public void setTotal_distance(double total_distance) {
		this.total_distance = total_distance;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}
}
