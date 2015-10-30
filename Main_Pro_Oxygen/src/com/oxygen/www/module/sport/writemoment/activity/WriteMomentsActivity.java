package com.oxygen.www.module.sport.writemoment.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.module.sport.activity.EventsResultActivity;
import com.oxygen.www.module.sport.writemoment.util.Bimp;
import com.oxygen.www.module.sport.writemoment.util.FileUtils;
import com.oxygen.www.module.sport.writemoment.util.ImageItem;
import com.oxygen.www.module.sport.writemoment.util.PublicWay;
import com.oxygen.www.module.sport.writemoment.util.Res;
import com.oxygen.www.utils.AppManager;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.qiniu.android.http.ResponseInfo;

/**
 * 活动详情页-发表动态 页面
 * 
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日 下午11:48:34
 */
public class WriteMomentsActivity extends Activity {
	private static final int NET_GETTOKEN = 1;
	private static final int NET_UPLOAP = 2;
	private final int NET_POSTMOOD = 3;
	/**
	 * 获取弹出金币个数
	 */
	private static final int NET_COIN_CNT = 4;
	private ImageView iv_back;
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private TextView activity_selectimg_send;
	private EditText et_words;
	private ProgressBar pb_wait;
	public static Bitmap bimap;
	private String[] photosurl;
	private int id;
	private String type;
	// 当前选择的图片数
	int number = 0;
	// 当前上传的图片数
	int uploap = 0;

	Calendar a = Calendar.getInstance();
	// 发表的内容
	private String words;
	private static final int OPenAlbmRequestCode = 10;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Res.init(this);
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_addpic_unfocused);
		
