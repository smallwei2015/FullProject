package com.blue.rchina.bean.living;

/**
 * Created by cj on 2018/9/12.
 */

public class SaveResult {


    /**
     * ret : {"httpPullUrl":"http://flv88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8.flv?netease=flv88ce13b9.live.126.net","hlsPullUrl":"http://pullhls88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8/playlist.m3u8","rtmpPullUrl":"rtmp://v88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8","name":"vode","cid":"c4e570a5db2345d08cfc95ceebc268e8","ctime":1536721563472,"pushUrl":"rtmp://p88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8?wsSecret=ad6c7f86c9273bbb6fd08c20600df729&wsTime=1536721563"}
     * requestId : livef1273a7c5da3431a8c64bdfbdd29f9ef
     * code : 200
     */

    private RetBean ret;
    private String requestId;
    private int code;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RetBean getRet() {
        return ret;
    }

    public void setRet(RetBean ret) {
        this.ret = ret;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class RetBean {
        /**
         * httpPullUrl : http://flv88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8.flv?netease=flv88ce13b9.live.126.net
         * hlsPullUrl : http://pullhls88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8/playlist.m3u8
         * rtmpPullUrl : rtmp://v88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8
         * name : vode
         * cid : c4e570a5db2345d08cfc95ceebc268e8
         * ctime : 1536721563472
         * pushUrl : rtmp://p88ce13b9.live.126.net/live/c4e570a5db2345d08cfc95ceebc268e8?wsSecret=ad6c7f86c9273bbb6fd08c20600df729&wsTime=1536721563
         */

        private String httpPullUrl;
        private String hlsPullUrl;
        private String rtmpPullUrl;
        private String name;
        private String cid;
        private long ctime;
        private String pushUrl;

        public String getHttpPullUrl() {
            return httpPullUrl;
        }

        public void setHttpPullUrl(String httpPullUrl) {
            this.httpPullUrl = httpPullUrl;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public String getPushUrl() {
            return pushUrl;
        }

        public void setPushUrl(String pushUrl) {
            this.pushUrl = pushUrl;
        }
    }
}
