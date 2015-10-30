package com.oxygen.www.base;

public class Constants {

	// O2分子环境
// 	public static String CONSUMER_KTY = "9a4f1d8685e8edadc5a86ab63b0f2574054b9ca60";
// 	public static String CONSUMER_SECRET = "378e0f58fffa40e3b5d9b6be76cdb136";
// 	public static String WEIXIN_APPID = "wx70a07ee2ebde556b";
// 	public static String WEIXIN_SECRET = "b39c1a97119b62593165aaadd23eaa71";
	
//	乐运动环境
	public static String CONSUMER_KTY = "30a2cbcc09b3a5b38bb434891e714221054f125bb";
	public static String CONSUMER_SECRET = "4b5abc06a0344fbdc354512676dd806d";
	public static String WEIXIN_APPID = "wxf83e7a42376703c7";
	public static String WEIXIN_SECRET = "258c35002588e3f6d325fa7bf4880840";

	public static String RECEVIER_RUN_UPDATELINK = "com.oxygen.www.run.updatelink";
	public static String RECEVIER_RUN_CO = "com.oxygen.www.run.co";
	public static String SERVICE_RUN_UPDATELINK = "com.oxygen.www.module.sport.service.runService";
	public static String MTA_APP_KEY = "AN943DWKBZ8L";

	public static String qiuniushare = "http://7xi7ub.com2.z0.glb.qiniucdn.com/share/";
	/**
	 * 七牛图片剪切-头像
	 */
	public static String qiniu_photo_head = "?imageMogr2/thumbnail/120x120";

	public static String qiniu_photo_find = "?imageMogr2/thumbnail/400x300";
	
	public static String qiniu_photo_feed = "?imageMogr2/thumbnail/1000x";
	
	public static String qiniu_photo_group = "?imageMogr2/thumbnail/1600x1200";

	public static String qiniu_photo_business = "?imageMogr2/thumbnail/200x200";

	public static int XIAOLE_ID = 1;
	/**
	 * 停止定位
	 */
	public static final int RUN_SERVICE_CO_STOP = 1;
	/**
	 * 继续定位
	 */
	public static final int RUN_SERVICE_CO_PLAY = 2;

	/**
	 * 摧毁服务
	 */
	public static final int RUN_SERVICE_CO_KILL = 3;

	/**
	 * 距离类
	 */
	public static int COUNT_CATEGORY_DISTANCE = 4;

	/**
	 * 其他类
	 */
	public static int COUNT_CATEGORY_OTHER = 6;
	/**
	 * 局数类
	 */
	public static int COUNT_CATEGORY_JU = 7;
	/**
	 * 跑步类
	 */
	public static int COUNT_CATEGORY_RUNNING = 8;
	/**
	 * 平板支撑类
	 */
	public static int COUNT_CATEGORY_PLANK = 9;
	/**
	 * 小局分类
	 */
	public static int COUNT_CATEGORY_BAR = 10;
	/**
	 * 篮球类
	 */
	public static int COUNT_CATEGORY_BASKETBALL = 11;
	/**
	 * 游泳类
	 */
	public final static int COUNT_CATEGORY_SWIMMING = 12;
	/**
	 * 足球类
	 */
	public static int COUNT_CATEGORY_FOOTBALL = 13;
	/**
	 * 时长类
	 */
	public static int COUNT_CATEGORY_DURATION = 14;

	/**
	 * 编辑卡页修改activity
	 */
	public static String INVITE_UPDATE = "updateactivity";
	/**
	 * 编辑邀请卡
	 * 
	 */
	public static String INVITE_CARD = "invitecard";

	public static String SPORTTYPE_ACTIVITY = "activity";
	public static String SPORTTYPE_EVENT = "event";
	/**
	 * 创建类型
	 */
	public static String SPORTTYPE_CREATED = "created";
	/**
	 * 修改类型
	 */
	public static String SPORTTYPE_UPDATE = "update";

	/**
	 * 其他类型
	 */
	public static String SPORTTYPE_OTHER = "other";

