package com.blue.rchina.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.blue.rchina.R;
import com.blue.rchina.activity.StartLivingActivity;
import com.blue.rchina.adapter.LivingListAdapter;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.living.LivingInfo;
import com.blue.rchina.manager.LocationInteface;
import com.blue.rchina.manager.LocationManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
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
@ContentView(R.layout.fragment_live_list)
public class LiveListFragment extends BaseFragment {

    public RecyclerView rec;
    public LivingListAdapter adapter;
    List<LivingInfo> datas;
    public PtrClassicFrameLayout ptr;
    private boolean isLoading=false;
    int cPager=1;
    AMapLocation clocation;
    public FloatingActionButton start;


    public LiveListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= x.view().inject(this,inflater, container);

        initView(view);
        initData();
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        LocationManager.initLocation(mActivity, new LocationInteface() {

            @Override
            public void locationSuccess(AMapLocation location) {
                clocation = location;
                fresh();
            }

            @Override
            public void locationFaild() {
                UIUtils.showToast("定位失败");
                fresh();
            }
        });
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        start = ((FloatingActionButton) view.findViewById(R.id.start));
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, StartLivingActivity.class);
                startActivity(intent);
            }
        });
        ptr=((PtrClassicFrameLayout) view.findViewById(R.id.nearby_ptr));
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

        rec = ((RecyclerView) view.findViewById(R.id.rec));
        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        //rec.addItemDecoration(new DividerItemDecoration(mActivity, LinearLayout.VERTICAL));

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
        adapter = new LivingListAdapter(mActivity,datas);

        rec.setAdapter(adapter);

    }
    private void loadData(int i) {
        isLoading=true;

        RequestParams entity = new RequestParams(UrlUtils.N_achieveLivingVideoList);
        entity.addBodyParameter("page",""+i);
        if (clocation != null) {
            entity.addBodyParameter("lon",clocation.getLongitude()+"");
            entity.addBodyParameter("lat",clocation.getLatitude()+"");
        }else {
            entity.addBodyParameter("lon","100");
            entity.addBodyParameter("lat","100");
        }
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {



                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult()==200){
                    List<LivingInfo> infos = JSON.parseArray(netData.getInfo(), LivingInfo.class);

                    if (infos != null) {

                        if (infos.size()>0){
                            cPager++;

                            datas.addAll(infos);
                            adapter.notifyDataSetChanged();
                        }else {
                            UIUtils.showToast("没有更多了");
                        }

                    }else {
                        UIUtils.showToast("暂无直播");
                    }


                }else {
                    UIUtils.showToast("获取直播列表失败");
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络请求失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isLoading=false;
            }
        });
    }

    private void fresh() {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveLivingVideoList);
        entity.addBodyParameter("page","1");

        if (clocation != null) {
            entity.addBodyParameter("lon",clocation.getLongitude()+"");
            entity.addBodyParameter("lat",clocation.getLatitude()+"");
        }else {
            entity.addBodyParameter("lon","100");
            entity.addBodyParameter("lat","100");
        }

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.w("66666",result);
                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult()==200){
                    cPager=1;
                    List<LivingInfo> infos = JSON.parseArray(netData.getInfo(), LivingInfo.class);

                    if (infos != null) {

                        datas.clear();
                        datas.addAll(infos);
                        adapter.notifyDataSetChanged();

                    }else {
                        UIUtils.showToast("暂无直播");
                    }


                }else {
                    UIUtils.showToast("获取直播列表失败");
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络请求失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                ptr.refreshComplete();

                if (datas.size()>0){
                    isNodata(false);
                }else {
                    isNodata(true);
                }
            }
        });
    }
}
