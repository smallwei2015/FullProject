package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class BalanceChargeActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.balance)
    EditText balance;
    @ViewInject(R.id.sure)
    TextView sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_charge);
        x.view().inject(this);

        initData();
        initView();
    }


    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white, "余额充值", R.mipmap.charge_record);
        sure.setOnClickListener(this);
        balance.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        balance.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        if (source.equals(".") && dest.toString().length() == 0) {
                            return "0.";
                        }

                        if (source.equals("0") && dest.toString().length() == 0) {
                            return "";
                        }
                        if (dest.toString().contains(".")) {
                            int index = dest.toString().indexOf(".");
                            int length = dest.toString().substring(index).length();
                            if (length == 3) {
                                return "";
                            }
                        }
                        return null;
                    }
                }
        });

    }

    @Override
    public void onClick(View v) {
        generateOrder();
    }

    private void generateOrder() {



        String balanceStr = balance.getText().toString();
        if (TextUtils.isEmpty(balanceStr)) {
            UIUtils.showToast("请输入金额");
            return;
        }

        isHideLoading(false);
        final double bDouble= Double.parseDouble(balanceStr);
        if (bDouble <= 0) {
            UIUtils.showToast("金额必须要大于0");
            return;
        }

        RequestParams entity = new RequestParams(UrlUtils.N_getRechargeOrder);
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        entity.addBodyParameter("arg1", balanceStr);
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                JSONObject object = JSON.parseObject(result);
                if (object.getInteger("result") == 200) {

                    String orderNo = object.getString("orderNo");

                    Intent intent = new Intent(mActivity, PayActivity.class);

                    intent.putExtra("id", orderNo);
                    intent.putExtra("money", bDouble);
                    intent.putExtra("flag",1);

                    startActivityForResult(intent,200);
                } else {
                    UIUtils.showToast("充值订单生成失败");
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
                isHideLoading(true);
            }
        });
    }

    @Override
    public void onRightIconClick(View view) {
        super.onRightIconClick(view);

        Intent intent=new Intent(mActivity,ChargeRecordActivity.class);
        startActivity(intent);
    }
}
