package com.oxygen.www.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.oxygen.www.utils.AnimateFirstDisplayListener;
import com.oxygen.www.utils.AppManager;

import de.greenrobot.event.EventBus;

/**
 * 框架父类
 * 
 * @author TANGROY
 * 
 */
public class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		AppManager.getInstance().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getInstance().finishActivity(this);
		if (EventBus.getDefault() != null) {
			EventBus.getDefault().unregister(this);
		}
	}

	@Override
	public void onBackPressed() {
		AnimateFirstDisplayListener.displayedImages.clear();
		super.onBackPressed();
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		// 注：回调 1
//		Bugtags.onResume(this);
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		// 注：回调 2
//		Bugtags.onPause(this);
//	}
//
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		// 注：回调 3
//		Bugtags.onDispatchTouchEvent(this, ev);
//		return super.dispatchTouchEvent(ev);
//	}
	//
}
