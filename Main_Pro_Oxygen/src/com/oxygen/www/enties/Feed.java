package com.oxygen.www.enties;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * 动态-Feed
 * 
 * @author 张坤
 *
 */
public class Feed implements Serializable {

	private String action;
	private int comment_count;
	private List<Comment> comments;
	private String content;
	private String created_at;
	private String created_by;
	private Map<String, Object> feed_data;
	private String group_id;
	private String id;
	private String receiver;
	private String target_id;
	private String target_type;
	private String title;
	private String user_id;
	private List<Vote>votes;
	private String curr_user_voted;
	
	public String getCurr_user_voted() {
		return curr_user_voted;
	}
	public void setCurr_user_voted(String curr_user_voted) {
		this.curr_user_voted = curr_user_voted;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
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
	public Map<String, Object> getFeed_data() {
		return feed_data;
	}
	public void setFeed_data(Map<String, Object> feed_data) {
		this.feed_data = feed_data;
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
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
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
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public List<Vote> getVotes() {
		return votes;
	}
	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}
	
	
}
