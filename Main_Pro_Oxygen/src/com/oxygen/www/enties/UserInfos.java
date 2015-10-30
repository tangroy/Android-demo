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
public class UserInfos implements Serializable {
	/**
	 * 用户信息
	 */
	private Map<String, UserInfo> users_info;

	public Map<String, UserInfo> getUsers_info() {
		return users_info;
	}

	public void setUsers_info(Map<String, UserInfo> users_info) {
		this.users_info = users_info;
	}

}
