package com.blue.rchina.activity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.Article;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

@ContentView(R.layout.activity_web_view)
public class WebViewActivity extends BaseActivity {

    @ViewInject(R.id.web_ptr)
    PtrClassicFrameLayout ptr;
    @ViewInject(R.id.web)
    WebView web;
    public List<Article> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        initData();
        initView();
    }

    @Override
    public void initData() {
        super.initData();
        articles = new ArrayList<>();
    }

    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white, "社区介绍", -1);

        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mActivity);
        ptr.setHeaderView(header);
        ptr.addPtrUIHandler(header);
        header.setLastUpdateTimeKey(getClass().getName());

        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //loadData();
                web.reload();
            }
        });

        //声明WebSettings子类
        WebSettings webSettings = web.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小


        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                isHideLoading(true);
                ptr.refreshComplete();
            }
        });
        web.loadUrl(UrlUtils.N_achieveCommunityIntroduce+"?appuserId="+UserManager.getAppuserId());
        //loadData();
    }

    private void loadData() {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveCommunityChannelData);
        entity.addBodyParameter("appuserId", UserManager.getAppuserId());
        entity.addBodyParameter("page", "1");
        entity.addBodyParameter("flag", "1");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult() == 200) {

                    String list = JSON.parseObject(netData.getInfo()).getString("list");
                    articles = JSON.parseArray(list, Article.class);
                    if (articles != null && articles.size() > 0) {
                        String linkUrl = articles.get(0).getLinkUrl();
                        web.loadUrl(linkUrl);
                    }

                } else {
                    UIUtils.showToast(netData.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("服务器错误");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isHideLoading(true);
                ptr.refreshComplete();
                if (articles.size() > 0) {
                    isNodata(false);
                } else {
                    isNodata(true);
                }
            }
        });
    }
}
