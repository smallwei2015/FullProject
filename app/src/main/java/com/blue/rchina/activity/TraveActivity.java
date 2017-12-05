package com.blue.rchina.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.AreaInfo;
import com.blue.rchina.fragment.TraveFragment;

public class TraveActivity extends BaseActivity {

    public TraveFragment fragment;
    public AreaInfo data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trave);

        initData();
        initView();
    }

    @Override
    public void initData() {
        super.initData();
        data = ((AreaInfo) getIntent().getSerializableExtra("data"));
    }

    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white,data.getAreaName(),-1);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = new TraveFragment();
        transaction.replace(R.id.fra, fragment);
    }
}
