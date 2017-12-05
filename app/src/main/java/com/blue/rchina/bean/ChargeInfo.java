package com.blue.rchina.bean;

import java.io.Serializable;

/**
 * Created by cj on 2017/11/24.
 */

public class ChargeInfo implements Serializable {
    private String orderId;
    private String orderNo;
    private int orderFlag;
    private double moneySum;
    private String datetime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderFlag() {
        return orderFlag;
    }

    public void setOrderFlag(int orderFlag) {
        this.orderFlag = orderFlag;
    }

    public double getMoneySum() {
        return moneySum;
    }

    public void setMoneySum(double moneySum) {
        this.moneySum = moneySum;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
