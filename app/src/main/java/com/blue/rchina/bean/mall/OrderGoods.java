package com.blue.rchina.bean.mall;

import java.io.Serializable;

/**
 * Created by cj on 2017/7/18.
 */

public class OrderGoods implements Serializable {

    private int count;
    private String desc;
    private int goodsId;
    private String params;
    private String picsrc;
    private double price;
    private String title;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
