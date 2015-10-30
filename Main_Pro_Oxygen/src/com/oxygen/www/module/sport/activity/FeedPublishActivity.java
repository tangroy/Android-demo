package com.oxygen.www.module.sport.activity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.TextUtils;
import com.oxygen.www.utils.ToastUtil;
import com.qiniu.android.http.ResponseInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 动态-发布动态
 * 
 * @author 张坤
 *
 */
public class FeedPublishActivity extends Activity implements OnClickListener {
	
	private static final int requestCodeSelectTags = 10;
	private static final int TAKE_PICTURE = 20;
	private static final int CHOOSE_PICTURE = 30;
	
	/**
	 * 获取上传图片的 token
	 */
	public static final int NET_GETTOKEN = 1;
	/**
	 * 上传图片至七牛服务器
	 */
	public static final int NET_UPLOAP = 2;
	/**
	 * 发表动态
	 */
	private final int NET_POSTMOOD = 3;
	/**
	 * 保存顶部墙纸(拍照/相册选择)
	 */
	private Bitmap photoBp;
	/**
	 * 顶部墙纸图片地址
	 */
	private String picurl = null;
	private Calendar a = Calendar.getInstance();
	/**
	 * 标签
	 */
	private String tags;

	private ImageView iv_back;
	private ImageView iv_select;
	private TextView tv_publish;
	private TextView tv_tags;
	private TextView tv_takephoto;
	private EditText et_words;
	private RelativeLayout rl_tag;

