package com.oxygen.www.module.team.activity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker.OnValueChangeListener;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Group;
import com.oxygen.www.module.find.activity.BusinessesListActivity;
import com.oxygen.www.module.find.eventbus_enties.BusinessName;
import com.oxygen.www.module.team.adapter.TeamSportAdapter;
import com.oxygen.www.module.team.eventbus_enties.RefreshTeamFragment;
import com.oxygen.www.utils.AppManager;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.CircleImageView;
import com.qiniu.android.http.ResponseInfo;
import de.greenrobot.event.EventBus;

/**
 * 编辑团队资料界面
 * 
 * @author yang 2015-5-20下午5:28:39
 */
public class CompleteTeamInfoActivity extends BaseActivity implements
		OnClickListener {
	protected static final int UPDATE_GROUP = 1;
	protected static final int NET_GETTOKEN = 2;
	private final int TAKE_PICTURE = 4;
	private final int CHOOSE_PICTURE = 5;
	private final int NET_UPLOAP = 6;
	private Group group;
	private String intro;
	private String address;

	private ImageView iv_back;
	private TextView tv_save;
	private CircleImageView ibt_team_icon;
	private RelativeLayout rl_team_name;
	private EditText tv_team_name;
	private RelativeLayout rl_sport_type;
	private TextView tv_sport_name;
	private RelativeLayout rl_create_time;
	private TextView tv_time;
	private RelativeLayout rl_sport_address;
	private TextView tv_address;
	private RelativeLayout rl_team_info;
	private EditText tv_info;
	private LinearLayout ll_team_sport;
	private LinearLayout ll_team_date;
	private Button bt_team_sure;
	private NumberPicker np_team_year;
	private NumberPicker np_team_month;
	private GridView gv_teamsports;
	private String ch_name;
	private String year;
	private String month;

	private PopupWindow popupWindow;
	private Button picture;
	private Button camera;
	private Button back;
	private Bitmap photoBp;
	Calendar a = Calendar.getInstance();
	protected String picurl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_completeteaminfo);
		EventBus.getDefault().register(this);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_save = (TextView) findViewById(R.id.tv_save);

		ibt_team_icon = (CircleImageView) findViewById(R.id.ibt_team_icon);
		rl_team_name = (RelativeLayout) findViewById(R.id.rl_team_name);
		tv_team_name = (EditText) findViewById(R.id.tv_team_name);

		rl_sport_type = (RelativeLayout) findViewById(R.id.rl_sport_type);
		tv_sport_name = (TextView) findViewById(R.id.tv_sport_name);

		rl_create_time = (RelativeLayout) findViewById(R.id.rl_create_time);
		tv_time = (TextView) findViewById(R.id.tv_time);

		rl_sport_address = (RelativeLayout) findViewById(R.id.rl_sport_address);
		tv_address = (TextView) findViewById(R.id.tv_address);

		rl_team_info = (RelativeLayout) findViewById(R.id.rl_team_info);
		tv_info = (EditText) findViewById(R.id.tv_info);
		gv_teamsports = (GridView) findViewById(R.id.gv_teamsports);
		ll_team_sport = (LinearLayout) findViewById(R.id.ll_team_sport);
		// 修改成立时间
		ll_team_date = (LinearLayout) findViewById(R.id.ll_team_date);
		bt_team_sure = (Button) findViewById(R.id.bt_team_sure);
		np_team_year = (NumberPicker) findViewById(R.id.np_team_year);
		np_team_month = (NumberPicker) findViewById(R.id.np_team_month);

		TeamSportAdapter teamSportAdapter = new TeamSportAdapter(this);
		gv_teamsports.setAdapter(teamSportAdapter);
		gv_teamsports.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ll_team_sport.setVisibility(View.GONE);
				group.setSport(Constants.sports[position]);
				ch_name = GDUtil.engforchn(CompleteTeamInfoActivity.this,
						"created_type_" + Constants.sports[position]);
				tv_sport_name.setText(ch_name);
			}
		});
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_save.setOnClickListener(this);
		rl_sport_type.setOnClickListener(this);
		rl_create_time.setOnClickListener(this);
		bt_team_sure.setOnClickListener(this);
		rl_sport_address.setOnClickListener(this);
		ibt_team_icon.setOnClickListener(this);
	}

	private void initValues() {
		Bundle bundle = getIntent().getExtras();
		group = (Group) bundle.getSerializable("group");
		ImageUtil
				.showImage(group.getPic(), ibt_team_icon, R.drawable.team_icon);
		ch_name = GDUtil.engforchn(this, "created_type_" + group.getSport());
		tv_team_name.setText(group.getName());
		tv_sport_name.setText(ch_name);
		tv_info.setText(group.getIntro());
		String date = group.getStarted_at().toString();
		year = date.substring(0, 4);
		month = date.substring(5, 7);
		tv_time.setText(year + "年" + month + "月");
		if (!TextUtils.isEmpty(group.getAddress())) {
			tv_address.setText(group.getAddress());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;

		case R.id.tv_save:
			updateTeamInfo();
			break;
		// 团队运动类型
		case R.id.rl_sport_type:
			ll_team_sport.setVisibility(View.VISIBLE);
			break;
		// 团队成立时间
		case R.id.rl_create_time:
			showCreateTimePopWindow();
			break;
		// 团队成立时间确定
		case R.id.bt_team_sure:
			ll_team_date.setVisibility(View.GONE);
			if (year != null && month != null) {
				tv_time.setText(year + "年" + month + "月");
			}
			group.setStarted_at(year + "-" + month);
			break;
		// 团队主场
		case R.id.rl_sport_address:
			Intent addressIntent = new Intent(this,
					BusinessesListActivity.class);
			addressIntent.putExtra("fromTeam", new String("fromTeam"));
			startActivity(addressIntent);
			break;
		case R.id.ibt_team_icon:
			showPopWindow();
			break;
		case R.id.picture:
			dismissPopupWindow();
			openPicture();
			break;
		case R.id.camera:
			dismissPopupWindow();
			openCamera();
			break;
		case R.id.back:
			dismissPopupWindow();
			break;
		default:
			break;
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
	 * 打开popWindow(相机和相册)
	 */
	private void showPopWindow() {
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int width = manager.getDefaultDisplay().getWidth();
		if (popupWindow == null || !popupWindow.isShowing()) {
			View contentView = View.inflate(this,
					R.layout.activity_psw_popupwindow, null);
			camera = (Button) contentView.findViewById(R.id.camera);
			picture = (Button) contentView.findViewById(R.id.picture);
			back = (Button) contentView.findViewById(R.id.back);
			camera.setOnClickListener(this);
			picture.setOnClickListener(this);
			back.setOnClickListener(this);
			popupWindow = new PopupWindow(contentView);
			popupWindow.setWidth(width);
			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);// 设置弹出框大小
			popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
			popupWindow.setOutsideTouchable(false);
		}
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
			switch (requestCode) {
			case TAKE_PICTURE:
				if (data != null) {
					startPhotoZoom(data.getData());
				}
				break;
			case CHOOSE_PICTURE:
				if (data != null) {
					try {
						photoBp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
						Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), photoBp, null,null));
						startPhotoZoom(uri);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection",true);
		startActivityForResult(intent, 3);
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
			if(photoBp != null){
				postHeadImage();
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

	/**
	 * 修改团队成立时间
	 */
	private void showCreateTimePopWindow() {
		ll_team_date.setVisibility(View.VISIBLE);
		np_team_year.setMaxValue(2020);
		np_team_year.setMinValue(1900);
		np_team_month.setMaxValue(12);
		np_team_month.setMinValue(1);
		np_team_year.setValue(Integer.parseInt(year));
		np_team_month.setValue(Integer.parseInt(month));

		np_team_year.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				year = newVal + "";
			}
		});

		np_team_month.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				month = newVal + "";
			}
		});
	}

	/**
	 * 修改(完善)团队信息
	 */
	private void updateTeamInfo() {
		intro = tv_info.getText().toString().trim();
		final Map<String, String> params = new HashMap<String,

		String>();
		params.put("pic", group.getPic());
		params.put("name", tv_team_name.getText().toString());
		params.put("sport", group.getSport());
		params.put("started_at", group.getStarted_at());
		if (!TextUtils.isEmpty(intro)) {
			params.put("intro", intro);
		}
		if (!TextUtils.isEmpty(address)) {
			params.put("address", address);
		}
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.UPDATE_GROUP + group.getId()
						+ ".json", handler, UPDATE_GROUP, params);
			}
		});
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE_GROUP:
				try {
					JSONObject jsonObject = (JSONObject) msg.obj;
					if (jsonObject.getInt("status") == 200) {
						EventBus.getDefault().post(
								new Group(group.getSport(), intro, tv_team_name
										.getText().toString(), group
										.getStarted_at(), address,picurl));
						CompleteTeamInfoActivity.this.finish();
						EventBus.getDefault().post(new RefreshTeamFragment());
					} else {
						ToastUtil.show(CompleteTeamInfoActivity.this,
								"完善团队信息失败");
						return;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch
					e.printStackTrace();
				}
				break;
				
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
							HttpUtil.UploadPhotoForQiuniu(token, GDUtil.Bitmap2Bytes(photoBp),
									key, handler, NET_UPLOAP);
							picurl = "http://" + domain + "/" + key;
							
						} else {
							ToastUtil.show(CompleteTeamInfoActivity.this,
									jsonenlist.getString("msg"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case NET_UPLOAP:
				ResponseInfo responseInfo = (ResponseInfo) msg.obj;
				if (responseInfo.statusCode == 200) {
					// 上传七牛成功 把图片在七牛的url地址上传到服务器
					group.setPic(picurl);
					ibt_team_icon.setImageBitmap(photoBp);
				} else {
					ToastUtil.show(CompleteTeamInfoActivity.this, "上传失败");
				}
				break;
			}
		}
		

	};

	private void onEventMainThread(BusinessName msg) {
		address = msg.getName();
		tv_address.setText(msg.getName());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppManager.getInstance().finishActivity(this);
		EventBus.getDefault().unregister(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (null != popupWindow && popupWindow.isShowing()) {
				popupWindow.dismiss();
				return true;
			}
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
