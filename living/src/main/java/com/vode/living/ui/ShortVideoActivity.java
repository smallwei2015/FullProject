package com.vode.living.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.netease.neliveplayer.playerkit.common.log.LogUtil;
import com.netease.neliveplayer.playerkit.sdk.PlayerManager;
import com.netease.neliveplayer.playerkit.sdk.VodPlayer;
import com.netease.neliveplayer.playerkit.sdk.VodPlayerObserver;
import com.netease.neliveplayer.playerkit.sdk.model.MediaInfo;
import com.netease.neliveplayer.playerkit.sdk.model.StateInfo;
import com.netease.neliveplayer.playerkit.sdk.model.VideoBufferStrategy;
import com.netease.neliveplayer.playerkit.sdk.model.VideoOptions;
import com.netease.neliveplayer.playerkit.sdk.model.VideoScaleMode;
import com.netease.neliveplayer.playerkit.sdk.view.AdvanceSurfaceView;
import com.netease.neliveplayer.sdk.NELivePlayer;
import com.vode.living.R;
import com.vode.living.widget.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

public class ShortVideoActivity extends AppCompatActivity {

    private String TAG = "ShortVideoActivity";

    private VerticalViewPager mViewPager;
    private VodPagerAdapter mPagerAdapter;
    private AdvanceSurfaceView surfaceView; //或者使用 AdvanceMultiTextureView
    private int mCurrentPosition = 0;
    private VodPlayer player;
    private PlayerInfo playerInfo;
    private List<String> mLiveUrlList;
    private boolean isErrorShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_video);
        initData();
        initView();
    }


    @Override
    public void onPause() {
        super.onPause();

        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.setMute(true);
            player.registerPlayerObserver(playerObserver, false);
            player.setupRenderView(null, VideoScaleMode.NONE);
            player = null;
        }
        mPagerAdapter.destroy();
    }

    private void initData() {
        mLiveUrlList = new ArrayList<>();
        mLiveUrlList.add("http://vodhj5bqn44.vod.126.net/vodhj5bqn44/1BrIAtvV_1818587477_shd.mp4");
        mLiveUrlList.add("http://vodhj5bqn44.vod.126.net/vodhj5bqn44/FmdVOTqd_1818586962_shd.mp4");
        mLiveUrlList.add("http://vodhj5bqn44.vod.126.net/vodhj5bqn44/wq1e35cQ_1818588221_shd.mp4");

    }

    private void initView() {

        mViewPager = (VerticalViewPager) findViewById(R.id.view_pager);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "mVerticalViewPager, onPageSelected position = " + position);
                mCurrentPosition = position;
                // 滑动界面，首先让之前的播放器暂停，并seek到0
                Log.i(TAG, "滑动后，让之前的播放器暂停，player = " + player);
                if (player != null) {
                    player.seekTo(0);
                    player.pause();
                }
                playerInfo = mPagerAdapter.findPlayerInfo(mCurrentPosition);
                if (playerInfo != null && !playerInfo.vodPlayer.isPlaying()) {
                    Log.i(TAG, "play start,transformPage position " + mCurrentPosition);
                    surfaceView = playerInfo.playerView;
                    playerInfo.vodPlayer.setMute(false);
                    //当前页面视频缓存100m
                    playerInfo.vodPlayer.setBufferSize(100 * 1024 * 1024);
                    playerInfo.vodPlayer.start();
                    player = playerInfo.vodPlayer;
                }
            }
        });

        mPagerAdapter = new VodPagerAdapter(mLiveUrlList);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
    }


    public class VodPagerAdapter extends PagerAdapter {
        private ArrayList<PlayerInfo> playerInfoList = new ArrayList<>();
        private List<String> liveUrlList;


        public VodPagerAdapter(List<String> liveUrlList) {
            this.liveUrlList = liveUrlList;
        }

        @Override
        public int getCount() {
            if (liveUrlList == null || liveUrlList.size() < 1) {
                return 0;
            }
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i(TAG, "instantiateItem " + position);

            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_layout, null);
            view.setId(position);


            // 获取此player
            PlayerInfo playerInfo = instantiatePlayerInfo(position);
            AdvanceSurfaceView playView = (AdvanceSurfaceView) view.findViewById(R.id.live_texture);
            playerInfo.playerView = playView;
            playerInfo.vodPlayer.setupRenderView(playView, VideoScaleMode.FULL);

            container.addView(view);

            return view;
        }

        protected PlayerInfo instantiatePlayerInfo(int position) {
            Log.i(TAG, "instantiatePlayerInfo " + position);
            PlayerInfo playerInfo = new PlayerInfo();
            VideoOptions options = new VideoOptions();
            options.bufferStrategy = VideoBufferStrategy.ANTI_JITTER;
            options.loopCount = -1;
            options.isNetReconnect = true;
            VodPlayer vodPlayer = PlayerManager.buildVodPlayer(ShortVideoActivity.this, liveUrlList.get(position % liveUrlList.size()), options);
            vodPlayer.registerPlayerObserver(playerObserver, true);
            vodPlayer.start();
            playerInfo.playURL = liveUrlList.get(position % liveUrlList.size());
            playerInfo.vodPlayer = vodPlayer;
            playerInfo.position = position;
            playerInfoList.add(playerInfo);
            return playerInfo;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i(TAG, "destroyItem" + position);
            destroyPlayerInfo(position);
            container.removeView((View) object);
        }

        public void destroy() {
            for (PlayerInfo playerInfo : playerInfoList) {
                if (playerInfo == null || playerInfo.vodPlayer == null) {
                    return;
                }
                playerInfo.vodPlayer.stop();
                playerInfo.vodPlayer = null;
            }
            playerInfoList.clear();
            playerInfoList = null;
        }


        public void destroyPlayerInfo(int position) {
            PlayerInfo playerInfo = findPlayerInfo(position);
            if (playerInfo == null || playerInfo.vodPlayer == null) {
                return;
            }
            playerInfo.vodPlayer.stop();
            playerInfo.vodPlayer = null;
            playerInfoList.remove(playerInfo);

            Log.i(TAG, "play stop,destroyPlayerInfo position " + position);
        }

        public void pausePlayerInfo(int position) {
            PlayerInfo playerInfo = findPlayerInfo(position);
            if (playerInfo == null || playerInfo.vodPlayer == null) {
                return;
            }
            if (playerInfo.vodPlayer.isPlaying()) {
                playerInfo.vodPlayer.setMute(true);
                playerInfo.vodPlayer.pause();
                Log.i(TAG, "play pause,pausePlayerInfo position " + position);
            }

        }

        public void pauseOtherPlayerInfo(int position) {
            for (PlayerInfo playerInfo : playerInfoList) {
                if (playerInfo.position != position && playerInfo.vodPlayer.isPlaying()) {
                    playerInfo.vodPlayer.setMute(true);
                    //其他页面视频缓存20m
                    playerInfo.vodPlayer.setBufferSize(20 * 1024 * 1024);
                    playerInfo.vodPlayer.pause();
                    LogUtil.i(TAG, "play pause,pausePlayerInfo position " + playerInfo.position);
                }
            }

        }


        public PlayerInfo findPlayerInfo(int position) {
            for (int i = 0; i < playerInfoList.size(); i++) {
                PlayerInfo playerInfo = playerInfoList.get(i);
                if (playerInfo.position == position) {
                    return playerInfo;
                }
            }
            return null;
        }

    }


    private class PlayerInfo {
        public VodPlayer vodPlayer;
        public String playURL;
        public AdvanceSurfaceView playerView;
        public int position;
    }

    private VodPlayerObserver playerObserver = new VodPlayerObserver() {
        @Override
        public void onCurrentPlayProgress(long currentPosition, long duration, float percent, long cachedPosition) {

        }

        @Override
        public void onSeekCompleted() {
            Log.d(TAG, "onSeekCompleted");
        }

        @Override
        public void onCompletion() {

        }

        @Override
        public void onAudioVideoUnsync() {
//            showToast("音视频不同步");

        }

        @Override
        public void onNetStateBad() {

        }

        @Override
        public void onDecryption(int ret) {
            Log.i(TAG, "onDecryption ret = " + ret);
        }

        @Override
        public void onPreparing() {

        }

        @Override
        public void onPrepared(MediaInfo info) {
            Log.i(TAG, "MediaInfo info = " + info);
        }

        @Override
        public void onError(int code, int extra) {
            Log.i(TAG, "player mCurrentPosition:" + mCurrentPosition + " ,onError code:" + code + " extra:" + extra);
            mPagerAdapter.pauseOtherPlayerInfo(mCurrentPosition);
            if (!isErrorShow && !mPagerAdapter.findPlayerInfo(mCurrentPosition).vodPlayer.isPlaying()) {
                isErrorShow = true;
                //发生错误时，这里Demo会弹出对话框提示错误，用户可以在此根据错误码进行重试或者其他操作
                AlertDialog.Builder build = new AlertDialog.Builder(ShortVideoActivity.this);
                build.setTitle("播放错误").setMessage("错误码：" + code)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                isErrorShow = false;
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        }

        @Override
        public void onFirstVideoRendered() {
            Log.i(TAG, "onFirstVideoRendered，mCurrentPosition->" + mCurrentPosition);
            mPagerAdapter.pauseOtherPlayerInfo(mCurrentPosition);
        }

        @Override
        public void onFirstAudioRendered() {
            Log.i(TAG, "onFirstAudioRendered ");

        }

        @Override
        public void onBufferingStart() {
//                mBuffer.setVisibility(View.VISIBLE);

        }

        @Override
        public void onBufferingEnd() {
//                mBuffer.setVisibility(View.GONE);

        }

        @Override
        public void onBuffering(int percent) {
//            Log.d(TAG, "缓冲中..." + percent + "%");
        }

        @Override
        public void onHardwareDecoderOpen() {

        }

        @Override
        public void onStateChanged(StateInfo stateInfo) {

        }

        @Override
        public void onHttpResponseInfo(int code, String header) {
            Log.i(TAG, "onHttpResponseInfo,code:" + code + " header:" + header);
        }

        @Override
        public void onVideoFrameFilter(final NELivePlayer.NEVideoRawData videoRawData) {

        }

        @Override
        public void onAudioFrameFilter(final NELivePlayer.NEAudioRawData audioRawData) {

        }
    };

    private void showToast(String msg) {
        Log.d(TAG, "showToast" + msg);
        try {
            Toast.makeText(ShortVideoActivity.this, msg, Toast.LENGTH_SHORT).show();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
