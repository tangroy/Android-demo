package com.oxygen.www.enties;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * 动态 - 全部数据
 * 
 * @author 张坤
 *
 */
public class Feeds implements Serializable {
	
	/**
	 * 顶部 banner
	 */
	private List<Banner> banners;
	/**
	 * 列表数据
	 */
	private List<Feed> data;
	/**
	 * 请求返回状态
	 */
	private String msg;
	/**
	 * 请求返回状态码
	 */
	private int status;
	/**
	 * 用户信息
	 */
	private Map<String, UserInfo> users_info;
	/**
	 * 当前用户信息
	 */
	private UserInfo current_user;
	/**
	 * 推荐好友
	 */
	private List<FeedUser> friend_suggest;
	
	
	public List<FeedUser> getFriend_suggest() {
		return friend_suggest;
	}
	public void setFriend_suggest(List<FeedUser> friend_suggest) {
		this.friend_suggest = friend_suggest;
	}
	public UserInfo getCurrent_user() {
		return current_user;
	}
	public void setCurrent_user(UserInfo current_user) {
		this.current_user = current_user;
	}
	public List<Banner> getBanners() {
		return banners;
	}
	public void setBanners(List<Banner> banners) {
		this.banners = banners;
	}
	public List<Feed> getData() {
		return data;
	}
	public void setData(List<Feed> data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Map<String, UserInfo> getUsers_info() {
		return users_info;
	}
	public void setUsers_info(Map<String, UserInfo> users_info) {
		this.users_info = users_info;
	}
	
	
}
