package com.blue.rchina.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.BitMapUtils;
import com.blue.rchina.utils.FileUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.GalleryFinal.OnHanlderResultCallback;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import static com.blue.rchina.manager.LocationManager.location;


/**
 * 身边功能的上传
 */
public class UploadActivity extends BaseActivity {

    /***
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /***
     * 使用相册中的图片
     */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;

    PopupWindow mPopupWindow;//选取图片

    File file;
    List<ImageView> mList = new ArrayList<ImageView>();
    List<String> mFilePaths = new ArrayList<String>();//文件名list
    int count = 0;//计数器
    private Uri mPhotoUri;
    private String mPicPath;
    String username;//用户名
    //    String title;//标题
    String desc;//描述
    int type;//0为爆料 1为头条

    EditText mEt_desc;
    //    EditText mEt_title;
    TextView tv_locate;

    ProgressDialog mDialog;

    public static final int FLAG_UPLOAD_SUCCESS = 1;
    String saveDir = FileUtils.CACHEPATH + File.separator + "uploadimages";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private View line;
    private AMapLocationClient mlocationClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initData();
        initView();
    }

    private  void locate(){

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
                        location=amapLocation;
                        tv_locate.setText(location.getAddress());

                        mlocationClient.stopLocation();
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                        Log.w("4444","location faild"+amapLocation.getErrorInfo());
                    }
                }
            }
        });
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000*6);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期
        mlocationClient.startLocation();

    }

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white, "发布", -1);

        type = getIntent().getIntExtra("type", 0);
        String title;

        tv_locate = (TextView) findViewById(R.id.tv_locate);
        if (location != null) {
            if (TextUtils.isEmpty(location.getAddress())) {
                tv_locate.setText(location.getAddress());
            } else {
                tv_locate.setText("定位失败");
                locate();
            }
        }
        mEt_desc = (EditText) findViewById(R.id.et_desc);
        line = (View) findViewById(R.id.line);

        mList.add((ImageView) findViewById(R.id.iv_photo1));
        mList.add((ImageView) findViewById(R.id.iv_photo2));
        mList.add((ImageView) findViewById(R.id.iv_photo3));
        mList.add((ImageView) findViewById(R.id.iv_photo4));
        mList.add((ImageView) findViewById(R.id.iv_photo5));
        mList.add((ImageView) findViewById(R.id.iv_photo6));

        TextView btn_title_right = (TextView) findViewById(R.id.btn_title_right);
        btn_title_right.setText("确定");
        btn_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload(v);
            }
        });
    }

    /**
     * 获取头像
     */
    public void getPicture(View view) {
        if (mFilePaths.size() == 6) {
            UIUtils.showToast("选择图片的数量已达到上限");
            return;
        }
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popView = inflater.inflate(R.layout.popwindow_getpicture1, null);
        ((TextView) popView.findViewById(R.id.tv_desc)).setText("请选择图片");
        mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);// 设置是否允许在外点击使其消失
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        popView.findViewById(R.id.ll_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                mPopupWindow.dismiss();
            }
        });

        popView.findViewById(R.id.ll_pick_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAsDropDown(nodata);
//        mPopupWindow.showAsDropDown(view, -150, 300);
    }

    //拍照
    private void takePhoto() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {

            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            file = new File(saveDir, sdf.format(new Date()) + ".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else
            UIUtils.showToast("SD不存在");
    }

    //相册选择
    private void pickPhoto() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
        int selectSize = 6;
        if (mFilePaths != null && mFilePaths.size() > 0) {
            selectSize -= mFilePaths.size();
        }

        //带配置
        FunctionConfig config = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(selectSize)
                .build();
        GalleryFinal.openGalleryMuti(SELECT_PIC_BY_PICK_PHOTO, config, new OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int i, List<PhotoInfo> list) {
                if (list == null || list.size() == 0)
                    return;
                for (PhotoInfo info : list) {
                    file = new File(saveDir, sdf.format(new Date()) + count + ".jpg");
                    file.mkdirs();
                    //按照800*480分辨率进行压缩
                    Bitmap bp = BitMapUtils.decodeSampledBitmapFromFile(info.getPhotoPath(), 480, 800);
                    BitMapUtils.saveBmp2File(bp, file.getPath());
                    if (file != null && file.exists()) {
                        mFilePaths.add(file.getPath());
                        mList.get(count).setImageBitmap(bp);
                        count++;
                    }
                }
            }

            @Override
            public void onHanlderFailure(int i, String s) {
                UIUtils.showToast("获取相册图片失败");
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mPopupWindow && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
        Bitmap bitmap = null;
        if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {//拍照
            if (resultCode != RESULT_OK) {
                return;
            }
            if (file != null && file.exists()) {
                //获取图片旋转角度。三星拍照时可能会进行旋转。
                int rote = BitMapUtils.readPictureDegree(file.getPath());
                //按照800*480分辨率进行压缩
                bitmap = BitMapUtils.decodeSampledBitmapFromFile(file.getPath(), 480, 800);
//                File f=new File(saveDir, sdf.format(new Date()) + ".jpg");
                BitMapUtils.saveBmp2File(bitmap, file.getPath());
                if (file.exists()) {
                    if (rote != 0)
                        bitmap = BitMapUtils.rote(bitmap, rote);
                    mList.get(count).setImageBitmap(bitmap);
                    mFilePaths.add(file.getPath());
                    count++;
                }
            } else {
                Toast.makeText(this, "照片文件保存失败", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {//相册
            if (resultCode != RESULT_OK) {
                return;
            }
            if (data == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
            mPhotoUri = data.getData();
            mPicPath = mPhotoUri.getPath();
            if (mPhotoUri == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            } else {
                String[] pojo = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(mPhotoUri, pojo, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    mPicPath = cursor.getString(columnIndex);
                }
                File file = null;
                Bitmap bp = null;
                if (mPicPath != null) {
                    try {
                        file = new File(saveDir, sdf.format(new Date()) + ".jpg");
                        file.mkdirs();
                        //按照800*480分辨率进行压缩
                        bp = BitMapUtils.decodeSampledBitmapFromFile(mPicPath, 480, 800);
                        BitMapUtils.saveBmp2File(bp, file.getPath());

                    } catch (Exception e) {
                        Toast.makeText(this, "图片加载出错，请选择正确的图片", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "文件路径错误，获取图片失败", Toast.LENGTH_LONG).show();
                }

                if (file != null && file.exists()) {
                    mFilePaths.add(file.getPath());
                    mList.get(count).setImageBitmap(bp);
                    count++;
                }
            }
        }
    }

    boolean isSetTotal;
    boolean isUploading;

    private void upload(View view) {
        isHideLoading(false);

        String editStr = mEt_desc.getText().toString();

        if (location == null) {
            UIUtils.showToast("定位失败，请稍后再试");
            isHideLoading(true);
            return;
        }

        if (!UserManager.isLogin()) {
            UserManager.toLogin();
            isHideLoading(true);
            return;
        }
        if (!TextUtils.isEmpty(editStr)) {

            if (editStr.length() > 210) {
                UIUtils.showToast("文本内容过长，请限制在两百字以内");
                isHideLoading(true);
                return;
            }

            //view.setVisibility(View.GONE);

            RequestParams entity = new RequestParams();
            entity.setUri(UrlUtils.N_report);

            entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
            entity.addBodyParameter("content", editStr);

            entity.addBodyParameter("arg2", location.getLatitude() + "");
            entity.addBodyParameter("arg1", location.getLongitude() + "");
            if (location.getAddress() != null) {
                entity.addBodyParameter("arg3", location.getAddress() + "");
            } else {
                entity.addBodyParameter("arg3", "");
            }


            for (int i = 0; i < mFilePaths.size() && i < 6; i++) {
                entity.addBodyParameter("file" + i, new File(mFilePaths.get(i)));

                Log.w("44444", mFilePaths.get(i));
            }
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

                    finish();
                }
            });
        } else {
            isHideLoading(true);
            UIUtils.showToast("请填写相关内容");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mList.clear();
        mPopupWindow = null;
    }
}
