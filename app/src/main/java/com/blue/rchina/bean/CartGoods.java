package com.blue.rchina.bean;

import android.location.Address;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cj on 2017/7/14.
 */

@Table(name = "cartgoods")
public class CartGoods implements Serializable {

    @Column(name = "id",isId = true,autoGen = true)
    private int id;
    @Column(name = "shopId")
    private long shopId;
    @Column(name = "goodsId")
    private long goodsId;
    @Column(name = "title")
    private String title;
    @Column(name = "desc")
    private String desc;
    @Column(name = "price")
    private double price;
    @Column(name = "picsrc")
    private String picsrc;
    @Column(name = "count")
    private int count;
    @Column(name = "list")
    private List<Address> list;

    private boolean isSelect;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
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

    public String getPicsrc() {
        return picsrc;
    }

    public void setPicsrc(String picsrc) {
        this.picsrc = picsrc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Address> getList() {
        return list;
    }

    public void setList(List<Address> list) {
        this.list = list;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
