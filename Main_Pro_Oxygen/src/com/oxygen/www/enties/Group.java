package com.oxygen.www.enties;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 团队信息
 * 
 * @author yang 2015-5-19下午6:20:38
 */
public class Group implements Serializable {
	public int id;
	public String sport;
	/**
	 * 创建时间
	 */
	public String created_at;
	public String intro;
	public String modified_at;
	public int created_by;
	public int modified_by;
	public String name;
	public String pic;
	public int location_id;
	/**
	 * 成立时间
	 */
	public String started_at;
	public String token;
	public String role;
	public int event_count;
	public int member_count;
	public int friend_count;
	public String address;
	public ArrayList<Event> events = new ArrayList<Event>();
	public ArrayList<ChallengesUser> members = new ArrayList<ChallengesUser>();
	public ArrayList<EventFeed> feeds = new ArrayList<EventFeed>();
	public ChallengesUser current_group_user;

	public Group(String sport, String intro, String name, String started_at,
			String address,String pic) {
		this.sport = sport;
		this.intro = intro;
		this.name = name;
		this.started_at = started_at;
		this.address = address;
		this.pic = pic;
	}

	public Group() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getModified_at() {
		return modified_at;
	}

	public void setModified_at(String modified_at) {
		this.modified_at = modified_at;
	}

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	public int getModified_by() {
		return modified_by;
	}

	public void setModified_by(int modified_by) {
		this.modified_by = modified_by;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getLocation_id() {
		return location_id;
	}

	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}

	public String getStarted_at() {
		return started_at;
	}

	public void setStarted_at(String started_at) {
		this.started_at = started_at;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getEvent_count() {
		return event_count;
	}

	public void setEvent_count(int event_count) {
		this.event_count = event_count;
	}

	public int getMember_count() {
		return member_count;
	}

	public void setMember_count(int member_count) {
		this.member_count = member_count;
	}

	public int getFriend_count() {
		return friend_count;
	}

	public void setFriend_count(int friend_count) {
		this.friend_count = friend_count;
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	public ArrayList<ChallengesUser> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<ChallengesUser> members) {
		this.members = members;
	}

	public ChallengesUser getCurrent_group_user() {
		return current_group_user;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCurrent_group_user(ChallengesUser current_group_user) {
		this.current_group_user = current_group_user;
	}

	public ArrayList<EventFeed> getFeeds() {
		return feeds;
	}

	public void setFeeds(ArrayList<EventFeed> feeds) {
		this.feeds = feeds;
	}

}
