package com.oxygen.www.enties;

import java.io.Serializable;
import java.util.ArrayList;

public class Sport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 运动id
	 */
	public int id;

	/**
	 * 运动类型英文
	 */
	public String sport;

	/**
	 * 开始地点
	 * 
	 */
	public String address = null;
	/**
	 * 开始时间
	 */
	public String start_time = null;
	/**
	 * 结束时间
	 */
	public String end_time = null;

	/**
	 * 创建时间
	 */
	public String created_at = null;
	/**
	 * 创建用户
	 */
	public int created_by = 0;
	/**
	 * 活动标题
	 */
	public String title;
	/**
	 * cancel为取消，其他状态忽略，以开始时间为准
	 */
	public String status = "cancel";

	/**
	 * 可报名人数
	 */
	public int limitation = 0;
	/**
	 * 报名人数
	 */
	public int accept_count = 0;

	/**
	 * 活动简介
	 */
	public String intro;

	public String token;

	public String pic;

	public int target_id;
	public String target_type;
	public String challenge_type;
	public String sport_data;
	public int team_id=0;
	public String days_left;
	public String NickName;
	public String pace_min="0'00''";
	public String pace_max="0'00''";;
	public int local =0;
	/**
	 * 1表示已同步成绩，0表示未同步成绩
	 */
	public int synchronize =1; 
	
	/**
	 * 数据来源, 取值: native 和 xiaomi
	 */
	public String source;
	

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getLimitation() {
		return limitation;
	}

	public void setLimitation(int limitation) {
		this.limitation = limitation;
	}

	public int getAccept_count() {
		return accept_count;
	}

	public void setAccept_count(int accept_count) {
		this.accept_count = accept_count;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int get_id() {
		return id;
	}

	public void set_id(int id) {
		this.id = id;
	}

	public String getaddresss() {
		return address;
	}

	public void setaddresss(String start_addresss) {
		this.address = start_addresss;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_time(String creat_at) {
		this.created_at = creat_at;
	}

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int creat_by) {
		this.created_by = creat_by;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getTarget_id() {
		return target_id;
	}

	public void setTarget_id(int target_id) {
		this.target_id = target_id;
	}

	public String getTarget_type() {
		return target_type;
	}

	public void setTarget_type(String target_type) {
		this.target_type = target_type;
	}

	public String getChallenge_type() {
		return challenge_type;
	}

	public void setChallenge_type(String challenge_type) {
		this.challenge_type = challenge_type;
	}

	public String getSport_data() {
		return sport_data;
	}

	public void setSport_data(String sport_data) {
		this.sport_data = sport_data;
	}

	public int getTeam_id() {
		return team_id;
	}

	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}

	public String getDays_left() {
		return days_left;
	}

	public void setDays_left(String days_left) {
		this.days_left = days_left;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public int getSynchronize() {
		return synchronize;
	}

	public void setSynchronize(int synchronize) {
		this.synchronize = synchronize;
	}

	@Override
	public String toString() {
		return "Sport [id=" + id + ", sport=" + sport + ", address=" + address
				+ ", start_time=" + start_time + ", end_time=" + end_time
				+ ", created_at=" + created_at + ", created_by=" + created_by
				+ ", title=" + title + ", status=" + status + ", limitation="
				+ limitation + ", accept_count=" + accept_count + ", intro="
				+ intro + ", token=" + token + ", pic=" + pic + ", target_id="
				+ target_id + ", target_type=" + target_type
				+ ", challenge_type=" + challenge_type + ", sport_data="
				+ sport_data + ", team_id=" + team_id + ", days_left="
				+ days_left + ", NickName=" + NickName + ", synchronize="
				+ synchronize + "]";
	}

	public int getLocal() {
		return local;
	}

	public void setLocal(int local) {
		this.local = local;
	}

	public String getPace_min() {
		return pace_min;
	}

	public void setPace_min(String pace_min) {
		this.pace_min = pace_min;
	}

	public String getPace_max() {
		return pace_max;
	}

	public void setPace_max(String pace_max) {
		this.pace_max = pace_max;
	}

}
