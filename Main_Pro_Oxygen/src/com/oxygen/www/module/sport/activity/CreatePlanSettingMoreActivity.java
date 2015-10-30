package com.oxygen.www.module.sport.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.R.color;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.enties.Group;
import com.oxygen.www.module.sport.eventbus_enties.MoreMore;
import com.oxygen.www.module.team.activity.RelevanceTeamListActivity;
import com.oxygen.www.module.team.eventbus_enties.BindTeamId;

import de.greenrobot.event.EventBus;

/**
 * 发起活动 - 设置更多
 * 
 * @author 张坤
 * 
 */
public class CreatePlanSettingMoreActivity extends BaseActivity implements OnClickListener {
	
	/**
	 * 是否是 从团队详情页发起活动
	 */
	private Group group;
	private ImageView iv_back;
	private TextView tv_save;
	
	private RelativeLayout rl_relate_team, rl_privacy, rl_settingmoremore;
	private TextView tv_privacy, et_relate_team_name, tv_moremore;
	private EditText et_people;
	
	/**
	 * 活动人数
	 */
	private int limition = 0;
	/**
	 * 关联了的团队的Id(如果关联了团队)
	 */
	private String teamId = "";
	/**
	 * 关联团队的名称
	 */
	private String teamName = "";
	/**
	 * 隐私设置 popwindow
	 */
	private PopupWindow popupWindow;
	/**
	 * 隐私状态
	 */
	private String privacyStatus = "仅限邀请";
	/**
	 * 隐私设置: 广场可见 
	 */
	private Button noPrivacy;
	/**
	 * 隐私设置: 仅限邀请
	 */
	private Button privacy;
	/**
	 * 报名者填写项
	 */
	private String moremore = "0000000";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_createplan_settingmore);

		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_save = (TextView) findViewById(R.id.tv_save);
		
		et_people= (EditText) findViewById(R.id.et_people);
		rl_relate_team = (RelativeLayout) findViewById(R.id.rl_relate_team);
		et_relate_team_name = (TextView) findViewById(R.id.et_relate_team_name);
		rl_privacy = (RelativeLayout) findViewById(R.id.rl_privacy);
		tv_privacy = (TextView) findViewById(R.id.tv_privacy);
		rl_settingmoremore = (RelativeLayout) findViewById(R.id.rl_settingmoremore);
		tv_moremore = (TextView) findViewById(R.id.tv_moremore);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_save.setOnClickListener(this);
		
		rl_relate_team.setOnClickListener(this);
		rl_privacy.setOnClickListener(this);
		rl_settingmoremore.setOnClickListener(this);
		
	}

	private void initValues() {
		
		EventBus.getDefault().register(this);
		// 团队详情页 发起团队活动 入口
		group = (Group) getIntent().getSerializableExtra("group");

		Intent intent = getIntent();
		// 活动人数
		int limitation = intent.getIntExtra("limitation", 0);
		if (limitation != 0) {
			et_people.setText(limitation+"");
		}
		// 关联的团队
		if (group != null) {
			// 是否团队详情页创建的活动
			teamId = group.getId() + "";
			teamName = group.getName();
			et_relate_team_name.setText(teamName);
		} else {
			teamId = intent.getStringExtra("teamId");
			teamName = intent.getStringExtra("teamName");
			if (!(TextUtils.isEmpty(teamId) ||  TextUtils.isEmpty(teamName))) {
				et_relate_team_name.setText(teamName);
			}
		}
		
		// 隐私设置
		privacyStatus = intent.getStringExtra("privacyStatus");
		if (!TextUtils.isEmpty(privacyStatus)) {
			tv_privacy.setText(privacyStatus);
		}
		
//		String more = intent.getStringExtra("moremore");
//		if (!TextUtils.isEmpty(moremore)) {
//			moremore = more;
//		}
		moremore = intent.getStringExtra("moremore");
		if (!"0000000".equals(moremore)) {
			tv_moremore.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.iv_back:
			finish();
			break;

		case R.id.tv_save:
			// 确定
			Intent intent = new Intent();
			// TODO...
			
			int people = 0;
			if (!TextUtils.isEmpty(et_people.getText())) {
				people = Integer.valueOf(et_people.getText().toString());
			}
			
			intent.putExtra("people", people);
			intent.putExtra("teamId", teamId);
			intent.putExtra("teamName", teamName);
//			intent.putExtra("privacy", privacyStatus);
			intent.putExtra("privacyStatus", privacyStatus);

			setResult(RESULT_OK, intent);

            finish();
			break;

		case R.id.rl_relate_team:
			// 关联团队/解绑团队
			if (group == null) {
				// 并非从团队详情页 发起团队活动入口 进入
				Intent intent_relate_team = new Intent(this, RelevanceTeamListActivity.class);
				startActivity(intent_relate_team);
			}
			break;

		case R.id.rl_privacy:
			// 隐私设置
			showPrivacyPopWindow();
			break;
			
		case R.id.rl_settingmoremore:
			// 设置报名者填写项
			Intent moreIntent = new Intent(CreatePlanSettingMoreActivity.this, CreatePlanSettingMoreMoreActivity.class);
			if (!"0000000".equals(moremore)) {
				moreIntent.putExtra("moremore", moremore);
			}
			startActivity(moreIntent);
			break;
			
		default:
			break;
		}
		
	}
	
	/**
	 * 隐私设置
	 */
	@SuppressWarnings("deprecation")
	private void showPrivacyPopWindow() {
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int height = manager.getDefaultDisplay().getHeight();
		int width = manager.getDefaultDisplay().getWidth();
		if (popupWindow == null || !popupWindow.isShowing()) {
			View contentView = View.inflate(this,
					R.layout.activity_sex_popupwindow, null);
			noPrivacy = (Button) contentView.findViewById(R.id.sex_man);
			privacy = (Button) contentView.findViewById(R.id.sex_woman);
			noPrivacy.setText("广场可见");
			privacy.setText("仅限邀请");
			Button select_sure = (Button) contentView.findViewById(R.id.sexselect_sure);
			
			noPrivacy.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					privacyStatus = "广场可见";
					noPrivacy.setTextColor(getResources().getColor(color.black));
					privacy.setTextColor(getResources().getColor(color.lightgray));
				}
			});
			privacy.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					privacyStatus = "仅限邀请";
					privacy.setTextColor(getResources().getColor(color.black));
					noPrivacy.setTextColor(getResources().getColor(color.lightgray));
					
				}
			});
			select_sure.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					popupWindow.dismiss();
					tv_privacy.setText(privacyStatus);
					
				}
			});

			popupWindow = new PopupWindow(contentView);
			popupWindow.setWidth(width);
			popupWindow.setHeight(height / 3);// 设置弹出框大小
			popupWindow.setFocusable(true);
			popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
			popupWindow.setOutsideTouchable(false);
			
			if ("广场可见".equals(privacyStatus)) {
				noPrivacy.setTextColor(getResources().getColor(color.black));
			} else if ("仅限邀请".equals(privacyStatus)) {
				privacy.setTextColor(getResources().getColor(color.black));
			}
			
		}
		
	}
	

	/**
	 * 关联团队
	 * @param msg
	 */
	public void onEventMainThread(BindTeamId msg) {
		teamId = msg.getTeamId();
		teamName = msg.getName();
		Log.i("settingmore", "teamId():"+msg.getTeamId());
		
		if("".equals(teamId)){
			et_relate_team_name.setText("暂不关联");
		}else {
			et_relate_team_name.setText(teamName);
		}
		
	}
	
	/**
	 * 设置报名者填写项
	 * 
	 * @param msg
	 */
	public void onEventMainThread(MoreMore msg) {
		
		if(!"0000000".equals(msg.getMoremore())){
			tv_moremore.setVisibility(View.VISIBLE);
		}else {
			tv_moremore.setVisibility(View.INVISIBLE);
		}
		
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		EventBus.getDefault().unregister(this);
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	
	}

}
