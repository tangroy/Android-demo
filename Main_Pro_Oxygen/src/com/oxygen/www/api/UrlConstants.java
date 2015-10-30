package com.oxygen.www.api;

public class UrlConstants {

	/**
	 * api前缀 测试环境
	 */
 
//	 public static String API_PREFIX = "http://o2fenzi.com";
	  public static String API_PREFIX = "http://api.leyundong.com"; 
 
	/**
	 * 获取时间轴数据
	 */
	public final static String EVENTS_LIST_URL_GET = "/sports/timeline.json";

	/**
	 * 删除一个ACTIVITIES数据
	 */
	public final static String ACTIVITIES_DEL_URL_GET = "/activities/destroy/";
	/**
	 * 创建一个ACTIVITIES数据
	 */
	public final static String ACTIVITIES_CREATE_URL_POST = "/activities/create.json";
	/**
	 * 同步可穿戴设备的数据
	 */
	public final static String SYN_WEARABLE_DATA = "/activities/sync_wearable_data.json";

	/**
	 * 创建活动
	 */
	public final static String EVENTS_CREATE_URL_POST = "/events/create.json";
	/**
	 * 报名者填写项
	 */
	public final static String EVENTS_ENTRY_FORM = "/events/entry_form/";

	/**
	 * 删除EVENT数据
	 */
	public final static String EVENT_DESTROY_URL_POST = "/events/destroy/";
	/**
	 * 更新一个ACTIVITY数据
	 */
	public final static String ACTIVITIES_UPDATE_URL_POST = "/activities/update/";
	/**
	 * 获取最佳成绩
	 */
	public final static String ACTIVITIES_BPP_URL_POST = "/activities/bpp/";

	/**
	 * 更新一个EVENTSY数据
	 */
	public final static String EVENTS_UPDATE_URL_POST = "/events/update/";

	public final static String EVENTS_NOTIFY = "/events/notify/";

	/**
	 * 获取activities数据
	 */
	public final static String ACTIVITIES_ACTIVITYINFO_GET = "/activities/show/";
	/**
	 * 用户活动签到
	 */
	public final static String EVENTS_CHECKIN_POST = "/events/checkin/";
	/**
	 * 用户活动签到
	 */
	public final static String EVENTS_SET_CHECKIN_STATUS_POST = "/events/set_checkin_status/";
	/**
	 * 活动支付
	 */
	public final static String EVENTS_PRE_PAY = "/events/prepay/";
	/**
	 * 获取events数据
	 */
	public final static String EVENTS_EVENTSINFO_GET = "/events/show/";
	/**
	 * 导出参与人列表为 excel
	 */
	public final static String EVENTS_EXPORT_ACCEPT_LIST = "/events/export_accept_list/";
	/**
	 * 处理接受/拒绝邀请(退出活动)
	 */
	public final static String EVENTS_INVITE_HANDLE_POST = "/events/invite_handle.json";
	/**
	 * 关闭一个event，把event 的status改为'cancel'
	 */
	public final static String EVENTS_CANCELEVENT_GET = "/events/cancel/";
	/**
	 * 请求event受邀者列表(报名情况),返回列表分为接受邀请名单数组， 拒绝邀请名单数组
	 */
	public final static String EVENT_ENLIST_GET = "/events/enlist/";
	/**
	 * 处理接受/拒绝邀请
	 */
	public final static String EVENT_INVITE_POST = "/events/invite_handle.json";
	/**
	 * 发布运动心情
	 */
	public final static String MOMENT_WRITE_MOOD = "/moments/create.json";

	/**
	 * 刪除momoent
	 */
	public final static String MOMENT_DEL_GET = "/moments/delete/";
	/**
	 * 对momoent进行评论
	 */
	public final static String MOMENT_CREATE_GET = "/operations/comment.json";
	/**
	 * 点赞
	 */
	public final static String OPERATIONS_VOTE_POST = "/operations/vote.json";
	/**
	 * 获取金币
	 */
	public final static String OPERATIONS_ACTION_REWARD = "/operations/action_reward/";
	/**
	 * 获取用户扩展信息
	 */
	public final static String USERS_USER_EXTEND_INFO = "/users/user_extend_info.json";
	/**
	 * 取消点赞
	 */
	public final static String OPERATIONS_UNVOTE_POST = "/operations/unvote/";
	/**
	 * 登录
	 */
	public final static String O_AUTH_LOGIN = "/o_auth/login.json";

