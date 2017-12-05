package com.blue.rchina.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blue.rchina.R;
import com.blue.rchina.adapter.LifeAdapter;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.Channel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LifeFragment extends BaseFragment {


    private RecyclerView recycler;
    private LinearLayoutManager manager;
    private List<Channel> items;
    private Context context;
    private LifeAdapter adapter;
    public Channel channel;


    public LifeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_life, container, false);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        recycler = ((RecyclerView) view.findViewById(R.id.recycler));
        manager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(manager);
        items=new ArrayList<>();

        adapter=new LifeAdapter(items);
        recycler.setAdapter(adapter);
    }

    @Override
    public void initData() {
        super.initData();

        Bundle arguments = getArguments();
        if (arguments != null) {
            channel = ((Channel) arguments.getSerializable("channel"));
        }

        if (channel!=null) {
            items.addAll(channel.getSons());
        }

        if (items.size()==0){
            isNodata(true);
        }
        adapter.notifyDataSetChanged();
    }

}
