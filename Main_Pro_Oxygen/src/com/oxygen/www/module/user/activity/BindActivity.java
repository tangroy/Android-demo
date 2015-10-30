package com.oxygen.www.module.user.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.module.user.eventbus_entities.BindMobile;
import com.oxygen.www.module.user.eventbus_entities.BindQQOrWX;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import de.greenrobot.event.EventBus;

/**
 * 绑定账号的界面 （包括手机号 微信和QQ的绑定入口）
 * 
 * @author 杨庆雷 2015-9-10下午4:58:04
 */
public class BindActivity extends BaseActivity implements OnClickListener {

	protected static final int BIND_PRECHECK = 2;
	protected static final int BIND_PHONE = 3;
	private Tencent mTencent;
	private ImageView iv_back;
	private RelativeLayout rl_bind_phone;
	private RelativeLayout rl_bind_wx;
	private RelativeLayout rl_bind_qq;
	private TextView tv_phone_bind;
	private TextView tv_wx_bind;
	private TextView tv_qq_bind;
	public String openId;
	private String unionid;
	private String bindType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind);
		EventBus.getDefault().register(this);

		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		rl_bind_phone = (RelativeLayout) findViewById(R.id.rl_bind_phone);
		rl_bind_wx = (RelativeLayout) findViewById(R.id.rl_bind_wx);
		rl_bind_qq = (RelativeLayout) findViewById(R.id.rl_bind_qq);
		tv_phone_bind = (TextView) findViewById(R.id.tv_phone_bind);
		tv_wx_bind = (TextView) findViewById(R.id.tv_wx_bind);
		tv_qq_bind = (TextView) findViewById(R.id.tv_qq_bind);

	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		rl_bind_phone.setOnClickListener(this);
		rl_bind_wx.setOnClickListener(this);
		rl_bind_qq.setOnClickListener(this);
	}

	private void initValues() {
		initBindInfo();
	}

	/**
	 * 判断手机号，微信或者QQ是否绑定
	 */
	private void initBindInfo() {
		
		String userMobile = (String) UserInfoUtils.getUserInfo(getApplicationContext(), Constants.MOBILE, "");
		if (!"".equals(userMobile)) {
			tv_phone_bind.setText(userMobile);
		}
		
		if (!"".equals((String)UserInfoUtils.getUserInfo(getApplicationContext(),Constants.UNIONID, ""))) {
			Drawable leftDrawable = getResources().getDrawable(
					R.drawable.icon_binded);
			leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
					leftDrawable.getMinimumHeight());
			tv_wx_bind.setCompoundDrawables(null, null, leftDrawable, null);
			tv_wx_bind.setText(" ");
			rl_bind_wx.setClickable(false);
		}
		if (!"".equals((String)UserInfoUtils.getUserInfo(this,Constants.QQ_OPENID, ""))) {
			Drawable leftDrawable = getResources().getDrawable(
					R.drawable.icon_binded);
			leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
					leftDrawable.getMinimumHeight());
			tv_qq_bind.setCompoundDrawables(null, null, leftDrawable, null);
			tv_qq_bind.setText(" ");
			rl_bind_qq.setClickable(false);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.rl_bind_phone:
			// 微信或者QQ登陆 且之前没有绑定
			if ("".equals((String)UserInfoUtils.getUserInfo(getApplicationContext(),Constants.MOBILE, ""))) {
				intent = new Intent(this, BindPhoneNumberActivity.class);
				intent.putExtra("bindType", "phoneNumber");
				startActivity(intent);
			} else {
				intent = new Intent(this, BindNumberInfoActivitiy.class);
				startActivity(intent);
			}
			break;
		case R.id.rl_bind_wx:
			if (!OxygenApplication.api.isWXAppInstalled()) {
				Toast.makeText(BindActivity.this, "您还未安装微信客户端",
						Toast.LENGTH_SHORT).show();
				return;
			}
			OxygenApplication.weixinsdk = "login";
			SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "com.oxygen.www";
			UserInfoUtils.setUserInfo(getApplicationContext(),Constants.LOGIN_TO_BIND, true);
			OxygenApplication.api.sendReq(req);
			break;
		case R.id.rl_bind_qq:
			bindQQGetOpenId();
			break;
		}
	}

	private void bindQQGetOpenId() {
		mTencent = OxygenApplication.tencent;
		mTencent.login(this, "all", new BaseUiListener());
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			try {
				openId = ((JSONObject) response).getString("openid");
				if (!TextUtils.isEmpty(openId)) {
					bindQQ(openId);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onError(UiError e) {

		}

		@Override
		public void onCancel() {

		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case BIND_PRECHECK:
				if (msg.obj == null) {
					ToastUtil.show(BindActivity.this, "网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonObject = (JSONObject) msg.obj;
						if ("yes".equals(jsonObject.getJSONObject("data")
								.getString("account_in_used"))) {
							showBindDialog();
						} else {
							bind();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			case BIND_PHONE:
				JSONObject jsonObject = (JSONObject) msg.obj;
				try {
					if (jsonObject.getInt("status") == 200) {
						Toast.makeText(BindActivity.this, "绑定成功！",
								Toast.LENGTH_LONG).show();
						if ("qq".equals(bindType)) {
							UserInfoUtils.setUserInfo(BindActivity.this.getApplicationContext(),Constants.QQ_OPENID,
									openId);
						} else if ("wx".equals(bindType)) {
							UserInfoUtils.setUserInfo(BindActivity.this.getApplicationContext(),Constants.UNIONID,
									unionid);
						}
						initBindInfo();
					} else {
						Toast.makeText(BindActivity.this, "绑定失败！",
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(BindActivity.this,
							getResources().getString(R.string.errcode_wx),
							Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		}

	};

	public void bindQQ(String openId) {
		bindType = "qq";
		final Map<String, String> params = new HashMap<String, String>();
		params.put("type", "qq");
		params.put("qq_openid", openId);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.BIND_PRECHECK, handler,
						BIND_PRECHECK, params);
			}
		});
	}

	protected void showBindDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.dialog_bind, null);
		// 对话框
		final Dialog dialog = new AlertDialog.Builder(BindActivity.this)
				.create();
		dialog.show();
		dialog.getWindow().setContentView(layout);
		TextView invite_cancel = (TextView) layout
				.findViewById(R.id.invite_cancel);
		TextView invite_sure = (TextView) layout.findViewById(R.id.invite_sure);

		invite_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		invite_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				bind();
			}
		});
	}

	/**
	 * 绑定账号
	 */
	protected void bind() {
		final Map<String, String> params = new HashMap<String, String>();
		if ("qq".equals(bindType)) {
			params.put("type", "qq");
			params.put("qq_openid", openId);
		} else if ("wx".equals(bindType)) {
			params.put("type", "wechat");
			params.put("union_id", unionid);
		}
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.BIND_PHONE, handler, BIND_PHONE,
						params);

			}
		});
	}

	private void onEventMainThread(BindMobile info) {
		tv_phone_bind.setText((String)UserInfoUtils.getUserInfo(this, Constants.MOBILE, ""));
	}

	private void onEventMainThread(BindQQOrWX info) {
		// unionid o__CouM_OrPAkfI1VfWB5KKr5YXc
		unionid = info.getUnionid();
		if ("wx".equals(info.getBindType())) {
			bindType = "wx";
			final Map<String, String> params = new HashMap<String, String>();
			params.put("type", "wechat");
			params.put("union_id", unionid);
			OxygenApplication.cachedThreadPool.execute(new Runnable() {
				public void run() {
					HttpUtil.Post(UrlConstants.BIND_PRECHECK, handler,
							BIND_PRECHECK, params);
				}
			});
		}
	}

}
