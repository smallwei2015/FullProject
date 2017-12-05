package com.blue.rchina.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cj on 2017/11/16.
 */

public class Alliance implements Serializable {
    private String franchiseeId;
    private String title;
    private String desc;
    private String franchiseeType;
    private List<Channel> sons;

    public String getFranchiseeId() {
        return franchiseeId;
    }

    public void setFranchiseeId(String franchiseeId) {
        this.franchiseeId = franchiseeId;
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

    public String getFranchiseeType() {
        return franchiseeType;
    }

    public void setFranchiseeType(String franchiseeType) {
        this.franchiseeType = franchiseeType;
    }

    public List<Channel> getSons() {
        return sons;
    }

    public void setSons(List<Channel> sons) {
        this.sons = sons;
    }
}
