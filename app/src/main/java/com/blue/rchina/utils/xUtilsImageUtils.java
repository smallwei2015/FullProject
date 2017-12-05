package com.blue.rchina.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.blue.rchina.R;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by Chen on 2016/4/5.
 */
public class xUtilsImageUtils {

    /**
     * 显示图片（默认情况）
     *
     * @param imageView 图像控件
     * @param iconUrl   图片地址
     */
    public static void display(final ImageView imageView, String iconUrl) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                .setFailureDrawableId(R.color.bg_color)
                .setLoadingDrawableId(R.color.bg_color)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();
        x.image().bind(imageView, iconUrl, imageOptions);
    }

    public static void displayRadio(final ImageView imageView, String iconUrl) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                .setFailureDrawableId(R.color.bg_color)
                .setLoadingDrawableId(R.color.bg_color)
                /*xutils设置圆角会受到原图大小的影响*/
                //.setRadius(10)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();
        x.image().bind(imageView, iconUrl, imageOptions);
    }

    public static void displayCity(final ImageView imageView, String iconUrl) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                .setFailureDrawableId(R.mipmap.mall)
                .setLoadingDrawableId(R.mipmap.mall)
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                .build();
        x.image().bind(imageView, iconUrl, imageOptions);
    }

    public static void display(ImageView imageView, String iconUrl, Callback.CommonCallback<Drawable> callback) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                .setFailureDrawableId(R.color.bg_color)
                .setLoadingDrawableId(R.color.bg_color)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();
        x.image().bind(imageView, iconUrl, imageOptions,callback);
    }

    /**
     * 显示圆角图片
     *
     * @param imageView 图像控件
     * @param iconUrl   图片地址
     * @param radius    圆角半径，
     */
    public static void display(ImageView imageView, String iconUrl, int radius) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setRadius(DensityUtil.dip2px(radius))
                .setIgnoreGif(false)
                .setCrop(true)//是否对图片进行裁剪
                .setFailureDrawableId(R.color.bg_color)
                .setLoadingDrawableId(R.color.bg_color)
                .build();
        x.image().bind(imageView, iconUrl, imageOptions);
    }

    /**
     * 显示圆形头像，第三个参数为true
     *
     * @param imageView  图像控件
     * @param iconUrl    图片地址
     * @param isCircluar 是否显示圆形
     */
    public static void display(final ImageView imageView, String iconUrl, boolean isCircluar) {
        final ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setCircular(isCircluar)
                .setCrop(true)
                .setLoadingDrawableId(R.drawable.circle_50)
                .setFailureDrawableId(R.drawable.circle_50)
                .build();
        x.image().bind(imageView, iconUrl, imageOptions);
    }
}