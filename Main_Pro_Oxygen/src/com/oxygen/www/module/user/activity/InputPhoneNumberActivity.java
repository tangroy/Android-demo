package com.oxygen.www.module.user.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
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

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;

public class InputPhoneNumberActivity extends BaseActivity implements
		OnClickListener {
	protected   final int GET_MESSAGE_SID = 1;
	private TextView find_psw_title;
	private EditText et_pnone_number;
	private Button send_code;
	private ImageView iv_back;
	private AlertDialog.Builder builder;
	private Dialog dialog;
	private String mobile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inputphonenumber);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		find_psw_title = (TextView) findViewById(R.id.find_psw_title);
		et_pnone_number = (EditText) findViewById(R.id.et_pnone_number);
		send_code = (Button) findViewById(R.id.send_code);
		iv_back = (ImageView) findViewById(R.id.iv_back);
	}

	private void initViewsEvent() {
		send_code.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}

	private void initValues() {
		find_psw_title.setText(getResources().getString(R.string.find_psw));
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.send_code:
			// intent = new Intent(this,);
			mobile = et_pnone_number.getText().toString().trim();
			if (TextUtils.isEmpty(mobile)) {
				ToastUtil.show(this,
						getResources().getString(R.string.toast_login_username));
				return;
			}
			if (!GDUtil.isMobileNO(mobile)) {
				ToastUtil.show(this,
						getResources().getString(R.string.toast_photo_error));
				return;
			}
			sendMessageDialog();
			break;
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.sendmessage_cancel:
			dialog.dismiss();
			break;
		case R.id.sendmessage_send:
			dialog.dismiss();
			getMssageSidFromNet();
			break;
		}
	}

	/**
	 * 显示发送短信的对话框
	 */
	private void sendMessageDialog() {
		View view = View.inflate(this,
				R.layout.activity_inputphonenumber_dailog, null);
		TextView sendmessage_cancel = (TextView) view.findViewById(R.id.sendmessage_cancel);
		TextView sendmessage_send = (TextView) view.findViewById(R.id.sendmessage_send);
		TextView tv_phone_number = (TextView) view.findViewById(R.id.tv_phone_number);
		tv_phone_number.setText(mobile);
		sendmessage_cancel.setOnClickListener(this);
		sendmessage_send.setOnClickListener(this);
		dialog = new Dialog(this, R.style.Theme_dialog);
		dialog.setContentView(view);
		dialog.show();
	}
	
	/**
	 * 获取Sid
	 */
	private void getMssageSidFromNet() {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("type", "reset_password");
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.POST_MESSAGE_SID, handler,
						GET_MESSAGE_SID, params);
			}
		});
	}


	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_MESSAGE_SID:
				if (msg.obj == null) {
					ToastUtil.show(InputPhoneNumberActivity.this,
							"网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonObject = (JSONObject) msg.obj;
						if (jsonObject.getInt("status") == 200) {
							JSONObject jsonData = jsonObject.getJSONObject("result");
							String sid = jsonData.getString("sid");
							Intent intent = new Intent(
									InputPhoneNumberActivity.this,
									InputMessageCodeActivity.class);
							intent.putExtra("phoneNumber", mobile);
							intent.putExtra("sid", sid);
							startActivity(intent);
							InputPhoneNumberActivity.this.finish();
						}else{
//							ToastUtil.show(InputPhoneNumberActivity.this, jsonObject.getString("msg"));
							ToastUtil.show(InputPhoneNumberActivity.this, "发送验证码失败");
							return;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	};

}
