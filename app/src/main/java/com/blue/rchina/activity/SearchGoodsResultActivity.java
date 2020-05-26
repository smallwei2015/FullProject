package com.blue.rchina.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.adapter.NearbyMallAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.Goods;
import com.blue.rchina.bean.MallGoods;
import com.blue.rchina.bean.Uninon;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.SPUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class SearchGoodsResultActivity extends BaseActivity {


    @ViewInject(R.id.rec)
    RecyclerView rec;

    public String name;
    public NearbyMallAdapter adapter;
    public ArrayList<DataWrap> datas;
    public int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_goods_result);
        x.view().inject(this);
        initData();
        initView();


    }


    @Override
    public void initData() {
        super.initData();
        name = getIntent().getStringExtra("name");
        flag = getIntent().getIntExtra("flag",1);
        loadData();
    }

    private void loadData() {

        String area = SPUtils.getSP().getString("area", "");
        Uninon areaInfo = JSON.parseObject(area, Uninon.class);

        RequestParams entity = new RequestParams(UrlUtils.N_achieveGoodsSearchData);
        entity.addBodyParameter("appuserId", UserManager.getAppuserId());
        entity.addBodyParameter("title",name);
        entity.addBodyParameter("areaId", areaInfo.getAreaId());
        entity.addBodyParameter("flag",flag+"");//1城市商品2社区超市
        //entity.addBodyParameter("franchiseeId","");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                MallGoods mallGoods = JSON.parseObject(result, MallGoods.class);

                /*设置购物车商品数量*/
                if (mallGoods.getResult().equals("200")) {

                    List<Goods> info = mallGoods.getInfo();

                    for (int i = 0; i < info.size(); i++) {
                        DataWrap e = new DataWrap();
                        e.setType(1);
                        e.setData(info.get(i));
                        datas.add(e);
                    }


                    adapter.notifyDataSetChanged();
                } else {
                    UIUtils.showToast(mallGoods.getMessage());
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
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"搜索结果",-1);

        GridLayoutManager layout = new GridLayoutManager(mActivity, 2);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

        rec.setLayoutManager(layout);

        datas = new ArrayList<>();
        adapter = new NearbyMallAdapter(datas,mActivity);
        rec.setAdapter(adapter);

    }
}
