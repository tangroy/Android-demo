package com.oxygen.www.enties;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

	public int id;
	public String type;
	public String username;
	public String mobile;
	public String email;
	public String openid;
	public String unionid;
	public String nickname;
	public int sex = 0;
	public String country;
	public String province;
	public String city;
	public String headimgurl;
	public String intro;
	public int weight = 0;
	public int height = 0;
	public int age = 0;
	public String sports;
	public String created_at;
	public String qq_openid;
	public int user_id;
	public int event_count;
	public int friend_count;
	public int group_count;
	public int bookmark_count;
	public String total_calorie;
	public String total_duration;
	public String relationship;
	public String new_relationship;
	public String token;

	public String checkin_status;
	public String checkin_at;
	public double latitude;
	public double longitude;
	public String address;
	public String level;
	public String coins;
	public String points;
	public String is_create;
	public String account_balance;
	public String period;
	public String total_duration_format;
	public int total_duration_hour;

	public String getIs_create() {
		return is_create;
	}

	public void setIs_create(String is_create) {
		this.is_create = is_create;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCheckin_at() {
		return checkin_at;
	}

	public void setCheckin_at(String checkin_at) {
		this.checkin_at = checkin_at;
	}

	public String getCheckin_status() {
		return checkin_status;
	}

	public void setCheckin_status(String checkin_status) {
		this.checkin_status = checkin_status;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	/**
	 * 环形图数据
	 */
	public ArrayList<Chart> pie_charts;
	/**
	 * 成绩
	 */
	public ArrayList<Summary> summarys;

	public ArrayList<Summary> getSummarys() {
		return summarys;
	}

	public void setSummarys(ArrayList<Summary> summarys) {
		this.summarys = summarys;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public int getBookmark_count() {
		return bookmark_count;
	}

	public void setBookmark_count(int bookmark_count) {
		this.bookmark_count = bookmark_count;
	}

	public String getQq_openid() {
		return qq_openid;
	}

	public void setQq_openid(String qq_openid) {
		this.qq_openid = qq_openid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSports() {
		return sports;
	}

	public void setSports(String sports) {
		this.sports = sports;
	}

	public int getEvent_count() {
		return event_count;
	}

	public void setEvent_count(int event_count) {
		this.event_count = event_count;
	}

	public int getFriend_count() {
		return friend_count;
	}

	public void setFriend_count(int friend_count) {
		this.friend_count = friend_count;
	}

	public int getGroup_count() {
		return group_count;
	}

	public void setGroup_count(int group_count) {
		this.group_count = group_count;
	}

	public String getTotal_calorie() {
		return total_calorie;
	}

	public void setTotal_calorie(String total_calorie) {
		this.total_calorie = total_calorie;
	}

	public String getTotal_duration() {
		return total_duration;
	}

	public void setTotal_duration(String total_duration) {
		this.total_duration = total_duration;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNew_relationship() {
		return new_relationship;
	}

	public void setNew_relationship(String new_relationship) {
		this.new_relationship = new_relationship;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getCoins() {
		return coins;
	}

	public void setCoins(String coins) {
		this.coins = coins;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getAccount_balance() {
		return account_balance;
	}

	public void setAccount_balance(String account_balance) {
		this.account_balance = account_balance;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public ArrayList<Chart> getPie_charts() {
		return pie_charts;
	}

	public void setPie_charts(ArrayList<Chart> pie_charts) {
		this.pie_charts = pie_charts;
	}

	public String getTotal_duration_format() {
		return total_duration_format;
	}

	public void setTotal_duration_format(String total_duration_format) {
		this.total_duration_format = total_duration_format;
	}

	public int getTotal_duration_hour() {
		return total_duration_hour;
	}

	public void setTotal_duration_hour(int total_duration_hour) {
		this.total_duration_hour = total_duration_hour;
	}

}
