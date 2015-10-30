package com.oxygen.www.enties;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;

public class Challenges implements Serializable {
	public int id;
	public String calorie;
	public String created_at;
	public int created_by;
	public int days;
	public double distance;
	public String end_time;
	public String intro;
	public String is_team;
	public String is_group;
	public String modified_at;
	public String pace;
	public int modified_by;
	public String ranking;
	public String speed;

	public String start_time;
	public String status;
	public String title;
	public String token;
	public String type;
	public ArrayList<Team> teams;
	public String days_left;
	public ChallengesUser current_challenge_user;
	public ArrayList<ArrayList<ChallengesUser>> leaderboard;
	public Team ateam;
	public Team bteam;
	public ChallengesUser my_performance;
	public MessageConfig notification_config;
	public ArrayList<ChallengesUser> groups_leaderboard;
	public ArrayList<ChallengesUser> my_group_performance;
	public ArrayList<Moment> moments;
	public String banner;

	/**
	 * 排行榜的 大小
	 */
	public Integer[] leardBoard;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCalorie() {
		return calorie;
	}

	public void setCalorie(String calorie) {
		this.calorie = calorie;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getIs_team() {
		return is_team;
	}

	public void setIs_team(String is_team) {
		this.is_team = is_team;
	}

	public String getModified_at() {
		return modified_at;
	}

	public void setModified_at(String modified_at) {
		this.modified_at = modified_at;
	}

	public String getPace() {
		return pace;
	}

	public void setPace(String pace) {
		this.pace = pace;
	}

	public int getModified_by() {
		return modified_by;
	}

	public void setModified_by(int modified_by) {
		this.modified_by = modified_by;
	}

	public String getRanking() {
		return ranking;
	}

	public void setRanking(String ranking) {
		this.ranking = ranking;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getIs_group() {
		return is_group;
	}

	public void setIs_group(String is_group) {
		this.is_group = is_group;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<Team> getTeams() {
		return teams;
	}

	public void setTeams(ArrayList<Team> teams) {
		this.teams = teams;
	}

	public String getDays_left() {
		return days_left;
	}

	public void setDays_left(String days_left) {
		this.days_left = days_left;
	}

	public ArrayList<ArrayList<ChallengesUser>> getLeaderboard() {
		return leaderboard;
	}

	public void setLeaderboard(ArrayList<ArrayList<ChallengesUser>> leaderboard) {
		this.leaderboard = leaderboard;
	}

	public ChallengesUser getCurrent_challenge_user() {
		return current_challenge_user;
	}

	public void setCurrent_challenge_user(ChallengesUser current_challenge_user) {
		this.current_challenge_user = current_challenge_user;
	}

	public Team getAteam() {
		return ateam;
	}

	public void setAteam(Team ateam) {
		this.ateam = ateam;
	}

	public Team getBteam() {
		return bteam;
	}

	public void setBteam(Team bteam) {
		this.bteam = bteam;
	}

	public ChallengesUser getMy_performance() {
		return my_performance;
	}

	public void setMy_performance(ChallengesUser my_performance) {
		this.my_performance = my_performance;
	}

	public ArrayList<ChallengesUser> getGroups_leaderboard() {
		return groups_leaderboard;
	}

	public void setGroups_leaderboard(
			ArrayList<ChallengesUser> groups_leaderboard) {
		this.groups_leaderboard = groups_leaderboard;
	}

	public ArrayList<ChallengesUser> getMy_group_performance() {
		return my_group_performance;
	}

	public void setMy_group_performance(
			ArrayList<ChallengesUser> my_group_performance) {
		this.my_group_performance = my_group_performance;
	}

	public ArrayList<Moment> getMoments() {
		return moments;
	}

	public void setMoments(ArrayList<Moment> moments) {
		this.moments = moments;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public MessageConfig getNotification_config() {
		return notification_config;
	}

	public void setNotification_config(MessageConfig notification_config) {
		this.notification_config = notification_config;
	}

	public Integer[] getLeardBoard() {
		return leardBoard;
	}

	public void setLeardBoard(Integer[] leardBoard) {
		this.leardBoard = leardBoard;
	}
	
	

}
