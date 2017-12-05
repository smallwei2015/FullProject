package com.blue.rchina.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.Alliance;
import com.blue.rchina.bean.Channel;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.fragment.NewsKindFragment;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class TopNewsDetailActivity extends BaseActivity {


    @ViewInject(R.id.top_news_tab)
    TabLayout tab;
    @ViewInject(R.id.top_news_pager)
    ViewPager pager;
    public List<Channel> datas;
    public Alliance data;
    public FragmentStatePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_news_detail);

        x.view().inject(this);
        initData();
        initView();
    }

    @Override
    public void initData() {
        super.initData();

        data = ((Alliance) getIntent().getSerializableExtra("data"));

        datas = new ArrayList<>();

        List<Channel> sons = data.getSons();
        datas.addAll(sons);

        //getAllianceStrcture();
    }

    private void getAllianceStrcture() {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveFranchiseeStructure);
        if (data != null) {
            entity.addBodyParameter("dataId",data.getFranchiseeId());
        }

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult()==200){
                    isNodata(false);

                    List<Channel> channels = JSON.parseArray(netData.getInfo(), Channel.class);

                    datas.clear();
                    datas.addAll(channels);

                    /*根据条目个数设置tab的模式*/
                    if (datas.size()<=4&&datas.size()>=3){
                        tab.setTabMode(TabLayout.MODE_FIXED);
                    }else {
                        tab.setTabMode(TabLayout.MODE_SCROLLABLE);
                    }

                    adapter.notifyDataSetChanged();



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

            }
        });
    }

    @Override
    public void initView() {
        super.initView();

        if (data != null) {
            initTop(R.mipmap.left_white,data.getTitle(),-1);
        }else {
            initTop(R.mipmap.left_white,"商户详情",-1);
        }


        adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                NewsKindFragment fragment=new NewsKindFragment();
                Bundle args = new Bundle();
                args.putSerializable("data",datas.get(position));
                /*flag用于控制详情页标题显示*/
                args.putInt("flag",1);
                fragment.setArguments(args);

                /*TopNewsKindFragment fragment = new TopNewsKindFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("flag",position);
                fragment.setArguments(bundle);*/
                return fragment;
            }

            @Override
            public int getCount() {
                if (datas != null) {
                    return datas.size();
                }
                return 0;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return datas.get(position).getTitle();
            }


        };
        pager.setAdapter(adapter);

        tab.setupWithViewPager(pager);
    }
}
