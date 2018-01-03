package com.blue.rchina.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.blue.rchina.R;
import com.blue.rchina.utils.UIUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import static com.blue.rchina.activity.PayActivity.WEIXID;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, WEIXID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp.errCode == 0) { //支付成功
                sendBroadcast(new Intent().setAction("rcchina_PAY_SUCCESS"));
                UIUtils.showToast("支付成功");
            } else if (baseResp.errCode==-1){
                sendBroadcast(new Intent().setAction("rcchina_PAY_FAILD"));
                UIUtils.showToast("支付失败");
            }else {
                sendBroadcast(new Intent().setAction("rcchina_PAY_CANCEL"));
                UIUtils.showToast("用户取消支付");
            }
            finish();
        }
    }
}