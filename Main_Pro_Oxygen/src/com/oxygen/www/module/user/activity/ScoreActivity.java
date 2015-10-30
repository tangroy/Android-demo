package com.oxygen.www.module.user.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.User;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.module.user.adapter.ScoreAdapter;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.CircleImageView;

public class ScoreActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_back, iv_sex;
	private CircleImageView iv_head;
	private TextView tv_title,tv_name, tv_age, tv_intro;
	private LinearLayout tv_sports;
	private ListView lv_score_list;
	private FrameLayout fl_userdate;
	private final int NET_USER_SHOW = 1;
	private String userid;
	private boolean isme = false;
	private boolean fromMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_sex = (ImageView) findViewById(R.id.iv_sex);
		iv_head = (CircleImageView) findViewById(R.id.iv_head);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_age = (TextView) findViewById(R.id.tv_age);
		tv_sports = (LinearLayout) findViewById(R.id.tv_sports);
		tv_intro = (TextView) findViewById(R.id.tv_intro);
		lv_score_list = (ListView) findViewById(R.id.lv_score_list);
		fl_userdate = (FrameLayout) findViewById(R.id.fl_userdate);
		tv_title =(TextView) findViewById(R.id.tv_title);

	}

	private void initViewsEvent() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(this);
		userid =getIntent().getStringExtra("userid");
		isme = getIntent().getBooleanExtra("isme", false);
		fromMenu = getIntent().getBooleanExtra("fromMenu", false);
	}

	private void initValues() {
		// TODO Auto-generated method stub
		getUserShow(userid);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.equals(iv_back)) {
			finish();
		}
	}

	/**
	 * 获取服务器events邀请信息列表
	 */
	private void getUserShow(final String userid) {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.USERS_SHOW_URL + userid + ".json",
						handler, NET_USER_SHOW);
			}
		});
	}

	/**
	 * handler 更新UI
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_USER_SHOW:
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsonenlist = new JSONObject(strObject);
						if (jsonenlist.getInt("status") == 200) {
							User user = UsersConstruct.ToUser(jsonenlist
									.getJSONObject("data"));
							UpdateScore(user);
						} else {
							ToastUtil.show(ScoreActivity.this,
									jsonenlist.getString("msg"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(ScoreActivity.this, "网络连接不可用，请稍后重试");

				}
				break;

			default:
				break;
			}
		}

	};

	private void UpdateScore(User user) {
		if (isme) {
			fl_userdate.setVisibility(View.GONE);
		} else {
			fl_userdate.setVisibility(View.VISIBLE);
			tv_title.setText("用户详情");
			int sex = user.getSex();
			if (sex == 1) {
				iv_sex.setImageDrawable(ScoreActivity.this.getResources()
						.getDrawable(R.drawable.icon_man));
			} else if (sex == 2) {
				iv_sex.setImageDrawable(ScoreActivity.this.getResources()
						.getDrawable(R.drawable.icon_women));
			} else {
				iv_sex.setImageDrawable(null);
			}
			tv_name.setText(user.getNickname());
			tv_age.setText(user.getAge() + "");
			if(!TextUtils.isEmpty(user.getSports())){
				tv_sports.removeAllViews();
				String[] picNames = user.getSports().split(",");
				for (int i = 0; i < picNames.length; i++) {
					if(i<5){
						ImageView imageView = new ImageView(this);
						imageView.setBackgroundDrawable(GDUtil.engSporttodrawable(this, "icon_index_"+picNames[i].replace(" ", "")));
						tv_sports.addView(imageView);
					}
				}
			}
			tv_intro.setText(user.getIntro());
			ImageUtil.showImage(user.getHeadimgurl(),iv_head,R.drawable.icon_def);
		}
		ScoreAdapter adapter = new ScoreAdapter(user.getSummarys(),
				ScoreActivity.this);
		lv_score_list.setAdapter(adapter);
		if(fromMenu){
			lv_score_list.setBackgroundColor(getResources().getColor(R.color.white));
		}
	}
}
