package com.blue.rchina.bean.enter;

import java.io.Serializable;

/**
 * Created by cj on 2018/2/6.
 */

public class Province implements Serializable {
    private String provinceId;
    private String provinceName;


    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
