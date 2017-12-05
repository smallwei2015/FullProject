package com.blue.rchina.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.mall.Order;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class OrderDetailActivity extends BaseActivity {

    @ViewInject(R.id.order_number)
    TextView transport_order;

    /*@ViewInject(R.id.trans_ptr)
    PtrClassicFrameLayout ptrFrame;*/
    @ViewInject(R.id.trans_web)
    WebView web;

    public Order order;
    public RecyclerView.Adapter<Holder> adapter;
    private WebSettings mSetting;

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"订单详情",-1);

        /*PtrClassicDefaultHeader ptrUIHandler = new PtrClassicDefaultHeader(mActivity);
        ptrFrame.addPtrUIHandler(ptrUIHandler);
        ptrFrame.setHeaderView(ptrUIHandler);

        ptrUIHandler.setLastUpdateTimeKey(getClass().getName());

        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,content,header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                fresh();
            }
        });*/

        /*rec.setLayoutManager(new LinearLayoutManager(mActivity));

        adapter = new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(Holder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };
        rec.setAdapter(adapter);*/



    }

    private void fresh() {
        web.reload();
    }

    private void loadMore(String order){
        String path="http://wap.guoguo-app.com/wuliuDetail.htm?mailNo="+order;


        mSetting = web.getSettings();
        mSetting.setJavaScriptEnabled(true);
        // 自适应屏幕
        mSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //mSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //mSetting.setLoadWithOverviewMode(true);
        ///mSetting.setBlockNetworkImage(false);

        mSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mSetting.setUseWideViewPort(true);
        mSetting.setLoadWithOverviewMode(true);

        web.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发

                if (progress == 100) {
                    isHideLoading(true);
                    //ptrFrame.refreshComplete();
                }
                super.onProgressChanged(view, progress);
            }

        });

        web.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                isHideLoading(true);
                //ptrFrame.refreshComplete();
            }
        });
        web.loadUrl(path);
    }

    @Override
    public void initData() {
        super.initData();
        order = ((Order)getIntent().getSerializableExtra("order"));

        if (order != null) {
            String logisticsNumber = order.getLogisticsNumber();

            if (TextUtils.isEmpty(logisticsNumber)){
                transport_order.setText("暂无订单号");
            }else {
                transport_order.setText(logisticsNumber);
                loadMore(order.getLogisticsNumber());
            }
        }else {
            transport_order.setText("暂无订单号");
            //loadMore("3947521506486");
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        x.view().inject(this);
        initView();
        initData();
    }

    class Holder extends RecyclerView.ViewHolder{

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
