package com.blue.rchina.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.adapter.NearbyRecordAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.NearbyRecord;
import com.blue.rchina.bean.NetData;
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

@ContentView(R.layout.activity_nearby_record)
public class NearbyRecordActivity extends BaseActivity {

    @ViewInject(R.id.nearby_ptr)
    PtrClassicFrameLayout ptr;
    @ViewInject(R.id.rec)
    RecyclerView rec;

    List<NearbyRecord> datas;
    private int curPager = 2;
    public NearbyRecordAdapter adapter;
    private boolean isLoading=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        initData();
        initView();
    }


    @Override
    public void initData() {
        super.initData();

        datas = new ArrayList<>();
    }

    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white, "社区报修记录", -1);

        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mActivity);
        ptr.setHeaderView(header);
        ptr.addPtrUIHandler(header);
        header.setLastUpdateTimeKey(getClass().getName());

        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadData(1);
            }
        });


        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new NearbyRecordAdapter(datas);
        rec.setAdapter(adapter);
        rec.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!rec.canScrollVertically(-1) && datas.size() > 0) {
                    if (!isLoading){
                        loadData(curPager);
                    }else {
                        UIUtils.showToast("加载中...");
                    }

                }
            }
        });
        loadData(1);
    }

    private void loadData(final int page) {

        isLoading=true;

        RequestParams entity = new RequestParams(UrlUtils.N_achieveCommunityReport);
        entity.addBodyParameter("appuserId", UserManager.getAppuserId());
        entity.addBodyParameter("page", page + "");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult() == 200) {
                    List<NearbyRecord> nearbyRecords = JSON.parseArray(netData.getInfo(), NearbyRecord.class);

                    if (page == 1) {
                        datas.clear();
                        curPager=2;
                    } else {
                        if (!nearbyRecords.isEmpty()) {
                            curPager++;
                        }
                    }
                    datas.addAll(nearbyRecords);
                    adapter.notifyDataSetChanged();

                } else {
                    UIUtils.showToast(netData.getMessage());
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("服务器错误");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isHideLoading(true);
                isLoading=false;
                ptr.refreshComplete();
                if (datas.size() == 0) {
                    isNodata(true);
                } else {
                    isNodata(false);
                }
            }
        });
    }
}
