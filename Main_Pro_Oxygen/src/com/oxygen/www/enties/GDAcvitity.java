package com.oxygen.www.enties;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 运动
 * 
 * @author sambatang
 * 
 */
public class GDAcvitity implements Serializable {
	/**
	 * 运动id
	 */
	public int id;

	public int event_id = -1;
	public int activity_id =0;

	/**
	 * 计划类型
	 */
	public String type = "unplanned";
	/**
	 * 运动类型中文
	 */
	public String sport_chn;
	/**
	 * 运动类型英文
	 */
	public String sport_eng;
	/**
	 * 距离
	 */
	public double distance = 0.0;
	/**
	 * 总共用时
	 */
	public int duration = 0;
	/**
	 * 开始坐标_经度
	 */
	public double longitude = 0.0;
	/**
	 * 开始坐标_纬度
	 */
	public double latitude = 0.0;
	/**
	 * 开始地点
	 * 
	 */
	public String address = null;
	/**
	 * 开始时间
	 */
	public String start_time = null;
	/**
	 * 结束时间
	 */
	public String end_time = null;
	/**
	 * 运动轨迹
	 */
	public ArrayList<Route> array_route = null;
	/**
	 * 创建时间
	 */
	public String created_at = null;
	/**
	 * 创建用户
	 */
	public int created_by = 0;
	/**
	 * 活动标题
	 */
	public String tilte;
	
	/**
	 * 配速
	 */
	public String pace;
	/**
	 * 速度
	 */
	public double speed=0.0;
	/**
	 * 卡路里
	 */
	public String calorie;
	/**
	 * 活动状态 open为记录 close为未记录
	 */
	public String status = "close";
	
	public String intro;

	

	/**
	 * 海拔
	 */
	public String altitude;
	
	
	/**
	 * 得分
	 */
	public int match_win = 0;
	/**
	 * 失分
	 */
	public int match_lose = 0;
	/**
	 * 
	 * 排名
	 */
	public int rank = 0;
	
	/**
	 * 得分
	 */
	public int score=0;
	
	public String manual;
	public String sport_data;
	public ArrayList<Photo> photos;

	public String token;
	
	public int local =0;
	
	public int user_id;
	public String pic;
	
	public String pace_min="0'00''";
	public String pace_max="0'00''";
	
	public String automatch = "no";
	
	/**
	 * 数据来源: xiaomi 和 native
	 */
	public String source;
	/**
	 * 运动步数
	 */
	public int step;
	

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	@Override
	public String toString() {
		return "GDAcvitity [id=" + id + ", event_id=" + event_id + ", type="
				+ type + ", sport_chn=" + sport_chn + ", sport_eng="
				+ sport_eng + ", distance=" + distance + ", duration="
				+ duration + ", longitude=" + longitude + ", latitude="
				+ latitude + ", address=" + address + ", start_time="
				+ start_time + ", end_time=" + end_time + ", array_route="
				+ array_route + ", created_at=" + created_at + ", created_by="
				+ created_by + ", tilte=" + tilte + ", pace=" + pace
				+ ", speed=" + speed + ", calorie=" + calorie + ", status="
				+ status + ", intro=" + intro + ", altitude=" + altitude
				+ ", match_win=" + match_win + ", match_lose=" + match_lose
				+ ", rank=" + rank + ", score=" + score + ", manual=" + manual
				+ ", sport_data=" + sport_data + ", photos=" + photos
				+ ", token=" + token;
	}

	public String getSport_data() {
		return sport_data;
	}

	public void setSport_data(String sport_data) {
		this.sport_data = sport_data;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getMatch_win() {
		return match_win;
	}

	public void setMatch_win(int match_win) {
		this.match_win = match_win;
	}

	public String getPace() {
		return pace;
	}

	public void setPace(String pace) {
		this.pace = pace;
	}

	public double getSpeed() {
		return speed;
	}

	public String getCalorie() {
		return calorie;
	}

	public void setCalorie(String calorie) {
		this.calorie = calorie+"";
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getMatch_lose() {
		return match_lose;
	}

	public void setMatch_lose(int match_lose) {
		this.match_lose = match_lose;
	}

	

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getsport_chn() {
		return sport_chn;
	}

	public void setsport_chn(String sport_chn) {
		this.sport_chn = sport_chn;
	}

	public String getsport_eng() {
		return sport_eng;
	}

	public void setsport_eng(String sport_eng) {
		this.sport_eng = sport_eng;
	}

	public String getAltitude() {
		return altitude;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}


	public String getTilte() {
		return tilte;
	}

	public void setTilte(String tilte) {
		this.tilte = tilte;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int get_id() {
		return id;
	}

	public void set_id(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getlongitude() {
		return longitude;
	}

	public void setlongitude(double start_longitude) {
		this.longitude = start_longitude;
	}

	public double getlatitude() {
		return latitude;
	}

	public void setlatitude(double start_latitude) {
		this.latitude = start_latitude;
	}

	public String getaddresss() {
		return address;
	}

	public void setaddresss(String start_addresss) {
		this.address = start_addresss;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public ArrayList<Route> getRoute() {
		return array_route;
	}

	public void setRoute(ArrayList<Route> array_route) {
		this.array_route = array_route;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_time(String creat_at) {
		this.created_at = creat_at;
	}

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int creat_by) {
		this.created_by = creat_by;
	}

	public String getManual() {
		return manual;
	}

	public void setManual(String manual) {
		this.manual = manual;
	}

	public ArrayList<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	public int getLocal() {
		return local;
	}

	public void setLocal(int local) {
		this.local = local;
	}

	public int getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPace_min() {
		return pace_min;
	}

	public void setPace_min(String pace_min) {
		this.pace_min = pace_min;
	}

	public String getPace_max() {
		return pace_max;
	}

	public void setPace_max(String pace_max) {
		this.pace_max = pace_max;
	}

	public String getAutomatch() {
		return automatch;
	}

	public void setAutomatch(String automatch) {
		this.automatch = automatch;
	}

	 

	 
	
}
