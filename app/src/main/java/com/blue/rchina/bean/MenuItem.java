package com.blue.rchina.bean;

/**
 * Created by cj on 2017/9/1.
 */

public class MenuItem {

    String itemName;
    int iconRes;
    int moreRes;//更多的图标，如红心、红点等

    public MenuItem(String itemName,int iconRes){
        this.iconRes=iconRes;
        this.itemName=itemName;
    }

    public MenuItem(String itemName,int iconRes,int moreRes){
        this.moreRes=moreRes;
        this.iconRes=iconRes;
        this.itemName=itemName;
    }

    public int getMoreRes() {
        return moreRes;
    }

    public void setMoreRes(int moreRes) {
        this.moreRes = moreRes;
    }

    public String getItemName() {
        return itemName;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }


    public void setItemName(String itemName) {
        this.itemName = itemName;

    }
}

