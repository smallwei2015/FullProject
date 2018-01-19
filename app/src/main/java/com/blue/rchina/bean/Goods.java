package com.blue.rchina.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by cj on 2017/6/26.
 */

@Table(name = "goods")
public class Goods implements Serializable{

    @Column(name = "id",autoGen = true,isId = true)
    private int id;
    @Column(name = "goodsId")
    private int goodsId;
    @Column(name = "title")
    private String title;
    @Column(name = "desc")
    private String desc;
    @Column(name = "price")
    private double price;
    @Column(name = "params")
    private String params;
    @Column(name = "picsrc")
    private String picsrc;
    @Column(name = "stockCount")
    private double stockCount;
    @Column(name = "upDatetime")
    private String upDatetime;


    private String DCTime;
    private String attention;

    private String shareLink;
    private int layoutType=1;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getPicsrc() {
        return picsrc;
    }

    public void setPicsrc(String picsrc) {
        this.picsrc = picsrc;
    }

    public double getStockCount() {
        return stockCount;
    }

    public void setStockCount(double stockCount) {
        this.stockCount = stockCount;
    }

    public String getUpDatetime() {
        return upDatetime;
    }

    public void setUpDatetime(String upDatetime) {
        this.upDatetime = upDatetime;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getDCTime() {
        return DCTime;
    }

    public void setDCTime(String DCTime) {
        this.DCTime = DCTime;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }
}
