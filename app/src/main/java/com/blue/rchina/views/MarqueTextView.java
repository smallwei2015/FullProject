package com.blue.rchina.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by cj on 2017/9/1.
 */

public class MarqueTextView extends TextView{
    public MarqueTextView(Context context) {
        super(context);
    }

    public MarqueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MarqueTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean isFocused() {
        //就是把这里返回true即可
        return true;
    }

}
