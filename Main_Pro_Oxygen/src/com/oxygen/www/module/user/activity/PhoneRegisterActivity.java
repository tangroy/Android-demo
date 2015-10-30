package com.oxygen.www.module.user.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.sdk.modelmsg.SendAuth;
//import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 注册界面
 * @author 杨庆雷
 * 2015-7-7上午10:30:36
 */
public class PhoneRegisterActivity extends Activity implements OnClickListener {

	protected static final int POST_QQ_REGISTER = 3;
	private final int SEND_VCODE = 1;
	private final int GO_REGISTER = 2;
	private String TAG = "PhoneRegisterActivity";
	private ImageView iv_back;
	private EditText et_mobile, et_vcode, et_password;
	private Button btn_getvcode;
	private TextView btn_register;
	private TextView login_wx_new;
	private TextView login_qq_new;
	private TextView login_lyd_new;
	private String sid;
	private ProgressBar progressBar;
	private   final int MSG_SET_ALIAS = 1001;
	private   final int POST_ALIAS = 1002;
	private int time = 60;
	private Timer timer = new Timer();
	private Tencent mTencent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		mTencent = OxygenApplication.tencent;
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_mobile = (EditText) findViewById(R.id.et_mobile);
		et_vcode = (EditText) findViewById(R.id.et_vcode);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_getvcode = (Button) findViewById(R.id.btn_getvcode);
		btn_register = (TextView) findViewById(R.id.btn_register);
		progressBar  =(ProgressBar) findViewById(R.id.progressBar);
		
