package com.blue.rchina.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * A simple {@link Fragment} subclass.
 */

@ContentView(R.layout.fragment_news_nearby)
public class NewsNearbyFragment extends BaseFragment {


    public NewsNearbyFragment() {
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
    }


    @Override
    public void initView(View view) {
        super.initView(view);
    }
}
