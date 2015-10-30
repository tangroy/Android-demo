package com.oxygen.www.module.sport.activity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Bitmap;

import com.oxygen.www.R;
import com.oxygen.www.module.challengs.activity.ChallengesDetailActivity;
import com.oxygen.www.module.sport.zbar.CaptureActivity;
import com.oxygen.www.utils.TextUtils;
import com.oxygen.www.widget.MyWebView;

/**
 * 富文本
 * 
 * @author 张坤
 *
 */
public class CreatePlanRichActivity extends Activity implements OnClickListener {
	
	private static final int requestCodeScan = 10;
	
	private RelativeLayout rl_top;
	private RelativeLayout rl_preview;
	private ImageView iv_back;
	private TextView tv_title;
	private TextView tv_step11;
	private TextView tv_click;
	private TextView tv_save;
	private TextView tv_clear;
	private MyWebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createplan_rich);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		rl_top = (RelativeLayout) findViewById(R.id.rl_top);	
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_step11 = (TextView) findViewById(R.id.tv_step11);
		tv_click = (TextView) findViewById(R.id.tv_click);
		webView = (MyWebView) findViewById(R.id.webview);
		
		tv_save = (TextView) findViewById(R.id.tv_save);
		tv_clear = (TextView) findViewById(R.id.tv_clear);
		rl_preview = (RelativeLayout) findViewById(R.id.rl_preview);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_click.setOnClickListener(this);
		tv_save.setOnClickListener(this);
		tv_clear.setOnClickListener(this);
		initWebView();
	}

	@SuppressLint("SetJavaScriptEnabled") 
	private void initWebView() {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		// 只让本应用程序的webview加载网页，而不调用外部浏览器
		webView.setWebViewClient(new WebViewClient(){
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("leyundong://")) {
					// leyundong://event/123
					
					Intent intent;
					int id;
					id = Integer.parseInt(url.substring(url.lastIndexOf("/") +1));
					if (url.contains("event")) {
						intent = new Intent(CreatePlanRichActivity.this, EventsResultActivity.class);
						intent.putExtra("eventid", id);
						startActivity(intent);
						
					} else if (url.contains("challenge")) {
						intent = new Intent(CreatePlanRichActivity.this, ChallengesDetailActivity.class);
						intent.putExtra("challengesid", id);
						startActivity(intent);
					}
					
				} else {
					view.loadUrl(url);
				}
				return true;
			}
		});
	}

	private void initValues() {
		Intent intent = getIntent();
		String target_url = intent.getStringExtra("target_url");
//		String htmldata = intent.getStringExtra("htmldata");
		body = intent.getStringExtra("htmldata");
		int event_id = intent.getIntExtra("event_id", 0);
		boolean hasTitle = intent.getBooleanExtra("hasTitle", false);
		if (hasTitle) {
			rl_top.setVisibility(View.VISIBLE);
			tv_title.setText(intent.getStringExtra("title"));
		}
		if (target_url != null) {
			rl_preview.setVisibility(View.VISIBLE);
			webView.loadUrl(target_url);
		} else if (!TextUtils.isEmpty(body)) {
			
			if (event_id != 0) {
				tv_step11.setText("http://leyundong.com/rich"+"/"+event_id);
			}
			
			rl_preview.setVisibility(View.VISIBLE);

			//			webView.loadData(htmldata, "text/html; charset=UTF-8", null);
			
			// 补全
			html = "<html> <head> <meta charset= \"utf-8\"> <meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1, user-scalable=no\"> </head>  <body>";
			html += body;
			html += "</body> </html>";

			webView.loadData(html, "text/html; charset=UTF-8", null);
			
		} else {
			
		}
		webView.setBackgroundColor(getResources().getColor(R.color.transparent3));
		
	}

	
	 @Override
	 public void onDestroy() {
		 super.onDestroy();
		 if(webView != null) {
			 try {
				 webView.removeAllViews();
				 webView.destroy();
				 webView = null;
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }
//		 System.exit(0);
		 
	 }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}
	/**
	 * 确定退出
	 */
	private void exit() {
		// 对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setMessage("确认清除图文编辑的所有内容?");
		builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   // User clicked OK button
		        	   
		        	Intent intent = new Intent();
		   			intent.putExtra("html", "");
		   			setResult(RESULT_OK, intent);

		   			dialog.dismiss();
		   			finish();
		        	   
		        	   
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
	

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;

		case R.id.tv_click:
			intent = new Intent(this, CaptureActivity.class);
			startActivityForResult(intent, requestCodeScan);
			break;
			
		case R.id.tv_save:
			// TODO...
			intent = new Intent();
//			intent.putExtra("html", html);
			intent.putExtra("html", body);
            setResult(RESULT_OK, intent);
            finish();
            break;

		case R.id.tv_clear:
			// TODO...
			exit();
			break;

		default:
			break;
		}
	}
	
	private String scanResult;
	private String body;
	private String html;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == requestCodeScan) {
			if (data != null) {
				scanResult = data.getStringExtra("scanResult");
				if (scanResult != null) {
					if (scanResult.equals("null")) {
//						Log.i("scan", "scanResult: "+"没有结果");
				
					} else {
//						Log.i("scan", "scanResult: "+scanResult);
						
						rl_preview.setVisibility(View.VISIBLE);
						webView.getSettings().setDefaultTextEncodingName("utf-8");
						webView.setBackgroundColor(getResources().getColor(R.color.white));
//						webView.loadUrl(scanResult);
						
						new Thread(){
							public void run() {
								String result = doGetConnect(scanResult);
								
								/*try {
									writeFile(fileName, result);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}*/
								
								body = result;
								Log.i("html", "body----> "+body);
								
								
								// 最终保存字符串
								html = "<html> <head> <meta charset= \"utf-8\"> <meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1, user-scalable=no\"> </head>  <body>";
								
								html += body;
								html += "</body> </html>";
								
//								Log.i("scan", "html----> "+html);
								
								Message message = Message.obtain();
								mHandler.sendEmptyMessage(0);
							};
							
						}.start();
						
//						webView.loadData(html,  "text/html", "utf_8");
					}
				}
				
				
			}
			
		}
	}
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			
			/*String html = "<html> <head> <meta charset= \"utf-8\"> </head>  <body>";
			html += body;
			html += "</body> </html>";*/
			
