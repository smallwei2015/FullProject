package com.blue.rchina.bean;

import java.io.Serializable;

/**
 * Created by cj on 2017/3/14.
 */

public class DataWrap implements Serializable {

    private Object data;
    private int type;
    private int state;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
