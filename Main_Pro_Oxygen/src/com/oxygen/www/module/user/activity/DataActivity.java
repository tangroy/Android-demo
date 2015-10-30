package com.oxygen.www.module.user.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.R.color;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.module.sport.writemoment.util.Res;
import com.oxygen.www.module.user.adapter.MyGridViewAdapter;
import com.oxygen.www.module.user.eventbus_entities.ModifyuserInfo;
import com.oxygen.www.utils.AppManager;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.widget.CircleImageView;
import com.oxygen.www.widget.MyNumberPicker;
import com.qiniu.android.http.ResponseInfo;

import de.greenrobot.event.EventBus;

/**
 * 个人信息界面
 * 
 * @author 杨庆雷 创建时间：2015-4-15 上午10:27:36
 */
public class DataActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_back;
	private CircleImageView iv_head;
	private TextView tv_nickname, tv_sex, tv_age, tv_height, tv_weight,
			tv_tags, tv_intro, tv_poptitle;
	private PopupWindow popupWindow;
	private Button picture;
	private Button camera;
	private Button back;
	private Button sex_man;
	private Button sex_woman;
	private RelativeLayout rl_head;
	private RelativeLayout rl_nickname;
	private RelativeLayout rl_sex;
	private RelativeLayout rl_age;
	private RelativeLayout rl_height;
	private RelativeLayout rl_weight;
	private RelativeLayout rl_tags;
	private RelativeLayout rl_signature;
	private LinearLayout weight_height;
	private final int AGE_TAG = 1;
	private final int HEIGHT_TAG = 2;
	private final int WEIGHT_TAG = 30;
	private final int POST_USER_UPDATE = 4;
	private final int UPDATE_NICKNAME = 5;
	private final int UPDATE_INTRO = 6;
	private final int TAKE_PICTURE = 7;
	private final int CHOOSE_PICTURE = 8;
	private final int NET_GETTOKEN = 10;
	private final int NET_UPLOAP = 11;
	/**
	 * 记录选择年龄 体重 身高等
	 */
	private int mPosition;
	/**
	 * 标记当前需要修改的是什么(身高,体重,年龄)
	 */
	private int numberTag;
	/**
	 * 选择运动的集合
	 */
	private List<String> sportSelectedNames;
	/**
	 * 用户的性别
	 */
	private String sex;
	/**
	 * 修改用户信息提交的参数
	 */
	private Map<String, String> params;
	private String age;
	private String userHeight;
	private String weight;
	private String headImgUrl;
	private String nickname;
	private String intro;
	Calendar a = Calendar.getInstance();
	private String picurl = null;
	private Bitmap photoBp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data);
		Res.init(this);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_head = (CircleImageView) findViewById(R.id.iv_head);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		tv_age = (TextView) findViewById(R.id.tv_age);
		tv_height = (TextView) findViewById(R.id.tv_height);
		tv_weight = (TextView) findViewById(R.id.tv_weight);
		tv_tags = (TextView) findViewById(R.id.tv_tags);
		tv_intro = (TextView) findViewById(R.id.tv_intro);
		rl_head = (RelativeLayout) findViewById(R.id.rl_head);
		rl_nickname = (RelativeLayout) findViewById(R.id.rl_nickname);
		rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
		rl_age = (RelativeLayout) findViewById(R.id.rl_age);
		rl_height = (RelativeLayout) findViewById(R.id.rl_height);
		rl_weight = (RelativeLayout) findViewById(R.id.rl_weight);
		rl_tags = (RelativeLayout) findViewById(R.id.rl_tags);
		rl_signature = (RelativeLayout) findViewById(R.id.rl_signature);
		weight_height = (LinearLayout) findViewById(R.id.weight_height);
		tv_poptitle = (TextView) findViewById(R.id.tv_poptitle);
	}

	private void initViewsEvent() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(this);

		headImgUrl = (String) UserInfoUtils.getUserInfo(
				getApplicationContext(), Constants.HEADIMG_URL, "");
		nickname = (String) UserInfoUtils.getUserInfo(getApplicationContext(),
				Constants.NICKNAME, getResources().getString(R.string.unknown));
		try {
			sex = (Integer) UserInfoUtils.getUserInfo(getApplicationContext(),
					Constants.SEX, 0) + "";
		} catch (Exception e) {
			sex = (String) UserInfoUtils.getUserInfo(getApplicationContext(),
					Constants.SEX, "0");
		}
		age = (String) UserInfoUtils.getUserInfo(getApplicationContext(),
				Constants.AGE, "0");
		userHeight = (String) UserInfoUtils.getUserInfo(
				getApplicationContext(), Constants.HEIGHT, "0");
		weight = (String) UserInfoUtils.getUserInfo(getApplicationContext(),
				Constants.WEIGHT, "0");
		intro = (String) UserInfoUtils.getUserInfo(getApplicationContext(),
				Constants.SIGN, "一起运动更快乐！");

		if (!TextUtils.isEmpty(headImgUrl)) {
			ImageUtil.showImage(headImgUrl, iv_head, R.drawable.icon_def);
		}
		tv_nickname.setText(nickname);
		if ("1".equals(sex) || "男".equals(sex)) {
			tv_sex.setText("男");
		} else if ("2".equals(sex) || "女".equals(sex)) {
			tv_sex.setText("女");
		} else {
			tv_sex.setText("位置");
		}
		tv_age.setText(age);
		tv_height.setText(userHeight + "cm");
		tv_weight.setText(weight + "kg");
		if ("1".equals(intro)) {
			tv_intro.setText("一起运动更快乐!");
		} else {
			tv_intro.setText(intro);
		}
		rl_head.setOnClickListener(this);
		rl_nickname.setOnClickListener(this);
		rl_sex.setOnClickListener(this);
		rl_age.setOnClickListener(this);
		rl_height.setOnClickListener(this);
		rl_weight.setOnClickListener(this);
		rl_tags.setOnClickListener(this);
		rl_signature.setOnClickListener(this);
	}

	private void initValues() {

		tv_nickname.setText(nickname);
		getsportsNames();
	}

	/**
	 * 获取并设置用户感兴趣的运动
	 */
	private void getsportsNames() {
		sportSelectedNames = new ArrayList<String>();
		String[] names = ((String) UserInfoUtils.getUserInfo(
				getApplicationContext(), Constants.SPORT_SELECTED, ""))
				.replace("[", "").replace("]", "").replace(" ", "").split(",");
		if (names[0].equals("")) {
			names = null;
		}
		if (names != null && names.length > 0) {
			for (int i = 0; i < names.length; i++) {
				sportSelectedNames.add(names[i]);
			}
		}

		String engName;
		String chnName;
		StringBuilder showNames = new StringBuilder();
		if (names != null && names.length > 0) {
			for (int i = 0; i < names.length; i++) {
				engName = names[i];
				chnName = GDUtil.engforchn(DataActivity.this, "created_type_"
						+ engName);
				showNames.append(" " + chnName);
			}
		}
		tv_tags.setText(showNames.toString());
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		params = new HashMap<String, String>();
		switch (v.getId()) {
		case R.id.iv_back:
			weight_height.setVisibility(View.GONE);
			if ("".equals((String) UserInfoUtils.getUserInfo(
					getApplicationContext(), Constants.NICKNAME, ""))) {
				ToastUtil.show(this, "昵称不能为空哦~");
				return;
			}
			if ("".equals((String) UserInfoUtils.getUserInfo(
					getApplicationContext(), Constants.AGE, ""))) {
				ToastUtil.show(this, "别忘了填写年龄哦~");
				return;
			}
			if ("".equals((String) UserInfoUtils.getUserInfo(
					getApplicationContext(), Constants.SPORT_SELECTED, ""))) {
				ToastUtil.show(this, "别忘了填写感兴趣的运动哦~");
				return;
			}
			finish();
			break;
		case R.id.rl_head:
			weight_height.setVisibility(View.GONE);
			showPopWindow();
			break;
		case R.id.rl_nickname:
			weight_height.setVisibility(View.GONE);
			intent = new Intent(this, EditDataActivity.class);
			intent.putExtra("title", getResources()
					.getString(R.string.nickname));
			startActivityForResult(intent, UPDATE_NICKNAME);
			break;
		case R.id.rl_sex:
			weight_height.setVisibility(View.GONE);
			showSexPopWindow();
			break;
		case R.id.rl_age:
			dismissPopupWindow();
			numberTag = AGE_TAG;
			tv_poptitle.setText("年龄");
			showNumberPopWindow();
			break;
		case R.id.rl_height:
			dismissPopupWindow();
			numberTag = HEIGHT_TAG;
			tv_poptitle.setText("身高");
			showNumberPopWindow();
			break;
		case R.id.rl_weight:
			dismissPopupWindow();
			numberTag = WEIGHT_TAG;
			tv_poptitle.setText("体重");
			showNumberPopWindow();
			break;
		case R.id.rl_tags:
			showSportPopWindow();
			break;
		case R.id.rl_signature:
			intent = new Intent(this, EditDataActivity.class);
			intent.putExtra("title",
					getResources().getString(R.string.signature));
			startActivityForResult(intent, UPDATE_INTRO);
			break;
		case R.id.picture:
			dismissPopupWindow();
			openPicture();
			break;
		case R.id.camera:
			dismissPopupWindow();
			openCamera();
			break;
		case R.id.sex_man:
			sex = getResources().getString(R.string.man);
			sex_man.setTextColor(getResources().getColor(color.black));
			sex_woman.setTextColor(getResources().getColor(color.lightgray));
			break;
		case R.id.sex_woman:
			sex = getResources().getString(R.string.woman);
			sex_man.setTextColor(getResources().getColor(color.lightgray));
			sex_woman.setTextColor(getResources().getColor(color.black));
			break;
		case R.id.back:
			dismissPopupWindow();
			break;
		case R.id.bt_sure:
			weight_height.setVisibility(View.GONE);
			saveUserInfo();
			break;
		case R.id.sportselect_sure:
			dismissPopupWindow();
			// 保存用户选择的英文名字
			// (首先对运动排序)
			for (int i = 0; i < Constants.sports.length; i++) {
				if (sportSelectedNames.contains(Constants.sports[i])) {
					sportSelectedNames.remove(Constants.sports[i]);
					sportSelectedNames.add(Constants.sports[i]);
				}
			}
			UserInfoUtils
					.setUserInfo(getApplicationContext(),
							Constants.SPORT_SELECTED,
							sportSelectedNames.toString().replace(" ", "")
									.replace("[", "").replace("]", ""));
			// UserInfoUtils.setSportsSelectedName(this,
			// sportSelectedName.toString());
			params.put("sports", sportSelectedNames.toString().replace(" ", "")
					.replace("[", "").replace("]", ""));
			submitUserInfoToNet(params);
			getsportsNames();
			break;
		case R.id.sexselect_sure:
			dismissPopupWindow();
			tv_sex.setText(sex);
			if (getResources().getString(R.string.man).equals(sex)) {
				params.put("sex", 1 + "");
				UserInfoUtils.setUserInfo(this, Constants.SEX, 1 + "");
			} else {
				params.put("sex", 2 + "");
				UserInfoUtils.setUserInfo(this, Constants.SEX, 2 + "");
			}
			submitUserInfoToNet(params);
			break;
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

	/**
	 * 打开popWindow(相机和相册)
	 */
	private void showPopWindow() {
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
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
			// 在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
			popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			popupWindow
					.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
			popupWindow.setOutsideTouchable(false);
		}
	}

	/**
	 * 显示选择性别的popWindow
	 */
	private void showSexPopWindow() {
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int height = manager.getDefaultDisplay().getHeight();
		@SuppressWarnings("deprecation")
		int width = manager.getDefaultDisplay().getWidth();
		if (popupWindow == null || !popupWindow.isShowing()) {
			View contentView = View.inflate(this,
					R.layout.activity_sex_popupwindow, null);
			sex_man = (Button) contentView.findViewById(R.id.sex_man);
			sex_woman = (Button) contentView.findViewById(R.id.sex_woman);
			Button sexselect_sure = (Button) contentView
					.findViewById(R.id.sexselect_sure);
			sex_man.setOnClickListener(this);
			sex_woman.setOnClickListener(this);
			sexselect_sure.setOnClickListener(this);

			popupWindow = new PopupWindow(contentView);
			popupWindow.setWidth(width);
			popupWindow.setHeight(height / 3);// 设置弹出框大小
			popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
			popupWindow.setOutsideTouchable(false);

			if ("男".equals(sex)) {
				tv_sex.setText(getResources().getString(R.string.man));
				sex_man.setTextColor(getResources().getColor(color.black));
			} else if ("女".equals(sex)) {
				tv_sex.setText(getResources().getString(R.string.woman));
				sex_woman.setTextColor(getResources().getColor(color.black));
			}
		}
	}

	/**
	 * 显示选择兴趣(体育运动类型)的popWindow
	 */
	private void showSportPopWindow() {
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int width = manager.getDefaultDisplay().getWidth();
		if (popupWindow == null || !popupWindow.isShowing()) {
			View contentView = View.inflate(this,
					R.layout.activity_sport_popupwindow, null);
			GridView gv_sports = (GridView) contentView
					.findViewById(R.id.gv_sports);
			Button sportselect_sure = (Button) contentView
					.findViewById(R.id.sportselect_sure);
			sportselect_sure.setOnClickListener(this);
			MyGridViewAdapter gridViewAdapter = new MyGridViewAdapter(this,
					sportSelectedNames);
			gv_sports.setAdapter(gridViewAdapter);
			popupWindow = new PopupWindow(contentView);
			popupWindow.setWidth(width);
			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);// 设置弹出框大小
			popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
			popupWindow.setOutsideTouchable(false);
		}

	}

	/**
	 * 显示选择年龄身高体重的popWindow
	 */
	private void showNumberPopWindow() {
		weight_height.setVisibility(View.VISIBLE);
		NumberPicker lv_number = (MyNumberPicker) findViewById(R.id.np_number);
		Button bt_sure = (Button) findViewById(R.id.bt_sure);
		bt_sure.setOnClickListener(DataActivity.this);
		switch (numberTag) {
		case AGE_TAG:
			lv_number.setMaxValue(99);
			lv_number.setMinValue(0);
			if (TextUtils.isEmpty((String) UserInfoUtils.getUserInfo(this,
					Constants.AGE, ""))) {
				mPosition = 25;
			} else {
				mPosition = Integer.parseInt((String) UserInfoUtils
						.getUserInfo(this, Constants.AGE, "0"));
				if (mPosition == 0) {
					mPosition = 25;
				}
			}
			lv_number.setValue(mPosition);
			break;

		case HEIGHT_TAG:
			lv_number.setMaxValue(219);
			lv_number.setMinValue(50);
			if (TextUtils.isEmpty((String) UserInfoUtils.getUserInfo(this,
					Constants.HEIGHT, ""))) {
				mPosition = 170;
			} else {
				mPosition = Integer.parseInt((String) UserInfoUtils
						.getUserInfo(this, Constants.HEIGHT, "0"));
				if (mPosition == 0) {
					mPosition = 170;
				}
			}
			lv_number.setValue(mPosition);
			break;

		case WEIGHT_TAG:
			lv_number.setMaxValue(199);
			lv_number.setMinValue(20);
			if (TextUtils.isEmpty((String) UserInfoUtils.getUserInfo(this,
					Constants.WEIGHT, ""))) {
				lv_number.setValue(40);
				mPosition = 40;
			} else {
				mPosition = Integer.parseInt((String) UserInfoUtils
						.getUserInfo(this, Constants.WEIGHT, "0"));
				if (mPosition == 0) {
					mPosition = 40;
				}
				lv_number.setValue(mPosition);
			}
			break;
		default:
			break;
		}
		lv_number.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				mPosition = newVal;
			}
		});
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
					photoBp = MediaStore.Images.Media.getBitmap(
							this.getContentResolver(), data.getData());
					Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
							getContentResolver(), photoBp, null, null));
					startPhotoZoom(uri);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case UPDATE_INTRO:
			if (data != null) {
				String introaBack = data.getExtras().getString("userinfo");
				tv_intro.setText(introaBack);
				UserInfoUtils.setUserInfo(getApplicationContext(),
						Constants.SIGN, introaBack);
			}
			break;
		case UPDATE_NICKNAME:
			if (data != null) {
				String nickname = data.getExtras().getString("userinfo");
				tv_nickname.setText(nickname);
				UserInfoUtils.setUserInfo(getApplicationContext(),
						Constants.NICKNAME, nickname);
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
		intent.putExtra("noFaceDetection", true);
		// photopsth = Environment.getExternalStorageDirectory() + "/"
		// + System.currentTimeMillis() + "_"
		// + (int) (Math.random() * 900) + ".jpg";
		// intent.putExtra("output", Uri.fromFile(new File(photopsth)));
		// intent.putExtra("return-data", false);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
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
			if (photoBp != null) {
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
	 * 保存用户的年龄，身高，体重信息
	 */
	public void saveUserInfo() {
		params = new HashMap<String, String>();
		switch (numberTag) {
		case AGE_TAG:
			UserInfoUtils.setUserInfo(getApplicationContext(), Constants.AGE,
					mPosition + "");
			params.put("age", mPosition + "");
			age = (String) UserInfoUtils.getUserInfo(getApplicationContext(),
					Constants.AGE, "0");
			tv_age.setText(age);
			break;
		case HEIGHT_TAG:
			UserInfoUtils.setUserInfo(this, Constants.HEIGHT, mPosition + "");
			params.put("height", mPosition + "");
			userHeight = (String) UserInfoUtils.getUserInfo(this,
					Constants.HEIGHT, "0");
			tv_height.setText(userHeight + "cm");
			break;
		case WEIGHT_TAG:
			UserInfoUtils.setUserInfo(this, Constants.WEIGHT, mPosition + "");
			params.put("weight", mPosition + "");
			weight = (String) UserInfoUtils.getUserInfo(this, Constants.WEIGHT,
					"0");
			tv_weight.setText(weight + "kg");
			break;
		}

		submitUserInfoToNet(params);
	}

	/**
	 * 提交用户信息到服务器
	 */
	private void submitUserInfoToNet(final Map<String, String> params) {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				String URL = UrlConstants.POST_USER_UPDATE
						+ (String) UserInfoUtils.getUserInfo(
								DataActivity.this.getApplicationContext(),
								Constants.USERID, "") + ".json";
				HttpUtil.Post(URL, handler, POST_USER_UPDATE, params);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case POST_USER_UPDATE:
				if (msg.obj == null) {
					ToastUtil.show(DataActivity.this, "网络连接不可用，请稍后重试");
				} else {
					try {
						JSONObject jsonObject = (JSONObject) msg.obj;
						if (jsonObject.getInt("status") == 200) {
							ToastUtil.show(
									DataActivity.this,
									getResources().getString(
											R.string.update_userinfo_success));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
							HttpUtil.UploadPhotoForQiuniu(token,
									GDUtil.Bitmap2Bytes(photoBp), key, handler,
									NET_UPLOAP);
							picurl = "http://" + domain + "/" + key;

						} else {
							ToastUtil.show(DataActivity.this,
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
					params.clear();
					params.put("headimgurl", picurl);
					submitUserInfoToNet(params);
					ToastUtil.show(DataActivity.this, "修改头像成功");
					iv_head.setImageBitmap(photoBp);
					UserInfoUtils.setUserInfo(
							DataActivity.this.getApplicationContext(),
							Constants.HEADIMG_URL, picurl);
				} else {
					ToastUtil.show(DataActivity.this, "上传失败");
				}
				break;
			default:
				break;
			}
		}
	};
	private Cursor cursor;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppManager.getInstance().finishActivity(this);
		if (null != cursor) {
			cursor.close();
		}
		if (photoBp != null) {
			photoBp.recycle();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (weight_height.getVisibility() == View.VISIBLE) {
				weight_height.setVisibility(View.GONE);
				return true;
			}
			if (null != popupWindow && popupWindow.isShowing()) {
				popupWindow.dismiss();
				return true;
			}
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
