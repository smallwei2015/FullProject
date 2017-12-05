package com.blue.rchina.bean;

import java.io.Serializable;

/**
 * Created by cj on 2017/11/17.
 */

public class Nearby implements Serializable {
    private String franchiseeId;
    private String title;
    private String desc;
    private int franchiseeType;
    private String phone;
    private String location;
    private String distance;
    private String picsrc;

    public String getFranchiseeId() {
        return franchiseeId;
    }

    public void setFranchiseeId(String franchiseeId) {
        this.franchiseeId = franchiseeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getFranchiseeType() {
        return franchiseeType;
    }

    public void setFranchiseeType(int franchiseeType) {
        this.franchiseeType = franchiseeType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPicsrc() {
        return picsrc;
    }

    public void setPicsrc(String picsrc) {
        this.picsrc = picsrc;
    }
}
