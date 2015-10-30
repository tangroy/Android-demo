package com.oxygen.www.enties;

import java.io.Serializable;

public class Route implements Serializable {
	/**
	 * 经度
	 */
	public double start_longitude = 0.0;
	/**
	 * 纬度
	 */
	public double start_latitude;
	/**
	 * 定位时间
	 */
	public long location_time;

//	public LatLng lnglat;

//	public LatLng getLnglat() {
//		return lnglat;
//	}
//
//	public void setLnglat(LatLng lnglat) {
//		this.lnglat = lnglat;
//	}

	@Override
	public String toString() {
		return "[" + start_longitude + "," + start_latitude + ","
				+ location_time + "]";
	}

	public double getStart_longitude() {
		return start_longitude;
	}

	public void setStart_longitude(double start_longitude) {
		this.start_longitude = start_longitude;
	}

	public double getStart_latitude() {
		return start_latitude;
	}

	public void setStart_latitude(double start_latitude) {
		this.start_latitude = start_latitude;
	}

	public long getLocation_time() {
		return location_time;
	}

	public void setLocation_time(long location_time) {
		this.location_time = location_time;
	}

}