	/**
	 * 微信或其他注册
	 */
	public final static String USERS_REGISTER = "/users/register.json?";

	/**
	 * 手机注册
	 */
	public final static String USERS_PHONE_REGISTER = "/users/phone_register.json?";

	/**
	 * 获取验证码
	 */
	public final static String SEND_SMS = "/pages/send_sms.json?";

	/**
	 * 账号绑定
	 */
	public final static String BIND_PHONE = "/users/account_bind.json";

	/**
	 * 获取场馆列表
	 */
	public final static String BUSINESSES_LIST_GET_URL = "/businesses.json?";
	/**
	 * 获取精选列表
	 */
	public final static String POSTS_LIST_GET_URL = "/posts/index.json?";
	/**
	 *  动态 feeds 接口
	 */
	public final static String FEEDS_LIST = "/feeds.json?";
	/**
	 *  动态 -hot-tag-list 接口
	 */
	public final static String FEEDS_HOT_TAG_LIST = "/feeds/search.json";
	/**
	 * 获取精选列表
	 */
	public final static String POSTS_LIST_GET_URL2 = "/posts/show/";
	/**
	 * 获取热门标签
	 */
	public final static String FEED_HOT_TAGS = "/operations/hot_tags.json";
	/**
	 * 搜索标签
	 */
	public final static String FEED_SEARCH_TAGS = "/operations/existing_tags.json";

	/**
	 * 邀请卡
	 */
//	public static String SHARE_INVIT_URL = API_PREFIX + "/m/app/#events/";
	/**
	 * 邀请卡(新)
	 */
	public static String SHARE_INVIT_URL = API_PREFIX + "/ws/event_show/";

	/**
	 * 邀请卡
	 */
	public static String SHARE_CHALLENGES_URL = API_PREFIX
			+ "/m/app/#challenges/";
	/**
	 * 个人数据分享页
	 */
	public static String SHARE_RESULT_URL = API_PREFIX
			+ "/m/activities.html#show/";

	/**
	 * 场馆详情
	 */
	public static String BUSINESSES_DETAIL_URL = API_PREFIX
			+ "/m/app/#businesses/";

	/**
	 * 运动头条详情
	 */
	public static String POSTS_DETAIL_URL = API_PREFIX + "/m/app/#posts/";

	/**
	 * 帮助中心
	 */
	public static String HELP_URL = API_PREFIX + "/m/help.html";
	/**
	 * 活动详情页邀请好友(微信 QQ)
	 */
	public static String INVITE_FRIEND = API_PREFIX + "/m/app/#users/resume/";

	/**
	 * 个人用户获得自己的好友列表
	 */
	public final static String USERS_FRIEND_LIST_URL = "/users/friend_list.json";

