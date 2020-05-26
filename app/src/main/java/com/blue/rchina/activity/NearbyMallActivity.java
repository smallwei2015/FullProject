package com.blue.rchina.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.adapter.NearbyMallAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.Goods;
import com.blue.rchina.bean.GoodsKind;
import com.blue.rchina.bean.MallGoods;
import com.blue.rchina.bean.Uninon;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.SPUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.blue.rchina.utils.UrlUtils.N_achieveCommunityStore;

public class NearbyMallActivity extends BaseActivity {

    public RecyclerView recLeft;
    public List<DataWrap> leftData;
    public List<DataWrap> rightData;
    public NearbyMallAdapter rightAdapter;
    public NearbyMallAdapter leftAdapter;
    public RecyclerView recRight;

    /*当前选择分类*/
    public GoodsKind.InfoBean curType;
    private int curPage = 1;
    private boolean isLoading=false;
    public TextView goodsCount;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_mall);
        initTop(R.mipmap.left_white, "社区超市", -1);
        initView();
        initData();
    }


    @Override
    public void initView() {
        super.initView();


        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,MallSearchActivity.class);
                intent.putExtra("flag",2);
                startActivity(intent);
            }
        });
        findViewById(R.id.top_mall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ShoppingCartActivity.class);
                startActivity(intent);
            }
        });

        goodsCount = ((TextView) findViewById(R.id.top_mall_count));
        /*int mallCount = SPUtils.getSP().getInt("mallCount", 0);

        goodsCount.setText(mallCount+"");*/


        recLeft = ((RecyclerView) findViewById(R.id.rec_left));
        recLeft.setLayoutManager(new LinearLayoutManager(mActivity));

        recRight = ((RecyclerView) findViewById(R.id.rec_right));
        GridLayoutManager layout = new GridLayoutManager(mActivity, 3);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                /*if (position==0){
                    return 3;
                }*/
                return 1;
            }
        });
        recRight.setLayoutManager(layout);


        leftData = new ArrayList<>();
        rightData = new ArrayList<>();

        leftAdapter = new NearbyMallAdapter(leftData, this);
        leftAdapter.setListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                curPage = 1;
                for (int i = 0; i < leftData.size(); i++) {
                    DataWrap dataWrap = leftData.get(i);
                    if (i == ((int) v.getTag())) {
                        dataWrap.setState(1);
                        curType = (GoodsKind.InfoBean) dataWrap.getData();

                        loadGoods();
                    } else {
                        dataWrap.setState(0);
                    }
                }
                leftAdapter.notifyDataSetChanged();
            }

        });
        recLeft.setAdapter(leftAdapter);


        rightAdapter = new NearbyMallAdapter(rightData, this);
        rightAdapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        recRight.setAdapter(rightAdapter);
        recRight.setOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    if (rightData.size()>0) {
                        if (!isLoading) {
                            loadGoods();
                            UIUtils.showToast("加载更多");
                        }

                    }
                }
            }
        });
    }


    @Override
    public void initData() {
        super.initData();

        IntentFilter filterMall = new IntentFilter();
        filterMall.addAction("action_change_mall");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    int count = intent.getIntExtra("count", 0);

                    goodsCount.setText(count+"");
                }
            }
        };
        registerReceiver(receiver, filterMall);

        x.http().post(new RequestParams(UrlUtils.N_achieveGoodsCategory), new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                GoodsKind goodsKind = JSON.parseObject(result, GoodsKind.class);
                if (goodsKind.getResult().equals("200")) {

                    List<GoodsKind.InfoBean> info = goodsKind.getInfo();
                    for (int i = 0; i < info.size(); i++) {
                        DataWrap e = new DataWrap();
                        if (i == 0) {
                            e.setState(1);
                        }
                        e.setType(0);
                        e.setData(info.get(i));
                        leftData.add(e);
                    }
                    leftAdapter.notifyDataSetChanged();

                    if (leftData.size() > 0) {
                        curType = (GoodsKind.InfoBean) leftData.get(0).getData();
                        loadGoods();
                    }
                } else {
                    UIUtils.showToast(goodsKind.getMessage());
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

            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    private void loadGoods() {


        isLoading=true;

        String area = SPUtils.getSP().getString("area", "");
        Uninon areaInfo = JSON.parseObject(area, Uninon.class);


        RequestParams entity = new RequestParams(N_achieveCommunityStore);
        entity.addBodyParameter("appuserId", UserManager.getAppuserId());
        entity.addBodyParameter("page", curPage + "");
        ///entity.addBodyParameter("areaId", areaInfo.getAreaId()+"");
        entity.addBodyParameter("flag", curType.getCategoryId() + "");

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                MallGoods mallGoods = JSON.parseObject(result, MallGoods.class);

                /*设置购物车商品数量*/
                goodsCount.setText(mallGoods.getShopcarNum()+"");
                if (mallGoods.getResult().equals("200")) {
                    if (curPage == 1) {
                        rightData.clear();
                    }
                    /*DataWrap e1 = new DataWrap();
                    e1.setType(2);
                    rightData.add(e1);*/
                    List<Goods> info = mallGoods.getInfo();

                    /*加载到数据，更新页数*/
                    if (info.size() > 0) {
                        curPage++;
                    }
                    for (int i = 0; i < info.size(); i++) {
                        DataWrap e = new DataWrap();
                        e.setType(1);
                        e.setData(info.get(i));
                        rightData.add(e);
                    }


                    rightAdapter.notifyDataSetChanged();
                } else {
                    UIUtils.showToast(mallGoods.getMessage());
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络错误");
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


}
