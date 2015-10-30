package com.oxygen.www.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.GDAcvitity;

public class GDUtil {

	public static SimpleDateFormat dwf = new SimpleDateFormat("MM月dd日 HH:mm");// 设置日期格式

	/**
	 * 去掉字符串开始和结尾的[]
	 * 
	 * @param str
	 *            集合转换成的字符串
	 * @return
	 */
	public static String deleteBrackets(String str) {
		if (str != null && str.length()>2) { // []
			return str.substring(1, str.length() - 1);
		} else {
			return "";
		}
	}

	/**
	 * 判断手机号是否符合
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(177)|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	/**
	 * 判断邮编
	 * 
	 * @param paramString
	 * @return
	 */
	public static boolean isZipNO(String zipString) {
		String str = "^[1-9][0-9]{5}$";
		return Pattern.compile(str).matcher(zipString).matches();
	}

	/**
	 * 判断邮箱是否合法
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (null == email || "".equals(email))
			return false;
		// Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * uincode字符转换
	 * 
	 * @param utfString
	 * @return
	 */
	public static String convert(String utfString) {
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;

		while ((i = utfString.indexOf("\\u", pos)) != -1) {
			sb.append(utfString.substring(pos, i));
			if (i + 5 < utfString.length()) {
				pos = i + 6;
				sb.append((char) Integer.parseInt(
						utfString.substring(i + 2, i + 6), 16));
			}
		}
		sb.append(utfString.substring(pos));
		return sb.toString();
	}

	/**
	 * 根据图片名称返回Drawable 图片
	 * 
	 * @param c
	 * @param photoname
	 *            图片名称
	 * @return
	 */
	public static Drawable engSporttodrawable(Context c, String photoname) {
		Resources res = c.getResources();
		int i = res.getIdentifier(photoname, "drawable", c.getPackageName());
		if (i > 0)
			return c.getResources().getDrawable(i);
		else
			// 无法识别返回其他
			return engSporttodrawable(c, "icon_def");
	}

