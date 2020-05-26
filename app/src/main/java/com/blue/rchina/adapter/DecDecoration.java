package com.blue.rchina.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blue.rchina.utils.UIUtils;

/**
 * Created by Administrator on 2020/5/26.
 *
 * @copyright 北京融城互联
 */
public class DecDecoration extends RecyclerView.ItemDecoration {
    private int padding= UIUtils.dp2px(1);

    public DecDecoration() {
    }

    public DecDecoration(int padding) {
        this.padding = padding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0,padding,0,0);
    }
}
