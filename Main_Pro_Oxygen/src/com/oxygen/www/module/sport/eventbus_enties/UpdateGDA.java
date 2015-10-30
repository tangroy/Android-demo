package com.oxygen.www.module.sport.eventbus_enties;

/**
 * 从运动记录页面到个人成绩页带过去activiryId
 * @author 杨庆雷
 * 2015-7-16下午4:03:46
 */
public class UpdateGDA {
	private int activityId;
	public UpdateGDA(int activityId){
		this.activityId = activityId;
	}
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	
}
