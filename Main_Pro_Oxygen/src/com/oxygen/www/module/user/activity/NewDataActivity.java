package com.oxygen.www.module.user.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.MenuActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.module.user.adapter.MyGridViewAdapter;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.KeyBoardUtils;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;

/**
 * 用户首次登陆录入信息
 * @author 杨庆雷
 * 2015-9-15下午5:18:28
 */
public class NewDataActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener, TextWatcher {
	protected static final int POST_USER_UPDATE = 1;
	private TextView tv_nickname_modified;
	private ImageView iv_sex_modified;
	private TextView tv_age_modified;
	private TextView tv_height_modified;
	private TextView tv_weight_modified;
	private TextView userinfo_title;
	private FrameLayout fl_userinfo;
	private RadioGroup userinfo_sex;
	private RadioButton rb_userinfo_man;
	private RadioButton rb_userinfo_woman;
	private EditText userinfo_modify;
	private RelativeLayout rl_userinfo_modify;
	private TextView userinco_unit;
	private ImageView iv_next;
	private RelativeLayout rl_sports_like;
	private GridView gv_sports_like;
	private ImageView userinfo_complete;
	private String sex;
	/**
	 * 性别，身高，体重，年龄
	 */
	private int count = 1;
	private Map<String, String> params;
	private MyHandler handler;
	private MyGridViewAdapter gridViewAdapter;
	private ProgressBar progressBar;
	private String loginType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newstart);

		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		tv_nickname_modified = (TextView) findViewById(R.id.tv_nickname_modified);
		iv_sex_modified = (ImageView) findViewById(R.id.iv_sex_modified);
		tv_age_modified = (TextView) findViewById(R.id.tv_age_modified);
		tv_height_modified = (TextView) findViewById(R.id.tv_height_modified);
		tv_weight_modified = (TextView) findViewById(R.id.tv_weight_modified);
		userinfo_title = (TextView) findViewById(R.id.userinfo_title);
		fl_userinfo = (FrameLayout) findViewById(R.id.fl_userinfo);
		userinfo_sex = (RadioGroup) findViewById(R.id.userinfo_sex);
		rb_userinfo_man = (RadioButton) findViewById(R.id.rb_userinfo_man);
		rb_userinfo_woman = (RadioButton) findViewById(R.id.rb_userinfo_woman);
		userinfo_modify = (EditText) findViewById(R.id.userinfo_modify);
		rl_userinfo_modify = (RelativeLayout) findViewById(R.id.rl_userinfo_modify);
		userinco_unit = (TextView) findViewById(R.id.userinco_unit);
		iv_next = (ImageView) findViewById(R.id.iv_next);
		rl_sports_like = (RelativeLayout) findViewById(R.id.rl_sports_like);
		gv_sports_like = (GridView) findViewById(R.id.gv_sports_like);
		userinfo_complete = (ImageView) findViewById(R.id.userinfo_complete);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}

	private void initViewsEvent() {
		params = new HashMap<String, String>();
		iv_next.setOnClickListener(this);
		userinfo_complete.setOnClickListener(this);
		userinfo_sex.setOnCheckedChangeListener(this);
		userinfo_modify.addTextChangedListener(this);
	}

	private void initValues() {
		loginType = getIntent().getStringExtra("loginType");
		rl_userinfo_modify.setVisibility(View.VISIBLE);
		userinfo_sex.setVisibility(View.GONE);
		iv_next.setVisibility(View.GONE);
		handler = new MyHandler(this);
		userinfo_title.setText("你的昵称");
		KeyBoardUtils.openKeybord(userinfo_modify, this);
		userinfo_modify.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
		userinfo_modify.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
		userinfo_sex.check(R.id.rb_userinfo_man);
		sex = "男";
		if(!"phoneNumber".equals(loginType)){
			count = 2;
			rl_userinfo_modify.setVisibility(View.GONE);
			userinfo_sex.setVisibility(View.VISIBLE);
			tv_nickname_modified.setText((String)UserInfoUtils.getUserInfo(this,Constants.NICKNAME, ""));
			tv_nickname_modified.setVisibility(View.VISIBLE);
			userinfo_title.setText("男生or女生？");
			iv_next.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_next:
			switch (count) {
			case 1:
				KeyBoardUtils.closeKeybord(userinfo_modify, this);
				iv_next.setVisibility(View.VISIBLE);
				count = count + 1;
				userinfo_title.setText("男生or女生？");
				rl_userinfo_modify.setVisibility(View.GONE);
				userinfo_sex.setVisibility(View.VISIBLE);
				String nickName = userinfo_modify.getText().toString().trim();
				params.put("nickname", nickName);
				UserInfoUtils.setUserInfo(getApplicationContext(),Constants.NICKNAME, nickName);
				tv_nickname_modified.setText(nickName);
				tv_nickname_modified.setVisibility(View.VISIBLE);
				userinfo_modify.setText("");
				break;
			case 2:
				iv_next.setVisibility(View.GONE);
				userinfo_modify
						.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
				KeyBoardUtils.openKeybord(userinfo_modify, this);
				userinfo_sex.setVisibility(View.GONE);
				rl_userinfo_modify.setVisibility(View.VISIBLE);
				count = count + 1;
				UserInfoUtils.setUserInfo(getApplicationContext(),Constants.SEX, sex);
				if (getResources().getString(R.string.man).equals(sex)) {
					params.put("sex", 1 + "");
					iv_sex_modified
							.setImageResource(R.drawable.icon_userinfo_man_select);
				} else {
					params.put("sex", 2 + "");
					iv_sex_modified
							.setImageResource(R.drawable.icon_userinfo_women_select);
				}
				iv_sex_modified.setVisibility(View.VISIBLE);
				userinfo_title.setText("请输入你的身高");
				userinco_unit.setText("cm");
				break;
			case 3:
				KeyBoardUtils.openKeybord(userinfo_modify, this);
				iv_next.setVisibility(View.GONE);
				String height = userinfo_modify.getText().toString().trim();
				userinfo_modify.setText("");
				UserInfoUtils.setUserInfo(getApplicationContext(),Constants.HEIGHT, height);
				params.put("height", height);
				userinfo_title.setText("请输入你的体重");
				userinco_unit.setText("kg");
				count = count + 1;
				Spannable heightSpan = new SpannableString(height + "cm");
				heightSpan.setSpan(new AbsoluteSizeSpan(18),
						heightSpan.length() - 2, heightSpan.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				tv_age_modified.setText(heightSpan);
				tv_age_modified.setVisibility(View.VISIBLE);
				userinfo_modify.setText("");
				break;
			case 4:
				KeyBoardUtils.openKeybord(userinfo_modify, this);
				iv_next.setVisibility(View.GONE);
				String weight = userinfo_modify.getText().toString().trim();
				UserInfoUtils.setUserInfo(getApplicationContext(),Constants.WEIGHT, weight);
				params.put("weight", weight);
				userinfo_title.setText("请输入你的年龄");
				userinco_unit.setText("岁");
				count = count + 1;
				Spannable weightSpan = new SpannableString(weight + "kg");
				weightSpan.setSpan(new AbsoluteSizeSpan(18),
						weightSpan.length() - 2, weightSpan.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				tv_height_modified.setText(weightSpan);
				tv_height_modified.setVisibility(View.VISIBLE);
				userinfo_modify.setText("");
				break;
			case 5:
				KeyBoardUtils.closeKeybord(userinfo_modify, this);
				String age = userinfo_modify.getText().toString().trim();
				UserInfoUtils.setUserInfo(getApplicationContext(),Constants.AGE, age);
				params.put("age", age);
				userinfo_title.setText("选择感兴趣的运动");
				userinfo_sex.setVisibility(View.GONE);
				userinfo_modify.setVisibility(View.GONE);
				rl_sports_like.setVisibility(View.VISIBLE);
				count = count + 1;
				Spannable ageSpan = new SpannableString(age + "岁");
				ageSpan.setSpan(new AbsoluteSizeSpan(18),
						ageSpan.length() - 1, ageSpan.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				tv_weight_modified.setText(ageSpan);
				tv_weight_modified.setVisibility(View.VISIBLE);
				gridViewAdapter = new MyGridViewAdapter(this,
						new ArrayList<String>());
				gv_sports_like.setAdapter(gridViewAdapter);
				break;
			}
			break;
		case R.id.userinfo_complete:
			UserInfoUtils.setUserInfo(
							getApplicationContext(),Constants.SPORT_SELECTED,
							gridViewAdapter.sportSelectedNames.toString()
									.replace(" ", "").replace("[", "")
									.replace("]", ""));
			params.put("sports", gridViewAdapter.sportSelectedNames.toString()
					.replace(" ", "").replace("[", "").replace("]", ""));
			saveAndPostUserInfo(params);
			break;
		default:
			break;
		}
	}

	/**
	 * 性别
	 * 
	 * @param params
	 */
	private void saveAndPostUserInfo(Map<String, String> param) {
		progressBar.setVisibility(View.VISIBLE);
		userinfo_complete.setClickable(false);
		submitUserInfoToNet(param);
	}

	private void submitUserInfoToNet(final Map<String, String> params) {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				String URL = UrlConstants.POST_USER_UPDATE
						+ (String)UserInfoUtils.getUserInfo(getApplicationContext(),Constants.USERID, "")
						+ ".json";
				HttpUtil.Post(URL, handler, POST_USER_UPDATE, params);
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_userinfo_man:
			sex = "男";
			break;
		case R.id.rb_userinfo_woman:
			sex = "女";
			break;

		default:
			break;
		}
	}

	static class MyHandler extends Handler {
		private final WeakReference<NewDataActivity> mActivityReference;

		public MyHandler(NewDataActivity newDataActivity) {
			mActivityReference = new WeakReference<NewDataActivity>(
					newDataActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			final NewDataActivity activity = mActivityReference.get();
			if (activity == null) {
				return;
			}
			switch (msg.what) {
			case POST_USER_UPDATE:
				if (msg.obj == null) {
					ToastUtil.show(activity, "网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonObject = (JSONObject) msg.obj;
						if (jsonObject.getInt("status") == 200) {
							Intent intent = new Intent(activity,
									MenuActivity.class);
							activity.startActivity(intent);
							activity.progressBar.setVisibility(View.GONE);
						}else{
							activity.progressBar.setVisibility(View.GONE);
							ToastUtil.show(activity, "提交失败");
							activity.userinfo_complete.setClickable(true);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		switch (this.count) {
		case 1:
			if (s.toString().length() > 0) {
				iv_next.setVisibility(View.VISIBLE);
			} else {
				iv_next.setVisibility(View.GONE);
			}
			break;
		case 3:
			int height = 0;
			if (s.toString().length() < 4 && !TextUtils.isEmpty(s.toString())) {
				String str = s.toString();
				try {
					height = Integer.parseInt(str);
				} catch (NumberFormatException e) {
					userinfo_modify.setText("");
					return;
				}
			}
			if (50 < height && height < 219) {
				iv_next.setVisibility(View.VISIBLE);
			} else {
				iv_next.setVisibility(View.GONE);
			}
			break;
		case 4:
			int weight = 0;
			if (s.toString().length() < 4 && !TextUtils.isEmpty(s.toString())) {
				String str = s.toString();
				try {
					weight = Integer.parseInt(str);
				} catch (NumberFormatException e) {
					userinfo_modify.setText("");
					return;
				}
			}
			if (20 < weight && weight < 199) {
				iv_next.setVisibility(View.VISIBLE);
			} else {
				iv_next.setVisibility(View.GONE);
			}
			break;
		case 5:
			int age = 0;
			if (s.toString().length() < 4 && !TextUtils.isEmpty(s.toString())) {
				String str = s.toString();
				try {
					age = Integer.parseInt(str);
				} catch (NumberFormatException e) {
					userinfo_modify.setText("");
					return;
				}
			}
			if (1 < age && age < 99) {
				iv_next.setVisibility(View.VISIBLE);
			} else {
				iv_next.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}
}
