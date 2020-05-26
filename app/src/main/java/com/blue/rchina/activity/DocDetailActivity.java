package com.blue.rchina.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.DocBean;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.FileUtils;
import com.blue.rchina.utils.OpenFileUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.blue.rchina.views.BasePopUpWindow;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

public class DocDetailActivity extends BaseActivity {

    public DocBean data;
    public WebView web;
    private WebSettings mSettings;
    private BasePopUpWindow mWindow;
    private PopupMenu mPopupMenu;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_detail);

        initData();
        initView();
    }


    @Override
    public void initData() {
        super.initData();
        data = ((DocBean) getIntent().getSerializableExtra("data"));
    }

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"文档预览",R.mipmap.menu_icon);

        web = ((WebView) findViewById(R.id.web));
        mSettings = web.getSettings();
        mSettings.setJavaScriptEnabled(true);
        mSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mSettings.setDisplayZoomControls(false);
        mSettings.setUseWideViewPort(true);
        mSettings.setLoadWithOverviewMode(true);


        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient());

        web.loadUrl(data.getWenkuLink());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            web.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (!web.canScrollVertically(1)) {
                        UIUtils.showToast("预览结束，请前往下载查看全部!");
                    }
                }
            });
        }

    }


    @Override
    public void onRightIconClick(View view) {
        super.onRightIconClick(view);


        mPopupMenu = new PopupMenu(mActivity, view);
        // 获取布局文件
        mPopupMenu.getMenuInflater().inflate(R.menu.web_menu, mPopupMenu.getMenu());
        mPopupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件

                switch (item.getItemId()){
                    case R.id.share:
                        buy();
                        break;
                    case R.id.collect:
                        download();
                        break;
                }
                mPopupMenu.dismiss();
                return true;
            }
        });
        mPopupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // 控件消失时的事件
                menu.dismiss();
            }
        });

    }

    private void download() {
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("提示：");
        mProgressDialog.setMessage("下载中请稍等！");
        mProgressDialog.show();

        RequestParams params = new RequestParams(data.getFileUrl());

        //自定义保存路径，Environment.getExternalStorageDirectory()：SD卡的根目录
        params.setSaveFilePath(FileUtils.DOWNLOAD+data.getFileName());

        //自动为文件命名
        params.setAutoRename(true);

        x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override

            public void onSuccess(File result) {

                //apk下载完成后，调用系统的安装方法
                mProgressDialog.dismiss();

                /*Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");

                startActivity(intent);*/

                OpenFileUtils.openFile(mActivity,result);

            }

            @Override

            public void onError(Throwable ex, boolean isOnCallback) {
                mProgressDialog.dismiss();
                Log.w("666667",ex.getMessage());
            }

            @Override

            public void onCancelled(CancelledException cex) {
                mProgressDialog.dismiss();
                Log.w("66666",cex.getMessage());
            }

            @Override

            public void onFinished() {

            }

            //网络请求之前回调

            @Override

            public void onWaiting() {

            }

            //网络请求开始的时候回调

            @Override

            public void onStarted() {

            }

            //下载的时候不断回调的方法

            @Override

            public void onLoading(long total, long current, boolean isDownloading) {

                //当前进度和文件总大小

                Log.i("JAVA","current："+ current +"，total："+total);

            }

        });
    }

    private void buy() {
        if (data.getPrice()>0){
            RequestParams entity = new RequestParams(UrlUtils.N_getRechargeOrder);
            entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
            entity.addBodyParameter("arg1", data.getPrice()+"");
            x.http().post(entity, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {

                    Log.w("666",result);

                    JSONObject object = JSON.parseObject(result);
                    if (object.getInteger("result") == 200) {

                        String orderNo = object.getString("orderNo");

                        Intent intent = new Intent(mActivity, PayActivity.class);

                        intent.putExtra("id", orderNo);
                        intent.putExtra("money", data.getPrice());
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
        }else {
            UIUtils.showToast("免费资源无需购买！");
        }
    }
}
