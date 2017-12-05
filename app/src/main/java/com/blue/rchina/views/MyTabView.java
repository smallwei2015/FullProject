package com.blue.rchina.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blue.rchina.R;

import java.util.List;

/**
 * Created by cj on 2017/11/7.
 */

public class MyTabView extends LinearLayout implements View.OnClickListener {

    private int layoutId;
    public ViewPager pager;

    public MyTabView(Context context) {
        super(context);
    }

    public MyTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyTabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setLayoutId(int layoutId){
        this.layoutId=layoutId;
    }

    private void addTab(String title, int iconSrc,int position){
        this.setOrientation(LinearLayout.HORIZONTAL);

        View inflate = LayoutInflater.from(getContext()).inflate(layoutId, null);

        inflate.setTag(position);

        TextView text = (TextView) inflate.findViewById(R.id.title);
        text.setText(title);
        ImageView icon = (ImageView) inflate.findViewById(R.id.icon);
        icon.setImageResource(iconSrc);

        inflate.setOnClickListener(this);

        this.addView(inflate);

        LayoutParams layoutParams = (LayoutParams) inflate.getLayoutParams();
        layoutParams.weight=1;
        layoutParams.gravity= Gravity.CENTER;
    }


    public void setUpWithViewPager(ViewPager pager, List<Integer> iconSrcs) throws Exception {

        this.pager = pager;

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        PagerAdapter adapter = pager.getAdapter();


        if (adapter==null){
            throw new Exception("必须要先为viewpager设置adapter");
        }
        int count = adapter.getCount();

        removeAllViews();

        for (int i = 0; i < count; i++) {
            String pageTitle = (String) adapter.getPageTitle(i);
            addTab(pageTitle,iconSrcs.get(i),i);
        }

        selectTab(0);
    }


    @Override
    public void onClick(View v) {

        int tag = (int) v.getTag();
        selectTab(tag);
        pager.setCurrentItem(tag);

    }

    private void selectTab(int position) {

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            TextView text = (TextView) child.findViewById(R.id.title);
            ImageView icon = (ImageView) child.findViewById(R.id.icon);
            if (i==position) {
                text.setSelected(true);
                icon.setSelected(true);
            }else {
                text.setSelected(false);
                icon.setSelected(false);
            }
        }

    }
}
