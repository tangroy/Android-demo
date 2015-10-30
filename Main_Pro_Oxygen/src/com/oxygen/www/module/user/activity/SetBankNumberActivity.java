package com.oxygen.www.module.user.activity;

import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;

/**
 * 设置收款账号(银行卡)
 * 
 * @author 杨庆雷 2015-8-18上午10:59:22
 */
public class SetBankNumberActivity extends BaseActivity implements
		OnClickListener {
	protected static final int SET_ACCOUNT_INFO = 1;
	private ImageView iv_back;
	private EditText account_name;
	private EditText bank_number;
	private EditText bank_name;
	private TextView tv_save;
	private String balance;
	private MyHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setbank_number);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_save = (TextView) findViewById(R.id.tv_save);
		account_name = (EditText) findViewById(R.id.account_name);
		bank_number = (EditText) findViewById(R.id.bank_number);
		bank_name = (EditText) findViewById(R.id.bank_name);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_save.setOnClickListener(this);
	}

	private void initValues() {
		handler = new MyHandler(this);
		balance = getIntent().getExtras().getString("acount_balance");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.tv_save:
			setWithDrawalBank();
			break;

		default:
			break;
		}
	}

	/**
	 * 设置提现银行卡信息
	 */
	private void setWithDrawalBank() {
		final Map<String, String> params = new HashMap<String, String>();
		String bank = account_name.getText().toString().trim();
		String bank_no = bank_number.getText().toString().trim();
		String realname = bank_name.getText().toString().trim();
		params.put("shop_id", (String)UserInfoUtils.getUserInfo(this,Constants.SHOP_ID, ""));
		params.put("balance", balance);
		params.put("bank", bank);
		params.put("bank_no", bank_no);
		params.put("realname", realname);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.SET_ACCOUNT_INFO, handler, SET_ACCOUNT_INFO, params);
			}
		});
	}
	
	static class MyHandler extends Handler {
		private final WeakReference<SetBankNumberActivity> mActivityReference;

		public MyHandler(SetBankNumberActivity myWalletActivity) {
			mActivityReference = new WeakReference<SetBankNumberActivity>(
					myWalletActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			final SetBankNumberActivity activity = mActivityReference.get();
			if (activity == null) {
				return;
			}
			switch (msg.what) {
			case SET_ACCOUNT_INFO:
				JSONObject jsonObject = (JSONObject) msg.obj;
				if(jsonObject != null){
					try {
						if (jsonObject.getInt("status") == 200) {
							ToastUtil.show(activity,"设置收款账户成功");
							activity.finish();
						}else{
							String failureMsg = jsonObject.getString("msg");
							ToastUtil.show(activity,URLDecoder.decode(failureMsg, "utf-8"));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
