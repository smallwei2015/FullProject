package com.blue.rchina.bean.mall;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by cj on 2017/7/10.
 */
@Table(name = "address")
public class Address implements Serializable {

    @Column(name = "id", isId = true, autoGen = true)
    private int id;

    @Column(name = "receiveId")
    private int receiveId;

    @Column(name = "isDefault")//0否1是
    private int isDefault;
    @Column(name = "receiveName")
    private String receiveName;
    @Column(name = "receivePhone")
    private String receivePhone;
    @Column(name = "district")
    private String district;
    @Column(name = "receiveAddress")
    private String receiveAddress;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(int receiveId) {
        this.receiveId = receiveId;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
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
}
