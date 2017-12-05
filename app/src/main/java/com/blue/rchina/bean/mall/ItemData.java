package com.blue.rchina.bean.mall;

import java.io.Serializable;

/**
 * Created by cj on 2017/7/18.
 */
public class ItemData implements Serializable{

    private String name;
    private int icon;
    private boolean checked;
    private Object data;


    public ItemData(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
