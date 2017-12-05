package com.blue.rchina.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.MD5Utils;
import com.blue.rchina.utils.SignUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class CheckPhoneActivity extends BaseActivity {

    @ViewInject(R.id.phone_edit)
    EditText phoneEdit;
    @ViewInject(R.id.verify_edit)
    EditText verifyEdit;
    @ViewInject(R.id.getVerify)
    TextView getVerify;
    @ViewInject(R.id.register)
    TextView check;

    private int timer = 30;


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {


            switch (msg.what) {
                case 100:
                    getVerify.setText(timer + "S");

                    timer--;
                    if (timer > 0) {
                        handler.sendEmptyMessageDelayed(100, 1000);
                    } else {
                        timer = 30;
                        getVerify.setText("获取验证码");
                        getVerify.setClickable(true);
                    }

                    break;
                case 200:

                    break;
            }
            return false;
        }
    });
    private int flag;
    private AlertDialog dialog;

    @Override
    public void initData() {
        super.initData();

        flag = getIntent().getIntExtra("flag", -1);
    }

    @Override
    public void initView() {

        super.initView();
        if (flag == 1) {
            initTop(R.mipmap.left_white, "修改密码", -1);
        } else if (flag == 2) {
            initTop(R.mipmap.left_white, "修改手机号", -1);
        } else {
            initTop(R.mipmap.left_white, "手机号快速注册", -1);
        }

        getVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVer();
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_phone);

        x.view().inject(this);
        initData();
        initView();
    }

    public void getVer() {


        RequestParams entity = new RequestParams(UrlUtils.N_achieveSMSCode);

        String phone = phoneEdit.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            UIUtils.showToast("电话不能为空");
            return;
        }

        handler.sendEmptyMessage(100);
        getVerify.setClickable(false);


        entity.addBodyParameter("phone", phone);
        entity.addBodyParameter("appid", SignUtils.App_ID);
        String data = UrlUtils.N_achieveSMSCode + "?phone=" + phone + "&appid=" + SignUtils.App_ID;

        String sign = SignUtils.sign(data, SignUtils.regularKey);
        entity.addBodyParameter("key", sign);


        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult() == 200) {
                    UIUtils.showToast("验证码获取成功");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络请求失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void check() {

        // TODO: 2017/7/5 这段要删除
        /*Intent intent = new Intent(mActivity, RegisterActivity.class);
        startActivity(intent);*/

        final String phone = phoneEdit.getText().toString();
        String key = verifyEdit.getText().toString();

        if (checkPhoneAndKey(phone, key)) {

            final ProgressDialog dialog = new ProgressDialog(mActivity);
            dialog.setMessage("加载中...");
            dialog.show();

            RequestParams entity = new RequestParams(UrlUtils.N_validateSMSCode);
            entity.addBodyParameter("phone", phone);
            entity.addBodyParameter("smsCode", key);
            x.http().post(entity, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {

                    NetData netData = JSON.parseObject(result, NetData.class);

                    if (netData.getResult() == 200) {

                        checkSuccess();

                    } else {
                        UIUtils.showToast("验证失败，请确认手机号和验证码");
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    UIUtils.showToast("网络请求失败");
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    dialog.dismiss();
                }
            });
        }
    }

    private void checkSuccess() {
        if (flag == 1) {
            //修改密码

            changePass();
        } else if (flag == 2) {
            //修改手机号
            changePhone();
        } else {
            Intent intent = new Intent(mActivity, RegisterActivity.class);
            intent.putExtra("phone", phoneEdit.getText().toString());
            startActivity(intent);

            finish();
        }
    }

    private void changePhone() {

        final ProgressDialog dialog=new ProgressDialog(mActivity);
        dialog.setMessage("修改中...");

        dialog.show();


        RequestParams entity = new RequestParams(UrlUtils.N_updatePersonalPhone);
        entity.addBodyParameter("phone", phoneEdit.getText().toString());
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        entity.addBodyParameter("smsCode", verifyEdit.getText().toString());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult() == 200) {
                    UIUtils.showToast("修改手机号成功");

                    finish();
                } else {
                    UIUtils.showToast("修改手机号失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络请求错误");
          }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dialog.dismiss();
            }
        });
    }

    private void changePass() {
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.interact_comment_pop, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(inflate);
        dialog = builder.create();

        ((TextView) inflate.findViewById(R.id.title)).setText("修改密码：");
        final EditText editText = (EditText) inflate.findViewById(R.id.edit);

        inflate.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        inflate.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = editText.getText().toString();
                if (TextUtils.isEmpty(pass)) {
                    UIUtils.showToast("密码不能为空");
                } else if (pass.length() > 20 || pass.length() < 6) {
                    UIUtils.showToast("密码长度在6-20");
                } else {
                    RequestParams entity = new RequestParams(UrlUtils.N_resetPassword);
                    entity.addBodyParameter("passWord", MD5Utils.getMD5(pass));
                    entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
                    x.http().post(entity, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            NetData netData = JSON.parseObject(result, NetData.class);

                            if (netData.getResult() == 200) {
                                UIUtils.showToast("修改成功");

                                finish();
                            } else {
                                UIUtils.showToast("修改失败");
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            UIUtils.showToast("网络请求失败");
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                }
            }
        });
        dialog.show();
    }

    private boolean checkPhoneAndKey(String phone, String key) {

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(key)) {

            UIUtils.showToast("请检查手机号和验证码填写是否正确");
            return false;
        }

        return true;

    }
}
