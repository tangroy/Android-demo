package com.oxygen.www.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import com.oxygen.www.base.OxygenApplication;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

public class WxUtil {
	public static void share2weixin(int flag, Context c, String url,
			String title, String sport, String intro) {
		// 初始化一个WXTextObject对象
		if(intro!=null&&intro.length()>100){
			intro = intro.substring(0, 100);
		}
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = intro;
		try {
			Bitmap bmp;
			if("logoUrl".equals(sport)){
				bmp = ImageUtil.getBitMBitmap(sport);
			}else{
				bmp = BitmapFactory.decodeResource(
						c.getResources(),
						c.getResources().getIdentifier(sport, "drawable",
								c.getPackageName()));
			}
			//Bitmap bmp = ImageLoader.getInstance().loadImageSync(sport);
			
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
			bmp.recycle();
			msg.setThumbImage(thumbBmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		OxygenApplication.weixinsdk = "share";
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = flag;
		OxygenApplication.api.sendReq(req);
	}
	
	public static void share2weixinPic(int flag,String imageUrl){
		
		File file = new File(imageUrl);
		if (!file.exists()) {
			return;
		}
		WXImageObject imgObj = new WXImageObject();
		imgObj.setImagePath(imageUrl);
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		msg.title = "乐运动";
		Bitmap bmp = BitmapFactory.decodeFile(imageUrl);
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
//		bmp.recycle();
		msg.thumbData = bmpToByteArray(thumbBmp, true);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = flag;
		OxygenApplication.api.sendReq(req);
	}
	
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
