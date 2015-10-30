package com.oxygen.www.enties;

import java.io.Serializable;
/**
 * Feed users_info
 * 
 * @author 张坤
 *
 */
public class UserInfo implements Serializable {
	
	public int id;
	public String headimgurl;
	public String nickname;
	public String sex;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}

	
}
