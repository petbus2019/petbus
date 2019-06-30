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

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

//https://www.jianshu.com/p/51b0ab35d9f8
//https://www.jianshu.com/p/4a5949d92821
//https://www.cnblogs.com/justboy/articles/5647253.html
//http://www.luyixian.cn/news_show_9846.aspx
// https://www.bbsmax.com/A/ke5jQ1r7Jr/

// https://blog.csdn.net/w855227/article/details/80499408
class LineGraphicView extends View
{
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private int m_current_xpos = 0;
    private int m_last_xpos = 0;
    private int m_last_ypos = 0;
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
     * Y轴最大值
     */
    private int maxValue;
    /**
     * Y轴间距值
     */
    private int averageValue;
    private int marginTop = 20;
    private int marginBottom = 40;

    /**
     * 曲线上总点数
     */
    private Point[] mPoints;
    /**
     * 纵坐标值
     */
    private ArrayList<Double> yRawData;
    /**
     * 横坐标值
     */
    private ArrayList<String> xRawDatas;
    private ArrayList<Integer> xList = new ArrayList<Integer>();// 记录每个x的值
    private int spacingHeight;

    public LineGraphicView(Context context)
    {
        this(context, null);
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
        // Log.d( "PetBusApp", "PetBusBusiness:onTouchEvent + " + event );
        int x = (int) event.getX();
        int y = (int) event.getY();
        
        // DisplayMetrics metrics = new DisplayMetrics();
        // getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // int widthPixels = metrics.widthPixels;
        // int heightPixels = metrics.heightPixels;

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
                    m_current_xpos += deltaX;
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
            this.canvasHeight = getHeight() - 20;
            this.canvasWidth = getWidth() ;
            if (bheight == 0){
                bheight = (int) (canvasHeight - marginBottom);
            }

            blwidh = dip2px(30);
            isMeasure = false;
        }

        Log.d( "PetBusApp", "PetBusBusiness:onSizeChanged + " + canvasWidth + "--" + getWidth() + "--" + getRight() );
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mPaint.setColor(res.getColor(R.color.color_f2f2f2));

        Log.d( "PetBusApp", "PetBusBusiness:onDraw + " + canvasWidth );
        drawAllXLine(canvas);
        drawAllYLine(canvas);
        mPoints = getPoints();

        mPaint.setColor(res.getColor(R.color.color_ff4631));
        mPaint.setStrokeWidth(dip2px(2.5f));
        mPaint.setStyle(Style.STROKE);
        // if (mStyle == Linestyle.Curve)
        // {
        //     drawScrollLine(canvas);
        // }
        // else
        // {
        //     drawLine(canvas);
        // }

        mPaint.setStyle(Style.FILL);
        for (int i = 0; i < mPoints.length; i++)
        {
            canvas.drawRect( mPoints[i].x + 20, mPoints[i].y, mPoints[i].x + 100, canvasHeight - marginTop, mPaint);
            // canvas.drawCircle(mPoints[i].x, mPoints[i].y, CIRCLE_SIZE / 2, mPaint);
        }
    }

    /**
     *  画所有横向表格，包括X轴
     */
    private void drawAllXLine(Canvas canvas)
    {
        canvas.drawLine(blwidh, bheight + marginTop, (canvasWidth - blwidh)
                       ,bheight + marginTop, mPaint);
        for (int i = 0; i < spacingHeight + 1; i++)
        {
            // Log.d("PetBusApp", "PetBusBusiness:drawAllXLine");
            drawText(String.valueOf(averageValue * i), blwidh / 2, bheight - (bheight / spacingHeight) * i + marginTop,
                    canvas);
        }
    }

    /**
     * 画所有纵向表格，包括Y轴    
     */
    private void drawAllYLine(Canvas canvas)
    {
        canvas.drawLine(blwidh, marginTop, blwidh, bheight + marginTop, mPaint);
        for (int i = 0; i < yRawData.size(); i++)
        {
            // Log.d( "PetBusApp", "PetBusBusiness:drawAllYLine + " + xRawDatas.get(i) );
            xList.add(blwidh + (canvasWidth - blwidh) / yRawData.size() * i);
            drawText(xRawDatas.get(i), blwidh + (canvasWidth - blwidh) / yRawData.size() * i
                     , bheight + dip2px(18), canvas);// X坐标
        }
    }

    private void drawScrollLine(Canvas canvas)
    {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++)
        {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, mPaint);
        }
    }

    private void drawLine(Canvas canvas)
    {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++)
        {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, mPaint);
        }
    }

    private void drawText(String text, int x, int y, Canvas canvas)
    {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(dip2px(12));
        p.setColor(res.getColor(R.color.color_999999));
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text, x, y , p);
    }

    private Point[] getPoints()
    {
        Point[] points = new Point[yRawData.size()];
        for (int i = 0; i < yRawData.size(); i++)
        {
            int ph = bheight - (int) (bheight * (yRawData.get(i) / maxValue));

            points[i] = new Point(xList.get(i), ph + marginTop);
        }
        return points;
    }

    public void setData(ArrayList<Double> yRawData, ArrayList<String> xRawData, int maxValue, int averageValue)
    {
        this.maxValue = maxValue;
        this.averageValue = averageValue;
        this.mPoints = new Point[yRawData.size()];
        this.xRawDatas = xRawData;
        this.yRawData = yRawData;
        this.spacingHeight = maxValue / averageValue;
    }

    public void setTotalvalue(int maxValue)
    {
        this.maxValue = maxValue;
    }

    public void setPjvalue(int averageValue)
    {
        this.averageValue = averageValue;
    }

    public void setMargint(int marginTop)
    {
        this.marginTop = marginTop;
    }

    public void setMarginb(int marginBottom)
    {
        this.marginBottom = marginBottom;
    }

    public void setMstyle(Linestyle mStyle)
    {
        this.mStyle = mStyle;
    }

    public void setBheight(int bheight)
    {
        this.bheight = bheight;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue)
    {
        return (int) (dpValue * dm.density + 0.5f);
    }

}
