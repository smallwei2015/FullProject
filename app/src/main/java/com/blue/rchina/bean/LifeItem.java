package com.blue.rchina.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by cj on 2017/3/9.
 */

@Table(name = "redStone_lifeItem")
public class LifeItem extends BaseDBData {

    @Column(name = "title",property = "")
    public String title;

    public List<LifeSubitem> items;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LifeSubitem> getItems() {
        return items;
    }

    public void setItems(List<LifeSubitem> items) {
        this.items = items;
    }

}
