package com.oxygen.www.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class NoScrollListView2 extends ListView {

	public NoScrollListView2(Context context) {
		super(context);
	}

	public NoScrollListView2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollListView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果（该写法太影响性能，请用原生listview或者其他，别用这个控件了）
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		return super.onTouchEvent(ev);
		return false;
	}
	
//	private boolean isMove = false;
	/**
	 * 定义一个boolean的全局变量isMove =
	 * false，然后在onTouch方法里的MotionEvent.ACTION_MOVE:里边设置isMove =
	 * true;在MotionEvent.ACTION_UP:判断isMove的值 if (isMove == false)
	 * {//对click事件的处理} else if (isMove == true) {//对onTouch事件的处理，我仅仅是更新坐标}
	 * 记得一定要设置在break之前再次设置isMove = false;
	 */
/*	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		boolean isMove = false;
		switch (ev.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			isMove = false;
			break;
		case MotionEvent.ACTION_MOVE:
			isMove = true;
			break;
		case MotionEvent.ACTION_UP:
			if (isMove) {
				// 对onTouch事件的处理
				isMove = false;
				return false;
			} else {
				// 对onClick事件的处理
				isMove = false;
				return true;
			}

		default:
			break;
		}
		isMove = false;
		return false;
//		return super.onTouchEvent(ev);
	}*/

	
	
}
