package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.AreaInfo;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.blue.rchina.manager.UserManager.action_cityChange;

public class CitySelectActivity extends BaseActivity {

    @ViewInject(R.id.city_flow)
    TagFlowLayout flow;
    public LayoutInflater mInflater;
    public TagAdapter<AreaInfo> adapter;
    public List<AreaInfo> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);

        x.view().inject(this);
        initData();
        initView();
    }

    @Override
    public void initData() {
        super.initData();
        loadCity();
    }

    private void loadCity(){
        RequestParams entity = new RequestParams(UrlUtils.N_achieveAreaStructure);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData data= JSON.parseObject(result,NetData.class);
                int dataResult = data.getResult();

                if (dataResult==200){
                    List<AreaInfo> areas = JSON.parseArray(data.getInfo(), AreaInfo.class);
                    if (tags != null) {
                        tags.clear();
                        tags.addAll(areas);
                    }

                    adapter.notifyDataChanged();

                }else {
                    UIUtils.showToast(data.getMessage()==null?"":data.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isHideLoading(true);
            }
        });
    }
    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white,"选择城市",-1);
        mInflater = getLayoutInflater();


        flow=((TagFlowLayout) findViewById(R.id.city_flow));
        tags = new ArrayList<>();

        adapter = new TagAdapter<AreaInfo>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, AreaInfo areaInfo) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_text,
                        flow, false);
                tv.setText(areaInfo.getAreaName());
                return tv;
            }

        };
        flow.setAdapter(adapter);
        flow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                AreaInfo areaInfo = tags.get(position);

                Intent intent = new Intent(action_cityChange);
                intent.putExtra("area",areaInfo);
                sendBroadcast(intent);

                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 100);

                return true;
            }
        });

        isHideLoading(false);
    }
}
