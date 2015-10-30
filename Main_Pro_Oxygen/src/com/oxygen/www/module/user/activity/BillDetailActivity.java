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
 * 账单明细
 * 
 * @author 杨庆雷 2015-8-18下午2:54:22
 */
public class BillDetailActivity extends BillBasePager {

	public static final int ACCOUNT_TEANSACTION = 1;
	private TextView account_balance;
	private ListView lv_bill_detail;
	public BillDetailActivity(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initData() {
		super.initData();
		View view = View.inflate(mContext, R.layout.activity_bill_detail, null);
		account_balance = (TextView) view.findViewById(R.id.account_balance);
		lv_bill_detail = (ListView) view.findViewById(R.id.lv_bill_detail);
		flContent.removeAllViews();
		flContent.addView(view);
		setAcountInfo();
		getDate();
	}

	private void getDate() {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("account_id", (String) UserInfoUtils.getUserInfo(mContext,
				Constants.ACCOUNT_ID, ""));
		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				HttpUtil.Post(UrlConstants.ACCOUNT_TEANSACTION, handler,
						ACCOUNT_TEANSACTION, params);
			}
		});
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {	
			case ACCOUNT_TEANSACTION:
				JSONObject jsonObject = (JSONObject) msg.obj;
				if(jsonObject != null){
					try {
						if (jsonObject.getInt("status") == 200) {
							List<WithdrawaInfo> WithdrawaInfos = WithdrawaConstruct.ToWithdrawaList(jsonObject.getJSONArray("data"));
							initWithdrawaList(WithdrawaInfos);
						}else{
							
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
	
	protected void initWithdrawaList(List<WithdrawaInfo> withdrawaInfos) {
		lv_bill_detail.setAdapter(
			new MyBaseAdapter<WithdrawaInfo>(mContext, withdrawaInfos, R.layout.item_billdetalil) {
				@Override
				public void convert(BaseViewHolder holder, WithdrawaInfo withdrawaInfo) {
					TextView withdrawa_time = (TextView)holder.getView(R.id.bill_time);
					TextView withdrawa_amount = (TextView)holder.getView(R.id.bill_amount);
					TextView withdrawa_info = (TextView)holder.getView(R.id.bill_info);
					withdrawa_time.setText(withdrawaInfo.getCreated_at());
					withdrawa_amount.setText(NumberUtil.divide(withdrawaInfo.getAmount(), "100", 2));
					withdrawa_info.setText(withdrawaInfo.getMemo());
				}
				
			});
	}
}
