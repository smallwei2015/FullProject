package com.blue.rchina.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cj on 2018/1/18.
 */

public class NearbyRecord implements Serializable {

    private String reportId;
    private String datetime;
    private String appuserId;
    private String nickName;
    private String headIcon;
    private String content;
    private List<String> manyPic;
    private List<Size> picsize;

    private String phone;
    /*0未处理1已处理*/
    private int state;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(String appuserId) {
        this.appuserId = appuserId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getManyPic() {
        return manyPic;
    }

    public void setManyPic(List<String> manyPic) {
        this.manyPic = manyPic;
    }

    public List<Size> getPicsize() {
        return picsize;
    }

    public void setPicsize(List<Size> picsize) {
        this.picsize = picsize;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
