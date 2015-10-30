package com.oxygen.www.enties;

import java.io.Serializable;

public class RankUser implements Serializable {

	public int checkin = -1;
	public int event_id = -1;
	public int id = -1;
	/**
	 * 是否参与排名
	 */
	public int is_rank = -1;
	/**
	 * 角色
	 */
	public String role;
	/**
	 * 状态
	 */
	public String status;

	public String created_at;
	public int activity_id;
	public String modified_at;
	public String calorie;
	public int match_lose=0;
	public int match_win=0;
	public String pace;
	public String speed;
	public String score;
	/**
	 * 是否为手动记录
	 */
	public String manual;
	
	
	
	public String getManual() {
		return manual;
	}

	public void setManual(String manual) {
		this.manual = manual;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public double distance = 0.0;
	public int duration = 0;
    public int user_id = -1;
	
	
	public String getCalorie() {
		return calorie;
	}

	public void setCalorie(String calorie) {
		this.calorie = calorie;
	}

	public int getMatch_lose() {
		return match_lose;
	}

	public void setMatch_lose(int match_lose) {
		this.match_lose = match_lose;
	}

	public int getMatch_win() {
		return match_win;
	}

	public void setMatch_win(int match_win) {
		this.match_win = match_win;
	}

	public String getPace() {
		return pace;
	}

	public void setPace(String pace) {
		this.pace = pace;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}



	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getModified_at() {
		return modified_at;
	}

	public void setModified_at(String modified_at) {
		this.modified_at = modified_at;
	}


	public int getCheckin() {
		return checkin;
	}

	public void setCheckin(int checkin) {
		this.checkin = checkin;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIs_rank() {
		return is_rank;
	}

	public void setIs_rank(int is_rank) {
		this.is_rank = is_rank;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}

}
