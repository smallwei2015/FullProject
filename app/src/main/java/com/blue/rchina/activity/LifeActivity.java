package com.blue.rchina.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blue.rchina.R;
import com.blue.rchina.adapter.LifeAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.Channel;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class LifeActivity extends BaseActivity {

    @ViewInject(R.id.life_rec)
    RecyclerView rec;
    private LifeAdapter adapter;
    private List<Channel> items;
    public Channel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life);

        x.view().inject(this);
        initData();
        initView();
    }

    @Override
    public void initData() {
        super.initData();

        items=new ArrayList<>();

        channel = ((Channel) getIntent().getSerializableExtra("channel"));

        if (channel != null) {
            items.addAll(channel.getSons());
        }

    }

    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white,channel.getTitle(),-1);
        adapter = new LifeAdapter(items);
        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        rec.setAdapter(adapter);
    }
}
