package com.blue.rchina.activity;

import android.os.Bundle;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;

import org.xutils.x;

public class NearbyServiceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_service);

        x.view().inject(this);
        initData();
        initView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"社区服务",-1);
    }
}
