package com.blue.rchina.bean;

import java.io.Serializable;

/**
 * Created by cj on 2017/9/4.
 */

public class SearchItem implements Serializable {

    private int type;
    private String content;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
