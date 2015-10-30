package com.oxygen.www.module.team.activity;

import java.io.UnsupportedEncodingException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.ViewUtil;
import com.oxygen.www.widget.CircleImageView;

/**
 * 查看团队二维码界面
 * 
 * @author yang 2015-5-19下午2:37:04
 */
public class TeamQRActivity extends BaseActivity implements OnClickListener {
	// 插入到二维码里面的图片对象
	private Bitmap mIcon;
	private ImageView iv_back, iv_qrimag, iv_share;
	private String shareurl;
	private String name;
	private String pic;
	private String description;
	private String type;
	private String title;
	private CircleImageView iv_head;
	private TextView tv_name,tv_challenge_name, tv_member_count, tv_savaphoto, tv_title;
	private RelativeLayout rl_group_date;
	private RelativeLayout rl_challenge_name;
	private PopupWindow popupWindow;
	/**
	 * 二维码生成成功
	 */
	private boolean success;
	private Bitmap bitmap;
	private final int UPDATEQR= 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_team);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_qrimag = (ImageView) findViewById(R.id.iv_qrimag);
		iv_head = (CircleImageView) findViewById(R.id.iv_head);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_challenge_name = (TextView) findViewById(R.id.tv_challenge_name);
		tv_member_count = (TextView) findViewById(R.id.tv_member_count);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		tv_title = (TextView) findViewById(R.id.tv_title);
		rl_group_date = (RelativeLayout) findViewById(R.id.rl_group_date);
		rl_challenge_name = (RelativeLayout) findViewById(R.id.rl_challenge_name);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		iv_share.setOnClickListener(this);
	}

	private void initValues() {
		mIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
		Bundle b = getIntent().getBundleExtra("data");
		shareurl = b.getString("shareurl");
		name = b.getString("name");
		pic = b.getString("pic");
		title = b.getString("title");
		description = b.getString("description");
		type = b.getString("type");
		tv_title.setText(title);
		if(Constants.QR_CHALLENGES.equals(type)){
			rl_challenge_name.setVisibility(View.VISIBLE);
			tv_challenge_name.setText(name);
		}else{
			ImageUtil.showImage(pic + Constants.qiniu_photo_head, iv_head,
					R.drawable.icon_def);
			tv_member_count.setText(description);
			tv_name.setText(name);
		}
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				createQR();
			}
		});

	}

	private void createQR() {
		if (null != shareurl && !shareurl.equals("")) {
			try {
				bitmap = ImageUtil.cretaeBitmap(new String(shareurl.getBytes(),
						"UTF-8"), mIcon);
				success = true;
 			} catch (UnsupportedEncodingException e) {
				success = false;
 			} catch (WriterException e) {
				success = false;
				
			}
		} else {
			success = false;
 		}
		
		Message msg = new Message();
		msg.what = UPDATEQR;
		msg.obj = bitmap;
		handler.sendMessage(msg);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_share:
			if (success) {
				getPopupWindow();
				// 这里是位置显示方式,在屏幕的左侧
				popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}
			break;
		case R.id.tv_savaphoto:
			// 将二维码写入内存卡
			if (ImageUtil.writeBitmap(bitmap)) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				ToastUtil.show(this, "成功保存图片");
				String mPath = Environment.getExternalStorageDirectory()
						+ "/leyundong/";
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
						Uri.parse("file://" + mPath)));
			} else {
				ToastUtil.show(this, "保存图片失败");
			}
			break;
		default:
			break;
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATEQR:
				Bitmap bm = (Bitmap) msg.obj;
				if(bm==null){
					ToastUtil.show(TeamQRActivity.this, "生成二维码出错！");
				}else{
					iv_qrimag.setImageBitmap(bitmap);
				}
				break;

			default:
				break;
			}
		}

	};

	/***
	 * 获取PopupWindow实例
	 */
	private void getPopupWindow() {
		if (null != popupWindow) {
			popupWindow.dismiss();
			return;
		} else {
			initPopuptWindow();
		}
	}

	/**
	 * 创建PopupWindow
	 */
	protected void initPopuptWindow() {
		// TODO Auto-generated method stub
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(
				R.layout.pop_qrphoto, null, false);
		tv_savaphoto = (TextView) popupWindow_view
				.findViewById(R.id.tv_savaphoto);
		tv_savaphoto.setOnClickListener(this);

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

}
