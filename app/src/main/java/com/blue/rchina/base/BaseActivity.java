package com.blue.rchina.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.manager.AutoRefreshInteface;
import com.blue.rchina.manager.NetInterface;
import com.blue.rchina.manager.UserInterface;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.AnimationUtils;
import com.blue.rchina.utils.SPUtils;
import com.blue.rchina.utils.StatusBarUtils;
import com.blue.rchina.utils.UIUtils;

import static com.blue.rchina.manager.UserManager.action_change;
import static com.blue.rchina.manager.UserManager.action_in;
import static com.blue.rchina.manager.UserManager.action_out;


/**
 * Created by cj on 2017/3/6.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseUIContainer {


    public BaseActivity mActivity;
    private UserInterface userHandler;
    private NetInterface netHandler;
    private AutoRefreshInteface refreshHandler;
    public String action;

    public static final String action_autoRefresh = "com.rcchina.fresh";



    public static void sendAutoRefreshBroadcast(Context context){
        Intent intent=new Intent();
        intent.setAction(action_autoRefresh);
        context.sendBroadcast(intent);
    }
    public void setNetHandler(NetInterface netHandler) {
        this.netHandler = netHandler;
        if (netHandler != null) {
            registerNetReceiver();
        }
    }

    public void setUserHandler(UserInterface userHandler) {
        this.userHandler = userHandler;

        if (userHandler != null) {
            registerUserReceiver();
        }

    }


    public void setRefreshHandler(AutoRefreshInteface freshHandler){
        this.refreshHandler=freshHandler;
        if (refreshHandler!=null){
            registerAutoRefreshReceiver();
        }
    }
    private View.OnClickListener ToolbarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left:
                    onLeftClick(v);
                    break;
                case R.id.left_text:
                    onLeftTextClick(v);
                    break;
                case R.id.right_icon:
                    onRightIconClick(v);
                    break;
                case R.id.right_text:
                    onRightTextClick(v);
                    break;
                case R.id.left_icon:
                    onLeftIconClick(v);
                    break;
            }
        }
    };

    public void onRightTextClick(View v) {

    }

    public void onLeftTextClick(View v) {
    }

    public void onLeftIconClick(View view) {

    }

    public void onRightIconClick(View view) {

    }

    public void onLeftClick(View view) {
        UIfinish();
    }

    private BroadcastReceiver userReceiver, connectionReceiver,autoRefreshReceiver;
    public View nodata,serverDie;
    public ContentLoadingProgressBar loading;


    public void initView() {
        nodata = findViewById(R.id.no_data);
        if (nodata != null) {
            nodata.setVisibility(View.GONE);
        }

        serverDie=findViewById(R.id.server_die);
        if (serverDie != null) {
            serverDie.setVisibility(View.GONE);
        }
        loading = (ContentLoadingProgressBar) findViewById(R.id.loading);
        if (loading != null) {
            loading.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*if (SystemBrightUtils.isAutoBrightness(mActivity)) {
            SystemBrightUtils.stopAutoBrightness(mActivity);
        }
        int bright = SPUtils.getSP().getInt("bright", -1);
        Log.w("33333",bright+"bright");
        if (bright!=-1){
            UIUtils.setBrightness(bright,mActivity);
            Log.w("33333",bright+"set");
        }*/

    }

    public void initData() {
    }

    public void initView(View view) {

        nodata = view.findViewById(R.id.no_data);
        if (nodata != null) {
            nodata.setVisibility(View.GONE);
        }
    }

    private int type;
    private String tag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //BaseApplication.push(this);
        type = TYPE_ACTIVITY;
        tag = getClass().getName();
        mActivity = this;

        int sizeType = SPUtils.getSP().getInt("sizeType", 0);



        /*if (sizeType==0){
            setTheme(R.style.AppTheme_middle);
        }else if (sizeType==1){
            setTheme(R.style.AppTheme_small);
        }else if (sizeType==2){
            setTheme(R.style.AppTheme_large);
        }else {
            setTheme(R.style.AppTheme_middle);
        }*/

        SharedPreferences sp = SPUtils.getSP();
        boolean isNightMode = sp.getBoolean("isNightMode",false);

        if (isNightMode) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            float brightValue = 60;
            lp.screenBrightness = (brightValue <= 0 ? -1.0f : brightValue / 255f);
            getWindow().setAttributes(lp);
        }
    }

    public void registerNetReceiver() {
        // unconnect network
        // connect network
        connectionReceiver = new BroadcastReceiver() {
            public int netState = 0;

            @Override
            public void onReceive(Context context, Intent intent) {


                if (intent.getAction().equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                        // unconnect network
                        UIUtils.showToast("网络已经断开");
                        netState = -1;

                        netHandler.handleBreak();
                    } else {
                        // connect network
                        if (netState != 0) {

                        }
                        if (mobNetInfo.isConnected()) {
                            UIUtils.showToast("当前为移动网络，请注意资费");
                            netHandler.handle4G();
                            netState = 1;
                        } else if (wifiNetInfo.isConnected()) {
                            if (netState == 1) {
                                UIUtils.showToast("已经切换到wifi，尽情玩耍吧");
                            }

                            netHandler.handleWifi();
                            netState = 2;
                        }
                    }
                }

            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);
    }

    public void registerUserReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(action_in);
        filter.addAction(action_out);
        filter.addAction(action_change);
        userReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase(action_in)) {
                    if (UserManager.isLogin()) {
                        userHandler.loginIn();
                    }
                } else if (intent.getAction().equalsIgnoreCase(action_out)) {
                    userHandler.loginOut();
                } else if (intent.getAction().equalsIgnoreCase(action_change)) {
                    userHandler.userStateChange();
                }
            }
        };
        registerReceiver(userReceiver, filter);
    }

    public void registerAutoRefreshReceiver(){
        IntentFilter filter=new IntentFilter();
        filter.addAction(action_autoRefresh);

        autoRefreshReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase(action_autoRefresh)) {
                    refreshHandler.refresh();
                }
            }
        };

        registerReceiver(autoRefreshReceiver,filter);
    }


    public void initTop(int left, String content, int right) {
        initTop(left, content, right, -1);
    }

    public void initTop(int left, String content, String right) {
        initTop(left, content, right,-1,R.color.white);
    }

    public void initTop(int left, String content, int right, int bgColor) {
        initTop(left, content, right, bgColor, -1);
    }

    public void initTop(int left, String content, Object right, int bgColor, int textColor) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (bgColor != -1) {
                toolbar.setBackgroundColor(getResources().getColor(bgColor));
                StatusBarUtils.setWindowStatusBarColor(this, bgColor);
            } else {
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                StatusBarUtils.setWindowStatusBarColor(this, R.color.colorPrimary);
            }

            ImageView rightIcon = (ImageView) findViewById(R.id.right_icon);
            TextView right_text = (TextView) findViewById(R.id.right_text);
            ImageView leftI = (ImageView) findViewById(R.id.left);
            ImageView leftIcon = (ImageView) findViewById(R.id.left_icon);
            TextView center = ((TextView) findViewById(R.id.center_text));

            if (right instanceof Integer) {
                int rightInt = (int) right;
                rightIcon.setVisibility(View.VISIBLE);
                right_text.setVisibility(View.GONE);
                if (rightInt > 0) {
                    rightIcon.setImageResource(rightInt);
                } else {
                    rightIcon.setVisibility(View.GONE);
                }
                rightIcon.setOnClickListener(ToolbarListener);
            } else if (right instanceof String) {
                String rightStr = (String) right;

                right_text.setVisibility(View.VISIBLE);
                rightIcon.setVisibility(View.GONE);

                if (TextUtils.isEmpty(rightStr)) {
                    right_text.setVisibility(View.GONE);
                } else {
                    right_text.setText(rightStr);
                }
                right_text.setOnClickListener(ToolbarListener);

            }
            if (left <= 0) {
                leftI.setVisibility(View.GONE);
            } else {
                leftI.setImageResource(left);
                leftI.setOnClickListener(ToolbarListener);
            }


            if (!TextUtils.isEmpty(content)) {

                center.setVisibility(View.VISIBLE);
                if (textColor != -1) {
                    center.setTextColor(getResources().getColor(textColor));
                } else {
                    center.setTextColor(getResources().getColor(R.color.white));
                }
                center.setText(content);
            }else {
                center.setVisibility(View.GONE);
            }


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userReceiver != null) {
            unregisterReceiver(userReceiver);
        }
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }

        if (autoRefreshReceiver != null) {
            unregisterReceiver(autoRefreshReceiver);
        }
        //BaseApplication.pop(this);
    }

    @Override
    public void UIfinish() {
        /*处理页面的销毁*/
        finish();
    }


    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getUITag() {
        return tag;
    }


    public void isNodata(boolean no) {
        if (nodata != null) {
            if (no) {
                if (nodata.getVisibility() != View.VISIBLE) {
                    nodata.setVisibility(View.VISIBLE);
                    nodata.setAnimation(AnimationUtils.scaleToSelfSize());
                }
            } else {
                nodata.setVisibility(View.GONE);
            }
        }
    }

    public void isHideLoading(boolean show){
        if (loading != null) {
            if (show){
                if (loading.getVisibility()==View.VISIBLE) {
                    loading.setVisibility(View.GONE);
                }
            }else {
                loading.setVisibility(View.VISIBLE);
            }
        }
    }

    public void isServerDie(boolean no){
        if (serverDie!=null){
            if (no){
                if (serverDie.getVisibility() != View.VISIBLE) {
                    serverDie.setVisibility(View.VISIBLE);
                    serverDie.setAnimation(AnimationUtils.scaleToSelfSize());
                }
            }else {
                serverDie.setVisibility(View.GONE);
            }
        }
    }
}
