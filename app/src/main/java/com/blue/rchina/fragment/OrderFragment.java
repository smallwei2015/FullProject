package com.blue.rchina.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.adapter.OrderAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.mall.Order;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_order)
public class OrderFragment extends BaseFragment {


    @ViewInject(R.id.order_ptr)
    PtrClassicFrameLayout ptrFrame;
    @ViewInject(R.id.order_rec)
    RecyclerView rec;

    private List<Order> datas;
    private OrderAdapter adapter;
    private boolean isLoading;

    private int cPage=2;
    public int orderType;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = x.view().inject(this, inflater, container);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getContext());
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);

        header.setLastUpdateTimeKey(this.getClass().getName());

        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                fresh();
            }
        });

        datas=new ArrayList<>();

        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new OrderAdapter(datas, (BaseActivity) mActivity);

        rec.setAdapter(adapter);

        rec.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)){
                    if (!isLoading){
                        loadMore();
                    }else {
                        UIUtils.showToast("加载中...");
                    }
                }
            }
        });
    }

    private void fresh(){
        getData(0,1);
    }
    private void loadMore() {
        getData(1,cPage);
    }

    private void getData(final int type, int page){

        isLoading=true;

        RequestParams entity = new RequestParams(UrlUtils.N_getOrderInfoByAppuser);

        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId()+"");
        entity.addBodyParameter("flag",orderType+"");
        entity.addBodyParameter("page",page+"");

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult()==200){

                    List<Order> orders = JSON.parseArray(netData.getInfo(), Order.class);

                    if (orders!=null) {
                        if (type == 0) {
                            datas.clear();
                            cPage=2;
                            if (orders.size()>0){
                                isNodata(false);
                            }else {
                                isNodata(true);
                            }
                        } else {
                            if(orders.size()>0) {
                                cPage++;
                            }
                        }

                        datas.addAll(orders);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                isNodata(true);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                ptrFrame.refreshComplete();
                isLoading=false;

                isHideLoading(true);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        orderType = getArguments().getInt("type");
        fresh();
    }
}
