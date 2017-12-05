package com.blue.rchina.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.AreaInfo;
import com.blue.rchina.fragment.MallFragment;
import com.blue.rchina.manager.UserManager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class MallActivity extends BaseActivity {
    public MallFragment mallFragment;
    public AreaInfo data;
    private BroadcastReceiver receiver;

    @ViewInject(R.id.top_mall)
    View mall_top;
    @ViewInject(R.id.top_mall_count)
    TextView mall_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall);

        x.view().inject(this);
        initData();
        initView();
    }

    @Override
    public void initData() {
        super.initData();

        data = ((AreaInfo) getIntent().getSerializableExtra("data"));

        IntentFilter filterMall = new IntentFilter();
        filterMall.addAction("action_change_mall");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int count = intent.getIntExtra("count", 0);
                setMallCount(count);
            }
        };
        registerReceiver(receiver, filterMall);
    }

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,data.getAreaName(),-1);


        mallFragment = new MallFragment();
        Bundle args = new Bundle();
        args.putSerializable("data",data);
        mallFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,mallFragment).commit();

        mall_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.isLogin()) {
                    Intent intent = new Intent(mActivity, ShoppingCartActivity.class);
                    startActivity(intent);
                } else {
                    UserManager.toLogin();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    public void setMallCount(int count) {
        if (count > 0) {
            mall_count.setVisibility(View.VISIBLE);
            if (count > 99) {
                count = 99;
            }
            mall_count.setText(count + "");
        } else {
            mall_count.setVisibility(View.GONE);
        }
    }
}
