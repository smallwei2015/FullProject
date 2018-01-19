package com.blue.rchina.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blue.rchina.R;
import com.blue.rchina.adapter.InteractAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.Report;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static com.blue.rchina.manager.LocationManager.location;

public class MyInteractActivity extends BaseActivity {

    @ViewInject(R.id.ptr)
    PtrClassicFrameLayout ptr;
    @ViewInject(R.id.rec)
    RecyclerView rec;
    public List<DataWrap> datas;
    public InteractAdapter adapter;
    private int currentCount=1;
    private View.OnClickListener listener;
    private boolean isLoading;
    public Report data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_interact);
        x.view().inject(this);
        initData();
        initView();

    }

    @Override
    public void initData() {
        super.initData();

        /*查看别人的所有发布就会有这个数据*/
        data = ((Report) getIntent().getSerializableExtra("data"));

        datas = new ArrayList<>();

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();

                DataWrap dataWrap = datas.get(position);
                Report data = (Report) dataWrap.getData();
                Intent intent = null;
                if (dataWrap.getType()==7){
                    intent=new Intent(mActivity,TopNewsDetailActivity.class);
                    startActivity(intent);
                    return;
                }

                switch (v.getId()) {
                    case R.id.interact_agree_parent:


                        if (UserManager.isLogin()) {
                            handleReport(position, 1);
                        } else {
                            UserManager.toLogin();
                        }


                        break;
                    case R.id.interact_comment_parent:
                        intent = new Intent(mActivity, InteractCommentActivity.class);
                        intent.putExtra("data", dataWrap);
                        startActivityForResult(intent, 100);
                        break;
                    case R.id.interact_report_parent:
                        if (UserManager.isLogin()) {
                            report(position);
                        } else {
                            UserManager.toLogin();
                        }
                        break;
                    case R.id.interact_delete:
                        UIUtils.showToast("delete item");
                        break;
                    default:
                        //整个布局点击的事件
                        intent = new Intent(mActivity, InteractCommentActivity.class);
                        intent.putExtra("data", dataWrap);
                        startActivityForResult(intent, 100);
                        break;
                }
            }
        };

        if (location != null) {
            fresh();
        }else {
            locate();
        }

    }
    private void locate() {
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        final AMapLocationClient mlocationClient = new AMapLocationClient(mActivity);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {

                        location = amapLocation;
                        fresh();

                        mlocationClient.stopLocation();

                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        fresh();
                    }
                }
            }
        });
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000 * 60);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期
        mlocationClient.startLocation();
    }
    @Override
    public void initView() {
        super.initView();

        if (data != null) {
            initTop(R.mipmap.left_white,"所有动态",-1);
        }else {
            initTop(R.mipmap.left_white, "我的动态", -1);
        }

        isHideLoading(false);

        PtrClassicDefaultHeader ptrUIHandler = new PtrClassicDefaultHeader(mActivity);
        ptr.setHeaderView(ptrUIHandler);
        ptr.addPtrUIHandler(ptrUIHandler);
        ptr.setLastUpdateTimeKey(this.getClass().getName());

        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.refreshComplete();
            }
        });

        adapter = new InteractAdapter(datas);
        adapter.setListener(listener);
        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        rec.setAdapter(adapter);

        rec.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && datas.size() > 0) {
                    if (!isLoading) {
                        loadData();
                    } else {
                        UIUtils.showToast("加载中...");
                    }
                }
            }
        });


    }

    private void fresh() {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveAppuserReport);

        if (location == null) {
            entity.addBodyParameter("arg1", "0");
            entity.addBodyParameter("arg2", "0");
        } else {
            entity.addBodyParameter("arg1", location.getLongitude() + "");
            entity.addBodyParameter("arg2", location.getLatitude() + "");
        }
        entity.addBodyParameter("page", "1");

        if (data!=null){
            entity.addBodyParameter("appuserId", data.getAppuserId()+ "");
        }else {
            if (UserManager.isLogin()) {
                entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
            }
        }
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                JSONObject object = JSON.parseObject(result);
                Integer code = object.getInteger("result");
                if (code == 200) {
                    currentCount = 1;
                    datas.clear();

                    /*DataWrap e1 = new DataWrap();
                    e1.setType(7);
                    e1.setData(new Report());
                    datas.add(e1);*/

                    List<Report> info = JSON.parseArray(object.getString("info"), Report.class);
                    for (int i = 0; i < info.size(); i++) {
                        DataWrap e = new DataWrap();

                        if (data!=null){
                            /*别人的动态就隐藏删除按钮*/
                        }else {
                            /*自己的动态就设置可显示删除按钮*/
                            info.get(i).setDisplayType(1);
                        }

                        int size = info.get(i).getManyPic().size();
                        if (size == 1) {
                            e.setType(4);
                        } else if (size == 2) {
                            e.setType(5);
                        } else if (size == 0) {
                            e.setType(6);
                        } else {
                            e.setType(0);
                        }
                        e.setData(info.get(i));
                        datas.add(e);
                    }


                    adapter.notifyDataSetChanged();


                    if (datas.size() == 0) {
                        isNodata(true);
                    }
                } else {
                    UIUtils.showToast("刷新失败");
                }

                if (datas.size() > 0) {
                    isNodata(false);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (datas.size() == 0) {
                    isNodata(true);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                ptr.refreshComplete();
                isHideLoading(true);
            }
        });
    }

    private void loadData() {

        isLoading=true;

        RequestParams entity = new RequestParams(UrlUtils.N_achieveAppuserReport);

        if (location != null) {
            entity.addBodyParameter("arg1", location.getLongitude() + "");
            entity.addBodyParameter("arg2", location.getLatitude() + "");
        } else {
            entity.addBodyParameter("arg1", "0");
            entity.addBodyParameter("arg2", "0");
        }
        entity.addBodyParameter("page", (currentCount+1) + "");

        if (data!=null){
            entity.addBodyParameter("appuserId", data.getAppuserId()+ "");
        }else if (UserManager.isLogin()) {
            entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        }
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                JSONObject object = JSON.parseObject(result);
                Integer code = object.getInteger("result");
                if (code == 200) {
                    currentCount++;
                    List<Report> info = JSON.parseArray(object.getString("info"), Report.class);
                    for (int i = 0; i < info.size(); i++) {
                        DataWrap e = new DataWrap();
                        int size = info.get(i).getManyPic().size();
                        if (size == 1) {
                            e.setType(4);
                        } else if (size == 2) {
                            e.setType(5);
                        } else if (size == 0) {
                            e.setType(6);
                        } else {
                            e.setType(0);
                        }
                        e.setData(info.get(i));
                        datas.add(e);
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    UIUtils.showToast("没有更多数据");
                }


                if (datas.size() == 0) {
                    isNodata(true);
                } else {
                    isNodata(false);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (datas.size() == 0) {
                    isNodata(true);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isHideLoading(true);
                isLoading=false;
            }
        });
    }

    private void report(final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        builder.setTitle("举报：");
        builder.setMessage("是否确认进行举报？");
        builder.setPositiveButton("确认举报", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                handleReport(i, 0);

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
    private void handleReport(final int position, final int flag) {

        final DataWrap dataWrap = datas.get(position);
        final Report data = (Report) dataWrap.getData();

        RequestParams entity = new RequestParams(UrlUtils.N_handleReport);
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        entity.addBodyParameter("flag", flag + "");
        entity.addBodyParameter("dataId", data.getReportId() + "");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject object = JSON.parseObject(result);
                Integer code = object.getInteger("result");
                if (code == 201) {
                    UIUtils.showToast("举报成功");
                } else if (code == 202) {
                    UIUtils.showToast("点赞成功");
                    /*设置已点赞，点赞数量加1*/
                    data.setPraiseState(1);
                    data.setPraiseCount(data.getPraiseCount()+1);
                    adapter.notifyDataSetChanged();
                    //fresh();
                } else if (code == 501) {
                    UIUtils.showToast("不可重复举报");
                } else if (code == 502) {
                    UIUtils.showToast("不可重复点赞");
                } else {
                    UIUtils.showToast("服务器异常");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100:
                fresh();
                break;
        }
    }
}