//		PublicWay.activityList.add(this);
		
		parentView = getLayoutInflater().inflate(R.layout.activity_selectimg,
				null);
		setContentView(parentView);
		Init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null != et_words) {
			et_words.setText(getSharedPreferences("WriteMoments",
					Context.MODE_MULTI_PROCESS).getString("words", ""));
		}
	}

	public void Init() {
		type = getIntent().getStringExtra("type");
		id = OxygenApplication.Eventid;
		iv_back = (ImageView) parentView.findViewById(R.id.iv_back);
		activity_selectimg_send = (TextView) parentView
				.findViewById(R.id.activity_selectimg_send);
		et_words = (EditText) parentView.findViewById(R.id.et_words);
		pb_wait = (ProgressBar) parentView.findViewById(R.id.pb_wait);
		pop = new PopupWindow(WriteMomentsActivity.this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);

		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(WriteMomentsActivity.this,
						AlbumActivity.class);
				startActivityForResult(intent, OPenAlbmRequestCode);
				overridePendingTransition(R.anim.default_anim_in,
						R.anim.default_anim_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				for (int i = 0; i < PublicWay.activityList.size(); i++) {
					if (null != PublicWay.activityList.get(i)) {
						PublicWay.activityList.get(i).finish();
						PublicWay.activityList.remove(i);
					}
				}
				
				// 收起软键盘
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et_words.getWindowToken(),
						InputMethodManager.HIDE_IMPLICIT_ONLY);
				finish();
			}
		});
		
		activity_selectimg_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (Bimp.tempSelectBitmap != null
						&& Bimp.tempSelectBitmap.size() > 0) {
					number = Bimp.tempSelectBitmap.size();
					photosurl = new String[number];
					pb_wait.setVisibility(View.VISIBLE);
					activity_selectimg_send.setEnabled(false);
					getQiuniuToken();
				} else if (!TextUtils.isEmpty(et_words.getText())) {
					// 收起软键盘
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et_words.getWindowToken(), 0);
					
					// 纯文字
					pb_wait.setVisibility(View.VISIBLE);
					activity_selectimg_send.setEnabled(false);
					PostMood(null);
				} else {
					ToastUtil.show(WriteMomentsActivity.this, getResources()
							.getString(R.string.toast_login_mood));
				}
			}
		});

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		
//		adapter.update();// 
		
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (arg2 == Bimp.tempSelectBitmap.size()) {
					ll_popup.startAnimation(AnimationUtils.loadAnimation(
							WriteMomentsActivity.this, R.anim.default_anim_in));
					// /在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
					pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
					pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
					// 保存editText中输入的内容
					SharedPreferences sp = getSharedPreferences("WriteMoments",
							Context.MODE_MULTI_PROCESS);
					sp.edit()
							.putString("words",
									et_words.getText().toString().trim())
							.commit();
					// 收起软键盘
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et_words.getWindowToken(), 0);
				} else {
					Intent intent = new Intent(WriteMomentsActivity.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap.size() == 9) {
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return Bimp.tempSelectBitmap.get(arg0);
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
						.getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = Message.obtain();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = Message.obtain();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			});
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			switch (requestCode) {
			case TAKE_PICTURE:
				if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
					String fileName = String
							.valueOf(System.currentTimeMillis());
					Bitmap bm = (Bitmap) data.getExtras().get("data");
					FileUtils.saveBitmap(bm, fileName);
					ImageItem takePhoto = new ImageItem();
					takePhoto.setBitmap(bm);
					takePhoto.setImagePath(FileUtils.SDPATH + fileName
							+ ".JPEG");
					Bimp.tempSelectBitmap.add(takePhoto);
				}
				
			case OPenAlbmRequestCode:
				if (resultCode == RESULT_OK) {
					
					adapter.notifyDataSetChanged();
				}
				
				break;
			}
		}
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
	 * 发送moments
	 */
	private void PostMood(String[] urls) {

		final Map<String, String> params = new HashMap<String, String>();
		if (Constants.SPORTTYPE_ACTIVITY.equals(type)) {
			params.put("activity_id", id + "");
		} else {
			params.put("event_id", id + "");
		}
		params.put("words", et_words.getText().toString());
		String str_url = null;
		if (urls != null) {
			for (int i = 0; i < urls.length; i++) {
				str_url = (str_url == null ? "" : str_url) + urls[i] + ",";
			}
			str_url = str_url.substring(0, str_url.length() - 1);
			params.put("photos", str_url);
		}
		SharedPreferences sp = getSharedPreferences("WriteMoments",
				Context.MODE_MULTI_PROCESS);
		sp.edit().putString("words", "").commit();
		PostMood_net(et_words.getText().toString(), params);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case NET_GETTOKEN:
				String strObject = (String) msg.obj;
				
//				Log.i("NET_GETTOKEN------>", msg.obj.toString());
				
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsonenlist = new JSONObject(strObject);
						if (jsonenlist.getInt("status") == 200) {
							String token = jsonenlist.getJSONObject("data")
									.getString("token");
							String domain = jsonenlist.getJSONObject("data")
									.getString("domain");
							for (int i = 0; i < number; i++) {

								String key = "moments/" + a.get(Calendar.YEAR)
										+ "" + a.get(Calendar.MONTH) + 1 + "/"
										+ System.currentTimeMillis() + "_"
										+ (int) (Math.random() * 900) + 100
										+ ".jpg";
								String picPath = Bimp.tempSelectBitmap.get(i).getImagePath();
								Bitmap bitmap = ImageUtil.getSmallBitmap(picPath,500,500);
								//截图之后删除本地图片
								ImageUtil.deleteTempFile(picPath);
								HttpUtil.UploadPhotoForQiuniu(token,GDUtil.Bitmap2Bytes(bitmap), key, handler,NET_UPLOAP);
								photosurl[i] = "http://" + domain + "/" + key;
							}
						} else {
							ToastUtil.show(WriteMomentsActivity.this,
									jsonenlist.getString("msg"));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;

			case NET_UPLOAP:
				uploap = uploap + 1;
				if (number == uploap) {
					// Bimp.tempSelectBitmap.clear();
					PostMood(photosurl);
				}
				break;

			case NET_POSTMOOD:
				pb_wait.setVisibility(View.GONE);
				if (msg.obj == null) {
					activity_selectimg_send.setEnabled(true);
					ToastUtil.show(WriteMomentsActivity.this, "网络连接不可用，请稍后重试");
				} else {
					JSONObject jsonobeObject = (JSONObject) msg.obj;
					try {
//						ToastUtil.show(WriteMomentsActivity.this,
//								jsonobeObject.getString("msg"));
						if (jsonobeObject.getInt("status") == 200) {
							
							
							if (!jsonobeObject.isNull("user_action_id")) {
								
								int userActionId = jsonobeObject.getInt("user_action_id");
								getToastContent(userActionId);
							} else {
								FinishWrite();
							}
							
						} else {

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
				
			case NET_COIN_CNT:
				// 弹出金币提示
				if (msg.obj == null) {
					ToastUtil.show(WriteMomentsActivity.this, "网络连接不可用，请稍后重试");
				} else {
					
//					Log.i("Coin", msg.obj.toString());
					
					try {
						JSONObject jsonobeObject = new JSONObject((String)msg.obj);
						if (jsonobeObject.getInt("status") == 200) {
							JSONObject data = jsonobeObject.getJSONObject("data");
							String content = data.getString("alert");
							int coins = data.getInt("coins");
							ToastUtil.showCoin(WriteMomentsActivity.this, content + " +" + coins +" 金币!");
							
						} else {
							ToastUtil.show(WriteMomentsActivity.this, "网络连接不可用，请稍后重试");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				FinishWrite();

				break;

			default:
				break;
			}

		}

	};
	
	/**
	 * 获取弹金币吐司内容
	 */
	private void getToastContent(final int user_action_id) {
		
		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				HttpUtil.Get(UrlConstants.OPERATIONS_ACTION_REWARD + user_action_id +".json" , handler, NET_COIN_CNT);
			}
		});
		
	}

	/**
	 * 发送moment
	 */
	private void PostMood_net(final String words,
			final Map<String, String> params) {
		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				HttpUtil.Post(UrlConstants.MOMENT_WRITE_MOOD, handler,
						NET_POSTMOOD, params);
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for (int i = 0; i < PublicWay.activityList.size(); i++) {
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
					PublicWay.activityList.remove(i);
					
				}
			}
			// 收起软键盘
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et_words.getWindowToken(),
					InputMethodManager.HIDE_IMPLICIT_ONLY);
			finish();
		}
		return true;
	}

	private void reback() {
		finish();
		AppManager.getInstance().finishActivity();
		Intent intent = new Intent(WriteMomentsActivity.this,
				EventsResultActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("eventid", id);
		startActivity(intent);
	}

	private void FinishWrite() {
		
		Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        for (int i = 0; i < PublicWay.activityList.size(); i++) {
        	if (null != PublicWay.activityList.get(i)) {
        		PublicWay.activityList.get(i).finish();
        		PublicWay.activityList.remove(i);
        	}
        }
        this.finish();
		
//		reback();

	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Bimp.tempSelectBitmap.clear();
		PublicWay.activityList.clear();
//		AppManager.getInstance().finishActivity(this);
	}
}
