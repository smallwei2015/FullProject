package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_my_wallet)
public class MyWalletActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.coupon)
    View coupon;
    @ViewInject(R.id.balance)
    View balance;
    @ViewInject(R.id.score)
    View score;
    @ViewInject(R.id.coupon_text)
    TextView coupon_text;
    @ViewInject(R.id.balance_text)
    TextView balance_text;
    @ViewInject(R.id.score_text)
    TextView score_text;

    @ViewInject(R.id.wallet_charge)
    View charge;
    @ViewInject(R.id.wallet_exchange_score)
    View exchange;
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_wallet);

        x.view().inject(this);
        initData();
        initView();

    }

    @Override
    public void initData() {
        super.initData();

        getInfo();
    }

    private void getInfo() {
        RequestParams entity = new RequestParams(UrlUtils.N_achievePersonalScore);
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId()+"");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult()==200){
                    JSONObject object = JSON.parseObject(netData.getInfo());
                    String scoreInfo = object.getString("score");
                    String walletInfo = object.getString("wallet");
                    String couponInfo = object.getString("coupon");

                    score_text.setText(scoreInfo);
                    balance_text.setText(walletInfo);
                    coupon_text.setText(couponInfo);
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

    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white,"我的钱包",-1);

        coupon.setOnClickListener(this);
        charge.setOnClickListener(this);
        balance.setOnClickListener(this);
        score.setOnClickListener(this);

        exchange.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.coupon:
                intent = new Intent(mActivity,CouponActivity.class);
                startActivity(intent);
                break;
            case R.id.wallet_charge:
                intent=new Intent(mActivity,BalanceChargeActivity.class);
                startActivity(intent);
                break;
            case R.id.balance:
                intent=new Intent(mActivity,ChargeRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.wallet_exchange_score:
                UIUtils.showToast("该功能暂未开通");
                break;
        }
    }
}
