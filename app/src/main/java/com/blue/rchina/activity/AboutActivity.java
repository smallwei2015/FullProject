package com.blue.rchina.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class AboutActivity extends BaseActivity {

    @ViewInject(R.id.about_version)
    TextView version;
    @ViewInject(R.id.toolbar)
    View toolbar;
    @ViewInject(R.id.qrcode)
    ImageView qrCode;

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"关于",-1,R.color.transparent);

        int statusBarHeight = UIUtils.getStatusBarHeight(mActivity);
        if (statusBarHeight>0){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin+statusBarHeight,0,0);
        }

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);

            version.setText("当前版本："+info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        qrCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(UrlUtils.N_DOWNLOAD));
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        x.view().inject(this);

        initView();
        initData();
    }
}
