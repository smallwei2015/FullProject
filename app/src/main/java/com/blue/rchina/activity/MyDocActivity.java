package com.blue.rchina.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blue.rchina.R;
import com.blue.rchina.adapter.Filedapter;
import com.blue.rchina.adapter.RecPaddingDecoration;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MyDocActivity extends BaseActivity {

    private PtrClassicFrameLayout ptr;
    private RecyclerView rec;

    private boolean isLoading=false;
    int cPager=1;
    private ArrayList<File> datas;
    private Filedapter adapter;
    public File download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doc);

        initData();
        initView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"我的文档","");

        ptr=((PtrClassicFrameLayout) findViewById(R.id.nearby_ptr));
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mActivity);
        ptr.setHeaderView(header);
        ptr.setLastUpdateTimeKey(getClass().getName());
        ptr.addPtrUIHandler(header);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                fresh();
            }
        });

        rec = ((RecyclerView) findViewById(R.id.rec));
        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        rec.addItemDecoration(new RecPaddingDecoration());

        rec.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoading) {
                        loadData(cPager+1);
                    }
                }
            }
        });
        datas=new ArrayList<>();
        adapter = new Filedapter(mActivity,datas);

        rec.setAdapter(adapter);

        ptr.autoRefresh();

    }

    private void fresh() {
        download = new File(FileUtils.DOWNLOAD);
        if (!download.exists()){
            download.mkdirs();
        }

        File[] files = download.listFiles();

        datas.clear();
        datas.addAll(Arrays.asList(files));
        adapter.notifyDataSetChanged();

        ptr.refreshComplete();
    }

    private void loadData(int i) {

    }
}
