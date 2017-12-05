package com.blue.rchina.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.Channel;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsWebFragment extends BaseFragment {


    private WebView web;
    private Channel data;
    private WebSettings mSetting;


    private View back;
    private PtrFrameLayout ptrFrame;

    public NewsWebFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_news_web, container, false);

        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);



        web = ((WebView) view.findViewById(R.id.news_web));
        back = view.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (web.canGoBack()) {
                    web.goBack();// 返回前一个页面
                }
            }
        });

        // 自适应屏幕
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("GBK");
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        web.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (newProgress==100){
                    isHideLoading(true);

                    if (ptrFrame != null) {
                        ptrFrame.refreshComplete();
                    }
                }else {
                    isHideLoading(false);
                }

            }
        });
        web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        ptrFrame = ((PtrFrameLayout) view.findViewById(R.id.fresh_frame));
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getContext());
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);

        header.setLastUpdateTimeKey(this.getClass().getName());

        ptrFrame.setPtrHandler(new PtrHandler()
        {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header)
            {
                //return !checkContentCanPullDown(frame, web, header);
                /*web取消下拉刷新*/
                return false;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                web.reload();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();

        Bundle arguments = getArguments();
        data = (Channel) arguments.getSerializable("data");
        String outLink = data.getOutLink();
        web.loadUrl(outLink);
    }

    private long lastBack;
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    private boolean checkContentCanPullDown(PtrFrameLayout frame, View content, View header) {
        if(content instanceof RecyclerView) {
            /*这里就单独处理recycler*/
            RecyclerView content1 = (RecyclerView) content;
            return content1.canScrollVertically(-1);
        } else {
            return content.getScrollY() > 0;
        }
    }

}
