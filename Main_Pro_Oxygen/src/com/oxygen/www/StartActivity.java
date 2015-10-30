package com.oxygen.www;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.jpush.android.api.JPushInterface;

import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.MenuActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.module.challengs.activity.ChallengesDetailActivity;
import com.oxygen.www.module.find.activity.PostsDetailActivity;
import com.oxygen.www.module.sport.activity.EventsResultActivity;
import com.oxygen.www.module.team.activity.GroupDetailActivity;
import com.oxygen.www.module.user.activity.LoginActivity;
import com.oxygen.www.module.user.activity.UserInfoActivity;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;

public class StartActivity extends Activity {

	/**
	 * 获取 post_token
	 */
	private static final int NET_GETPOSTS = 1;
	/**
	 * post_id
	 */
	private String post_id;
	/**
	 * 加载中
	 */
	private RelativeLayout rl_loading;

	private List<Integer> guidePictureIds;
	private List<View> guidePictures;
	private RelativeLayout appname;
	private ViewPager vp_guide;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);

		// 初始化腾讯云统计
		initMTA();
		// 是否是第一次安装
		isFirstInstall();

	}

	/**
	 * 获取服务器post的token
	 */
	private void getpostsInNet(final int post_id) {
		rl_loading.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.POSTS_LIST_GET_URL2 + post_id
						+ ".json", handler, NET_GETPOSTS);
			}
		});

	}

	private void initMTA() {
		try {
			android.os.Debug.startMethodTracing("MTA");
			StatConfig.setDebugEnable(true);
			java.util.UUID.randomUUID();
			StatService.startStatService(this, Constants.MTA_APP_KEY,
					com.tencent.stat.common.StatConstants.VERSION);
		} catch (MtaSDkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 判断是不是第一次启动
	 */
	private void isFirstInstall() {
		boolean installFlag = (Boolean) UserInfoUtils.getUserInfo(
				getApplicationContext(), Constants.FIRST_INSTALL, true);
		if (installFlag) {
			vp_guide = (ViewPager) findViewById(R.id.vp_guide);
			vp_guide.setVisibility(View.VISIBLE);
			guidePictureIds = new ArrayList<Integer>();
			guidePictures = new ArrayList<View>();
			guidePictureIds.add(R.drawable.guide_1);
			guidePictureIds.add(R.drawable.guide_2);
			guidePictureIds.add(R.drawable.guide_3);
			guidePictureIds.add(R.drawable.guide_4);
			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			for (int i = 0; i < guidePictureIds.size(); i++) {
				ImageView iv = new ImageView(this);
				iv.setLayoutParams(mParams);
				iv.setBackgroundColor(getResources().getColor(R.color.white));
				iv.setScaleType(ImageView.ScaleType.FIT_XY);
				iv.setImageDrawable(StartActivity.this.getResources().getDrawable(guidePictureIds.get(i)));
				guidePictures.add(iv);
			}
			MyPageAdapter adapter = new MyPageAdapter();
			vp_guide.setAdapter(adapter);
		} else {
			appname = (RelativeLayout) findViewById(R.id.appname);
			appname.setVisibility(View.VISIBLE);
			MyCount mc = new MyCount(3000, 1000);
			mc.start();

		}
	}

	class MyPageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return guidePictures != null && guidePictures.size() > 0 ? guidePictures
					.size() : 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(guidePictures.get(position));
			if (3 == position) {
				guidePictures.get(position).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(StartActivity.this,
										LoginActivity.class);
								startActivity(intent);
								UserInfoUtils.setUserInfo(StartActivity.this,
										Constants.FIRST_INSTALL, false);
								finish();
							}
						});
			}
			return guidePictures.get(position);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			// 是否以前登录过
			if (TextUtils.isEmpty((String) UserInfoUtils.getUserInfo(
					StartActivity.this.getApplicationContext(),
					Constants.USERID, ""))
					|| TextUtils.isEmpty((String) UserInfoUtils.getUserInfo(
							StartActivity.this.getApplicationContext(),
							Constants.TOKEN, ""))) {

				Intent intent;
				// 用户信息是否完整
				// if(!UserInfoUtils.userInfoIsComoplete(StartActivity.this)){
				// intent = new Intent(StartActivity.this,
				// NewDataActivity.class);
				// }else{
				intent = new Intent(StartActivity.this, LoginActivity.class);
				// }
				startActivity(intent);
				finish();
			} else {
				// 自定义 URL scheme
				Intent i_getvalue = getIntent();
				String action = i_getvalue.getAction();

				// Log.i("scheme", "scheme: "+i_getvalue.getScheme());
				// Log.i("scheme", "action: "+action);
				// Log.i("scheme", "dataString: "+i_getvalue.getDataString());
				if (Intent.ACTION_VIEW.equals(action)) {
					// 浏览器打开
					// 拉起 App
					laQiApp(i_getvalue);

				} else {
					// 正常启动
					Intent intent;
					// if(!UserInfoUtils.userInfoIsComoplete(StartActivity.this)){
					// intent = new Intent(StartActivity.this,
					// NewDataActivity.class);
					// }else{
					intent = new Intent(StartActivity.this, MenuActivity.class);
					// }
					startActivity(intent);
					finish();
				}

			}

		}

		@Override
		public void onTick(long arg0) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * 自定义 URL scheme 拉起 App 相关页面
	 * 
	 * @param i_getvalue
	 */
	private void laQiApp(Intent i_getvalue) {
		Uri uri = i_getvalue.getData();
		if (uri != null) {

			if (i_getvalue.getDataString().contains("event")) {
				// 活动
				// String eventid = uri.getQueryParameter("event_id");
				String dataString = i_getvalue.getDataString();
				String eventid = dataString.substring(dataString
						.lastIndexOf("/") + 1);
				Log.i("scheme", "eventid:" + eventid);

				if (!TextUtils.isEmpty(eventid)) {

					Intent intent = new Intent(StartActivity.this,
							EventsResultActivity.class);
					intent.putExtra("eventid", Integer.parseInt(eventid));
					startActivity(intent);
					finish();

				}

			} else if (i_getvalue.getDataString().contains("group")) {
				// 团队
				// String group_id = uri.getQueryParameter("group_id");
				String dataString = i_getvalue.getDataString();
				String group_id = dataString.substring(dataString
						.lastIndexOf("/") + 1);
				Log.i("scheme", "group_id:" + group_id);
				if (!TextUtils.isEmpty(group_id)) {

					Intent intent = new Intent(StartActivity.this,
							GroupDetailActivity.class);
					intent.putExtra("groupid", Integer.parseInt(group_id));
					startActivity(intent);
					finish();

				}
			} else if (i_getvalue.getDataString().contains("user")) {
				// 用户
				// String user_id = uri.getQueryParameter("user_id");
				String dataString = i_getvalue.getDataString();
				String user_id = dataString.substring(dataString
						.lastIndexOf("/") + 1);
				Log.i("scheme", "user_id:" + user_id);
				if (!TextUtils.isEmpty(user_id)) {

					Intent intent = new Intent(StartActivity.this,
							UserInfoActivity.class);
					intent.putExtra("userid", user_id);
					// intent.putExtra("userid", "4953");
					startActivity(intent);
					finish();

				}
			} else if (i_getvalue.getDataString().contains("challenge")) {
				// 挑战
				// String challenge_id = uri.getQueryParameter("challenge_id");
				String dataString = i_getvalue.getDataString();
				String challenge_id = dataString.substring(dataString
						.lastIndexOf("/") + 1);
				Log.i("scheme", "challenge_id:" + challenge_id);
				if (!TextUtils.isEmpty(challenge_id)) {

					Intent intent = new Intent(StartActivity.this,
							ChallengesDetailActivity.class);
					intent.putExtra("challengesid",
							Integer.parseInt(challenge_id));
					startActivity(intent);
					finish();

				}
			} else if (i_getvalue.getDataString().contains("post")) {
				// 精选
				// String post_id = uri.getQueryParameter("post_id");
				String dataString = i_getvalue.getDataString();
				post_id = dataString.substring(dataString.lastIndexOf("/") + 1);
				if (!TextUtils.isEmpty(post_id)) {
					// 获取 post_token
					getpostsInNet(Integer.parseInt(post_id));
				}

			}

		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case NET_GETPOSTS:
				String strObject = (String) msg.obj;
				rl_loading.setVisibility(View.GONE);
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsonenlist = new JSONObject(strObject);
						if (jsonenlist.getInt("status") == 200) {
							Log.i("scheme", strObject);
							JSONObject data = jsonenlist.getJSONObject("data");
							if (data != null) {
								String token = data.getString("token");
								Log.i("scheme", "token:" + token);
								if (!TextUtils.isEmpty(token)) {

									Intent intent = new Intent(
											StartActivity.this,
											PostsDetailActivity.class);
									intent.putExtra("posts_id",
											Integer.parseInt(post_id));
									intent.putExtra("posts_token", token);
									startActivity(intent);
									finish();

								}
							}

						} else {
							ToastUtil.show(StartActivity.this,
									jsonenlist.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(StartActivity.this, "网络连接不可用，请稍后重试");
				}
				break;
			default:
				break;
			}
		}

	};

}
