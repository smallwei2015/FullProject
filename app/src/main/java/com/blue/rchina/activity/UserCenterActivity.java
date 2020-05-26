package com.blue.rchina.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.User;
import com.blue.rchina.manager.UserInterface;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.manager.xUtilsManager;
import com.blue.rchina.utils.CameralUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.blue.rchina.views.BasePopUpWindow;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import static com.blue.rchina.R.id.share;

public class UserCenterActivity extends BaseActivity implements GalleryFinal.OnHanlderResultCallback{


    @ViewInject(R.id.user_nickname)
    View nickname;
    @ViewInject(R.id.user_gender)
    RadioGroup gender;
    @ViewInject(R.id.user_password)
    View password;
    @ViewInject(R.id.user_phone)
    View phone;
    @ViewInject(R.id.toolbar)
    View toolbar;

    @ViewInject(R.id.read)
    TextView tv_read;
    @ViewInject(share)
    TextView tv_share;
    @ViewInject(R.id.recommend)
    TextView tv_rec;
    @ViewInject(R.id.interact)
    TextView tv_interact;

    @ViewInject(R.id.icon_image)
    ImageView icon_image;
    private AlertDialog dialog;
    public PopupWindow window;

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"用户信息",-1,R.color.transparent);

        int statusBarHeight = UIUtils.getStatusBarHeight(mActivity);
        if (statusBarHeight>0){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();

            layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin+statusBarHeight,0,0);
        }


        nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View inflate = LayoutInflater.from(mActivity).inflate(R.layout.interact_comment_pop, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setView(inflate);
                dialog = builder.create();

                ((TextView) inflate.findViewById(R.id.title)).setText("修改昵称：");
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
                        String nickname = editText.getText().toString();
                        if (TextUtils.isEmpty(nickname)) {
                            UIUtils.showToast("昵称不能为空");
                        } else if (nickname.length()>10){
                            UIUtils.showToast("昵称长度不能超过10位");
                        }else {
                            changeNickname(nickname);
                        }
                    }
                });
                dialog.show();
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass();
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhone();
            }
        });
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.man){
                    changeGender(1);
                }else if (checkedId==R.id.woman){
                    changeGender(2);
                }else {
                    changeGender(3);
                }
            }
        });

        icon_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window = new BasePopUpWindow(mActivity);

                View inflate = LayoutInflater.from(mActivity).inflate(R.layout.image_pic, null);

                inflate.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {

                            if(CameralUtils.hasCamera()) {
                                GalleryFinal.openCamera(100, UserCenterActivity.this);
                            }else {
                                UIUtils.showToast("该设备无法使用摄像头");
                            }
                        }catch (Exception e){
                            UIUtils.showToast("该设备无法使用摄像头");
                        }

                    }
                });

                inflate.findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GalleryFinal.openGallerySingle(200, UserCenterActivity.this);
                    }
                });


                window.setContentView(inflate);
                window.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

                window.setBackgroundDrawable(new ColorDrawable(0x00000000));
                window.setOutsideTouchable(false);
                window.setFocusable(true);

                window.showAsDropDown(v);
            }
        });


        setUserHandler(new UserInterface() {
            @Override
            public void loginIn() {

            }

            @Override
            public void loginOut() {

            }

            @Override
            public void userStateChange() {
                UIUtils.showToast("数据修改成功,请重新登录");
                UserManager.toLogin();
            }
        });

        loadUserInfo();
    }

    private void loadUserInfo() {
        RequestParams entity = new RequestParams(UrlUtils.N_achievePersonalInfoCount);
        entity.addBodyParameter("appuserId",UserManager.getUser().getAppuserId()+"");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult()==200){
                    JSONObject object = JSON.parseObject(netData.getInfo());
                    String read = object.getString("read");
                    String share = object.getString("share");
                    String recommend = object.getString("recommend");
                    String report = object.getString("report");


                    tv_read.setText(read);
                    tv_share.setText(share);
                    tv_rec.setText(recommend);
                    tv_interact.setText(report);
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

    private void changeGender(final int gender) {

        if (UserManager.getUser().getSex()==gender){
            return;
        }
        RequestParams entity = new RequestParams(UrlUtils.N_updatePersonalSex);
        entity.addBodyParameter("appuserId",UserManager.getUser().getAppuserId()+"");
        entity.addBodyParameter("arg1",gender+"");

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.w("44444",result);
                NetData data = JSON.parseObject(result, NetData.class);

                int code=data.getResult();
                if (code == 200) {
                    UIUtils.showToast("修改成功");
                    UserManager.getUser().setSex(gender);

                } else if (code == 500) {
                    UIUtils.showToast("服务器异常");
                } else {
                    UIUtils.showToast("程序异常");
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

    private void changePhone() {
        Intent intent=new Intent(mActivity,CheckPhoneActivity.class);
        intent.putExtra("flag",2);
        startActivity(intent);
    }

    private void changePass() {
        Intent intent=new Intent(mActivity,CheckPhoneActivity.class);
        intent.putExtra("flag",1);
        startActivity(intent);
    }

    private void changeNickname(final String nickname) {
        RequestParams entity = new RequestParams(UrlUtils.N_updatePersonalNickName);
        entity.addBodyParameter("nickName",nickname);
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData data = JSON.parseObject(result, NetData.class);
                int code = data.getResult();

                if (code == 200) {
                    UIUtils.showToast("修改成功");
                    UserManager.getUser().setNickName(nickname);
                    dialog.dismiss();

                } else if (code == 500) {
                    UIUtils.showToast("服务器异常");
                } else {
                    UIUtils.showToast("程序异常");
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

    private void changeIcon(final String path){


        final ProgressDialog dialog=new ProgressDialog(mActivity);
        dialog.setMessage("上传中...");

        dialog.show();

        final File file=new File(path);

        RequestParams entity = new RequestParams(UrlUtils.N_updatePersonalHeadIcon);
        entity.addBodyParameter("file",file);
        entity.addBodyParameter("appuserId",UserManager.getUser().getAppuserId()+"");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                /*{"info":{"headIcon":"http://smartcity.blueapp.com.cn:8088/smartchina/static/images/upload/058540005d044d1a8edda61396464597_file.png"},
                "message":"修改成功","result":"200"}*/
                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult()==200) {
                    UIUtils.showToast("修改成功");
                    JSONObject object = JSON.parseObject(netData.getInfo());

                    String headIcon = object.getString("headIcon");
                    x.image().bind(icon_image,headIcon);

                    UserManager.getUser().setHeadIcon(headIcon);
                    /*存储当前头像*/
                    //FileUtils.copyFile(path, FileUtils.USERICON);

                    finish();

                }else {
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
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        x.view().inject(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (UserManager.isLogin()) {
            User user = UserManager.getUser();


            x.image().bind(icon_image,user.getHeadIcon(), xUtilsManager.getCircleImageOption());

            int sex = user.getSex();
            if (sex ==1) {
                gender.check(R.id.man);
            }else if (sex ==2){
                gender.check(R.id.woman);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*图片选择回调*/
    @Override
    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {

        window.dismiss();

        String path = resultList.get(0).getPhotoPath();


        if (reqeustCode == 100) {
            /*拍照*/
            changeIcon(path);

        } else if (reqeustCode == 200) {
            /*相册*/
            changeIcon(path);
        }

    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {

    }

}
