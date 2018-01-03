package com.blue.rchina.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.fragment.GoodsOrderFragment;
import com.blue.rchina.fragment.TraveOrderFragment;

import org.xutils.x;

public class OrderActivity extends BaseActivity {


    private int orderKind;
    public GoodsOrderFragment goodsFragment;
    public TraveOrderFragment traveFragment;


    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"商品订单",R.mipmap.exchange);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        goodsFragment = new GoodsOrderFragment();
        transaction.add(R.id.order_container, goodsFragment);

        traveFragment = new TraveOrderFragment();
        transaction.add(R.id.order_container, traveFragment);

        transaction.hide(traveFragment);
        transaction.show(goodsFragment);
        transaction.commit();
    }


    @Override
    public void onRightIconClick(View view) {
        super.onRightIconClick(view);

        if (orderKind==1){
            orderKind=0;
        }else {
            orderKind=1;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (orderKind==1) {
            initTop(R.mipmap.left_white, "旅游订单", R.mipmap.exchange);
            transaction.show(traveFragment);
            transaction.hide(goodsFragment);
        }else {
            initTop(R.mipmap.left_white, "商品订单", R.mipmap.exchange);
            transaction.hide(traveFragment);
            transaction.show(goodsFragment);
        }

        /*刷新数据*/
        transaction.commit();
    }

    @Override
    public void initData() {
        super.initData();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        x.view().inject(this);
        initView();
        initData();
    }
}
