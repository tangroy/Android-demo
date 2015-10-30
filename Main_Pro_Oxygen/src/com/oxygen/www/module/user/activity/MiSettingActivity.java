package com.oxygen.www.module.user.activity;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.xiaomi.account.openauth.XiaomiOAuthResults;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 *  绑定小米手环页面
 *  
 * @author 张坤
 *
 */
public class MiSettingActivity extends Activity implements OnClickListener {

	private static final int MI_DATA = 1;
	private static final int SYN_WEARABLE_DATA = 2;
	
	private ImageView iv_back;
	private TextView tv_unbind;
	private TextView tv_syn;
	private TextView tv_syn_time;
	private TextView tv_click;
	private ProgressBar progressBar;
	
	/**
	 * 时间格式化器
	 */
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
	/**
	 * 时间格式化器2
	 */
	DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	
	XiaomiOAuthResults results;
	private String access_token;
	private String mac_key;
	/**
	 * 小米服务器获取到的数据
	 */
	private String data;
	protected SharedPreferences sp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mi_setting);

		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_unbind = (TextView) findViewById(R.id.tv_unbind);
		tv_syn = (TextView) findViewById(R.id.tv_syn);
		tv_syn_time = (TextView) findViewById(R.id.tv_syn_time);
		tv_click = (TextView) findViewById(R.id.tv_click);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_unbind.setOnClickListener(this);
		tv_click.setOnClickListener(this);
	}

	private void initValues() {
		
		access_token = getIntent().getStringExtra("access_token");
		mac_key = getIntent().getStringExtra("mac_key");
		
		sp = getSharedPreferences("com.oxygen.www_", MODE_MULTI_PROCESS);
		String time = sp.getString("synmidatatime", "");
		if (!time.isEmpty() && time.substring(0, 10).equals(df2.format(new Date(System.currentTimeMillis())))) {
			// 已经同步
			tv_syn.setTextColor(getResources().getColor(R.color.xiaomi2));
			tv_syn.setText("同步已完成");
			tv_syn_time.setVisibility(View.VISIBLE);
			tv_syn_time.setText("最后更新时间: "+time);
			
		} else {
			// 今天未同步
			tv_syn.setTextColor(getResources().getColor(R.color.xiaomi1));
			tv_syn.setText("今天尚未同步");
			tv_syn_time.setVisibility(View.GONE);
			
		}
	}
	
	/**
	 * 获取数据
	 * 
	 * @param result
	 */
	protected void getdata() {
		// TODO Auto-generated method stub
//		参数名	描述
//		appid	应用id（在小米开发者平台注册应用分配的）
//		third_appid	由小米手环分发给第三方应用的请求id
//		third_appsecret	由小米手环分发给第三方应用的请求秘钥
//		mac_key	通过小米账号 Oauth2.0 用户登陆获取
//		call_id	时间戳，系统时间的秒值,同个应用的不同api请求的time值应该是递增的, 用于防replay攻击
//		access_token 	通过小米账号 Oauth2.0 用户登陆获取
//		fromdate	查询开始日期格式yyyy-MM-dd
//		todate	查询结束日期yyyy-MM-dd
//		v	API的版本号，请设置成 1.0.
//		l	小写的L，系统语言，请设置成english
		
		progressBar.setVisibility(View.VISIBLE);
		
		final String appid = "2882303761517403144";
		final String third_appid = "1444359994";
		final String third_appsecret = "Y2YyMjRlODhmODMzMWVhZWQyNjc3YWZkN2VjNWU5OTU";
		final String call_id = ""+System.currentTimeMillis();
		final String fromdate = df2.format(new Date(System.currentTimeMillis()));
		final String todate = df2.format(new Date(System.currentTimeMillis()));
		final String v = "1.0";
		final String l = "english";
		
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = "https://hmservice.mi-ae.com.cn/user/summary/getData";
				url += ("?appid=" + appid);
				url += ("&third_appid=" + third_appid);
				url += ("&third_appsecret=" + third_appsecret);
				url += ("&mac_key=" + mac_key);
				url += ("&call_id=" + call_id);
				url += ("&access_token=" + access_token);
				url += ("&fromdate=" + fromdate);
				url += ("&todate=" + todate);
				url += ("&v=" + v);
				url += ("&l=" + l);
				
//				Log.i("xiaomi", "请求url:"+url);
				
				HttpUtil.Get_Noauth(url, handler, MI_DATA);

			}
		});
		
	}

	

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		
		case R.id.iv_back:
			finish();
			break;

		case R.id.tv_unbind:
			// TODO...
			showDialog();
			break;
			
		case R.id.tv_click:
			// 获取数据并同步到服务器
			getdata();
			break;
			
		default:
			break;
		}

	}
	
	/**
	 * * 同步可穿戴设备的数据 484: *
	 * API:http://HOST/activities/sync_wearable_data.json 485: 
	 * HTTP POST
	 * parameters: 
	 * <source: 数据来源, e.g. xiaomi, huawei...，必选参数>
	 * <source_data: 存放穿戴设备提供的数据，以json的形式传递，必选参数>  
	 * <date:查询日期，必选参数> 
	 *  @return void
	 */
	private void syn() {
		// TODO Auto-generated method stub
		
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Map<String, String> params = new HashMap<String, String>();
				params.put("source", "xiaomi");
				params.put("source_data", data);
				params.put("date", df2.format(new Date(System.currentTimeMillis())));
				
				HttpUtil.Post(UrlConstants.SYN_WEARABLE_DATA, handler, SYN_WEARABLE_DATA, params);

			}
		});
		
		
	}

	

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case MI_DATA:
				if (msg.obj == null) {
					progressBar.setVisibility(View.GONE);
					Log.i("xiaomidata", "出错了");
				} else {
					Log.i("xiaomidata", (String) msg.obj);
					
					try {
						JSONObject obj = new JSONObject((String)msg.obj);
						if (1 == obj.getInt("code")) {
							
							JSONArray jsonArray = obj.getJSONArray("data");

							if (jsonArray != null && jsonArray.length()>0) {
								// 有今天的数据
								data = (String) msg.obj;
								// 同步到服务器
								syn();
								
							} else {
								progressBar.setVisibility(View.GONE);
								// 今天没有上传数据
								tv_syn_time.setVisibility(View.GONE);
								
								tv_syn.setTextColor(getResources().getColor(R.color.xiaomi1));
								tv_syn.setText("同步失败, 请先将数据上传到云端");
								
							}
							
						} else if (0 == obj.getInt("code")) {
							progressBar.setVisibility(View.GONE);
							// 今天没有上传数据
							tv_syn_time.setVisibility(View.GONE);
							
							tv_syn.setTextColor(getResources().getColor(R.color.xiaomi1));
							tv_syn.setText("同步失败, 请先将数据上传到云端");
							
						} else {
							progressBar.setVisibility(View.GONE);
							Log.i("xiaomitoken", "token 错误");
							
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				break;
				
			case SYN_WEARABLE_DATA:
				progressBar.setVisibility(View.GONE);
				if (msg.obj != null) {
//					Log.i("synxiaomi", (String) msg.obj);
					JSONObject obj = (JSONObject) msg.obj;
					
					Log.i("synxiaomi", obj.toString());
					
					try {
						if (200 == obj.getInt("status")) {
							// 同步陈功
							tv_syn.setTextColor(getResources().getColor(R.color.xiaomi2));
							tv_syn.setText("同步已完成");
							tv_syn_time.setVisibility(View.VISIBLE);
							String syntime = df.format(new Date(System.currentTimeMillis()));
							tv_syn_time.setText("最后更新时间: "+ syntime);
							
							// 保存同步时间
							sp.edit().putString("synmidatatime", syntime).commit();
							
						} else {
							// toast
							ToastUtil.show(MiSettingActivity.this, "网络异常, 请检查网络并重试!");
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

		};
	};
	
	/**
	 * 确定退出
	 */
	private void showDialog() {
		// 对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		
		builder.setMessage("解绑后手环记录的数据将不会同步");
		builder.setPositiveButton("解绑", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   // User clicked OK button
		        	   dialog.dismiss();
		        	   unbind();
		           }
		       });
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User cancelled the dialog
		        	   dialog.dismiss();
		           }
		       });

		AlertDialog dialog = builder.create();
		builder.show();
		
	}

	/**
	 * 解除绑定
	 */
	protected void unbind() {
		// 清楚数据
		sp.edit().remove("access_token").commit();
		sp.edit().remove("mac_key").commit();
		sp.edit().remove("synmidatatime").commit();

		ToastUtil.show(this, "小米手环已成功解除绑定");
		finish();
	}
	
	
}
