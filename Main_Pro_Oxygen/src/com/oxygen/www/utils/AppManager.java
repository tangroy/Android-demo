package com.oxygen.www.utils;


import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

/**
 * Activity 管理类
 * @author TANGROY
 *
 */
public class AppManager {

	public static Stack<Activity> activityStack;
	private static AppManager instance;

	public AppManager() {
	}

	public static AppManager getInstance() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加activity到堆栈
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 * 
	 * @return
	 */
	public Activity getCurrentActivity() {
		return activityStack.lastElement();
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		finishActivity(activityStack.lastElement());
	}

	/**
	 * 结束指定的Activity
	 * 
	 * @param activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	@SuppressWarnings("deprecation")
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
				activityMgr.killBackgroundProcesses(context.getPackageName());
			} else {
				activityMgr.restartPackage(context.getPackageName());
			}
			killAppliction(context);
			System.exit(0);
		} catch (Exception e) {
		}
	}

	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		int j = serviceList.size();
		for (int i = 0; i < j; i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	/**
	 * desc 退出当前进程，但并没有停止服务，需要先停止服务，否则进程会重启
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	protected void killAppliction(Context context) {
		if (Build.VERSION.SDK_INT > 7) {
			System.exit(0);
		} else {
			ActivityManager am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			am.restartPackage(context.getPackageName());
		}
	}
}
