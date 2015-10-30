package com.oxygen.www.module.user.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;


/**
 * 意见反馈界面
 * @author 杨庆雷
 * 2015-9-15下午4:56:24
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener {
	protected   final int FEED_BACK = 1;
	private ImageView iv_back;
	private TextView tv_nav_save;
	private EditText et_feefback;
	private String feedbackInfo;
	private Map<String, String> params;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_nav_save = (TextView) findViewById(R.id.tv_nav_save);
		et_feefback = (EditText) findViewById(R.id.et_feefback);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_nav_save.setOnClickListener(this);
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
			feedbackInfo = et_feefback.getText().toString().trim();
			if(TextUtils.isEmpty(feedbackInfo)){
				ToastUtil.show(this, "意见反馈不能为空");
				return;
			}
			params = new HashMap<String, String>();
			params.put("content", feedbackInfo);
			OxygenApplication.cachedThreadPool.execute(new Runnable() {
				public void run() {
					HttpUtil.Post(UrlConstants.FEED_BACK, handler,
							FEED_BACK, params);
				}
			});
			;
			break;
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case FEED_BACK:
				if (msg.obj == null) {
					ToastUtil.show(FeedBackActivity.this, "网络连接不可用，请稍后重试");
				} else {
					JSONObject jsonObject = (JSONObject) msg.obj;
					if (!jsonObject.isNull("status")) {
						try {
							if (jsonObject.getInt("status") == 200) {
								ToastUtil.show(
										FeedBackActivity.this,
										getResources().getString(
												R.string.feedback_success));
								FeedBackActivity.this.finish();
							} else {
								ToastUtil.show(
										FeedBackActivity.this,
										getResources().getString(
												R.string.feedback_success));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				break;

			default:
				break;
			}
		}

	};
}
