package com.blue.rchina.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;

import org.xutils.x;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //透明导航栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        x.view().inject(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
    }
}
