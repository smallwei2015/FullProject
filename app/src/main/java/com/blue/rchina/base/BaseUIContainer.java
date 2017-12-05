package com.blue.rchina.base;

import android.view.View;

/**
 * Created by cj on 2017/3/6.
 */

public interface BaseUIContainer {

    public static final int TYPE_ACTIVITY=0;
    public static final int TYPE_FRAGMENT=1;

    abstract void initView();
    abstract void initView(View view);
    abstract void initData();
    abstract void UIfinish();

    abstract int getType();
    abstract String getUITag();
}
