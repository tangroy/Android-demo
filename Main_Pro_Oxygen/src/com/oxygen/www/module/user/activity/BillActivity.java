package com.oxygen.www.module.user.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.BaseViewHolder;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.MyBaseAdapter;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.WithdrawaInfo;
import com.oxygen.www.module.user.construct.WithdrawaConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.NumberUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;

/**
 * （查看）账单
 * 
 * @author 杨庆雷 2015-8-17下午2:57:59
 */
public class BillActivity extends BaseActivity implements OnClickListener {
	protected static final int ACCOUNT_TEANSACTION = 1;
	private ImageView iv_back;
	private PullToRefreshListView lv_bill_detail;
	/**
	 * 当前页
	 */
	private int page = 1;
	/**
	 * 每页获取数据数
	 */
	private int limit = 10;
	private ListView actualListView;
	private ProgressBar progressBar;
	private List<WithdrawaInfo> allWithdrawas = new ArrayList<WithdrawaInfo>();;
	private List<WithdrawaInfo> withdrawas;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		lv_bill_detail = (PullToRefreshListView) findViewById(R.id.lv_bill_detail);
		lv_bill_detail.setMode(Mode.BOTH);
		ILoadingLayout endLabels = lv_bill_detail.getLoadingLayoutProxy(false,
				true);
		endLabels.setPullLabel("加载更多...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
		actualListView = lv_bill_detail.getRefreshableView();
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		lv_bill_detail.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新
				page = 1;
				if (allWithdrawas != null) {
					allWithdrawas.clear();
				}
				initValues();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 上拉加载更多
				page = page + 1;
				initValues();
			}
		});
	}

	private void initValues() {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("account_id", (String) UserInfoUtils.getUserInfo(
				getApplicationContext(), Constants.ACCOUNT_ID, ""));
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.ACCOUNT_TEANSACTION + "?limit=" + limit
						+ "&page=" + page, handler,
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
				progressBar.setVisibility(View.GONE);
				JSONObject jsonObject = (JSONObject) msg.obj;
				if (jsonObject != null) {
					try {
						if (jsonObject.getInt("status") == 200) {
							withdrawas = WithdrawaConstruct
									.ToWithdrawaList(jsonObject
											.getJSONArray("data"));
							if(page != 1 && withdrawas.size() == 0){
								ToastUtil.show(BillActivity.this, "没有更多明细了");
							}else{
								if(withdrawas != null){
									allWithdrawas.addAll(withdrawas);
								}
								initWithdrawaList();
							}
						} else {

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				lv_bill_detail.onRefreshComplete();
			}
		}
	};

	protected void initWithdrawaList() {
		lv_bill_detail.setAdapter(new MyBaseAdapter<WithdrawaInfo>(
				BillActivity.this.getApplicationContext(), allWithdrawas,
				R.layout.item_billdetalil) {
			@Override
			public void convert(BaseViewHolder holder,
					WithdrawaInfo withdrawaInfo) {
				TextView withdrawa_time = (TextView) holder
						.getView(R.id.bill_time);
				TextView withdrawa_amount = (TextView) holder
						.getView(R.id.bill_amount);
				TextView withdrawa_info = (TextView) holder
						.getView(R.id.bill_info);
				withdrawa_time.setText(withdrawaInfo.getCreated_at());
				if(!withdrawaInfo.getAmount().startsWith("-")){
					withdrawa_amount.setTextColor(getResources().getColor(R.color.withdraw_money));
					withdrawa_amount.setText("+"+NumberUtil.divide(
							withdrawaInfo.getAmount(), "100", 2));
				}else{
					withdrawa_amount.setTextColor(getResources().getColor(R.color.black));
					withdrawa_amount.setText(NumberUtil.divide(
							withdrawaInfo.getAmount(), "100", 2));
				}
				withdrawa_info.setText(withdrawaInfo.getMemo());
			}
		});
		actualListView.setSelection((page - 1) * 10);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}

}
