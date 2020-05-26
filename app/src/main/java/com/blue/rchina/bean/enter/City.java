package com.blue.rchina.bean.enter;

import java.io.Serializable;

/**
 * Created by cj on 2018/2/6.
 */

public class City implements Serializable {

    private String cityId;
    private String cityName;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
