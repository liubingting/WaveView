package com.wingsofts.cardiograph;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class WaveView extends View {
    //画笔
    private Paint mPaint;
    private List<PointF> dataPoint = new ArrayList<>();
    private int windowWidth;
    private int windowHeight;
    private static float valueY;
    private int maxValue = 100;//计算Y值的基数
    private int distance = 20; //每一个点走的距离
    private Random random = new Random();
    public WaveView(Context context) {
        this(context,null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        mPaint = new Paint();
        mPaint.setColor(array.getColor(R.styleable.WaveView_paintColor, Color.BLACK));
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        if (windowWidth == 0) {
            initWindowWidth(context);
        }
    }
    //    获取屏幕宽度
    private void initWindowWidth(Context context) {
        WindowManager manager = ((Activity) context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        windowWidth = outMetrics.widthPixels;
    }

    //    波形图Y值计算方法
    private float caculateY() {
//        windowHeight = getHeight();
//        float caulate = (float) Math.abs(windowHeight - ((valueY -4157) / maxValue * windowHeight));
//        if (caulate > 100){
//            caulate = 100;
//        }
//        if (caulate < 0){
//            caulate = 0 ;
//        }
//        return  caulate;
        valueY = random.nextInt(100);
        return valueY;
    }
    //    绘制波形图

    private void drawPath(Canvas canvas){
        if (dataPoint.size() == 0) {
            PointF point1 = new PointF(windowWidth-2,caculateY());
            dataPoint.add(0, point1);
            PointF point2 = new PointF(windowWidth - distance - 2,caculateY());
            dataPoint.add(0, point2);
            return;
        } else {
            if (dataPoint.get(0).x <= windowWidth - distance + 2 && dataPoint.get(1).x <= windowWidth - 2){
                PointF point = new PointF(dataPoint.get(0).x - distance + 2,caculateY());
                dataPoint.add(0,point);
                Iterator<PointF> iterator = dataPoint.iterator();
                while (iterator.hasNext()) {
                    PointF everyPoint = iterator.next();
                    everyPoint.offset(1, 0);
                    if (everyPoint.x < distance) {
                        iterator.remove();
                    }
                }
            }
        }
        if (dataPoint.size() >200) {
            for (int i = 200; i < dataPoint.size(); i++) {
                dataPoint.remove(i);
            }
        }
        PointF point_pre = null;
        for (PointF everyPoint : dataPoint) {
            if (point_pre == null) {
                point_pre = everyPoint;
                continue;//第一个点
            } else {
                canvas.drawLine(everyPoint.x, everyPoint.y, point_pre.x, point_pre.y,mPaint);
                point_pre = everyPoint;
            }
//            canvas.drawCircle(everyPoint.x, everyPoint.y, 0, mPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPath(canvas);
        postInvalidateDelayed(10);
    }


}
