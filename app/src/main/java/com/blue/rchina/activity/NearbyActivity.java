package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.Channel;
import com.blue.rchina.bean.Nearby;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.fragment.NewsFragment;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class NearbyActivity extends BaseActivity {

    List<Channel> datas;
    private NewsFragment fragment;
    private FragmentTransaction fragmentTransaction;


    @ViewInject(R.id.nearby_intro)
    View intro;
    @ViewInject(R.id.nearby_info)
    View info;
    @ViewInject(R.id.nearby_send)
    View send;
    @ViewInject(R.id.nearby_other)
    View other;
    public String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        x.view().inject(this);
        initData();
        initView();
    }

    @Override
    public void initData() {
        super.initData();
        datas = new ArrayList<>();

        name = getIntent().getStringExtra("name");
    }

    private void getNearbyStruct() {

        isHideLoading(false);
        RequestParams entity = new RequestParams(UrlUtils.N_achieveAppuserCommunity);
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult() == 200) {
                    List<Channel> channels = JSON.parseArray(netData.getInfo(), Channel.class);

                    datas.clear();
                    datas.addAll(channels);

                    /*if (channels != null && channels.size() > 0) {
                        fragment = new NewsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("channel", channels.get(0));
                        *//*社区新闻显示*//*
                        bundle.putInt("flag",2);
                        fragment.setArguments(bundle);

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fra, fragment);
                        fragmentTransaction.commit();
                    }*/
                } else {
                    Intent intent = new Intent(mActivity, NearbySelectListActivity.class);
                    startActivityForResult(intent, 200);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络连接失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isHideLoading(true);
                if (datas.size() > 0) {
                    isNodata(false);
                } else {
                    isNodata(true);
                }
            }
        });
    }

    @Override
    public void initView() {
        super.initView();
        if (!TextUtils.isEmpty(name)){
            initTop(R.mipmap.left_white, name, R.mipmap.exchange);
        }else {
            initTop(R.mipmap.left_white, "我的社区", R.mipmap.exchange);
        }
        //getNearbyStruct();
    }

    @Override
    public void onRightIconClick(View view) {
        super.onRightIconClick(view);

        Intent intent = new Intent(mActivity, NearbySelectListActivity.class);
        startActivityForResult(intent, 200);
    }

    @Event({R.id.nearby_send, R.id.nearby_intro, R.id.nearby_other, R.id.nearby_info,R.id.nearby_mall})
    private void click(View view) {

        Intent intent;
        switch (view.getId()) {
            case R.id.nearby_intro:
                intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("flag", 2);
                intent.putExtra("title", "社区介绍");

                startActivity(intent);
                break;
            case R.id.nearby_other:
                intent = new Intent(mActivity, NearbyDetailActivity.class);
                /*1社区介绍
                2社区通知
                3社区新闻*/
                intent.putExtra("flag",3);
                startActivity(intent);
                /*intent=new Intent(mActivity,NearbyServiceActivity.class);
                startActivity(intent);*/

                break;
            case R.id.nearby_info:
                intent = new Intent(mActivity, NearbyDetailActivity.class);
                intent.putExtra("flag",2);
                startActivity(intent);
                break;
            case R.id.nearby_send:
                intent = new Intent(mActivity, SendNearbyActivity.class);
                startActivity(intent);
                break;
            case R.id.nearby_mall:
                intent = new Intent(mActivity, NearbyMallActivity.class);

                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*重新选择社区*/
        if (requestCode==200&&resultCode==100){
            Nearby nearby = (Nearby) data.getSerializableExtra("data");

            if (nearby != null) {
                initTop(R.mipmap.left_white, nearby.getTitle(), R.mipmap.exchange);
            }
        }
    }
}
