package com.blue.rchina.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

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

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.blue.rchina.manager.UserManager.action_cityChange;

public class CitySelectListActivity extends BaseActivity implements View.OnClickListener {

    public RecyclerView recycler;
    public List<DataWrap> datas;
    public CitySelectAdapter adapter;
    private int flag = 1;
    private BroadcastReceiver cityReceiver;
    public Bundle bundle;
    private View topPadding;
    private AreaInfo parentArea;

    public AreaInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select_list);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        topPadding = findViewById(R.id.top_padding);
        int statusBarHeight = UIUtils.getStatusBarHeight(mActivity);
        if (statusBarHeight > 0) {
            topPadding.getLayoutParams().height = statusBarHeight;
        }
        initView();
        initData();

    }

    @Override
    public void initData() {
        super.initData();

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
                datas.add(e);

                List<AreaInfo> sons = info.getSons();
                if (sons != null) {
                    for (int i = 0; i < sons.size(); i++) {
                        DataWrap e1 = new DataWrap();
                        e1.setType(1);
                        e1.setData(sons.get(i));
                        datas.add(e1);
                    }
                }
                adapter.notifyDataSetChanged();

            } else {
                isNodata(true);
            }
        }

        registerCityReceiver();
    }

    private void loadLocalData() {
        /*加载本地数据*/
        String achieveAreaStructure = SPUtils.getCacheSp().getString("achieveAreaStructure", "");
        NetData data = JSON.parseObject(achieveAreaStructure, NetData.class);

        if (data != null) {
            List<AreaInfo> areas = JSON.parseArray(data.getInfo(), AreaInfo.class);
            for (int i = 0; i < areas.size(); i++) {
                AreaInfo areaInfo = areas.get(i);
                DataWrap e = new DataWrap();
                e.setType(1);
                e.setData(areaInfo);
                datas.add(e);
            }
            adapter.notifyDataSetChanged();
        }

        if (datas.size() == 0) {
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

                    SPUtils.getCacheSp().edit().putString("achieveAreaStructure", result).apply();
                    List<AreaInfo> areas = JSON.parseArray(data.getInfo(), AreaInfo.class);

                    datas.clear();

                    for (int i = 0; i < areas.size(); i++) {
                        AreaInfo areaInfo = areas.get(i);
                        DataWrap e = new DataWrap();
                        e.setType(1);
                        e.setData(areaInfo);
                        datas.add(e);
                    }


                    adapter.notifyDataSetChanged();

                } else {
                    UIUtils.showToast(data.getMessage() == null ? "" : data.getMessage());
                    isNodata(true);
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
                if (datas.size() > 0) {
                    isNodata(false);
                } else {
                    isNodata(true);
                }
            }
        });
    }

    private void registerCityReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(action_cityChange);

        cityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                AreaInfo area = (AreaInfo) intent.getSerializableExtra("area");

                mActivity.setResult(200);
                finish();
            }
        };
        registerReceiver(cityReceiver, filter);
    }

    @Override
    public void initView() {
        super.initView();

        bundle = getIntent().getBundleExtra("data");
        if (bundle != null) {
            flag = bundle.getInt("flag", 1);
        }

        boolean firstOpen = SPUtils.getSP().getBoolean("firstOpen", true);


        if (firstOpen) {
            initTop(-1, "选择城市", -1, R.color.bg_color, R.color.middle_gray);
        } else if (flag == 1) {
            initTop(R.mipmap.left_gray, "城市切换", "常用", R.color.bg_color, R.color.middle_gray);
        } else {
            initTop(R.mipmap.left_gray, "城市切换", "联盟APP", R.color.bg_color, R.color.middle_gray);
        }

        datas = new ArrayList<>();
        recycler = ((RecyclerView) findViewById(R.id.city_rec));

        recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        /*1表示切换城市，0表示选择城市*/
        adapter = new CitySelectAdapter(datas, 1);
        recycler.setAdapter(adapter);
        adapter.setListener(this);


    }

    @Override
    public void onRightTextClick(View v) {
        super.onRightTextClick(v);
        if (flag > 1) {
            /*跳转到联盟APP*/
            Intent intent=new Intent(mActivity,UnionActivity.class);
            intent.putExtra("city",info);
            startActivity(intent);
        } else {
            /*省级没有联盟，打开常用*/
            Intent intent = new Intent(mActivity, CityUsuallyActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cityReceiver != null) {
            unregisterReceiver(cityReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        boolean firstOpen = SPUtils.getSP().getBoolean("firstOpen", true);

        if (firstOpen && flag == 1) {

        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.parent) {
            /*切换到该城市*/
            Uninon tag = (Uninon) v.getTag();

            if (!TextUtils.isEmpty(tag.getUnionName())) {
                //tag.setAreaName(tag.getUnionName());
                tag.setIsOperate(1);
                tag.setAreaIcon(tag.getUnionIcon());
            }
            if (tag.getIsOperate() == 1) {

                Intent intent = new Intent(action_cityChange);
                intent.putExtra("area", tag);
                mActivity.sendBroadcast(intent);

                mActivity.finish();
            } else {
                UIUtils.showToast("该城市还未开通，无法选择");
            }
        } else {
            DataWrap dataWrap = datas.get((int) v.getTag());
            final AreaInfo data = (AreaInfo) dataWrap.getData();

            switch (dataWrap.getType()) {
                case 0:

                    if (data.getIsOperate() == 1) {

                        if (parentArea != null) {
                                        /*存储父节点name，用于天气查询*/
                            data.setProvinceCapital(parentArea.getProvinceCapital());
                        }
                        Intent intent = new Intent(action_cityChange);
                        intent.putExtra("area", data);
                        sendBroadcast(intent);

                        setResult(200);
                        finish();
                    } else {
                        UIUtils.showToast("该城市还未开通，无法选择");
                    }
                    break;
                case 1:
                    Intent intent = new Intent(mActivity, CitySelectListActivity.class);
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
                    startActivity(intent);
                    break;
            }
        }
    }
}
