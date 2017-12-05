package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.Channel;
import com.blue.rchina.bean.Nearby;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.fragment.NewsKindFragment;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.SPUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.blue.rchina.views.MyTabView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class NearbyActivity extends BaseActivity {


    @ViewInject(R.id.nearby_tab)
    MyTabView tab;
    @ViewInject(R.id.nearby_pager)
    ViewPager pager;
    List<Integer> iconSrcs;
    LinkedHashMap<Integer, String> maps;

    List<Channel> datas;


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

        datas=new ArrayList<>();
        iconSrcs= new ArrayList<>();
        iconSrcs.add(R.drawable.message_icon);
        iconSrcs.add(R.drawable.bell_icon);
        iconSrcs.add(R.drawable.service_icon);
        maps = new LinkedHashMap<>();

        String nearby = SPUtils.getSP().getString("nearby", "");

        if (TextUtils.isEmpty(nearby)){
            UIUtils.showToast("当前未选择社区，请重新选择");

            Intent intent=new Intent(mActivity,NearbySelectListActivity.class);
            startActivity(intent);
        }else {
            Nearby nea = JSON.parseObject(nearby, Nearby.class);
            getNearbyStruct(nea);
        }


        maps.put(iconSrcs.get(0),"社区消息");
        maps.put(iconSrcs.get(1),"社区报修");
        maps.put(iconSrcs.get(2),"社区服务");
    }

    private void getNearbyStruct(Nearby nea) {
        isHideLoading(false);
        RequestParams entity = new RequestParams(UrlUtils.N_achieveAppuserCommunity);
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId()+"");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult()==200) {
                    List<Channel> channels = JSON.parseArray(netData.getInfo(), Channel.class);

                    datas.clear();
                    datas.addAll(channels);

                }else {
                    UIUtils.showToast(netData.getMessage());
                }


                if (datas.size()>0){
                    isNodata(false);
                }else {
                    isNodata(true);
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
            }
        });
    }

    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white,"我的社区",R.mipmap.exchange);
        pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new NewsKindFragment();
            }

            @Override
            public int getCount() {
                if (maps != null) {
                    return maps.size();
                }
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return maps.get(iconSrcs.get(position));
            }
        });

        try {
            tab.setLayoutId(R.layout.tab_item);
            tab.setUpWithViewPager(pager, iconSrcs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRightIconClick(View view) {
        super.onRightIconClick(view);

        Intent intent=new Intent(mActivity,NearbySelectListActivity.class);
        startActivity(intent);
    }
}
