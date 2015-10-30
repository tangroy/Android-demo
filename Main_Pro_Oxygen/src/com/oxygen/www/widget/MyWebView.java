package com.oxygen.www.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

/**
 * @author 张坤
<<<<<<< HEAD
=======
 * 
>>>>>>> 9570eb7301297ac7910888818bf1b607132d1d84
 */
public class MyWebView extends WebView {

	private boolean is_gone = false;

	public MyWebView(Context context) {
		super(context);
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {

		super.onWindowVisibilityChanged(visibility);

		if (visibility == View.GONE) {
			try {
				WebView.class.getMethod("onPause").invoke(this);
				// stop flash
			} catch (Exception e) {

			}

			this.pauseTimers();

			this.is_gone = true;

		} else if (visibility == View.VISIBLE) {

			try {

				WebView.class.getMethod("onResume").invoke(this);
				// resume flash

			} catch (Exception e) {

			}

			this.resumeTimers();

			this.is_gone = false;

		}

	}

	@Override
	protected void onDetachedFromWindow() {

		if (this.is_gone) {

			try {
				this.destroy();

			} catch (Exception e) {

			}

		}

	}

}