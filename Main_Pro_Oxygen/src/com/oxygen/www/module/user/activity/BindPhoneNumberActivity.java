package com.oxygen.www.module.user.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.module.user.eventbus_entities.BindMobile;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;

import de.greenrobot.event.EventBus;

public class BindPhoneNumberActivity extends BaseActivity implements
		OnClickListener {

	protected static final int BIND_PHONE = 1;
	protected static final int SEND_SMS = 2;
	protected static final int BIND_PRECHECK = 3;
	private int time = 60;
	private Timer timer = new Timer();
	private TimerTask task;

	private ImageView iv_back;
	private EditText et_mobile, et_vcode, et_password;
	private Button btn_getvcode;
	private TextView btn_bind;
	private ProgressBar progressBar;
	protected String sid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_phonenumber);
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
		btn_bind = (TextView) findViewById(R.id.btn_bind);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		btn_getvcode.setOnClickListener(this);
		btn_bind.setOnClickListener(this);
	}

	private void initValues() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.btn_getvcode:
			sendVcode();
			break;
		case R.id.btn_bind:
			bindPhoneNumberCheck();
			break;

		default:
			break;
		}
	}

	/**
	 * 绑定手机号之前的检查操作(各种数据的合法性以及此手机号是否绑定过其他的账号)
	 */
	private void bindPhoneNumberCheck() {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("type", "mobile");
		params.put("mobile", et_mobile.getText().toString().trim());
		params.put("vcode", et_vcode.getText().toString().trim());
		params.put("sid", sid);
		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				HttpUtil.Post(UrlConstants.BIND_PRECHECK, handler,
						BIND_PRECHECK, params);
			}
		});
	}

	/**
	 * 发送验证码
	 */
	private void sendVcode() {
		if (TextUtils.isEmpty(et_mobile.getText())) {
			Toast.makeText(this,
					getResources().getString(R.string.toast_login_username),
					Toast.LENGTH_LONG).show();
		} else if (!GDUtil.isMobileNO(et_mobile.getText().toString())) {
			Toast.makeText(this,
					getResources().getString(R.string.toast_photo_error),
					Toast.LENGTH_LONG).show();
		} else {
			getCode();
			final Map<String, String> params = new HashMap<String, String>();
			params.put("mobile", et_mobile.getText().toString());
			params.put("type", "mobile_bind");
			OxygenApplication.cachedThreadPool.execute(new Runnable() { 
				public void run() {
					HttpUtil.Post_Noauth(UrlConstants.SEND_SMS, handler,
							SEND_SMS, params);
				}
			});
		}
	}

	/**
	 * 绑定手机号
	 */
	private void bindPhoneNumber() {
		if (TextUtils.isEmpty(et_mobile.getText())) {
			Toast.makeText(this,
					getResources().getString(R.string.toast_login_username),
					Toast.LENGTH_LONG).show();
		} else if (!GDUtil.isMobileNO(et_mobile.getText().toString())) {
			Toast.makeText(this,
					getResources().getString(R.string.toast_photo_error),
					Toast.LENGTH_LONG).show();
		} else if (TextUtils.isEmpty(et_vcode.getText())) {
			Toast.makeText(this,
					getResources().getString(R.string.toast_vcode_error),
					Toast.LENGTH_LONG).show();
		} else if (et_vcode.getText().toString().length() < 4) {
			Toast.makeText(this,
					getResources().getString(R.string.toast_vcode_error),
					Toast.LENGTH_LONG).show();
		} else {
			final Map<String, String> params = new HashMap<String, String>();
			params.put("type", "mobile");
			params.put("password", et_password.getText().toString());
			params.put("mobile", et_mobile.getText().toString());
			progressBar.setVisibility(View.VISIBLE);
			OxygenApplication.cachedThreadPool.execute(new Runnable() { 
				public void run() {
					HttpUtil.Post(UrlConstants.BIND_PHONE,
							handler, BIND_PHONE, params);

				}
			});
		}
	}

	/**
	 * 获取验证码倒计时的操作
	 */
	private void getCode() {
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
							setGetCodeBtnType();
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
	

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SEND_SMS:
				et_vcode.setFocusable(true);
				String str_vcode = (String) msg.obj;
				if (str_vcode != null && str_vcode.length() > 0) {
					try {
						JSONObject json_vcode = new JSONObject(str_vcode);
						if (json_vcode.getInt("status") == 200) {
							Toast.makeText(BindPhoneNumberActivity.this,
									"验证码已发送",
									Toast.LENGTH_LONG).show();
							sid = new JSONObject(json_vcode.getString("result")).getString("sid");
						} else {
							Toast.makeText(BindPhoneNumberActivity.this,
									json_vcode.getString("msg"),
									Toast.LENGTH_LONG).show();
							setGetCodeBtnType();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(BindPhoneNumberActivity.this,
							getResources().getString(R.string.errcode_wx),
							Toast.LENGTH_LONG).show();
					setGetCodeBtnType();
				}
				break;	
			case BIND_PHONE:
				progressBar.setVisibility(View.GONE);
				JSONObject jsonObject = (JSONObject) msg.obj;
				try {
					if (jsonObject.getInt("status") == 200) {
						Toast.makeText(BindPhoneNumberActivity.this,
								"绑定成功！",
								Toast.LENGTH_LONG).show();
						UserInfoUtils.setUserInfo(BindPhoneNumberActivity.this,Constants.MOBILE, et_mobile.getText().toString());
						BindPhoneNumberActivity.this.finish();
						EventBus.getDefault().post(new BindMobile());
					} else {
						Toast.makeText(BindPhoneNumberActivity.this,
								"绑定失败！",
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(BindPhoneNumberActivity.this,
							getResources().getString(R.string.errcode_wx),
							Toast.LENGTH_LONG).show();
				}
				break;
			case BIND_PRECHECK:
				if (msg.obj == null) {
					ToastUtil.show(BindPhoneNumberActivity.this, "网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonObjectCheck = (JSONObject) msg.obj;
						if (jsonObjectCheck.getInt("status") == 200) {
							JSONObject data = new JSONObject(jsonObjectCheck.getString("data"));
							//此账号是否已绑定
							if("yes".equals(data.getString("account_in_used"))){
								showBindDialog();
							}else{
								bindPhoneNumber();
							}
						}else{
							Toast.makeText(BindPhoneNumberActivity.this,
									"绑定失败！",
									Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			break;
			}
		}
	};
	
	protected void showBindDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.dialog_bind, null);
		// 对话框
		final Dialog dialog = new AlertDialog.Builder(BindPhoneNumberActivity.this)
				.create();
		dialog.show();
		dialog.getWindow().setContentView(layout);
		TextView invite_cancel = (TextView) layout
				.findViewById(R.id.invite_cancel);
		TextView invite_sure = (TextView) layout.findViewById(R.id.invite_sure);

		invite_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				setGetCodeBtnType();
			}
		});
		invite_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				bindPhoneNumber();
			}
		});
	}
}
