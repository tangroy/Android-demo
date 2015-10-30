package com.oxygen.www.enties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class EventFeed implements Serializable{
	public String action;
	public String comment_count;
	public List<Comment> comments = new ArrayList<Comment>();
	public ArrayList<Vote> votes = new ArrayList<Vote>();
	public String content;
	public String created_at;
	public String created_by;
	public String curr_user_voted;
	public String group_id;
	public String id;
	public String target_id;
	public String target_type;
	public String title;
	public String type;
	public String pic;
	public String intro;
	public String user_id;
	public String vote_count;
	public String address;
	public String data_title;
	public String days_left;
	public String name;
	public String sport;
	public String is_team;
	public String is_group;
	
	public String getIs_team() {
		return is_team;
	}
	public void setIs_team(String is_team) {
		this.is_team = is_team;
	}
	public String getIs_group() {
		return is_group;
	}
	public void setIs_group(String is_group) {
		this.is_group = is_group;
	}
	public String getSport() {
		return sport;
	}
	public void setSport(String sport) {
		this.sport = sport;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getData_title() {
		return data_title;
	}
	public void setData_title(String data_title) {
		this.data_title = data_title;
	}
	public String getDays_left() {
		return days_left;
	}
	public void setDays_left(String days_left) {
		this.days_left = days_left;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getComment_count() {
		return comment_count;
	}
	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getCurr_user_voted() {
		return curr_user_voted;
	}
	public void setCurr_user_voted(String curr_user_voted) {
		this.curr_user_voted = curr_user_voted;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTarget_id() {
		return target_id;
	}
	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}
	public String getTarget_type() {
		return target_type;
	}
	public void setTarget_type(String target_type) {
		this.target_type = target_type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getVote_count() {
		return vote_count;
	}
	public void setVote_count(String vote_count) {
		this.vote_count = vote_count;
	}
	
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public ArrayList<Vote> getVotes() {
		return votes;
	}
	public void setVotes(ArrayList<Vote> votes) {
		this.votes = votes;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	
}
