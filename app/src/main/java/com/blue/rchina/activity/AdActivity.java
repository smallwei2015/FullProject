package com.blue.rchina.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class AdActivity extends AppCompatActivity {


    public static final String APP_ID = "SDK20171512030632690fn35schyj25t";
    public static final String keySet[] = new String[]{APP_ID};
    public RelativeLayout container;
    private boolean isJumping;
    private boolean adClicked=false;
    private boolean isDisplying=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        container = (RelativeLayout) findViewById(R.id.activity_ad);

        init();
        startAd();

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
                jump();
            }

            @Override
            public void onAdRecieved(String s) {
                /*判断三秒钟是否还未显示，那么就跳过*/
                container.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isDisplying) {
                            jump();
                        }
                    }
                }, 3000);
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
}
