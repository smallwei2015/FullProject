package com.blue.rchina.base;

import android.graphics.drawable.ColorDrawable;

import com.blue.rchina.R;
import com.blue.rchina.adapter.xUtilsImageLoad;
import com.blue.rchina.bean.User;
import com.blue.rchina.manager.MyCrashHandler;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.SPUtils;
import com.blue.rchina.utils.UIUtils;
import com.mob.MobApplication;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by cj on 2017/6/21.
 */

public class BaseApplication extends MobApplication {

    private static BaseApplication mApp=null;
    public static long MainThreadId;
    /*管理栈顶*/
    private static List<BaseUIContainer> containers;
    public static DbManager.DaoConfig daoConfig;

    public static BaseApplication getInstance(){
        return mApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mApp=this;

        initData();
        initThird();
        loginAuto();
    }

    private void loginAuto() {
        try {
            List<User> all = x.getDb(daoConfig).findAll(User.class);

            if (all != null&&all.size()>0) {

                if (all.size()>1){

                    x.getDb(BaseApplication.daoConfig).delete(User.class);

                    return;
                }
                User user = all.get(0);
                if (user!=null&&user.getExpiration()>System.currentTimeMillis()) {

                    int userType = SPUtils.getSP().getInt("userFlag", -1);

                    if (userType==1) {

                        UserManager.loginOutWithoutDelay();
                        SPUtils.getSP().edit().putInt("userFlag",0).apply();
                    }else {
                        UserManager.setUser(user);
                    }
                }else {
                    UIUtils.showToast("登录超时请重新登录");
                    /*登录超时删除用户*/
                    UserManager.loginOutWithoutDelay();
                    SPUtils.getSP().edit().putInt("userFlag",0).apply();
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    private void initThird() {

        MyCrashHandler.getInstance().init(getApplicationContext());

        //对xUtils进行初始化
        x.Ext.init(this);



        File dbDir = new File(getFilesDir()+File.separator+"dateBase");
        if (!dbDir.exists()) {
            //将数据库文件存储在database文件内
            dbDir.mkdirs();
        }

        daoConfig = new DbManager.DaoConfig()
                .setDbName("rcchina_db.db")//创建数据库的名称
                .setDbVersion(2)//数据库版本号
                .setDbDir(dbDir)//设置数据库的存储位置
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {

                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        try {
                            /*删除用户表*/
                            db.dropTable(User.class);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                });

        /*初始化galleryfinal*/
        //配置主题
        ThemeConfig theme = null;
        theme = new ThemeConfig.Builder()
                .setTitleBarTextColor(getResources().getColor(R.color.white))
                .setTitleBarBgColor(getResources().getColor(R.color.colorPrimary))
                .setIconBack(R.mipmap.left_white)
                .setEditPhotoBgTexture(new ColorDrawable(getResources().getColor(R.color.transparent)))
                .setFabNornalColor(getResources().getColor(R.color.primaryColor))
                .setFabPressedColor(getResources().getColor(R.color.primaryColorPressed))//.setIconPreview()
                /*.setIconCamera(R.drawable.camera_gray)
                .setIconFolderArrow(R.drawable.arrow_down_gray)
                .setIconPreview(R.drawable.eye_gray)
                .setIconCrop(R.drawable.crop_gray)
                .setIconRotate(R.drawable.rotate_gray)*/
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(false)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();

        //配置imageloader
        ImageLoader imageloader = new xUtilsImageLoad();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme)
                //.setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);


        /*初始化极光推送*/
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        /*初始化shareSDK*/
        //3.x不需要这一步


    }

    private void initData() {
        MainThreadId = android.os.Process.myTid();
        containers=new ArrayList<>();
    }


    /*当前栈顶元素*/
    public static int push(BaseUIContainer container){
        if (containers == null) {
            containers=new ArrayList<>();
        }
        containers.add(container);

        return containers.size();
    }
    public static int pop(BaseUIContainer container){

        if (containers.contains(container)) {
            containers.remove(container);
        }

        return containers.size();
    }

    public static void clearContainers(){
        if (containers!=null){

            for (int i = 0; i < containers.size(); i++) {
                containers.get(i).UIfinish();
            }
            containers.clear();
            containers=null;
        }
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        clearContainers();
    }
}
