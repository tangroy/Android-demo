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
public class HotTags implements Serializable {
	
	/**
	 * 列表数据
	 */
	private List<FeedMoment> data;
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
	
	
	public UserInfo getCurrent_user() {
		return current_user;
	}
	public void setCurrent_user(UserInfo current_user) {
		this.current_user = current_user;
	}
	public List<FeedMoment> getData() {
		return data;
	}
	public void setData(List<FeedMoment> data) {
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
