package com.blue.rchina;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.blue.rchina.activity.AdActivity;
import com.blue.rchina.activity.CitySelectListActivity;
import com.blue.rchina.activity.CollectActivity;
import com.blue.rchina.activity.FragmentHolderActivity;
import com.blue.rchina.activity.InfoActivity;
import com.blue.rchina.activity.MyInteractActivity;
import com.blue.rchina.activity.MyWalletActivity;
import com.blue.rchina.activity.NearbyActivity;
import com.blue.rchina.activity.NearbySelectListActivity;
import com.blue.rchina.activity.OrderActivity;
import com.blue.rchina.activity.SearchActivity;
import com.blue.rchina.activity.SendInteractActivity;
import com.blue.rchina.activity.SettingActivity;
import com.blue.rchina.activity.ShoppingCartActivity;
import com.blue.rchina.activity.UserCenterActivity;
import com.blue.rchina.activity.WeatherActivity;
import com.blue.rchina.adapter.MenuAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.AreaInfo;
import com.blue.rchina.bean.Channel;
import com.blue.rchina.bean.MenuItem;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.Uninon;
import com.blue.rchina.bean.Weather;
import com.blue.rchina.fragment.InteractFragment;
import com.blue.rchina.fragment.LifeFragment;
import com.blue.rchina.fragment.MallContainerFragment;
import com.blue.rchina.fragment.MallFragment;
import com.blue.rchina.fragment.NewsFragment;
import com.blue.rchina.fragment.NewsMainFragment;
import com.blue.rchina.fragment.TraveContainerFragment;
import com.blue.rchina.manager.LocationInteface;
import com.blue.rchina.manager.LocationManager;
import com.blue.rchina.manager.UserInterface;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.FileUtils;
import com.blue.rchina.utils.GPSUtils;
import com.blue.rchina.utils.PackageUtils;
import com.blue.rchina.utils.RightUtils;
import com.blue.rchina.utils.SPUtils;
import com.blue.rchina.utils.SystemBrightUtils;
import com.blue.rchina.utils.UIDUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.blue.rchina.utils.WeatherUtils;
import com.blue.rchina.views.ProDialog;
import com.caption.update.UpdateUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.blue.rchina.manager.UserManager.action_cityChange;

public class Main2Activity extends BaseActivity {

    @ViewInject(R.id.activity_main2)
    LinearLayout main_layout;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    // 侧滑的DrawerLayout
    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawer;
    @ViewInject(R.id.top_view)
    public View headPadding;
    @ViewInject(R.id.navi_holder)
    View navi;


    //侧拉菜单view

    @ViewInject(R.id.tv_appname)
    TextView appName;
    @ViewInject(R.id.lv_menu)
    ListView menuList;
    @ViewInject(R.id.lv_profit)
    RelativeLayout menuProfit;
    @ViewInject(R.id.tv_cityname)
    TextView cityName;
    @ViewInject(R.id.tv_temp)
    TextView menuTemp;
    @ViewInject(R.id.iv_weather_icon)
    ImageView menuWeaIcon;
    @ViewInject(R.id.tv_weaher)
    TextView menuWea;
    @ViewInject(R.id.menu_top_view)
    View menuHeadPadding;
    @ViewInject(R.id.menu_setting)
    LinearLayout menuSetting;
    @ViewInject(R.id.menu_night)
    LinearLayout menuNight;
    @ViewInject(R.id.menu_weather)
    LinearLayout menuWeather;
    @ViewInject(R.id.tv_change)
    TextView menuChange;
    @ViewInject(R.id.change_city)
    LinearLayout change_city;
    @ViewInject(R.id.menu_date)
    TextView menuDate;

    //主界面view
    @ViewInject(R.id.main)
    FrameLayout main;

    @ViewInject(R.id.bottom_container)
    LinearLayout bottomContainer;
    /*@ViewInject(R.id.news)
    View news;
    @ViewInject(R.id.life)
    View life;
    @ViewInject(R.id.interact)
    View interact;
    @ViewInject(R.id.vip)
    View vip;
    @ViewInject(R.id.mall)
    View mall;*/

    @ViewInject(R.id.top_mall)
    RelativeLayout mall;
    @ViewInject(R.id.top_mall_right)
    ImageView mall_icon;
    @ViewInject(R.id.top_mall_count)
    TextView mall_count;
    @ViewInject(R.id.top_title)
    TextView title;
    @ViewInject(R.id.top_right)
    ImageView right;
    @ViewInject(R.id.top_search)
    RelativeLayout search;
    @ViewInject(R.id.top_search_edit)
    EditText search_edit;
    @ViewInject(R.id.icon)
    ImageView icon;

