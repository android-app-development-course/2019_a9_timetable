package com.example.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.example.myapplication.R;

public class HorizontalProgressBarWithProgress extends ProgressBar {
    private  static final int DEFAULT_TEXT_SIZE=16;
    private  static final int DEFAULT_TEXT_COLOR=0XFF00ffff;
    private  static final int DEFAULT_COLOR_UNREACH=0XFFD3D6DA;
    private  static final int DEFAULT_HEIGHT_UNREACH=2;
    private  static final int DEFAULT_COLOR_REACH=0XFF00ffff;
    private  static final int DEFAULT_HEIGHT_REACH=2;
    private  static final int DEFAULT_TEXT_OFFSET=10;
    protected   int mTextSize=sp2px(DEFAULT_TEXT_SIZE);
    protected int  mTextColor=DEFAULT_TEXT_COLOR;
    protected int mUnreachColor=DEFAULT_COLOR_UNREACH;
    protected int mUnreachHeight=dp2px(DEFAULT_HEIGHT_UNREACH);
    protected int mReachColor=DEFAULT_COLOR_REACH;
    protected int mReachHeight=dp2px(DEFAULT_HEIGHT_REACH);
    protected int mTextOffset=dp2px(DEFAULT_TEXT_OFFSET);
    protected Paint mPaint=new Paint();
    protected int mRealWidth;
    public HorizontalProgressBarWithProgress(Context context) {
        this(context,null);
    }
    public HorizontalProgressBarWithProgress(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }
    public HorizontalProgressBarWithProgress(Context context, AttributeSet attrs,int defStyle) {
        super(context,attrs,defStyle);
        obtainStyledAttrs(attrs);
    }

    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray ta=getContext().obtainStyledAttributes(attrs,
                R.styleable.HorizontalProgressBarWithProgress);
    mTextSize=(int)ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_text_size,mTextSize);
        mUnreachColor=(int)ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_unreach_color,mUnreachColor);
        mTextColor=(int)ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_text_color,mTextColor);
        mUnreachHeight=(int)ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_unreach_height,mUnreachHeight);
        mReachColor=(int)ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_reach_color,mReachColor);
        mReachHeight=(int)ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_reach_height,mReachHeight);
        mTextOffset=(int)ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_text_offset,mTextOffset);

        ta.recycle();
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode= MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int height=measureHeight(heightMeasureSpec);
        setMeasuredDimension(widthSize,height);
        mRealWidth=getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(),getHeight()/2);
        boolean noNeedUnReach=false;

        float radio=getProgress()*1.0f/getMax();
        float progressX=radio*mRealWidth;
        float endX=progressX-mTextOffset/2;
        String text=getProgress()+"%";
        int textWidth=(int)mPaint.measureText(text);
if(progressX+textWidth>mRealWidth){
    progressX=mRealWidth-textWidth;
    noNeedUnReach=true;
}
//draw reach
        if(endX>0){
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0,0,endX,0,mPaint);
        }
        //draw text
        mPaint.setColor(mTextColor);
        int y=(int)((mPaint.descent()+mPaint.ascent())/2);
        canvas.drawText(text,progressX,y,mPaint);
        //draw unreach
if(!noNeedUnReach){
    float start=progressX+mTextOffset/2+textWidth;
    mPaint.setColor(mUnreachColor);
    mPaint.setStrokeWidth(mUnreachHeight);
    canvas.drawLine(start,0,mRealWidth,0,mPaint);
}
        canvas.restore();
    }

    private
    int measureHeight(int heightMeasureSpec) {
        int result=0;
        int mode=MeasureSpec.getMode(heightMeasureSpec);
        int size=MeasureSpec.getSize(heightMeasureSpec);
        if(mode==MeasureSpec.EXACTLY){
result=size;
        }
        else{
            int textHeight=(int)(mPaint.descent()-mPaint.ascent());
            result=getPaddingBottom()+getPaddingTop()+
                    Math.max(Math.max(mReachHeight,mUnreachHeight),Math.abs(textHeight));
            if(mode==MeasureSpec.AT_MOST){
                result=Math.min(result,size);
            }
        }
        return result;
    }

    protected   int dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,
                getResources().getDisplayMetrics());
    }
    protected   int sp2px(int spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spVal,
                getResources().getDisplayMetrics());
    }
}
