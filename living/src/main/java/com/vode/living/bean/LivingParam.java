package com.vode.living.bean;

import com.netease.LSMediaCapture.lsMediaCapture;
import com.netease.vcloud.video.effect.VideoEffect;

import java.io.Serializable;

import static com.netease.LSMediaCapture.lsMediaCapture.FormatType.RTMP;
import static com.netease.LSMediaCapture.lsMediaCapture.StreamType.AV;

/**
 * Created by cj on 2018/9/11.
 */

public class LivingParam implements Serializable {
    String pushUrl = null; //推流地址
    lsMediaCapture.StreamType streamType = AV;  // 推流类型
    lsMediaCapture.FormatType formatType = RTMP; // 推流格式
    String recordPath; //文件录制地址，当formatType 为 MP4 或 RTMP_AND_MP4 时有效
    lsMediaCapture.VideoQuality videoQuality = lsMediaCapture.VideoQuality.SUPER; //清晰度
    boolean isScale_16x9 = false; //是否强制16:9
    boolean useFilter = true; //是否使用滤镜
    VideoEffect.FilterType filterType = VideoEffect.FilterType.clean; //滤镜类型
    boolean frontCamera = false; //是否默认前置摄像头
    boolean watermark = false; //是否添加水印
    boolean qosEnable = true;  //是否开启QOS
    int qosEncodeMode = 1; // 1:流畅优先, 2:清晰优先
    boolean graffitiOn = false; //是否添加涂鸦
    boolean uploadLog = true; //是否上传SDK运行日志


    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public boolean isUploadLog() {
        return uploadLog;
    }

    public void setUploadLog(boolean uploadLog) {
        this.uploadLog = uploadLog;
    }

    public lsMediaCapture.StreamType getStreamType() {
        return streamType;
    }

    public void setStreamType(lsMediaCapture.StreamType streamType) {
        this.streamType = streamType;
    }

    public lsMediaCapture.FormatType getFormatType() {
        return formatType;
    }

    public void setFormatType(lsMediaCapture.FormatType formatType) {
        this.formatType = formatType;
    }

    public String getRecordPath() {
        return recordPath;
    }

    public void setRecordPath(String recordPath) {
        this.recordPath = recordPath;
    }

    public lsMediaCapture.VideoQuality getVideoQuality() {
        return videoQuality;
    }

    public void setVideoQuality(lsMediaCapture.VideoQuality videoQuality) {
        this.videoQuality = videoQuality;
    }

    public boolean isScale_16x9() {
        return isScale_16x9;
    }

    public void setScale_16x9(boolean scale_16x9) {
        isScale_16x9 = scale_16x9;
    }

    public boolean isUseFilter() {
        return useFilter;
    }

    public void setUseFilter(boolean useFilter) {
        this.useFilter = useFilter;
    }

    public VideoEffect.FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(VideoEffect.FilterType filterType) {
        this.filterType = filterType;
    }

    public boolean isFrontCamera() {
        return frontCamera;
    }

    public void setFrontCamera(boolean frontCamera) {
        this.frontCamera = frontCamera;
    }

    public boolean isWatermark() {
        return watermark;
    }

    public void setWatermark(boolean watermark) {
        this.watermark = watermark;
    }

    public boolean isQosEnable() {
        return qosEnable;
    }

    public void setQosEnable(boolean qosEnable) {
        this.qosEnable = qosEnable;
    }

    public int getQosEncodeMode() {
        return qosEncodeMode;
    }

    public void setQosEncodeMode(int qosEncodeMode) {
        this.qosEncodeMode = qosEncodeMode;
    }

    public boolean isGraffitiOn() {
        return graffitiOn;
    }

    public void setGraffitiOn(boolean graffitiOn) {
        this.graffitiOn = graffitiOn;
    }
}
