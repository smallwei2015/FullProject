package com.blue.rchina.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by cj on 2017/12/5.
 */

public class RadioImageView extends ImageView {
    public int width;
    public int height;

    public RadioImageView(Context context) {
        super(context);
    }

    public RadioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RadioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {


        if (width > 12 && height > 12) {
            Path path = new Path();
            path.moveTo(12, 0);
            path.lineTo(width - 12, 0);
            path.quadTo(width, 0, width, 12);
            path.lineTo(width, height - 12);
            path.quadTo(width, height, width - 12, height);
            path.lineTo(12, height);
            path.quadTo(0, height, 0, height - 12);
            path.lineTo(0, 12);
            path.quadTo(0, 0, 12, 0);
            canvas.clipPath(path);
        }

        super.onDraw(canvas);
    }
}
