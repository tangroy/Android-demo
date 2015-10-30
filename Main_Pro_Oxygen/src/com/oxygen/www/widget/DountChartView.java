package com.oxygen.www.widget;

import com.oxygen.www.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class DountChartView extends View {
	private int ScrWidth;
	private Paint[] arrPaintArc;
	private Paint PaintText = null;
	private int[] colors = {R.color.grey};
	private float persents[] = {100};
	private String month = "";
	private int[] sportsId = {-1};
	private BlurMaskFilter paintBGBlur;
	

	public DountChartView(Context context) {
		super(context);
		initView();
	}

	public DountChartView(Context context, AttributeSet attrs) {
		super(context,attrs);
		initView();
	}
	
	public DountChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}
	private void initView(){
		// 解决4.1版本 以下canvas.drawTextOnPath()不显示问题
		this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		// 屏幕信息
		DisplayMetrics dm = getResources().getDisplayMetrics();
		ScrWidth = dm.widthPixels;
		paintBGBlur = new BlurMaskFilter(1,BlurMaskFilter.Blur.INNER);
		arrPaintArc = new Paint[colors.length];
		for (int i = 0; i < colors.length; i++) {
			arrPaintArc[i] = new Paint();
			arrPaintArc[i].setColor(getResources().getColor(colors[i] ));
			arrPaintArc[i].setStyle(Paint.Style.FILL);
			arrPaintArc[i].setStrokeWidth(4);
			arrPaintArc[i].setMaskFilter(paintBGBlur);
		}
		PaintText = new Paint();
		PaintText.setColor(Color.BLUE);
		PaintText.setTextSize(22);
	}
	
	public void setMonth(String month) {
		this.month = month;
		postInvalidate();
	}
	public void setSports(int[] sportsId) {
		this.sportsId = sportsId;
	}
	public void setColors(int[] colors) {
		this.colors = colors;
		arrPaintArc = new Paint[colors.length];
		for (int i = 0; i < colors.length; i++) {
			arrPaintArc[i] = new Paint();
			arrPaintArc[i].setColor(getResources().getColor(colors[i] ));
			arrPaintArc[i].setStyle(Paint.Style.FILL);
			arrPaintArc[i].setStrokeWidth(4);
			arrPaintArc[i].setMaskFilter(paintBGBlur);
		}
	}
	public void setPersents(float[] persents) {
		this.persents = persents;
    	postInvalidate();
	}

	public void onDraw(Canvas canvas) {
		// 画布背景
		canvas.drawColor(Color.WHITE);
		float cirX = ScrWidth / 2;
		float cirY = ScrWidth / 2;
		float radius = ScrWidth / 3;

		float arcLeft = cirX - radius;
		float arcTop = cirY - radius;
		float arcRight = cirX + radius;
		float arcBottom = cirY + radius;
		RectF arcRF0 = new RectF(arcLeft, arcTop, arcRight, arcBottom);
		Path pathArc = new Path();
		// x,y,半径 ,CW
		pathArc.addCircle(cirX, cirY, radius, Direction.CW);
		// 绘出饼图大轮廓
		canvas.drawPath(pathArc, arrPaintArc[0]);
		float startAngle = 0f; // 偏移角度
		float percentangle = 0f; // 当前所占比例

		int i = 0;
		if(colors.length > 1){
			for (i = 0; i < colors.length; i++){
				// 将百分比转换为饼图显示角度
				percentangle = 360 * (persents[i] / 100);
				percentangle = (float) (Math.round(percentangle * 100)) / 100;
				
				// 在饼图中显示所占比例
				canvas.drawArc(arcRF0, startAngle, percentangle, true, arrPaintArc[i]);
				drawLine(canvas, cirX, cirY, radius, startAngle, percentangle,sportsId[i],persents[i]);
				// 下次的起始角度
				startAngle += percentangle;
			}
		}
		// 画内圆
		PaintText.setColor(Color.WHITE);
		canvas.drawCircle(cirX, cirY, radius *2 / 3, PaintText);
		PaintText.setColor(Color.BLACK);
		PaintText.setTextSize(100.f);
		canvas.drawText(month, cirX + (3-month.length()) * 20 - 100.f, cirY + 35.f, PaintText);
		PaintText.setTextSize(30.f);
	}

	/**
	 * 
	 * @param canvas
	 * @param cirX
	 *            原点横坐标
	 * @param cirY
	 *            原点纵坐标
	 * @param arrPer
	 *            旋转百分比
	 */
	private void drawLine(Canvas canvas, float cirX, float cirY, float radius,
			float startAngle,float percentangle,int sportsid,float percent) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), sportsid);
		int height = bitmap.getHeight();
		float allPercent = startAngle + percentangle/2;
		if(percentangle < 18 || percentangle == 360){
			//do nothing
		}else{
			double sin = Math.sin(Math.toRadians(allPercent));
			double cos = Math.cos(Math.toRadians(allPercent));
			// 起点
			float startPointX;
			float startPointY;
			float endPointX;
			float endPointY;
			if( 45  < allPercent && allPercent < 135){
				startPointX = (float) (cirX + (radius + 20.f) * cos);
				startPointY = (float) (cirY + (radius + 20.f) * sin);
				// 终点
				endPointX = (float) (cirX + (radius + height) * cos);
				endPointY = (float) (cirY + (radius + height) * sin);
			}else{
				startPointX = (float) (cirX + (radius + 20.f) * cos);
				startPointY = (float) (cirY + (radius + 20.f) * sin);
				// 终点
				endPointX = (float) (cirX + (radius + 50.f) * cos);
				endPointY = (float) (cirY + (radius + 50.f) * sin);
			}
			PaintText.setColor(Color.BLACK);
			// PaintText.set
			// 起始位置小黑点
			canvas.drawCircle(startPointX, startPointY, 3.f, PaintText);
			canvas.drawLine(startPointX, startPointY, endPointX, endPointY,
					PaintText);
			
			startPointX = endPointX;
			startPointY = endPointY;
			//画运动图标
			if (90 < allPercent && allPercent < 270) {
				endPointX = startPointX - height;
				canvas.drawBitmap(bitmap, endPointX, startPointY - height -5, PaintText);
				canvas.drawText((int)percent+"%", endPointX+15, startPointY+25, PaintText);
			} else {
				endPointX = startPointX + height;
				canvas.drawBitmap(bitmap, startPointX, startPointY - height -5, PaintText);
				canvas.drawText((int)percent+"%", startPointX+15, startPointY+25, PaintText);
			}
			//画第二条线
			canvas.drawLine(startPointX, startPointY, endPointX, endPointY,PaintText);
		}
	}
}
