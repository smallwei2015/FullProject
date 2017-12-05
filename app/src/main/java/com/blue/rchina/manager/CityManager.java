package com.blue.rchina.manager;

import android.content.Intent;

import com.blue.rchina.base.BaseApplication;
import com.blue.rchina.bean.City;


/**
 * Created by cj on 2017/9/1.
 */

public class CityManager {

    public static final String action_city_change="com.blue.city_change";

    public static City cCity;

    public static City getcCity() {
        return cCity;
    }

    public static void setcCity(City newCity) {
        if (newCity != null) {
            cCity = newCity;

            if (cCity != null) {
                /*如果当前城市不为空，那么就是切换城市*/
                sendChange();
            }
        }

    }


    private static void sendChange(){
        Intent intent = new Intent();
        intent.setAction(action_city_change);
        BaseApplication.getInstance().sendBroadcast(intent);
    }
}
