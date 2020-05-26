package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.adapter.CitySelectAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.enter.City;
import com.blue.rchina.bean.enter.County;
import com.blue.rchina.bean.enter.Province;
import com.blue.rchina.bean.enter.Town;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_open_city)
public class OpenCityActivity extends BaseActivity {

    @ViewInject(R.id.rec)
    RecyclerView rec;
    public List<DataWrap> datas;
    public CitySelectAdapter adapter;
    public int flag;
    public Serializable data;

    Province cProvince;
    City cCity;
    County cCounty;
    Town cTown;

    /*是开通城市还是联盟0联盟1城市2置顶版位*/
    private int openFlag=0;

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

        /*flag 0表示省，以此类推*/

        openFlag=getIntent().getIntExtra("openFlag",0);
        flag = getIntent().getIntExtra("flag", 0);
        data = getIntent().getSerializableExtra("data");

        Serializable province = getIntent().getSerializableExtra("province");
        if (province != null) {
            cProvince= (Province) province;
        }

        Serializable city = getIntent().getSerializableExtra("city");
        if (city != null) {
            cCity= (City) city;
        }


        Serializable county = getIntent().getSerializableExtra("county");
        if (county != null) {
            cCounty= (County) county;
        }

        Serializable town = getIntent().getSerializableExtra("town");
        if (town != null) {
            cTown= (Town) town;
        }

        datas = new ArrayList<>();

    }

    private void loadData(final int flag) {
        isHideLoading(false);
        RequestParams entity = new RequestParams();

        if (flag == 0) {
            entity.setUri(UrlUtils.N_achieveProvinceList);
        } else if (flag==1){
            entity.setUri(UrlUtils.N_achieveCityList);
            entity.addBodyParameter("provinceId", ((Province) data).getProvinceId());
        }else if (flag==2){
            entity.setUri(UrlUtils.N_achieveCountyList);
            entity.addBodyParameter("cityId", ((City) data).getCityId());
        }else if (flag==3){
            entity.setUri(UrlUtils.N_achieveTownList);
            entity.addBodyParameter("countyId", ((County) data).getCountyId());
        }else {
            DataWrap e1 = new DataWrap();
            e1.setType(6);
            e1.setData(data);
            datas.add(e1);
            adapter.notifyDataSetChanged();
        }

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult() == 200) {

                    if (flag == 0) {
                        List<Province> provinces = JSON.parseArray(netData.getInfo(), Province.class);
                        for (int i = 0; i < provinces.size(); i++) {
                            DataWrap e = new DataWrap();
                            e.setData(provinces.get(i));
                            e.setType(2);
                            datas.add(e);
                        }
                    }else if (flag == 1) {
                        List<City> city = JSON.parseArray(netData.getInfo(), City.class);

                        DataWrap e1 = new DataWrap();
                        e1.setType(6);
                        e1.setData(data);
                        datas.add(e1);

                        for (int i = 0; i < city.size(); i++) {
                            DataWrap e = new DataWrap();
                            e.setData(city.get(i));
                            e.setType(3);
                            datas.add(e);
                        }
                    }else if (flag == 2) {
                        List<County> county = JSON.parseArray(netData.getInfo(), County.class);
                        DataWrap e1 = new DataWrap();
                        e1.setType(6);
                        e1.setData(data);
                        datas.add(e1);

                        for (int i = 0; i < county.size(); i++) {
                            DataWrap e = new DataWrap();
                            e.setData(county.get(i));
                            e.setType(4);
                            datas.add(e);
                        }
                    }else if (flag == 3) {
                        List<Town> town = JSON.parseArray(netData.getInfo(), Town.class);
                        DataWrap e1 = new DataWrap();
                        e1.setType(6);
                        e1.setData(data);
                        datas.add(e1);
                        for (int i = 0; i < town.size(); i++) {
                            DataWrap e = new DataWrap();
                            e.setData(town.get(i));
                            e.setType(5);
                            datas.add(e);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    UIUtils.showToast(netData.getMessage());
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

                if (datas.size()==0){
                    isNodata(true);
                }else {
                    isNodata(false);
                }
            }
        });

    }

    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white, "选择城市", -1);

        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new CitySelectAdapter(datas, 3);
        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();
                DataWrap dataWrap = datas.get(tag);

                if (flag == 0) {
                    Intent intent = new Intent(mActivity, OpenCityActivity.class);
                    intent.putExtra("openFlag",openFlag);
                    intent.putExtra("data", ((Serializable) dataWrap.getData()));
                    intent.putExtra("province",(Province)dataWrap.getData());
                    intent.putExtra("flag", flag + 1);
                    startActivity(intent);
                }else {
                    if (tag==0){

                        if (UserManager.isLogin()) {
                        /*第一条，可选中*/
                            Intent intent = new Intent(mActivity, OpenCityResultActivity.class);
                            intent.putExtra("openFlag",openFlag);
                            if (cProvince != null) {
                                intent.putExtra("province", cProvince);
                            }

                            if (cCity != null) {
                                intent.putExtra("city", cCity);
                            }

                            if (cCounty != null) {
                                intent.putExtra("county", cCounty);
                            }

                            if (cTown != null) {
                                intent.putExtra("town", cTown);
                            }
                            startActivity(intent);
                        }else {
                            UserManager.toLogin();
                        }
                    }else {
                        Intent intent = new Intent(mActivity, OpenCityActivity.class);
                        intent.putExtra("openFlag",openFlag);
                        intent.putExtra("data", (Serializable) dataWrap.getData());
                        if (cProvince != null) {
                            intent.putExtra("province",cProvince);
                        }

                        if (cCity!=null){
                            intent.putExtra("city",cCity);
                        }

                        if (cCounty != null) {
                            intent.putExtra("county",cCounty);
                        }

                        if (cTown != null) {
                            intent.putExtra("town",cTown);
                        }

                        if (flag==1) {
                            //Province data = (Province) dataWrap.getData();
                            intent.putExtra("city", ((City) dataWrap.getData()));
                        }else if (flag==2){
                            //City city = (City) dataWrap.getData();
                            intent.putExtra("county", ((County) dataWrap.getData()));
                        }else if (flag==3){
                            //County county = (County) dataWrap.getData();
                            intent.putExtra("town", ((Town) dataWrap.getData()));
                        }else if (flag==4){
                            //Town town = (Town) dataWrap.getData();
                        }
                        intent.putExtra("flag", flag + 1);
                        startActivity(intent);
                    }
                }

            }
        });
        rec.setAdapter(adapter);

        loadData(flag);
    }
}
