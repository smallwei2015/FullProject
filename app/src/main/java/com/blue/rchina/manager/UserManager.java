package com.blue.rchina.manager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.activity.LoginActivity;
import com.blue.rchina.base.BaseApplication;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.User;
import com.blue.rchina.utils.MD5Utils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import static com.blue.rchina.base.BaseApplication.daoConfig;


/**
 * Created by cj on 2017/6/21.
 */

public class UserManager {

    public static final String action_in = "com.rcchina.login";
    public static final String action_out = "com.rcchina.login_out";
    public static final String action_change="com.rcchina.change";

    public static final String action_cityChange="com.rcchina.city_change";
    private static User cUser;

    public static User getUser(){

        if (cUser!=null){
            return cUser;
        }else{
            return null;
        }
    }

    public static void setUser(User user){
        if (user != null) {
            cUser=user;
            sendLogin();

            setAlias();
        }
    }

    private static void setAlias() {
        /*JPushInterface.setAlias(BaseApplication.getInstance(), cUser.getUserName(), new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.w("3333","设置alias成功"+i+"-"+s);

                if (i==6002){
                    *//*超时就去重新设置*//*
                    //setAlias();
                }
            }
        });*/
    }

    public static void login(String name, String pass, final UserManagerInterface manager){

        RequestParams entity = new RequestParams(UrlUtils.N_login);
        entity.addBodyParameter("arg1",name);
        entity.addBodyParameter("passWord", MD5Utils.getMD5(pass));

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult()==200){

                    User user = JSON.parseObject(netData.getInfo(), User.class);
                    cUser=user;

                    manager.success(user);
                    saveUser(user);
                    sendLogin();

                    setAlias();
                }else {
                    manager.faild(null);
                    if (netData.getResult()==401){
                        UIUtils.showToast("登录名密码错误");
                    }
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                manager.faild(null);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public static void saveUser(User user) {
        try {
            /*登录过时时间为7天*/
            user.setExpiration(System.currentTimeMillis()+1000L*60*60*24*30);
            x.getDb(daoConfig).saveOrUpdate(user);
        } catch (DbException e) {
            e.printStackTrace();

        }
    }

    private static void sendLogin() {
        Intent intent = new Intent();
        intent.setAction(action_in);
        BaseApplication.getInstance().sendBroadcast(intent);
    }

    public static void loginOut(View view,final UserManagerInterface manager){

        final ProgressDialog dialog=new ProgressDialog(view.getContext());
        dialog.setMessage("正在退出登录...");
        dialog.show();

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                manager.success(cUser);


                /*JPushInterface.setAlias(BaseApplication.getInstance(), "", new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                        Log.w("3333","取消别名设置");
                    }
                });*/
                deleteUser();
                sendLoginOut();
            }
        }, 1000);


    }

    public static void loginOutWithoutDelay() {
        /*JPushInterface.setAlias(BaseApplication.getInstance(), "", new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.w("3333","取消别名设置");
            }
        });*/
        //deleteUser();

        DbManager db = x.getDb(daoConfig);
        try {
            db.delete(User.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        cUser=null;
        sendLoginOut();
    }

    private static void deleteUser() {
        try {
            if (cUser != null) {
                DbManager db = x.getDb(daoConfig);
                db.delete(cUser);

                db.delete(User.class);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        cUser=null;
    }

    private static void sendLoginOut() {
        Intent intent = new Intent();
        intent.setAction(action_out);
        BaseApplication.getInstance().sendBroadcast(intent);
    }

    public static void register(String name,String pass,String nickname,String address,String path,final UserManagerInterface manager){


        RequestParams entity = new RequestParams(UrlUtils.N_regiWithText);
        entity.addBodyParameter("phone",name);
        entity.addBodyParameter("passWord",MD5Utils.getMD5(pass));
        entity.addBodyParameter("nickName",nickname);
        entity.addBodyParameter("file",new File(path));

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult()==200){
                    manager.success(null);
                }else {
                    manager.faild(null);
                    if (netData.getResult()==401){
                        UIUtils.showToast("该手机号已注册");
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                manager.faild(null);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIUtils.showToast("网络请求失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static boolean isLogin() {
        return cUser!=null;
    }

    public static void loginThird(String userId, String nameStr, String userIcon,final UserManagerInterface manager) {

        RequestParams entity = new RequestParams(UrlUtils.N_thirdPartyLogin);

        entity.addBodyParameter("thirdAuth",userId);
        entity.addBodyParameter("nickName",nameStr);
        entity.addBodyParameter("arg1",userIcon);


        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult()==200){

                    User user = JSON.parseObject(netData.getInfo(), User.class);
                    cUser=user;
                    manager.success(user);

                    saveUser(user);
                    sendLogin();

                    setAlias();

                }else {
                    manager.faild(null);
                    UIUtils.showToast("登录成功");
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络连接失败");
                manager.faild(null);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    public static void sendChange(){

        loginOutWithoutDelay();
        try {
            x.getDb(daoConfig).saveOrUpdate(cUser);

            Intent intent = new Intent();
            intent.setAction(action_change);
            BaseApplication.getInstance().sendBroadcast(intent);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    public static void toLogin() {
        BaseApplication instance = BaseApplication.getInstance();
        Intent intent=new Intent(instance, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        instance.startActivity(intent);
    }
}
