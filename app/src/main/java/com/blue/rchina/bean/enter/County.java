package com.blue.rchina.bean.enter;

import java.io.Serializable;

/**
 * Created by cj on 2018/2/6.
 */

public class County implements Serializable {

    private String countyId;
    private String countyName;

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
}
