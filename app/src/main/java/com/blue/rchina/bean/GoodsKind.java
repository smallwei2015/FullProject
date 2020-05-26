package com.blue.rchina.bean;

import java.util.List;

/**
 * Created by cj on 2018/5/24.
 */

public class GoodsKind {

    /**
     * info : [{"title":"其他","categoryId":1},{"title":"食品","categoryId":2},{"title":"日用品","categoryId":3},{"title":"饮料","categoryId":4},{"title":"烟酒类","categoryId":5},{"title":"瓜果蔬菜","categoryId":6},{"title":"服装","categoryId":7},{"title":"家用电器","categoryId":8},{"title":"文具","categoryId":9},{"title":"玩具","categoryId":10},{"title":"床上用品","categoryId":11}]
     * message : 获取成功
     * result : 200
     */

    private String message;
    private String result;
    private List<InfoBean> info;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * title : 其他
         * categoryId : 1
         */

        private String title;
        private int categoryId;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }
    }
}
