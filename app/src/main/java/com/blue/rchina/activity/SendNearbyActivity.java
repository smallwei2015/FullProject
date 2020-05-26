package com.blue.rchina.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.CameralUtils;
import com.blue.rchina.utils.FileUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.blue.rchina.views.ImagePicView;
import com.yanzhenjie.permission.AndPermission;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.toolsfinal.BitmapUtils;

import static android.Manifest.permission.CAMERA;

public class SendNearbyActivity extends BaseActivity {


    @ViewInject(R.id.sure)
    TextView sure;

    @ViewInject(R.id.send_edit)
    EditText editText;

    @ViewInject(R.id.pic_img)
    ImagePicView picker;
    @ViewInject(R.id.send_scroll)
    HorizontalScrollView scroll;
    @ViewInject(R.id.send_phone)
    EditText phone;


    private AMapLocation location;
    private PopupWindow mPopupWindow;
    private static final int REQUEST_CODE_CAMERA = 800;
    private static final int SELECT_PIC_BY_PICK_PHOTO = 900;
    String saveDir = FileUtils.CACHEPATH + File.separator + "uploadimages";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private int count = 0;
    private GalleryFinal.OnHanlderResultCallback callback;
    public AMapLocationClient mlocationClient;
    public List<ImagePicView.ImgData> imgDatas;
    public ImagePicView.ImgAdapter adapter;

    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white, "社区报修", "记录");
        phone.setVisibility(View.VISIBLE);


        imgDatas = new ArrayList<>();
        imgDatas.add(new ImagePicView.ImgData("", imgDatas.size() + 1));
        picker.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();
                if ((tag == imgDatas.size() - 1)) {
                    getImage();
                } else {

                }
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(v);
            }
        });

        adapter = picker.new ImgAdapter(imgDatas);
        picker.setAdapter(adapter);

    }

    @Override
    public void initData() {
        super.initData();

        if (false) {
            if (location != null) {
                //locate.setText(getDisplayString());
            } else {
                locate();
            }
        }
    }

    @Override
    public void onRightTextClick(View v) {
        super.onRightTextClick(v);

        Intent intent = new Intent(mActivity,NearbyRecordActivity.class);
        startActivity(intent);
    }

    private void locate() {

        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {

                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        location = amapLocation;
                        //locate.setText(getDisplayString());

                        mlocationClient.stopLocation();
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                        Log.w("4444", "location faild" + amapLocation.getErrorInfo());
                    }
                }
            }
        });
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000 * 6);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期
        mlocationClient.startLocation();

    }

    private String getDisplayString() {

        if (location != null) {

            if (!TextUtils.isEmpty(location.getAoiName())) {
                return location.getCity() + "·" + location.getAoiName();
            } else if (!TextUtils.isEmpty(location.getStreet())) {
                return location.getCity() + "·" + location.getStreet();
            } else if (!TextUtils.isEmpty(location.getPoiName())) {
                return location.getCity() + "·" + location.getPoiName();
            } else {
                return location.getAddress();
            }

        }
        return "定位失败";
    }

    private void send(final View view) {

        String editStr = editText.getText().toString();
        String phoneStr = phone.getText().toString().trim();
        if (!UserManager.isLogin()) {
            UserManager.toLogin();
            return;
        }

        if (TextUtils.isEmpty(phoneStr)) {
            UIUtils.showToast("请输入手机号");
            return;
        }

        isHideLoading(false);


        if (!TextUtils.isEmpty(editStr)) {

            RequestParams entity = new RequestParams(UrlUtils.N_reportCommunity);

            entity.addBodyParameter("phone", phoneStr);

            entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
            entity.addBodyParameter("content", editStr);


            for (int i = 0; i < imgDatas.size() && i < 6; i++) {
                String path = imgDatas.get(i).getPath();
                if (!TextUtils.isEmpty(path)) {
                    File file = new File(path);
                    entity.addBodyParameter("file" + i, file);
                }

            }

            view.setEnabled(false);
            x.http().post(entity, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    JSONObject object = JSON.parseObject(result);
                    Integer code = object.getInteger("result");
                    if (code == 200) {
                        UIUtils.showToast("发布成功");
                        finish();
                    } else {
                        UIUtils.showToast("发布失败");
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                    UIUtils.showToast("网络请求失败");

                    finish();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    isHideLoading(true);
                    view.setEnabled(true);
                }
            });
        } else {
            isHideLoading(true);
            UIUtils.showToast("请填写相关内容");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_nearby);

        x.view().inject(this);
        initView();
        initData();
    }

    private void getImage() {
        View popView = LayoutInflater.from(SendNearbyActivity.this).inflate(R.layout.popwindow_getpicture, null);
        mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);// 设置是否允许在外点击使其消失
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //按照分辨率压缩4倍
        callback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList == null || resultList.size() == 0)
                    return;
                for (PhotoInfo info : resultList) {
                    File file = new File(saveDir, sdf.format(new Date()) + count + ".jpg");
                    file.mkdirs();

                    //按照分辨率压缩4倍
                    Bitmap bitmap = BitmapUtils.compressBitmapSmall(info.getPhotoPath(), 4);
                    BitmapUtils.saveBitmap(bitmap, file);

                    imgDatas.add(imgDatas.size() - 1, new ImagePicView.ImgData(file.getPath(), imgDatas.size() - 1));

                    count++;
                }
                adapter.notifyDataChanged();
                scroll.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    }
                }, 100);

            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                UIUtils.showToast("未成功获取图片");
            }
        };
        popView.findViewById(R.id.take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasPermission(CAMERA)) {

                    try {

                        if(CameralUtils.hasCamera()) {
                            GalleryFinal.openCamera(REQUEST_CODE_CAMERA, callback);
                        }else {
                            UIUtils.showToast("该设备无法使用摄像头");
                        }
                    }catch (Exception e){
                        UIUtils.showToast("该设备无法使用摄像头");
                    }

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{CAMERA}, 200);
                    }
                }
                mPopupWindow.dismiss();
            }
        });

        popView.findViewById(R.id.pick_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAsDropDown(picker);
    }


    private void pickPhoto() {
        int selectSize = 7;
        if (imgDatas != null && imgDatas.size() > 0) {
            selectSize -= imgDatas.size();
        }

        //带配置
        FunctionConfig config = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(selectSize)
                .build();

        GalleryFinal.openGalleryMuti(SELECT_PIC_BY_PICK_PHOTO, config, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int i, List<PhotoInfo> list) {
                if (list == null || list.size() == 0)
                    return;
                for (PhotoInfo info : list) {

                    File file = new File(saveDir, sdf.format(new Date()) + count + ".jpg");
                    file.mkdirs();


                    File rawFile = new File(info.getPhotoPath());
                    Bitmap bitmap = null;
                    //按照分辨率压缩图片
                    if (rawFile.length() > 1024 * 1024 * 8) {
                        //UIUtils.showToast("图片过大，无法上传");
                        bitmap = BitmapUtils.compressBitmapSmall(info.getPhotoPath(), 8);
                    } else if (rawFile.length() > 1024 * 1024 * 4) {
                        bitmap = BitmapUtils.compressBitmapSmall(info.getPhotoPath(), 4);
                    } else if (rawFile.length() > 1024 * 1024) {
                        bitmap = BitmapUtils.compressBitmapSmall(info.getPhotoPath(), 3);
                    } else if (rawFile.length() > 1024 * 1024 / 2) {
                        bitmap = BitmapUtils.compressBitmapSmall(info.getPhotoPath(), 2);
                    } else {
                        bitmap = BitmapUtils.compressBitmapSmall(info.getPhotoPath(), 1);
                    }
                    BitmapUtils.saveBitmap(bitmap, file);

                    count++;
                    imgDatas.add(imgDatas.size() - 1, new ImagePicView.ImgData(file.getPath(), imgDatas.size() - 1));

                }
                adapter.notifyDataChanged();

                scroll.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    }
                }, 100);

            }

            @Override
            public void onHanlderFailure(int i, String s) {
                UIUtils.showToast("获取相册图片失败");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 200:
                boolean hasRight = true;
                for (int i = 0; i < grantResults.length; i++) {
                    hasRight = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                }

                if (hasRight) {
                    //授权成功之后，调用系统相机进行拍照操作等
                    GalleryFinal.openCamera(REQUEST_CODE_CAMERA, callback);
                } else {
                    //用户授权拒绝之后，友情提示一下就可以了
                    UIUtils.showToast("拍照权限被拒绝系统将不能正常使用");
                    AndPermission.defaultSettingDialog(mActivity, 400).show();
                }
                break;

        }

    }

    private boolean hasPermission(String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        }
        return false;

    }
}
