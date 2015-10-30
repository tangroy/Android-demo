package com.oxygen.www.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.module.sport.activity.GDActivityResultActivity;

public class ImageUtil {

	public static void showImage(String url, final ImageView iamge,
			final int def_drawable) {
		// ImageLoadingListener animateFirstListener = new
		// AnimateFirstDisplayListener();
		ImageLoader imageLoader = ImageLoader.getInstance();
		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(def_drawable)
				.showImageForEmptyUri(def_drawable)
				.showImageOnFail(def_drawable)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// 设置图片的解码类型//
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.build();

		if (TextUtils.isEmpty(url)) {
			iamge.setBackgroundResource(def_drawable);
		} else if (!url.startsWith("http://")) {
			Log.i("http", url);
			iamge.setBackgroundResource(def_drawable);
		}
		/*
		 * if (url!=null&&!"".equals(url)&&!"null".equals(url)&&!url.startsWith(
		 * "http://")) { url = "http://"+url; }
		 * if(url==null||"".equals(url)||"null".equals(url)){
		 * iamge.setBackgroundResource(def_drawable); }
		 */else {
			ImageAware imageAware = new ImageViewAware(iamge, false);
			imageLoader.displayImage(url, imageAware, options,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							iamge.setBackgroundResource(def_drawable);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub
							iamge.setBackgroundResource(def_drawable);
						}
					});

			/*
			 * imageLoader.displayImage(url, iamge, options, new
			 * ImageLoadingListener() {
			 * 
			 * @Override public void onLoadingStarted(String imageUri, View
			 * view) {
			 * 
			 * }
			 * 
			 * @Override public void onLoadingFailed(String imageUri, View view,
			 * FailReason failReason) { // TODO Auto-generated method stub
			 * iamge.setBackgroundResource(def_drawable); }
			 * 
			 * @Override public void onLoadingComplete(String imageUri, View
			 * view, Bitmap loadedImage) { // TODO Auto-generated method stub
			 * 
			 * }
			 * 
			 * @Override public void onLoadingCancelled(String imageUri, View
			 * view) { // TODO Auto-generated method stub
			 * iamge.setBackgroundResource(def_drawable); } });
			 */
		}
	}

	public static void showImage2(String url, final ImageView iamge,
			final int def_drawable) {
		ImageLoader imageLoader = ImageLoader.getInstance();
		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(def_drawable)
				.showImageForEmptyUri(def_drawable)
				.showImageOnFail(def_drawable)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// 设置图片的解码类型//
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.build();

		if (TextUtils.isEmpty(url)) {
			iamge.setImageResource(def_drawable);
		} else if (!url.startsWith("http://")) {
			Log.i("http", url);
			iamge.setImageResource(def_drawable);
		} else {

			imageLoader.loadImage(url, options, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					iamge.setImageResource(def_drawable);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					iamge.setImageResource(def_drawable);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					iamge.setImageBitmap(loadedImage);
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					iamge.setImageResource(def_drawable);
				}
			});

		}
	}

	/**
	 * 生成二维码 中间插入小图片
	 * 
	 * @param str
	 *            内容
	 * @return Bitmap
	 * @throws WriterException
	 */
	public static Bitmap cretaeBitmap(String str, Bitmap icon)
			throws WriterException {
		// 图片宽度的一般
		int IMAGE_HALFWIDTH = 20;
		// 前景色
		int FOREGROUND_COLOR = 0xff000000;
		// 背景色
		int BACKGROUND_COLOR = 0xffffffff;
		// 缩放一个40*40的图片
		icon = Untilly.zoomBitmap(icon, IMAGE_HALFWIDTH);
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, 300, 300, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int halfW = width / 2;
		int halfH = height / 2;
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
						&& y > halfH - IMAGE_HALFWIDTH
						&& y < halfH + IMAGE_HALFWIDTH) {
					pixels[y * width + x] = icon.getPixel(x - halfW
							+ IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
				} else {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = FOREGROUND_COLOR;
					} else { // 无信息设置像素点为白色
						pixels[y * width + x] = BACKGROUND_COLOR;
					}
				}

			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

		return bitmap;
	}

	/**
	 * 保存二维码
	 * 
	 * @param b
	 * @return
	 */
	public static boolean writeBitmap(Bitmap b) {

		String mPath = Environment.getExternalStorageDirectory()
				+ "/leyundong/";
		String mFileName = java.lang.System.currentTimeMillis() + ".png";
		ByteArrayOutputStream by = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.PNG, 100, by);
		byte[] stream = by.toByteArray();
		return Untilly.writeToSdcard(stream, mPath, mFileName);
	}

	/**
	 * 截图并保存到本地
	 * 
	 * @param a
	 *            activity 对象
	 * @param filePath
	 *            图片保存的路径
	 */

	public static void screenShoot(Activity a, File filePath, int falg) {
		if (filePath == null) {
			return;
		}
		if (!filePath.getParentFile().exists()) {
			filePath.getParentFile().mkdirs();
		}
		ImageUtil.savePic(ImageUtil.takeScreenShot(a,null), filePath, falg);
	}

	public static Bitmap takeScreenShot(Activity activity,View shotView) {
		// View是你需要截图的View
		View view;
		if(shotView == null){
			view = activity.getWindow().getDecorView();
		}else{
			view = shotView;
		}
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();

		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		// 获取屏幕长和高
		@SuppressWarnings("deprecation")
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		@SuppressWarnings("deprecation")
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		//
		int contentTop = activity.getWindow()
				.findViewById(Window.ID_ANDROID_CONTENT).getTop();
		// statusBarHeight是上面所求的状态栏的高度
		int titleBarHeight = contentTop - statusBarHeight;
		int cutHeight = titleBarHeight + statusBarHeight;

		int bHeight = b1.getHeight();
		int nHeight = height - cutHeight;
		if ((cutHeight + nHeight) > bHeight) {
			nHeight = bHeight - cutHeight;
		}
		// 去掉标题栏
		Bitmap b = Bitmap.createBitmap(b1, 0, cutHeight, width, nHeight);
		view.destroyDrawingCache();
		return b;
	}

	private static void savePic(Bitmap b, File filePath, int flag) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			}
			if (flag == GDActivityResultActivity.SHARE_WX
					|| flag == GDActivityResultActivity.SHARE_WXF) {
				WxUtil.share2weixinPic(flag, filePath.toString());
			}
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public static void showImage(String url, ImageView iamge, int def_drawable,
			boolean scrolling) {
		if (!scrolling) {
			showImage(url, iamge, def_drawable);
		} 
		
	}  
	
