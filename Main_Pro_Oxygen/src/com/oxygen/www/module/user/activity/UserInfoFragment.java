package com.oxygen.www.module.user.activity;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseViewHolder;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.MyBaseAdapter;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Chart;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.widget.CircleImageView;
import com.oxygen.www.widget.DountChartView;
import com.oxygen.www.widget.NoScrollListView;

/**
 * 个人资料界面
 * 
 * @author 杨庆雷 2015-6-24上午11:09:28
 */
public class UserInfoFragment extends Fragment implements OnClickListener {
	protected final int USERS_MY_INFO = 1;
	protected final int ADD_FRIEND = 2;
	protected final int REMOVE_FRIEND = 3;
	protected final int ADD_FRIEND_ACCEPT = 4;
	private ImageView modify_user_info;
	private TextView tv_name;
	private String nickname;
	private String headImgUrl;
	private String sex;
	private String age;
	private CircleImageView user_headimg;
	private ImageView user_sex;
	private TextView user_age;
	private LinearLayout ll_sports;
	private LinearLayout rl_level_coin;
	private TextView user_nickname;
	private TextView tv_level;
	private TextView tv_coin;
	private TextView tv_user_info;
	private RelativeLayout rl_user_friends;
	private RelativeLayout rl_user_collections;
	private RelativeLayout rl_user_wallet;
	private TextView sport_time_total;
	private DountChartView data_dountchart;
	private NoScrollListView lv_chartdata;
	private RadioButton sport_level_1;
	private RadioButton sport_level_2;
	private RadioButton sport_level_3;
	private TextView sport_no_data;
	private User user;
	private View rootView;
	private Context context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != rootView) {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (null != parent) {
				parent.removeView(rootView);
			}
		} else {
			rootView = inflater.inflate(R.layout.activity_userinfonew, null);
		}
		initViews();// 控件初始化
		initViewsEvent();
		initValues();
		return rootView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				HttpUtil.Get(UrlConstants.USERS_MY_INFO,
						handler, USERS_MY_INFO);
			}
		});
	}

	private void initViews() {
		context = getActivity();
		modify_user_info = (ImageView) rootView.findViewById(R.id.modify_user_info);
		tv_name = (TextView) rootView.findViewById(R.id.tv_name);
		user_headimg = (CircleImageView) rootView.findViewById(R.id.user_headimg);
		user_sex = (ImageView) rootView.findViewById(R.id.user_sex);
		user_age = (TextView) rootView.findViewById(R.id.user_age);
		ll_sports = (LinearLayout) rootView.findViewById(R.id.ll_sports);
		rl_level_coin = (LinearLayout) rootView.findViewById(R.id.rl_level_coin);
		user_nickname = (TextView) rootView.findViewById(R.id.user_nickname);
		tv_level = (TextView) rootView.findViewById(R.id.tv_level);
		tv_coin = (TextView) rootView.findViewById(R.id.tv_coin);
		tv_user_info = (TextView) rootView.findViewById(R.id.tv_user_info);
		rl_user_friends = (RelativeLayout) rootView.findViewById(R.id.rl_user_friends);
		rl_user_collections = (RelativeLayout) rootView.findViewById(R.id.rl_user_collections);
		rl_user_wallet = (RelativeLayout) rootView.findViewById(R.id.rl_user_wallet);
		
		data_dountchart = (DountChartView) rootView.findViewById(R.id.data_dountchart);
		sport_time_total = (TextView) rootView.findViewById(R.id.sport_time_total);
		lv_chartdata = (NoScrollListView) rootView.findViewById(R.id.lv_chartdata);
		sport_level_1 = (RadioButton) rootView.findViewById(R.id.sport_level_1);
		sport_level_2 = (RadioButton) rootView.findViewById(R.id.sport_level_2);
		sport_level_3 = (RadioButton) rootView.findViewById(R.id.sport_level_3);
		sport_no_data = (TextView) rootView.findViewById(R.id.sport_no_data);
	}
	private void initViewsEvent() {
		modify_user_info.setOnClickListener(this);
		rl_user_friends.setOnClickListener(this);
		rl_user_collections.setOnClickListener(this);
		rl_user_wallet.setOnClickListener(this);
		rl_level_coin.setOnClickListener(this);
		sport_level_1.setClickable(false);
		sport_level_2.setClickable(false);
		sport_level_3.setClickable(false);
	}

	private void initValues() {
		// 让scrollView中的一个控件 获取焦点 这样scrollView就能自动显示在最顶端(下面三行)
		user_headimg.setFocusable(true);
		user_headimg.setFocusableInTouchMode(true);
		user_headimg.requestFocus();
		getUserInfoFromNet();
	}

	/**
	 * 获取个人资料信息
	 */
	private void getUserInfoFromNet() {
		getCach();
		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				HttpUtil.Get(UrlConstants.USERS_MY_INFO,
						handler, USERS_MY_INFO);
			}
		});
	}

	/**
	 * 解析用户信息
	 */
	private void getUserInfo() {
		UserInfoUtils.setUserInfo(context.getApplicationContext(),Constants.LEVEL, user.level);
		UserInfoUtils.setUserInfo(context.getApplicationContext(),Constants.COINS, user.coins);
		UserInfoUtils.setUserInfo(context.getApplicationContext(), Constants.POINTS,user.points);
		headImgUrl = user.getHeadimgurl();
		nickname = user.getNickname();
		sex = user.getSex() + "";
		age = user.getAge() + "";
		setUserInfo();
		setChartdata();
	}

	private void setChartdata() {
		List<Chart> charts = user.getPie_charts();
		data_dountchart.setMonth(user.getPeriod());
		//本月运动数据不为空
		if(charts != null){
			sport_no_data.setVisibility(View.GONE);
			float percents[] = new float[charts.size()];
			int times[] = new int[charts.size()];
			int sports[] = new int[charts.size()];
			int colors[] = new int[charts.size()];
			for (int i = 0; i < charts.size(); i++) {
				percents[i] = (charts.get(i).getPercentage());
				times[i] = (charts.get(i).getTimes());
				sports[i] = GDUtil.drawableId("icon_index_"+(charts.get(i).getSport()));
				colors[i] = GDUtil.getColorId(charts.get(i).getSport());
			}
			data_dountchart.setSports(sports);
			data_dountchart.setColors(colors);
			data_dountchart.setPersents(percents);
			lv_chartdata.setAdapter(new MyBaseAdapter<Chart>(context, user.getPie_charts(), R.layout.item_chartdata) {
				@Override
				public void convert(BaseViewHolder holder, Chart chart) {
					View sport_color = (View)holder.getView(R.id.sport_color);
					TextView tv_sport_name = (TextView)holder.getView(R.id.tv_sport_name);
					TextView tv_sport_duration = (TextView)holder.getView(R.id.tv_sport_duration);
					TextView tv_sport_times = (TextView)holder.getView(R.id.tv_sport_times);
					sport_color.setBackgroundResource(GDUtil.getColorId(chart.getSport()));
					tv_sport_name.setText(GDUtil.engforchn(context, "created_type_" + chart.getSport()));
					tv_sport_duration.setText(chart.getTime());
					tv_sport_times.setText(chart.getTimes()+"次");
				}
			});
		}else{
			sport_no_data.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置用户的信息
	 */
	private void setUserInfo() {
		tv_name.setText("我的");
		user_nickname.setText(nickname);
		ImageUtil.showImage(headImgUrl, user_headimg,
				R.drawable.icon_default_head);
		if ("1".equals(sex)) {
			user_sex.setBackgroundResource(R.drawable.user_sex_man);
		} else if ("2".equals(sex)) {
			user_sex.setBackgroundResource(R.drawable.user_sex_woman);
		}
		user_age.setText(age);
		tv_level.setText("LV"+user.getLevel());
		tv_coin.setText("  "+user.getCoins()+"  ");
		tv_user_info.setText(user.getIntro());
		sport_time_total.setText(user.getTotal_duration_format());
		int hours = user.getTotal_duration_hour();
		if(hours < 10){
			sport_level_1.setChecked(true);
		}else if(hours < 20){
			sport_level_2.setChecked(true);
		}else{
			sport_level_3.setChecked(true);
		}
		setUserSports();
	}

	/**
	 * 兴趣运动
	 */
	private void setUserSports() {
		if (!TextUtils.isEmpty(user.getSports())) {
			ll_sports.removeAllViews();
			String[] picNames = user.getSports().split(",");
			for (int i = 0; i < picNames.length; i++) {
				if (i < 5) {
					ImageView imageView = new ImageView(context); 
					imageView.setLayoutParams(new LinearLayout.LayoutParams((int)(20 * OxygenApplication.ppi), (int)(20*OxygenApplication.ppi)));
					imageView.setBackground(GDUtil.engSporttodrawable(
									context,"icon_index_"+ picNames[i].replace(" ", "")));
					ll_sports.addView(imageView);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_user_friends:
			Intent friendsIntent = new Intent(context, FriendsActivity.class);
			friendsIntent.putExtra("userId", user.getId() + "");
			friendsIntent.putExtra("userName", user.getNickname());
			friendsIntent.putExtra("isSelf", true);
			context.startActivity(friendsIntent);
			break;
		case R.id.rl_user_collections:
			Intent collectionsIntent = new Intent(context, MyCollectionsActivity.class);
			startActivity(collectionsIntent);
			break;
		case R.id.rl_user_wallet:
			Intent walletIntent = new Intent(context, MyWalletActivity.class);
			startActivity(walletIntent);
			break;
		case R.id.rl_level_coin:
			Intent levelIntent = new Intent(context,LevelCoinActivity.class);
			startActivity(levelIntent);
			break;
		case R.id.modify_user_info:
			Intent setIntent = new Intent(context,SettingActivity.class);
			startActivity(setIntent);
			break;
		default:
			break;
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case USERS_MY_INFO:
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsonenlist = new JSONObject(strObject);
						if (jsonenlist.getInt("status") == 200) {
							String userInfo = (String) UserInfoUtils.getUserInfo(context, UrlConstants.USERS_MY_INFO, "");
							if(!userInfo.equals(jsonenlist.getJSONObject("data").toString())){
								user = UsersConstruct.ToUser(jsonenlist.getJSONObject("data"));
								setCach(jsonenlist.getJSONObject("data").toString());
								if (user != null) {
									getUserInfo();
								}
							}
						} else {
							ToastUtil.show(context,
									jsonenlist.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(context, "网络连接不可用，请稍后重试");
				}
				break;
			default:
				break;
			}
		}

	};
	/**
	 * 获取本地缓存数据
	 */
	private void getCach() {
		try {
			String userInfo = (String) UserInfoUtils.getUserInfo(context, UrlConstants.USERS_MY_INFO, "");
			if(!TextUtils.isEmpty(userInfo)){
				user = UsersConstruct.ToUser(new JSONObject(userInfo));
				getUserInfo();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 缓存数据
	 */
	protected void setCach(String json) {
		UserInfoUtils.setUserInfo(context, UrlConstants.USERS_MY_INFO, json);
	}
	
}
