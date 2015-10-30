package com.oxygen.www.module.user.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
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

public class LoginByPhoneNumberActivity extends BaseActivity implements
		OnClickListener {
	public final int NET_USERLOGIN = 1;
	private final int MSG_SET_ALIAS = 1001;
	private final int POST_ALIAS = 1002;
	private final int POST_QQ_REGISTER = 1004;
	private ImageView iv_back;
	private EditText username;
	private EditText password;
	private TextView login;
	private TextView forget_psw;
	private ProgressBar progressBar;

	private TextView login_wx_new;
	private TextView login_qq_new;
	private TextView login_lyd_new;

	private String TAG = "LoginByPhoneNumberActivity";
	private String alias;
	private QQAuth mQQAuth;
	private Tencent mTencent;
	private User mInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phonenumber_login);
		mQQAuth = OxygenApplication.qqAuth;
		mTencent = OxygenApplication.tencent;
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		forget_psw = (TextView) findViewById(R.id.forget_psw);
		login = (TextView) findViewById(R.id.login);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		login_wx_new = (TextView) findViewById(R.id.login_wx_new);
		login_qq_new = (TextView) findViewById(R.id.login_qq_new);
		login_lyd_new = (TextView) findViewById(R.id.login_lyd_new);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		forget_psw.setOnClickListener(this);
		login.setOnClickListener(this);
		login_wx_new.setOnClickListener(this);
		login_qq_new.setOnClickListener(this);
	}

	private void initValues() {
		login_lyd_new.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.iv_back:
			intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.login:
			if (TextUtils.isEmpty(username.getText())) {
				ToastUtil.show(LoginByPhoneNumberActivity.this, getResources()
						.getString(R.string.toast_login_username));
			} else if (!GDUtil.isMobileNO(username.getText().toString())) {
				ToastUtil.show(LoginByPhoneNumberActivity.this, getResources()
						.getString(R.string.toast_photo_error));
			} else if (TextUtils.isEmpty(password.getText())) {
				ToastUtil.show(LoginByPhoneNumberActivity.this, getResources()
						.getString(R.string.toast_login_password));
			} else {
				login(username.getText().toString(), password.getText()
						.toString());
			}
			break;

		case R.id.login_wx_new:
			if (!OxygenApplication.api.isWXAppInstalled()) {
				Toast.makeText(LoginByPhoneNumberActivity.this, "您还未安装微信客户端",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!OxygenApplication.api.isWXAppInstalled()) {
				Toast.makeText(LoginByPhoneNumberActivity.this, "您还未安装微信客户端",
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
			mTencent.login(this, "all", new BaseUiListener());
			break;
		case R.id.forget_psw:
			intent = new Intent(LoginByPhoneNumberActivity.this,
					InputPhoneNumberActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	private void login(final String username, final String password) {
		// POST 参数
		final Map<String, String> params = new HashMap<String, String>();
		params.put("consumer_key", Constants.CONSUMER_KTY);
		params.put("mobile", username);
		params.put("password", password);
		progressBar.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post_Noauth(UrlConstants.O_AUTH_LOGIN, handler,
						NET_USERLOGIN, params);

			}
		});

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
			case POST_ALIAS:
				break;
			case NET_USERLOGIN:
				progressBar.setVisibility(View.GONE);
				String logindata = (String) msg.obj;
				// System.out.println(logindata.toString());
				if (logindata != null) {
					try {
						JSONObject jsonobject = new JSONObject(logindata);

						if (!jsonobject.isNull("status")) {
							if (jsonobject.getInt("status") == 200) {
								OxygenApplication.OAUTH_TOKEN = jsonobject
										.getString("oauth_token");
								OxygenApplication.OAUTH_TOKEN_SECRET = jsonobject
										.getString("oauth_token_secret");
								OxygenApplication.OAUTH_SIGNATURE = jsonobject
										.getString("oauth_signature");
								// 存储token值
								UserInfoUtils.setUserInfo(
										getApplicationContext(),
										Constants.OAUTH_TOKEN,
										jsonobject.getString("oauth_token"));
								UserInfoUtils.setUserInfo(
												LoginByPhoneNumberActivity.this,
												Constants.OAUTH_TOKEN_SECRET,
												jsonobject.getString("oauth_token_secret"));
								UserInfoUtils.setUserInfo(
												LoginByPhoneNumberActivity.this,
												Constants.OAUTH_SIGNATURE,
												jsonobject.getString("oauth_signature"));
								User user = null;
								if (!jsonobject.isNull("current_user")) {
									user = new User();
									JSONObject json_user = new JSONObject(
											jsonobject
													.getString("current_user"));
									user = UsersConstruct.ToUser(json_user);
									// 注册jpush别名
									setAlias(user);
									// 保存用户信息
									saveUserInfoToSp(user);
								}
								Intent intent;
								if (TextUtils.isEmpty(user.getIs_create())) {
									intent = new Intent(
											LoginByPhoneNumberActivity.this,
											MenuActivity.class);
								} else {
									intent = new Intent(
											LoginByPhoneNumberActivity.this,
											NewDataActivity.class);
									intent.putExtra("loginType", "phoneNumber");
								}
								intent.putExtra("user", user);
								startActivity(intent);
								finish();
							} else {
								Toast.makeText(LoginByPhoneNumberActivity.this,
										jsonobject.getString("msg"),
										Toast.LENGTH_LONG).show();
							}
						}
					} catch (JSONException e) {
						Toast.makeText(LoginByPhoneNumberActivity.this, "网络异常",
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
				} else {
					Toast.makeText(LoginByPhoneNumberActivity.this,
							getResources().getString(R.string.errcode_wx),
							Toast.LENGTH_LONG).show();
				}

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
									OxygenApplication.context,
									Constants.OAUTH_TOKEN,
									OxygenApplication.OAUTH_TOKEN);
							UserInfoUtils.setUserInfo(
									OxygenApplication.context,
									Constants.OAUTH_TOKEN_SECRET,
									OxygenApplication.OAUTH_TOKEN_SECRET);
							UserInfoUtils.setUserInfo(
									OxygenApplication.context,
									Constants.OAUTH_SIGNATURE,
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
								intent = new Intent(
										LoginByPhoneNumberActivity.this,
										MenuActivity.class);
							} else {
								intent = new Intent(
										LoginByPhoneNumberActivity.this,
										NewDataActivity.class);
								intent.putExtra("loginType", "qq");
							}
							intent.putExtra("user", user);
							startActivity(intent);
							LoginByPhoneNumberActivity.this.finish();
						} else {
							Toast.makeText(LoginByPhoneNumberActivity.this,
									json_qq_register.getString("msg"),
									Toast.LENGTH_LONG).show();
							LoginByPhoneNumberActivity.this.finish();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(LoginByPhoneNumberActivity.this,
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
			Toast.makeText(LoginByPhoneNumberActivity.this, R.string.alias,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!GDUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(LoginByPhoneNumberActivity.this, R.string.alias,
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
				getUserSimpleInfo(openId);
				ToastUtil.show(LoginByPhoneNumberActivity.this, "QQ授权成功，正在登录");
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
		UserInfo info = new UserInfo(getApplicationContext(), qqToken);
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
					UserInfoUtils.setUserInfo(getApplicationContext(),Constants.NICKNAME,
							nickname);
					UserInfoUtils.setUserInfo(getApplicationContext(),Constants.SEX,
							gender);
					UserInfoUtils.setUserInfo(getApplicationContext(),Constants.HEADIMG_URL, headUrl);
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