//	private static Bitmap compressBmpFromBmp(Bitmap image) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        int options = 100;
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        while (baos.toByteArray().length / 1024 > 100) { 
//                baos.reset();
//                options -= 10;
//                image.compress(Bitmap.CompressFormat.JPEG, options, baos);
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
//        return bitmap;
//	}

	/**
	 * 图片毛玻璃效果处理
	 * @param sentBitmap
	 * @param radius
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressLint("NewApi")
	public static Bitmap blur(Bitmap sentBitmap, float radius) {
		//if (VERSION.SDK_INT > 16) {
			Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
			final RenderScript rs = RenderScript
					.create(OxygenApplication.context);
			final Allocation input = Allocation.createFromBitmap(rs,
					sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
					Allocation.USAGE_SCRIPT);
			final Allocation output = Allocation.createTyped(rs,
					input.getType());
			final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
					Element.U8_4(rs));
			script.setRadius(radius /* e.g. 3.f */);
			script.setInput(input);
			script.forEach(output);
			output.copyTo(bitmap);
			return bitmap;
		//} else {
		//	return sentBitmap;
		//}
	}
	
	
	/**
	 * 把bitmap转换成String
	 * 
	 * @param filePath
	 * @return
	 */
	public static String bitmapToString(String filePath,int width,int height) {

		Bitmap bm = getSmallBitmap(filePath, width, height);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();
		
		return Base64.encodeToString(b, Base64.DEFAULT);
		
	}

	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	

	
	/**
	 * 根据路径获得突破并压缩返回bitmap用于显示
	 * @param filePath
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath,int width,int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 根据路径删除图片
	 * 
	 * @param path
	 */
	public static void deleteTempFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 添加到图库
	 */
	public static void galleryAddPic(Context context, String path) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(path);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}

	/**
	 * 获取保存图片的目录
	 * 
	 * @return
	 */
	public static File getAlbumDir() {
		File dir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				getAlbumName());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 获取保存 隐患检查的图片文件夹名称
	 * 
	 * @return
	 */
	public static String getAlbumName() {
		return "sheguantong";
	}
	
	/**
	 * @param urlpath
	 * @return Bitmap
	 * 根据图片url获取图片对象
	 */
	public static Bitmap getBitMBitmap(String urlpath) {
		Bitmap map = null;
		try {
			URL url = new URL(urlpath);
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream in;
			in = conn.getInputStream();
			map = BitmapFactory.decodeStream(in);
			// TODO Auto-generated catch block
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
