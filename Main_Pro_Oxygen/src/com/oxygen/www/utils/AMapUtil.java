/**
 * 
 */
package com.oxygen.www.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolygonOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.oxygen.www.R;
import com.oxygen.www.enties.Route;

public class AMapUtil {
	/**
	 * 判断edittext是否null
	 */
	public static String checkEditText(EditText editText) {
		if (editText != null && editText.getText() != null
				&& !(editText.getText().toString().trim().equals(""))) {
			return editText.getText().toString().trim();
		} else {
			return "";
		}
	}

	public static Spanned stringToSpan(String src) {
		return src == null ? null : Html.fromHtml(src.replace("\n", "<br />"));
	}

	public static String colorFont(String src, String color) {
		StringBuffer strBuf = new StringBuffer();

		strBuf.append("<font color=").append(color).append(">").append(src)
				.append("</font>");
		return strBuf.toString();
	}

	public static String makeHtmlNewLine() {
		return "<br />";
	}

	public static String makeHtmlSpace(int number) {
		final String space = "&nbsp;";
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < number; i++) {
			result.append(space);
		}
		return result.toString();
	}

	public static boolean IsEmptyOrNullString(String s) {
		return (s == null) || (s.trim().length() == 0);
	}

	/**
	 * long类型时间格式化
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}

	/**
	 * long类型时间戳
	 */
	@SuppressLint("SimpleDateFormat")
	public static long convertToTimestamp(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		Date datestamp = null;
		try {
			datestamp = df.parse(df.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (datestamp.getTime() / 1000);
	}

	/**
	 * 绘画线路
	 * 
	 * @param location
	 */

	public static void drawline(ArrayList<Route> routes, AMap aMap) {
		double frist_lat = 0;
		double frist_lng = 0;
		double two_lat;
		double two_lng;
		double max_lat = 0;
		double max_lng = 0;
		double min_lat = 0;
		double min_lng = 0;
		long frist_time = 0;
		long two_time = 0;
		MarkerOptions markerOption = new MarkerOptions();
		int newcount = routes.size();
		ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
		int color = Color.GREEN;
		for (int i = 0; i < newcount; i++) {
			Route route = routes.get(i);
			if (i == 0) {
				frist_lat = route.start_latitude;
				frist_lng = route.start_longitude;
				frist_time = route.getLocation_time();

				max_lat = route.start_latitude;
				max_lng = route.start_longitude;
				min_lat = route.start_latitude;
				min_lng = route.start_longitude;
				// 画起点
				markerOption.position(new LatLng(frist_lng, frist_lat));
				markerOption.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.mylocayion));
				aMap.addMarker(markerOption);
				aMap.addPolygon(new PolygonOptions()
				.addAll(createRectangle(new LatLng(frist_lng, frist_lat), 1, 1))
				.fillColor(Color.argb(100, 0, 0, 0))
				.strokeColor(Color.LTGRAY).strokeWidth(1));
			}

			two_lat = route.start_latitude;
			two_lng = route.start_longitude;
			two_time = route.getLocation_time();
			LatLng marker = new LatLng(frist_lng, frist_lat);
			LatLng old_marker = new LatLng(two_lng, two_lat);
			double distance = AMapUtils.calculateLineDistance(old_marker,
					marker);
			int time = (int) ((two_time - frist_time));
			double speed = distance / time;
			if (distance > 0.0) {
				int red = 0;
				int green = 0;
				int blue = 0;
				if (speed < 0.5) {
					red = 255;
					green = 40;
					blue = 0;
				} else if (speed < 1.0) {
					red = 255;
					green = 80;
					blue = 0;
				} else if (speed < 1.0) {
					red = 255;
					green = 120;
					blue = 0;
				} else if (speed < 1.0) {
					red = 255;
					green = 160;
					blue = 0;
				} else if (speed < 1.0) {
					red = 255;
					green = 200;
					blue = 0;
				} else if (speed < 1.0) {
					red = 255;
					green = 240;
					blue = 0;
				}

				else if (speed < 1.0) {
					red = 255;
					green = 255;
					blue = 0;
				} else if (speed < 1.5) {
					red = 240;
					green = 200;
					blue = 0;
				} else if (speed < 2.0) {
					red = 200;
					green = 255;
					blue = 0;
				} else if (speed < 2.5) {
					red = 160;
					green = 255;
					blue = 0;
				} else if (speed < 3.5) {
					red = 120;
					green = 255;
					blue = 0;
				} else if (speed < 4.0) {
					red = 80;
					green = 255;
					blue = 0;
				} else {
					red = 40;
					green = 255;
					blue = 0;
				}
				color = Color.argb(255, red, green, blue);

			}
			if (max_lat < two_lat) {
				max_lat = two_lat;
			}
			if (max_lng < two_lng) {
				max_lng = two_lng;
			}
			if (min_lat > two_lat) {
				min_lat = two_lat;
			}
			if (min_lng > two_lng) {
				min_lng = two_lng;
			}
			Polyline polyline = aMap.addPolyline((new PolylineOptions()).add(
					new LatLng(frist_lng, frist_lat),
					new LatLng(two_lng, two_lat)).color(color));
			if (polyline != null) {
				polyline.setWidth(8);
				polyline.setVisible(true);
				polyline.setZIndex(1);
				polyline.setGeodesic(true);
				 
			}
			latLngs.add(new LatLng(frist_lng, frist_lat));
			frist_lat = two_lat;
			frist_lng = two_lng;
			frist_time = two_time;
			// 画终点
			if (i == newcount - 1) {
				markerOption.position(new LatLng(two_lng, two_lat));
				markerOption.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_runover));
				aMap.addMarker(markerOption);
			}
		}
