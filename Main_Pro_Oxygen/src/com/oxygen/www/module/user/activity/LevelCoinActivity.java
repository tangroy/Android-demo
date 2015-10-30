package com.oxygen.www.module.user.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.BaseViewHolder;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.MyBaseAdapter;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.LevelInfo;
import com.oxygen.www.enties.Task;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.activity.WebViewActivity;
import com.oxygen.www.module.user.adapter.LevelCoinAdapter;
import com.oxygen.www.module.user.construct.LevelInfoConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.widget.NoScrollListView;

/**
 * 等级金币界面
 * 
 * @author 杨庆雷 2015-8-3下午2:23:20
 */
public class LevelCoinActivity extends BaseActivity implements OnClickListener {
	protected static final int LEVEL = 1;
	private LevelInfo levelInfo;
	private ImageView iv_back;
	private ImageView level_coin_info,iv_web_description;
	private TextView tv_mylevel;
	private TextView tv_myscore;
	private TextView tv_mycoins;
	private TextView tv_getcoin;
	private TextView missions_completed;
	private NoScrollListView lv_missions;
	private MyHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_levelcoin);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		handler = new MyHandler(this);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		level_coin_info = (ImageView) findViewById(R.id.level_coin_info);
		tv_mylevel = (TextView) findViewById(R.id.tv_mylevel);
		tv_myscore = (TextView) findViewById(R.id.tv_myscore);
		tv_mycoins = (TextView) findViewById(R.id.tv_mycoins);
		tv_getcoin = (TextView) findViewById(R.id.tv_getcoin);
		missions_completed = (TextView) findViewById(R.id.missions_completed);
		lv_missions = (NoScrollListView) findViewById(R.id.lv_missions);
		iv_web_description = (ImageView) findViewById(R.id.iv_web_description);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		level_coin_info.setOnClickListener(this);
		iv_web_description.setOnClickListener(this);
	}

	private void initValues() {
		getLevelCoinInfo();
	}

	private void getLevelCoinInfo() {
		final String userId = (String)UserInfoUtils.getUserInfo(getApplicationContext(),Constants.USERID, "");
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.LEVEL + "?user_id=" + userId,
						handler, LEVEL);
			}
		});
	}
	
	static class MyHandler extends Handler {
		private final WeakReference<LevelCoinActivity> mActivityReference;

		public MyHandler(LevelCoinActivity activity) {
			mActivityReference = new WeakReference<LevelCoinActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			final LevelCoinActivity activity = mActivityReference.get();
			if(activity == null){
				return;
			}
			switch (msg.what) {
			case LEVEL:
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					JSONObject jsonenlist;
					try {
						jsonenlist = new JSONObject(strObject);
						if (jsonenlist.getInt("status") == 200) {
							activity.levelInfo = LevelInfoConstruct
									.TolevelInfo(jsonenlist
											.getJSONObject("data"));
							activity.updateUi();
						} else {
							ToastUtil.show(activity,
									jsonenlist.getString("msg"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.level_coin_info:
			Intent intent = new Intent(this,LevelCoinInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_web_description:
			Intent intent_w = new Intent(this,WebViewActivity.class);
			intent_w.putExtra("target_url", UrlConstants.AWSTATS);
			intent_w.putExtra("hasTitle", true);
			intent_w.putExtra("title", "金币兑换说明");
			startActivity(intent_w);
			break;

		default:
			break;
		}
	}

	/**
	 * 更新界面
	 */
	protected void updateUi() {
		// 让scrollView中的一个控件 获取焦点 这样scrollView就能自动显示在最顶端(下面三行代码)
		iv_back.setFocusable(true);
		iv_back.setFocusableInTouchMode(true);
		iv_back.requestFocus();
		
		User user = levelInfo.getUser();
		int totalCoins = levelInfo.getTotal_coins();
		int completes = levelInfo.getCompleted();
		int totals = levelInfo.getTotal();
		String points = user.getPoints();
		String level = user.getLevel();
		String coins = user.getCoins();
		tv_mylevel.setText("LV" + level);
		tv_myscore.setText(points + "积分");
		tv_mycoins.setText(coins);
		tv_getcoin.setText("+" + totalCoins);
		missions_completed.setText("每日任务(" + completes + "/" + totals + ")");
		List<Task> tasks = levelInfo.getTasks();
		LevelCoinAdapter adapter = new LevelCoinAdapter(getApplicationContext(), tasks);
		lv_missions.setAdapter(adapter);
//		lv_missions.setAdapter(new MyBaseAdapter<Task>(getApplication(), tasks, R.layout.item_levelcoin) {
//			@Override
//			public void convert(BaseViewHolder holder, Task t) {
//				ImageView task_img = (ImageView)holder.getView(R.id.task_img);
//				TextView task_name = (TextView)holder.getView(R.id.task_name);
//				TextView task_coin = (TextView)holder.getView(R.id.task_coin);
//				task_name.setText(t.getTitle());
//				String imgUrl = UrlConstants.API_PREFIX+"/m/app/public/images/mylevel/"+"mylevel"+t.getAction().replace("/", "_")+".png";
//				ImageUtil.showImage(imgUrl, task_img, R.drawable.icon_def);
////				bitmapUtils.display(task_img, imgUrl);
//				task_coin.setText("+"+t.getCoins());
//				if("yes".equals(t.getCompleted())){
//					task_img.setImageDrawable(getResources().getDrawable(R.drawable.icon_task_copleted));
//					task_name.setTextColor(getResources().getColor(R.color.mygray));
//				}else{
//					task_name.setTextColor(getResources().getColor(R.color.black));
//				}
//			}
//		});
	}
}
