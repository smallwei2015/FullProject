package com.blue.rchina.activity;

import android.os.Bundle;
import android.view.View;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.utils.UIUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

@ContentView(R.layout.activity_join_us)
public class JoinUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        initTop(R.mipmap.left_white,"开通APP",-1);
    }


    @Event(value ={R.id.join_city,R.id.join_alliance,R.id.join_top},type = View.OnClickListener.class)
    private void onClick(View view){
        UIUtils.showToast("该功能暂未开通");
    }
}
