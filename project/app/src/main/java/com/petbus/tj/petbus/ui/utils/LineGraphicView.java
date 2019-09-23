package com.petbus.tj.petbus.ui;

import com.petbus.tj.petbus.ui.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.MotionEvent;
import android.graphics.PathEffect;
import android.graphics.DashPathEffect;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

//https://www.jianshu.com/p/51b0ab35d9f8
//https://www.jianshu.com/p/4a5949d92821
//https://www.cnblogs.com/justboy/articles/5647253.html
//http://www.luyixian.cn/news_show_9846.aspx
// https://www.bbsmax.com/A/ke5jQ1r7Jr/

// https://blog.csdn.net/w855227/article/details/80499408

class graphic_data
{
    public graphic_data( int years, int month,int feed,int bath,int shovelshit,int walk )
    {
        m_years = years;
        m_month = month;
        m_feed = feed;
        m_bath = bath;
        m_walk = walk;
        m_shovelshit = shovelshit;
    }
    public int m_years;
    public int m_month;
    public int m_feed;
    public int m_bath;
    public int m_walk;
    public int m_shovelshit;
}

public class LineGraphicView extends View
{
    private static int m_text_size = 12;
    private static int m_text_space_size = 6;
    private static int m_jiange_size = 20;
    private static int m_item_size = 40;
    private static int m_signal_size = 10;
    private static int marginLeft = 30;
    private static int marginBottom = 54;
    private static int height_per_value = 4;

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private int m_current_xpos = 0;
    private int m_last_xpos = 0;
    private int m_last_ypos = 0;

    private int m_current_years = 0;
    /**
     * 公共部分
     */
    private static final int CIRCLE_SIZE = 10;

    private static enum Linestyle
    {
        Line, Curve
    }

    private Context mContext;
    private Paint mPaint;
    private Resources res;
    private DisplayMetrics dm;

    /**
     * data
     */
    private Linestyle mStyle = Linestyle.Curve;

    private int canvasHeight;
    private int canvasWidth;
    private int bheight = 0;
    private int blwidh;
    private boolean isMeasure = true;

    /**
     * Y轴间距值
     */

    /**
     * 纵坐标值
     */
    private ArrayList<Double> yRawData;
    /**
     * 横坐标值
     */
    private ArrayList<String> xRawDatas;

    private ArrayList<graphic_data> m_graphic_data = new ArrayList<graphic_data>();

    public LineGraphicView(Context context)
    {
        this(context, null);
        mContext = context;
        initView();
    }

