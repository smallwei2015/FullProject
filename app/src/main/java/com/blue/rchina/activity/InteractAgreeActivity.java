package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blue.rchina.R;
import com.blue.rchina.adapter.InteractAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.Report;
import com.blue.rchina.bean.User;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class InteractAgreeActivity extends BaseActivity {

    @ViewInject(R.id.recycler)
    RecyclerView recycler;
    private List<DataWrap> items;
    private InteractAdapter adapter;
    private LinearLayoutManager manager;
    private Report data;

    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white,"点赞详情",-1);
        items = new ArrayList<>();
        adapter = new InteractAdapter(items);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);
    }

    @Override
    public void initData() {
        super.initData();

        Intent intent = getIntent();
        data = ((Report) intent.getSerializableExtra("data"));
        List<User> praiseList = data.getPraiseList();

        for (int i = 0; i < praiseList.size(); i++) {
            DataWrap e = new DataWrap();
            e.setType(3);
            e.setData(praiseList.get(i));
            items.add(e);
        }

        adapter.notifyDataSetChanged();

        if (praiseList==null||praiseList.size()==0){
            isNodata(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact_agree);
        x.view().inject(this);
        initView();
        initData();
    }
}