	/**
	 * 根据图片的名字获取id
	 * 
	 * @param photoname
	 * @return
	 */
	public static int drawableId(String photoname) {
		Class<com.oxygen.www.R.drawable> cls = R.drawable.class;
		try {
			int value = cls.getDeclaredField(photoname).getInt(null);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 根据运动名字获取id
	 * 
	 * @param sportName
	 * @return
	 */
	public static int getColorId(String sportName) {
		Class<com.oxygen.www.R.color> cls = R.color.class;
		try {
			int value = cls.getDeclaredField(sportName).getInt(null);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 根据英文返回string 的中文
	 * 
	 * @param c
	 * @param eng
	 * @return
	 */
	public static String engforchn(Context c, String eng) {
		Resources res = c.getResources();
		int i = res.getIdentifier(eng, "string", c.getPackageName());
		if (i > 0)
			return c.getResources().getString(i);
		else
			// 无法识别返回其他
			return engforchn(c, "app_name");
	}

	/**
	 * 判断运动大类型
	 * 
	 * @param type
	 * @return
	 */
	public static int SportCategory(String type) {
		String[] distancecategory = { "climbing", "cycling", "walking" };
		String[] jucategory = { "tennis", "volleyball", "billiards", "bowling" };
		String[] runningcategory = { "running" };
		String[] plankcategory = { "plank" };
		String[] barcategory = { "pingpong", "badminton" };
		String[] basketballcategory = { "basketball" };
		String[] footballcategory = { "football" };
		String[] swimmingcategory = { "swimming" };
		String[] durationcategory = { "fitness", "yoga", "golf", "skating" };

		for (int i = 0; i < distancecategory.length; i++) {
			if (distancecategory[i].equals(type)) {
				return Constants.COUNT_CATEGORY_DISTANCE;
			}
		}

		for (int i = 0; i < jucategory.length; i++) {
			if (jucategory[i].equals(type)) {
				return Constants.COUNT_CATEGORY_JU;
			}
		}
		for (int i = 0; i < runningcategory.length; i++) {
			if (runningcategory[i].equals(type)) {
				return Constants.COUNT_CATEGORY_RUNNING;
			}
		}
		for (int i = 0; i < plankcategory.length; i++) {
			if (plankcategory[i].equals(type)) {
				return Constants.COUNT_CATEGORY_PLANK;
			}
		}
		for (int i = 0; i < barcategory.length; i++) {
			if (barcategory[i].equals(type)) {
				return Constants.COUNT_CATEGORY_BAR;
			}
		}
		for (int i = 0; i < basketballcategory.length; i++) {
			if (basketballcategory[i].equals(type)) {
				return Constants.COUNT_CATEGORY_BASKETBALL;
			}
		}
		for (int i = 0; i < footballcategory.length; i++) {
			if (footballcategory[i].equals(type)) {
				return Constants.COUNT_CATEGORY_FOOTBALL;
			}
		}
		for (int i = 0; i < swimmingcategory.length; i++) {
			if (swimmingcategory[i].equals(type)) {
				return Constants.COUNT_CATEGORY_SWIMMING;
			}
		}
		for (int i = 0; i < durationcategory.length; i++) {
			if (durationcategory[i].equals(type)) {
				return Constants.COUNT_CATEGORY_DURATION;
			}
		}
		return Constants.COUNT_CATEGORY_OTHER;
	}

	/**
	 * 获取手机imei码
	 * 
	 * @param context
	 * @param imei
	 *            传“”
	 * @return
	 */
	public static String getImei(Context context, String imei) {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();
		} catch (Exception e) {
		}
		return imei;
	}

	// 取得版本号
	public static int GetVersionCode(Context context) {
		try {
			PackageInfo manager = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return manager.versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}
	}

	public static String GetVersionCodename(Context context) {
		try {
			PackageInfo manager = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return manager.versionName;
		} catch (NameNotFoundException e) {
			return "";
		}
	}

	// 校验Tag Alias 只能是数字,英文字母和中文
	public static boolean isValidTagAndAlias(String s) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
		Matcher m = p.matcher(s);
		return m.matches();
	}

	public static boolean isConnected(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	/**
	 * 开始时间与当前时间的比较 开始时间比当前时间晚返回true，反之返回false
	 * 
	 * @param starttime
	 *            开始时间
	 * @return
	 */
	public static boolean CompareTime(Date starttime) {
		Date now = new Date();
		int days = 0;
		try {
			days = daysBetween(starttime, now);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (days > -1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * //判断时间date1是否在时间date2之前 时间格式 2005-4-21 16:16:34
	 * 
	 * @return
	 */

	public static boolean isDateBefore(Date date1) {
		try {
			if (date1.getTime() > new Date().getTime()) {
				return true;
			} else if (date1.getTime() < new Date().getTime()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return true;
	}

	/**
	 * 判断时间格式（多久前）
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeDiff2(Date date) {
		Calendar cal = Calendar.getInstance();
		long diff = 0;
		Date dnow = cal.getTime();
		String str = "";
		diff = dnow.getTime() - date.getTime();

		if (diff > 86400000) { // 24 * 60 * 60 * 1000=86400000 毫秒
			// System.out.println("X天前");
			str = dwf.format(date);
		} else if (diff > 3600000) {// 60 * 60 * 1000=3600000 毫秒
			// System.out.println("X小时前");
			str = (int) Math.floor(diff / 3600000f) + "小时前";
		} else if (diff > 60000) {// 1 * 60 * 1000=60000 毫秒
			// System.out.println("X分钟前");
			str = (int) Math.floor(diff / 60000) + "分钟前";
		} else {
			str = "刚刚";
		}
		return str;
	}

	/**
	 * 格式化时间
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static String formatTime(String startTime, String endTime) {
		String formatTime = startTime.substring(5, 16) + "~"
				+ endTime.substring(5, 16);

		int startYear = Integer.parseInt(startTime.substring(0, 4));
		int startMonth = Integer.parseInt(startTime.substring(5, 7));
		int startDay = Integer.parseInt(startTime.substring(8, 10));

		int endYear = Integer.parseInt(endTime.substring(0, 4));
		int endMonth = Integer.parseInt(endTime.substring(5, 7));
		int endDay = Integer.parseInt(endTime.substring(8, 10));

		if (endYear > startYear) {
			formatTime = startTime.substring(0, 16) + "~"
					+ endTime.substring(0, 16);
		} else if (endMonth > startMonth) {
			formatTime = startTime.substring(5, 16) + "~"
					+ endTime.substring(5, 16);
		} else if (endDay == startDay) {
			formatTime = startTime.substring(5, 16) + "~"
					+ endTime.substring(11, 16);
		}

		return formatTime;
	}

	/**
	 * 计算两个时间的间隔
	 * 
	 * @param date
	 * @return
	 */
	public static int getTimeDiff_send(String starttime, String endtime) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse(starttime);
			Date d2 = df.parse(endtime);
			long diff = d2.getTime() - d1.getTime();
			return (int) (diff / 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;

	}

	/**
	 * 判断时间（多久前）
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeDiff(Date date) {
		Calendar cal = Calendar.getInstance();
		long diff = 0;
		Date dnow = cal.getTime();
		String str = "";
		diff = dnow.getTime() - date.getTime();

		if (diff > 2592000000L) {// 30 * 24 * 60 * 60 * 1000=2592000000 毫秒
			str = "1个月前";
		} else if (diff > 1814400000) {// 21 * 24 * 60 * 60 * 1000=1814400000 毫秒
			str = "3周前";
		} else if (diff > 1209600000) {// 14 * 24 * 60 * 60 * 1000=1209600000 毫秒
			str = "2周前";
		} else if (diff > 604800000) {// 7 * 24 * 60 * 60 * 1000=604800000 毫秒
			str = "1周前";
		} else if (diff > 86400000) { // 24 * 60 * 60 * 1000=86400000 毫秒
			// System.out.println("X天前");
			str = (int) Math.floor(diff / 86400000f) + "天前";
		} else if (diff > 3600000) {// 60 * 60 * 1000=3600000 毫秒
			// System.out.println("X小时前");
			str = (int) Math.floor(diff / 3600000f) + "小时前";
		} else if (diff > 60000) {// 1 * 60 * 1000=60000 毫秒
			// System.out.println("X分钟前");
			str = (int) Math.floor(diff / 60000) + "分钟前";
		} else {
			str = (int) Math.floor(diff / 1000) + "秒前";
		}
		return str;
	}

	/**
	 * 判断时间首页（多久前多久后）
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeDiffforindex(Date date) {
		String[] weekDays = { "日", "一", "二", "三", "四", "五", "六" };
		Calendar cal1 = Calendar.getInstance();
		Date dnow = cal1.getTime();
		String str = "";
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setFirstDayOfWeek(Calendar.MONDAY);
		calendar2.setTime(date);
		int date_week = calendar2.get(Calendar.WEEK_OF_YEAR);
		int week = calendar2.get(Calendar.DAY_OF_WEEK);

		Calendar calendar3 = Calendar.getInstance();
		calendar3.setTime(dnow);
		int dnow_week = calendar3.get(Calendar.WEEK_OF_YEAR);
		int days = 0;
		try {
			days = daysBetween(date, dnow);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (days == 0) {
			str = "今天";
		} else if (days == -1) {
			str = "明天";
		} else if (days == -2) {
			str = "后天";
		} else if (days < -2 & days > -8) {
			if (date_week == dnow_week) {
				str = "本周" + weekDays[week - 1];
			} else {
				str = -days + "天后";
			}
		} else {
			str = (calendar2.get(Calendar.MONTH) + 1) + "月"
					+ calendar2.get(Calendar.DAY_OF_MONTH) + "日";
		}

		return str;
	}

	/**
	 * 判断时间首页（多久前多久后）
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeDiffforindexforchallenges(Date starttime,
			Date endtime) {
		Calendar cal1 = Calendar.getInstance();
		Date dnow = cal1.getTime();
		String str = "";
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setFirstDayOfWeek(Calendar.MONDAY);
		calendar2.setTime(starttime);

		Calendar calendar3 = Calendar.getInstance();
		calendar3.setTime(dnow);

		Calendar calendar4 = Calendar.getInstance();
		calendar4.setTime(endtime);
		int days = 0;
		try {
			days = daysBetween(dnow, endtime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 正在进行
		if (days > 0 && days < 7) {
			str = "今天";
		}
		// 已结束，显示结束时间
		else if (days < 0) {
			str = getTimeDiffforindex(endtime);
			// 未开始
		} else if (days > 6) {
			str = getTimeDiffforindex(starttime);
		}

		return str;
	}

	/**
	 * 判断时间首页（多久前多久后）
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeDiffforindexforchallengesday(Date starttime,
			Date endtime) {
		Calendar cal1 = Calendar.getInstance();
		Date dnow = cal1.getTime();
		String str = "";
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setFirstDayOfWeek(Calendar.MONDAY);
		calendar2.setTime(starttime);

		Calendar calendar3 = Calendar.getInstance();
		calendar3.setTime(dnow);

		Calendar calendar4 = Calendar.getInstance();
		calendar4.setTime(endtime);
		int days = 0;
		try {
			days = daysBetween(dnow, endtime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 正在进行
		if (days > 0 && days < 7) {
			str = "还剩" + (days) + "天";
		}
		// 已结束，显示结束时间
		else if (days < 0) {
			str = calendar4.get(Calendar.DAY_OF_MONTH) + "／"
					+ (calendar4.get(Calendar.MONDAY) + 1) + "月";
			// 未开始
		} else if (days > 6) {
			str = "未开始";
		}

		return str;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 秒转换成00：00：00格式时间
	 * 
	 * @param duration
	 *            秒
	 * @return
	 */
	public static String TransitionTime(int duration) {
		if (duration > 0) {
			String time = (((duration / 3600) < 10 ? "0" + (duration / 3600)
					: (duration / 3600))
					+ ":"
					+ (duration % 3600 / 60 < 10 ? "0" + (duration % 3600 / 60)
							: (duration % 3600 / 60)) + ":" + (duration % 60 < 10 ? "0"
					+ (duration % 60)
					: (duration % 60)));
			return time;
		}
		return "00:00:00";
	}

	/**
	 * 分钟/公里 转化为----分钟‘秒’’/公里
	 * 
	 * @param speed
	 * @return
	 */
	public static String speed2speed(Double speed) {
		speed = 50 / 3 / speed;
		int speedMinute = (int) Math.floor(speed);
		int speedSecond = (int) Math.floor((speed - speedMinute) * 60);
		return speedMinute + "'" + speedSecond + "''";
	}

	/**
	 * 判断定位是否可用
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean isGpsEnable(Context mContext) {
		LocationManager locationManager = ((LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE));
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * 返回日期（格式为 XX月XX日）
	 * 
	 * @param time
	 * @return
	 */
	public static String getDate(String time) {
		String month = time.substring(5, 7);
		String day = time.substring(8, 10);
		if (month.startsWith("0")) {
			month = month.substring(1);
		}
		if (day.startsWith("0")) {
			day = day.substring(1);
		}
		String date = month + "月" + day + "日";
		return date;
	}

	/**
	 * bitmap转字节
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 打开apk文件
	 * 
	 * @param file_path
	 *            文件目录
	 * @param c
	 */
	public static void installApk(String file_path, Context c) {
		Uri uri = Uri.fromFile(new File(file_path));
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		c.startActivity(intent);
	}

	// 创建JSONObject对象
	public static String activitytojson(GDAcvitity avt) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("type", avt.getType());
			jsonObject.put("sport", avt.getsport_eng());
			jsonObject.put("distance", avt.getDistance());
			jsonObject.put("duration", avt.getDuration());
			jsonObject.put("latitude", avt.getlatitude());
			jsonObject.put("longitude", avt.getlongitude());
			jsonObject.put("address", avt.getaddresss());
			jsonObject.put("start_time", avt.getStart_time());
			jsonObject.put("end_time", avt.getEnd_time());
			jsonObject.put("route", avt.getRoute());
			jsonObject.put("created_at", avt.getCreated_at());
			jsonObject.put("created_by", avt.getCreated_by());
			jsonObject.put("tilte", avt.getTilte());
			jsonObject.put("status", avt.getStatus());
			jsonObject.put("event_id", avt.getEvent_id());
			jsonObject.put("intro", avt.getIntro());
			jsonObject.put("pace_min", avt.getPace_min());
			jsonObject.put("pace_max", avt.getPace_max());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	/**
	 * 解决Android 5.0中出现的error：Service Intent must be explicit
	 * 
	 * @param context
	 * @param implicitIntent
	 * @return
	 */
	public static Intent getExplicitIntent(Context context,
			Intent implicitIntent) {
		// Retrieve all services that can match the given intent
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent,
				0);
		// Make sure only one match was found
		if (resolveInfo == null || resolveInfo.size() != 1) {
			return null;
		}
		// Get component info and create ComponentName
		ResolveInfo serviceInfo = resolveInfo.get(0);
		String packageName = serviceInfo.serviceInfo.packageName;
		String className = serviceInfo.serviceInfo.name;
		ComponentName component = new ComponentName(packageName, className);
		// Create a new intent. Use the old one for extras and such reuse
		Intent explicitIntent = new Intent(implicitIntent);
		// Set the component to be explicit
		explicitIntent.setComponent(component);
		return explicitIntent;
	}

	/**
	 * 计算配速
	 * 
	 * @param distance
	 *            距离（米）
	 * @param duration
	 *            时间（秒）
	 * @return 分钟/公里 00'00''
	 */
	public static String CalculationPace(double distance, int duration) {
		int pace_time = (int) (duration / (distance / 1000));
		int min = pace_time / 60;
		int seconds = pace_time % 60;
		return (min > 9 ? (min + "") : ("0" + min)) + "'"
				+ (seconds > 9 ? (seconds + "") : ("0" + seconds)) + "''";
	}

	/**
	 * pace1>pace2 返回true 例如 03'04''>04'00''
	 * 
	 * @param pace1
	 * @param pace2
	 * @return
	 */
	public static Boolean Comparisonpace(String pace1, String pace2) {
		int pace_min_pace1 = Integer.parseInt(pace1.substring(0, 2));
		int pace_min_pace2 = Integer.parseInt(pace2.substring(0, 2));
		int pace_seconds_pace1 = Integer.parseInt(pace1.substring(3, 5));
		int pace_seconds_pace2 = Integer.parseInt(pace2.substring(3, 5));
		if (pace_min_pace1 > pace_min_pace2) {
			return false;
		} else if (pace_min_pace1 == pace_min_pace2) {
			if (pace_seconds_pace1 > pace_seconds_pace2) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	/**
	 * 替代全局变量(获取值)
	 * @param c
	 * @param key 变量名称
	 * @return
	 */
	public static Boolean getGlobal(Context c,String key) {
		SharedPreferences sp = c.getSharedPreferences(Constants.USER_INFO,
				Context.MODE_MULTI_PROCESS);
		return sp.getBoolean(key, false);
	}

	/**
	 * 替代全局变量(保存值)
	 * @param c
	 * @param key 变量名称
	 * @param values 变量值
	 */
	public static void setGlobal(Context c,String key,boolean values) {
		SharedPreferences sp = c.getSharedPreferences(Constants.USER_INFO,
				Context.MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, values);
		SharedPreferencesCompat.apply(editor);
	}
}
