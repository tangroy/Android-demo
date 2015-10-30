package com.oxygen.www.base;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import cn.jpush.android.api.JPushInterface;

import com.amap.api.location.AMapLocation;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.oxygen.www.module.sport.writemoment.util.Res;
import com.oxygen.www.utils.UserInfoUtils;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

public class OxygenApplication extends Application implements
		Thread.UncaughtExceptionHandler {
	public static RequestQueue requestQueue;

	public static String OAUTH_TOKEN;
	public static String OAUTH_TOKEN_SECRET;
	public static String OAUTH_SIGNATURE;
	public static IWXAPI api;
	public static QQAuth qqAuth;
	public static Tencent tencent;
	public static AMapLocation location;
	public static String weixinsdk = "";
	public static int Eventid = 0;
	// public static ExecutorService executorService;
	// private static final String APP_ID = "222222";
	private static final String APP_ID = "1104195866";
	private static final String APP_KEY = "PbWewEUZPghHVlE9";
	/**
	 * 最多选择上传图片
	 */
	public static int num = 9;
	/**
	 * 是否检测更新，启动一次只检测一次
	 */
	public static boolean detection_update = true;

	public static Context context;
	public static double ppi;
	
	/**
	 * 团队是否有数据更新
	 */
//	public static boolean groups_is_rerfresh = false;
	/**
	 * 加载本地缓存数据
	 */
//	public static boolean update_loca_data = false;

	private File cacheDir;
	public static ExecutorService cachedThreadPool;

	@Override
	public void onCreate() {
		OxygenApplication.context = getApplicationContext();
		//afcc79d6b9946e3bf8618f90b348272d0560222d7
		OAUTH_TOKEN = (String) UserInfoUtils.getUserInfo(context, Constants.OAUTH_TOKEN, "");
		OAUTH_TOKEN_SECRET = (String) UserInfoUtils.getUserInfo(context, Constants.OAUTH_TOKEN_SECRET, "");
		OAUTH_SIGNATURE = (String) UserInfoUtils.getUserInfo(context, Constants.OAUTH_SIGNATURE, "");
		// 注册volley
		requestQueue = Volley.newRequestQueue(this);
		// 注册微信
		api = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APPID, false);
		api.registerApp(Constants.WEIXIN_APPID);
		// 注册QQ
		qqAuth = QQAuth.createInstance(APP_ID, this.getApplicationContext());
		tencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
		// 注册极光推送
		JPushInterface.setDebugMode(true);// 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);
		Res.init(this);
		super.onCreate();
		cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(),
				"imageloader/Cache");
		initImageLoader(getApplicationContext());

		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		ppi = displayMetrics.densityDpi / 160;
		Thread.setDefaultUncaughtExceptionHandler(this);
		cachedThreadPool = Executors.newCachedThreadPool();
		//在这里初始化Bugtags
//		BugtagsOptions options = new BugtagsOptions.Builder().
//		        trackingLocation(true).//是否获取位置
//		        trackingCrashLog(true).//是否收集crash
//		        trackingConsoleLog(true).//是否收集console log
//		        trackingUserSteps(true).//是否收集用户操作步骤
//		        build();
//        Bugtags.start("7f90390e26b3a84be3199d353f99a6a9", this, Bugtags.BTGInvocationEventBubble,options);
	}

	public void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCacheExtraOptions(480, 800)
				// default = device screen dimensions
				.discCache(new UnlimitedDiskCache(cacheDir))
				// 自定义缓存路径
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}

	public static Context getAppContext() {
		return context;
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		System.exit(0);

	}

}
