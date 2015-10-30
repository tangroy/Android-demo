package com.oxygen.www.module.user.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;

/**
 * 设置新密码界面
 * @author yang
 * 2015-5-28下午2:33:58
 */
public class SetNewPswActivity extends BaseActivity implements OnClickListener {
	protected   final int RESET_PASSWORD = 1;
	private EditText et_oldpsw;
	private EditText et_newpsw;
	private EditText et_re_newpsw;
	private Button bt_reset_login;
	private ImageView iv_back;
	private TextView input_code_title;
	private String sid;
	private String mobile;
	private String newPsw;
	private String oldpsw;
	private String surepsw;
	private String vcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setnewpsw);

		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		input_code_title = (TextView) findViewById(R.id.input_code_title);
		et_oldpsw = (EditText) findViewById(R.id.et_oldpsw);
		et_newpsw = (EditText) findViewById(R.id.et_newpsw);
		et_re_newpsw = (EditText) findViewById(R.id.et_re_newpsw);
		bt_reset_login = (Button) findViewById(R.id.bt_reset_login);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		bt_reset_login.setOnClickListener(this);
	}

	private void initValues() {
		Intent intent = getIntent();
		mobile = intent.getStringExtra("phoneNumber");
		vcode = intent.getStringExtra("vcode");
		sid = intent.getStringExtra("sid");
		input_code_title.setText(getResources().getString(R.string.resetpsw));
		if(mobile != null){
			et_oldpsw.setText(mobile);
			et_oldpsw.setVisibility(View.GONE);
		}else{
			et_oldpsw.setHint("原密码");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.bt_reset_login:
			newPsw = et_newpsw.getText().toString().trim();
			oldpsw = et_oldpsw.getText().toString().trim();
			surepsw = et_re_newpsw.getText().toString().trim();
			if (TextUtils.isEmpty(newPsw) || TextUtils.isEmpty(surepsw)) {
				ToastUtil.show(SetNewPswActivity.this, getResources()
						.getString(R.string.psw_notempty));
				return;
			}
			if (!newPsw.equals(surepsw)) {
				ToastUtil.show(SetNewPswActivity.this, getResources()
						.getString(R.string.psw_notsame));
				return;
			}
			if(newPsw.length()<6||newPsw.length()>20){
				ToastUtil.show(SetNewPswActivity.this, getResources()
						.getString(R.string.psw_length));
				return;
			}
			setNewPsw();
			break;
		}
	}

	private void setNewPsw() {
		final Map<String, String> params = new HashMap<String, String>();
		// 修改密码
		if (getIntent().getExtras().getString("setNewPsw") != null
				&& getIntent().getExtras().getString("setNewPsw").equals("setNewPsw")) {
			params.put("old_password", oldpsw);
			params.put("new_password", newPsw);
			params.put("user_id", (String)UserInfoUtils.getUserInfo(getApplicationContext(),Constants.USERID, ""));
			// params.put("mobile", et_number.getText().toString().trim());
			OxygenApplication.cachedThreadPool.execute(new Runnable() {
				public void run() {
					HttpUtil.Post(UrlConstants.POST_RESET_PASSWORD, handler,
							RESET_PASSWORD, params);
				}
			});
			// 忘记密码
		} else {
			params.put("mobile", mobile);
			params.put("sid", sid + "");
			params.put("password", newPsw);
//			params.put("isdev", 1 + "");
			OxygenApplication.cachedThreadPool.execute(new Runnable() {
				public void run() {
					HttpUtil.Post_Noauth(UrlConstants.POST_RESET_PASSWORD,
							handler, RESET_PASSWORD, params);
				}
			});
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case RESET_PASSWORD:
				if (msg.obj == null) {
					ToastUtil.show(SetNewPswActivity.this, "网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonObject = new JSONObject();
						if (msg.obj.getClass().isInstance(jsonObject)) {
							jsonObject = (JSONObject) msg.obj;
						} else {
							jsonObject = new JSONObject((String) msg.obj);
						}
						if (!jsonObject.isNull("status")) {
							if (jsonObject.getInt("status") == 200) {
								ToastUtil
										.show(SetNewPswActivity.this, "修改密码成功");
								UserInfoUtils.setUserInfo(
										getApplicationContext(),Constants.SET_PASSWORD, newPsw);
								SetNewPswActivity.this.finish();
							} else {
								ToastUtil
										.show(SetNewPswActivity.this, "修改密码失败");
							}
						} else {
							Toast.makeText(SetNewPswActivity.this,
									jsonObject.getString("msg"),
									Toast.LENGTH_LONG).show();
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;

			default:
				break;
			}
		}
	};
}