//		// 画线
//		 Polyline polyline = aMap.addPolyline((new PolylineOptions()).addAll(
//		 latLngs).color(Color.GREEN));
//		 polyline.setWidth(13);
		// 设置可视区域边界
		double center_lat = (max_lat + min_lat) / 2;
		double center_lng = (max_lng + min_lng) / 2;
		LatLng marker = new LatLng(center_lng, center_lat);
		LatLngBounds bounds = new LatLngBounds.Builder()
				.include(new LatLng(frist_lng, frist_lat)).include(marker)
				.include(new LatLng(min_lng, min_lat))
				.include(new LatLng(max_lng, max_lat)).build();
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));


	}
	/**
	 * 生成一个长方形的四个坐标点
	 */
	public static List<LatLng> createRectangle(LatLng center, double halfWidth,
			double halfHeight) {
		return Arrays.asList(new LatLng(center.latitude - halfHeight,
				center.longitude - halfWidth), new LatLng(center.latitude
				- halfHeight, center.longitude + halfWidth), new LatLng(
				center.latitude + halfHeight, center.longitude + halfWidth),
				new LatLng(center.latitude + halfHeight, center.longitude
						- halfWidth));
	}
	/**
	 * 速度排序
	 * 
	 * @param routes
	 * @return
	 */
	public static List<Double> getsmoothSpeeds(ArrayList<Route> routes) {
		double frist_lat = 0;
		double frist_lng = 0;
		double two_lat;
		double two_lng;
		long frist_time = 0;
		long two_time = 0;
		List<Double> smoothSpeeds = new ArrayList<Double>();
		double slowestSpeed = 99999;
		double fastestSpeed = 0.0;
		for (int i = 0; i < routes.size(); i++) {
			Route route = routes.get(i);
			if (i == 0) {
				frist_lat = route.start_latitude;
				frist_lng = route.start_longitude;
				frist_time = route.getLocation_time();
			}
			two_lat = route.start_latitude;
			two_lng = route.start_longitude;
			two_time = route.getLocation_time();

			LatLng marker = new LatLng(frist_lng, frist_lat);
			LatLng old_marker = new LatLng(two_lng, two_lat);
			double distance = AMapUtils.calculateLineDistance(old_marker,
					marker);
			int time = (int) ((two_time - frist_time));
			double speed = distance / time;
			slowestSpeed = speed < slowestSpeed ? speed : slowestSpeed;
			fastestSpeed = speed > fastestSpeed ? speed : fastestSpeed;
			if (speed > 0.0 && speed < 999) {
				smoothSpeeds.add(new Double(speed));
			} else {
				smoothSpeeds.add(new Double(0.0));
			}
			frist_lat = two_lat;
			frist_lng = two_lng;
			frist_time = two_time;

		}
		return smoothSpeeds;
	}

	public static final String HtmlBlack = "#000000";
	public static final String HtmlGray = "#808080";
}
