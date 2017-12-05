package com.blue.rchina.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.blue.rchina.base.BaseApplication;


/**
 * Created by cj on 2017/3/6.
 */

public class SPUtils {
    public static SharedPreferences mSp;
    public static SharedPreferences mSp_cache;
    private static String name = "config";
    private static String cache = "cache";

    public static void initSP(Context context) {
        mSp = context.getSharedPreferences(name, 0);
        mSp_cache = context.getSharedPreferences(cache, 0);
    }

    public static SharedPreferences getSP() {
        if (null == mSp)
            initSP(BaseApplication.getInstance());
        return mSp;
    }

    public static SharedPreferences getCacheSp() {
        if (null == mSp_cache)
            initSP(BaseApplication.getInstance());
        return mSp_cache;
    }

}

