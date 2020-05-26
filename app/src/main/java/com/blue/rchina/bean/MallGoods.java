package com.blue.rchina.bean;

import java.util.List;

/**
 * Created by cj on 2018/5/25.
 */

public class MallGoods {

    private String message;
    private String result;
    private List<Goods> info;
    private List<Goods> hot;
    private int shopcarNum;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Goods> getInfo() {
        return info;
    }

    public void setInfo(List<Goods> info) {
        this.info = info;
    }

    public int getShopcarNum() {
        return shopcarNum;
    }

    public void setShopcarNum(int shopcarNum) {
        this.shopcarNum = shopcarNum;
    }

    public List<Goods> getHot() {
        return hot;
    }

    public void setHot(List<Goods> hot) {
        this.hot = hot;
    }
}
