package com.blue.rchina.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.Goods;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class WebGoodsDetailActivity extends BaseActivity {

    @ViewInject(R.id.web)
    WebView web;

    public Goods goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_web_goods_detail);
        x.view().inject(this);
        initData();
        initView();
    }

    @Override
    public void initData() {
        super.initData();

        goods = (Goods) getIntent().getSerializableExtra("goods");


    }

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"商品详细信息",-1);


        if (goods != null) {
            web.loadUrl(goods.getPageLink());
        }
    }
}
