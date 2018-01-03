package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.adapter.UnionAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.AreaInfo;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.Uninon;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_union)
public class UnionActivity extends BaseActivity {

    @ViewInject(R.id.rec)
    RecyclerView rec;
    private UnionAdapter uAdapter;
    private List<DataWrap> uDatas;
    public AreaInfo city;
    /*0表示是从切换城市过来的，1表示选择添加城市*/
    public int flag;
    private int kind=0;

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

        city = ((AreaInfo) getIntent().getSerializableExtra("city"));
        flag = getIntent().getIntExtra("flag", 0);
        kind=getIntent().getIntExtra("kind",0);
        uDatas = new ArrayList<>();

    }

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white, "联盟APP", -1);


        GridLayoutManager manager = new GridLayoutManager(mActivity, 3);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rec.setLayoutManager(manager);
        if (flag == 0) {
            uAdapter = new UnionAdapter(uDatas, 0);
        } else {
            uAdapter = new UnionAdapter(uDatas, 3);
            uAdapter.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    Uninon data = (Uninon) uDatas.get(tag).getData();

                    /*联盟只能选择联盟*/
                    if (kind == 2) {
                        Intent intent = new Intent();
                        intent.putExtra("city", data);
                        intent.putExtra("flag", 2);
                        setResult(200, intent);

                        finish();
                    } else {
                        UIUtils.showToast("请选择城市");
                    }
                }
            });
        }
        rec.setAdapter(uAdapter);

        loadUnion(city);
    }


    private void loadUnion(AreaInfo info) {

        if (info == null) {
            isNodata(true);
            return;
        }

        isHideLoading(false);
        RequestParams entity = new RequestParams(UrlUtils.N_achieveUnionList);
        entity.addBodyParameter("areaId", info.getAreaId().trim());

        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult() == 200) {
                    List<Uninon> uninons = JSON.parseArray(netData.getInfo(), Uninon.class);
                    uDatas.clear();

                    for (int i = 0; i < uninons.size(); i++) {
                        DataWrap e = new DataWrap();
                        e.setState(0);
                        e.setData(uninons.get(i));
                        uDatas.add(e);
                    }
                    uAdapter.notifyDataSetChanged();
                } else {
                    UIUtils.showToast(netData.getMessage());
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

                isHideLoading(true);
                if (uDatas.size() > 0) {
                    isNodata(false);
                } else {
                    isNodata(true);
                }
            }
        });
    }
}
