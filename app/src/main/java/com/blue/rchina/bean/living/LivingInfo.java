package com.blue.rchina.bean.living;

import java.io.Serializable;

/**
 * Created by cj on 2018/9/12.
 */

public class LivingInfo implements Serializable {


    /**
     * distance : 0
     * liveState : 1
     * liveVideo : {}
     * anchor : {"headIcon":"http://smartcity.blueapp.com.cn:8088/smartchina/static/images/upload/37dccdf5b54149179603318abbeb1df8_file.png","lon":123,"location":"北京","nickName":"Vode","lat":123}
     * live : {"httpPullUrl":"http://flv88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8.flv?netease=flv88ce13b9.live.126.net","picsrc":"","hlsPullUrl":"http://pullhls88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8/playlist.m3u8","rtmpPullUrl":"rtmp://v88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8","beginTime":"28分钟前","channeName":"vode","pushUrl":"rtmp://p88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8?wsSecret=ad6c7f86c9273bbb6fd08c20600df729&wsTime=1536721563"}
     */

    private int distance;
    private int liveState;
    private LiveVideoBean liveVideo;
    private AnchorBean anchor;
    private LiveBean live;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getLiveState() {
        return liveState;
    }

    public void setLiveState(int liveState) {
        this.liveState = liveState;
    }

    public LiveVideoBean getLiveVideo() {
        return liveVideo;
    }

    public void setLiveVideo(LiveVideoBean liveVideo) {
        this.liveVideo = liveVideo;
    }

    public AnchorBean getAnchor() {
        return anchor;
    }

    public void setAnchor(AnchorBean anchor) {
        this.anchor = anchor;
    }

    public LiveBean getLive() {
        return live;
    }

    public void setLive(LiveBean live) {
        this.live = live;
    }

    public static class LiveVideoBean implements Serializable{
        private String videoName;
        private String createTime;
        private String videoUrl;
        private String duration;
        private String snapshotUrl;
        private String initialSize;

        private int width;
        private int height;
        /**
         * height : 640
         * width : 480
         * channeName : 测试直播封面
         */

        private String channeName;


        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getSnapshotUrl() {
            return snapshotUrl;
        }

        public void setSnapshotUrl(String snapshotUrl) {
            this.snapshotUrl = snapshotUrl;
        }

        public String getInitialSize() {
            return initialSize;
        }

        public void setInitialSize(String initialSize) {
            this.initialSize = initialSize;
        }

        public String getChanneName() {
            return channeName;
        }

        public void setChanneName(String channeName) {
            this.channeName = channeName;
        }
    }

    public static class AnchorBean implements Serializable{
        /**
         * headIcon : http://smartcity.blueapp.com.cn:8088/smartchina/static/images/upload/37dccdf5b54149179603318abbeb1df8_file.png
         * lon : 123
         * location : 北京
         * nickName : Vode
         * lat : 123
         */


        private String headIcon;
        private int lon;
        private String location;
        private String nickName;
        private int lat;

        public String getHeadIcon() {
            return headIcon;
        }

        public void setHeadIcon(String headIcon) {
            this.headIcon = headIcon;
        }

        public int getLon() {
            return lon;
        }

        public void setLon(int lon) {
            this.lon = lon;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getLat() {
            return lat;
        }

        public void setLat(int lat) {
            this.lat = lat;
        }
    }

    public static class LiveBean implements Serializable{
        /**
         * httpPullUrl : http://flv88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8.flv?netease=flv88ce13b9.live.126.net
         * picsrc :
         * hlsPullUrl : http://pullhls88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8/playlist.m3u8
         * rtmpPullUrl : rtmp://v88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8
         * beginTime : 28分钟前
         * channeName : vode
         * pushUrl : rtmp://p88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8?wsSecret=ad6c7f86c9273bbb6fd08c20600df729&wsTime=1536721563
         */

        private String httpPullUrl;
        private String picsrc;
        private String hlsPullUrl;
        private String rtmpPullUrl;
        private String beginTime;
        private String channeName;
        private String pushUrl;
        private String cid;


        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getHttpPullUrl() {
            return httpPullUrl;
        }

        public void setHttpPullUrl(String httpPullUrl) {
            this.httpPullUrl = httpPullUrl;
        }

        public String getPicsrc() {
            return picsrc;
        }

        public void setPicsrc(String picsrc) {
            this.picsrc = picsrc;
        }

        public String getHlsPullUrl() {
            return hlsPullUrl;
        }

        public void setHlsPullUrl(String hlsPullUrl) {
            this.hlsPullUrl = hlsPullUrl;
        }

        public String getRtmpPullUrl() {
            return rtmpPullUrl;
        }

        public void setRtmpPullUrl(String rtmpPullUrl) {
            this.rtmpPullUrl = rtmpPullUrl;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getChanneName() {
            return channeName;
        }

        public void setChanneName(String channeName) {
            this.channeName = channeName;
        }

        public String getPushUrl() {
            return pushUrl;
        }

        public void setPushUrl(String pushUrl) {
            this.pushUrl = pushUrl;
        }
    }
}
