package com.blue.rchina.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blue.rchina.R;
import com.blue.rchina.adapter.TraveAdapter;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.Trave;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_trave)
public class TraveFragment extends BaseFragment {

    @ViewInject(R.id.rec)
    RecyclerView rec;
    @ViewInject(R.id.ptr)
    PtrClassicFrameLayout ptr;
    private List<Trave> datas;
    public TraveAdapter adapter;

    public TraveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inject = x.view().inject(this, inflater, container);
        initData();
        initView(inject);
        return inject;
    }


    @Override
    public void initData() {
        super.initData();
        datas=new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            datas.add(new Trave());
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mActivity);
        ptr.setHeaderView(header);
        ptr.addPtrUIHandler(header);
        header.setLastUpdateTimeKey(getClass().getName());
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.refreshComplete();
            }
        });

        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new TraveAdapter(datas);
        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rec.setAdapter(adapter);

    }
}
