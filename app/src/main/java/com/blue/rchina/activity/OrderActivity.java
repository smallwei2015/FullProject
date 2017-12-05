package com.blue.rchina.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.fragment.OrderFragment;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends BaseActivity {


    @ViewInject(R.id.order_tab)
    TabLayout tab;
    @ViewInject(R.id.order_pager)
    ViewPager pager;
    private FragmentStatePagerAdapter adapter;

    List<BaseFragment> fragments;
    List<String> titles;


    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"我的订单",-1);

        fragments=new ArrayList<>();
        titles=new ArrayList<>();

        adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                if (fragments != null) {
                    return fragments.size();
                }
                return 0;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        };
        pager.setAdapter(adapter);

        tab.setupWithViewPager(pager);

    }

    @Override
    public void initData() {
        super.initData();

        /*0待付款1待发货
2待收货3全部*/

        OrderFragment e = new OrderFragment();
        Bundle args = new Bundle();

        titles.add("全部");
        args.putInt("type",3);
        e.setArguments(args);
        fragments.add(e);

        titles.add("待付款");
        e=new OrderFragment();
        args=new Bundle();
        args.putInt("type",0);
        e.setArguments(args);
        fragments.add(e);

        titles.add("待发货");
        e=new OrderFragment();
        args=new Bundle();
        args.putInt("type",1);
        e.setArguments(args);
        fragments.add(e);

        titles.add("待收货");
        e=new OrderFragment();
        args=new Bundle();
        args.putInt("type",2);
        e.setArguments(args);
        fragments.add(e);

        titles.add("已完成");
        e=new OrderFragment();
        args=new Bundle();
        args.putInt("type",4);
        e.setArguments(args);
        fragments.add(e);

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        x.view().inject(this);
        initView();
        initData();
    }
}
