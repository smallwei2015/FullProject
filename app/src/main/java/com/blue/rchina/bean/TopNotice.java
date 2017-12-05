package com.blue.rchina.bean;

import java.io.Serializable;

/**
 * Created by cj on 2017/11/16.
 */

public class TopNotice implements Serializable {
    private String contentId;
    private String title;
    private String datetime;
    private int articleType;
    private int displayType;
    private String picsrc;
    private String linkUrl;
    private String shareUrl;
    private String outType;
    private String ly;
    private String summary;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getArticleType() {
        return articleType;
    }

    public void setArticleType(int articleType) {
        this.articleType = articleType;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public String getPicsrc() {
        return picsrc;
    }

    public void setPicsrc(String picsrc) {
        this.picsrc = picsrc;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public String getLy() {
        return ly;
    }

    public void setLy(String ly) {
        this.ly = ly;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
