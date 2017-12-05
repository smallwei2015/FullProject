package com.blue.rchina.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cj on 2017/10/27.
 */

public class Channel implements Serializable {



    private String title;
    private int id;
    private int channelType;//1外链2发布3含子栏目4其他
    private int sort;
    private String outLink;
    private String showicon;
    private String areaId;
    private int parentId;
    private List<Channel> sons;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getOutLink() {
        return outLink;
    }

    public void setOutLink(String outLink) {
        this.outLink = outLink;
    }

    public String getShowicon() {
        return showicon;
    }

    public void setShowicon(String showicon) {
        this.showicon = showicon;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public List<Channel> getSons() {
        return sons;
    }

    public void setSons(List<Channel> sons) {
        this.sons = sons;
    }
}
