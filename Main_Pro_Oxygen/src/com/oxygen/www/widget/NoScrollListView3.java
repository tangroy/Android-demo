package com.oxygen.www.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class NoScrollListView3 extends ListView {

	public NoScrollListView3(Context context) {
		super(context);
	}

	public NoScrollListView3(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollListView3(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}
	
	
}
