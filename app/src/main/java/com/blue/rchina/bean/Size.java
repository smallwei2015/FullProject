package com.blue.rchina.bean;

import java.io.Serializable;

/**
 * Created by cj on 2017/3/22.
 */

public class Size implements Serializable {

    private int width;
    private int height;



    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public static Size defaultSize(){
        Size size = new Size();
        size.setHeight(20);
        size.setWidth(20);
        return size;
    }
}
