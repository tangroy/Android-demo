package com.oxygen.www.module.sport.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Event;
import com.oxygen.www.enties.GDMessage;
import com.oxygen.www.module.sport.adapter.EventsMessageAdapter;
import com.oxygen.www.module.sport.construct.MessageConstruct;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;

/**
 * 群发消息界面
 * @author 杨庆雷
 * 2015-6-11下午3:38:53
 */
public class EventSendMessageActivity extends BaseActivity implements
		OnClickListener {
	/**
	 * 请求网络, 获取历史消息列表数据
	 */
	public static final int NET_MESSAGE_LIST = 1;
	/**
	 * 请求网络, 发送消息
	 */
	public static final int NET_SENMESSAGE = 2;

	private ImageView iv_back;
	private TextView btn_send;
	private EditText et_message;
	private Event event;
	private RelativeLayout rl_loading;
	/**
	 * 历史消息
	 */
	private ListView lv_message;
	/**
	 * 历史消息列表数据
	 */
	private List<GDMessage> messageList;
	/**
	 * 群发消息历史列表的 adapter
	 */
	private EventsMessageAdapter adapter;
	/**
	 * 当前页
	 */
	private int page = 1;
	/**
	 * 每页获取数据数
	 */
	private int limit = 20;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eventsendmessage);
		initViews();
		initViewsEvent();
		initValues();
 	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		btn_send = (TextView) findViewById(R.id.send_message);
		et_message = (EditText) findViewById(R.id.et_message);
		lv_message = (ListView) findViewById(R.id.lv_message);
		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		btn_send.setOnClickListener(this);
	}
 
	private void initValues() {
		event= (Event) getIntent().getSerializableExtra("event");
		getHistoryMessage();
	}

	/**
	 * 获取历史消息
	 */
	private void getHistoryMessage() {
		rl_loading.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				HttpUtil.Get(UrlConstants.EVENT_MSG_HISTORY + event.getId() + ".json?" 
						+ "page=" + page + "&limit=" + limit, handler, NET_MESSAGE_LIST);
			}
		});
	}

	/**
	 * 发送群消息
	 */
	private void Sendmessage() {
		rl_loading.setVisibility(View.VISIBLE);
		// POST 参数
		final Map<String, String> params = new HashMap<String, String>();
		params.put("sender", event.getCreated_by()+"");
		params.put("status", "accept");
		params.put("content", et_message.getText().toString().trim());
 		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				HttpUtil.Post(UrlConstants.EVENTS_NOTIFY
						+event.get_id()+ ".json", handler,
						NET_SENMESSAGE, params);
			}
		});

	}
	
	/**
	 * 刷新列表数据
	 */
	private void notifylist(List<GDMessage> messages) {
		if (messageList == null) {
			messageList = messages;
			adapter = new EventsMessageAdapter(EventSendMessageActivity.this, messageList);
			lv_message.setAdapter(adapter);
		}else {
			messageList.addAll(messages);
			adapter.notifyDataSetChanged();
		}
		
//		lv_message.setSelection((page - 1) * 20);
//		page = page + 1;

	}

	@Override
	public void onClick(View arg0) {
		
		switch (arg0.getId()) {
		
		case R.id.iv_back:
			// 返回
			finish();
			break;
		
		case R.id.send_message:
			// 发送群消息
			if (!TextUtils.isEmpty(et_message.getText())) {
				btn_send.setEnabled(false);
				Sendmessage();
			}
			break;
		
		default:
			break;
		}
	}
	
	@SuppressLint("HandlerLeak") 
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			
			case NET_MESSAGE_LIST:
				// 历史消息
				if (msg.obj == null) {
					ToastUtil.show(EventSendMessageActivity.this, "网络连接不可用，请稍后重试");
				} else {
					String strObject = (String) msg.obj;
					try {
						JSONObject jsonObject = new JSONObject(strObject);
						
						if (jsonObject.getInt("status") == 200) {
							
							List<GDMessage> messages = MessageConstruct.Toactivitylist(jsonObject);
							
							if (messages != null && messages.size() > 0) {
								notifylist(messages);
							} 
							
						} else {
							ToastUtil.show(EventSendMessageActivity.this, jsonObject.getString("msg"));
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				rl_loading.setVisibility(View.GONE);
				break;
			
			case NET_SENMESSAGE:
				// 发送消息
				if (msg.obj == null) {
					btn_send.setEnabled(true);
					ToastUtil.show(EventSendMessageActivity.this, "网络连接不可用，请稍后重试");
				} else {
					JSONObject jsonobeObject = (JSONObject) msg.obj;
					try {
						if (jsonobeObject.getInt("status") == 200) {
							 finish();
						} else {
							ToastUtil.show(EventSendMessageActivity.this,
									jsonobeObject.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				rl_loading.setVisibility(View.GONE);
				break;
			
			default:
				break;
			}

		}
	};

	
}
