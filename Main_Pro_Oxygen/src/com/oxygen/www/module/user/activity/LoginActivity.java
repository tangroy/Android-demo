package com.oxygen.www.module.user.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.MenuActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class LoginActivity extends Activity implements OnClickListener {
	public final int NET_USERLOGIN = 1;
	private final int MSG_SET_ALIAS = 1001;
	private final int POST_ALIAS = 1002;
	private final int QQResponse = 1003;
	private final int POST_QQ_REGISTER = 1004;
	private TextView tv_register;
	private TextView tv_weixinlogin;
	private TextView login_wx_new;
	private TextView login_qq_new;
	private TextView login_lyd_new;
	private ProgressBar progressBar;
	private RelativeLayout rl_weixinlogin;

	private String TAG = "LoginActivity";
	private String alias;
	private QQAuth mQQAuth;
	private Tencent mTencent;
	private User mInfo;
	private boolean QQFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mQQAuth = OxygenApplication.qqAuth;
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_weixinlogin = (TextView) findViewById(R.id.tv_weixinlogin);
		login_wx_new = (TextView) findViewById(R.id.login_wx_new);
		login_qq_new = (TextView) findViewById(R.id.login_qq_new);
		login_lyd_new = (TextView) findViewById(R.id.login_lyd_new);
		rl_weixinlogin = (RelativeLayout) findViewById(R.id.rl_weixinlogin);

		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}

	private void initViewsEvent() {
		tv_register.setOnClickListener(this);
		rl_weixinlogin.setOnClickListener(this);
		login_qq_new.setOnClickListener(this);
		login_lyd_new.setOnClickListener(this);
	}

	private void initValues() {
		login_wx_new.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.rl_weixinlogin:
			if (!OxygenApplication.api.isWXAppInstalled()) {
				Toast.makeText(LoginActivity.this, "您还未安装微信客户端",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!OxygenApplication.api.isWXAppInstalled()) {
				Toast.makeText(LoginActivity.this, "您还未安装微信客户端",
						Toast.LENGTH_SHORT).show();
			} else {
				OxygenApplication.weixinsdk = "login";
				SendAuth.Req req = new SendAuth.Req();
				req.scope = "snsapi_userinfo";
				req.state = "com.oxygen.www";
				OxygenApplication.api.sendReq(req);
			}

			break;
		case R.id.login_qq_new:
			QQLogin(this, true);
			break;
		case R.id.tv_register:
			intent = new Intent(LoginActivity.this, PhoneRegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.login_lyd_new:
			QQFlag = false;
			intent = new Intent(LoginActivity.this,
					LoginByPhoneNumberActivity.class);
			startActivity(intent);
			this.finish();
			break;
		default:
			break;
		}

	}

	public void QQLogin(Activity context, boolean loginFlag) {
		mTencent = OxygenApplication.tencent;
		mTencent.login(context, "all", new BaseUiListener());
		QQFlag = true;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				JPushInterface.setAliasAndTags(getApplicationContext(),
						(String) msg.obj, null, mAliasCallback);

				break;
			case POST_QQ_REGISTER:
				String qq_register = (String) msg.obj;
				if (qq_register != null && qq_register.length() > 0) {
					try {
						JSONObject json_qq_register = new JSONObject(
								qq_register);
						if (json_qq_register.getInt("status") == 200) {
							// 储存个人信息
							OxygenApplication.OAUTH_TOKEN = json_qq_register
									.getString("oauth_token");
							OxygenApplication.OAUTH_TOKEN_SECRET = json_qq_register
									.getString("oauth_token_secret");
							if (!json_qq_register.isNull("oauth_signature")) {
								OxygenApplication.OAUTH_SIGNATURE = json_qq_register
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
							if (!json_qq_register.isNull("current_user")) {
								user = new User();
								JSONObject json_user = new JSONObject(
										json_qq_register
												.getString("current_user"));
								user = UsersConstruct.ToUser(json_user);
								saveUserInfoToSp(user);
								// 注册jpush别名
								setAlias(user);
							}
							Intent intent;
							if (TextUtils.isEmpty(user.getIs_create())) {
								intent = new Intent(LoginActivity.this,
										MenuActivity.class);
							} else {
								intent = new Intent(LoginActivity.this,
										NewDataActivity.class);
								intent.putExtra("loginType", "qq");
							}
							intent.putExtra("user", user);
							startActivity(intent);
							LoginActivity.this.finish();
						} else {
							Toast.makeText(LoginActivity.this,
									json_qq_register.getString("msg"),
									Toast.LENGTH_LONG).show();
							LoginActivity.this.finish();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(LoginActivity.this,
							getResources().getString(R.string.errcode_wx),
							Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		}

	};

	private void setAlias(User user) {
		alias = user.getId() + "";
		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(LoginActivity.this, R.string.alias,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!GDUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(LoginActivity.this, R.string.alias,
					Toast.LENGTH_SHORT).show();
			return;
		}
		postAliasToNet();
		// 调用JPush API设置Alias
		handler.sendMessage(handler.obtainMessage(MSG_SET_ALIAS, alias));
	}

	/**
	 * 向服务器提交alias 以供推送使用
	 * 
	 * @param alias
	 */
	public void postAliasToNet() {
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

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Log.i(TAG, logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				if (GDUtil.isConnected(getApplicationContext())) {
					handler.sendMessageDelayed(
							handler.obtainMessage(MSG_SET_ALIAS, alias),
							1000 * 60);
				} else {
					Log.i(TAG, "No network");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				Log.e(TAG, logs);
			}

		}

	};

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			try {
				String openId = ((JSONObject) response).getString("openid");
				ToastUtil.show(LoginActivity.this, "QQ授权成功，正在登录");
				getUserSimpleInfo(openId);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onError(UiError e) {

		}

		@Override
		public void onCancel() {

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 登陆成功后获取用户的信息
	 */
	public void getUserSimpleInfo(final String openId) {
		QQToken qqToken = mTencent.getQQToken();
		UserInfo info = new UserInfo(this, qqToken);
		info.getUserInfo(new IUiListener() {

			@Override
			public void onError(UiError arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(Object arg0) {
				try {
					JSONObject jsonObject = (JSONObject) arg0;
					String nickname = (String) jsonObject.get("nickname");
					String gender = (String) jsonObject.get("gender");
					String headUrl = (String) jsonObject.get("figureurl_qq_2");
					UserInfoUtils.setUserInfo(getApplicationContext(),Constants.NICKNAME, nickname);
					UserInfoUtils.setUserInfo(getApplicationContext(),Constants.SEX, gender);
					UserInfoUtils.setUserInfo(getApplicationContext(),Constants.HEADIMG_URL, headUrl);
					UserInfoUtils.setUserInfo(getApplicationContext(),Constants.OPENID, openId);
					final Map<String, String> params = new HashMap<String, String>();
					params.put("consumer_key", Constants.CONSUMER_KTY);
					params.put("type", "qq");
					params.put("qq_openid", openId);
					params.put("nickname", nickname);
					if ("男".equals(gender)) {
						params.put("sex", "0");
					} else {
						params.put("sex", "1");
					}
					params.put("headimgurl", headUrl);
					OxygenApplication.cachedThreadPool.execute(new Runnable() {
						public void run() {
							HttpUtil.Post_Noauth(UrlConstants.O_AUTH_LOGIN,
									handler, POST_QQ_REGISTER, params);

						}
					});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}
		});
	}
}
