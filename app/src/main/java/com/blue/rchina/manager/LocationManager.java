package com.blue.rchina.manager;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by cj on 2017/11/1.
 */

public class LocationManager {

    public static AMapLocation location;
    private static int locationCounter=0;

    public static void initLocation(Context context, final LocationInteface loc){
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        final AMapLocationClient mlocationClient = new AMapLocationClient(context);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {

                locationCounter++;

                Log.w("44444",locationCounter+":"+amapLocation.toString());
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {

                        location=amapLocation;

                        mlocationClient.stopLocation();
                        loc.locationSuccess(location);

                    }else {
                        if (locationCounter>3){
                            mlocationClient.stopLocation();
                            locationCounter=0;
                        }

                        loc.locationFaild();


                    }
                }else {
                    if (locationCounter>3){
                        mlocationClient.stopLocation();
                        locationCounter=0;
                    }
                    loc.locationFaild();


                }
            }
        });
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000*60);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期
        mlocationClient.startLocation();
    }

}
