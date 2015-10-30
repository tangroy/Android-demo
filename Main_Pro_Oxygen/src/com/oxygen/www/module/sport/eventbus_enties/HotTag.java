package com.oxygen.www.module.sport.eventbus_enties;

import java.util.List;
import java.util.Map;

import com.oxygen.www.enties.Feed;
import com.oxygen.www.enties.FeedMoment;
import com.oxygen.www.enties.UserInfo;

/**
 * 微信支付
 * 
 * @author 张坤
 */
public class HotTag {
	
	public int status;
	public List<FeedMoment> list;
	public Map<String, UserInfo> usersInfo;
	
	public HotTag(int status, List<FeedMoment> list, Map<String, UserInfo> usersInfo) {
		this.status = status;
		this.list = list;
		this.usersInfo = usersInfo;
	}
	
	public Map<String, UserInfo> getUsersInfo() {
		return usersInfo;
	}

	public void setUsersInfo(Map<String, UserInfo> usersInfo) {
		this.usersInfo = usersInfo;
	}

	public List<FeedMoment> getList() {
		return list;
	}

	public void setList(List<FeedMoment> list) {
		this.list = list;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
