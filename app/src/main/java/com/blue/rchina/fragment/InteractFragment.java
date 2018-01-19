package com.blue.rchina.fragment;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blue.rchina.Main2Activity;
import com.blue.rchina.R;
import com.blue.rchina.activity.InteractCommentActivity;
import com.blue.rchina.activity.LoginActivity;
import com.blue.rchina.activity.MyInteractActivity;
import com.blue.rchina.activity.NewsKindDetailActivty;
import com.blue.rchina.activity.TopNewsDetailActivity;
import com.blue.rchina.adapter.InteractAdapter;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.Alliance;
import com.blue.rchina.bean.Article;
import com.blue.rchina.bean.Channel;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.Report;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.SPUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.blue.rchina.views.MarqueeTextViewIgnoreLength;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.blue.rchina.base.BaseActivity.action_autoRefresh;


/**
 * A simple {@link Fragment} subclass.
 */
public class InteractFragment extends BaseFragment {


    private RecyclerView recycler;
    private LinearLayoutManager manager;
    private List<DataWrap> items;

    private int currentCount = 1;
    private boolean isLoading = false;
    private View.OnClickListener listener;
    private InteractAdapter adapter;
    private AMapLocation location;
    private PtrFrameLayout ptrFrameLayout;
    public Channel channel;
    public MarqueeTextViewIgnoreLength marque;
    private BroadcastReceiver autoRefreshReceiver;