//			Log.i("html", html);
			
			webView.loadData(html, "text/html; charset=UTF-8", null);
			
			
		};
	};
	
	/**
	 * 读取 html 内容
	 * 
	 * @param url
	 * @return
	 */
	private String doGetConnect(String url) {
		String result = "";
		InputStream is = null;

		HttpGet httpRequest = new HttpGet(url);
		// HttpGet httpRequest = new HttpGet(url+"/index.html");
		try {
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) { // 正确

				is = httpResponse.getEntity().getContent();
				// is = httpResponse.getEntity().getContent();
				byte[] data = new byte[1024];
				int n = -1;
				ByteArrayBuffer buf = new ByteArrayBuffer(10 * 1024);
				while ((n = is.read(data)) != -1)
					buf.append(data, 0, n);
				result = new String(buf.toByteArray(), "utf-8");
				// Log.i("result==", result);
				is.close();
				return result;
			} else {
				// Log.i("tip==", "error response code");
			}
		} catch (Exception e) {
			// Log.e("error==", "" + e.getMessage());
		}
		return null;
	}

	private String fileName = "scanResult";

	/**
	 * 写入文件
	 * 
	 * @param fileName
	 * @param writestr
	 * @throws IOException
	 */
	public void writeFile(String fileName, String writestr) throws IOException {
		try {

			FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);

			byte[] bytes = writestr.getBytes();

			fout.write(bytes);

			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
		 
}
	 
	
