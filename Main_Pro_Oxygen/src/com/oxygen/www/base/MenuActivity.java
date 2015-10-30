package com.oxygen.www.base;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.module.sport.activity.EventsIndexFragment;
import com.oxygen.www.module.sport.activity.FeedFragment;
import com.oxygen.www.module.sport.activity.SportStartActivity;
import com.oxygen.www.module.team.activity.TeamIndexFragment;
import com.oxygen.www.module.user.activity.UserInfoFragment;
import com.oxygen.www.utils.AppManager;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;

public class MenuActivity extends BaseActivity implements View.OnClickListener {

	private static final int POST_VERSION_UPDATE = 0;
	private ImageView nav_sport, nav_feed, nav_startsport, nav_group, nav_me;// 底部菜单icon
	private Context context;
	private long exitTime = 0;
	public static FragmentManager fm;

	/**
	 * 本地的版本号
	 */
	private int nativeVersionCode;
	/**
	 * 版本号
	 */
	private String version_code;
	/**
	 * 版本描述
	 */
	private String description;
	/**
	 * 版本名称
	 */
	private String version_name;
	/**
	 * 新版本下载地址
	 */
	private String url;
	private String file;
	private EventsIndexFragment eventf;
	private FeedFragment feedf;
	private TeamIndexFragment groupf;
	private UserInfoFragment userf;
	/**
	 * FeedFragment 评论框
	 */
	public static LinearLayout ll_comment;
	public static EditText et_comment;
	public static Button bt_comment;
	public static ImageView iv_message_red;

