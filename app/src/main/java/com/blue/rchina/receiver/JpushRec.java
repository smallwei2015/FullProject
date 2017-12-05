package com.blue.rchina.receiver;

import android.content.Context;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * Created by cj on 2017/11/28.
 */

public class JpushRec extends JPushMessageReceiver {
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);


    }
}
