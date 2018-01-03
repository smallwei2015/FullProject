package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.adapter.CitySelectAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.AreaInfo;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.Uninon;
import com.blue.rchina.utils.SPUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.zhy.view.flowlayout.TagAdapter;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ContentView(R.layout.activity_city_select)
public class CitySelectActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.city_rec)
    RecyclerView recycler;

    public LayoutInflater mInflater;
    public TagAdapter<Uninon> adapter;
    public List<Uninon> tags;
    public List<DataWrap> dataWraps;

    private CitySelectAdapter cAdapter;
    private int flag = 1;
    private AreaInfo parentArea;
    /*1表示选择城市，2表示选择联盟*/
    public int kind = 1;
    public AreaInfo info;

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
        Bundle bundle = getIntent().getBundleExtra("data");
        /*1表示选择城市，2表示选择联盟*/
        kind=getIntent().getIntExtra("kind",1);
        if (bundle != null) {
            /*1表示选择城市省那一级别*/
            flag = bundle.getInt("flag", 1);
        }
        tags = new ArrayList<>();
        dataWraps = new ArrayList<>();

        if (flag == 1) {

            loadLocalData();
            loadCity();
        } else {
            if (bundle != null) {

                parentArea = ((AreaInfo) bundle.getSerializable("parent"));
                info = (AreaInfo) bundle.getSerializable("info");

                DataWrap e = new DataWrap();
                e.setType(0);
                e.setData(info);
                dataWraps.add(e);

                List<AreaInfo> sons = info.getSons();
                if (sons != null) {
                    for (int i = 0; i < sons.size(); i++) {
                        DataWrap e1 = new DataWrap();
                        e1.setType(1);
                        e1.setData(sons.get(i));
                        dataWraps.add(e1);
                    }
                }

            } else {
                isNodata(true);
            }
        }
    }
    private void loadLocalData() {
        /*加载本地数据*/
        String achieveAreaStructure = SPUtils.getCacheSp().getString("achieveAreaStructure", "");
        NetData data = JSON.parseObject(achieveAreaStructure, NetData.class);

        if (data!=null) {
            List<AreaInfo> areas = JSON.parseArray(data.getInfo(), AreaInfo.class);
            for (int i = 0; i < areas.size(); i++) {
                AreaInfo areaInfo = areas.get(i);
                DataWrap e = new DataWrap();
                e.setType(1);
                e.setData(areaInfo);
                dataWraps.add(e);
            }
        }

        if (dataWraps.size()==0){
            isHideLoading(false);
        }
    }
    private void loadCity() {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveAreaStructure);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData data = JSON.parseObject(result, NetData.class);
                int dataResult = data.getResult();

                if (dataResult == 200) {
                    List<AreaInfo> areas = JSON.parseArray(data.getInfo(), AreaInfo.class);

                    dataWraps.clear();

                    for (int i = 0; i < areas.size(); i++) {
                        AreaInfo areaInfo = areas.get(i);
                        DataWrap e = new DataWrap();
                        e.setType(1);
                        e.setData(areaInfo);
                        dataWraps.add(e);
                    }


                    cAdapter.notifyDataSetChanged();

                } else {
                    UIUtils.showToast(data.getMessage() == null ? "" : data.getMessage());
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
                if (dataWraps.size() > 0) {
                    isNodata(false);
                } else {
                    isNodata(true);
                }
            }
        });
    }

    private void loadUnion(AreaInfo info) {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveUnionList);
        entity.addBodyParameter("areaId", info.getAreaId().trim());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult() == 200) {
                    List<Uninon> uninons = JSON.parseArray(netData.getInfo(), Uninon.class);
                    tags.addAll(uninons);
                    adapter.notifyDataChanged();
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
                if (kind == 0) {
                    if (tags.size() == 0) {
                        isNodata(true);
                    } else {
                        isNodata(false);
                    }
                }
            }
        });
    }

    @Override
    public void initView() {
        super.initView();
        if (flag==1){
            initTop(R.mipmap.left_white, "选择城市", -1);
        }else {
            initTop(R.mipmap.left_white, "选择城市", "联盟APP");
        }
        mInflater = getLayoutInflater();

        tags = new ArrayList<>();

        recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        /*1表示切换城市，0表示选择城市*/
        cAdapter = new CitySelectAdapter(dataWraps, 0);
        cAdapter.setListener(this);
        recycler.setAdapter(cAdapter);
    }


    @Override
    public void onRightTextClick(View v) {
        super.onRightTextClick(v);

        Intent intent=new Intent(mActivity,UnionActivity.class);
        intent.putExtra("city",info);
        intent.putExtra("flag",1);
        intent.putExtra("kind",kind);
        startActivityForResult(intent,300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 300 && resultCode == 200) {
            setResult(200, data);
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        int pos = (int) v.getTag();
        DataWrap dataWrap = dataWraps.get(pos);
        final AreaInfo data = (AreaInfo) dataWrap.getData();

        switch (dataWrap.getType()) {
            case 0:
                if (data.getIsOperate() == 1) {

                    if (parentArea != null) {
                        /*存储父节点name，用于天气查询*/
                        data.setProvinceCapital(parentArea.getProvinceCapital());
                    }

                    /*添加城市只能选择城市*/
                    if (kind==1) {
                        Intent data1 = new Intent();
                        data1.putExtra("city", data);
                        setResult(200, data1);
                        finish();
                    }else {
                        UIUtils.showToast("请选择联盟APP");
                    }
                } else {
                    UIUtils.showToast("该城市还未开通，无法选择");
                }
                break;
            case 1:

                Intent intent = new Intent(mActivity, CitySelectActivity.class);
                intent.putExtra("kind",kind);
                Bundle value = new Bundle();
                value.putInt("flag", 2);
                value.putSerializable("info", data);
                String reg = "[县市区]*$";
                Pattern compile = Pattern.compile(reg);
                Matcher matcher = compile.matcher(data.getAreaName());

                if (matcher.find()) {
                    if (parentArea != null) {
                        data.setProvinceCapital(parentArea.getAreaName());
                    }
                    value.putSerializable("parent", data);
                } else {
                    value.putSerializable("parent", parentArea);
                }
                intent.putExtra("data", value);
                startActivityForResult(intent, 300);
                break;
        }
    }
}
