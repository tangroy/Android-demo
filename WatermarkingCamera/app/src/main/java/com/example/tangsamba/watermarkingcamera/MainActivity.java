package com.example.tangsamba.watermarkingcamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;


public class MainActivity extends Activity {
    private int requestcode = 1;
    private ImageView iv_watermarkingphoto;
    private Bitmap bitmap = null;
    private Bitmap waterMak =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_watermarkingphoto = (ImageView) findViewById(R.id.iv_watermarkingphoto);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, requestcode);
        waterMak = BitmapFactory.decodeResource(this.getResources(), R.mipmap.icon_share_appfriend);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestcode == requestCode && data != null) {
            System.out.println("相机数据");
            Bundle b = data.getExtras();
            bitmap = (Bitmap) b.get("data");
            bitmap = createBitmap(bitmap,waterMak,"1111111i love you 44,,一起运动更快乐");
            iv_watermarkingphoto.setImageBitmap(bitmap);

        }
    }

    /**
     * 进行添加水印图片和文字
     *
     * @param src
     * @param waterMak
     * @return
     */
    public Bitmap createBitmap(Bitmap src, Bitmap waterMak, String
            title) {
        // 获取原始图片与水印图片的宽与高
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = waterMak.getWidth();
        int wh = waterMak.getHeight();

        Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(newBitmap);
        // 往位图中开始画入src原始图片
        mCanvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();
        //paint.setAlpha(100);
        mCanvas.drawBitmap(waterMak, w/4, h - wh - 5, paint);
        // 开始加入文字
        if (null != title) {
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(8);
            String familyName = "yyy";
            Typeface typeface = Typeface.createFromAsset(this.getAssets(),
                    "fonts/Impact.ttf");
            textPaint.setTypeface(typeface);
            textPaint.setTextAlign(Paint.Align.CENTER);
            mCanvas.drawText(title, w / 2, 50, textPaint);
        }
        mCanvas.save(Canvas.ALL_SAVE_FLAG);
        mCanvas.restore();
        return newBitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
        }
    }
}
