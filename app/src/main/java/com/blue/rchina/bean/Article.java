package com.blue.rchina.bean;

import java.util.List;

/**
 * Created by cj on 2017/3/17.
 */

public class Article extends BaseData {


    private int articleType;
    private long clickCount;
    private long commentCount=0;
    private long contentId;
    private String  datetime;

    private int displayType;
    private long hateCount;
    private long likeCount;
    private String linkUrl;
    private String ly;
    private int outType;
    private String picsrc;
    private String summary;
    private String title;

    private String shareUrl;

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public int getArticleType() {
        return articleType;
    }

    public void setArticleType(int articleType) {
        this.articleType = articleType;
    }

    public long getClickCount() {
        return clickCount;
    }

    public void setClickCount(long clickCount) {
        this.clickCount = clickCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public long getHateCount() {
        return hateCount;
    }

    public void setHateCount(long hateCount) {
        this.hateCount = hateCount;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLy() {
        return ly;
    }

    public void setLy(String ly) {
        this.ly = ly;
    }

    public int getOutType() {
        return outType;
    }

    public void setOutType(int outType) {
        this.outType = outType;
    }

    public String getPicsrc() {
        return picsrc;
    }

    public void setPicsrc(String picsrc) {
        this.picsrc = picsrc;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public BaseData parseObject(String json) {
        return null;
    }

    @Override
    public List<BaseData> parseList(String jsonList) {
        return null;
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleType=" + articleType +
                ", clickCount=" + clickCount +
                ", commentCount=" + commentCount +
                ", contentId=" + contentId +
                ", datetime='" + datetime + '\'' +
                ", displayType=" + displayType +
                ", hateCount=" + hateCount +
                ", likeCount=" + likeCount +
                ", linkUrl='" + linkUrl + '\'' +
                ", ly='" + ly + '\'' +
                ", outType=" + outType +
                ", picsrc='" + picsrc + '\'' +
                ", summary='" + summary + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
