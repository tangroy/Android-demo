package com.oxygen.www.wxapi;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.MenuActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.module.user.activity.NewDataActivity;
import com.oxygen.www.module.user.eventbus_entities.BindQQOrWX;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.modelmsg.SendAuth;

import de.greenrobot.event.EventBus;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private final int GET_ACCESS_TOKEN = 1;
	private final int GET_WX_USERINFO = 2;
	private final int POST_WX_REGISTER = 3;
	private String wxcode;
	private String wxget_access_token_url = "https://api.weixin.qq.com/sns/";
	private final int MSG_SET_ALIAS = 1001;
	protected final int POST_ALIAS = 1002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxlogin);
		OxygenApplication.api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		OxygenApplication.api.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			if (OxygenApplication.weixinsdk.equals("login")) {
				// wxcode = ((SendAuth.Resp) resp).token;
				wxcode = ((SendAuth.Resp) resp).code;
			}

			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		if (resp.errCode != BaseResp.ErrCode.ERR_OK) {
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
		} else {
			// 获取access_token
			if (OxygenApplication.weixinsdk.equals("login")) {
				OxygenApplication.cachedThreadPool.execute(new Runnable() {
					public void run() {
						HttpUtil.Get_Noauth(wxget_access_token_url
								+ "oauth2/access_token?appid="
								+ Constants.WEIXIN_APPID + "&secret="
								+ Constants.WEIXIN_SECRET + "&code=" + wxcode
								+ "&grant_type=authorization_code", handler,
								GET_ACCESS_TOKEN);
					}
				});
			} else {
				finish();
			}

		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_ACCESS_TOKEN:
				String wxresult = (String) msg.obj;
				if (wxresult != null && wxresult.length() > 0) {
					try {
						JSONObject jsonobject_token = new JSONObject(wxresult);
						if (!jsonobject_token.isNull("access_token")) {
							final String access_token = jsonobject_token
									.getString("access_token");
							final String openid = jsonobject_token
									.getString("openid");

							// 获取access_token
							OxygenApplication.cachedThreadPool
									.execute(new Runnable() {
										public void run() {
											HttpUtil.Get_Noauth(
													wxget_access_token_url
															+ "userinfo?lang=zh_CN&access_token="
															+ access_token
															+ "&openid="
															+ openid, handler,
													GET_WX_USERINFO);
										}
									});
						} else if (!jsonobject_token.isNull("errmsg")) {
							Toast.makeText(WXEntryActivity.this,
									jsonobject_token.getString("errmsg"),
									Toast.LENGTH_LONG).show();
							WXEntryActivity.this.finish();
						} else {
							WXEntryActivity.this.finish();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(WXEntryActivity.this,
							getResources().getString(R.string.errcode_wx),
							Toast.LENGTH_LONG).show();
					WXEntryActivity.this.finish();
				}
				break;

			case GET_WX_USERINFO:
				String wx_userinfo = (String) msg.obj;
				if (wx_userinfo != null && wx_userinfo.length() > 0) {
					JSONObject json_wx_userinfo;
					try {
						json_wx_userinfo = new JSONObject(wx_userinfo);
						if (!json_wx_userinfo.isNull("openid")) {
							if ((Boolean) UserInfoUtils.getUserInfo(
									getApplicationContext(),
									Constants.LOGIN_TO_BIND,
									false)) {
								EventBus.getDefault().post(
										new BindQQOrWX("wx", json_wx_userinfo
												.getString("unionid")));
								WXEntryActivity.this.finish();
							} else {
								// POST 参数
								final Map<String, String> params = new HashMap<String, String>();
								params.put("consumer_key",
										Constants.CONSUMER_KTY);
								params.put("openid",
										json_wx_userinfo.getString("openid"));
								params.put("nickname",
										json_wx_userinfo.getString("nickname"));
								params.put("sex",
										json_wx_userinfo.getInt("sex") + "");
								params.put("language",
										json_wx_userinfo.getString("language"));
								params.put("city",
										json_wx_userinfo.getString("city"));
								params.put("province",
										json_wx_userinfo.getString("province"));
								params.put("country",
										json_wx_userinfo.getString("country"));
								params.put("headimgurl", json_wx_userinfo
										.getString("headimgurl"));
								params.put("privilege",
										json_wx_userinfo.getString("privilege"));
								params.put("unionid",
										json_wx_userinfo.getString("unionid"));
								params.put("type", "wechat");
								OxygenApplication.cachedThreadPool
										.execute(new Runnable() {
											public void run() {
												HttpUtil.Post_Noauth(
														UrlConstants.O_AUTH_LOGIN,
														handler,
														POST_WX_REGISTER,
														params);

											}
										});
							}
						} else if (!json_wx_userinfo.isNull("errmsg")) {
							Toast.makeText(WXEntryActivity.this,
									json_wx_userinfo.getString("errmsg"),
									Toast.LENGTH_LONG).show();
							WXEntryActivity.this.finish();
						} else {
							WXEntryActivity.this.finish();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					Toast.makeText(WXEntryActivity.this,
							getResources().getString(R.string.errcode_wx),
							Toast.LENGTH_LONG).show();
					WXEntryActivity.this.finish();
				}
				break;

			case POST_WX_REGISTER:
				String wx_register = (String) msg.obj;
				if (wx_register != null && wx_register.length() > 0) {
					try {
						JSONObject json_wx_register = new JSONObject(
								wx_register);
						if (json_wx_register.getInt("status") == 200) {
							// 储存个人信息
							OxygenApplication.OAUTH_TOKEN = json_wx_register
									.getString("oauth_token");
							OxygenApplication.OAUTH_TOKEN_SECRET = json_wx_register
									.getString("oauth_token_secret");
							if (!json_wx_register.isNull("oauth_signature")) {
								OxygenApplication.OAUTH_SIGNATURE = json_wx_register
										.getString("oauth_signature");
							}
							// 登陆后 保存信息
							UserInfoUtils.setUserInfo(
									getApplicationContext(),Constants.OAUTH_TOKEN,
									OxygenApplication.OAUTH_TOKEN);
							UserInfoUtils.setUserInfo(
									getApplicationContext(),Constants.OAUTH_TOKEN_SECRET,
									OxygenApplication.OAUTH_TOKEN_SECRET);
							UserInfoUtils.setUserInfo(
									getApplicationContext(),Constants.OAUTH_SIGNATURE,
									OxygenApplication.OAUTH_SIGNATURE);
							User user = null;
							if (!json_wx_register.isNull("current_user")) {
								user = new User();
								JSONObject json_user = new JSONObject(
										json_wx_register
												.getString("current_user"));
								user = UsersConstruct.ToUser(json_user);
								saveUserInfoToSp(user);
								// 注册jpush别名
								setAlias(user);
							}
							Intent intent;
							if (TextUtils.isEmpty(user.getIs_create())) {
								intent = new Intent(WXEntryActivity.this,
										MenuActivity.class);
							} else {
								intent = new Intent(WXEntryActivity.this,
										NewDataActivity.class);
								intent.putExtra("loginType", "weixin");
							}

							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							WXEntryActivity.this.finish();
							// System.exit(0);

						} else {
							Toast.makeText(WXEntryActivity.this,
									json_wx_register.getString("msg"),
									Toast.LENGTH_LONG).show();
							WXEntryActivity.this.finish();
						}
					} catch (JSONException e) {
						e.printStackTrace();
						WXEntryActivity.this.finish();
					}
				} else {
					Toast.makeText(WXEntryActivity.this,
							getResources().getString(R.string.register_f),
							Toast.LENGTH_LONG).show();
					WXEntryActivity.this.finish();
				}
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 保存用户信息到sp
	 */
	private void saveUserInfoToSp(User user) {
		UserInfoUtils.setUserInfo(getApplicationContext(), Constants.AGE,user.age + "");
		UserInfoUtils.setUserInfo(getApplicationContext(),Constants.HEIGHT, user.height + "");
		UserInfoUtils.setUserInfo(getApplicationContext(), Constants.WEIGHT,user.weight + "");
		UserInfoUtils.setUserInfo(getApplicationContext(),Constants.SPORT_SELECTED, user.sports);
		UserInfoUtils.setUserInfo(getApplicationContext(),Constants.NICKNAME, user.nickname);
		UserInfoUtils.setUserInfo(getApplicationContext(), Constants.SEX, user.sex + "");
		if (user.intro == null || user.intro.equals("")) {
			UserInfoUtils.setUserInfo(getApplicationContext(),Constants.SIGN, "一起运动更快乐！");
		} else {
			UserInfoUtils.setUserInfo(getApplicationContext(), Constants.SIGN,user.intro + "");
		}
		UserInfoUtils.setUserInfo(getApplicationContext(),Constants.HEADIMG_URL, user.headimgurl);
		UserInfoUtils.setUserInfo(getApplicationContext(), Constants.USERID,user.id + "");
		UserInfoUtils.setUserInfo(getApplicationContext(), Constants.MOBILE,user.mobile);
		UserInfoUtils.setUserInfo(getApplicationContext(),Constants.TOKEN, user.token);
		UserInfoUtils.setUserInfo(getApplicationContext(), Constants.UNIONID,user.unionid);
		UserInfoUtils.setUserInfo(getApplicationContext(), Constants.OPENID,user.openid);
		UserInfoUtils.setUserInfo(getApplicationContext(), Constants.QQ_OPENID,user.qq_openid);
		UserInfoUtils.setUserInfo(getApplicationContext(),Constants.LEVEL, user.level);
		UserInfoUtils.setUserInfo(getApplicationContext(),Constants.COINS, user.coins);
		UserInfoUtils.setUserInfo(getApplicationContext(), Constants.POINTS,user.points);
	}

	private void setAlias(User user) {
		String alias = user.getId() + "";
		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(WXEntryActivity.this, R.string.alias,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!GDUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(WXEntryActivity.this, R.string.alias,
					Toast.LENGTH_SHORT).show();
			return;
		}
		postAliasToNet(alias);
		// 调用JPush API设置Alias
		handler.sendMessage(handler.obtainMessage(MSG_SET_ALIAS, alias));
	}

	/**
	 * 向服务器提交alias 以供推送使用
	 * 
	 * @param alias
	 */
	public void postAliasToNet(String alias) {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", alias);
		params.put("alias", alias);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.POST_ALIAS, handler, POST_ALIAS,
						params);

			}
		});
	}

}
