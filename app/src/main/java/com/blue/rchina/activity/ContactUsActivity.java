package com.blue.rchina.activity;

import android.os.Bundle;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;

import org.xutils.x;

public class ContactUsActivity extends BaseActivity {

    @Override
    public void initView() {
        initTop(R.mipmap.left_white,"联系我们",-1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        x.view().inject(this);
        initView();
        initData();
    }
}
