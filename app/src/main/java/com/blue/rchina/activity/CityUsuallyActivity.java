package com.blue.rchina.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.fragment.UnionFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.blue.rchina.R.id.tab_all;


@ContentView(R.layout.activity_city_usually)
public class CityUsuallyActivity extends BaseActivity {
    @ViewInject(tab_all)
    TextView all;
    @ViewInject(R.id.tab_local)
    TextView local;
    private FragmentManager manager;
    public UnionFragment cityFragment;
    public UnionFragment unionFragment;

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
        manager=getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();
        /*1常用城市2常用联盟*/

        cityFragment = new UnionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("flag",1);
        cityFragment.setArguments(bundle);
        transaction.add(R.id.usually_contain, cityFragment);
        unionFragment = new UnionFragment();

        bundle=new Bundle();
        bundle.putInt("flag",2);
        unionFragment.setArguments(bundle);
        transaction.add(R.id.usually_contain, unionFragment);

        transaction.show(cityFragment);
        transaction.hide(unionFragment);

        transaction.commit();

        local.setSelected(true);

    }

    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white,"常用",-1);
    }

    @Event(type = View.OnClickListener.class,value = {R.id.tab_local, R.id.tab_all})
    private void tabClick(View v){
        v.setSelected(true);
        FragmentTransaction transaction = manager.beginTransaction();
        switch (v.getId()){
            case R.id.tab_local:
                all.setSelected(false);
                transaction.show(cityFragment);
                transaction.hide(unionFragment);

                break;
            case tab_all:

                local.setSelected(false);

                transaction.hide(cityFragment);
                transaction.show(unionFragment);
                break;
        }

        transaction.commit();
    }

}
