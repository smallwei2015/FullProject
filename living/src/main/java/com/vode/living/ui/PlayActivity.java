package com.vode.living.ui;

import android.text.TextUtils;
import android.view.Surface;
import android.view.TextureView;

import com.netease.neliveplayer.sdk.NELivePlayer;
import com.netease.neliveplayer.sdk.NESDKConfig;
import com.netease.neliveplayer.sdk.constant.NEBufferStrategy;
import com.vode.living.R;

import java.io.IOException;
import java.util.Map;

public class PlayActivity extends LivingBaseActivity {

    private NELivePlayer.OnDataUploadListener mOnDataUploadListener;
    private NELivePlayer mLivePlayer;
    private TextureView container;
    private int mBufferStrategy = NEBufferStrategy.NELPLOWDELAY; //直播低延时

    private boolean mHardwareDecoder = false;
    public String url;

    @Override
    void initData() {
        super.initData();

        url = getIntent().getStringExtra("url");

        if (TextUtils.isEmpty(url)){
            url="http://jdvodlro4j3ya.vod.126.net/jdvodlro4j3ya/48be118827ba473f82fa74848b04bdcd_1536737959423_1536737972069_924642846-00000.mp4";
        }
    }

    @Override
    void initView() {
        super.initView();
        setContentView(R.layout.activity_play);

        container = (TextureView) findViewById(R.id.live_texture);
        initPlayer();
    }

    private void initPlayer() {
        NESDKConfig config = new NESDKConfig();
        config.dataUploadListener =  mOnDataUploadListener;
        NELivePlayer.init(this,config);

        NELivePlayer.OnDataUploadListener mOnDataUploadListener = new NELivePlayer.OnDataUploadListener() {
            @Override
            public boolean onDataUpload(String url, String data) {

                return true;
            }

            @Override
            public boolean onDocumentUpload(String url, Map<String, String> params, Map<String, String> filepaths) {
                return  true;
            }
        };

        mLivePlayer = NELivePlayer.create();


        mLivePlayer.setBufferStrategy(mBufferStrategy);
        mLivePlayer.setHardwareDecoder(mHardwareDecoder); // false 为软件解码， true 为硬件解码

        mLivePlayer.setOnPreparedListener(new NELivePlayer.OnPreparedListener() {
            @Override
            public void onPrepared(NELivePlayer mp) {

            }
        });
        /*mLivePlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
        mLivePlayer.setOnCompletionListener(mCompletionListener);
        mLivePlayer.setOnErrorListener(mErrorListener);
        mLivePlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
        mLivePlayer.setOnInfoListener(mInfoListener);
        mLivePlayer.setOnSeekCompleteListener(mSeekCompleteListener);
        mLivePlayer.setOnVideoParseErrorListener(mVideoParseErrorListener);
        mLivePlayer.setOnDefinitionListener(mOnDefinitionListener);
        mLivePlayer.setOnCurrentRealTimeListener(mIntervalTime,mOnCurrentRealTimeListener);
        mLivePlayer.setOnCurrentSyncTimestampListener(mIntervalTime, mOnCurrentSyncTimestampListener);
        mLivePlayer.setOnCurrentSyncContentListener( mOnCurrentSyncContentListener);
        mLivePlayer.setOnSubtitleListener(mOnSubtitleListener);*/

        try {
            mLivePlayer.setDataSource(url);//设置数据源，返回 true 成功，返回 false 失败
        } catch (IOException e) {
            e.printStackTrace();
        }

        //若用户显示的 View 继承 SurfaceView，则通过 setDisplay() 接口将 SurfaceHolder 传到底层用于显示。

        //mLivePlayer.setDisplay(container.getSurfaceTexture()); //设置显示surface

        //若用户显示的 View 继承 TextureView，则通过 setSurface() 接口将 SurfaceTexture 传到底层用于显示。

        mLivePlayer.setSurface(new Surface(container.getSurfaceTexture())); //设置显示surface

        mLivePlayer.prepareAsync();

        //预处理完成后，sdk会有一个回调，需要先注册一个 OnPreparedListener 获取准备完成的回调，步骤(4)已经注册了。

        NELivePlayer.OnPreparedListener mPreparedListener = new NELivePlayer.OnPreparedListener() {
            public void onPrepared(NELivePlayer mp) {
                mLivePlayer.start();
            }

        };
    }
}
