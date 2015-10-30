package com.oxygen.www.enties;

import java.io.Serializable;
import java.util.ArrayList;

public class Moment implements Serializable{
	public int id=0;
	public int activity_id =0;
	public ArrayList<Comment> comments;
	public String created_at;
	public int created_by=0;
	public int event_id=0;
	public String modified_at;
	public int modified_by=0;
	public ArrayList<Photo> photos;
	public String series_id;
	public ArrayList<Vote> votes;
	public String words;
	public String challenge_id;
	public String group_id;
	public String team_id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}
	public ArrayList<Comment> getComments() {
		return comments;
	}
	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
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
	public int getEvent_id() {
		return event_id;
	}
	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}
	public String getModified_at() {
		return modified_at;
	}
	public void setModified_at(String modified_at) {
		this.modified_at = modified_at;
	}
	public int getModified_by() {
		return modified_by;
	}
	public void setModified_by(int modified_by) {
		this.modified_by = modified_by;
	}
	public ArrayList<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}
	public String getSeries_id() {
		return series_id;
	}
	public void setSeries_id(String series_id) {
		this.series_id = series_id;
	}
	public ArrayList<Vote> getVotes() {
		return votes;
	}
	public void setVotes(ArrayList<Vote> votes) {
		this.votes = votes;
	}
	public String getWords() {
		return words;
	}
	public void setWords(String words) {
		this.words = words;
	}
	public String getChallenge_id() {
		return challenge_id;
	}
	public void setChallenge_id(String challenge_id) {
		this.challenge_id = challenge_id;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getTeam_id() {
		return team_id;
	}
	public void setTeam_id(String team_id) {
		this.team_id = team_id;
	}
	
	
}
