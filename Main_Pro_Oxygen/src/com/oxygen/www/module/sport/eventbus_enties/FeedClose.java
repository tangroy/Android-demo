package com.oxygen.www.module.sport.eventbus_enties;

/**
 * 微信支付
 * 
 * @author 张坤
 */
public class FeedClose {
	
	public int status;
	
	public FeedClose(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
