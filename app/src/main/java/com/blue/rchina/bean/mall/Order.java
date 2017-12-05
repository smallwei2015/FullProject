package com.blue.rchina.bean.mall;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cj on 2017/7/10.
 */

public class Order implements Serializable {
    private int id;
    private int type=0;
    private int orderFlag;
    private String orderNo;
    private long orderId;
    private double moneySum;
    private List<OrderGoods> goodsInfo;

    private String receiveName;
    private String receiveAddress;
    private String receivePhone;
    private String district;
    private String datetime;

    private String logisticsNumber;


    /*  "orderFlag": 0,
            "orderNo": "B101500313431",
            "moneySum": 0.01,
            "receiveName": "Vode",
            "receiveAddress": "哈哈哈",
            "receivePhone": "123456",
            "district": "北京市东城区",
            "datetime": "2017-07-18 18:31:04",
            "orderId": 28
        */

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderFlag() {
        return orderFlag;
    }

    public void setOrderFlag(int orderFlag) {
        this.orderFlag = orderFlag;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public double getMoneySum() {
        return moneySum;
    }

    public void setMoneySum(double moneySum) {
        this.moneySum = moneySum;
    }

    public List<OrderGoods> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<OrderGoods> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }
}
