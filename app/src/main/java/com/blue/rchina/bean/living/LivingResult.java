package com.blue.rchina.bean.living;

import java.util.List;

/**
 * Created by cj on 2018/9/12.
 */

public class LivingResult {
    private int result;
    private List<LivingInfo> infos;
    private String message;


    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<LivingInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<LivingInfo> infos) {
        this.infos = infos;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
