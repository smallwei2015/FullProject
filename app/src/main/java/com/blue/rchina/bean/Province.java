package com.blue.rchina.bean;

import java.util.List;

/**
 * 省
 */

public class Province {
    String name;//省名
    List<City> city;//城市列表

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }
}
