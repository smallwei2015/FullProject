package com.blue.rchina.bean;

import java.io.Serializable;

/**
 * Created by cj on 2018/8/2.
 */

public class OrderPrice implements Serializable {

    private int shopcartNum;
    private String coupon;
    private double freight;
    private double goodsMoney;
    private double moneySum;

    public int getShopcartNum() {
        return shopcartNum;
    }

    public void setShopcartNum(int shopcartNum) {
        this.shopcartNum = shopcartNum;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public double getGoodsMoney() {
        return goodsMoney;
    }

    public void setGoodsMoney(double goodsMoney) {
        this.goodsMoney = goodsMoney;
    }

    public double getMoneySum() {
        return moneySum;
    }

    public void setMoneySum(double moneySum) {
        this.moneySum = moneySum;
    }
}
