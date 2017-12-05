package com.blue.rchina.manager;


import com.blue.rchina.bean.User;

/**
 * Created by cj on 2017/6/27.
 */

public interface UserManagerInterface {
    void success(User user);
    void faild(User user);
}
