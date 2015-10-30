package com.oxygen.www.module.team.activity;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Group;
import com.oxygen.www.module.sport.activity.ChooseSportActivity;
import com.oxygen.www.module.sport.writemoment.util.FileUtils;
import com.oxygen.www.module.team.construt.GroupsConstruct;
import com.oxygen.www.module.team.eventbus_enties.RefreshTeamFragment;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.CircleImageView;
import com.qiniu.android.http.ResponseInfo;

import de.greenrobot.event.EventBus;

/**
 * 创建活动界面（选择图标和名字）
 * 
 * @author yang 2015-5-19下午2:33:48
 */
public class CreateTeamActivity extends BaseActivity implements OnClickListener {

	private final int CHOOSE_PICTURE = 1;
	private final int TAKE_PICTURE = 2;
	private final int NET_GETTOKEN = 3;
	private final int NET_UPLOAP_TEAM_ICON = 4;
	private final int CREATE_GROUP = 5;
	private final int REQUEST_COD = 6;
	private Calendar a = Calendar.getInstance();
	private Map<String, String> params;
	private ImageView iv_back;
	private TextView tv_save;
	private TextView tv_sport_type;
	private EditText et_team_name;
	private EditText tv_team_info;
	private RelativeLayout rl_sport_type;
	private CheckBox cb_suggest;
	private ProgressBar progressBar;
	private CircleImageView team_icon;
	private Button camera;
	private Button picture;
	private Button back;
	private PopupWindow popupWindow;
	private String picurl = null;
	private String event_id;
	private String sport;
	private String name;
	private String fromTeamIndex;
	private Bitmap photoBp;
	private String sport_cn;
	private String sport_eng;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createteam);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_save = (TextView) findViewById(R.id.tv_save);
		team_icon = (CircleImageView) findViewById(R.id.team_icon);
		et_team_name = (EditText) findViewById(R.id.et_team_name);
		tv_team_info = (EditText) findViewById(R.id.tv_team_info);
		rl_sport_type = (RelativeLayout) findViewById(R.id.rl_sport_type);
		tv_sport_type = (TextView) findViewById(R.id.tv_sport_type);
		cb_suggest = (CheckBox) findViewById(R.id.cb_suggest);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		event_id = getIntent().getExtras().getInt("event_id") + "";
		params = new HashMap<String, String>();
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_save.setOnClickListener(this);
		team_icon.setOnClickListener(this);
		rl_sport_type.setOnClickListener(this);
	}

	private void initValues() {
		cb_suggest.setChecked(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.rl_sport_type:
			Intent intent_choose = new Intent(CreateTeamActivity.this,
					ChooseSportActivity.class);
			intent_choose.putExtra("fromTeamIndex", "fromTeamIndex");
			startActivityForResult(intent_choose, REQUEST_COD);
			break;
		case R.id.tv_save:
			if (photoBp == null) {
				ToastUtil.show(CreateTeamActivity.this, "未选择团队标识");
			} else if (TextUtils.isEmpty(et_team_name.getText())) {
				ToastUtil.show(CreateTeamActivity.this, "请填写团队名称");
			} else if(TextUtils.isEmpty(sport_cn)){
				ToastUtil.show(CreateTeamActivity.this, "请选择运动标签");
			}else{
				progressBar.setVisibility(View.VISIBLE);
				tv_save.setEnabled(false);
				// 上传头像到七牛
				postHeadImage();
			}
			break;
		// 打开图库或者相机选择图片作为团队图标
		case R.id.team_icon:
			showPopWindow();
			break;
		case R.id.camera:
			dismissPopupWindow();
			openCamera();
			break;
		case R.id.picture:
			dismissPopupWindow();
			openPicture();
			break;
		case R.id.back:
			dismissPopupWindow();
			break;
		default:
			break;
		}
	}

	/**
	 * 打开popWindow(相机和相册)
	 */
	private void showPopWindow() {
		if (popupWindow == null || !popupWindow.isShowing()) {
			View contentView = View.inflate(this,
					R.layout.activity_psw_popupwindow, null);
			camera = (Button) contentView.findViewById(R.id.camera);
			picture = (Button) contentView.findViewById(R.id.picture);
			back = (Button) contentView.findViewById(R.id.back);
			camera.setOnClickListener(this);
			picture.setOnClickListener(this);
			back.setOnClickListener(this);
			popupWindow = new PopupWindow(contentView,LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
			popupWindow.setOutsideTouchable(false);
			popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
		}
	}

	/**
	 * 打开相机的图库
	 */
	private void openPicture() {
		dismissPopupWindow();
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, CHOOSE_PICTURE);
	}

	/**
	 * 打开相机
	 */
	private void openCamera() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, 3);
	}

	/**
	 * 关闭当前界面的弹出窗体
	 */
	private void dismissPopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (data != null) {
				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);
				File temp = new File(FileUtils.SDPATH + fileName + ".JPEG");
				startPhotoZoom(Uri.fromFile(temp));
			}
			break;
		case CHOOSE_PICTURE:
			if (data != null) {
				try {
					Uri uri = Uri.parse(
							MediaStore.Images.Media.insertImage(getContentResolver(),
							MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData()),
							null, 
							null));
					startPhotoZoom(uri);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case REQUEST_COD:
			if(data != null){
				sport_eng = data.getStringExtra("sport");
				sport_cn = GDUtil.engforchn(getApplicationContext(), "created_type_"+sport_eng);
				tv_sport_type.setText(sport_cn);
			}
			break;
		case 3:
			if (data != null) {
				setPicToView(data);
			}
			break;
		}
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			photoBp = extras.getParcelable("data");
			if (photoBp != null) {
				team_icon.setImageBitmap(photoBp);
			}
		}
	}

	private void postHeadImage() {
		getQiuniuToken();
	}

	/**
	 * 获取服务器events邀请信息列表
	 */
	private void getQiuniuToken() {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.GET_QINIU_TOKEN, handler,
						NET_GETTOKEN);
			}
		});

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_GETTOKEN:
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsonenlist = new JSONObject(strObject);
						if (jsonenlist.getInt("status") == 200) {
							String token = jsonenlist.getJSONObject("data")
									.getString("token");
							String domain = jsonenlist.getJSONObject("data")
									.getString("domain");
							String key = "users/" + a.get(Calendar.YEAR) + ""
									+ a.get(Calendar.MONTH) + 1 + "/"
									+ System.currentTimeMillis() + "_"
									+ (int) (Math.random() * 900) + 100
									+ ".jpg";
							picurl = "http://" + domain + "/" + key;
							HttpUtil.UploadPhotoForQiuniu(token,
									GDUtil.Bitmap2Bytes(photoBp), key, handler,
									NET_UPLOAP_TEAM_ICON);
						} else {
							progressBar.setVisibility(View.GONE);
							ToastUtil.show(CreateTeamActivity.this,
									jsonenlist.getString("msg"));
						}
					} catch (JSONException e) {
						progressBar.setVisibility(View.GONE);
						e.printStackTrace();
					}
				} else {
					tv_save.setEnabled(true);
					progressBar.setVisibility(View.GONE);
				}
				break;
			case NET_UPLOAP_TEAM_ICON:
				ResponseInfo responseInfo = (ResponseInfo) msg.obj;
				if (responseInfo.statusCode == 200) {
					// 上传七牛成功 把图片在七牛的url地址上传到服务器
					name = et_team_name.getText().toString().trim();
					params.put("name", name);
					params.put("pic", picurl);
					params.put("intro", tv_team_info.getText().toString());
					params.put("sport", sport_eng);
					if(cb_suggest.isChecked()){
						params.put("privacy", "0");
					}else{
						params.put("privacy", "1");
					}
					OxygenApplication.cachedThreadPool.execute(new Runnable() {
						public void run() {
							HttpUtil.Post(UrlConstants.CREATE_GROUP, handler,
									CREATE_GROUP, params);
						}
					});
					photoBp.recycle();
				} else {
					tv_save.setEnabled(true);
					progressBar.setVisibility(View.GONE);
					ToastUtil.show(CreateTeamActivity.this, "上传团队头像失败");
					photoBp.recycle();
					return;
				}
				break;
			case CREATE_GROUP:
				tv_save.setEnabled(true);
				if (msg.obj == null) {
					progressBar.setVisibility(View.GONE);
					ToastUtil.show(CreateTeamActivity.this, "网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonObject = (JSONObject) msg.obj;
						if (jsonObject.getInt("status") == 200) {
							progressBar.setVisibility(View.GONE);
							Group group = GroupsConstruct.ToGroup(jsonObject.getJSONObject("data"));
							Intent teamInfoIntent = new Intent(
									CreateTeamActivity.this,GroupDetailActivity.class);
							teamInfoIntent.putExtra("groupid",group.getId());
							startActivity(teamInfoIntent);
							EventBus.getDefault().post(new RefreshTeamFragment());
							CreateTeamActivity.this.finish();
						} else {
							progressBar.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						ToastUtil.show(CreateTeamActivity.this, getResources()
								.getString(R.string.create_team_success));
						progressBar.setVisibility(View.GONE);
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
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 3);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 1200);
		intent.putExtra("outputY", 900);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
}