    public LineGraphicView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView()
    {
        this.res = mContext.getResources();
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(widthMeasureSpec * 2, heightMeasureSpec);
    }
    /**
     * 计算两个手指间的距离
     *
     * @param event 触摸事件
     * @return 放回两个手指之间的距离
     */
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);//两点间距离公式
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int x = (int) event.getX();
        int y = (int) event.getY();
        
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                //单点触控
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //多点触控
                float oriDis = distance(event);
                if (oriDis > 10f) {
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指滑动事件
                int deltaX = x - m_last_xpos;
                if (mode == DRAG) {
                    // 是一个手指拖动
                    m_current_xpos -= deltaX;
                    if( m_current_xpos <= 0 )
                    {
                        m_current_xpos = 0;
                    }
                    else if( m_current_xpos >= canvasWidth - getWidth() / 2 )
                    {

                        m_current_xpos = canvasWidth - getWidth() / 2;
                    }
                    setScrollX( m_current_xpos );
                } else if (mode == ZOOM) {
                    // 两个手指滑动
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                // 手指放开事件
                mode = NONE;
                break;
        }
        m_last_xpos = x;
        m_last_ypos = y;
        Log.d( "PetBusApp", "PetBusBusiness:onTouchEvent + " + mode );
        return true;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if (isMeasure)
        {
            this.canvasHeight = getHeight();
            this.canvasWidth = getWidth() ;
            if (bheight == 0){
                bheight = (int) (canvasHeight - marginBottom);
            }

            blwidh = dip2px(m_text_size + m_text_space_size);
            isMeasure = false;
        }

        Log.d( "PetBusApp", "PetBusBusiness:onSizeChanged + " + canvasWidth + "--" + getWidth() + "--" + getRight() );
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mPaint.setColor(res.getColor(R.color.colorBlack));

        drawAllXLine(canvas);
        drawAllYLine(canvas);

        mPaint.setColor(res.getColor(R.color.colorBlack));
        mPaint.setStrokeWidth(dip2px(1f));
        mPaint.setStyle(Style.STROKE);
        mPaint.setStyle(Style.FILL);
    }

    private void drawDashlie( Canvas canvas, Paint paint_p,int x, int y, int x1, int y1 )
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(res.getColor(R.color.colorBlack));
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x1,y1);      
        PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }

    /**
     *  画所有横向表格，包括X轴
     */
    private void drawAllXLine(Canvas canvas)
    {
        int linx_x = bheight - 10 * dpToPx( height_per_value );
        // canvas.drawLine(0, linx_x, (canvasWidth ),linx_x , mPaint);
        drawDashlie( canvas, mPaint, 0,linx_x, canvasWidth, linx_x );

        linx_x -= 10 * dpToPx( height_per_value );
        // canvas.drawLine(0, linx_x, (canvasWidth ),linx_x , mPaint);
        drawDashlie( canvas, mPaint, 0,linx_x, canvasWidth, linx_x );

        linx_x -= 10 * dpToPx( height_per_value );
        // canvas.drawLine(0, linx_x, (canvasWidth ),linx_x , mPaint);
        drawDashlie( canvas, mPaint, 0,linx_x, canvasWidth, linx_x );

        linx_x -= 10 * dpToPx( height_per_value );
        // canvas.drawLine(0, linx_x, (canvasWidth ),linx_x , mPaint);
        drawDashlie( canvas, mPaint, 0,linx_x, canvasWidth, linx_x );

        canvas.drawLine(0, bheight, (canvasWidth ),bheight , mPaint);
    }

    private void draw_signal_view( int i, Canvas canvas,int feed_value,int bath_value,int shovelshit, int walk_value )
    {
        int item_width = dpToPx( m_item_size );
        int total_size = dpToPx( m_item_size + m_jiange_size );
        int left = dpToPx( marginLeft );
        int signal_width = dpToPx( m_signal_size );
        int offset = 0;

        feed_value = feed_value * dpToPx( height_per_value );
        bath_value = bath_value * dpToPx( height_per_value );
        shovelshit = shovelshit * dpToPx( height_per_value );
        walk_value = walk_value * dpToPx( height_per_value );

        mPaint.setColor(res.getColor(R.color.colorBlack));
        String month = String.valueOf(m_graphic_data.get(i).m_month);
        drawText( month, left + (item_width - dpToPx(m_text_size) ) / 2 + total_size * i, bheight + dpToPx(m_text_size), canvas);// X坐标
        
        mPaint.setColor(res.getColor(R.color.overview_feed));
        canvas.drawRect( left + offset + total_size * i , bheight - feed_value, left + offset + total_size * i + signal_width, bheight, mPaint);
        offset += signal_width;

        mPaint.setColor(res.getColor(R.color.overview_bath));
        canvas.drawRect( left + offset + total_size * i , bheight - bath_value, left + offset + total_size * i + signal_width, bheight, mPaint);
        offset += signal_width;

        mPaint.setColor(res.getColor(R.color.overview_shovelshit));
        canvas.drawRect( left + offset + total_size * i , bheight - shovelshit, left + offset + total_size * i + signal_width, bheight, mPaint);
        offset += signal_width;

        mPaint.setColor(res.getColor(R.color.overview_walk));
        canvas.drawRect( left + offset + total_size * i , bheight - walk_value, left + offset + total_size * i + signal_width, bheight, mPaint);
    }
    /**
     * 画所有纵向表格，包括Y轴
     */
    private void drawAllYLine(Canvas canvas)
    {
        int total_size = dip2px( m_item_size + m_jiange_size );
        for (int i = 0; i < m_graphic_data.size(); i++)
        {
            if( m_current_years != m_graphic_data.get(i).m_years )
            {
                m_current_years = m_graphic_data.get(i).m_years;
                canvas.save();
                canvas.rotate(-18, blwidh + i * total_size , bheight + dip2px( m_text_size) );
                drawText( String.valueOf(m_current_years), i * total_size + dip2px(m_text_space_size) , bheight + dip2px(m_text_size)+ dip2px(m_text_space_size / 2), canvas);
                canvas.restore();
            }
            draw_signal_view( i,canvas, m_graphic_data.get(i).m_feed, m_graphic_data.get(i).m_bath
                            , m_graphic_data.get(i).m_shovelshit, m_graphic_data.get(i).m_walk );
        }
    }

    private void drawText(String text, int x, int y, Canvas canvas)
    {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(dip2px(12));
        p.setColor(res.getColor(R.color.colorBlack));
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text, x, y , p);
    }

    public void setData(ArrayList<Double> yRawData, ArrayList<String> xRawData, int maxValue, int averageValue)
    {
        this.xRawDatas = xRawData;
        this.yRawData = yRawData;
    }

    public void clearData()
    {
        m_current_years = 0;
        m_graphic_data.clear();
    }

    public void addData( int years, int month, int feed,int bath,int shovelshit, int walk )
    {
        graphic_data data = new graphic_data( years, month, feed, bath, shovelshit, walk );
        m_graphic_data.add( data );
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue)
    {
        return (int) (dpValue * dm.density + 0.5f);
    }
    private int dpToPx(int dps) {
       return Math.round(getResources().getDisplayMetrics().density * dps);
    }

}
