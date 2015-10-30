package com.oxygen.www.module.user.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Account;
import com.oxygen.www.module.user.construct.AccountConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.NumberUtil;
import com.oxygen.www.utils.UserInfoUtils;

/**
 * 账单总览界面
 * @author 杨庆雷
 * 2015-8-18下午2:54:04
 */
public class BillOverViewActivity extends BillBasePager {

	public static final int ACCOUNT = 1;
	private TextView account_balance;
	private ListView lv_bill_overview;
	public BillOverViewActivity(Context context) {
		super(context);
	}
	
	@Override
	public void initData() {
		super.initData();
		View view = View.inflate(mContext, R.layout.activity_bill_overview, null);
		account_balance = (TextView) view.findViewById(R.id.account_balance);
		lv_bill_overview = (ListView) view.findViewById(R.id.lv_bill_overview);
		flContent.removeAllViews();
		flContent.addView(view);
		account_balance.setText((String)UserInfoUtils.getUserInfo(mContext, Constants.ACCOUNT_BALANCE, "0.00") + "元");
		getDate();
	}
	
	private void getDate() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				HttpUtil.Get(UrlConstants.ACCOUNT_ACCOUNT, handler,
						ACCOUNT);
			}
		});
	}

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case ACCOUNT:
				String accountstr = (String) msg.obj;
				if (accountstr != null && accountstr.length() > 10) {
					JSONObject accountJson;
					try {
						accountJson = new JSONObject(accountstr);
						if(accountJson.getInt("status") == 200){
							Account account = AccountConstruct.ToAccountInfo(accountJson.getJSONObject("data"));
							setAcountInfo(account);
						}
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	private void setAcountInfo(Account account){
		String balance = NumberUtil.round(NumberUtil.divide(account.getBalance(),"100"),2);
		account_balance.setText(balance + "元");
		UserInfoUtils.setUserInfo(mContext, Constants.ACCOUNT_BALANCE, balance);
	}
}
