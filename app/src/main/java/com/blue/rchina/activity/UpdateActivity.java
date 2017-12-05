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
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class UpdateActivity extends BaseActivity {

    @ViewInject(R.id.update_version)
    TextView version;

    @ViewInject(R.id.update_qrcode)
    ImageView imageView;

    private String appVersion;

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"检测更新",-1,R.color.transparent);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
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

    public void initData() {
        // 获取当前版本号

        try {
            PackageManager manager =getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            appVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version.setText("版本号：" + appVersion);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        x.view().inject(this);
        initView();
        initData();
    }
}
