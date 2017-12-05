package com.blue.rchina.bean;

import java.util.List;

/**
 * 市
 */

public class City {
    String name;//城市名
    List<String> area;//区的数组

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }
}
