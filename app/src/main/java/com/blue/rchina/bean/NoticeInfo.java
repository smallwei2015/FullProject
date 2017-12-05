package com.blue.rchina.bean;

import java.io.Serializable;

/**
 * Created by cj on 2017/7/26.
 */

public class NoticeInfo implements Serializable {

    private int id;
    private int noticeId;
    private int type;
    private String title;
    private String content;
    private String datetime;
    private String author;
    private int sex;
    private String headIcon;
    private double addNum;
    private String addDate;

    private int readState=0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public double getAddNum() {
        return addNum;
    }

    public void setAddNum(double addNum) {
        this.addNum = addNum;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public int getReadState() {
        return readState;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }
}
