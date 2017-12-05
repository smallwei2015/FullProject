package com.blue.rchina.bean;

import java.util.List;

/**
 * Created by cj on 2017/3/22.
 */

public class Comment extends BaseData{


    private String headIcon;
    private String phone;
    private String nickName;
    private String commentContent;
    private String datetime;
    private int appuserId;
    private int commentId;
    private String content;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(int appuserId) {
        this.appuserId = appuserId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    @Override
    public BaseData parseObject(String json) {
        return null;
    }

    @Override
    public List<BaseData> parseList(String jsonList) {
        return null;
    }
}
