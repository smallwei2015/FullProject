package com.blue.rchina.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by cj on 2017/12/4.
 */

public class DeleteTextView extends TextView {

    public DeleteTextView(Context context) {
        super(context);
    }

    public DeleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint=new Paint();
        paint.setAlpha(100);
        paint.setColor(Color.RED);
        canvas.drawLine(0,getHeight()/2,getWidth(),getHeight()/2,paint);

    }
}
