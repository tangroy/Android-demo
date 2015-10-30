package com.oxygen.www.module.sport.eventbus_enties;

import java.util.List;
import java.util.Map;

import com.oxygen.www.enties.Feed;
import com.oxygen.www.enties.FeedUser;
import com.oxygen.www.enties.UserInfo;

/**
 * feeds-好友推荐-改变
 * 
 * @author 张坤
 */
public class FeedFriends {
	
	public int status;
	public List<FeedUser> list;
	
	public FeedFriends(int status, List<FeedUser> list) {
		this.status = status;
		this.list = list;
	}

	public List<FeedUser> getList() {
		return list;
	}

	public void setList(List<FeedUser> list) {
		this.list = list;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
