package com.oxygen.www.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewUtil {
	/**
	 * 设置控件的宽高
	 */
	public static void setViewHeightWidth(View view, int width, int height) {
		LayoutParams layoutParams = view.getLayoutParams();
		if(width != -1){
			layoutParams.width = width;
		}
		if(height != -1){
			layoutParams.height = height;
		}
		view.setLayoutParams(layoutParams);
	}

	/**
	 * 设置控件四周的边距
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public static void setViewInRelativeLayoutbound(View view, int left, int top, int right,
			int bottom) {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
				.getLayoutParams();
		layoutParams.setMargins(left, top, right, bottom);
		view.setLayoutParams(layoutParams);
	}
	
	/**
	 * 设置控件四周的边距
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public static void setViewInLineLayoutbound(View view, int left, int top, int right,
			int bottom) {
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		layoutParams.setMargins(left, top, right, bottom);
		view.setLayoutParams(layoutParams);
	}

	/**
	 * 设置TextView的左边的drawable
	 */
	public static void setTextViewLeftDrawable(Drawable leftDrawable,
			TextView textView) {
		if (leftDrawable != null) {
			leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
					leftDrawable.getMinimumHeight());
		}
		textView.setCompoundDrawables(leftDrawable, null, null, null);
	}

	/**
	 * 设置TextView的上边的drawable
	 */
	public static void setTextViewTopDrawable(Drawable topDrawable,
			TextView textView) {
		if (topDrawable != null) {
			topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(),
					topDrawable.getMinimumHeight());
		}
		textView.setCompoundDrawables(null, topDrawable, null, null);
	}

	/**
	 * 设置TextView的右边的drawable
	 */
	public static void setTextViewRightDrawable(Drawable rightDrawable,
			TextView textView) {
		if (rightDrawable != null) {
			rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
					rightDrawable.getMinimumHeight());
		}
		textView.setCompoundDrawables(null, null, rightDrawable, null);
	}

	/**
	 * 设置TextView的下边的drawable
	 */
	public static void setTextViewBottomDrawable(Drawable bottomDrawable,
			TextView textView) {
		if (bottomDrawable != null) {
			bottomDrawable.setBounds(0, 0, bottomDrawable.getMinimumWidth(),
					bottomDrawable.getMinimumHeight());
		}
		textView.setCompoundDrawables(null, null, null, bottomDrawable);
	}
}
