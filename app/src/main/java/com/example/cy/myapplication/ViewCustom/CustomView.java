package com.example.cy.myapplication.ViewCustom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by CY on 2017/9/16.
 */

public class CustomView extends View {
    /**     * 实例化时调用     */
    public CustomView (Context context){
        this(context,null);
    }
    /**     * 设置属性时调用     * @param context     * @param attrs 属性集合     */
    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    /**     * 设置样式时调用     * @param context     * @param attrs     * @param defStyleAttr 样式     */
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private Paint paint;
    private void initPaint(){
        // 实例化画笔
        paint = new Paint();
    }
    private RectF rectF;
    @Override
    protected void onDraw(Canvas canvas) {
        paint.setAntiAlias(true);  //抗锯齿
        paint.setStyle(Paint.Style.STROKE);  //描边
        // 设置画笔颜色
        paint.setColor(Color.RED);

        paint.setStrokeWidth(30);
        canvas.drawCircle(500,500,500,paint);

        rectF = new RectF();
        rectF.left = 0;
        rectF.top = 1000;
        rectF.right = 1000;
        rectF.bottom = 1500;
        canvas.drawOval(rectF,paint); //椭圆

        rectF.left = 300;
        rectF.right = 400;
        rectF.top = 300;
        rectF.bottom = 400;
        canvas.drawRect(rectF,paint); //矩形

        rectF.left = 600;rectF.right = 700;
        rectF.top = 300;rectF.bottom = 400;
        canvas.drawRect(rectF,paint);

        rectF.left = 350;rectF.right = 650;
        rectF.top = 600;rectF.bottom = 750;
        canvas.drawRoundRect(rectF,50,50,paint);

        canvas.drawLine(650,700,650,900,paint);  //线

        rectF.left = 0;rectF.right = 1000;
        rectF.top = -200;rectF.bottom = 200;
        canvas.drawArc(rectF,0,180,false,paint);//弧

        rectF.left = 450;rectF.right = 550;
        rectF.top = 400;rectF.bottom = 550;
        canvas.drawArc(rectF,75,30,true,paint);//扇

        // 设置文本大小
        paint.setTextSize(50);
        paint.setStrokeWidth(2);
        // 绘制文字
        canvas.drawText("Hello 妹子，你好~",700,600,paint);
        // 绘制指定长度文本
        canvas.drawText("Hello,嘿嘿嘿~~~~~",6,11,700,700,paint);

        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
