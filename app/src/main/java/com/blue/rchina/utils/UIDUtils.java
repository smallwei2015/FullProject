package com.blue.rchina.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by cj on 2017/5/16.
 */

public class UIDUtils {
    private static Context mContext;

    public static String getUID(Context context){
        mContext=context;
        /*这个为什么会变*/
        String uid = "RC" +

                Build.BOARD.length()%10 +
                Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 +
                Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 +
                Build.HOST.length()%10 +
                Build.ID.length()%10 +
                Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 +
                Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 +
                Build.TYPE.length()%10 +
                Build.USER.length()%10 ; //13 digits

        String uidStr = "RC" + MD5Utils.getMD5(getIMEI()+getAndroidID()+getWLANMACAddress());
        return uidStr;
    }

    private static String getIMEI() {
        TelephonyManager TelephonyMgr = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        return szImei;
    }

    private static String getAndroidID() {
        String m_szAndroidID = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return m_szAndroidID;
    }

    private static String getWLANMACAddress() {
        WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        return m_szWLANMAC;
    }
}
