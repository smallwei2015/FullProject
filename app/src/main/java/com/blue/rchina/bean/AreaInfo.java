package com.blue.rchina.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cj on 2017/10/27.
 */

@Table(name = "area")
public class AreaInfo implements Serializable {
    @Column(name = "areaId",isId = true)
    private String areaId;
    @Column(name = "areaName")
    private String areaName;
    @Column(name = "applicationName")
    private String applicationName;
    @Column(name = "picsrc")
    private String picsrc;
    @Column(name = "parentId")
    private String parentId;
    @Column(name = "sons")
    private List<AreaInfo> sons;
    @Column(name = "provinceCapital")
    private String provinceCapital;
    @Column(name="isOperate")
    private int isOperate;
    @Column(name = "areaIcon")
    private String areaIcon;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPicsrc() {
        return picsrc;
    }

    public void setPicsrc(String picsrc) {
        this.picsrc = picsrc;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<AreaInfo> getSons() {
        return sons;
    }

    public void setSons(List<AreaInfo> sons) {
        this.sons = sons;
    }

    public String getProvinceCapital() {
        return provinceCapital;
    }

    public void setProvinceCapital(String provinceCapital) {
        this.provinceCapital = provinceCapital;
    }

    public int getIsOperate() {
        return isOperate;
    }

    public void setIsOperate(int isOperate) {
        this.isOperate = isOperate;
    }

    public String getAreaIcon() {
        return areaIcon;
    }

    public void setAreaIcon(String areaIcon) {
        this.areaIcon = areaIcon;
    }
}
