package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.Goods;
import com.blue.rchina.bean.Report;
import com.blue.rchina.utils.xUtilsImageUtils;
import com.blue.rchina.views.PinchImageView;
import com.blue.rchina.views.PinchImageViewPager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewsKindImageActivity extends BaseActivity {

    public static final String TRA_NAME = "pic_tran";
    @ViewInject(R.id.news_image_container)
    FrameLayout container;
    private PinchImageView pinchImageView;
    private PinchImageViewPager pager;
    private List<ImageView> mImages;
    private Report report;
    private Serializable data;

    @Override
    public void initView() {

        super.initView();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                |View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        pager=new PinchImageViewPager(this);
        mImages=new ArrayList<>();

        Intent intent = getIntent();
        data = intent.getSerializableExtra("data");
        int position = intent.getIntExtra("position", -1);

        List<String> manyPic =initPics(data);

        for (int i = 0; i < manyPic.size(); i++) {
            ImageView imageView = new PinchImageView(this);
            xUtilsImageUtils.display(imageView,manyPic.get(i));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            mImages.add(i, imageView);
        }
        pager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(final ViewGroup container,
                                          final int position) {
                container.addView(mImages.get(position));
                return mImages.get(position);
            }

            @Override
            public int getCount() {
                return mImages.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImages.get(position));
            }
        });
        if (position!=-1) {
            pager.setCurrentItem(position);
        }
        container.addView(pager);
    }

    private List<String> initPics(Serializable data) {

        if (data != null) {
            if (data instanceof Report){
                return ((Report) data).getManyPic();
            }else if (data instanceof Goods){
                List<String> temp=new ArrayList<>();
                temp.add(((Goods) data).getPicsrc());
                return (temp);
            }
        }

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_kind_image);
        x.view().inject(this);
        initView();
        initData();
    }
}
