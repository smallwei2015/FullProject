package com.blue.rchina.bean;

import java.util.List;

/**
 * Created by cj on 2017/3/22.
 */

public class Report extends BaseData {

    private List<Size> picsize;
    private int praiseCount;
    private String location;
    private int commentCount;
    private int reportId;
    private int appuserId;
    private List<User> praiseList;
    private String content;
    private String headIcon;
    private long distance;
    private String nickName;
    private String datetime;
    private List<String> manyPic;
    private int praiseState;

    private int displayType=0;


    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(int appuserId) {
        this.appuserId = appuserId;
    }

    public List<Size> getPicsize() {
        return picsize;
    }

    public void setPicsize(List<Size> picsize) {
        this.picsize = picsize;
    }

    public List<User> getPraiseList() {
        return praiseList;
    }

    public void setPraiseList(List<User> praiseList) {
        this.praiseList = praiseList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public List<String> getManyPic() {
        return manyPic;
    }

    public void setManyPic(List<String> manyPic) {
        this.manyPic = manyPic;
    }

    public int getPraiseState() {
        return praiseState;
    }

    public void setPraiseState(int praiseState) {
        this.praiseState = praiseState;
    }

    @Override
    public BaseData parseObject(String json) {
        return null;
    }

    @Override
    public List<BaseData> parseList(String jsonList) {
        return null;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }
}