		login_wx_new = (TextView) findViewById(R.id.login_wx_new);
		login_qq_new = (TextView) findViewById(R.id.login_qq_new);
		login_lyd_new = (TextView) findViewById(R.id.login_lyd_new);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		btn_getvcode.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		login_wx_new.setOnClickListener(this);
		login_qq_new.setOnClickListener(this);
		login_lyd_new.setOnClickListener(this);
	}

	private void initValues() {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_getvcode:
			sendVcode();
			break;
		case R.id.btn_register:
			goRegister();
			break;
		case R.id.login_wx_new:
			if (!OxygenApplication.api.isWXAppInstalled()) {
				Toast.makeText(PhoneRegisterActivity.this, "您还未安装微信客户端",
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
		case R.id.login_lyd_new:
			Intent intent = new Intent(this,LoginByPhoneNumberActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	/**
	 * 发送验证码
	 */
	private void sendVcode() {
		if (TextUtils.isEmpty(et_mobile.getText())) {
			Toast.makeText(PhoneRegisterActivity.this,
					getResources().getString(R.string.toast_login_username),
					Toast.LENGTH_LONG).show();
		} else if (!GDUtil.isMobileNO(et_mobile.getText().toString())) {
			Toast.makeText(PhoneRegisterActivity.this,
					getResources().getString(R.string.toast_photo_error),
					Toast.LENGTH_LONG).show();
		} else {
			getCode();
			final Map<String, String> params = new HashMap<String, String>();
			params.put("mobile", et_mobile.getText().toString());
			params.put("type", "register");
			OxygenApplication.cachedThreadPool.execute(new Runnable() {
				public void run() {
					HttpUtil.Post_Noauth(UrlConstants.SEND_SMS, handler,
							SEND_VCODE, params);

				}
			});
		}
	}

	/**
	 * 快速注册
	 */
	private void goRegister() {
		if (TextUtils.isEmpty(et_mobile.getText())) {
			Toast.makeText(PhoneRegisterActivity.this,
					getResources().getString(R.string.toast_login_username),
					Toast.LENGTH_LONG).show();
		} else if (!GDUtil.isMobileNO(et_mobile.getText().toString())) {
			Toast.makeText(PhoneRegisterActivity.this,
					getResources().getString(R.string.toast_photo_error),
					Toast.LENGTH_LONG).show();
		} else if (TextUtils.isEmpty(et_vcode.getText())) {
			Toast.makeText(PhoneRegisterActivity.this,
					getResources().getString(R.string.toast_vcode_error),
					Toast.LENGTH_LONG).show();
		} else if (et_vcode.getText().toString().length() < 4) {
			Toast.makeText(PhoneRegisterActivity.this,
					getResources().getString(R.string.toast_vcode_error),
					Toast.LENGTH_LONG).show();
		} else if (TextUtils.isEmpty(et_password.getText())) {
			Toast.makeText(PhoneRegisterActivity.this,
					getResources().getString(R.string.toast_login_password),
					Toast.LENGTH_LONG).show();
		} else if (sid == null) {
			Toast.makeText(PhoneRegisterActivity.this, "sid is null...",
					Toast.LENGTH_LONG).show();
		} else {
			final Map<String, String> params = new HashMap<String, String>();
			params.put("mobile", et_mobile.getText().toString());
			params.put("username", et_mobile.getText().toString());
			params.put("password", et_password.getText().toString());
			params.put("sid", sid);
			params.put("vcode", et_vcode.getText().toString());
			params.put("consumer_key", Constants.CONSUMER_KTY);
			progressBar.setVisibility(View.VISIBLE);
			OxygenApplication.cachedThreadPool.execute(new Runnable() {
				public void run() {
					HttpUtil.Post_Noauth(UrlConstants.USERS_PHONE_REGISTER,
							handler, GO_REGISTER, params);

				}
			});

		}
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
								intent = new Intent(PhoneRegisterActivity.this,
										MenuActivity.class);
							} else {
								intent = new Intent(PhoneRegisterActivity.this,
										NewDataActivity.class);
								intent.putExtra("loginType", "qq");
							}
							
							intent.putExtra("user", user);
							startActivity(intent);
							PhoneRegisterActivity.this.finish();
						} else {
							Toast.makeText(PhoneRegisterActivity.this,
									json_qq_register.getString("msg"),
									Toast.LENGTH_LONG).show();
							PhoneRegisterActivity.this.finish();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(PhoneRegisterActivity.this,
							getResources().getString(R.string.errcode_wx),
							Toast.LENGTH_LONG).show();
				}
				break;
			case SEND_VCODE:
				et_vcode.setFocusable(true);
				String str_vcode = (String) msg.obj;
				if (str_vcode != null && str_vcode.length() > 0) {
					try {
						JSONObject json_vcode = new JSONObject(str_vcode);
						if (json_vcode.getInt("status") == 200) {
							// 发送短信成功，记录sid
							sid = new JSONObject(json_vcode.getString("result"))
									.getString("sid");
							Toast.makeText(PhoneRegisterActivity.this,
									"验证码已发送",
									Toast.LENGTH_LONG).show();
						} else if(json_vcode.getInt("status") == 400){
							Toast.makeText(PhoneRegisterActivity.this,
									json_vcode.getString("msg"),
									Toast.LENGTH_LONG).show();
							setGetCodeBtnType();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(PhoneRegisterActivity.this,
							getResources().getString(R.string.errcode_wx),
							Toast.LENGTH_LONG).show();
					setGetCodeBtnType();
				}
				break;
			case GO_REGISTER:
				progressBar.setVisibility(View.GONE);
				String str_register = (String) msg.obj;
				if (str_register != null && str_register.length() > 0) {
					JSONObject json_register;
					try {
						json_register = new JSONObject(str_register);
						if (json_register.getInt("status") == 200) {
							OxygenApplication.OAUTH_TOKEN = json_register
									.getString("oauth_token");
							OxygenApplication.OAUTH_TOKEN_SECRET = json_register
									.getString("oauth_token_secret");
							OxygenApplication.OAUTH_SIGNATURE = json_register
									.getString("oauth_signature");
							
							// 存储token值
							UserInfoUtils.setUserInfo(
									getApplicationContext(),
									Constants.OAUTH_TOKEN,
									json_register.getString("oauth_token"));
							UserInfoUtils.setUserInfo(
									getApplicationContext(),
											Constants.OAUTH_TOKEN_SECRET,
											json_register.getString("oauth_token_secret"));
							UserInfoUtils.setUserInfo(
									getApplicationContext(),
											Constants.OAUTH_SIGNATURE,
											json_register.getString("oauth_signature"));
							User user = null;
							if (!json_register.isNull("current_user")) {
								user = new User();
								JSONObject json_user = new JSONObject(
										json_register.getString("current_user"));
								user = UsersConstruct.ToUser(json_user);
							}
							//保存用户信息到sp
							UserInfoUtils.saveUserInfoToSp(PhoneRegisterActivity.this,user);
							setAlias(user);//注册jpush
							//保存密码
							UserInfoUtils.setUserInfo(getApplicationContext(),Constants.SET_PASSWORD, et_password.getText().toString());
							Intent intent = new Intent(
									PhoneRegisterActivity.this,
									NewDataActivity.class);
							intent.putExtra("loginType", "phoneNumber");
							startActivity(intent);
							PhoneRegisterActivity.this.finish();
						} else {
							Toast.makeText(PhoneRegisterActivity.this,
									json_register.getString("msg"),
									Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					Toast.makeText(PhoneRegisterActivity.this,
							getResources().getString(R.string.errcode_wx),
							Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		}

	};
	private TimerTask task;
	/**
	 * 但获取验证码成功或者60秒以后  回复获取验证码按钮的状态
	 */
	private void setGetCodeBtnType() {
		btn_getvcode.setEnabled(true);
		btn_getvcode
				.setBackgroundResource(R.drawable.wane_shape_getcode_red);
		btn_getvcode.setText("获取验证码");
		btn_getvcode.setTextSize(14);
		btn_getvcode.setTextColor(Color.WHITE);
		if(task != null){
			task.cancel();
		}
	}
	private void setAlias(User user) {
		String alias = user.getId() + "";
		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(PhoneRegisterActivity.this, R.string.alias,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!GDUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(PhoneRegisterActivity.this, R.string.alias,
					Toast.LENGTH_SHORT).show();
			return;
		}

		//注册成功后向服务器提交alias
		postAliasToNet(alias);
		// 调用JPush API设置Alias
		handler.sendMessage(handler.obtainMessage(MSG_SET_ALIAS, alias));
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
		if (user.sex == 0 || "0".equals(user.sex + "")) {
			UserInfoUtils.setUserInfo(getApplicationContext(), Constants.SEX,getResources().getString(R.string.man));
		} else if (user.sex == 1 || "1".equals(user.sex + "")) {
			UserInfoUtils.setUserInfo(getApplicationContext(),Constants.SEX, getResources().getString(R.string.woman));
		} else {
			UserInfoUtils.setUserInfo(getApplicationContext(),Constants.SEX,
					getResources().getString(R.string.unknown));
		}
		if (user.intro == null || user.intro.equals("")) {
			UserInfoUtils.setUserInfo(getApplicationContext(),Constants.SIGN, "一起运动更快乐！");
		} else {
			UserInfoUtils.setUserInfo(getApplicationContext(), Constants.SIGN,user.intro + "");
		}
		UserInfoUtils.setUserInfo(getApplicationContext(),Constants.HEADIMG_URL, user.headimgurl);
		UserInfoUtils.setUserInfo(getApplicationContext(), Constants.USERID,user.id + "");
		UserInfoUtils.setUserInfo(getApplicationContext(), Constants.MOBILE,user.mobile);
		UserInfoUtils.setUserInfo(getApplicationContext(),Constants.TOKEN, user.token);
	}
	
	/**
	 * 向服务器提交alias 以供推送使用
	 * @param alias
	 */
	public void postAliasToNet(String alias) {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", alias);
		params.put("alias", alias);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.POST_ALIAS, handler,
						POST_ALIAS, params);

			}
		});
	}
	
	private void getCode(){
		btn_getvcode.setEnabled(false);
		btn_getvcode.setBackgroundResource(R.drawable.wane_shape_getcode_grey);
		btn_getvcode.setText("");
		task = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (time <= 0) {
							btn_getvcode.setEnabled(true);
							btn_getvcode.setBackgroundResource(R.drawable.wane_shape_getcode_red);
							btn_getvcode.setText("获取验证码");
							btn_getvcode.setTextSize(14);
							btn_getvcode.setTextColor(Color.WHITE);
							task.cancel();
						} else {
							btn_getvcode.setText(time + "秒");
							btn_getvcode.setTextSize(22);
						}
						time--;
					}
				});
			}
		};
		time = 60;
		timer.schedule(task, 0, 1000);
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
				ToastUtil.show(PhoneRegisterActivity.this, "QQ授权成功，正在登录");
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
					UserInfoUtils.setUserInfo(getApplicationContext(),Constants.NICKNAME, nickname);
					UserInfoUtils.setUserInfo(getApplicationContext(),Constants.SEX, gender);
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

}
