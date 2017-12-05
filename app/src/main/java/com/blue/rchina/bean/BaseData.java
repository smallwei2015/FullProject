package com.blue.rchina.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cj on 2017/3/17.
 */

public abstract class BaseData implements Serializable {
    public abstract BaseData parseObject(String json);
    public abstract List<BaseData> parseList(String jsonList);
}
