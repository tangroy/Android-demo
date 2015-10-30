package com.oxygen.www.module.user.eventbus_entities;

public class BindQQOrWX {
	private String bindType;
	private String unionid;

	public BindQQOrWX(String bindType,String unionid) {
		this.bindType = bindType;
		this.unionid = unionid;
	}

	public String getBindType() {
		return bindType;
	}

	public void setBindType(String bindType) {
		this.bindType = bindType;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	
}
