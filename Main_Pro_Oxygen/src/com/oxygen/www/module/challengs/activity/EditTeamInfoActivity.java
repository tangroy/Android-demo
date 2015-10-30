package com.oxygen.www.module.challengs.activity;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.widget.CircleImageView;

/**
 * 编辑战队名称
 * 
 * @author sambatang
 * 
 */
public class EditTeamInfoActivity extends BaseActivity implements
		OnClickListener {
	private ImageView iv_back;
	private CircleImageView iv_head_a, iv_head_b;
	private TextView tv_save;
	private EditText et_name_a, et_name_b;
	private final int CHOOSE_PICTURE_A = 1;
	private final int CHOOSE_PICTURE_B = 2;
	private final int CURT_PICTURE_A = 3;
	private final int CURT_PICTURE_B = 4;
	private Bitmap logo_a, logo_b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challengs_editteaminfo);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_head_a = (CircleImageView) findViewById(R.id.iv_head_a);
		iv_head_b = (CircleImageView) findViewById(R.id.iv_head_b);
		tv_save = (TextView) findViewById(R.id.tv_save);
		et_name_a = (EditText) findViewById(R.id.et_name_a);
		et_name_b = (EditText) findViewById(R.id.et_name_b);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		iv_head_a.setOnClickListener(this);
		iv_head_b.setOnClickListener(this);
		tv_save.setOnClickListener(this);
	}

	private void initValues() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_head_a:
			Intent intent_a = new Intent();
			intent_a.setType("image/*");
			intent_a.setAction(Intent.ACTION_GET_CONTENT);
			// 相片类型
			startActivityForResult(intent_a, CHOOSE_PICTURE_A);
			break;
		case R.id.iv_head_b:
			Intent intent_b = new Intent();
			intent_b.setType("image/*");
			intent_b.setAction(Intent.ACTION_GET_CONTENT);
			// 相片类型
			startActivityForResult(intent_b, CHOOSE_PICTURE_B);
			break;
		case R.id.tv_save:
			Bundle b = new Bundle();
			b.putString("name_a", et_name_a.getText().toString());
			b.putString("name_b", et_name_b.getText().toString());
			b.putParcelable("logo_a", logo_a);
			b.putParcelable("logo_b", logo_b);
			Intent intent = new Intent(EditTeamInfoActivity.this,
					CreatChallengesActivity.class);
			intent.putExtra("teaminfo", b);
			setResult(RESULT_OK, intent);
			finish();
			break;

		default:
			break;
		}

	}

	protected void onActivityResult(int requestCode, int arg1, Intent arg2) {
		if (arg2 != null) {
			Uri uri;
			// 可以根据多个请求代码来作相应的操作
			switch (requestCode) {

			case CHOOSE_PICTURE_A:
				try {
					uri = Uri.parse(
						MediaStore.Images.Media.insertImage(getContentResolver(),
						MediaStore.Images.Media.getBitmap(this.getContentResolver(), arg2.getData()),
						null, 
						null));
					startPhotoZoom(uri, CURT_PICTURE_A);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case CHOOSE_PICTURE_B:
				try {
					uri = Uri.parse(
						MediaStore.Images.Media.insertImage(getContentResolver(),
						MediaStore.Images.Media.getBitmap(this.getContentResolver(), arg2.getData()),
						null, 
						null));
					startPhotoZoom(uri, CURT_PICTURE_B);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case CURT_PICTURE_A:
				if (arg2 != null) {
					setPicToView(arg2, iv_head_a);
				}
				break;
			case CURT_PICTURE_B:
				if (arg2 != null) {
					setPicToView(arg2, iv_head_b);
				}
				break;
			}
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri, int imageview) {
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
		startActivityForResult(intent, imageview);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata, ImageView imageview) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			if (imageview.equals(iv_head_a)) {
				logo_a = bitmap;
			} else {
				logo_b = bitmap;
			}
			imageview.setImageBitmap(bitmap);
		}
	}
}
