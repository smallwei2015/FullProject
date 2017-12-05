package com.blue.rchina.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.fragment.ChargeRecordFragment;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class ChargeRecordActivity extends BaseActivity {

    @ViewInject(R.id.tab)
    TabLayout tab;
    @ViewInject(R.id.pager)
    ViewPager pager;
    public List<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_record);

        x.view().inject(this);

        initData();
        initView();
    }

    @Override
    public void initData() {
        super.initData();

        titles = new ArrayList<>();
        titles.add("未完成");
        titles.add("已完成");


    }



    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"充值记录",-1);


        pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                ChargeRecordFragment chargeRecordFragment = new ChargeRecordFragment();
                Bundle args = new Bundle();
                args.putInt("flag",position);
                chargeRecordFragment.setArguments(args);

                return chargeRecordFragment;
            }

            @Override
            public int getCount() {
                if (titles != null) {
                    return titles.size();
                }
                return 0;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });

        tab.setupWithViewPager(pager);

    }
}
