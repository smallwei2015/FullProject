package com.blue.rchina.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.blue.rchina.R;

/**
 * Created by cj on 2017/11/27.
 */

public class ClipImageView extends ImageView {
    public ClipImageView(Context context) {
        super(context);
    }

    public ClipImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClipImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ClipImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画边框
        Rect rec=canvas.getClipBounds();
        rec.bottom--;
        rec.right--;
        rec.top++;
        rec.left++;
        Paint paint=new Paint();
        paint.setColor(getResources().getColor(R.color.bg_color));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rec, paint);
    }
}
