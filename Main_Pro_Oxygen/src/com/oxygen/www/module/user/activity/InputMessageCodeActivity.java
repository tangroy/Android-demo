package com.oxygen.www.module.user.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;

/**
 * 找回密码 输入验证码界面
 * @author 杨庆雷
 * 2015-7-7上午10:39:35
 */
public class InputMessageCodeActivity extends BaseActivity implements OnClickListener {
	protected   final int VERIFICATE_SID = 1;
	private TextView input_code_title;
	private ImageView iv_back;
	private EditText et_code;
	private TextView tv_number;
	private Button verificate;
	private String mobile;
	private String sid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inputmessagecode);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		input_code_title = (TextView) findViewById(R.id.input_code_title);
		tv_number = (TextView) findViewById(R.id.tv_number);
		et_code = (EditText) findViewById(R.id.et_code);
		verificate = (Button) findViewById(R.id.verificate);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		verificate.setOnClickListener(this);
	}

	private void initValues() {
		Intent intent = getIntent();
		mobile = intent.getStringExtra("phoneNumber");
		sid = intent.getStringExtra("sid");
		tv_number.setText(mobile);
		input_code_title.setText(getResources().getString(R.string.find_psw));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.verificate:
			verrifySid();
			break;
		}
	}

	/**
	 * 验证手机号码 、sid、验证码
	 */
	private void verrifySid() {
		vcode = et_code.getText().toString().trim();
		final Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("sid", sid);
		params.put("vcode", vcode);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.POST_VERDITY_VCODE, handler,
						VERIFICATE_SID, params);
			}
		});
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case VERIFICATE_SID:
				if (msg.obj == null) {
					ToastUtil.show(InputMessageCodeActivity.this,
							"网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonObject = (JSONObject) msg.obj;
						if (jsonObject.getInt("status") == 200) {
							Intent intent = new Intent(InputMessageCodeActivity.this,SetNewPswActivity.class);
							intent.putExtra("phoneNumber", mobile);
							intent.putExtra("sid", sid);
							intent.putExtra("vcode", vcode);
							startActivity(intent);
							InputMessageCodeActivity.this.finish();
						}else{
							ToastUtil.show(InputMessageCodeActivity.this, jsonObject.getString("msg"));
							return;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;

			default:
				break;
			}
		}
		
	};
	private String vcode;
}
