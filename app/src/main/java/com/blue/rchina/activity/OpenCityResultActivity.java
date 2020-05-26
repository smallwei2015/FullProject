package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.enter.Channel;
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

import java.util.List;


@ContentView(R.layout.activity_open_city_result)
public class OpenCityResultActivity extends BaseActivity {


    @ViewInject(R.id.name)
    View nameParent;
    @ViewInject(R.id.name_edit)
    EditText name;
    @ViewInject(R.id.application_edit)
    EditText application;
    @ViewInject(R.id.phone_edit)
    EditText phone;
    @ViewInject(R.id.sure)
    TextView sure;

    public Province province;
    public City city;
    public County county;
    public Town town;
    public List<Channel> channels;
    public int flag;

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


        flag = getIntent().getIntExtra("openFlag",0);

        province = ((Province) getIntent().getSerializableExtra("province"));

        city = ((City) getIntent().getSerializableExtra("city"));

        county = ((County) getIntent().getSerializableExtra("county"));

        town = ((Town) getIntent().getSerializableExtra("town"));




    }

    @Override
    public void initView() {
        super.initView();

        if (flag==0) {
            initTop(R.mipmap.left_white, "开通联盟APP", -1);
        }else if (flag==1){
            initTop(R.mipmap.left_white, "开通城市APP", -1);
            nameParent.setVisibility(View.GONE);
        }else {
            initTop(R.mipmap.left_white, "开通置顶版位", -1);
        }
        Intent intent=new Intent(mActivity,OpenChannelActivity.class);
        startActivityForResult(intent,200);

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==0) {
                    open();
                }else {
                    openArea();
                }
            }
        });

    }

    private void open() {


        RequestParams entity = new RequestParams(UrlUtils.N_getUnionOrderId);

        entity.addBodyParameter("appuserId", UserManager.getAppuserId());
        if (province != null) {
            entity.addBodyParameter("provinceId",province.getProvinceId());
        }
        if (city != null) {
            entity.addBodyParameter("cityId",city.getCityId());
        }
        if (county != null) {
            entity.addBodyParameter("countyId",county.getCountyId());
        }
        if (town != null) {
            entity.addBodyParameter("townId",town.getTownId());
        }

        if (channels!=null&&channels.size()>0){

            StringBuilder builder=new StringBuilder();

            for (int i = 0; i < channels.size(); i++) {
                builder.append(channels.get(i).getChannelId());
                if (i!=channels.size()-1){
                    builder.append(",");
                }
            }
            entity.addBodyParameter("channelIds",builder.toString());
        }
        String nameStr = name.getText().toString().trim();
        if (TextUtils.isEmpty(nameStr)){
            UIUtils.showToast("请输入联盟全称");
            return;
        }
        entity.addBodyParameter("arg1", nameStr);

        String appStr = application.getText().toString().trim();
        if (TextUtils.isEmpty(appStr)){
            UIUtils.showToast("请输入应用名称");
            return;
        }
        entity.addBodyParameter("arg2",appStr);

        String phoneStr = phone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneStr)){
            UIUtils.showToast("请输入联系电话");
            return;
        }
        entity.addBodyParameter("phone",phoneStr);

        isHideLoading(false);
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject object = JSON.parseObject(result);
                if (object.getInteger("result")==200){
                    String orderNo = object.getString("orderNo");
                    Double money = object.getDouble("money");
                    Intent intent=new Intent(mActivity,PayActivity.class);
                    intent.putExtra("id",orderNo);
                    intent.putExtra("money",money);
                    startActivity(intent);
                }else {
                    UIUtils.showToast(object.getString("message"));
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
    public void openArea(){
        RequestParams entity = new RequestParams(UrlUtils.N_oppenAreaApply);

        entity.addBodyParameter("appuserId", UserManager.getAppuserId());
        if (province != null) {
            entity.addBodyParameter("provinceId",province.getProvinceId());
        }
        if (city != null) {
            entity.addBodyParameter("cityId",city.getCityId());
        }
        if (county != null) {
            entity.addBodyParameter("countyId",county.getCountyId());
        }
        if (town != null) {
            entity.addBodyParameter("townId",town.getTownId());
        }

        if (channels!=null&&channels.size()>0){

            StringBuilder builder=new StringBuilder();

            for (int i = 0; i < channels.size(); i++) {
                builder.append(channels.get(i).getChannelId());
                if (i!=channels.size()-1){
                    builder.append(",");
                }
            }
            entity.addBodyParameter("channelIds",builder.toString());
        }

        String appStr = application.getText().toString().trim();
        if (TextUtils.isEmpty(appStr)){
            UIUtils.showToast("请输入应用名称");
            return;
        }
        entity.addBodyParameter("arg2",appStr);


        String phoneStr = phone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneStr)){
            UIUtils.showToast("请输入联系电话");
            return;
        }
        entity.addBodyParameter("phone",phoneStr);


        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult()==200){
                    UIUtils.showToast("申请成功请等待审核");
                    finish();
                }else {
                    UIUtils.showToast(netData.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("服务器异常");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==200&&resultCode==200){
            channels = ((List<Channel>) data.getSerializableExtra("channel"));
        }else {
            finish();
        }
    }
}
