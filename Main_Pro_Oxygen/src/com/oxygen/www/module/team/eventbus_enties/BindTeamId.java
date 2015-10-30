package com.oxygen.www.module.team.eventbus_enties;

/**
 * 关联团队的id
 * 
 * @author yang 2015-5-20上午11:08:08
 */
public class BindTeamId {
	public String teamId;
	public String name;
	public String picUrl;

	public BindTeamId(String teamId,String name,String picUrl) {
		this.teamId = teamId;
		this.name = name;
		this.picUrl = picUrl;
	}
	
	public BindTeamId(String teamId,String name) {
		this.teamId = teamId;
		this.name = name;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
}
