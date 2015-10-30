package com.oxygen.www.enties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 运动
 * 
 * @author sambatang
 * 
 */
public class Event implements Serializable {
	/**
	 * 运动id
	 */
	public int id;

	/**
	 * 运动类型中文
	 */
	public String sport_chn;
	/**
	 * 运动类型英文
	 */
	public String sport_eng;

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
	 * cancel为取消，其他状态忽略，以开始时间为准
	 */
	public String status = "cancel";

	/**
	 * 动态
	 */
	public ArrayList<Moment> moments;

	/**
	 * 海拔
	 */
	public double altitude;
	/**
	 * 可报名人数
	 */
	public int limitation = 0;
	/**
	 * 报名人数
	 */
	public int accept_count = 0;

	/**
	 * 战绩秀人数
	 */
	public int rank_count = 0;

	/**
	 * 报名列表
	 */
	public List<User> acceptlist;

	public String attend_end_time;

	public int checkin;
	/**
	 * 最多排名人数
	 */
	public int ranklist_limit = 0;
	public ArrayList<GDAcvitity> ranklist;

	/**
	 * 是否已发布榜单 0未发布 1已发布
	 */
	public int show_rank = 0;
	/**
	 * 时长duration,距离distance,速度speed,配速pace,卡路里calorie
	 */
	public String ranking = "duration";

	/**
	 * 当前EVENT参与用户的信息
	 */
	public CurrentEventUser current_event_user;

	/**
	 * 活动简介
	 */
	public String intro;

	public String token;
	/**
	 * 个人成绩
	 */
	public GDAcvitity performance;

	public GDAcvitity bpp;

	public String pic;
	public String group_id;
	public Group group;
	public int photo_count;
	public List<Photo> photos;
	/**
	 * 隐私设置
	 */
	public int privacy;
	/**
	 * 活动付费类型
	 */
	public String pricing;
	/**
	 * 加入活动所需费用
	 */
	public int fees;
	/**
	 * 最高付费价格
	 */
	public int max_fees;
	/**
	 * 报名者填写项
	 */
	public String entry_form;
	
	
	public String getEntry_form() {
		return entry_form;
	}

	public void setEntry_form(String entry_form) {
		this.entry_form = entry_form;
	}

	public int getPrivacy() {
		return privacy;
	}

	public void setPrivacy(int privacy) {
		this.privacy = privacy;
	}

	public String getPricing() {
		return pricing;
	}

	public void setPricing(String pricing) {
		this.pricing = pricing;
	}

	public int getFees() {
		return fees;
	}

	public void setFees(int fees) {
		this.fees = fees;
	}

	public int getMax_fees() {
		return max_fees;
	}

	public void setMax_fees(int max_fees) {
		this.max_fees = max_fees;
	}

	public GDAcvitity getBpp() {
		return bpp;
	}

	public void setBpp(GDAcvitity bpp) {
		this.bpp = bpp;
	}

	public String getRanking() {
		return ranking;
	}

	public void setRanking(String ranking) {
		this.ranking = ranking;
	}

	public GDAcvitity getPerformance() {
		return performance;
	}

	public int getRank_count() {
		return rank_count;
	}

	public void setRank_count(int rank_count) {
		this.rank_count = rank_count;
	}

	public void setPerformance(GDAcvitity performance) {
		this.performance = performance;
	}

	public CurrentEventUser getCurrent_event_user() {
		return current_event_user;
	}

	public void setCurrent_event_user(CurrentEventUser current_event_user) {
		this.current_event_user = current_event_user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ArrayList<GDAcvitity> getRanklist() {
		return ranklist;
	}

	public void setRanklist(ArrayList<GDAcvitity> ranklist) {
		this.ranklist = ranklist;
	}

	public List<User> getAcceptlist() {
		return acceptlist;
	}

	public void setAcceptlist(ArrayList<User> acceptlist) {
		this.acceptlist = acceptlist;
	}

	public int getLimitation() {
		return limitation;
	}

	public void setLimitation(int limitation) {
		this.limitation = limitation;
	}

	public int getAccept_count() {
		return accept_count;
	}

	public void setAccept_count(int accept_count) {
		this.accept_count = accept_count;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getSport_chn() {
		return sport_chn;
	}

	public void setSport_chn(String sport_chn) {
		this.sport_chn = sport_chn;
	}

	public String getSport_eng() {
		return sport_eng;
	}

	public void setSport_eng(String sport_eng) {
		this.sport_eng = sport_eng;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public ArrayList<Moment> getMoments() {
		return moments;
	}

	public void setMoments(ArrayList<Moment> moment) {
		this.moments = moment;
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAttend_end_time() {
		return attend_end_time;
	}

	public void setAttend_end_time(String attend_end_time) {
		this.attend_end_time = attend_end_time;
	}

	public int getCheckin() {
		return checkin;
	}

	public void setCheckin(int checkin) {
		this.checkin = checkin;
	}

	public int getRanklist_limit() {
		return ranklist_limit;
	}

	public void setRanklist_limit(int ranklist_limit) {
		this.ranklist_limit = ranklist_limit;
	}

	public int getShow_rank() {
		return show_rank;
	}

	public void setShow_rank(int show_rank) {
		this.show_rank = show_rank;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
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

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public int getPhoto_count() {
		return photo_count;
	}

	public void setPhoto_count(int photo_count) {
		this.photo_count = photo_count;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

}