	private RelativeLayout rl_loading;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_feed_publish);
		
		initViews();
		initEvents();
		initValues();
		
	}

	private void initViews() {
		// TODO Auto-generated method stub
		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_publish = (TextView) findViewById(R.id.tv_publish);
		tv_takephoto = (TextView) findViewById(R.id.tv_takephoto);

		iv_select= (ImageView) findViewById(R.id.iv_select);
		et_words = (EditText) findViewById(R.id.et_words);
		
		rl_tag = (RelativeLayout) findViewById(R.id.rl_tag);
		tv_tags = (TextView) findViewById(R.id.tv_tags);
		
	}

	private void initEvents() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(this);
		tv_publish.setOnClickListener(this);
		iv_select.setOnClickListener(this);
		rl_tag.setOnClickListener(this);
		
	}

	private void initValues() {
		// TODO Auto-generated method stub
			
		Intent intent = getIntent();
        if(intent != null)  {
//            Bitmap bitmap = intent.getParcelableExtra("bitmap");
        	Uri uri = intent.getParcelableExtra("uri");
        	Bitmap bitmap;
			try {
				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
				iv_select.setImageBitmap(bitmap);
				
				photoBp = bitmap;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Intent intent;
		
		switch (v.getId()) {
		
		case R.id.iv_back:
			finish();
			break;
		
		case R.id.iv_select:
			// TODO...
			Builder builder = new Builder(this);
			final AlertDialog  dialog = builder.create();
			View dialogview = View.inflate(this, R.layout.dialog_feed_select_photo, null);
			final TextView tv_photo = (TextView) dialogview.findViewById(R.id.tv_photo);
			final TextView tv_select = (TextView) dialogview.findViewById(R.id.tv_select);
			
			dialog.setView(dialogview, 0, 0, 0, 0);

			dialog.setCanceledOnTouchOutside(true);
			
			dialog.show();
			
			tv_photo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					photo();
					dialog.dismiss();
					
				}
			});
			
			tv_select.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(intent, CHOOSE_PICTURE);
					
					dialog.dismiss();
				}
			});
			
			
			break;

		case R.id.tv_publish:
			
			// TODO...
			tv_publish.setTextColor(getApplicationContext().getResources().getColor(R.color.mygray));
			tv_publish.setEnabled(false);
			if (photoBp != null) {
				// 是否需要上传图片(更该页面顶部壁纸)
				getQiuniuToken();
			} 
			
			
			break;

		case R.id.rl_tag:
			// TODO...
			intent = new Intent(this, FeedPublishSelectTagsActivity.class);
			if (tags != null && !"[]".equals(tags)) {
				intent.putExtra("tags", tags);
			}
			startActivityForResult(intent, requestCodeSelectTags);
			break;
			
			

		default:
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (data != null) {
			if (requestCode == requestCodeSelectTags) {
				
				tags = data.getStringExtra("tags");

				if (!"[]".equals(tags)) {
					tv_tags.setText(tags);
				} else {
					tv_tags.setText("至少选择一个标签!");
					
				}
				
			}
			
			if (TAKE_PICTURE == requestCode && Activity.RESULT_OK == resultCode) {
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				iv_select.setImageBitmap(bm);
				
				tv_takephoto.setVisibility(View.GONE);
				
				photoBp = bm;
				
			}
			
			if (CHOOSE_PICTURE == requestCode && Activity.RESULT_OK == resultCode) {
				try {
					
					photoBp = MediaStore.Images.Media.getBitmap(
							this.getContentResolver(), data.getData());
					iv_select.setImageBitmap(photoBp);
					tv_takephoto.setVisibility(View.GONE);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
		}
		
		
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			
			case NET_POSTMOOD:
				// TODO...
				finish();
				break;
				
			case NET_GETTOKEN:
				// 获取上传图片的 token
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsonenlist = new JSONObject(strObject);
						if (jsonenlist.getInt("status") == 200) {
							String token = jsonenlist.getJSONObject("data")
									.getString("token");
							String domain = jsonenlist.getJSONObject("data")
									.getString("domain");

							String key = "events/" + a.get(Calendar.YEAR) + ""
									+ a.get(Calendar.MONTH) + 1 + "/"
									+ System.currentTimeMillis() + "_"
									+ (int) (Math.random() * 900) + 100
									+ ".jpg";
							HttpUtil.UploadPhotoForQiuniu(token,
									GDUtil.Bitmap2Bytes(photoBp), key, handler,
									NET_UPLOAP);
							picurl = "http://" + domain + "/" + key;

						} else {
							ToastUtil.show(FeedPublishActivity.this,
									jsonenlist.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
				
			case NET_UPLOAP:
				// 上传图片
				ResponseInfo responseInfo = (ResponseInfo) msg.obj;
				if (responseInfo.statusCode == 200) {
					
					Map<String, String> params = new HashMap<String, String>();
					if (!TextUtils.isEmpty(tags) && !"[]".equals(tags)) {
						params.put("tags", GDUtil.deleteBrackets(tags).replace(" ", ""));
//						Log.i("tagClick", "上传:"+GDUtil.deleteBrackets(tags).replace(" ", ""));
					} else {
						ToastUtil.showShort(FeedPublishActivity.this, "至少添加一个标签!");
						tv_publish.setTextColor(Color.BLACK);
						tv_publish.setEnabled(true);
						return;
					}
					
					if (picurl != null) {
						params.put("photos", picurl);
					} else {
						ToastUtil.showShort(FeedPublishActivity.this, "请选择一张图片!");
						tv_publish.setTextColor(Color.BLACK);
						tv_publish.setEnabled(true);
						return;
					}
					
					String words = et_words.getText().toString();
					if (words != null && words.length() > 1000) {
						ToastUtil.showShort(FeedPublishActivity.this, "最多只能500字!");
						tv_publish.setTextColor(Color.BLACK);
						tv_publish.setEnabled(true);
						return;
					} else {
						params.put("words", et_words.getText().toString());
					}
					
					params.put("feed", "yes");
					
					publish(params);
					
					
					
					
				} else {
//					progressbar.setVisibility(View.GONE);
					tv_publish.setTextColor(Color.BLACK);
					tv_publish.setEnabled(true);
					
					ToastUtil.show(FeedPublishActivity.this, "图片上传失败");
				}
				break;


			default:
				break;
			}
			
		};
	};
	
	/**
	 * 发表动态
	 */
	private void publish(final Map<String, String> params) {
		rl_loading.setVisibility(View.VISIBLE);
		
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.MOMENT_WRITE_MOOD, handler,
						NET_POSTMOOD, params);
				GDUtil.setGlobal(FeedPublishActivity.this, "feedpulish", true);
			}
		});
	}
	
	
	/**
	 * 拍照
	 */
	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
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
	 

}
