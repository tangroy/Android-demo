package com.oxygen.www.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.oxygen.www.R;
import com.oxygen.www.base.OxygenApplication;

/**
 * 
 * @author tangsamba
 *
 */
public class TasksCompletedView extends View {

	// 画实心圆的画笔
	private Paint mCirclePaint;
	// 画圆环的画笔
	private Paint mRingPaint;
	// 圆环颜色
	private int mRingColor;
	// 半径
	private float mRadius;
	// 圆环半径
	private float mRingRadius;
	// 圆环宽度
	private float mStrokeWidth;
	// 圆心x坐标
	private int mXCenter;
	// 圆心y坐标
	private int mYCenter;

	// 总进度
	private int mTotalProgress = 100;
	// 当前进度
	private int mProgress;

	private Bitmap bitmap;

	public TasksCompletedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获取自定义的属性
		bitmap = BitmapFactory.decodeResource(OxygenApplication.context.getResources(),
				R.drawable.icon_over);
		initAttrs(context, attrs);
		initVariable();
		
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		mRadius = bitmap.getWidth() / 2;
		mStrokeWidth = 10;
		mRingColor =OxygenApplication.context.getResources().getColor(R.color.darkcyan);;
		mRingRadius = bitmap.getWidth() / 2 + mStrokeWidth;
	}

	private void initVariable() {
		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);

		mRingPaint = new Paint();
		mRingPaint.setAntiAlias(true);
		mRingPaint.setColor(mRingColor);
		mRingPaint.setStyle(Paint.Style.STROKE);
		mRingPaint.setStrokeWidth(mStrokeWidth);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		mXCenter = getWidth() / 2;
		mYCenter = getHeight() / 2;
		canvas.drawBitmap(bitmap, mXCenter - mRadius, mYCenter - mRadius,
				mCirclePaint);
		// canvas.drawCircle(mXCenter, mYCenter, mCirclePaint);

		if (mProgress > 0) {
			RectF oval = new RectF();
			oval.left = (mXCenter - mRingRadius);
			oval.top = (mYCenter - mRingRadius);
			oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
			oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
			canvas.drawArc(oval, -90,
					((float) mProgress / mTotalProgress) * 360, false,
					mRingPaint); //

		}
	}

	public void setProgress(int progress) {
		mProgress = progress;
		postInvalidate();
	}

}