	/**
	 * 查看个人信息
	 */
	public final static String USERS_SHOW_URL = "/users/show/";
	/**
	 * 查看我的信息
	 */
	public final static String USERS_MY_INFO = "/users/my_info.json";
	/**
	 * 返回活动排名列表:用户按类型分组（参加排名，不参加排名）
	 */
	public final static String EVENT_RANKLIST = "/events/ranklist/";
	/**
	 * 获得完整排名列表
	 */
	public final static String EVENT_LEADERBOARD_GET = "/events/leaderboard/";
	/**
	 * 设定活动的排名方式
	 */
	public final static String EVENT_RANK_SET_RANKING = "/events/set_ranking/";
	/**
	 * 允许某个user参加排名
	 */
	public final static String EVENT_RANK_ENABLE_USER_RANK = "/events/enable_user_rank/";
	/**
	 * 不允许某个user参加排名
	 */
	public final static String EVENT_RANK_DISABLE_USER_RANK = "/events/disable_user_rank/";
	/**
	 * 允许发布榜单
	 */
	public final static String EVENT_RANK_ENABLE_RANK = "/events/enable_rank/";
	/**
	 * 不允许发布榜单
	 */
	public final static String EVENT_RANK_DISABLE_RANK = "/events/disable_rank/";
	/**
	 * 请求七牛图片上传TOKEN
	 */
	public final static String GET_QINIU_TOKEN = "/operations/gen_qiniu_token.json";
	/**
	 * 修改用户信息
	 */
	public final static String POST_USER_UPDATE = "/users/update/";
	/**
	 * 获取版本信息
	 */
	public final static String POST_VERSION_UPDATE = "/operations/app_check_upgrade.json";
	/**
	 * 用户反馈
	 */
	public final static String FEED_BACK = "/feedback/add.json";
	/**
	 * 收藏
	 */
	public final static String POST_BOOKMARK = "/operations/bookmark.json";
	/**
	 * 取消我的收藏
	 */
	public final static String GET_UNBOOKMARK = "/operations/unbookmark/";
	/**
	 * 点赞
	 */
	public final static String POST_VOTE = "/operations/vote.json";
	/**
	 * 取消我的点赞
	 */
	public final static String GET_UNVOTE = "/operations/unvote/";
	/**
	 * 获取我的收藏
	 */
	public final static String GET_BOOKMARK = "/posts/bookmark_list.json?";
	/**
	 * 获取忘记密码的SID <mobile: 手机号码，必选参数>
	 * <type:　请求类型(register,reset_password),目前仅有这两种类型，必选参数>
	 * 
	 */
	public final static String POST_MESSAGE_SID = "/pages/send_sms.json";
	/**
	 * 上传单张图片地址到服务器
	 */
	public final static String POST_PHOTE_URL = "/operations/photo_add.json";
	/**
	 * 验证验证码和Sid的接口
	 */
	public final static String POST_VERDITY_VCODE = "/pages/verify_vcode.json";
	/**
	 * 忘记密码或者重置密码
	 */
	public final static String POST_RESET_PASSWORD = "/users/reset_password.json";

	public final static String NOTIFICATION_INDEX_POST = "/notifications.json";
	/**
	 * 向服务器提交alias
	 */
	public final static String POST_ALIAS = "/users/add_device.json";
	/**
	 * 返回各个挑战模板被使用的总数
	 */
	public final static String CHALLENGS_COUNT_GET = "/challenges/challenges_count.json";
	/**
	 * 创建一个挑战
	 */
	public final static String CHALLENGES_CREAT_POST = "/challenges/create.json";

	/**
	 * 挑战详情show
	 */
	public final static String CHALLENGES_SHOW_GET = "/challenges/show/";
	/**
	 * 加入站队
	 */
	public final static String CHALLENGES_JOIN_TEAM = "/challenges/join_team/";

	/**
	 * 退出挑战(拒绝邀请)
	 */
	public final static String CHALLENGES_DECLINE = "/challenges/decline/";
	/**
	 * 退出多团队挑战(拒绝邀请)
	 */
	public final static String CHALLENGES_GROUP_DECLINE = "/challenges/group_decline/";
	/**
	 * 邀请某个团队加入挑战（只针对团队挑战）
	 */
	public final static String CHALLENGES_GROUP_INVITE = "/challenges/group_invite/";
	/**
	 * 接受挑战
	 */
	public final static String CHALLENGES_ACCEPT = "/challenges/accept/";
	/**
	 * 取消挑战
	 */
	public final static String CHALLENGES_CANCEL = "/challenges/cancel/";
	/**
	 * 挑战中用户个人运动成绩(跑步)
	 */
	public final static String CHALLENGES_RUNNING_ACTIVITIES = "/challenges/activities/";
	/**
	 * 创建团队
	 */
	public final static String CREATE_GROUP = "/groups/create.json";
	/**
	 * 与当前用户有关联的群
	 */
	public final static String GROUPS_INDEX_GET = "/groups.json";
	/**
	 * 与某一用户的群（团队）
	 */
	public final static String GROUPS_LIST = "/groups/group_list.json";
	/**
	 * 关联团队
	 */
	public final static String EVENT_BIND_GROUP = "/events/bind_group/";
	/**
	 * 查找团队，按名字模糊查询
	 */
	public final static String GROUPS_SELECT_GET = "/groups/find_group.json?";
	/**
	 * 取消关联团队
	 */
	public final static String EVENT_UNBIND_GROUP = "/events/unbind_group/";
	/**
	 * 完善团队信息
	 */
	public final static String UPDATE_GROUP = "/groups/update/";
	/**
	 * 团队详情
	 */
	public final static String GROUPS_SHOW_GET = "/groups/show/";
	/**
	 * 团队分享邀请H5
	 */
	public final static String GROUPS_H5 = API_PREFIX + "/m/app/#groups/card/";
	/**
	 * 团队详情运动数据H5
	 */
	public final static String GROUPS_DATA = API_PREFIX
			+ "/m/app/#groups/data/";
	
