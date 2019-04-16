package com.example.keepbookkeeping.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.keepbookkeeping.utils.ToastUtil;

/**
 * @author 邹永鹏
 * @date 2019/1/22
 * @description :banner显示的某月份的收入或支出
 * 参考：https://www.jianshu.com/p/2eb8ae713c1f
 * https://blog.csdn.net/xmxkf/article/details/51454685
 */
public class DoubleLineTextView extends View{

    private Paint mPaint;

    public DoubleLineTextView(Context context) {
        this(context, null);
    }
    public DoubleLineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public DoubleLineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint();
        mPaint.setTextSize(20);
        mPaint.setColor(Color.BLACK);
    }

    public void drawMoreLineText(String[] strings,Canvas canvas,Point point){
        //ToastUtil.success("开始绘制");
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics=mPaint.getFontMetrics();
        float top=fontMetrics.top;
        float bottom=fontMetrics.bottom;
        int lineCount=strings.length;
        float total=(lineCount-1)*(bottom-top)+(fontMetrics.descent-fontMetrics.ascent);
        float offset=total/2-bottom;
        for (int i=0;i<lineCount;i++){
            float yAxis=-(lineCount-i-1)*(bottom-top)+offset;
            canvas.drawText(strings[i],point.x,point.y+yAxis+50,mPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String[] strings={"12月收入","10000.00元"};
        drawMoreLineText(strings,canvas,new Point(0,0));
    }
}
