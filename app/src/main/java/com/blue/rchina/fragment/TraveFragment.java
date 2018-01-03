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
import com.blue.rchina.adapter.TraveAdapter;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.AreaInfo;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.Trave;
import com.blue.rchina.utils.SPUtils;
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
    private AreaInfo area;
    private boolean isLocal;
    private int cPage=1;
    private boolean isLoading=false;

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


        Bundle arguments = getArguments();

        if (arguments != null) {
            area=((AreaInfo) arguments.getSerializable("data"));
            isLocal=false;
        }else {
            String areaStr = SPUtils.getSP().getString("area", "");
            area = JSON.parseObject(areaStr, AreaInfo.class);

            isLocal=true;
        }

    }

    private void getTraves(final int page) {
        isLoading=true;

        String n_getLocalScenicList = "";
        if (isLocal) {
            n_getLocalScenicList=UrlUtils.N_getLocalScenicList;
        }else {
            n_getLocalScenicList=UrlUtils.N_getAreaScenicList;
        }

        RequestParams entity = new RequestParams(n_getLocalScenicList);
        entity.addBodyParameter("areaId",area==null?"":area.getAreaId());
        entity.addBodyParameter("page",page+"");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                if (page==1){
                    datas.clear();
                    cPage=1;
                    NetData netData = JSON.parseObject(result, NetData.class);
                    if (netData.getResult()==200){
                        List<Trave> traves = JSON.parseArray(netData.getInfo(), Trave.class);
                        datas.addAll(traves);
                    }else {
                        UIUtils.showToast(netData.getMessage());
                    }

                    adapter.notifyDataSetChanged();
                }else {
                    /*加载更多*/
                    NetData netData = JSON.parseObject(result, NetData.class);
                    if (netData.getResult()==200){
                        cPage++;
                        List<Trave> traves = JSON.parseArray(netData.getInfo(), Trave.class);
                        datas.addAll(traves);
                    }else {
                        UIUtils.showToast(netData.getMessage());
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络连接失败");
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
                isLoading=false;
                isHideLoading(true);
            }
        });
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
                getTraves(1);
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
        rec.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!rec.canScrollVertically(1) && datas.size() > 0) {
                    if (!isLoading) {
                        getTraves(cPage+1);
                    } else {
                        UIUtils.showToast("加载中...");
                    }
                }
            }
        });

        isHideLoading(false);
        getTraves(1);

    }
}
