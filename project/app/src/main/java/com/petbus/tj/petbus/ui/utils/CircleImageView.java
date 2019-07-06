package com.petbus.tj.petbus.ui;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import android.util.Log;
/**
 * 自定义的圆形ImageView，可以直接当组件在布局中使用。
 * @author caizhiming
 *
 */
public class CircleImageView extends ImageView{

    private Paint paint ;
    private Bitmap m_circel_bitmap;
    
    public CircleImageView(Context context) {  
        this(context,null);  
    }  
  
    public CircleImageView(Context context, AttributeSet attrs) {  
        this(context, attrs,0);  
    }  
  
    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle); 
        paint = new Paint();
        
    }  
  
    /**
     * 绘制圆形图片
     * @author caizhiming
     */
    @Override  
    protected void onDraw(Canvas canvas) {  
  
        Drawable drawable = getDrawable();  
        if (null != drawable) {  
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if( null != bitmap )
            {
                Log.d("PetBusApp", "CircleImageView width:" + bitmap.getWidth()+ "--" + getWidth() + "::Height:" + bitmap.getHeight() + "--" + getHeight() );
                Bitmap b = m_circel_bitmap;  
                final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
                final Rect rectDest = new Rect(0,0,getWidth(),getHeight());
                paint.reset();  
                canvas.drawBitmap(b, rectSrc, rectDest, paint);  
            }
            else
            {
                super.onDraw( canvas );
            }
        } else {  
            super.onDraw(canvas);  
        }  
    }

    @Override
    public void setImageBitmap(Bitmap bm_src) {
        Matrix matrix = new Matrix();
        matrix.setScale(0.25f, 0.25f);
        Bitmap bm = Bitmap.createBitmap(bm_src, 0, 0, 
                                        bm_src.getWidth(),
                                        bm_src.getHeight(),
                                        matrix, true);

        Log.d("PetBusApp", "CircleImageView setImageBitmap" );
        m_circel_bitmap = getCircleBitmap( bm_src, 14);
        super.setImageBitmap( m_circel_bitmap );
    }
  
    /**
     * 获取圆形图片方法
     * @param bitmap
     * @param pixels
     * @return Bitmap
     * @author caizhiming
     */
    public Bitmap getCircleBitmap(Bitmap bitmap, int pixels) {
        int x = bitmap.getWidth();
        int y = bitmap.getHeight();
        int left = 0;
        int top = 0;

        int point = x < y ? x : y;
        if (x < y){
            top = -(y - x) / 2;
        }
        else{
            left = -(x - y) / 2;
        }
        Bitmap output = Bitmap.createBitmap(point,
                point, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);  
          
        final int color = 0xff424242;
       
        final Rect rect = new Rect(left, top, point, point);
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle( point / 2, point / 2, point / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;  
    }  
}  