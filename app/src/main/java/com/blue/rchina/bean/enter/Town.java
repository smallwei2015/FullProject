package com.blue.rchina.bean.enter;

import java.io.Serializable;

/**
 * Created by cj on 2018/2/6.
 */

public class Town implements Serializable {

    private String townId;
    private String townName;

    public String getTownId() {
        return townId;
    }

    public void setTownId(String townId) {
        this.townId = townId;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }
}
