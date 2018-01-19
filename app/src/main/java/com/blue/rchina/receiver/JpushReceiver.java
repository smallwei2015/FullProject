package com.blue.rchina.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blue.rchina.Main2Activity;
import com.blue.rchina.R;
import com.blue.rchina.activity.InfoActivity;
import com.blue.rchina.activity.OrderActivity;
import com.blue.rchina.bean.NoticeInfo;

import java.util.Date;

import cn.jpush.android.api.JPushInterface;

public class JpushReceiver extends BroadcastReceiver {

    private String TAG = "vode";

    public JpushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            notifyUser(context, intent);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "收到了通知");
            //notifyUser(context, intent);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            String mes = bundle.getString(JPushInterface.EXTRA_MESSAGE);

            Intent i=new Intent();
            i.putExtra("data", bundle);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (!TextUtils.isEmpty(mes)) {
                JSONObject object = JSON.parseObject(mes, JSONObject.class);
                Integer type = object.getInteger("type");
                switch (type){
                    /*1系统通知（用户）
                        2新闻推送（用户）
                        5付款通知（商户）
                        6发货通知（用户）*/
                    case 1:
                        i=new Intent(context,InfoActivity.class);
                        context.startActivity(i);
                        break;
                    case 2:
                        i = new Intent(context, Main2Activity.class);  //自定义打开的界面
                        context.startActivity(i);
                        break;
                    case 6:
                        i = new Intent(context, OrderActivity.class);  //自定义打开的界面
                        context.startActivity(i);
                        break;
                    default:
                        i = new Intent(context, Main2Activity.class);  //自定义打开的界面
                        context.startActivity(i);
                        break;
                }
            }else {
                // 在这里可以自己写代码去定义用户点击后的行为
                i = new Intent(context, Main2Activity.class);  //自定义打开的界面
                context.startActivity(i);
            }
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }

    }

    private void notifyUser(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String mes = bundle.getString(JPushInterface.EXTRA_MESSAGE);

        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性

        Intent mainIntent=new Intent(context, InfoActivity.class);
        NoticeInfo value = new NoticeInfo();

        value.setReadState(1);
        value.setType(1);
        value.setContent("融城中国");

        String format = new java.text.SimpleDateFormat("yyyy-mm-dd HH-MM-ss").format(new Date());
        value.setDatetime(format);
        mainIntent.putExtra("data", value);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                //设置小图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo))
                .setSmallIcon(R.mipmap.logo)
                //设置通知标题
                .setContentTitle(context.getResources().getString(R.string.app_name))
                //设置通知内容
                .setContentText(mes)
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent);
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notifyManager.notify(1, builder.build());


    }

}
