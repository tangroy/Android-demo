package com.oxygen.www.module.user.activity;

import java.lang.ref.WeakReference;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.media.tv.TvContract.Programs;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Account;
import com.oxygen.www.module.user.construct.AccountConstruct;
import com.oxygen.www.module.user.eventbus_entities.Withdrawa;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.NumberUtil;
import com.oxygen.www.utils.UserInfoUtils;

import de.greenrobot.event.EventBus;
/**
 * 我的钱包
 * @author 杨庆雷
 * 2015-10-21上午10:32:03
 */
public class MyWalletActivity extends BaseActivity implements OnClickListener {
	protected static final int ACCOUNT = 1;
	private MyHandler handler;
	private String total_income;
	private String balance;
	
	private ImageView iv_back;
	private TextView tv_details;
	private TextView tv_my_balance;
	private Button bt_withdraw;
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet);
		EventBus.getDefault().register(this);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		handler = new MyHandler(this);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_details = (TextView) findViewById(R.id.tv_details);
		tv_my_balance = (TextView) findViewById(R.id.tv_my_balance);
		bt_withdraw = (Button) findViewById(R.id.bt_withdraw);
		progressBar = (ProgressBar) findViewById(R.id.wallet_progressBar);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_details.setOnClickListener(this);
		bt_withdraw.setOnClickListener(this);
	}

	private void initValues() {
		progressBar.setVisibility(View.VISIBLE);
		getUserAccountInfo();
	}

	private void getUserAccountInfo() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.ACCOUNT_ACCOUNT, handler, ACCOUNT);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_details:
			Intent billIntent = new Intent(MyWalletActivity.this, BillActivity.class);
			startActivity(billIntent);
			break;
		case R.id.bt_withdraw:
			Intent withdrawIntent = new Intent(MyWalletActivity.this,
					WithdrawalsActivity.class);
			withdrawIntent.putExtra("acount_balance", balance);
			startActivity(withdrawIntent);
			break;
		default:
			break;
		}
	}

	static class MyHandler extends Handler {
		private final WeakReference<MyWalletActivity> mActivityReference;

		public MyHandler(MyWalletActivity myWalletActivity) {
			mActivityReference = new WeakReference<MyWalletActivity>(
					myWalletActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			final MyWalletActivity activity = mActivityReference.get();
			if (activity == null) {
				return;
			}
			switch (msg.what) {
			case ACCOUNT:
				String accountstr = (String) msg.obj;
				if (accountstr != null && accountstr.length() > 10) {
					JSONObject accountJson;
					try {
						accountJson = new JSONObject(accountstr);
						if (accountJson.getInt("status") == 200) {
							Account account = AccountConstruct
									.ToAccountInfo(accountJson
											.getJSONObject("data"));
							activity.setAcountInfo(account);
						}
					} catch (JSONException e) {

						e.printStackTrace();
					}
				}
			}
		}
	}

	public void setAcountInfo(Account account) {
		balance = account.getBalance();
		total_income = account.getTotal_income();
		tv_my_balance.setText("¥ " + NumberUtil.round(NumberUtil.divide(balance,"100"),2));
		UserInfoUtils.setUserInfo(this, Constants.SHOP_ID, account.getShop_id());
		UserInfoUtils.setUserInfo(this, Constants.ACCOUNT_ID,account.getAccount_id());
		progressBar.setVisibility(View.GONE);
	}
	
	@SuppressWarnings("unused")
	private void onEventMainThread(Withdrawa info) {
		initValues();
	}
}