    private FragmentTransaction trascation;
    private int curPos;

    private PopupWindow window;
    private LifeFragment lifeFragment;
    private InteractFragment interactFragment;
    private LifeFragment vipFragment;
    private MallFragment mallFragmnet;
    private NewsFragment newsFragment;
    public int height;
    public int systemBright;
    private int appBright = 80;
    private BroadcastReceiver cityReceiver;


    public long lastTime;
    public AreaInfo curCity;
    public String defaultCity;
    public List<BaseFragment> fragments;
    public List<View> bottomViews;
    public List<Channel> channels;
    public MenuAdapter menuAdapter;
    public List<MenuItem> menuItemArrayList;
    public String defaultCapital;
    private int getWeatherCount = 0;
    public BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if (savedInstanceState != null) {
            /*后台杀死就重新创建,避免fragment状态丢失造成显示混乱*/
            Intent intent = new Intent(mActivity, AdActivity.class);
            startActivity(intent);

            android.os.Process.killProcess(android.os.Process.myPid());
        }

        x.view().inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            headPadding.setVisibility(View.VISIBLE);
            int statusBarHeight = UIUtils.getStatusBarHeight(mActivity);
            if (statusBarHeight > 0) {
                headPadding.getLayoutParams().height = statusBarHeight;
            }

