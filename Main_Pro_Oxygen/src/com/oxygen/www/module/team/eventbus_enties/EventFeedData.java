package com.oxygen.www.module.team.eventbus_enties;

import java.util.ArrayList;
import java.util.Map;

import com.oxygen.www.enties.EventFeed;
import com.oxygen.www.enties.UserInfo;

public class EventFeedData {
	public ArrayList<EventFeed> list;
	public Map<String, UserInfo> usersInfo;
	
	
	public EventFeedData(ArrayList<EventFeed> list, Map<String, UserInfo> usersInfo) {
		super();
		this.list = list;
		this.usersInfo = usersInfo;
	}
	public ArrayList<EventFeed> getList() {
		return list;
	}
	public void setList(ArrayList<EventFeed> list) {
		this.list = list;
	}
	public Map<String, UserInfo> getUsersInfo() {
		return usersInfo;
	}
	public void setUsersInfo(Map<String, UserInfo> usersInfo) {
		this.usersInfo = usersInfo;
	}
	
	
}
