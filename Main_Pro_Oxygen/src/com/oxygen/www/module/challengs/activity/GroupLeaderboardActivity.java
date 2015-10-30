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
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.ChallengesUser;
import com.oxygen.www.module.challengs.adapter.PersonChallengesAdapter;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.module.team.construt.MembersConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.CircleImageView;

public class GroupLeaderboardActivity extends BaseActivity implements
		OnClickListener {
	protected static final int CHALLENGES_GROUP_LEADERBOARD = 1;
	int challenge_id = 0;
	int group_id = 0;
	private ImageView iv_back;
	private CircleImageView group_img;
	private TextView gruop_name;
	private TextView group_distance;
	private ListView lv_personalrank;
	private ArrayList<ChallengesUser> le;
	private JSONObject jsonobject_userinfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groupleaderboard);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		group_img = (CircleImageView) findViewById(R.id.group_img);
		gruop_name = (TextView) findViewById(R.id.gruop_name);
		group_distance = (TextView) findViewById(R.id.group_distance);
		lv_personalrank = (ListView) findViewById(R.id.lv_personalrank);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
	}

	private void initValues() {
		challenge_id = getIntent().getIntExtra("challenge_id", 0);
		group_id = getIntent().getIntExtra("group_id", 0);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.CHALLENGES_GROUP_LEADERBOARD + challenge_id+"/"+group_id
						+ ".json", handler, CHALLENGES_GROUP_LEADERBOARD);
			}
		});
		
		lv_personalrank.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String userid = le.get(position).getUser_id() + "";
				double maxdistanceorfrist = le.get(0).getDistance();
				Intent intent = new Intent(GroupLeaderboardActivity.this,
						PersonPerformance.class);
				try {
					intent.putExtra("user", UsersConstruct
							.ToUser(jsonobject_userinfo.getJSONObject(userid)));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				intent.putExtra("maxdistanceorfrist", maxdistanceorfrist);
				intent.putExtra("challengesid", challenge_id);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
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
			case CHALLENGES_GROUP_LEADERBOARD:
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsoninfos = new JSONObject(strObject);
						if (jsoninfos.getInt("status") == 200) {
							JSONObject groupInfo = jsoninfos.getJSONObject("data").getJSONObject("group");
							String picUri = groupInfo.getString("pic");
							String name = groupInfo.getString("name");
							ImageUtil.showImage(picUri, group_img, R.drawable.icon_def);
							gruop_name.setText(name);
							le = MembersConstruct.Tomemberlist(jsoninfos.getJSONObject("data").getJSONArray("leaderboard"));
							double totalDistance = 0;
							if(le != null && le.size()>0){
								for (int i = 0; i < le.size(); i++) {
									totalDistance = totalDistance+le.get(i).getDistance();
								}
							}
							DecimalFormat df = new DecimalFormat("#0.00");
							group_distance.setText(df.format(totalDistance/1000)+"km");
							jsonobject_userinfo = jsoninfos
									.getJSONObject("users_info");
							PersonChallengesAdapter other_adapter = new PersonChallengesAdapter(
									le, GroupLeaderboardActivity.this,
									jsonobject_userinfo, null, false);
							lv_personalrank.setAdapter(other_adapter);
						} else {
							ToastUtil.show(GroupLeaderboardActivity.this,
									"请求服务器异常");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(GroupLeaderboardActivity.this,
							"请求服务器异常");
				}

			default:
				break;
			}
		}

	};
}