            menuHeadPadding.setVisibility(View.VISIBLE);
            if (statusBarHeight > 0) {
                menuHeadPadding.getLayoutParams().height = statusBarHeight;
            }

        } else {
            headPadding.setVisibility(View.GONE);
            menuHeadPadding.setVisibility(View.GONE);
        }
        navi.setVisibility(View.GONE);

        initData();
        initView();

        init();
        initBright();
        UpdateUtils.getInit().update(mActivity, 1);

    }

    @Override
    public void initPermission() {
        super.initPermission();

        /*申请权限*/
        AndPermission.with(this)
                .requestCode(200)
                .permission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {

                        if (!AndPermission.hasPermission(mActivity,Manifest.permission.READ_PHONE_STATE)) {
                            AndPermission.defaultSettingDialog(mActivity, 2001).show();
                        }else if (!AndPermission.hasPermission(mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            AndPermission.defaultSettingDialog(mActivity, 2002).show();
                        }
                    }
                }).callback(this).start();
    }

    @PermissionYes(200)
    private void getPermissionYes(List<String> grantedPermissions) {
        // TODO 申请权限成功。
        if (grantedPermissions.contains(Manifest.permission.READ_PHONE_STATE)) {
            openApp();
        }

        if (grantedPermissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            FileUtils.creatImgFromAsset(getApplicationContext(),"qrCode.png");
            FileUtils.creatImgFromAsset(getApplicationContext(),"logo.png");
        }
    }
    @PermissionNo(200)
    private void getPermissionNo(List<String> deniedPermissions) {
        // TODO 申请权限失败。
        if (deniedPermissions.contains(Manifest.permission.READ_PHONE_STATE)) {
            AndPermission.defaultSettingDialog(this, 2001).show();
        }

        if (deniedPermissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            AndPermission.defaultSettingDialog(this, 2002).show();
        }
    }


    @Override
    public void initView() {
        super.initView();
        serverDie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                isHideLoading(false);
                if (curCity != null) {
                    changeSt(curCity, null);
                    v.setClickable(false);
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            v.setClickable(true);
                        }
                    }, 1000);
                } else {
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isHideLoading(true);
                        }
                    }, 1000);

                }
            }
        });
    }

    private void initBright() {


        boolean isNightMode = SPUtils.getSP().getBoolean("isNightMode", false);

        if (isNightMode) {
            UIUtils.showToast("当前处于夜间模式");
        }
        try {
            systemBright = SystemBrightUtils.getSystemBright(mActivity);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*退出应用还原系统亮度*/
        /*if (systemBright > 0) {
            SystemBrightUtils.setSystemBright(mActivity, systemBright);
            SystemBrightUtils.stopAutoBrightness(mActivity);
        }*/

        if (cityReceiver != null) {
            unregisterReceiver(cityReceiver);
        }

        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    private void init() { //向系统设置



        /*请求权利*/
        /*判断是否有定位模块，没有就去显示北京*/
        if (GPSUtils.hasGPSDevice(mActivity)) {
            if (!AndPermission.hasPermission(mActivity,Manifest.permission.ACCESS_FINE_LOCATION)) {
                AndPermission.with(this)
                        .requestCode(300)
                        .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .rationale(new RationaleListener() {
                            @Override
                            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                AndPermission.defaultSettingDialog(mActivity, 3001).show();
                            }
                        }).callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, List<String> grantedPermissions) {
                        // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。

                        initLocation();

                    }

                    @Override
                    public void onFailed(int requestCode, List<String> deniedPermissions) {
                        // 权限申请失败回调。

                        AndPermission.defaultSettingDialog(mActivity, 3001).show();

                        boolean firstOpen = SPUtils.getSP().getBoolean("firstOpen", true);
                        if (firstOpen) {
                            changeToLocation("北京市");
                        }
                    }
                }).start();
            } else {
                initLocation();
            }
        }else {
            boolean firstOpen = SPUtils.getSP().getBoolean("firstOpen", true);
            if (firstOpen) {
                changeToLocation("北京市");
            }
        }

        setSupportActionBar(toolbar); //设置线性管理器

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                if (isOpen) {
                    //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                    imm.showSoftInput(main, InputMethodManager.SHOW_FORCED);
                    imm.hideSoftInputFromWindow(main.getWindowToken(), 0); //强制隐藏键盘

                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);*/
            }
        };
        drawer.addDrawerListener(toggle); //该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
        toggle.syncState(); //为侧边栏的每一个子item设置监听

        curCity = null;
        String area = SPUtils.getSP().getString("area", null);
        if (!TextUtils.isEmpty(area)) {
            AreaInfo areaInfo = parseObject(area, Uninon.class);
            curCity = areaInfo;
        }

        defaultCity = curCity != null ? curCity.getAreaName() : "北京市";
        defaultCapital = curCity != null ? curCity.getProvinceCapital() : "北京市";
        initMain(defaultCity);
        initMenu(defaultCity);

        registerCityReceiver();
        setUserHandler(new UserInterface() {
            @Override
            public void loginIn() {
                initMenu(defaultCity);
            }

            @Override
            public void loginOut() {
                initMenu(defaultCity);
            }

            @Override
            public void userStateChange() {

            }
        });

    }

    private void initLocation() {
        LocationManager.initLocation(mActivity, new LocationInteface() {
            @Override
            public void locationSuccess(AMapLocation location) {

                /*是否第一次打开app*/
                boolean firstOpen = SPUtils.getSP().getBoolean("firstOpen", true);
                /*自动切换是否打开*/
                boolean agreeAutoChange = SPUtils.getSP().getBoolean("agreeAutoChange", false);
                if (firstOpen) {
                    /*第一次打开就切换到当前定位城市*/
                    changeToLocation(location.getCity());
                    return;
                }else {


                    String area = SPUtils.getSP().getString("area", "");

                    Uninon areaInfo = JSON.parseObject(area, Uninon.class);
                    if (areaInfo != null&&!TextUtils.isEmpty(areaInfo.getUnionName())) {
                        /*当前若为联盟就不去切换城市*/
                        return;
                    }

                    /*当前选择城市不为定位城市，提示*/
                    if (location != null && curCity != null && !location.getCity().equals(curCity.getProvinceCapital())) {

                        if (agreeAutoChange) {
                            /*自动切换打开也切换到当前定位城市*/
                            changeToLocation(location.getCity());
                        }else {
                            String lastLocationCity = SPUtils.getSP().getString("lastLocationCity", "");

                            if (location.getCity().equals(lastLocationCity)) {
                                Log.w("44444", "用户取消了当前城市定位提示");
                            } else {
                                getAreaByLocation(location.getCity());
                            }
                        }
                    }
                }
            }

            @Override
            public void locationFaild() {
                boolean firstOpen = SPUtils.getSP().getBoolean("firstOpen", true);
                if (firstOpen) {
                    changeToLocation("北京市");
                }
            }
        });
    }

    private void openApp() {
        RequestParams entity = new RequestParams(UrlUtils.N_openAPP);

        if (UserManager.isLogin()) {
            entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        } else {
            entity.addBodyParameter("appuserId", "");
        }

        entity.addBodyParameter("arg1", UIDUtils.getUID(mActivity));
        entity.addBodyParameter("arg2", "1");

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            entity.addBodyParameter("arg3", info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        entity.addBodyParameter("arg4", "0");

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.w("4444", result);
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

    private void changeToLocation(String city) {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveAreaByLocation);
        entity.addBodyParameter("arg1", city);
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                NetData data = JSON.parseObject(result, NetData.class);
                if (data.getResult() == 200) {

                    List<AreaInfo> areaInfos = JSON.parseArray(data.getInfo(), AreaInfo.class);

                    if (areaInfos != null && areaInfos.size() > 0) {
                        AreaInfo areaInfo = areaInfos.get(0);
                        if (areaInfo != null && areaInfo.getIsOperate() == 1) {
                            changeCity(areaInfo);
                        } else {
                            changeToLocation("北京市");
                        }
                    } else {
                        changeToLocation("北京市");
                    }
                } else {
                    changeToLocation("北京市");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                changeToLocation("北京市");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getAreaByLocation(final String cityName) {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveAreaByLocation);
        entity.addBodyParameter("arg1", cityName);
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData data = parseObject(result, NetData.class);
                if (data.getResult() == 200) {

                    List<AreaInfo> areaInfos = JSON.parseArray(data.getInfo(), AreaInfo.class);


                    if (areaInfos != null && areaInfos.size() > 0) {
                        final AreaInfo areaInfo = areaInfos.get(0);
                        if (areaInfo != null) {

                            if (cityName.equals(areaInfo.getAreaName())) {
                                if (areaInfo.getIsOperate() == 1) {
                                    ProDialog proDialog = ProDialog.newInstance("提示", "当前定位到您位于" + cityName + "，是否要切换到当前定位城市？", false, cityName);

                                    proDialog.setmListener(new ProDialog.ConfirmDialogListener() {
                                        @Override
                                        public void onConform(ProDialog proDialog) {
                                            if (areaInfo.getIsOperate() == 1) {
                                                changeCity(areaInfo);
                                            } else {
                                                UIUtils.showToast("当前城市未开通");
                                            }
                                            proDialog.dismiss();
                                        }
                                    });

                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.add(proDialog, "pop");
                                    transaction.commitAllowingStateLoss();

                                } else {
                                    Log.w("4444", "当前定位城市开通，但是没有operate");
                                }
                            } else {
                                Log.w("4444", "当前定位城市未开通,返回北京");
                            }
                        }
                    }


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

    private void registerCityReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(action_cityChange);

        cityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                AreaInfo area = (AreaInfo) intent.getSerializableExtra("area");
                if (area != null) {
                    changeCity(area);
                } else {
                    UIUtils.showToast("请选择正确城市");
                }
            }
        };
        registerReceiver(cityReceiver, filter);


        IntentFilter filterMall = new IntentFilter();
        filterMall.addAction("action_change_mall");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int count = intent.getIntExtra("count", 0);
                setMallCount(count);
            }
        };
        registerReceiver(receiver, filterMall);
    }

    private void changeCity(AreaInfo area) {
        ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setMessage("切换城市中...");
        dialog.show();

        curCity = area;

        changeSt(area, dialog);
        changeMenu(area);


    }

    private void changeSt(final AreaInfo area, final Dialog dialog) {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveApplicationStructure);
        entity.addQueryStringParameter("areaId", area.getAreaId());

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData data = parseObject(result, NetData.class);
                if (data.getResult() == 200) {
                    /*城市切换成功就存储起来*/
                    if (area != null) {
                        String str = JSON.toJSONString(area);
                        Log.w("4444",str);
                        SPUtils.getSP().edit().putString("area", str).putBoolean("firstOpen", false).apply();
                    }
                    isServerDie(false);

                    channels = JSON.parseArray(data.getInfo(), Channel.class);

                    if (channels != null && channels.size() > 0) {
                        clearStruture();

                        List<String> names = new ArrayList<String>();
                        for (int i = 0; i < channels.size(); i++) {
                            Channel channel = channels.get(i);
                            names.add(channel.getTitle());
                        }
                        initFragmentAndBottom(names);

                        if (names.isEmpty()) {
                            isNodata(true);
                        } else {
                            isNodata(false);
                        }

                    } else {
                        UIUtils.showToast("当前选择城市栏目为空");

                    }

                } else {
                    UIUtils.showToast(data.getMessage() == null ? "" : data.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                clearStruture();
                isServerDie(true);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isHideLoading(true);

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    private void clearStruture() {

        bottomContainer.removeAllViews();
        trascation = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            trascation.remove(fragments.get(i));
        }
        trascation.commitAllowingStateLoss();
        fragments.clear();
        bottomViews.clear();

    }

    private void changeMenu(AreaInfo area) {
        //cityName.setText(area.getAreaName());
        appName.setText(area.getApplicationName());
        getWeather(area.getAreaName());
    }

    private void initMenu(String city) {
        getWeather(city);

        cityName.setText(defaultCity);
        if (curCity != null) {
            appName.setText(curCity.getApplicationName());
        }

        menuWeather.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, WeatherActivity.class);
                intent.putExtra("city", curCity);
                startActivity(intent);
            }
        });
        menuSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SettingActivity.class);
                startActivity(intent);
            }
        });

        menuNight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                SharedPreferences sp = SPUtils.getSP();
                boolean isNightMode = sp.getBoolean("isNightMode", false);

                if (!isNightMode) {

                    sp.edit().putBoolean("isNightMode", true).apply();
                    SystemBrightUtils.setCurBright(mActivity, 60);
                    UIUtils.showToast("夜间模式已打开");
                } else {

                    sp.edit().putBoolean("isNightMode", false).apply();
                    SystemBrightUtils.setCurBright(mActivity, systemBright);
                    UIUtils.showToast("夜间模式已关闭");
                }
            }
        });
        change_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CitySelectListActivity.class);
                startActivity(intent);
            }
        });


        menuItemArrayList = new ArrayList<>();

        boolean isVolunteer = false;
        final String username = UserManager.isLogin() ? UserManager.getUser().getNickName() : "未登录";
        if (isVolunteer)
            menuItemArrayList.add(new MenuItem(username, R.drawable.denglu, R.mipmap.xin));
        else
            menuItemArrayList.add(new MenuItem(username, R.drawable.denglu));
