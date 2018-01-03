package com.blue.rchina.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.AreaInfo;
import com.blue.rchina.bean.Nearby;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.SPUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.blue.rchina.utils.xUtilsImageUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.permission.EasyPermissions;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static com.blue.rchina.manager.LocationManager.location;

public class NearbySelectListActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.nearby_rec)
    RecyclerView rec;
    @ViewInject(R.id.nearby_ptr)
    PtrClassicFrameLayout ptr;
    public List<Nearby> datas;
    private AMapLocationClient mlocationClient;
    public RecyclerView.Adapter<Holder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_select_list);
        x.view().inject(this);
        initData();
        initView();
    }

    @Override
    public void initData() {
        super.initData();

        datas = new ArrayList<>();

        if (location==null){
            if (AndPermission.hasPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                locate();
            }else {
                AndPermission.with(mActivity).requestCode(200).rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.defaultSettingDialog(mActivity, 400).show();
                    }
                }).permission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .callback(new EasyPermissions.PermissionCallbacks() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                locate();
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {
                                AndPermission.defaultSettingDialog(mActivity, 400).show();
                            }

                            @Override
                            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

                            }
                        }).start();
            }
        }else {
            getNearbyList();
        }
    }
    private  void locate(){

        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {

                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        location=amapLocation;
                        mlocationClient.stopLocation();

                        getNearbyList();
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。

                    }
                }
            }
        });
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000*6);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期
        mlocationClient.startLocation();

    }
    private void getNearbyList() {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveCommunity);

        if (location != null) {
            entity.addBodyParameter("arg2",location.getLatitude()+"");
            entity.addBodyParameter("arg1",location.getLongitude()+"");
        }else {
            UIUtils.showToast("定位失败");
            ptr.refreshComplete();
            return;
        }

        String area = SPUtils.getSP().getString("area", "");
        if (!TextUtils.isEmpty(area)) {
            AreaInfo areaInfo = JSON.parseObject(area, AreaInfo.class);
            entity.addBodyParameter("areaId", areaInfo.getAreaId());
        }

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult()==200){
                    datas.clear();

                    List<Nearby> nearbies = JSON.parseArray(netData.getInfo(), Nearby.class);
                    datas.addAll(nearbies);

                    adapter.notifyDataSetChanged();
                }else {
                    UIUtils.showToast(netData.getMessage());
                }

                if (datas.size()>0) {
                    isNodata(false);
                }else {
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
                ptr.refreshComplete();
            }
        });
    }

    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white,"选择社区",-1);

        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mActivity);
        ptr.setHeaderView(header);
        ptr.setLastUpdateTimeKey(getClass().getName());
        ptr.addPtrUIHandler(header);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                fresh();
            }
        });

        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view= LayoutInflater.from(mActivity).inflate(R.layout.nearby_select_item,parent,false);


                return new Holder(view);
            }

            @Override
            public void onBindViewHolder(Holder holder, int position) {

                holder.parent.setTag(position);
                holder.parent.setOnClickListener(NearbySelectListActivity.this);
                Nearby nearby = datas.get(position);

                holder.title.setText(nearby.getTitle());
                holder.des.setText(nearby.getLocation());
                xUtilsImageUtils.display(holder.icon,nearby.getPicsrc());

                double v = Integer.parseInt(nearby.getDistance()) / 1000.0;
                holder.distance.setText(String.format("%.1f",v)+"km");
            }


            @Override
            public int getItemCount() {
                if (datas != null) {
                    return datas.size();
                }
                return 0;
            }
        };
        rec.setAdapter(adapter);

    }

    private void fresh() {
        getNearbyList();
    }

    @Override
    public void onClick(View v) {

        Nearby nearby = datas.get(((int) v.getTag()));
        switch (v.getId()){
            default:
                selectNearby(nearby);
                break;
        }
    }

    private void selectNearby(final Nearby nearby) {
        isHideLoading(false);
        if (!UserManager.isLogin()){
            UserManager.toLogin();
            return;
        }
        RequestParams entity = new RequestParams(UrlUtils.N_selectCommunity);
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId()+"");
        entity.addBodyParameter("dataId",nearby.getFranchiseeId());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult()==200){
                    UIUtils.showToast("选择社区成功");

                    UserManager.getUser().setCommunityName(nearby.getTitle());
                    UserManager.saveUser(UserManager.getUser());
                    /*存储当前选择的城市*/
                    SPUtils.getSP().edit().putString("nearby",JSON.toJSONString(nearby)).apply();
                    finish();
                }else {
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
            }
        });
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView title;
        View parent;
        public TextView des;
        public ImageView icon;
        public TextView distance;

        public Holder(View itemView) {
            super(itemView);
            parent=itemView;
            title = ((TextView) itemView.findViewById(R.id.title));
            des = ((TextView) itemView.findViewById(R.id.des));
            icon = ((ImageView) itemView.findViewById(R.id.icon));
            distance = ((TextView) itemView.findViewById(R.id.distance));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==400){
            if(AndPermission.hasPermission(mActivity,Manifest.permission.ACCESS_FINE_LOCATION)){
                locate();
            }
        }
    }
}
