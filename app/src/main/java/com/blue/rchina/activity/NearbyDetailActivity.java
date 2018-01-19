package com.blue.rchina.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blue.rchina.R;
import com.blue.rchina.adapter.NewsAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.Article;
import com.blue.rchina.bean.DataWrap;
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


@ContentView(R.layout.activity_nearby_detail)
public class NearbyDetailActivity extends BaseActivity {


    @ViewInject(R.id.ptr)
    PtrClassicFrameLayout ptr;
    @ViewInject(R.id.rec)
    RecyclerView rec;
    public NewsAdapter adapter;
    private List<DataWrap> items;

    private int cPager=2;
    public int flag;
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
        items=new ArrayList<>();

        flag = getIntent().getIntExtra("flag",3);
    }

    @Override
    public void initView() {
        super.initView();

        if (flag==3) {
            initTop(R.mipmap.left_white, "社区新闻", -1);
        }else {
            initTop(R.mipmap.left_white, "社区通知", -1);
        }

        PtrClassicDefaultHeader ptrUIHandler = new PtrClassicDefaultHeader(mActivity);
        ptr.addPtrUIHandler(ptrUIHandler);
        ptr.setHeaderView(ptrUIHandler);
        ptrUIHandler.setLastUpdateTimeKey(getClass().getName());
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadData(1);
            }
        });


        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new NewsAdapter(items);
        rec.setAdapter(adapter);


        rec.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!rec.canScrollVertically(-1)&&items.size()>0){
                    if (!isLoading){
                        loadData(cPager);
                    }else {
                        UIUtils.showToast("加载中...");
                    }
                }
            }
        });
        isHideLoading(false);
        loadData(1);
    }

    private void loadData(final int page) {

        isLoading=true;

        RequestParams entity = new RequestParams(UrlUtils.N_achieveCommunityChannelData);
        entity.addBodyParameter("appuserId", UserManager.getAppuserId());
        entity.addBodyParameter("page", page+"");
        entity.addBodyParameter("flag", flag+"");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData != null) {
                    if (netData.getResult()==200){
                        JSONObject object = JSON.parseObject(netData.getInfo());
                        List<Article> articles = JSON.parseArray(object.getString("list"), Article.class);
                        List<Article> hots = JSON.parseArray(object.getString("top"), Article.class);

                        if (page==1){
                            items.clear();
                            cPager=2;
                        }else {
                            if (articles.size()>0){
                                cPager++;
                            }
                        }
                        if (hots != null && hots.size() > 0) {
                            DataWrap e1 = new DataWrap();
                            e1.setType(0);
                            e1.setData(hots);
                            items.add(e1);
                        }
                        for (int i = 0; i < articles.size(); i++) {
                            DataWrap e = new DataWrap();
                            Article article = articles.get(i);
                            e.setData(article);
                            e.setType(article.getDisplayType());
                            items.add(e);
                        }


                        adapter.notifyDataSetChanged();
                    }
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

                isLoading=false;
                ptr.refreshComplete();
                isHideLoading(true);
                if (items.size()==0){
                    isNodata(true);
                }else {
                    isNodata(false);
                }
            }
        });
    }
}
