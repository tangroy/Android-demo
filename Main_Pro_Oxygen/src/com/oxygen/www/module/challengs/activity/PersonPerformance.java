package com.oxygen.www.module.challengs.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.GDAcvitity;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.challengs.adapter.PerformanceAdapter;
import com.oxygen.www.module.sport.activity.GDActivityResultActivity;
import com.oxygen.www.module.sport.construct.ActivitiesConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;

/**
 * 个人运动成绩页面
 * 
 * @author sambatang
 * 
 */
public class PersonPerformance extends BaseActivity implements OnClickListener {
	private int challengesid = 0;
	private User user;
	private ProgressBar progressbar, pb_distance;
	private ImageView iv_back, iv_head;
	private TextView tv_name, tv_maxditance, tv_performance_count;
	private ListView lv_performance;
	private final int NET_GETACTIVITYS = 1;
	private ArrayList<GDAcvitity> activities;
	private double performance;
	private double maxdistanceorfrist;
 	DecimalFormat df = new DecimalFormat("#0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challengs_personperformance);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		pb_distance = (ProgressBar) findViewById(R.id.pb_distance);
		iv_head = (ImageView) findViewById(R.id.iv_head);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_maxditance = (TextView) findViewById(R.id.tv_maxditance);
		tv_performance_count = (TextView) findViewById(R.id.tv_performance_count);
		lv_performance = (ListView) findViewById(R.id.lv_performance);

	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		lv_performance.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				if ("xiaomi".equals(activities.get(arg2).getSource())) {
					// do nothing
				} else {
					// TODO Auto-generated method stub
					lv_performance.setEnabled(false);
					progressbar.setVisibility(View.VISIBLE);
					Intent intent_start = new Intent(PersonPerformance.this,
							GDActivityResultActivity.class);
					intent_start.putExtra("activityid", activities.get(arg2)
							.get_id());
					intent_start.putExtra("sportcategory", Constants.COUNT_CATEGORY_RUNNING);
					startActivity(intent_start);
					
				}

			}

		});
	}

	private void initValues() {
		Intent intent = getIntent();
		challengesid = intent.getIntExtra("challengesid", 0);
		user = (User) intent.getSerializableExtra("user");
		maxdistanceorfrist = intent.getDoubleExtra("maxdistanceorfrist", 0.0);
 		ImageUtil.showImage(user.getHeadimgurl() + Constants.qiniu_photo_head,
				iv_head, R.drawable.icon_def);
		tv_name.setText(user.getNickname());
		getPersonrunningActivitys(challengesid, user.getId());
	}

	@Override
	protected void onResume() {
		super.onResume();
		lv_performance.setEnabled(true);
		progressbar.setVisibility(View.GONE);
	}

	private void getPersonrunningActivitys(final int challengesid,
			final int userid) {

		progressbar.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.CHALLENGES_RUNNING_ACTIVITIES
						+ challengesid + "/" + userid + ".json", handler,
						NET_GETACTIVITYS);
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
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
			case NET_GETACTIVITYS:

				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsoninfos = new JSONObject(strObject);
						if (jsoninfos.getInt("status") == 200) {
							JSONObject data = jsoninfos.getJSONObject("data");
							if (!data.isNull("activities")) {
								activities = ActivitiesConstruct
										.Toactivitylist(data
												.getJSONArray("activities"));

							}
							JSONObject jsonobject_userinfo = null;
							if (!jsoninfos.isNull("users_info")) {
								jsonobject_userinfo = jsoninfos
										.getJSONObject("users_info");
							}
							performance = data.getDouble("performance");
							UpdateUi();

							progressbar.setVisibility(View.GONE);
						} else {
							progressbar.setVisibility(View.GONE);
							ToastUtil.show(PersonPerformance.this, "请求服务器异常");

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					progressbar.setVisibility(View.GONE);
				}

				break;

			default:
				break;
			}
		}

	};

	private void UpdateUi() {
		tv_performance_count.setText(df.format(performance / 1000));
		if (activities != null && activities.size() > 0) {
			PerformanceAdapter adapter = new PerformanceAdapter(activities,
					PersonPerformance.this);
			lv_performance.setAdapter(adapter);
		}

		if(maxdistanceorfrist == 0.0){
			tv_maxditance.setVisibility(View.INVISIBLE);
		}else{
			tv_maxditance.setText("第一名：" + df.format(maxdistanceorfrist / 1000)
					+ "km");
		}
		if (performance == 0) {
			pb_distance.setProgress(1);
		} else {
			pb_distance
					.setProgress((int) (performance / maxdistanceorfrist * 80));

		}
	}
}
