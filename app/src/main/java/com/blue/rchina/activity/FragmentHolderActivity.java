package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.Channel;
import com.blue.rchina.fragment.NewsFragment;
import com.blue.rchina.fragment.NewsWebFragment;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class FragmentHolderActivity extends BaseActivity {

    public static final int FLAG_LIFE=1;
    public static final int FLAG_LIFE_OUTLINK=2;
    public static final int FLAG_NEARBY=3;

    @ViewInject(R.id.fragment_container)
    FrameLayout container;
    public Object data;
    public int flag;

    public FragmentTransaction fragmentTransaction;
    public BaseFragment fragment;
    public Bundle bundle;
    public String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_holder);
        x.view().inject(this);

        initData();
        initView();
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        data = intent.getSerializableExtra("data");
        flag = intent.getIntExtra("flag",-1);
        title = intent.getStringExtra("title");

    }

    @Override
    public void initView() {
        super.initView();

        fragment = null;

        if (data != null) {

            switch (flag) {
                /*首页模式展示*/
                case FLAG_LIFE:
                    Channel lifeData = (Channel) this.data;
                    initTop(R.mipmap.left_white, lifeData.getTitle(), -1);
                    fragment = new NewsFragment();
                    bundle = new Bundle();
                    bundle.putSerializable("channel", lifeData);
                    /*广告新闻显示*/
                    bundle.putInt("flag",1);
                    fragment.setArguments(bundle);
                    break;
                /*网页链接展示*/
                case FLAG_LIFE_OUTLINK:
                    Channel lifeLinkData = (Channel) this.data;
                    initTop(R.mipmap.left_white, lifeLinkData.getTitle(), -1);
                    fragment = new NewsWebFragment();
                    bundle = new Bundle();
                    bundle.putSerializable("data", lifeLinkData);
                    fragment.setArguments(bundle);
                    break;
                case FLAG_NEARBY:
                    Channel nearbyData = (Channel) this.data;
                    initTop(R.mipmap.left_white, nearbyData.getTitle(), R.mipmap.exchange);
                    fragment = new NewsFragment();
                    bundle = new Bundle();
                    bundle.putSerializable("channel", nearbyData);
                    /*社区新闻显示*/
                    bundle.putInt("flag",2);
                    fragment.setArguments(bundle);
                    break;
                default:

                    break;
            }
        }else {
            if (TextUtils.isEmpty(title)) {
                initTop(R.mipmap.left_white, "", -1);
            }else {
                initTop(R.mipmap.left_white,title,-1);
            }
        }

        if (fragment != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }else {
            isNodata(true);
        }

    }


    @Override
    public void onRightIconClick(View view) {
        super.onRightIconClick(view);
        switch (flag){
            case FLAG_NEARBY:
                Intent intent=new Intent(mActivity,NearbySelectListActivity.class);
                startActivityForResult(intent,100);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 100:
                finish();
                break;
        }
    }
}
