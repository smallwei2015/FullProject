package com.blue.rchina.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by cj on 2017/6/27.
 */

@Table(name = "user")
public class User implements Serializable{

    @Column(name = "appuserId",isId = true,autoGen = false)
    private long appuserId;
    @Column(name = "phone")
    private String phone;
    @Column(name = "nickName")
    private String nickName;
    @Column(name = "userName")
    private String userName;
    @Column(name = "headIcon")
    private String headIcon;
    @Column(name = "sex")
    private int sex;//1男2女3没有选过
    @Column(name = "recommendCode")
    private String recommendCode;
    @Column(name = "expiration")
    private long expiration;
    @Column(name = "userType")
    private int userType;
    @Column(name = "communityName")
    private String communityName;

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public long getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(long appuserId) {
        this.appuserId = appuserId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getRecommendCode() {
        return recommendCode;
    }

    public void setRecommendCode(String recommendCode) {
        this.recommendCode = recommendCode;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
}
