package com.oxygen.www.module.user.activity;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.module.user.eventbus_entities.Withdrawa;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.NumberUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.utils.ViewUtil;
import de.greenrobot.event.EventBus;

/**
 * 我要提现
 * 
 * @author 杨庆雷 2015-8-17下午2:58:20
 */
public class WithdrawalsActivity extends BaseActivity implements
		OnClickListener {
	protected static final int ACCOUNT_WITHDRAWAL = 1;
	private ImageView iv_back;
	private TextView account_balance;
	private TextView tv_bindwx;
	private TextView withdrawal_info;
	private TextView tv_finish_time;
	private EditText et_withdrawaname;
	private EditText et_withdrawaamount;
	private Button button_withdrawal;
	private RelativeLayout rl_prompt;
	private String balance;
	private TimerTask task;
	private int time = 5;
	private Timer timer = new Timer();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdrawals);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_withdrawaname = (EditText) findViewById(R.id.et_withdrawaname);
		et_withdrawaamount = (EditText) findViewById(R.id.et_withdrawaamount);
		account_balance = (TextView) findViewById(R.id.account_balance);
		tv_bindwx = (TextView) findViewById(R.id.tv_bindwx);
		withdrawal_info = (TextView) findViewById(R.id.withdrawal_info);
		tv_finish_time = (TextView) findViewById(R.id.tv_finish_time);
		button_withdrawal = (Button) findViewById(R.id.button_withdrawal);
		rl_prompt = (RelativeLayout) findViewById(R.id.rl_prompt);

	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_bindwx.setOnClickListener(this);
		button_withdrawal.setOnClickListener(this);
		rl_prompt.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}

	private void initValues() {
		balance = getIntent().getExtras().getString("acount_balance");
		account_balance.setText("账户余额：共" + NumberUtil.divide(balance, "100", 2)
				+ "元");
		String unionId = (String) UserInfoUtils.getUserInfo(
				getApplicationContext(), Constants.UNIONID, "");
		if (TextUtils.isEmpty(unionId)) {
			tv_bindwx.setVisibility(View.VISIBLE);
		} else {
			tv_bindwx.setVisibility(View.GONE);
			withdrawal_info.setText("  微信账号已绑定，提现金额会直接转入微信钱包");
			ViewUtil.setTextViewLeftDrawable(
					getResources().getDrawable(R.drawable.icon_withdraw_bined),
					withdrawal_info);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_bindwx:
			Intent intent = new Intent(this, BindActivity.class);
			startActivity(intent);
			break;
		case R.id.button_withdrawal:
			iWantWithdrawal();
			break;

		default:
			break;
		}
	}

	/**
	 * 提现
	 */
	private void iWantWithdrawal() {
		final Map<String, String> params = new HashMap<String, String>();
		String money = et_withdrawaamount.getText().toString().trim();
		String name = et_withdrawaname.getText().toString().trim();
		if (TextUtils.isEmpty(name)) {
			ToastUtil.show(this, "真实姓名不能为空");
			return;
		}
		if (TextUtils.isEmpty(money)) {
			ToastUtil.show(this, "请输入提现金额");
			return;
		}
		try {
			Float.parseFloat(money);
		} catch (NumberFormatException e) {
			ToastUtil.show(this, "提现金额输入不正确");
			return;
		}
		if (Float.parseFloat(money) < 10) {
			ToastUtil.show(this, "提现金额不小于10");
			return;
		}
		if (Float.parseFloat(money) * 100 > Float.parseFloat(balance)) {
			ToastUtil.show(this, "提现金额不能超出余额");
			return;
		}
		money = NumberUtil.divide(money, "0.01",0);
		params.put("amount", money);
		params.put("realname", name);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.ACCOUNT_WITHDRAWAL, handler,
						ACCOUNT_WITHDRAWAL, params);
			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ACCOUNT_WITHDRAWAL:
				JSONObject jsonObject = (JSONObject) msg.obj;
				if (jsonObject != null) {
					try {
						if (jsonObject.getInt("status") == 200) {
							ToastUtil.show(WithdrawalsActivity.this, "提现成功");
							EventBus.getDefault().post(new Withdrawa());
							rl_prompt.setVisibility(View.VISIBLE);
							prompt();
						} else {
							String failureMsg = jsonObject.getString("msg");
							ToastUtil.show(WithdrawalsActivity.this,
									URLDecoder.decode(failureMsg, "utf-8"));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	};

	public void prompt() {
		task = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (time <= 0) {
							WithdrawalsActivity.this.finish();
							task.cancel();
						} else {
							tv_finish_time.setText(time + "秒后自动返回我的钱包界面");
						}
						time--;
					}
				});
			}
		};
		time = 5;
		timer.schedule(task, 0, 1000);
	}
}
