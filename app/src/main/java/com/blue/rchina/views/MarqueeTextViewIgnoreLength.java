package com.blue.rchina.views;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextViewIgnoreLength extends TextView {

    /**
     * 是否停止滚动
     */
    private boolean mStopMarquee;
    private String mText;
    private float mCoordinateX;
    private float mTextWidth;

    private  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (Math.abs(mCoordinateX) > (mTextWidth + 100)) {
                        mCoordinateX = 0;
                        invalidate();
                        if (!mStopMarquee) {
                            sendEmptyMessageDelayed(0, 2000);
                        }
                    } else {
                        mCoordinateX -= 1;
                        invalidate();
                        if (!mStopMarquee) {
                            sendEmptyMessageDelayed(0, 30);
                        }
                    }

                    break;
            }
            super.handleMessage(msg);
        }
    };
    public boolean isAuto;


    public MarqueeTextViewIgnoreLength(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        /*TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.marque);
        mText = ta.getString(R.styleable.marque_android_text);
        isAuto = ta.getBoolean(R.styleable.marque_isAutoScroll, true);


        ta.recycle();*/

    }

    public MarqueeTextViewIgnoreLength(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextViewIgnoreLength(Context context) {
        super(context);
    }

    public void setText(String text) {
        this.mText = text;
        mTextWidth = getPaint().measureText(mText);
        if (mHandler.hasMessages(0))
            mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    protected void onAttachedToWindow() {
        mStopMarquee = false;
        if (!TextUtils.isEmpty(mText))
            mHandler.sendEmptyMessageDelayed(0, 2000);

        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        mStopMarquee = true;
        if (mHandler.hasMessages(0))
            mHandler.removeMessages(0);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(mText))
            canvas.drawText(mText, mCoordinateX, this.getTextSize(), getPaint());
    }



}