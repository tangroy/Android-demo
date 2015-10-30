package com.oxygen.www.enties;

import java.io.Serializable;

public class ChallengesUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String calorie;
	public int challenge_id;
	public String created_at;
	public int created_by;
	public double distance;
	public String finished_at;
	public String is_finished;
	public String modified_at;
	public String pace;
	public int modified_by;
	public String speed;
	public String status;
	public String team_name;
	public int team_id=0;
	public int user_id;
	public int rank;
	
	public String role;
	public int group_id = 0;
 	public String group_name;
 	public String is_my_group;
	public String pic;
	public String is_group_leader = "no";
 
	@Override
	public String toString() {
		return "ChallengesUser [id=" + id + ", calorie=" + calorie
				+ ", challenge_id=" + challenge_id + ", created_at="
				+ created_at + ", created_by=" + created_by + ", distance="
				+ distance + ", finished_at=" + finished_at + ", is_finished="
				+ is_finished + ", modified_at=" + modified_at + ", pace="
				+ pace + ", modified_by=" + modified_by + ", speed=" + speed
				+ ", status=" + status + ", team_name=" + team_name
				+ ", team_id=" + team_id + ", user_id=" + user_id
				+ ", position=" + rank + "]";
	}

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

	public int getChallenge_id() {
		return challenge_id;
	}

	public void setChallenge_id(int challenge_id) {
		this.challenge_id = challenge_id;
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

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getFinished_at() {
		return finished_at;
	}

	public void setFinished_at(String finished_at) {
		this.finished_at = finished_at;
	}

	public String getIs_finished() {
		return is_finished;
	}

	public void setIs_finished(String is_finished) {
		this.is_finished = is_finished;
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

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTeam_name() {
		return team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}

	public int getTeam_id() {
		return team_id;
	}

	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getIs_my_group() {
		return is_my_group;
	}

	public void setIs_my_group(String is_my_group) {
		this.is_my_group = is_my_group;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getIs_group_leader() {
		return is_group_leader;
	}

	public void setIs_group_leader(String is_group_leader) {
		this.is_group_leader = is_group_leader;
	}
	

}
