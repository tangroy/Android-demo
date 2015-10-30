package com.oxygen.www.utils;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import com.oxygen.www.base.OxygenApplication;
import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class QQUtils {
	public static String[] shareParams = {QQShare.SHARE_TO_QQ_TITLE
										,QQShare.SHARE_TO_QQ_IMAGE_URL
										,QQShare.SHARE_TO_QQ_SUMMARY
										,QQShare.SHARE_TO_QQ_TARGET_URL};
	public static void doShareToQQ(final Bundle params, final Activity context) {
		// QQ分享要在主线程做
		ThreadManager.getMainHandler().post(new Runnable() {
			@Override
			public void run() {
				if (null != OxygenApplication.tencent) {
					OxygenApplication.tencent.shareToQQ(context, params,
							new IUiListener() {
						@Override
						public void onCancel() {

						}

						@Override
						public void onComplete(Object response) {
							ToastUtil.show(context, "成功");
//							ToastUtil.showCoin(context, "已发送邀请！ +200金币");
							
						}

						@Override
						public void onError(UiError e) {
							ToastUtil.show(context, e.errorMessage);
						}
					});
				}
			}
		});
	}
	/**
	 * 
	 * @param context
	 * @param strings 参数依次为1,标题    2,图片url  3,简介    4,跳转地址
	 */
	public static void doShareToQQ(final Activity context,final String... strings) {
		// QQ分享要在主线程做
		ThreadManager.getMainHandler().post(new Runnable() {
			@Override
			public void run() {
				if (null != OxygenApplication.tencent) {
					Bundle params = new Bundle();
					for (int i = 0; i < strings.length; i++) {
						if(!TextUtils.isEmpty(strings[i])){
							params.putString(shareParams[i], strings[i]);
						}
					}
					OxygenApplication.tencent.shareToQQ(context, params,
							new IUiListener() {
						@Override
						public void onCancel() {
							
						}
						
						@Override
						public void onComplete(Object response) {
							ToastUtil.show(context, "成功");
						}
						
						@Override
						public void onError(UiError e) {
							ToastUtil.show(context, e.errorMessage);
						}
					});
				}
			}
		});
	}
}
