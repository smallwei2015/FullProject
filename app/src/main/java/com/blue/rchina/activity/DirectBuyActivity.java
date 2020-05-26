package com.blue.rchina.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.CartGoods;
import com.blue.rchina.bean.Goods;
import com.blue.rchina.bean.OrderPrice;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.manager.xUtilsManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.blue.rchina.views.GoodsView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DirectBuyActivity extends BaseActivity {

    public GoodsView countHandle;
    public Goods data;
    public TextView price;
    public TextView des;
    public ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_buy);

        initData();
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"购买商品",-1);

        data = (Goods) getIntent().getSerializableExtra("goods");

        countHandle = ((GoodsView) findViewById(R.id.count_handle));
        price = ((TextView) findViewById(R.id.goods_total_price));
        icon = ((ImageView) findViewById(R.id.order_img));

        des = ((TextView) findViewById(R.id.order_des));
        des.setText(data.getDesc());
        price.setText(String.format("¥%.2f",data.getPrice()));

        x.image().bind(icon, data.getPicsrc(), xUtilsManager.getRectangleImageOption());
    }

    public void btn_buy(View view) {
        getOrderMoney();
    }


    private void getOrderMoney() {

        final List<CartGoods> buyGoods = new ArrayList<CartGoods>();
        CartGoods e = new CartGoods();

        e.setGoodsId(data.getGoodsId());
        e.setCount(countHandle.getCount());
        e.setDesc(data.getDesc());
        e.setPicsrc(data.getPicsrc());
        e.setPrice(data.getPrice());
        e.setTitle(data.getTitle());

        buyGoods.add(e);


        StringBuilder arg = new StringBuilder();

        arg.append("[");

        for (int i = 0; i < buyGoods.size(); i++) {
            arg.append("{");
            arg.append("\"" + "id" + "\"" + ":" + "\"" + buyGoods.get(i).getGoodsId() + "\"");
            arg.append("}");

            if (i != buyGoods.size() - 1) {
                arg.append(",");
            }
        }

        arg.append("]");



        RequestParams entity = new RequestParams(UrlUtils.N_getOrderMoney);
        String value = arg.toString();
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        entity.addBodyParameter("arg1", value);

        entity.addBodyParameter("type", "0");

        entity.addBodyParameter("flag", "0");
        entity.addBodyParameter("dataId", "");
        entity.addBodyParameter("num",""+countHandle.getCount());


        final ProgressDialog dialog=new ProgressDialog(mActivity);

        dialog.setMessage("加载中...");
        dialog.show();

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                JSONObject object = JSON.parseObject(result);

                Integer code = object.getInteger("result");
                if (code == 200) {
                    OrderPrice info = JSON.parseObject(object.getString("info"), OrderPrice.class);

                    Intent intent = new Intent(mActivity, OrderGenerateActivity.class);
                    intent.putExtra("goods", (Serializable) buyGoods);
                    intent.putExtra("flag", 1);
                    intent.putExtra("info",(Serializable) info);
                    intent.putExtra("num",countHandle.getCount());
                    startActivity(intent);

                    finish();

                } else{
                    UIUtils.showToast(""+object.getString("message"));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络请求失败");
                Log.w("44444",ex.getMessage());
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
}
