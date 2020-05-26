package com.blue.rchina.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/5/26.
 *
 * @copyright 北京融城互联
 */
public class DocBean implements Serializable {

    /**
     * fileName : 如何做创意——13位美国杰出艺术指导和文案撰稿人的创意观念、方法与作品.pdf
     * buyNum : 0
     * wenkuType : 4
     * description : 如何做创意——13位美国杰出艺术指导和文案撰稿人的创意观念、方法与作品     by理查德尼德翰姆  （348P）
     * collectNum : 2
     * title : 《如何做创意》
     * tagName : 其他文案
     * shareNum : 13
     * isVip : 1
     * isFree : 1
     * createTime : 2019-11-22
     * fileSize : 2098495
     * collectState : 1
     * checkState : 3
     * clickNum : 368
     * fileUrl : https://okcm.cn/cmzk/static/file/11/20191122/如何做创意——13位美国杰出艺术指导和文案撰稿人的创意观念、方法与作品.pdf
     * wenkuLink : https://okcm.cn/cmzk/static/file/11/20191122/f31a677d11d04f82b5cd05f3e5bb0dd6.html
     * downNum : 18
     * fileType : 4
     * card : {"cardState":2,"cardUrl":""}
     * fileId : 1920
     * desc : 如何做创意——13位美国杰出艺术指导和文案撰稿人的创意观念、方法与作品     by理查德尼德翰姆  （348P）
     * scanState : 2
     */

    private String fileName;
    private int buyNum;
    private int wenkuType;
    private String description;
    private int collectNum;
    private String title;
    private String tagName;
    private int shareNum;
    private int isVip;
    private int isFree;
    private String createTime;
    private int fileSize;
    private int collectState;
    private int checkState;
    private int clickNum;
    private String fileUrl;
    private String wenkuLink;
    private int downNum;
    private int fileType;
    private CardEntity card;
    private int fileId;
    private String desc;
    private int scanState;
    /**
     * inPayId : DOCPIC2.
     * price : 12
     */

    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getWenkuType() {
        return wenkuType;
    }

    public void setWenkuType(int wenkuType) {
        this.wenkuType = wenkuType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getCollectState() {
        return collectState;
    }

    public void setCollectState(int collectState) {
        this.collectState = collectState;
    }

    public int getCheckState() {
        return checkState;
    }

    public void setCheckState(int checkState) {
        this.checkState = checkState;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getWenkuLink() {
        return wenkuLink;
    }

    public void setWenkuLink(String wenkuLink) {
        this.wenkuLink = wenkuLink;
    }

    public int getDownNum() {
        return downNum;
    }

    public void setDownNum(int downNum) {
        this.downNum = downNum;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public CardEntity getCard() {
        return card;
    }

    public void setCard(CardEntity card) {
        this.card = card;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getScanState() {
        return scanState;
    }

    public void setScanState(int scanState) {
        this.scanState = scanState;
    }


    public static class CardEntity implements Serializable{
        /**
         * cardState : 2
         * cardUrl :
         */

        private int cardState;
        private String cardUrl;

        public int getCardState() {
            return cardState;
        }

        public void setCardState(int cardState) {
            this.cardState = cardState;
        }

        public String getCardUrl() {
            return cardUrl;
        }

        public void setCardUrl(String cardUrl) {
            this.cardUrl = cardUrl;
        }
    }
}
