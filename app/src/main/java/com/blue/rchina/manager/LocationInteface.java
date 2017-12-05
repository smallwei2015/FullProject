package com.blue.rchina.manager;

import com.amap.api.location.AMapLocation;

/**
 * Created by cj on 2017/11/1.
 */

public interface LocationInteface {
    void locationSuccess(AMapLocation location);
    void locationFaild();
}
