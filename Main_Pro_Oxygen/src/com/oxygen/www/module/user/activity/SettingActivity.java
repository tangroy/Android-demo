package com.oxygen.www.module.user.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.OperationCanceledException;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.utils.AppManager;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.QQUtils;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.utils.WxUtil;
import com.xiaomi.account.openauth.XMAuthericationException;
import com.xiaomi.account.openauth.XiaomiOAuthConstants;
import com.xiaomi.account.openauth.XiaomiOAuthFuture;
import com.xiaomi.account.openauth.XiaomiOAuthResults;
import com.xiaomi.account.openauth.XiaomiOAuthorize;

/**
 * 设置界面
 * @author 杨庆雷
 * 2015-9-14上午9:43:22
 */
public class SettingActivity extends BaseActivity implements OnClickListener {
	protected static final int POST_VERSION_UPDATE = 1;
	protected static final int BIND_PRECHECK = 2;
	protected static final int BIND_PHONE = 3;
	private ImageView iv_back;
	private RelativeLayout rl_setting;
	private RelativeLayout rl_bind;
	private RelativeLayout rl_opinion;
	private RelativeLayout rl_score;
	private RelativeLayout rl_password;
	private RelativeLayout rl_share_lyd;
	private RelativeLayout rl_runset_lyd;
	/** 小米手环 */
	private RelativeLayout rl_mi;
	private RelativeLayout version_update;
	private RelativeLayout rl_about;
	private Button exit_login;
	private String url;
	private String description;
	private String version_code;
	private String need_upgrade;
	private int nativeVersionCode;
	private String file;
	private TextView update_version;
	/**
	 * 版本名称
	 */
	private String version_name;
	private PopupWindow popupWindow;
	private ImageView iv_share_weixin,iv_share_appfriend, iv_share_qq, iv_share_weixin_friends;
	
	/**
	 * 小米 token 获取
	 */
	XiaomiOAuthResults results;
	
