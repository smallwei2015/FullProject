package com.blue.rchina.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.User;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.manager.UserManagerInterface;
import com.blue.rchina.utils.FileUtils;
import com.blue.rchina.utils.SPUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.caption.update.UpdateUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


public class SettingActivity extends BaseActivity {


    @ViewInject(R.id.setting_change)
    View change;
    @ViewInject(R.id.setting_contact)
    View contact;

    @ViewInject(R.id.setting_size)
    View size;

    @ViewInject(R.id.setting_clear)
    View clear;

    @ViewInject(R.id.loginOut)
    View out;

    @ViewInject(R.id.setting_upload)
    View upload;

    @ViewInject(R.id.setting_about)
    View about;
    @ViewInject(R.id.setting_recommend)
    View recommend;

    @ViewInject(R.id.switch_btn)
    ImageView switch_btn;

    private Intent intent;
    private String titleUrl = "http://www.bluepacific.com.cn";
    public SharedPreferences sp;

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"系统设置",-1);

        sp = SPUtils.getSP();
        final boolean agreeAutoChange = sp.getBoolean("agreeAutoChange", false);

        switch_btn.setSelected(agreeAutoChange);

        switch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());

                sp.edit().putBoolean("agreeAutoChange",v.isSelected()).apply();

                if (v.isSelected()){
                    UIUtils.showToast("自动切换城市打开");
                }else {
                    UIUtils.showToast("自动切换城市关闭");
                }

            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CitySelectListActivity.class);
                startActivityForResult(intent,200);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*intent=new Intent(mActivity, ContactUsActivity.class);
                                startActivity(intent);*/

                if(UserManager.isLogin()) {
                    intent = new Intent(mActivity, UserCenterActivity.class);
                    startActivity(intent);
                }else {
                    UserManager.toLogin();
                }
            }
        });

        size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(mActivity,FeedbackActivity.class);
                startActivity(intent);*/

                intent=new Intent(mActivity, ContactUsActivity.class);
                startActivity(intent);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File appCacheDir = new File(getFilesDir().getAbsolutePath()+"/webcache");
                    File webviewCacheDir = new File(getCacheDir().getAbsolutePath()+"/webviewCache");
                    long folderSize = FileUtils.getFolderSize(new File(FileUtils.CACHEPATH));

                    folderSize+=FileUtils.getFolderSize(appCacheDir);
                    folderSize+=FileUtils.getFolderSize(webviewCacheDir);

                    String formatSize = FileUtils.getFormatSize(folderSize);



                    AlertDialog.Builder builder1=new AlertDialog.Builder(mActivity);

                    builder1.setTitle("清除缓存：");
                    builder1.setMessage("现有缓存文件"+formatSize+",确认清除缓存文件？");
                    builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FileUtils.deleteFolderFile(FileUtils.CACHEPATH, true);
                            //FileUtils.cleanFiles(mActivity);

                            x.image().clearCacheFiles();
                            x.image().clearMemCache();

                            CookieManager cm = CookieManager.getInstance();
                            /*cm.removeSessionCookie();
                            cm.removeAllCookie();*/
                            /*webview自动创建的缓存数据库*/
                            boolean b = deleteDatabase("webview.db");
                            boolean b1 = deleteDatabase("webviewCache.db");

                            clearWebViewCache();

                            UIUtils.showToast("缓存清理成功");
                        }
                    });

                    builder1.create().show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,UpdateActivity.class);
                startActivity(intent);

                UpdateUtils.getInit().update(mActivity,0);

            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,AboutActivity.class);
                startActivity(intent);
            }
        });

        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.loginOut(v, new UserManagerInterface() {
                    @Override
                    public void success(User user) {
                        finish();
                    }

                    @Override
                    public void faild(User user) {

                    }
                });

            }
        });


        if (UserManager.isLogin()){
            out.setVisibility(View.VISIBLE);
        }else {
            out.setVisibility(View.GONE);
        }


    }
    public void clearWebViewCache(){

        //清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath()+"/webcache");

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath()+"/webviewCache");

        //删除webview 缓存目录
        if(webviewCacheDir.exists()){
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if(appCacheDir.exists()){
            deleteFile(appCacheDir);
        }
    }
    public void deleteFile(File file) {

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
        }
    }

    private void share(){
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(titleUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("邀请好友");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //oks.setImageUrl("");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数

        oks.setUrl(UrlUtils.N_SHARE);
        final String path = FileUtils.CACHEPATH + File.separator + "qrCode.png";

        File file=new File(path);
        if (!file.exists()){
            FileUtils.creatImgFromAsset(mActivity,"qrCode.png");
        }
        oks.setImagePath(path);//确保SDcard下面存在此张图片

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                //分享成功后调用，分享成功的接口

                RequestParams entity = new RequestParams(UrlUtils.N_inviteFriend);
                if (UserManager.isLogin()) {
                    entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
                }
                x.http().post(entity, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        NetData netData = JSON.parseObject(result, NetData.class);
                        if (netData.getResult()==200){
                            UIUtils.showToast("分享成功");
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                UIUtils.showToast(""+throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.w("3333",i+"");
            }
        });
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {

                //先拿到分享能力在设置为空
                if (platform.getName().equalsIgnoreCase(QQ.NAME)) {
                    paramsToShare.setText(null);
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl(null);
                    paramsToShare.setImagePath(path);
                } else if (platform.getName().equalsIgnoreCase(QZone.NAME)) {
                    paramsToShare.setText(null);
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl(null);
                    paramsToShare.setImagePath(path);
                }else if (platform.getName().equalsIgnoreCase(Wechat.NAME)){
                    //微信必须要设置具体的方分享类型
                    paramsToShare.setText(null);
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl(null);
                    paramsToShare.setShareType(Platform.SHARE_IMAGE);
                    //paramsToShare.setImagePath(path);
                }else if (platform.getName().equals(WechatMoments.NAME)) {
                    paramsToShare.setShareType(Platform.SHARE_IMAGE);
                }

            }

        });

        oks.setUrl(titleUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("填写评论");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(titleUrl);

        // 启动分享GUI
        oks.show(mActivity);
    }


    private void shareUrl() {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用

        final String appName = getString(R.string.app_name);
        oks.setTitle(appName);


        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(UrlUtils.APP_URL);


        // text是分享文本，所有平台都需要这个字段
        oks.setText("邀请好友");

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数


        oks.setImagePath(FileUtils.APP_LOGO);
        oks.setUrl(UrlUtils.APP_URL);

        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(appName);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(UrlUtils.APP_URL);

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                //分享成功后调用，分享成功的接口
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                UIUtils.showToast("分享失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                UIUtils.showToast("取消分享");
            }
        });


        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {

                if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
                    //微信必须要设置具体的方分享类型
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                } else if (platform.getName().equals(WechatMoments.NAME)) {
                    paramsToShare.setTitle(appName);
                }

            }
        });
        // 启动分享GUI
        oks.show(this);

    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(UrlUtils.N_SHARE);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("融城互联智慧中国-城市服务联盟，尊享智慧生活。");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath(FileUtils.APP_LOGO);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(UrlUtils.N_SHARE);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("融城中国");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(UrlUtils.N_SHARE);

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                //Log.w("4444",platform.getName()+":"+throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {

                if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
                    //微信必须要设置具体的方分享类型
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                } else if (platform.getName().equals(WechatMoments.NAME)) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                }

            }
        });
        // 启动分享GUI
        oks.show(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        x.view().inject(this);
        initView();
        initData();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 200:

                if (resultCode==200) {
                    finish();
                }
                break;
        }
    }
}
