package com.blue.rchina.manager;


import com.blue.rchina.R;

import org.xutils.image.ImageOptions;

/**
 * Created by cj on 2017/6/21.
 */

public class xUtilsManager {

    public static ImageOptions getCircleImageOption(){
        ImageOptions options=new ImageOptions.Builder()
                //设置加载过程中的图片
                .setLoadingDrawableId(R.drawable.circle_gray)
                //设置加载失败后的图片
                .setFailureDrawableId(R.drawable.circle_gray)
                //设置使用缓存
                .setUseMemCache(true)
                //设置显示圆形图片
                .setCircular(true)
                //设置支持gif
                .setIgnoreGif(true)
                .setCrop(true)
                .build();

        return options;
    }

    public static ImageOptions getRectangleImageOption(){
        ImageOptions options=new ImageOptions.Builder()
                //设置加载过程中的图片
                .setLoadingDrawableId(R.color.lightGray)
                //设置加载失败后的图片
                .setFailureDrawableId(R.color.lightGray)
                //设置使用缓存
                .setUseMemCache(true)
                //设置显示圆形图片
                .setCircular(false)
                //设置支持gif
                .setIgnoreGif(false)
                .setCrop(true)
                .build();

        return options;
    }
}