    public InteractFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interact, container, false);
        initView(view);
        initData();

        registerAutoRefreshReceiver();
        return view;
    }

    public void registerAutoRefreshReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(action_autoRefresh);

        autoRefreshReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase(action_autoRefresh)) {
                    isHideLoading(false);
                    fresh();
                }
            }
        };

        getActivity().registerReceiver(autoRefreshReceiver, filter);
    }

    @Override
    public void initView(View view) {
        super.initView(view);


        marque = ((MarqueeTextViewIgnoreLength) view.findViewById(R.id.marque));
        marque.setText("蓝太平洋舆情监测系统(BiRadar)利用互联网信息采集技术、信息智能处理技术和全文检索技术，对境内外网络中的新闻网页、论坛、贴吧、博客、微博、新闻评论等网络资源进行全网采集、定向采集和智能分析，提供舆情信息检索、热点信息的发现、热点跟踪定位、敏感信息监测、辅助决策支持…… ");
        marque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    Article tag1 = (Article) tag;
                    Intent intent=new Intent(mActivity, NewsKindDetailActivty.class);
                    intent.putExtra("data",tag1);
                    startActivity(intent);
                }else {
                    UIUtils.showToast("暂无通知");
                }
            }
        });


        recycler = ((RecyclerView) view.findViewById(R.id.recycler));
        ptrFrameLayout = (PtrFrameLayout) view.findViewById(R.id.fresh_frame);
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getContext());
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);


        header.setLastUpdateTimeKey(this.getClass().getName());

        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recycler, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                fresh();
                isHideLoading(true);
            }
        });


        recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && items.size() > 0) {
                    if (!isLoading) {
                        //loadData(currentCount + 1);
                        loadCurCityData(2);
                    } else {
                        UIUtils.showToast("加载中...");
                    }
                }
            }
        });

    }


    @Override
    public void initData() {
        super.initData();


        Bundle arguments = getArguments();
        if (arguments != null) {
            channel = (Channel) arguments.getSerializable("channel");
        }
        items = new ArrayList<>();
        /*加载缓存数据*/
        String interact = SPUtils.getCacheSp().getString("interact", "");

        NetData netData = JSON.parseObject(interact, NetData.class);
        if (netData != null) {
            JSONObject jsonInfo = JSON.parseObject(netData.getInfo());

            List<Report> reports = JSON.parseArray(jsonInfo.getString("report"), Report.class);


            for (int i = 0; i < reports.size(); i++) {
                DataWrap e = new DataWrap();
                int size = reports.get(i).getManyPic().size();
                if (size == 1) {
                    e.setType(4);
                } else if (size == 2) {
                    e.setType(5);
                } else if (size == 0) {
                    e.setType(6);
                } else {
                    e.setType(0);
                }
                e.setData(reports.get(i));
                items.add(e);
            }

            List<Alliance> near = JSON.parseArray(jsonInfo.getString("near"), Alliance.class);

            if (near!=null&&near.size()>0) {
                DataWrap e1 = new DataWrap();
                e1.setType(7);
                e1.setData(near.get(0));
                items.add(0,e1);
            }
        }


        if (location != null) {
            fresh();
        } else {
            locate();
        }


        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();

                DataWrap dataWrap = items.get(position);

                Intent intent = null;
                if (dataWrap.getType() == 7) {
                    intent = new Intent(mActivity, TopNewsDetailActivity.class);
                    intent.putExtra("data", ((Alliance) dataWrap.getData()));
                    startActivity(intent);

                    //getAllianceStrcture(((Alliance) dataWrap.getData()));
                    return;
                }

                /*type7数据类型不一样*/
                Report data = (Report) dataWrap.getData();
                switch (v.getId()) {
                    case R.id.interact_agree_parent:


                        if (UserManager.isLogin()) {
                            agree(position);
                        } else {
                            intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }


                        break;
                    case R.id.interact_comment_parent:
                        intent = new Intent(getActivity(), InteractCommentActivity.class);
                        intent.putExtra("data", dataWrap);
                        startActivityForResult(intent, 100);
                        break;
                    case R.id.interact_report_parent:
                        if (UserManager.isLogin()) {
                            report(position);
                        } else {
                            intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.interact_icon:
                        intent = new Intent(mActivity, MyInteractActivity.class);
                        intent.putExtra("data", data);
                        startActivity(intent);
                        break;
                    default:
                        //整个布局点击的事件
                        intent = new Intent(getActivity(), InteractCommentActivity.class);
                        intent.putExtra("data", dataWrap);
                        startActivityForResult(intent, 100);
                        break;
                }
            }
        };
        manager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(manager);


        adapter = new InteractAdapter(items);
        adapter.setListener(listener);

        recycler.setAdapter(adapter);

        if (items.size()==0){
            isHideLoading(false);
        }

    }

    private void agree(int position) {
        handleReport(position, 1);
    }

    private void locate() {
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        final AMapLocationClient mlocationClient = new AMapLocationClient(getContext());
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

    private void fresh() {
        loadCurCityData(1);
    }

    private void freshData(){
        RequestParams entity = new RequestParams(UrlUtils.N_achieveReport);
        if (location == null) {
            entity.addBodyParameter("arg1", "0");
            entity.addBodyParameter("arg2", "0");
        } else {
            entity.addBodyParameter("arg1", location.getLongitude() + "");
            entity.addBodyParameter("arg2", location.getLatitude() + "");
        }
        entity.addBodyParameter("page", "1");
        if (UserManager.isLogin()) {
            entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        }
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject object = JSON.parseObject(result);
                Integer code = object.getInteger("result");
                if (code == 200) {
                    currentCount = 1;
                    items.clear();
                    DataWrap e1 = new DataWrap();
                    e1.setType(7);
                    e1.setData(new Report());
                    items.add(e1);
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
                        items.add(e);
                    }
                    adapter.notifyDataSetChanged();
                    if (items.size() == 0) {
                        isNodata(true);
                    }
                } else {
                    UIUtils.showToast("刷新失败");
                }
                if (items.size() > 0) {
                    isNodata(false);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (items.size() == 0) {
                    isNodata(true);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                ptrFrameLayout.refreshComplete();
                isHideLoading(true);
            }
        });
    }
    private void loadData(int page) {
        isLoading = true;

        RequestParams entity = new RequestParams(UrlUtils.N_achieveReport);
        //arg1当前所在经度
        //arg2当前所在纬度
        //page

        if (location != null) {
            entity.addBodyParameter("arg1", location.getLongitude() + "");
            entity.addBodyParameter("arg2", location.getLatitude() + "");
        } else {
            entity.addBodyParameter("arg1", "0");
            entity.addBodyParameter("arg2", "0");
        }
        entity.addBodyParameter("page", (currentCount + 1) + "");
        if (UserManager.isLogin()) {
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
                        items.add(e);
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    UIUtils.showToast("没有更多数据");
                }


                if (items.size() == 0) {
                    isNodata(true);
                } else {
                    isNodata(false);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (items.size() == 0) {
                    isNodata(true);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isHideLoading(true);
                isLoading = false;
            }
        });
    }


    private void loadCurCityData(final int type) {

        isLoading = true;

        RequestParams entity = new RequestParams(UrlUtils.N_achieveDynamicData);
        if (location != null) {
            entity.addBodyParameter("arg1", location.getLongitude() + "");
            entity.addBodyParameter("arg2", location.getLatitude() + "");
        } else {
            entity.addBodyParameter("arg1", "0");
            entity.addBodyParameter("arg2", "0");
        }
        if (type == 1) {
            /*1表示刷新*/
            entity.addBodyParameter("page", "1");
        } else {
            entity.addBodyParameter("page", (currentCount + 1) + "");
        }
        if (UserManager.isLogin()) {
            entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        }

        entity.addBodyParameter("areaId", ((Main2Activity) mActivity).curCity.getAreaId());

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                JSONObject object = JSON.parseObject(result);

                int code = object.getInteger("result");
                if (code == 200) {
                    if (type == 1) {
                        currentCount = 1;
                        items.clear();
                        /*刷新情况*/

                        SPUtils.getCacheSp().edit().putString("interact",result).commit();

                        JSONObject jsonInfo = JSON.parseObject(object.getString("info"));

                        List<Report> reports = JSON.parseArray(jsonInfo.getString("report"), Report.class);


                        for (int i = 0; i < reports.size(); i++) {
                            DataWrap e = new DataWrap();
                            int size = reports.get(i).getManyPic().size();
                            if (size == 1) {
                                e.setType(4);
                            } else if (size == 2) {
                                e.setType(5);
                            } else if (size == 0) {
                                e.setType(6);
                            } else {
                                e.setType(0);
                            }
                            e.setData(reports.get(i));
                            items.add(e);
                        }

                        List<Alliance> near = JSON.parseArray(jsonInfo.getString("near"), Alliance.class);

                        if (near!=null&&near.size()>0) {
                            DataWrap e1 = new DataWrap();
                            e1.setType(7);
                            e1.setData(near.get(0));
                            items.add(0,e1);
                        }

                        List<Article> notices = JSON.parseArray(jsonInfo.getString("notice"), Article.class);
                        if (notices!=null&&notices.size()>0){
                            marque.setText(notices.get(0).getTitle());
                            marque.setTag(notices.get(0));
                        }

                        adapter.notifyDataSetChanged();

                    } else {
                        /*上拉加载情况*/
                        JSONObject jsonInfo = JSON.parseObject(object.getString("info"));
                        List<Report> reports = JSON.parseArray(jsonInfo.getString("report"), Report.class);

                        if (reports.size() > 0) {
                            currentCount++;
                        }
                        for (int i = 0; i < reports.size(); i++) {
                            DataWrap e = new DataWrap();
                            int size = reports.get(i).getManyPic().size();
                            if (size == 1) {
                                e.setType(4);
                            } else if (size == 2) {
                                e.setType(5);
                            } else if (size == 0) {
                                e.setType(6);
                            } else {
                                e.setType(0);
                            }
                            e.setData(reports.get(i));
                            items.add(e);
                        }

                        /*List<Alliance> near = JSON.parseArray(jsonInfo.getString("near"), Alliance.class);
                        List<TopNotice> notice = JSON.parseArray(jsonInfo.getString("notice"), TopNotice.class);*/
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络异常");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isLoading = false;
                ptrFrameLayout.refreshComplete();
                isHideLoading(true);
            }
        });
    }

    private void handleReport(final int position, final int flag) {

        final DataWrap dataWrap = items.get(position);
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

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void report(final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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


    private void getAllianceStrcture(Alliance data) {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveFranchiseeStructure);
        if (data != null) {
            entity.addBodyParameter("dataId",data.getFranchiseeId());
        }

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult()==200){

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
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (autoRefreshReceiver != null) {
            getActivity().unregisterReceiver(autoRefreshReceiver);
        }
    }
}
