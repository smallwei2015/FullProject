package com.blue.rchina.bean;

import org.xutils.db.annotation.Column;

import java.io.Serializable;

/**
 * Created by cj on 2017/3/9.
 */

public abstract class BaseDBData implements Serializable {
    @Column(name = "id",isId = true,autoGen = true)
    public int id;
    public int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
