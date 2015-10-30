package com.oxygen.www.module.sport.eventbus_enties;

/**
 * 微信支付
 * 
 * @author 张坤
 */
public class WeChatPay {
	
	public int status;
	
	public WeChatPay(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
