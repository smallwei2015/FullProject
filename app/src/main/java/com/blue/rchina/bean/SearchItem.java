package com.blue.rchina.bean;

import java.io.Serializable;

/**
 * Created by cj on 2017/9/4.
 */

public class SearchItem implements Serializable {

    private int type=1;
    private String title;


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
}
