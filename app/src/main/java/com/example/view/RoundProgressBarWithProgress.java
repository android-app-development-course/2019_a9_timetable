package com.example.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.example.myapplication.R;

public class RoundProgressBarWithProgress extends HorizontalProgressBarWithProgress {
private int mRadius=dp2px(60);
private  int mMaxPaintWidth;
    public RoundProgressBarWithProgress(Context context) {
        this(context,null);
    }
    public RoundProgressBarWithProgress(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }
    public RoundProgressBarWithProgress(Context context, AttributeSet attrs,int defStyle) {
        super(context,attrs,defStyle);
        mReachHeight=(int)(mUnreachHeight*2.5f);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBarWithProgress);
        mRadius=(int)ta.getDimension(R.styleable.RoundProgressBarWithProgress_radius,mRadius);
        ta.recycle();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
      //  obtainStyledAttrs(attrs);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxPaintWidth=Math.max(mReachHeight,mUnreachHeight);
        int expect=mRadius*2+mMaxPaintWidth+getPaddingRight()+getPaddingLeft();
        int width=resolveSize(expect,widthMeasureSpec);
        int height=resolveSize(expect,heightMeasureSpec);
        int readWidth= Math.min(width,height);
        mRadius=(readWidth-getPaddingLeft()-getPaddingRight()-mMaxPaintWidth)/2;
        setMeasuredDimension(readWidth,readWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        String text=getProgress()+"%";
        float textWidth=mPaint.measureText(text);
        float textHeight=(mPaint.descent()+mPaint.ascent())/2;
        canvas.save();
        canvas.translate(getPaddingLeft()+mMaxPaintWidth/2,getPaddingTop()+mMaxPaintWidth/2);
        mPaint.setStyle(Paint.Style.STROKE);
        //draw unReach
        mPaint.setColor(mUnreachColor);
        mPaint.setStrokeWidth(mUnreachHeight);
        canvas.drawCircle(mRadius,mRadius,mRadius,mPaint);
        //draw Reach
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
       float sweepAngle=(float)(getProgress()*1.0/getMax()*360);
       canvas.drawArc(new RectF(0,0,mRadius*2,mRadius*2),0,sweepAngle,false,mPaint);
       //draw text
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text,mRadius-textWidth/2,mRadius-textHeight/2,mPaint);

        canvas.restore();
        //draw text
    }
}
