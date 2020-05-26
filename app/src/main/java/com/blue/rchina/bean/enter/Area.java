package com.blue.rchina.bean.enter;

import java.io.Serializable;

/**
 * Created by cj on 2018/2/6.
 */

public class Area implements Serializable {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
