package com.blue.rchina.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.living.SaveResult;
import com.blue.rchina.manager.LocationInteface;
import com.blue.rchina.manager.LocationManager;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.netease.LSMediaCapture.lsMediaCapture;
import com.vode.living.bean.LivingParam;
import com.vode.living.ui.LiveStreamingActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class StartLivingActivity extends BaseActivity {

    private LivingParam publishParam;
    public EditText title;
    private AMapLocation clocation;
    public String cid;
    public ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_living);
        initTop(R.mipmap.left_white, "开始直播", -1);

        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();

        title = ((EditText) findViewById(com.vode.living.R.id.title));

        RadioGroup scaleGroup = (RadioGroup) findViewById(R.id.main_screen_scale);
        scaleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.main_scale_16x9:
                        publishParam.setScale_16x9(true);
                        break;
                    case R.id.main_scale_normal:
                        publishParam.setScale_16x9(false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        publishParam = new LivingParam();
    }

    public void btn_start(View view) {

        if (!UserManager.isLogin()) {
            UserManager.toLogin();
            return;
        }


        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("开始直播中...");
        dialog.show();

        LocationManager.initLocation(mActivity, new LocationInteface() {

            @Override
            public void locationSuccess(AMapLocation location) {
                clocation = location;
                startLiving();
            }

            @Override
            public void locationFaild() {
                UIUtils.showToast("定位失败");
                dialog.dismiss();
            }
        });

    }

    private void startLiving() {
        String titleStr = title.getText().toString().trim();
        if (TextUtils.isEmpty(titleStr)) {
            UIUtils.showToast("请输入直播标题");
            dialog.dismiss();
            return;
        }

        if (titleStr.length()<6||titleStr.length()>50){
            UIUtils.showToast("直播标题长度错误");

            return;
        }
        RequestParams entity = new RequestParams(UrlUtils.N_saveLivingVideoInfo);
        entity.addBodyParameter("appuserId", UserManager.getAppuserId() + "");
        entity.addBodyParameter("channelName", titleStr);
        entity.addBodyParameter("needRecord", "1");
        entity.addBodyParameter("lon", clocation.getLongitude() + "");
        entity.addBodyParameter("lat", clocation.getLatitude() + "");
        entity.addBodyParameter("location", clocation.getAddress());

        x.http().post(entity, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {


                SaveResult netData = JSON.parseObject(result, SaveResult.class);

                if (netData.getCode() == 200) {

                    SaveResult.RetBean ret = netData.getRet();
                    cid = ret.getCid();
                    publishParam.setPushUrl(ret.getPushUrl());
                        /*横屏直播*/
                    //publishParam.setScale_16x9(true);
                    publishParam.setVideoQuality(lsMediaCapture.VideoQuality.HIGH);

                    Intent intent = new Intent(mActivity, LiveStreamingActivity.class);
                    intent.putExtra("data", publishParam);
                    startActivity(intent);

                    finish();
                } else {
                    UIUtils.showToast("生成直播间失败" + netData.getMsg());
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
                dialog.dismiss();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        RequestParams entity = new RequestParams(UrlUtils.N_liveFinish);
        entity.addBodyParameter("cid", cid);
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult() == 200) {
                    UIUtils.showToast("直播已结束");
                } else {
                    UIUtils.showToast("结束直播出错");
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
}
