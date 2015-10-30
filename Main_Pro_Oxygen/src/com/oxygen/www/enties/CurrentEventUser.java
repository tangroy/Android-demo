package com.oxygen.www.enties;

import java.io.Serializable;

public class CurrentEventUser implements Serializable {

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
	public String modified_at;
	
	public String checkin_staus;
	
	

	public String getCheckin_staus() {
		return checkin_staus;
	}

	public void setCheckin_staus(String checkin_staus) {
		this.checkin_staus = checkin_staus;
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



	public int user_id = -1;

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

}
