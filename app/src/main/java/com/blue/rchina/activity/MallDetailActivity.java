package com.blue.rchina.activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.Goods;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.manager.xUtilsManager;
import com.blue.rchina.utils.PhoneUtils;
import com.blue.rchina.utils.RightUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


public class MallDetailActivity extends BaseActivity {

    private Goods data;
    @ViewInject(R.id.mall_detail_rec)
    RecyclerView rec;
    @ViewInject(R.id.pay_goods)
    TextView pay;
    @ViewInject(R.id.add_goods)
    TextView add;
    @ViewInject(R.id.kefu)
    View kefu;

    private List<DataWrap> infos;
    private RecyclerView.Adapter<Holder> adapter;
    public static final String qqNum = "1781293168";

    public static final String phone = "4006306658";

    //public DbManager db = x.getDb(BaseApplication.daoConfig);

    @Override
    public void initData() {
        super.initData();
        infos = new ArrayList<>();

        String dataId = getIntent().getStringExtra("dataId");
        if (!TextUtils.isEmpty(dataId)) {
            isHideLoading(false);
            loadData(dataId);
        } else {
            data = ((Goods) getIntent().getSerializableExtra("data"));

            if (data != null) {
                DataWrap first = new DataWrap();
                first.setType(1);
                List<String> firstData = new ArrayList<>();
                firstData.add(data.getPicsrc());

                first.setData(firstData);
                infos.add(first);


                addDes();
            }

        }
    }

    private void addDes() {
        DataWrap second = new DataWrap();
        second.setType(2);
        infos.add(second);

        DataWrap third = new DataWrap();
        third.setType(3);
        third.setData("产品规格");
        infos.add(third);

        DataWrap fourth = new DataWrap();
        fourth.setType(3);
        fourth.setData("产品介绍");
        infos.add(fourth);

        DataWrap fifth = new DataWrap();
        fifth.setType(3);
        fifth.setData("配送时间");
        infos.add(fifth);

        DataWrap sixth = new DataWrap();
        sixth.setType(4);
        sixth.setData("商品详情");
        infos.add(sixth);
    }

