package com.oxygen.www.enties;

public class Businesses {

	/**
	 * 场馆ID
	 */
	public int id;
	/**
	 * 场馆名称
	 */
	public String name;
	/**
	 * 场馆电话
	 */
	public String telephone;
	/**
	 * 场馆地址
	 */
	public String address;
	/**
	 * 
	 * 场馆图片
	 */
	public String s_photo_url;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getS_photo_url() {
		return s_photo_url;
	}

	public void setS_photo_url(String s_photo_url) {
		this.s_photo_url = s_photo_url;
	}

}