//        menuItemArrayList.add(new MenuItem("我的监控", R.drawable.icon_jiankong));
//        menuItemArrayList.add(new MenuItem("我的投票", R.drawable.icon_toupiao));
        menuItemArrayList.add(new MenuItem("我的动态", R.drawable.toutiao));
        MenuItem item_house = new MenuItem("我的订单", R.drawable.dingdan);
        if (SPUtils.getSP().getBoolean("RECEIVE_HOUSE", false))
            item_house.setMoreRes(R.drawable.circle_red_5_5);
        menuItemArrayList.add(item_house);
//        menuItemArrayList.add(new MenuItem("我的积分", R.drawable.icon_store));

        menuItemArrayList.add(new MenuItem("我的社区", R.drawable.shequ));
        menuItemArrayList.add(new MenuItem("我的收藏", R.drawable.shoucang));

        /*2017.2.20 by vode ,添加我的钱包和我的管理*/
        menuItemArrayList.add(new MenuItem("我的钱包", R.drawable.qianbao));


        //menuItemArrayList.add(new MenuItem("我的好友", R.drawable.haoyou));
        MenuItem item_message = new MenuItem("我的消息", R.drawable.xiaoxi);
        if (SPUtils.getSP().getBoolean("RECEIVE_MESSAGE", false))
            item_message.setMoreRes(R.drawable.circle_red_5_5);
        menuItemArrayList.add(item_message);

        menuItemArrayList.add(new MenuItem("我的管理", R.drawable.guanli));

        menuAdapter = new MenuAdapter(mActivity, menuItemArrayList);
        menuList.setAdapter(menuAdapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!UserManager.isLogin()) {
                    UserManager.toLogin();
                    return;
                }

                MenuItem item = (MenuItem) parent.getAdapter().getItem(position);
                Intent intent;

                String itemName = item.getItemName();
                if (itemName.equals("登陆")) {
                    intent = new Intent(mActivity, UserCenterActivity.class);
                    startActivity(intent);
                } else if (itemName.equals("我的动态")) {
                    intent = new Intent(mActivity, MyInteractActivity.class);
                    startActivity(intent);
                } else if (itemName.equals("我的订单")) {
                    intent = new Intent(mActivity, OrderActivity.class);
                    startActivity(intent);
                } else if (itemName.equals("我的社区")) {

                    /*判断用户是否绑定社区*/
                    if (TextUtils.isEmpty(UserManager.getUser().getCommunityName())) {
                        intent = new Intent(mActivity, NearbySelectListActivity.class);
                        startActivity(intent);
                    } else {
                        intent = new Intent(mActivity, NearbyActivity.class);
                        startActivity(intent);
                    }

                } else if (itemName.equals("我的收藏")) {
                    intent = new Intent(mActivity, CollectActivity.class);
                    intent.putExtra("title", "我的收藏");
                    startActivity(intent);
                } else if (itemName.equals("我的钱包")) {
                    intent = new Intent(mActivity, MyWalletActivity.class);
                    startActivity(intent);
                } else if (itemName.equals("我的好友")) {
                    intent = new Intent(mActivity, FragmentHolderActivity.class);
                    intent.putExtra("title", itemName);
                    startActivity(intent);
                } else if (itemName.equals("我的消息")) {
                    intent = new Intent(mActivity, InfoActivity.class);
                    startActivity(intent);
                } else if (itemName.equals("我的管理")) {
                    //popUp();
                    openRchina4b();
                } else if(itemName.equals("商品管理")){
                    openRchina4b();
                }else {
                    intent = new Intent(mActivity, UserCenterActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void openRchina4b() {
        AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);

        builder.setTitle("提示：");
        builder.setMessage("打开融城管家APP?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String uri = "http://openbox.mobilem.360.cn/index/d/sid/3954347";

                if (PackageUtils.isAvilible(mActivity, "com.blue.rchina4b")) {
                    Intent resolveIntent = getPackageManager().getLaunchIntentForPackage("com.blue.rchina4b");
                    // 这里的packname就是从上面得到的目标apk的包名
                    // 启动目标应用
                    startActivity(resolveIntent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void popUp() {

        View view = LayoutInflater.from(mActivity).inflate(R.layout.manager_pop_layout, null);


        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);


        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x1f000000);
        window.setBackgroundDrawable(dw);


        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.manager_popwindow_anim_style);
        // 在底部显示
        window.showAtLocation(main_layout,
                Gravity.BOTTOM, 0, 0);


        View cancel = view.findViewById(R.id.pop_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });


        /*根据用户的类型判断用户是否具有管理权限*/
        View community = view.findViewById(R.id.pop_community);

        if (SPUtils.getSP().getInt("isCommunity",1)==1) {
            community.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserManager.isLogin()) {

                    }else
                        UIUtils.showToast("请先登录");
                    window.dismiss();
                }
            });
        }else {
            community.setVisibility(View.GONE);
        }

        View goods = view.findViewById(R.id.pop_goods);
        if (SPUtils.getSP().getInt("isGoods",1)==1) {

            goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openRchina4b();
                    window.dismiss();
                }
            });
        }else {
            goods.setVisibility(View.GONE);
        }

        View landscape = view.findViewById(R.id.pop_landscape);
        if (SPUtils.getSP().getInt("isScenery",1)==1) {
            landscape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openRchina4b();
                    window.dismiss();
                }
            });
        }else {
            landscape.setVisibility(View.GONE);
        }

        View channel = view.findViewById(R.id.pop_channel);
        if (SPUtils.getSP().getInt("isChannel",1)==1) {

            channel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                }
            });
        }else {
            channel.setVisibility(View.GONE);
        }

    }

    private void initMain(String city) {


        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        search_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SearchActivity.class);
                startActivity(intent);
            }
        });

        List<String> names = new ArrayList<>();
        /*names.add("首页");
        names.add("动态");
        names.add("服务");
        names.add("政务");*/
        //names.add("商城");
        initFragmentAndBottom(names);

        if (curCity != null) {
            changeSt(curCity, null);
        }


    }

    private void initFragmentAndBottom(List<String> names) {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        if (bottomViews == null) {
            bottomViews = new ArrayList<>();
        }

        for (int i = 0; i < names.size(); i++) {
            String s = names.get(i);
            insertBottomViewByName(s);
            insertFragmentByName(s);
        }

        if (bottomViews.size() > 0) {
            bottomViews.get(0).performClick();
        }

    }

    private int insertFragmentByName(String name) {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }

        trascation = getSupportFragmentManager().beginTransaction();

        BaseFragment fragment = null;

        if (name.equals("首页")) {
            fragment = new NewsMainFragment();
        } else if (name.equals("动态")) {
            fragment = new InteractFragment();
        } else if (name.equals("服务")) {
            fragment = new LifeFragment();
        } else if (name.equals("政务")) {
            fragment = new LifeFragment();
        } else if (name.equals("商城")) {
            fragment = new MallContainerFragment();
        } else if (name.equals("旅游")) {
            fragment = new TraveContainerFragment();
        } else {
            fragment = new NewsFragment();
        }

        if (fragment != null) {

            if (channels != null && channels.size() > 0) {
                for (int i = 0; i < channels.size(); i++) {
                    if (channels.get(i).getTitle().equals(name)) {
                        Bundle args = new Bundle();
                        args.putSerializable("channel", channels.get(i));
                        fragment.setArguments(args);
                    }
                }
            }
            trascation.add(R.id.main, fragment);
            fragments.add(fragment);
            trascation.hide(fragment);
        } else {
            Log.w("4444", "未找到要添加的fragment");
        }
        trascation.commitAllowingStateLoss();
        return fragments.size();
    }

    private int insertBottomViewByName(final String name) {

        if (bottomViews == null) {
            bottomViews = new ArrayList<>();
        }

        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.bottom_button, null);

        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trascation = getSupportFragmentManager().beginTransaction();
                for (int j = 0; j < bottomViews.size(); j++) {
                    View view = bottomViews.get(j);
                    if (view.equals(v)) {
                        select(v, true);
                        initTop(name, true);
                        trascation.show(fragments.get(j));
                    } else {
                        select(view, false);
                        trascation.hide(fragments.get(j));
                    }
                }

                trascation.commitAllowingStateLoss();
            }
        });
        if (name.equals("首页")) {
            initButton(inflate, "首页", R.drawable.bottom_shouye);
        } else if (name.equals("动态")) {
            initButton(inflate, "动态", R.drawable.bottom_dongtai);
        } else if (name.equals("服务")) {
            initButton(inflate, "服务", R.drawable.bottom_fuwu);
        } else if (name.equals("政务")) {
            initButton(inflate, "政务", R.drawable.bottom_zhengwu);
        } else if (name.equals("商城")) {
            initButton(inflate, "商城", R.drawable.bottom_shangcheng);
        } else if (name.equals("旅游")) {
            initButton(inflate, "旅游", R.drawable.bottom_lvyou);
        } else {
            Log.w("4444", "未找到当前bottom view");
            initButton(inflate, name, R.drawable.bottom_shouye);
        }
        bottomViews.add(inflate);
        bottomContainer.addView(inflate);
        ((LinearLayout.LayoutParams) inflate.getLayoutParams()).weight = 1;

        return bottomViews.size();
    }

    private void initButton(View v, String textStr, int iconId) {
        ImageView icon = (ImageView) v.findViewById(R.id.image);
        TextView text = (TextView) v.findViewById(R.id.text);

        if (text != null) {
            text.setText(textStr);
        }

        if (iconId > 0) {
            icon.setImageResource(iconId);
        }
    }

    private void select(View v, boolean select) {

        ImageView icon = (ImageView) v.findViewById(R.id.image);
        TextView text = (TextView) v.findViewById(R.id.text);

        text.setEnabled(select);
        icon.setSelected(select);
    }

    private void initTop(String titStr, boolean show) {


        if (titStr.equals("首页")) {
            search.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            title.setText(!(curCity == null) ? curCity.getApplicationName() : "融城中国");

            right.setVisibility(View.VISIBLE);
            right.setImageResource(R.mipmap.search_white);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserManager.isLogin()) {
                        Intent intent = new Intent(mActivity, SearchActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        UserManager.toLogin();
                    }

                }
            });
            mall.setVisibility(View.GONE);
        } else if (titStr.equals("动态")) {
            search.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            title.setText(!(curCity == null) ? curCity.getApplicationName() : "融城中国");


            right.setImageResource(R.mipmap.interact_add);
            right.setVisibility(View.VISIBLE);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserManager.isLogin()) {
                        //Intent intent = new Intent(mActivity, UploadActivity.class);
                        Intent intent = new Intent(mActivity, SendInteractActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        UserManager.toLogin();
                    }
                }
            });
            mall.setVisibility(View.GONE);

        } else if (titStr.equals("服务")) {
            search.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            title.setText(!(curCity == null) ? curCity.getApplicationName() : "融城中国");

            right.setVisibility(View.GONE);
            mall.setVisibility(View.GONE);
        } else if (titStr.equals("政务")) {
            search.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            title.setText(!(curCity == null) ? curCity.getApplicationName() : "融城中国");

            right.setVisibility(View.GONE);
            mall.setVisibility(View.GONE);
        } else if (titStr.equals("商城")) {
            search.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            title.setText(!(curCity == null) ? curCity.getApplicationName() : "融城中国");

            mall.setVisibility(View.VISIBLE);

            mall_icon.setImageResource(R.mipmap.shopping_cart);
            mall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserManager.isLogin()) {
                        Intent intent = new Intent(mActivity, ShoppingCartActivity.class);
                        startActivity(intent);
                    } else {
                        UserManager.toLogin();
                    }
                }
            });
            right.setVisibility(View.GONE);
        } else if (titStr.equals("旅游")) {
            search.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            title.setText(!(curCity == null) ? curCity.getApplicationName() : "融城中国");

            right.setVisibility(View.GONE);
            mall.setVisibility(View.GONE);
        } else {
            search.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            title.setText(!(curCity == null) ? curCity.getApplicationName() : "融城中国");

            right.setVisibility(View.GONE);
            mall.setVisibility(View.GONE);
        }
    }

    public void setMallCount(int count) {
        if (count > 0) {
            mall_count.setVisibility(View.VISIBLE);
            if (count > 99) {
                count = 99;
            }
            mall_count.setText(count + "");
        } else {
            mall_count.setVisibility(View.GONE);
        }
    }

    private void getWeather(String city) {
        try {
            RequestParams entity = new RequestParams(String.format(UrlUtils.BWEATHER, URLEncoder.encode(city, "UTF-8")));

            x.http().get(entity, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {

                    Weather weather = parseObject(result, Weather.class);
                    if (weather != null && weather.getResults() != null && weather.getResults().size() > 0) {
                        Weather.ResultsBean.WeatherDataBean weatherDataBean = weather.getResults().get(0).getWeather_data().get(0);

                        String[] split = weatherDataBean.getDate().split("\\(");
                        menuDate.setText(split[0]);
                        menuTemp.setText(WeatherUtils.sortTemperature(weatherDataBean.getTemperature()));

                        menuWea.setText(weatherDataBean.getWeather());

                        menuWeaIcon.setImageResource(WeatherUtils.getWeatherIconRes(weatherDataBean.getWeather()));

                        getWeatherCount = 0;
                    }

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                    //menuWea.setText("获取失败");
                    menuDate.setText(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));

                    getWeatherCount++;
                    /*限定请求次数*/
                    if (getWeatherCount < 3 && curCity != null && curCity.getProvinceCapital() != null) {
                        getWeather(curCity.getProvinceCapital());
                    }
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void onMenuClick(View v) {

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();

            long l = System.currentTimeMillis();
            if (l - lastTime < 1000) {
                super.onBackPressed();
            } else if (l - lastTime < 2000) {
                UIUtils.showToast("双击退出");
            }
            lastTime = l;

        }
    }


    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        boolean hasRight = true;
        for (int i = 0; i < grantResults.length; i++) {
            hasRight = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
        switch (permsRequestCode) {

            case 200:
                if (hasRight) {
                    initLocation();
                } else {
                    //用户授权拒绝之后

                    RightUtils.getAppDetailSettingIntent(mActivity);
                }
                break;
            case 300:
                if (hasRight) {
                    openApp();
                } else {
                    //用户授权拒绝之后
                    RightUtils.getAppDetailSettingIntent(mActivity);
                }
                break;

            case 400:

                if (hasRight) {
                    FileUtils.creatImgFromAsset(getApplicationContext(),"qrCode.png");
                    FileUtils.creatImgFromAsset(getApplicationContext(),"logo.png");
                } else {
                    //用户授权拒绝之后
                    RightUtils.getAppDetailSettingIntent(mActivity);
                }


                break;
            default:
                if (hasRight) {

                } else {
                    //用户授权拒绝之后，友情提示一下就可以了
                    RightUtils.getAppDetailSettingIntent(mActivity);
                }
                break;

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 2001:
                if (AndPermission.hasPermission(mActivity, Manifest.permission.READ_PHONE_STATE)) {
                    openApp();
                }else {
                    AndPermission.defaultSettingDialog(mActivity, 2001).show();
                }

                break;
            case 2002:
                if (AndPermission.hasPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    FileUtils.creatImgFromAsset(getApplicationContext(),"qrCode.png");
                    FileUtils.creatImgFromAsset(getApplicationContext(),"logo.png");
                }else {
                    AndPermission.defaultSettingDialog(mActivity, 2002).show();
                }
                break;
            case 3001:
                if (AndPermission.hasPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    initLocation();
                }else {
                    AndPermission.defaultSettingDialog(mActivity, 3001).show();
                }

                break;
        }
    }
}
