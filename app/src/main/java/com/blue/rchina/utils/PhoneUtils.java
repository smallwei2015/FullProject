package com.blue.rchina.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by cj on 2018/6/8.
 */

public class PhoneUtils {

    public static void callPhone(Context context,String number){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number));
        context.startActivity(intent);
    }
}
