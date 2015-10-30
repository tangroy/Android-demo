package com.oxygen.www.enties;

import java.util.ArrayList;

public class Posts {
	public int id;
	public String token;
	/**
	 * 标题
	 */
	public String title;
	/**
	 * 作者
	 */
	public String author;
	/**
	 * 发布时间
	 */
	public String publish_time;
	/**
	 * 头条图片
	 */
	public String pic;
	/**
	 * 简介
	 */
	public String summary;
	/**
	 * 点赞
	 */
	public int curruser_voted;
	/**收藏
	 */
	public int curruser_bookmarked;
	/**
	 * 标签
	 */
	public ArrayList<PostsTag> tags;
	
	public int vote_count=0;
	
	public int read_count=0;
 
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getRead_count() {
		return read_count;
	}

	public void setRead_count(int read_count) {
		this.read_count = read_count;
	}

	public int getVote_count() {
		return vote_count;
	}

	public void setVote_count(int vote_count) {
		this.vote_count = vote_count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public ArrayList<PostsTag> getTags() {
		return tags;
	}

	public void setTags(ArrayList<PostsTag> tags) {
		this.tags = tags;
	}

}
