package com.oxygen.www.module.user.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.module.user.eventbus_entities.ModifyuserInfo;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;

import de.greenrobot.event.EventBus;

/**
 * 修改用户密码和昵称的类
 * 
 * @author kunyuan
 * 
 */
public class EditDataActivity extends BaseActivity implements OnClickListener {
	protected final int POST_USER_UPDATE = 1;
	/**
	 * 修改个人信息的返回码
	 */
	public final int UPDATE_USERINFO_RESULTCODE = 2;
	protected final int MODIFY_PASSWORD = 3;
	private TextView tv_nav_title;
	/**
	 * 返回按钮
	 */
	private ImageView tv_nav_cnacel;
	/**
	 * 保存按钮
	 */
	private TextView tv_nav_save;
	/**
	 * 编辑的信息
	 */
	private EditText et_editdata;
	/**
	 * 删除编辑内容的按钮
	 */
	private ImageView iv_deletedata;
	/**
	 * 最上面显示的标题
	 */
	private String title;
	/**
	 * 修改的信息
	 */
	private String text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editdata);
		initData();
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initData() {
		Intent intent = getIntent();
		title = intent.getStringExtra("title");

	}

	private void initViews() {
		tv_nav_title = (TextView) findViewById(R.id.tv_nav_title);
		tv_nav_cnacel = (ImageView) findViewById(R.id.iv_back);
		tv_nav_save = (TextView) findViewById(R.id.tv_nav_save);
		et_editdata = (EditText) findViewById(R.id.et_editdata);
		iv_deletedata = (ImageView) findViewById(R.id.iv_deletedata);
		tv_nav_title.setText(title);

		if (getResources().getString(R.string.signature).equals(title)) {
			et_editdata.setLines(5);
			iv_deletedata.setVisibility(View.GONE);
			et_editdata.setText((String) UserInfoUtils.getUserInfo(this,
					Constants.SIGN, ""));
			et_editdata.setSelection(((String) UserInfoUtils.getUserInfo(this,
					Constants.SIGN, "")).length());
		} else if (getResources().getString(R.string.nickname).equals(title)) {
			et_editdata.setText((String) UserInfoUtils.getUserInfo(this,
					Constants.NICKNAME, ""));
			et_editdata.setSelection(((String) UserInfoUtils.getUserInfo(this,
					Constants.NICKNAME, "")).length());
		}

	}

	private void initViewsEvent() {
		// TODO Auto-generated method stub
		tv_nav_cnacel.setOnClickListener(this);
		tv_nav_save.setOnClickListener(this);
		iv_deletedata.setOnClickListener(this);
	}

	private void initValues() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.tv_nav_save:
			saveData();
			break;
		case R.id.iv_deletedata:
			et_editdata.setText("");
			break;
		}
	}

	/**
	 * 保存用户修改的信息
	 */
	private void saveData() {
		text = et_editdata.getText().toString().trim();
		final Map<String, String> params = new HashMap<String, String>();
		// 修改昵称
		if (getResources().getString(R.string.nickname).equals(title)) {
			if (text.length() > 16 || text.length() < 2) {
				ToastUtil.show(this, "昵称需要2-16位字符，请重新编辑");
				return;
			}
			UserInfoUtils.setUserInfo(getApplicationContext(),
					Constants.NICKNAME, text);
			params.clear();
			params.put("nickname", text);
			// 修改个性签名
		} else if (getResources().getString(R.string.signature).equals(title)) {
			UserInfoUtils.setUserInfo(getApplicationContext(), Constants.SIGN,
					text);
			params.clear();
			params.put("intro", text);
		}
		if (getResources().getString(R.string.password_p).equals(title)) {
			// 修改密码
			OxygenApplication.cachedThreadPool.execute(new Runnable() {
				public void run() {
					HttpUtil.Post_Noauth(UrlConstants.POST_RESET_PASSWORD,
							handler, MODIFY_PASSWORD, params);
				}
			});
		} else {
			OxygenApplication.cachedThreadPool.execute(new Runnable() {
				public void run() {
					String URL = UrlConstants.POST_USER_UPDATE
							+ (String) UserInfoUtils.getUserInfo(
									getApplicationContext(), Constants.USERID,
									"") + ".json";
					HttpUtil.Post(URL, handler, POST_USER_UPDATE, params);
				}
			});
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case POST_USER_UPDATE:
				if (msg.obj == null) {
					ToastUtil.show(EditDataActivity.this, "网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonObject = (JSONObject) msg.obj;
						if (jsonObject.getInt("status") == 200) {
							// 修改数据成功后把数据带回之前的activity
							Intent data = new Intent();
							data.putExtra("userinfo", text);
							setResult(UPDATE_USERINFO_RESULTCODE, data);
							ToastUtil.show(
									EditDataActivity.this,
									getResources().getString(
											R.string.update_userinfo_success));
							finish();
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
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
}