    private void loadData(String dataId) {
        RequestParams entity = new RequestParams(UrlUtils.N_getGoodsInfoById);
        entity.addBodyParameter("dataId", dataId);
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult() == 200) {
                    Goods goods = JSON.parseObject(netData.getInfo(), Goods.class);

                    data = goods;

                    if (data != null) {
                        DataWrap first = new DataWrap();
                        first.setType(1);
                        List<String> firstData = new ArrayList<>();
                        firstData.add(goods.getPicsrc());

                        first.setData(firstData);
                        infos.add(0, first);

                        addDes();

                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("服务器错误");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isHideLoading(true);
            }
        });
    }

    @Override
    public void initView() {
        super.initView();
        //initTop(R.mipmap.arrow_left,"商品详情",R.mipmap.qq_white);


            /*社区超市跳转不去显示添加购物车*/
        if (getIntent().getBooleanExtra("isFromMall", false)) {
            add.setVisibility(View.GONE);
        }
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.isLogin()) {

                    if (data.getStockCount() > 0) {
                        payGoods();

                    } else {
                        UIUtils.showToast("当前库存量为0");
                    }
                } else {
                    UserManager.toLogin();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.isLogin()) {

                    if (data.getStockCount() > 0) {
                        addGoods();
                    } else {
                        UIUtils.showToast("当前库存量为0");
                    }

                } else {
                    UserManager.toLogin();
                }
            }
        });

        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(mActivity);

                View view = null;
                switch (viewType) {
                    case 1:
                        view = inflater.inflate(R.layout.mall_detail_first, parent, false);
                        break;
                    case 2:
                        view = inflater.inflate(R.layout.mall_detail_second, parent, false);
                        break;
                    case 3:
                        view = inflater.inflate(R.layout.mall_detail_third, parent, false);
                        break;
                    case 4:
                        view = inflater.inflate(R.layout.mall_detail_fourth, parent, false);
                        break;
                }

                return new Holder(view, viewType);
            }

            @Override
            public void onBindViewHolder(final Holder holder, int position) {
                final DataWrap dataWrap = infos.get(position);

                switch (dataWrap.getType()) {
                    case 0:

                        break;
                    case 1:

                        final List<ImageView> views = new ArrayList<>();
                        final List<String> first = (List<String>) dataWrap.getData();

                        for (int i = 0; i < first.size(); i++) {
                            ImageView imageView = new ImageView(mActivity);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                imageView.setTransitionName(NewsKindImageActivity.TRA_NAME);
                            }
                            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            views.add(imageView);
                        }
                        holder.text.setText(1 + "/" + first.size());

                        holder.pager.setAdapter(new PagerAdapter() {
                            @Override
                            public int getCount() {

                                if (first != null) {
                                    return first.size();
                                }
                                return 0;
                            }

                            @Override
                            public boolean isViewFromObject(View view, Object object) {
                                return view == object;
                            }

                            @Override
                            public Object instantiateItem(ViewGroup container, int position) {
                                String path = first.get(position);
                                final ImageView child = views.get(position);

                                child.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(mActivity, NewsKindImageActivity.class);
                                        intent.putExtra("data", data);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, child, NewsKindImageActivity.TRA_NAME);
                                            startActivity(intent, options.toBundle());
                                        } else {
                                            startActivity(intent);
                                        }
                                    }
                                });
                                x.image().bind(child, path, xUtilsManager.getRectangleImageOption());

                                container.addView(child);

                                return views.get(position);
                            }

                            @Override
                            public void destroyItem(ViewGroup container, int position, Object object) {
                                container.removeView(views.get(position));
                            }

                        });
                        holder.pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                holder.text.setText(position + 1 + "/" + first.size());
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });

                        break;

                    case 2:
                        holder.text.setText(data.getTitle());
                        holder.price.setText("价格：¥" + data.getPrice());

                        if (data.getIsFree()==1){
                            holder.freight.setText("(免运费)");
                        }else {
                            holder.freight.setText(String.format("(运费¥%.2f)",data.getFreight()));
                        }
                        break;
                    case 3:

                        String titleStr = (String) dataWrap.getData();
                        holder.title.setText(titleStr);

                        if (position == 2) {
                            holder.text.setText(data.getParams());
                        } else if (position == 3) {
                            holder.text.setText(data.getDesc());
                        } else if (position == 4) {
                            if (TextUtils.isEmpty(data.getDCTime())) {
                                holder.text.setText("具体时间请联系客服");
                            } else {
                                holder.text.setText(data.getDCTime());
                            }
                        } else if (position == 5) {
                            if (TextUtils.isEmpty(data.getAttention())) {
                                holder.text.setText("具体注意事项请联系客服");
                            } else {
                                holder.text.setText("" + data.getAttention());
                            }
                        }


                        break;
                    case 4:
                        holder.detail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(mActivity,WebGoodsDetailActivity.class);
                                intent.putExtra("goods",data);
                                startActivity(intent);
                            }
                        });
                        break;
                }
            }

            @Override
            public int getItemCount() {
                if (infos != null) {
                    return infos.size();
                }
                return 0;
            }

            @Override
            public int getItemViewType(int position) {
                return infos.get(position).getType();
            }
        };
        rec.setAdapter(adapter);
    }

    private void addGoods() {


        final ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setMessage("加载中...");

        dialog.show();

        RequestParams entity = new RequestParams(UrlUtils.N_joinShopCart);

        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        entity.addBodyParameter("dataId", data.getGoodsId() + "");
        entity.addBodyParameter("num", "1");

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                NetData netData = JSON.parseObject(result, NetData.class);

                Integer shopcarNum = JSON.parseObject(result).getInteger("shopcarNum");

                if (netData.getResult() == 200) {
                    Intent intent = new Intent();
                    intent.setAction("action_change_mall");
                    intent.putExtra("count", shopcarNum);
                    sendBroadcast(intent);

                    UIUtils.showToast("成功添加到购物车");

                } else {
                    UIUtils.showToast("添加失败");
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


                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        });
    }

    private void payGoods() {


        Intent intent=new Intent(mActivity,DirectBuyActivity.class);
        intent.putExtra("goods", (Serializable) data);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_detail);

        x.view().inject(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

            /*getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);*/
    }


    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void btn_back(View view) {
        finish();
    }

    public void btn_qq(View view) {
        if (checkApkExist(this, "com.tencent.mobileqq")) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum + "&version=1")));
        } else {
            Toast.makeText(this, "本机未安装QQ应用", Toast.LENGTH_SHORT).show();
        }
    }

    public void btn_share(View view) {
        showShare(data.getShareLink());
    }

    private void showShare(String path) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(data.getTitle());
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(path);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(data.getDesc());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath(FileUtils.APP_LOGO);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(path);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("融城中国");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(path);

        oks.setImageUrl(data.getPicsrc());

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {

                if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
                    //微信必须要设置具体的方分享类型
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                } else if (platform.getName().equals(WechatMoments.NAME)) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                    paramsToShare.setTitle(data.getDesc());
                }

            }
        });
        // 启动分享GUI
        oks.show(this);
    }

    public void btn_kefu(View view) {
        //btn_qq(view);
        //PhoneUtils.callPhone(this, phone);
        if (AndPermission.hasPermission(mActivity, Manifest.permission.CALL_PHONE)) {
            PhoneUtils.callPhone(this, phone);
        } else {
            AndPermission.with(this)
                    .requestCode(300)
                    .permission(Manifest.permission.CALL_PHONE)
                    .rationale(new RationaleListener() {
                        @Override
                        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                            AndPermission.defaultSettingDialog(mActivity, 3001).show();
                        }
                    }).callback(new PermissionListener() {
                @Override
                public void onSucceed(int requestCode, List<String> grantedPermissions) {
                    // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
                    PhoneUtils.callPhone(MallDetailActivity.this, phone);
                }

                @Override
                public void onFailed(int requestCode, List<String> deniedPermissions) {
                    //用户授权拒绝之后
                    RightUtils.getAppDetailSettingIntent(mActivity);
                }
            }).start();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 3001:
                if (AndPermission.hasPermission(mActivity, Manifest.permission.CALL_PHONE)) {
                    PhoneUtils.callPhone(this, phone);
                } else {
                    //AndPermission.defaultSettingDialog(mActivity, 3001).show();
                    RightUtils.getAppDetailSettingIntent(mActivity);
                }

                break;
        }

    }

    class Holder extends RecyclerView.ViewHolder {
        View parent;
        ViewPager pager;
        TextView text, price, title,freight,detail;

        public Holder(View itemView, int type) {
            super(itemView);

            parent = itemView;

            switch (type) {
                case 0:

                    break;
                case 1:
                    text = ((TextView) itemView.findViewById(R.id.mall_text));
                    pager = ((ViewPager) itemView.findViewById(R.id.mall_pager));
                    break;
                case 2:
                    text = ((TextView) itemView.findViewById(R.id.mall_text));
                    price = ((TextView) itemView.findViewById(R.id.mall_price));
                    freight= (TextView) itemView.findViewById(R.id.mall_freight);
                    break;
                case 3:
                    text = ((TextView) itemView.findViewById(R.id.mall_text));
                    title = ((TextView) itemView.findViewById(R.id.mall_title));
                    break;
                case 4:
                    detail= (TextView) itemView.findViewById(R.id.detail);
                    break;
            }
        }
    }
}