	public final static String AWSTATS = API_PREFIX+"/awstats/coin_exchange.html";
	/**
	 * 团队相册 管理员移除某一个成员(只有管理员有权限调用该接口)
	 */
	public final static String GROUPS_REMOVE_MEMBER_GET = "/groups/remove_member/";
	/**
	 * 团队相册
	 */
	public final static String GROUPS_PHOTOS = "/groups/photos/";
	/**
	 * 加入团队
	 */
	public final static String GROUP_JOIN_GET = "/groups/join/";
	/**
	 * 退出团队
	 */
	public final static String GROUP_EXIT_GET = "/groups/leave/";
	/**
	 * 解散团队
	 */
	public final static String DISBAND_GROUP = "/groups/dismiss/";
	/**
	 * 添加好友
	 */
	public final static String ADD_FRIEND = "/users/add_friend/";
	/**
	 * 接受添加好友前(把response设置为yes)
	 */
	public final static String ADD_FRIEND_SETRESPONSE = "/notifications/set_response/";
	/**
	 * 接受添加好友
	 */
	public final static String ADD_FRIEND_ACCEPT = "/users/accept_friend/";
	/**
	 * 拒绝添加好友
	 */
	public final static String ADD_FRIEND_DECLINE = "/users/decline_friend/";
	/**
	 * 邀请好友参加活动
	 */
	public final static String EVENT_INVITE = "/events/invite.json";
	/**
	 * 邀请好友参加挑战
	 */
	public final static String CHALLENGE_INVITE = "/challenges/invite/";
	/**
	 * 删除好友
	 */
	public final static String REMOVE_FRIEND = "/users/remove_friend/";
	/**
	 * 推荐好友(可能认识的人)
	 */
	public final static String FRIEND_SUGGEST = "/users/friend_suggest.json";
	/**
	 * 搜索好友
	 */
	public final static String SEARCH_FRIEND = "/users/search.json?";
	/**
	 * 活动参与成员
	 */
	public final static String EVENT_ACCEPT = "/events/acceptlist/";
	/**
	 * 群发消息历史记录
	 */
	public final static String EVENT_MSG_HISTORY = "/events/msg_history/";
	/**
	 * 接受团队参加挑战
	 */
	public final static String CHALLENGES_GROUP_ACCEPT_POST = "/challenges/group_accept/";
	/**
	 * 团队中的排行榜
	 */
	public final static String CHALLENGES_GROUP_LEADERBOARD = "/challenges/group_leaderboard/";
	/**
	 * 等级积分
	 */
	public final static String LEVEL = "/users/level.json";
	/**
	 * 判断账号是否绑定
	 */
	public final static String BIND_PRECHECK = "/users/account_bind_precheck.json";
	/**
	 * 获取我的账户信息
	 */
	public final static String ACCOUNT_ACCOUNT = "/accounts/show.json";
	/**
	 * 设置提现银行卡
	 */
	public final static String SET_ACCOUNT_INFO = "/accounts/set_account_info.json";
	/**
	 * 提现
	 */
	public final static String ACCOUNT_WITHDRAWAL = "/accounts/withdraw.json";
	/**
	 * 账户明细
	 */
	public final static String ACCOUNT_TEANSACTION = "/accounts/transaction_details.json";
	/**
	 * 提现记录
	 */
	public final static String WITHDRAW_DETAIlS = "/accounts/withdraw_details.json";
	/**
	 * 优秀团队推荐
	 */
	public final static String GROUP_SUGGEST = "/groups/suggest_list.json";
	/**
	 * 客户端设置通知推送
	 */
	public final static String NOTIFICATION_SET = "/operations/notification_config.json";
	/**
	 * 导出人挑战数据
	 */
	public final static String CHALLENGE_EXPORT = "/challenges/export/";
}
