package com.oxygen.www.module.team.eventbus_enties;

public class CreateTeamName {
	public String name;
	public String imgUrl;
	public String id;

	public CreateTeamName(String name,String imgUrl,String id) {
		this.name = name;
		this.imgUrl = imgUrl;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
