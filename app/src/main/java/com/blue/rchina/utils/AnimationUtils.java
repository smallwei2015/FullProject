package com.blue.rchina.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by cj on 2017/7/21.
 */

public class AnimationUtils {

    public static TranslateAnimation moveToViewBottom() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    public static TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    public static ScaleAnimation scaleToSelfSize(){
        ScaleAnimation show=new ScaleAnimation( 0,1,0,1,Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF,0.5f);

        show.setDuration(400);
        return show;
    }

    public static AlphaAnimation alphaAnimation(){
        AlphaAnimation show=new AlphaAnimation(0f,1.0f);
        show.setDuration(400);
        return show;
    }
}
