package com.blue.rchina.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.blue.rchina.R;
import com.kyview.InitConfiguration;
import com.kyview.interfaces.AdViewSpreadListener;
import com.kyview.manager.AdViewBannerManager;
import com.kyview.manager.AdViewInstlManager;
import com.kyview.manager.AdViewNativeManager;
import com.kyview.manager.AdViewSpreadManager;
import com.kyview.manager.AdViewVideoManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

public class AdActivity extends AppCompatActivity {


    //public static final String APP_ID = "SDK20171505030508aeipv2k3umhdk04";
    //public static final String APP_ID="SDK20171021101248glctd8nqbv4slic";
    public static final String APP_ID="SDK20171528030650grw6gsq6irtk4b3";
    public static final String keySet[] = new String[]{APP_ID};
    public RelativeLayout container;
    private boolean isJumping;
    private boolean adClicked=false;
    private boolean isDisplying=false;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        /*处理js打开方式*/
        parseJsData();


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        container = (RelativeLayout) findViewById(R.id.activity_ad);

        if(!AndPermission.hasPermission(this,new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
            AndPermission.with(this)
                    .requestCode(200)
                    .permission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .rationale(new RationaleListener() {
                        @Override
                        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                            AndPermission.defaultSettingDialog(AdActivity.this, 400).show();
                        }
                    }).callback(this).start();
        }else {
            init();
            startAd();
        }


    }

    private void parseJsData() {

        String TAG="vode";
        Intent intent =getIntent();
        if (intent != null) {
            Log.e(TAG, "scheme:" +intent.getScheme());
            Uri uri =intent.getData();
            if (uri != null) {
                Log.e(TAG, "scheme: "+uri.getScheme());
                Log.e(TAG, "host: "+uri.getHost());
                Log.e(TAG, "port: "+uri.getPort());
                Log.e(TAG, "path: "+uri.getPath());
                Log.e(TAG, "queryString: "+uri.getQuery().length());
                Log.e(TAG, "queryParameter: "+uri.getQueryParameter("contentId"));
            }

        }


    }

    @PermissionYes(200)
    private void getPermissionYes(List<String> grantedPermissions) {
        // TODO 申请权限成功。
        init();
        startAd();
    }

    @PermissionNo(200)
    private void getPermissionNo(List<String> deniedPermissions) {
        // TODO 申请权限失败。
        AndPermission.defaultSettingDialog(this, 400).show();
    }

    private void startAd() {

        // 设置开屏下方LOGO，必须调用该方法
        AdViewSpreadManager.getInstance(this).setSpreadLogo(R.mipmap.ad_banner);
        //AdViewSpreadManager.getInstance(this).setSpreadBackgroudPic(R.mipmap.banner);
        // 设置开屏背景颜色，可不设置
        AdViewSpreadManager.getInstance(this).setSpreadBackgroudColor(
                Color.WHITE);
        // 请求开屏广告
        AdViewSpreadListener adViewAdInterface = new AdViewSpreadListener() {
            @Override
            public void onAdDisplay(String s) {
                isDisplying=true;
            }

            @Override
            public void onAdClose(String s) {
                /*显示两秒后关闭*/
                container.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jump();
                    }
                }, 2000);
            }

            @Override
            public void onAdRecieved(String s) {

            }

            @Override
            public void onAdClick(String s) {
                adClicked=true;
            }

            @Override
            public void onAdFailed(String s) {
                jump();
            }

            @Override
            public void onAdSpreadNotifyCallback(String s, ViewGroup viewGroup, int i, int i1) {

            }
        };
        AdViewSpreadManager.getInstance(this).request(this, APP_ID, adViewAdInterface,
                container);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adClicked){
            jump();
        }
    }

    private void jump() {

        if (!isJumping) {
            isJumping = true;
            Intent intent = new Intent(AdActivity.this, FlashActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void init() {
        // 请求广告之前请务必先进行初始化，否则将无法使用广告
        // 设置广告请求配置参数，可使用默认配置InitConfiguration.createDefault(this);
        InitConfiguration initConfig = new InitConfiguration.Builder(this)
                .setUpdateMode(InitConfiguration.UpdateMode.EVERYTIME) // 实时获取配置，非必须写
                .setBannerCloseble(InitConfiguration.BannerSwitcher.CANCLOSED) //横幅可关闭按钮
                .setRunMode(InitConfiguration.RunMode.TEST) //测试模式时log更多，方便调试，不影响竞价展示正式
                .build();
        // 初始化横幅、插屏、原生、开屏、视频的广告配置，必须写
        AdViewBannerManager.getInstance(this).init(initConfig,keySet);
        AdViewInstlManager.getInstance(this).init(initConfig,keySet);
        AdViewNativeManager.getInstance(this).init(initConfig,keySet);
        AdViewSpreadManager.getInstance(this).init(initConfig,keySet);
        AdViewVideoManager.getInstance(this).init(initConfig,keySet);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 400: { // 这个400就是你上面传入的数字。
                // 你可以在这里检查你需要的权限是否被允许，并做相应的操作。
                if (AndPermission.hasPermission(AdActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                    init();
                    startAd();
                }else {
                    AndPermission.defaultSettingDialog(AdActivity.this, 400).show();
                }

                break;
            }
        }
    }
}
