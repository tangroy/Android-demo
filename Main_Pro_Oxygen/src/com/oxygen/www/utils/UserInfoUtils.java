package com.oxygen.www.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.User;

/**
 * 获取已登录的用户信息的工具类
 * 
 * @author kunyuan
 */
public class UserInfoUtils {

//	private static SharedPreferences sp;

	/**
	 * 清除用户的登录信息
	 * 
	 * @param context
	 */
	public static void clearUserInfo(Context context) {
//		if (sp == null) {
			SharedPreferences sp = context.getSharedPreferences(Constants.USER_INFO,
					Context.MODE_MULTI_PROCESS);
//		}
		sp.edit().clear().commit();
		setUserInfo(context, Constants.FIRST_INSTALL, false);
	}

	/**
	 * 保存用户信息到sp
	 */
	public static void saveUserInfoToSp(Context context, User user) {
		setUserInfo(context.getApplicationContext(), Constants.AGE,user.age + "");
		setUserInfo(context.getApplicationContext(), Constants.HEIGHT, user.height + "");
		setUserInfo(context.getApplicationContext(), Constants.WEIGHT,user.weight + "");
		setUserInfo(context.getApplicationContext(), Constants.SPORT_SELECTED, user.sports);
		setUserInfo(context.getApplicationContext(), Constants.NICKNAME, user.nickname);
		setUserInfo(context.getApplicationContext(), Constants.SEX,user.sex + "");
		setUserInfo(context.getApplicationContext(), Constants.SIGN,user.intro + "");
		setUserInfo(context.getApplicationContext(), Constants.HEADIMG_URL, user.headimgurl);
		setUserInfo(context.getApplicationContext(), Constants.USERID,user.id + "");
		setUserInfo(context.getApplicationContext(), Constants.MOBILE,user.mobile);
		setUserInfo(context.getApplicationContext(), Constants.TOKEN, user.token);
	}

	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 * 
	 * @param context
	 * @param key
	 * @param object
	 */
	public static void setUserInfo(Context context, String key, Object object) {

		if (object != null) {
				SharedPreferences sp = context.getSharedPreferences(Constants.USER_INFO,
						Context.MODE_MULTI_PROCESS);
			SharedPreferences.Editor editor = sp.edit();

			if (object instanceof String) {
				editor.putString(key, (String) object);
			} else if (object instanceof Integer) {
				editor.putInt(key, (Integer) object);
			} else if (object instanceof Boolean) {
				editor.putBoolean(key, (Boolean) object);
			} else if (object instanceof Float) {
				editor.putFloat(key, (Float) object);
			} else if (object instanceof Long) {
				editor.putLong(key, (Long) object);
			} else {
				editor.putString(key, object.toString());
			}

			SharedPreferencesCompat.apply(editor);
		}
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * 
	 * @param context
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public static Object getUserInfo(Context context, String key,
			Object defaultObject) {
//		if (sp == null) {
			SharedPreferences sp = context.getSharedPreferences(Constants.USER_INFO,
					Context.MODE_MULTI_PROCESS);
//		}
		if (defaultObject instanceof String) {
			return sp.getString(key, (String) defaultObject);
		} else if (defaultObject instanceof Integer) {
			return sp.getInt(key, (Integer) defaultObject);
		} else if (defaultObject instanceof Boolean) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if (defaultObject instanceof Float) {
			return sp.getFloat(key, (Float) defaultObject);
		} else if (defaultObject instanceof Long) {
			return sp.getLong(key, (Long) defaultObject);
		}

		return null;
	}

	/**
	 * 移除某个key值已经对应的值
	 * 
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key) {
//		if (sp == null) {
			SharedPreferences sp = context.getSharedPreferences(Constants.USER_INFO,
					Context.MODE_MULTI_PROCESS);
//		}
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	
}