	/**
	 * 用户信息
	 */
	public static String USER_INFO = "user_info";
	/**
	 * 用户OAUTH_TOKEN
	 */
	public static String OAUTH_TOKEN = "oauth_token";
	/**
	 * 用户OAUTH_TOKEN
	 */
	public static String OAUTH_TOKEN_SECRET = "oauth_token_secret";
	/**
	 * 用户OAUTH_SIGNATURE
	 */
	public static String OAUTH_SIGNATURE = "oauth_signature";
	/**
	 * 用户TOKEN
	 */
	public static String TOKEN = "token";
	/**
	 * 用户UNIONID
	 */
	public static String UNIONID = "unionid";
	/**
	 * 用户OPENID
	 */
	public static String OPENID = "openid";
	/**
	 * 用户QQ_OPENID
	 */
	public static String QQ_OPENID = "qq_openid";
	/**
	 * 用户等级
	 */
	public static String LEVEL = "level";
	/**
	 * 用户金币
	 */
	public static String COINS = "coins";
	/**
	 * 用户积分
	 */
	public static String POINTS = "points";
	/**
	 * 用户账户余额
	 */
	public static String ACCOUNT_BALANCE = "account_balance";
	/**
	 * 用户绑定的手机号码
	 */
	public static String BIND_NUMBER = "bind_number";
	/**
	 * 用于绑定的登陆
	 */
	public static String LOGIN_TO_BIND = "login_to_bind";
	/**
	 * 用户用手机号码登陆
	 */
	public static String LOGIN_BY_LYD = "login_by_lyd";
	/**
	 * 用户MOBILE
	 */
	public static String MOBILE = "mobile";
	/**
	 * 用户ID
	 */
	public static String USERID = "userid";
	/**
	 * 昵称
	 */
	public static String NICKNAME = "nickname";
	/**
	 * 性别
	 */
	public static String SEX = "sex";
	/**
	 * 年龄
	 */
	public static String AGE = "age";
	/**
	 * 身高
	 */
	public static String HEIGHT = "height";
	/**
	 * 体重
	 */
	public static String WEIGHT = "weight";
	/**
	 * 兴趣
	 */
	public static String HOBBY = "hobby";
	/**
	 * 个人签名
	 */
	public static String SIGN = "sign";
	/**
	 * 用户头像地址
	 */
	public static String HEADIMG_URL = "headimg_url";
	/**
	 * 用户ShopId
	 */
	public static String SHOP_ID = "shop_id";
	/**
	 * 用户AccountId
	 */
	public static String ACCOUNT_ID = "account_id";
	/**
	 * 用户选择的运动所在的id
	 */
	public static String SPORT_SELECTED = "sport_select";
	/**
	 * 用户选择的运动所在的名字
	 */
	public static String SPORT_SELECTED_NAME = "sport_select_name";
	/**
	 * 我的收藏的url
	 */
	public static String MY_COLLECT = "my_collect";
	/**
	 * 首次安装
	 */
	public static String FIRST_INSTALL = "first_install";
	/**
	 * 首次点击加号
	 */
	public static String FIRST_EVENT = "first_event";
	/**
	 * 首次创建运动
	 */
	public static String FIRST_CREATE_EVENT = "first_create_event";
	/**
	 * 保存密码
	 */
	public static String SET_PASSWORD = "set_password";
	/**
	 * 保存评论id
	 */
	public static String SET_COMENT_ID = "set_coment_id";

	public static int INVETE_GROUP = 1001;
	/**
	 * 用户默认的运动类型
	 */
	public static String DEFT_SPORT_TYPE = "deft_sport_type";

	public static String USER_IMAGEURL = "user_imageurl";
	public static String QR_CHALLENGES = "qr_hallenges";
	public static String QR_EVENT = "qr_event";

	public static String CHALLENGES_TYPE_PERSON = "challengs_person";
	public static String CHALLENGES_TYPE_TWOTEAM = "challengs_twoteam";
	public static String CHALLENGES_TYPE_MORETEAM = "challengs_moreteam";

	public static String sports[] = { "running", "cycling", "walking",
			"climbing", "pingpong", "badminton", "tennis", "basketball",
			"football", "volleyball", "plank", "swimming", "fitness", "yoga",
			"billiards", "bowling", "golf", "skating", "party", "other" };
	public static String[] ranking = { "distance", "speed", "pace", "calorie",
			"match", "score", "duration" };
	public static String[] ranking_chn = { "距离", "速度", "配速", "卡路里", "比分", "得分",
			"时长" };
}
