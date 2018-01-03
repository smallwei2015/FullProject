package com.blue.rchina.bean;

import java.io.Serializable;

/**
 * Created by cj on 2017/12/4.
 */

public class Trave implements Serializable {

    private String scenicId;
    private String title;
    private String desc;

    private String picsrc;
    private String location;
    private String phone;
    private String openTime;
    private String shareLink;
    private double price;

    private int monthSales;

    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
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

    public String getPicsrc() {
        return picsrc;
    }

    public void setPicsrc(String picsrc) {
        this.picsrc = picsrc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(int monthSales) {
        this.monthSales = monthSales;
    }
}
