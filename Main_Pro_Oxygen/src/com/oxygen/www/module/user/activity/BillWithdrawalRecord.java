package com.oxygen.www.module.user.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseViewHolder;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.MyBaseAdapter;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.WithdrawaInfo;
import com.oxygen.www.module.user.construct.WithdrawaConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.NumberUtil;
import com.oxygen.www.utils.UserInfoUtils;

/**
 * 提现记录
 * 
 * @author 杨庆雷 2015-8-18下午2:54:36
 */
public class BillWithdrawalRecord extends BillBasePager {

	public static final int WITHDRAW_DETAIlS = 1;
	private TextView account_balance;
	private ListView lv_withdrawa;

	public BillWithdrawalRecord(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initData() {
		super.initData();
		View view = View.inflate(mContext,
				R.layout.activity_bill_withdrawalrecord, null);
		account_balance = (TextView) view.findViewById(R.id.account_balance);
		lv_withdrawa = (ListView) view.findViewById(R.id.lv_withdrawa);
		flContent.removeAllViews();
		flContent.addView(view);	
		setAcountInfo();
		getDate();
	}

	private void getDate() {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("type", "bank_card");
//		params.put("type", "wechat");
		params.put("account_id", (String) UserInfoUtils.getUserInfo(mContext,
				Constants.ACCOUNT_ID, ""));
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.WITHDRAW_DETAIlS, handler,
						WITHDRAW_DETAIlS, params);
			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case WITHDRAW_DETAIlS:
				JSONObject jsonObject = (JSONObject) msg.obj;
				if (jsonObject != null) {
					try {
						if (jsonObject.getInt("status") == 200) {
							List<WithdrawaInfo> WithdrawaInfos = WithdrawaConstruct.ToWithdrawaList(jsonObject.getJSONArray("data"));
							initWithdrawaList(WithdrawaInfos);
						} else {

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	private void setAcountInfo() {
		String balance = (String) UserInfoUtils.getUserInfo(mContext, Constants.ACCOUNT_BALANCE, "0.00");
		account_balance.setText(balance + "元");
	}

	protected void initWithdrawaList(List<WithdrawaInfo> WithdrawaInfos) {
		lv_withdrawa.setAdapter(
			new MyBaseAdapter<WithdrawaInfo>(mContext, WithdrawaInfos, R.layout.item_withdrawa) {
				@Override
				public void convert(BaseViewHolder holder, WithdrawaInfo withdrawaInfo) {
					TextView withdrawa_time = (TextView)holder.getView(R.id.withdrawa_time);
					TextView withdrawa_amount = (TextView)holder.getView(R.id.withdrawa_amount);
					withdrawa_time.setText(withdrawaInfo.getCreated_at());
					withdrawa_amount.setText(NumberUtil.divide(withdrawaInfo.getAmount(), "100", 2));
				}
				
			});
	}
}