	/**
	 * 运营页
	 */
	public RelativeLayout rl_adbg, rl_ad;
	public ImageView iv_indexad_close, iv_ad;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homemain);
		context = this;
		fm = getSupportFragmentManager();
		initViews();
		initViewsEvent();
		initValues();
		GDUtil.setGlobal(this, "update_loca_data", true);
		if (OxygenApplication.detection_update) {
			OxygenApplication.detection_update = false;
			checkUpdate();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	public void initViews() {
		nav_sport = (ImageView) findViewById(R.id.nav_sport);
		nav_feed = (ImageView) findViewById(R.id.nav_feed);
		nav_startsport = (ImageView) findViewById(R.id.nav_startsport);
		nav_group = (ImageView) findViewById(R.id.nav_group);
		nav_me = (ImageView) findViewById(R.id.nav_me);

		ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
		et_comment = (EditText) findViewById(R.id.et_comment);
		bt_comment = (Button) findViewById(R.id.bt_comment);
		rl_adbg = (RelativeLayout) findViewById(R.id.rl_adbg);
		rl_ad = (RelativeLayout) findViewById(R.id.rl_ad);
		iv_indexad_close = (ImageView) findViewById(R.id.iv_indexad_close);
		iv_ad = (ImageView) findViewById(R.id.iv_ad);
		iv_message_red = (ImageView) findViewById(R.id.iv_message_red);

	}

	public void initViewsEvent() {
		nav_sport.setOnClickListener(this);
		nav_feed.setOnClickListener(this);
		nav_startsport.setOnClickListener(this);
		nav_group.setOnClickListener(this);
		nav_me.setOnClickListener(this);
		iv_indexad_close.setOnClickListener(this);
		rl_adbg.setOnClickListener(this);
		iv_ad.setOnClickListener(this);
	}

	public void initValues() {
		eventf = new EventsIndexFragment();
		feedf = new FeedFragment();
		groupf = new TeamIndexFragment();
		userf = new UserInfoFragment();
		changeScrollView(eventf, nav_sport, true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nav_sport:
			changeScrollView(eventf, nav_sport, false);
			break;
		case R.id.nav_feed:
			changeScrollView(feedf, nav_feed, false);
			iv_message_red.setVisibility(View.GONE);
			// startActivity(new Intent(this, FeedActivity.class));
			break;
		case R.id.nav_startsport:
			changeScrollView(null, nav_startsport, false);
			break;
		case R.id.nav_group:
			changeScrollView(groupf, nav_group, false);
			break;
		case R.id.nav_me:
			changeScrollView(userf, nav_me, false);
			break;
		case R.id.iv_indexad_close:
			rl_adbg.setVisibility(View.GONE);
			break;

		default:
			break;
		}

	}

	private void changeScrollView(Fragment targetFragment, View v, boolean init) {
		if (v.equals(nav_sport)) {
			nav_sport.setImageResource(R.drawable.nav_sport_down);
			nav_feed.setImageResource(R.drawable.nav_feed_normal);
			nav_group.setImageResource(R.drawable.nav_group_narmal);
			nav_me.setImageResource(R.drawable.nav_me_normal);
			nav_startsport.setImageResource(R.drawable.nav_startsport_normal);

		} else if (v.equals(nav_feed)) {
			nav_sport.setImageResource(R.drawable.nav_sport_normal);
			nav_feed.setImageResource(R.drawable.nav_feed_down);
			nav_group.setImageResource(R.drawable.nav_group_narmal);
			nav_me.setImageResource(R.drawable.nav_me_normal);
			nav_startsport.setImageResource(R.drawable.nav_startsport_normal);

		} else if (v.equals(nav_startsport)) {
			Intent intent = new Intent(context, SportStartActivity.class);
			intent.putExtra("type", Constants.SPORTTYPE_CREATED);
			startActivity(intent);

		} else if (v.equals(nav_group)) {
			nav_sport.setImageResource(R.drawable.nav_sport_normal);
			nav_feed.setImageResource(R.drawable.nav_feed_normal);
			nav_group.setImageResource(R.drawable.nav_group_down);
			nav_me.setImageResource(R.drawable.nav_me_normal);
			nav_startsport.setImageResource(R.drawable.nav_startsport_normal);

		} else if (v.equals(nav_me)) {
			nav_sport.setImageResource(R.drawable.nav_sport_normal);
			nav_feed.setImageResource(R.drawable.nav_feed_normal);
			nav_group.setImageResource(R.drawable.nav_group_narmal);
			nav_me.setImageResource(R.drawable.nav_me_down);
			nav_startsport.setImageResource(R.drawable.nav_startsport_normal);

		}
		changeFragment(targetFragment, init);
	}

	/**
	 * 主框架Frage切换
	 * 
	 * @param targetFragment
	 */
	private void changeFragment(Fragment targetFragment, boolean init) {
		if (null != targetFragment) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.containerBody, targetFragment);
			if (!init)
				ft.addToBackStack(null);
			ft.commit();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 1500) {
				Toast.makeText(getApplicationContext(),
						this.getResources().getString(R.string.str_toast_exit),
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				AppManager.getInstance().AppExit(this);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case POST_VERSION_UPDATE:
				if (msg.obj != null) {
					try {
						JSONObject jsonObject = (JSONObject) msg.obj;
						if (jsonObject.getInt("status") == 200) {
							if (!jsonObject.isNull("data")) {
								String data = jsonObject.getString("data");
								jsonObject = new JSONObject(data);
								if (!jsonObject.isNull("version_name")) {
									version_name = jsonObject
											.getString("version_name");
									version_code = jsonObject
											.getString("version_code");
									description = jsonObject
											.getString("description");
								}
								url = jsonObject.getString("url");
								if (Integer.parseInt(version_code) > nativeVersionCode) {
									uploadVersionDialog();
								}
							}
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

	/**
	 * 检查更新
	 */
	private void checkUpdate() {
		nativeVersionCode = GDUtil.GetVersionCode(context);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {

			public void run() {
				final Map<String, String> params = new HashMap<String, String>();
				params.put("type", "android");
				params.put("version_code", nativeVersionCode + "");
				HttpUtil.Post(UrlConstants.POST_VERSION_UPDATE, handler,
						POST_VERSION_UPDATE, params);
			}
		});

	}

	/**
	 * 选择是否下载新版本的对话框
	 */
	public void uploadVersionDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.dialog_version_check, null);
		// 对话框
		final Dialog dialog = new AlertDialog.Builder(MenuActivity.this)
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
		file = Environment.getExternalStorageDirectory().toString()
				+ "/Oxygen.apk";
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
					private AlertDialog dialog;
					private Builder builder;
					private TextView tv;
					private ProgressBar pb;

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

						tv.setText("已下载: "
								+ Formatter.formatFileSize(context, current)
								+ "/"
								+ Formatter.formatFileSize(context, total));
						pb.setMax((int) total);
						pb.setProgress((int) current);
					}

					@Override
					public void onStart() {
						super.onStart();
						progressDialog = new ProgressDialog(context);
						builder = new Builder(MenuActivity.this);

						View view = View.inflate(MenuActivity.this,
								R.layout.dialog_progress_update, null);
						tv = (TextView) view.findViewById(R.id.tv);
						pb = (ProgressBar) view.findViewById(R.id.pb);
						builder.setView(view);
						builder.setCancelable(false);

						dialog = builder.create();
						dialog.show();

					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						// progressDialog.dismiss();
						dialog.dismiss();
						ToastUtil.show(context, "下载完成");
						GDUtil.installApk(file, MenuActivity.this);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// progressDialog.dismiss();
						dialog.dismiss();
						ToastUtil.show(context, "更新失败");
					}
				});
	}

}
