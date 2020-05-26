package com.blue.rchina.utils;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by cj on 2018/6/8.
 */

public class MetadataUtils {

    public static boolean isFromHuawei(Activity activity){

        try {
            ApplicationInfo info = activity.getPackageManager()
                    .getApplicationInfo(activity.getPackageName(),
                            PackageManager.GET_META_DATA);

            String msg =info.metaData.getString("channel");

            if (msg.equals("huawei")){
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
