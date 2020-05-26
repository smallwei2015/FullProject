package com.blue.rchina.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.User;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.manager.UserManagerInterface;
import com.blue.rchina.utils.UIUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.toRegister)
    View toRegister;

    @ViewInject(R.id.name_edit)
    EditText nameEdit;
    @ViewInject(R.id.pass_edit)
    EditText passEdit;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.view().inject(this);
        initData();
        initView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white,"登录",-1);

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(mActivity,CheckPhoneActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    public void btn_login(View view) {

        String name=nameEdit.getText().toString();
        String pass=passEdit.getText().toString();



        if (checkNameAndPass(name,pass)) {
            mDialog = ProgressDialog.show(LoginActivity.this, null, "正在登录，请稍后...");
            mDialog.setCancelable(true);


            UserManager.login(name, pass, new UserManagerInterface() {
                @Override
                public void success(User user) {
                    if (mDialog != null && mDialog.isShowing())
                        mDialog.dismiss();
                    UIUtils.showToast("登录成功");
                    finish();
                }

                @Override
                public void faild(User user) {
                    if (mDialog != null && mDialog.isShowing())
                        mDialog.dismiss();
                    //UIUtils.showToast("登录失败");
                }

            });

        }
    }

    private boolean checkNameAndPass(String name,String pass) {

        if (TextUtils.isEmpty(name)||TextUtils.isEmpty(pass)) {
            UIUtils.showToast("请确认密码和用户名填写正确");
            return false;
        }

        return true;
    }


    private void login(final String nameStr, final String userId, final String userIcon){


        UserManager.loginThird(userId, nameStr, userIcon, new UserManagerInterface() {
            @Override
            public void success(User user) {
                if (mDialog != null && mDialog.isShowing())
                    mDialog.dismiss();

                if (TextUtils.isEmpty(user.getPhone())) {
                    /*Intent intent = new Intent(mActivity, PerfectUserInfo.class);
                    startActivity(intent);*/
                }

                finish();
            }

            @Override
            public void faild(User user) {
                if (mDialog != null && mDialog.isShowing())
                    mDialog.dismiss();

                //UIUtils.showToast("登录失败");
            }
        });

    }

    public void loginByWeChat(View view) {
        authorize(new Wechat());
    }

    public void loginByQQ(View view) {
        authorize(new QQ());
    }

    public void loginBySina(View view) {
        authorize(new SinaWeibo());
    }
    private void authorize(final Platform plat) {
        if (plat == null) {
            return;
        }
        mDialog = ProgressDialog.show(LoginActivity.this, null, "正在登录，请稍后...");
        mDialog.setCancelable(true);
        //判断指定平台是否已经完成授权
        if(plat.isAuthValid()) {

            plat.removeAccount(true);

            PlatformDb db = plat.getDb();
            try {
                String nickname = URLEncoder.encode(plat.getDb().getUserName(), "utf-8");
                String userId = db.getUserId();
                String userIcon = db.getUserIcon();

                if (userId != null) {
                    login(nickname, userId, userIcon);
                    return;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                String nickname = platform.getDb().getUserName();
                String userId = platform.getDb().getUserId();
                String userIcon = platform.getDb().getUserIcon();


                login(nickname,userId,userIcon);

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

                UIUtils.showToast("授权失败");
                mDialog.dismiss();
                platform.removeAccount(true);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                UIUtils.showToast("取消登录");
                platform.removeAccount(true);
                mDialog.dismiss();
            }

        });
        // true不使用SSO授权，false使用SSO授权
        plat.SSOSetting(false);
        //获取用户资料
        //plat.authorize();
        plat.showUser(null);
    }

    public void btn_forget(View view) {


        Intent intent=new Intent(mActivity,CheckPhoneActivity.class);
        intent.putExtra("flag",1);
        startActivity(intent);
    }
}
