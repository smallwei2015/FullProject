package com.blue.rchina.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.activity.CitySelectActivity;
import com.blue.rchina.adapter.UnionAdapter;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.AreaInfo;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.Uninon;
import com.blue.rchina.utils.UIDUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_union)
public class UnionFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.add)
    TextView add;
    @ViewInject(R.id.rec)
    RecyclerView rec;


    private List<DataWrap> wraps;
    public int flag;
    public LayoutInflater inflater;
    public int windowWidth;
    public UnionAdapter recAdapter;
    public GridLayoutManager manager;

    public UnionFragment() {

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
        windowWidth = UIUtils.getWindowWidth(mActivity);
        /*1常用城市2常用联盟*/
        flag = getArguments().getInt("flag", 1);
        wraps=new ArrayList<>();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        inflater = LayoutInflater.from(mActivity);

        add.setOnClickListener(this);

        manager = new GridLayoutManager(mActivity,3);
        manager.setOrientation(LinearLayout.VERTICAL);
        rec.setLayoutManager(manager);
        recAdapter = new UnionAdapter(wraps,flag);
        recAdapter.setListener(this);
        rec.setAdapter(recAdapter);
        loadUnion();
    }

    private void loadUnion() {
        isHideLoading(false);

        RequestParams entity = new RequestParams(UrlUtils.N_achieveCommonAreaAndUnion);
        entity.addBodyParameter("flag", flag + "");
        entity.addBodyParameter("dataId", UIDUtils.getUID(mActivity));
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult() == 200) {
                    List<Uninon> areaInfos = JSON.parseArray(netData.getInfo(), Uninon.class);

                    wraps.clear();

                    for (int i = 0; i < areaInfos.size(); i++) {
                        DataWrap e = new DataWrap();
                        e.setState(0);
                        e.setData(areaInfos.get(i));
                        wraps.add(e);
                    }
                    recAdapter.notifyDataSetChanged();
                } else {
                    UIUtils.showToast(netData.getMessage());
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
                isHideLoading(true);

                if (wraps.size() > 0) {
                    //add.setVisibility(View.VISIBLE);
                    isNodata(false);
                } else {
                    //add.setVisibility(View.GONE);
                    isNodata(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tag_del) {
            //View tag = (View) v.getTag();
            //flow.removeView(tag);
            Uninon uni = (Uninon) v.getTag();

            delete(uni);
        } else if (v.getId() == R.id.add) {

            Intent intent = new Intent(mActivity, CitySelectActivity.class);

            intent.putExtra("kind", flag);

            startActivityForResult(intent, 300);

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 300) {
            if (resultCode == 200) {
                int flag = data.getIntExtra("flag", 1);

                if (flag == 1) {
                    AreaInfo city = (AreaInfo) data.getSerializableExtra("city");
                    if (city != null) {
                        add(city,1);
                    }
                } else if (flag == 2) {
                    Uninon city = (Uninon) data.getSerializableExtra("city");
                    if (city != null) {
                        add(city, 2);
                    }

                }

            }
        }

    }

    private void add(final AreaInfo areaInfo, int flag) {
        isHideLoading(false);

        RequestParams entity = new RequestParams(UrlUtils.N_addCommon);
        entity.addBodyParameter("flag", flag + "");
        entity.addBodyParameter("dataId", UIDUtils.getUID(mActivity));
        entity.addBodyParameter("areaId", areaInfo.getAreaId());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult() == 200) {
                    UIUtils.showToast("添加成功");

                    loadUnion();
                    /*List<Uninon> areaInfos = JSON.parseArray(netData.getInfo(), Uninon.class);
                    datas.clear();
                    datas.addAll(areaInfos);
                    adapter.notifyDataChanged();*/

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
            }
        });

    }

    private void delete(final AreaInfo areaInfo) {
        RequestParams entity = new RequestParams(UrlUtils.N_delCommon);
        entity.addBodyParameter("dataId", UIDUtils.getUID(mActivity));
        entity.addBodyParameter("areaId", areaInfo.getAreaId());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult() == 200) {
                    UIUtils.showToast("删除成功");

                    loadUnion();
                    /*List<Uninon> areaInfos = JSON.parseArray(netData.getInfo(), Uninon.class);
                    datas.clear();
                    datas.addAll(areaInfos);
                    adapter.notifyDataChanged();*/

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

            }
        });
    }
}
