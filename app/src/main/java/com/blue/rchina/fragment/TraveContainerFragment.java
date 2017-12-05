package com.blue.rchina.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.blue.rchina.R.id.tab_all;

/**
 * A simple {@link Fragment} subclass.
 */

@ContentView(R.layout.fragment_trave_container)
public class TraveContainerFragment extends BaseFragment {


    @ViewInject(tab_all)
    TextView all;
    @ViewInject(R.id.tab_local)
    TextView local;

    @ViewInject(R.id.mall_contain)
    FrameLayout contain;
    public TraveFragment localFragment;
    public MallAllFragment allFragment;
    public FragmentManager manager;

    public TraveContainerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);

        initData();
        initView(view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        localFragment = new TraveFragment();
        transaction.add(R.id.mall_contain, localFragment);
        allFragment = new MallAllFragment();
        Bundle args = new Bundle();
        /*1表示旅游栏目*/
        args.putInt("flag",1);
        allFragment.setArguments(args);
        transaction.add(R.id.mall_contain, allFragment);

        transaction.hide(allFragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        local.setSelected(true);
    }

    @Event(type = View.OnClickListener.class,value = {R.id.tab_local, tab_all})
    private void tabClick(View v){
        v.setSelected(true);
        FragmentTransaction transaction = manager.beginTransaction();
        switch (v.getId()){
            case R.id.tab_local:
                all.setSelected(false);
                transaction.show(localFragment);
                transaction.hide(allFragment);

                break;
            case R.id.tab_all:

                local.setSelected(false);

                transaction.hide(localFragment);
                transaction.show(allFragment);
                break;
        }

        transaction.commit();
    }
}
