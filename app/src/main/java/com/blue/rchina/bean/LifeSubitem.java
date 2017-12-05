package com.blue.rchina.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by cj on 2017/3/9.
 */

@Table(name = "redStone_lifeSubitem")
public class LifeSubitem extends BaseDBData {

    @Column(name = "title")
    private String title;
    @Column(name = "iconStr")
    private String iconStr;
    private int icon;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public LifeSubitem() {
    }

    public LifeSubitem(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconStr() {
        return iconStr;
    }

    public void setIconStr(String iconStr) {
        this.iconStr = iconStr;
    }

}