	private static final int MI_DATA = 4;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		iv_back = (ImageView) findViewById(R.id.iv_back);
		rl_setting = (RelativeLayout) findViewById(R.id.rl_setting);
		rl_bind = (RelativeLayout) findViewById(R.id.rl_bind);
		rl_opinion = (RelativeLayout) findViewById(R.id.rl_opinion);
		rl_score = (RelativeLayout) findViewById(R.id.rl_score);
		rl_password = (RelativeLayout) findViewById(R.id.rl_password);
		rl_share_lyd = (RelativeLayout) findViewById(R.id.rl_share_lyd);
		rl_runset_lyd = (RelativeLayout) findViewById(R.id.rl_runset_lyd);
		rl_mi = (RelativeLayout) findViewById(R.id.rl_mi);
		version_update = (RelativeLayout) findViewById(R.id.version_update);
		rl_about = (RelativeLayout) findViewById(R.id.rl_about);
		exit_login = (Button) findViewById(R.id.exit_login);
		update_version = (TextView) findViewById(R.id.update_version);
	}

	private void initViewsEvent() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(this);
		rl_opinion.setOnClickListener(this);
		rl_bind.setOnClickListener(this);
		rl_score.setOnClickListener(this);
		rl_password.setOnClickListener(this);
		rl_share_lyd.setOnClickListener(this);
		rl_runset_lyd.setOnClickListener(this);
		rl_mi.setOnClickListener(this);
		version_update.setOnClickListener(this);
		rl_about.setOnClickListener(this);
		exit_login.setOnClickListener(this);
		rl_setting.setOnClickListener(this);
	}

	private void initValues() {
		if (!"".equals((String)(UserInfoUtils.getUserInfo(getApplicationContext(),Constants.NICKNAME, "")))) {
			exit_login.setVisibility(View.VISIBLE);
		}
		checkUpdate();
		
	
		
	}

	private void getToken() {
		// TODO Auto-generated method stub
		
		XiaomiOAuthFuture<XiaomiOAuthResults> future = new XiaomiOAuthorize()
				.setAppId(2882303761517403144L)
				.setRedirectUrl("http://leyundong.com/")
				.setScope(getScope())
				.startGetAccessToken(SettingActivity.this);
		// 如果是要获得Code的方式，则把startGetAccessToken改成startGetOAuthCode即可。其他相同
		// 参数解释：
		// appID : 开发者预先申请好的 AppID。
		// redirectUrl : 开发者预先申请时填好的 redirectUrl。
		// scope : int数组，可以用XiaomiOAuthConstants.SCOPE_*等常量
		// activity : 用于启动用户登陆/授权的Activity。

		waitAndShowFutureResult(future);
		
	}
	
	private <V> void waitAndShowFutureResult(final XiaomiOAuthFuture<V> future) {
		new AsyncTask<Void, Void, V>() {
			Exception e;

			 @Override
	            protected void onPreExecute() {
	                showResult("waiting for Future result...");
	            }

	            @Override
	            protected V doInBackground(Void... params) {
	                V v = null;
	                try {
	                    v = future.getResult();
	                } catch (IOException e1) {
	                    this.e = e1;
	                } catch (OperationCanceledException e1) {
	                    this.e = e1;
	                } catch (XMAuthericationException e1) {
	                    this.e = e1;
	                }
	                return v;
	            }

	            @Override
	            protected void onPostExecute(V v) {
	                if (v != null) {
	                    if (v instanceof XiaomiOAuthResults) {
	                        results = (XiaomiOAuthResults) v;
//	                        getdata(results);
	                        
	                        String access_token = results.getAccessToken();
	                        String mac_key = results.getMacKey();
	                    	SharedPreferences sp = getSharedPreferences("com.oxygen.www_", MODE_MULTI_PROCESS);
	                        sp.edit().putString("access_token", access_token).commit();
	                        sp.edit().putString("mac_key", mac_key).commit();
	                        
	                        Intent intent = new Intent(SettingActivity.this, MiSettingActivity.class);
	            			intent.putExtra("access_token", access_token);
	            			intent.putExtra("mac_key", mac_key);
	            			startActivity(intent);
	                        
//	                        Log.i("xiaomitoken", "token:"+results.getAccessToken());
//	                        Log.i("xiaomitoken", "mac_key:"+results.getMacKey());
	                   
	                    }
//	                    showResult(v.toString());
	                    
	                    
	                } else if (e != null) {
	                    showResult(e.toString());
	                } else {
	                    showResult("done and ... get no result :(");
	                }
	            }
	        }.execute();
	}
	 

	private void showResult(String text) {
		Log.i("xiaomi", "result:" + text);
	}
	
	private int[] getScope() {
		// TODO Auto-generated method stub
		int[] scopes = new int[]{XiaomiOAuthConstants.SCOPE_OPEN_ID, XiaomiOAuthConstants.SCOPE_PROFILE};
		return scopes;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		String targitUrl;
		String logoUrl;
		String info;
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.rl_setting:
			intent = new Intent(this, DataActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_bind:
			intent = new Intent(this, BindActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_password:
			if (!"".equals((String)(UserInfoUtils.getUserInfo(getApplicationContext(),Constants.MOBILE,"")))) {
				intent = new Intent(this, SetNewPswActivity.class);
				intent.putExtra("setNewPsw", "setNewPsw");
				startActivity(intent);
			}
			break;
		case R.id.rl_share_lyd:
			getPopupWindow(v);
			if (popupWindow != null) {
				popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}
			break;
		case R.id.rl_opinion:
			intent = new Intent(this, FeedBackActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_runset_lyd:
			 intent = new Intent(this, RunsettintActivity.class);
			 startActivity(intent);
			 break;
		case R.id.rl_mi:
			SharedPreferences sp = getSharedPreferences("com.oxygen.www_", MODE_MULTI_PROCESS);
			String access_token = sp.getString("access_token", "");
			String mac_key = sp.getString("mac_key", "");
			if (access_token.isEmpty() || mac_key.isEmpty()) {
				getToken();
			} else {
				intent = new Intent(this, MiSettingActivity.class);
				intent.putExtra("access_token", access_token);
				intent.putExtra("mac_key", mac_key);
				startActivity(intent);
			}
			break;

		case R.id.rl_score:
			break;
		case R.id.version_update:
			cilckable(version_update, false);
			// 0 表示 没有版本更新
			if (need_upgrade == null) {
				ToastUtil.show(SettingActivity.this, "检查版本更新失败");
				cilckable(version_update, true);
			} else if ("0".equals(need_upgrade)) {
				ToastUtil.show(SettingActivity.this, "当前已是最新版本！");
				cilckable(version_update, true);
			} else {
				if (Integer.parseInt(version_code) > nativeVersionCode) {
					uploadVersionDialog();
				} else {
					ToastUtil.show(SettingActivity.this, "当前已是最新版本！");
					cilckable(version_update, true);
				}
			}
			break;
		case R.id.rl_about:
			intent = new Intent(this, AboutSportActivity.class);
			startActivity(intent);
			break;
		case R.id.exit_login:
			clearUserInfo();
			if (!OxygenApplication.tencent.isSessionValid()) {
				OxygenApplication.tencent.logout(this);
			}
			break;
		case R.id.iv_share_qq:
			dismissPopWindow();
			targitUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.oxygen.www&g_f=991653";
			logoUrl = "http://leyundong.com/images/logo.png";
			info = "一起运动更快乐！快加入我们吧！";
			QQUtils.doShareToQQ(this,"乐运动",logoUrl,info,targitUrl);
			break;
		case R.id.iv_share_weixin:
			dismissPopWindow();
			targitUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.oxygen.www&g_f=991653";
			logoUrl = "http://leyundong.com/images/logo.png";
			info = "一起运动更快乐！快加入我们吧！";
			WxUtil.share2weixin(0, SettingActivity.this, targitUrl,"乐运动", "logoUrl",info);
			break;
		case R.id.iv_share_weixin_friends:
			dismissPopWindow();
			targitUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.oxygen.www&g_f=991653";
			logoUrl = "http://leyundong.com/images/logo.png";
			info = "一起运动更快乐！快加入我们吧！";
			WxUtil.share2weixin(1, SettingActivity.this, targitUrl,"乐运动", "logoUrl",info);
			break;
			
		default:
			break;
		}
	}

	private void cilckable(View view, boolean b) {
		view.setEnabled(b);
		view.setFocusable(b);
		view.setClickable(b);
	}
	
	/***
	 * 获取PopupWindow实例
	 */
	private void getPopupWindow(View v) {
		if (null != popupWindow) {
			popupWindow.dismiss();
			return;
		} else {
			initPopuptWindow(v);
		}
	}
	
	protected void initPopuptWindow(View v) {
		// TODO Auto-generated method stub
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(
				R.layout.pop_challengesdetail, null, false);
		initPopViews(popupWindow_view);
		
		// 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		// 点击其他地方消失
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
	}
	
	private void initPopViews(View popupWindow_view) {
		iv_share_appfriend = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_appfriend);
		iv_share_weixin = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_weixin);
		iv_share_qq = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_qq);
		iv_share_weixin_friends = (ImageView) popupWindow_view
				.findViewById(R.id.iv_share_weixin_friends);
		TextView cancel_challenges = (TextView) popupWindow_view
				.findViewById(R.id.cancel_challenges);
		TextView exit_challenges = (TextView) popupWindow_view
				.findViewById(R.id.exit_challenges);
		TextView challenges_QR = (TextView) popupWindow_view
				.findViewById(R.id.challenges_QR);
		TextView challenges_data = (TextView) popupWindow_view
				.findViewById(R.id.challenges_data);
		
		iv_share_appfriend.setVisibility(View.GONE);
		cancel_challenges.setVisibility(View.GONE);
		exit_challenges.setVisibility(View.GONE);
		challenges_QR.setVisibility(View.GONE);
		challenges_data.setVisibility(View.GONE);
		
		iv_share_weixin.setOnClickListener(this);
		iv_share_qq.setOnClickListener(this);
		iv_share_weixin_friends.setOnClickListener(this);
	}
	
	/**
	 * 关闭分享的弹窗
	 */
	protected void dismissPopWindow() {
		// TODO Auto-generated method stub
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}
	
	/**
	 * 退出登录 删除本地保存的个人信息
	 */
	private void clearUserInfo() {
		showExitDialog();
	}

	/**
	 * 退出登录的对话框
	 */
	private void showExitDialog() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.exit_login));
		builder.setPositiveButton(getResources().getString(R.string.sure),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						UserInfoUtils.setUserInfo(getApplicationContext(),Constants.USERID, "");
						if (((String)UserInfoUtils.getUserInfo(getApplicationContext(), Constants.USERID,""))
								.equals("")) {
							ToastUtil.show(SettingActivity.this, "退出登录成功");
							UserInfoUtils.clearUserInfo(SettingActivity.this);
							Intent intent = new Intent(SettingActivity.this,
									LoginActivity.class);
							startActivity(intent);
							for (int i = 0; i < AppManager.activityStack.size(); i++) {
								AppManager.activityStack.get(i).finish();
							}
						} else {
							ToastUtil.show(SettingActivity.this, "退出登录失败");
						}
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();
	}

	/**
	 * 检测更新
	 */
	private void checkUpdate() {
		nativeVersionCode = GDUtil.GetVersionCode(this);
		final Map<String, String> params = new HashMap<String, String>();
		params.put("type", "android");
		params.put("version_code", 1 + "");
		HttpUtil.Post(UrlConstants.POST_VERSION_UPDATE, handler,
				POST_VERSION_UPDATE, params);
	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case POST_VERSION_UPDATE:
				if (msg.obj == null) {
					ToastUtil.show(SettingActivity.this, "网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonObject = (JSONObject) msg.obj;
						need_upgrade = jsonObject.getString("need_upgrade");
						if ("0".equals(need_upgrade)) {
							update_version.setText("检查更新");
						} else {
							if (jsonObject.getInt("status") == 200) {
								if (!jsonObject.isNull("data")) {
									String data = jsonObject.getString("data");
									jsonObject = new JSONObject(data);
									url = jsonObject.getString("url");
									description = jsonObject
											.getString("description");
									version_name = jsonObject
											.getString("version_name");
									version_code = jsonObject
											.getString("version_code");
									if (nativeVersionCode < Integer
											.parseInt(version_code)) {
										update_version.setText("检查更新(发现新版本)");
									}
								}
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
				
			case MI_DATA:
				if (msg.obj == null) {
					Log.i("xiaomidata", "出错了");
				} else {
					Log.i("xiaomidata", (String)msg.obj);
				}				
				break;
				
			default:
				break;
			}
		}

	};
	
	/**
	 * 选择是否下载新版本的对话框
	 */
	public void uploadVersionDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.dialog_version_check, null);
		// 对话框
		final Dialog dialog = new AlertDialog.Builder(SettingActivity.this)
				.create();
		dialog.show();
		dialog.getWindow().setContentView(layout);
		TextView dialog_title = (TextView) layout
				.findViewById(R.id.dialog_title);
		TextView dialog_verson_code = (TextView) layout
				.findViewById(R.id.dialog_verson_code);
		TextView dialog_des = (TextView) layout.findViewById(R.id.dialog_des);
		TextView invite_cancel = (TextView) layout
				.findViewById(R.id.invite_cancel);
		TextView invite_sure = (TextView) layout.findViewById(R.id.invite_sure);

		dialog_title
				.setText(getResources().getString(R.string.find_newversion));
		dialog_verson_code.setText("最新版本号：" + version_name);
		dialog_des.setText(description);
		invite_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				cilckable(version_update, true);
			}
		});
		invite_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkUpdataVersion();
				dialog.dismiss();
			}
		});
	}

	/**
	 * 访问服务器 下载新版本
	 */
	protected void checkUpdataVersion() {
		if (Environment.getExternalStorageDirectory().equals(
				Environment.MEDIA_MOUNTED)) {
			file = Environment.getExternalStorageDirectory().toString()
					+ "/Oxygen.apk";
		} else {
			file = Environment.getDataDirectory() + "/Oxygen.apk";
		}
		File files = new File(file);
		if (files.exists()) {
			files.delete();
		}
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.download(url,// 接口地址
				file,// 文件路径
				true,// 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
				false,// 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
				new RequestCallBack<File>() {
					private ProgressDialog progressDialog;

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						progressDialog.setMax((int) total);
						progressDialog.setProgress((int) current);
					}

					@Override
					public void onStart() {
						super.onStart();
						progressDialog = new ProgressDialog(
								SettingActivity.this);
						progressDialog
								.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						progressDialog.setTitle("下载");
						progressDialog.setMessage("正在下载");
						progressDialog.show();

					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						progressDialog.dismiss();
						ToastUtil.show(SettingActivity.this, "下载完成");
						installApk();
						cilckable(version_update, true);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						progressDialog.dismiss();
						ToastUtil.show(SettingActivity.this, "更新失败");
						cilckable(version_update, true);
					}
				});
	}

	protected void installApk() {
		Uri uri = Uri.fromFile(new File(file));
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		startActivity(intent);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

}
