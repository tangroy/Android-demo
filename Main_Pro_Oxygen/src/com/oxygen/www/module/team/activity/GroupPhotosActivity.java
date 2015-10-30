package com.oxygen.www.module.team.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Event;
import com.oxygen.www.enties.Group;
import com.oxygen.www.module.sport.construct.EventConstruct;
import com.oxygen.www.module.team.adapter.GroupEventAdapter;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;

/**
 * 团队相册界面
 * @author yang
 * 2015-5-26上午10:14:57
 */
public class GroupPhotosActivity extends BaseActivity implements OnClickListener {
	protected static final int NET_GETGROUP_PHOTOS = 1;
	private Group group;
	private ImageView iv_back;
	private TextView tv_edit;
	private ListView lv_group_photos;
	private RelativeLayout rl_delete;
	private ViewPager vp_group_photo;
	private TextView tv_delete_number;
	private ProgressBar progressBar;
	private RelativeLayout no_team_photos;
	private List<Event> events;
	private boolean edit_flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groupphotos);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_edit = (TextView) findViewById(R.id.tv_edit);
		lv_group_photos = (ListView) findViewById(R.id.lv_group_photos);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		rl_delete = (RelativeLayout) findViewById(R.id.rl_delete);
		tv_delete_number = (TextView) findViewById(R.id.tv_delete_number);
		vp_group_photo = (ViewPager) findViewById(R.id.vp_group_photo);
		no_team_photos = (RelativeLayout) findViewById(R.id.no_team_photos);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_edit.setOnClickListener(this);
		tv_delete_number.setOnClickListener(this);
	}

	private void initValues() {
		group = (Group) getIntent().getSerializableExtra("group");
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.GROUPS_PHOTOS + group.getId()+".json",
						handler, NET_GETGROUP_PHOTOS);
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.tv_edit:
			if(edit_flag){
				tv_edit.setText("编辑");
				edit_flag = false;
				rl_delete.setVisibility(View.GONE);
			}else{
				tv_edit.setText("取消");
				edit_flag = true;
				rl_delete.setVisibility(View.VISIBLE);
				tv_delete_number.setText("删除(0)");
			}
			break;
			//删除图片
		case R.id.tv_delete_number:
			
			break;
		default:
			break;
		}
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_GETGROUP_PHOTOS:
				progressBar.setVisibility(View.GONE);
				String strObject = (String) msg.obj;
				if(strObject != null && strObject.length() > 10){
					try {
						JSONObject jsoninfos = new JSONObject(strObject);
						if (jsoninfos.getInt("status") == 200) {
							if(jsoninfos.getString("data") == null || jsoninfos.getString("data").length()<10){
								no_team_photos.setVisibility(View.VISIBLE);
							}else{
								events = EventConstruct.Toeventlist(jsoninfos);
								GroupEventAdapter groupindexAdapter = new GroupEventAdapter(GroupPhotosActivity.this,events,vp_group_photo);
								lv_group_photos.setAdapter(groupindexAdapter);
							}
						} else {
							ToastUtil.show(GroupPhotosActivity.this, "请求异常");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;

			default:
				break;
			}
		}
		
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(vp_group_photo.isShown()){
			vp_group_photo.setVisibility(View.GONE);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
